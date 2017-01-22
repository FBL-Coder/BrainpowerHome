package cn.semtec.community2.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.adapter.MessageListAdapter;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.view.SliderListView;

public class MessageListActivity extends MyBaseActivity implements AdapterView.OnItemClickListener {
    private SliderListView listView;
    //数据集
    private ArrayList<HashMap<String, Object>> mList;
    private SQLiteDatabase db;
    //保证mList 的comefrom唯一
    public ArrayList<String> fromList;
    public MessageListAdapter adapter;
    public static MessageListActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        instance = this;

        fromList = new ArrayList<String>();// 存放发送方名称 用于判断是否存在
        mList = new ArrayList<HashMap<String, Object>>();// 存放发送方数据

        listView = (SliderListView) findViewById(R.id.listView);
        adapter = new MessageListAdapter(this, mList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        initData();
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String comefrom = (String) mList.get(position).get("comefrom");
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("comefrom", comefrom);
        startActivity(intent);
    }

    public void initData() {
        if(MyApplication.cellphone ==null){
            return;
        }
        db = MyApplication.getDB();
        Cursor mCursor = db.query(DBhelper.MESSAGE, null, DBhelper.MESSAGE_ACCOUNT + "=?", new String[]{MyApplication.cellphone}, null, null, DBhelper.MESSAGE_DATE + " desc");
        //JReceiber 更改数据的原因  list放到外面  在这clear；
        fromList.clear();
        mList.clear();
        for (int i = 0; i < mCursor.getCount(); i++) {
            mCursor.moveToPosition(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            String comefrom = mCursor.getString(mCursor.getColumnIndex(DBhelper.MESSAGE_FROM)); // 发送方
            if (!fromList.contains(comefrom)) {
                fromList.add(comefrom);
                map.put("comefrom", comefrom);
                map.put("time", mCursor.getLong(mCursor.getColumnIndex(DBhelper.MESSAGE_DATE)));
                map.put("body", mCursor.getString(mCursor.getColumnIndex(DBhelper.MESSAGE_CONTENT)));
                //isread为0时  未读count+1；
                map.put("newsCount", (mCursor.getInt(mCursor.getColumnIndex(DBhelper.MESSAGE_ISREAD)) == 0) ? 1 : 0);
                mList.add(map);
            } else {
                if (mCursor.getInt(mCursor.getColumnIndex(DBhelper.MESSAGE_ISREAD)) == 0) {
                    int index = fromList.indexOf(comefrom);
                    mList.get(index).put("newsCount", (Integer) mList.get(index).get("newsCount") + 1);
                }
            }
        }
        mCursor.close();
    }

    @Override
    public void onDestroy() {
        instance = null;
        super.onDestroy();
    }
}
