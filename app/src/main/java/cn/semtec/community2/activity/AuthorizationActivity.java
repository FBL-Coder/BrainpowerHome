package cn.semtec.community2.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.adapter.AuthorizationAdapter;
import cn.semtec.community2.database.DBhelper;

/**
 * 动态密码开门
 */
public class AuthorizationActivity extends Activity {
    private ListView listView_pass;
    private ImageView btn_back;
    private ImageView btn_add_pass;

    private Dialog buffer;
    private ArrayList<HashMap<String, String>> mList;
    private View left, right;
    private TextView tv_num;
    private int num = 1;
    private AuthorizationAdapter adapter;
    private final int listSize = 12;
    private String url_path;
    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        setView();
        listView_pass = (ListView) findViewById(R.id.listView_pass);
        btn_add_pass = (ImageView) findViewById(R.id.btn_add_pass);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        tv_num = (TextView) findViewById(R.id.tv_num);
        mList = new ArrayList<>();
        adapter = new AuthorizationAdapter(this,mList);
        listView_pass.setAdapter(adapter);

        setListener();
    }

    private void setView() {
        listView_pass = (ListView) findViewById(R.id.listView_pass);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_add_pass = (ImageView) findViewById(R.id.btn_add_pass);
    }

    private void setListener() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num--;
                    tv_num.setText("" + num);
                    getData();
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() < listSize) {
                    return;
                }
                num++;
                tv_num.setText("" + num);
                getData();
            }
        });
        btn_add_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthorizationActivity.this,AddPassActivity.class);
                startActivity(intent);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
        private void getData() {
            int nowSize = (num-1)*12;
            int limitSize = nowSize+12;
            if (MyApplication.cellphone == null) {
                return;
            }
            Log.e("nowSize",nowSize+"  "+limitSize);
            db = MyApplication.getDB();
            cursor = db.query(DBhelper.DYNAMIC, null,null, null,
                    null, null, DBhelper.DYNAMIC_DATE + " desc",nowSize+","+limitSize);
            mList.clear();
            for (int i = 0; i < cursor.getCount() && i < 16; i++) {
                cursor.moveToPosition(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", cursor.getString(cursor.getColumnIndex(DBhelper.DYNAMIC_DATE)));
                map.put("name", cursor.getString(cursor.getColumnIndex(DBhelper.DYNAMIC_NAME)));
                map.put("_id", cursor.getString(cursor.getColumnIndex(DBhelper.DYNAMIC_ID)));
                map.put("password", cursor.getString(cursor.getColumnIndex(DBhelper.DYNAMIC_PASSWORD)));
                mList.add(map);
            }
            cursor.close();
            adapter.notifyDataSetChanged();
        }

    @Override
    protected void onResume() {
        super.onResume();
        mList.clear();
        getData();
    }
}


