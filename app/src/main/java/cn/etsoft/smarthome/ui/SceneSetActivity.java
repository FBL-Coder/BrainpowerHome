package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.RoomAdapter;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class SceneSetActivity extends Activity implements AdapterView.OnItemClickListener {

    private ScrollView sv;
    private ListView lv;
    private TextView mTitle;
    private RoomAdapter roomAdapter;
    private List<WareDev> mWareDev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sceneset_listview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化标题栏
        initTitleBar();
        //初始化ListView
        initListView();

        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                initListView();
                super.handleMessage(msg);
            }
        };
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData() {
                Message message = mHandler.obtainMessage();
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText(getIntent().getStringExtra("title"));
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        lv = (ListView) findViewById(R.id.sceneset_lv);
        sv = (ScrollView) findViewById(R.id.sceneset_sv);
        sv.smoothScrollTo(0, 0);

        mWareDev = new ArrayList<>();
        for(int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            mWareDev.add(MyApplication.getWareData().getDevs().get(i));
        }
        for (int i = 0; i < mWareDev.size() - 1; i++) {
            for (int j = mWareDev.size() - 1; j > i; j--) {
                if (mWareDev.get(i).getRoomName().equals(mWareDev.get(j).getRoomName())) {
                    mWareDev.remove(j);
                }
            }
        }

        if (roomAdapter != null){
            roomAdapter.notifyDataSetChanged();
        }else{
            roomAdapter = new RoomAdapter(this,mWareDev);
            lv.setAdapter(roomAdapter);
        }
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Intent intent = new Intent(SceneSetActivity.this, ParlourActivity.class);
        intent.putExtra("title", mWareDev.get(position).getRoomName());
        startActivity(intent);
    }
}
