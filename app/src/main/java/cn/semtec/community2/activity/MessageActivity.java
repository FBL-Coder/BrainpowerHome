package cn.semtec.community2.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.adapter.MessageActivityAdapter;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.view.PullToRefreshListView;

public class MessageActivity extends MyBaseActivity implements View.OnClickListener {

    private TextView tv_name;
    private ImageView btn_back;
    private TextView btn_manage;
    private PullToRefreshListView listView;
    private Cursor c;
    public static String comefrom;
    //记录所有消息数据对象
    private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
    //记录 消息数据对应的date
    private ArrayList<String> dateList = new ArrayList<String>();
    private SQLiteDatabase db;
    //限制list 的长度  item的数量
    private int mCount = 5;
    //刷新增加的数量
    private int addNum = 5;
    private MessageActivityAdapter adapter;
    public static boolean isManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initData();
        setView();
        setListener();
        adapter = new MessageActivityAdapter(this, mList);
        listView.setAdapter(adapter);
        //下拉刷新 监听
        listView.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            CatchUtil.catchM(e);
                        }
                        mCount = mList.size() + addNum;
                        initData();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        adapter.pubTime = null;
                        adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });
    }

    private void initData() {
        comefrom = getIntent().getExtras().getString("comefrom");
        db = MyApplication.getDB();
        c = db.query(DBhelper.MESSAGE, null, DBhelper.MESSAGE_FROM + "=? and " + DBhelper.MESSAGE_ACCOUNT + "=?", new String[]{comefrom, MyApplication.cellphone},
                null, null, DBhelper.MESSAGE_DATE + " asc");
        mList.clear();
        dateList.clear();

        if (c.getCount() == 0) {
            return;
        }
        c.moveToLast();
        int isRead = c.getInt(c.getColumnIndex(DBhelper.MESSAGE_ISREAD));

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToPosition(i);
            if (c.getInt(c.getColumnIndex(DBhelper.MESSAGE_ISREAD)) == isRead) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("date", c.getLong(c.getColumnIndex(DBhelper.MESSAGE_DATE)));
                map.put("content", c.getString(c.getColumnIndex(DBhelper.MESSAGE_CONTENT)));
                map.put("isread", c.getInt(c.getColumnIndex(DBhelper.MESSAGE_ISREAD)));
                map.put("type", c.getInt(c.getColumnIndex(DBhelper.MESSAGE_TYPE)));
                map.put("url", c.getString(c.getColumnIndex(DBhelper.MESSAGE_URL)));
                if (isRead == 1) {
                    if (mList.size() >= mCount) {
                        mList.remove(0);
                        dateList.remove(0);
                    }
                }
                mList.add(map);
                dateList.add(String.valueOf(c.getLong(c.getColumnIndex(DBhelper.MESSAGE_DATE))));
            }
        }
        c.close();
        if (isRead == 0) {
            //修改数据库
            ContentValues cv = new ContentValues();
            cv.put(DBhelper.MESSAGE_ISREAD, 1);
            db.update(DBhelper.MESSAGE, cv, DBhelper.MESSAGE_FROM + "=? and " + DBhelper.MESSAGE_ACCOUNT + "=?",
                    new String[]{comefrom, MyApplication.cellphone});
        }

    }

    private void setView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_manage = (TextView) findViewById(R.id.btn_manage);
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        tv_name.setText(comefrom);
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_manage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (isManage) {
                    return;
                } else {
                    finish();
                }
                break;
            case R.id.btn_manage:
                adapter.pubTime = null;
                if (isManage) {
                    if (adapter.selectList.size() > 0) {
                        for (int i = 0; i < adapter.selectList.size(); i++) {
                            String date = adapter.selectList.get(i);
                            db.delete(DBhelper.MESSAGE, DBhelper.MESSAGE_DATE + "=?", new String[]{date});

                            if (dateList.contains(adapter.selectList.get(i))) {
                                int p = dateList.indexOf(adapter.selectList.get(i));
                                dateList.remove(p);
                                mList.remove(p);
                            }
                        }
                    }
                    isManage = false;
                    btn_manage.setText(getString(R.string.compile));
                } else {
                    isManage = true;
                    btn_manage.setText(getString(R.string.delete));
                }
                adapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isManage) {
            isManage = false;
            btn_manage.setText(getString(R.string.compile));
            adapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        isManage = false;
        db.close();
        super.onDestroy();
    }
}
