package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.adapter.IClick;
import cn.etsoft.smarthome.adapter.SystemAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.widget.CustomDialog;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SwipeListView;

/**
 * Created by Say GoBay on 2016/9/2.
 */
public class SceneSetActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageView iv_cancel, back;
    private EditText name;
    private Button sure, cancel;
    private TextView title;
    private SwipeListView lv;

    private SystemAdapter systemAdapter;
    private List<WareSceneEvent> event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sceneset_listview2);
        //初始化标题栏
        initTitleBar();
        //初始化ListView
        initListView();

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //初始化ListView
                initListView();
                super.handleMessage(msg);
            }
        };
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_addSceneEvents.getValue()
                        || what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delSceneEvents.getValue()) {
                    Message message = mHandler.obtainMessage();
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        title = (TextView) findViewById(R.id.tv_home);
        back = (ImageView) findViewById(R.id.back);
        title.setText("情景设置");
        back.setOnClickListener(this);
        event = new ArrayList<>();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {

        event.clear();
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
            event.add(MyApplication.getWareData().getSceneEvents().get(i));
        }
        lv = (SwipeListView) findViewById(R.id.sceneSet_lv);
        if (systemAdapter != null) {
            systemAdapter.notifyDataSetChanged();
        } else {
            systemAdapter = new SystemAdapter(this, event, mListener);
            lv.setAdapter(systemAdapter);
        }
        lv.setOnItemClickListener(this);
    }

    /**
     * 初始化自定义dialog
     */
    CustomDialog dialog;

    public void getDialog() {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_sceneset);
        dialog.show();
        iv_cancel = (ImageView) dialog.findViewById(R.id.scene_iv_cancel);
        name = (EditText) dialog.findViewById(R.id.scene_et_name);
        sure = (Button) dialog.findViewById(R.id.scene_btn_sure);
        cancel = (Button) dialog.findViewById(R.id.scene_btn_cancel);
        iv_cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int listSize = event.size();
        if (listSize > 0) {
            if (position < listSize) {
                Intent intent = new Intent(SceneSetActivity.this, SceneSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putByte("eventId", event.get(position).getEventld());
                bundle.putString("sceneName", event.get(position).getSceneName());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                getDialog();
            }
        }else {
            ToastUtil.showToast(this,"数据异常");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scene_iv_cancel:
                dialog.dismiss();
                break;
            case R.id.scene_btn_sure:
                String data = name.getText().toString();
                if (!"".equals(data)) {
                    //新增情景模式
                    add_scene(event.size(), data);

                    initListView();
                    dialog.dismiss();
                } else {
                    ToastUtil.showToast(SceneSetActivity.this, "请填写情景名称");
                }
                break;
            case R.id.scene_btn_cancel:
                dialog.dismiss();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 新增情景模式
     *
     * @param eventID
     * @param name
     */
    private void add_scene(int eventID, String name) {
        byte[] data = {0};
        try {
            data = name.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);

        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"sceneName\":\"" + str_gb + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_addSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + eventID +
                ",\"devCnt\":" + 0 +
                ",\"itemAry\":[{" +
                "\"uid\":\"\"" +
                ",\"devType\":" + 0 +
                ",\"devID\":" + 0 +
                ",\"bOnOff\":" + 0 +
                ",\"lmVal\":0" +
                ",\"param1\":0" +
                ",\"param2\":0" +
                "}]}";
        MyApplication.sendMsg(ctlStr);
    }

    /**
     * 删除情景模式
     *
     * @param eventID
     * @param name
     */
    private void del_scene(int eventID, String name) {
        byte[] data = {0};
        try {
            data = name.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);

        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"sceneName\":\"" + str_gb + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + eventID +
                ",\"devCnt\":" + 0 +
                ",\"itemAry\":[{" +
                "\"uid\":\"\"" +
                ",\"devType\":" + 0 +
                ",\"devID\":" + 0 +
                ",\"bOnOff\":" + 0 +
                ",\"lmVal\":0" +
                ",\"param1\":0" +
                ",\"param2\":0" +
                "}]}";
        LogUtils.LOGE("情景模式测试数据:", ctlStr);
        MyApplication.sendMsg(ctlStr);
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private IClick mListener = new IClick() {
        @Override
        public void listViewItemClick(final int position, View v) {
            switch (v.getId()) {
                case R.id.deploy_delete:
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(SceneSetActivity.this);
                    builder.setTitle("提示 :");
                    builder.setMessage("您确定删除此模式?");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            int eventId = MyApplication.getWareData().getSceneEvents().get(position).getEventld();
                            String name = MyApplication.getWareData().getSceneEvents().get(position).getSceneName();
                            //删除情景模式
                            del_scene(eventId, name);
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };
}
