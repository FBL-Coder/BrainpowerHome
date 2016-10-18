package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.SceneAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.widget.CustomDialog;

/**
 * Created by Say GoBay on 2016/8/23.
 */
public class SceneActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageView iv_cancel;
    private EditText editText;
    private Button sure, cancel;
    private TextView mTitle;
    private ScrollView sv;
    private ListView lv;
    private SceneAdapter sceneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_scene_listview);
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
        lv = (ListView) findViewById(R.id.scene_lv);
        sv = (ScrollView) findViewById(R.id.scene_sv);
        sv.smoothScrollTo(0, 0);
        if (sceneAdapter != null){
            sceneAdapter.notifyDataSetChanged();
        }else{
            sceneAdapter = new SceneAdapter(this);
            lv.setAdapter(sceneAdapter);
        }
        lv.setOnItemClickListener(this);
    }

    /**
     * 初始化自定义dialog
     */
    CustomDialog dialog;

    public void getDialog() {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_scene);
        dialog.show();
        iv_cancel = (ImageView) dialog.findViewById(R.id.scene_iv_cancel);
        editText = (EditText) dialog.findViewById(R.id.scene_et_name);
        sure = (Button) dialog.findViewById(R.id.scene_btn_sure);
        cancel = (Button) dialog.findViewById(R.id.scene_btn_cancel);
        iv_cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int listSize = MyApplication.getWareData().getSceneEvents().size();

        if (listSize > 0) {
            if (position < listSize) {
                Intent intent = new Intent(SceneActivity.this, SceneSetActivity.class);
                intent.putExtra("title", MyApplication.getWareData().getSceneEvents().get(position).getSceneName());
                startActivity(intent);
            } else {
                getDialog();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scene_iv_cancel:
                dialog.dismiss();
                break;
            case R.id.scene_btn_sure:
                break;
            case R.id.scene_btn_cancel:
                dialog.dismiss();
                break;

        }
    }
}
