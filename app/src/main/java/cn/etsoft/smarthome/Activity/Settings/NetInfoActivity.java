package cn.etsoft.smarthome.Activity.Settings;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;

import cn.etsoft.smarthome.Adapter.ListView.NetWork_Adapter;
import cn.etsoft.smarthome.Adapter.ListView.SeekListAdapter;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Net_AddorDel_Helper;

/**
 * Author：FBL  Time： 2017/7/24.
 * 模块详情
 */

public class NetInfoActivity extends BaseActivity {

    private TextView mSeekNetBack;
    private TextView net_ID, net_Pass;
    private TextView name, IP, Ip_mask, GetWay, Server;
    private ImageView NetWork_EditName;
    private int FLAG = 0;
    private int position = 0;
    private RcuInfo info;

    @Override
    public void initView() {
        setLayout(R.layout.activity_netinfo);
        mSeekNetBack = getViewById(R.id.SeekNet_Back);

        net_ID = (TextView) findViewById(R.id.NetWork_ID);
        net_Pass = (TextView) findViewById(R.id.NetWork_Pass);
        name = (TextView) findViewById(R.id.NetWork_Name);
        IP = (TextView) findViewById(R.id.NetWork_Ip);
        Ip_mask = (TextView) findViewById(R.id.NetWork_Ip_Mask);
        GetWay = (TextView) findViewById(R.id.NetWork_GetWay);
        Server = (TextView) findViewById(R.id.NetWork_Server);
        NetWork_EditName = (ImageView) findViewById(R.id.NetWork_EditName);

    }

    @Override
    public void initData() {
        FLAG = getIntent().getBundleExtra("BUNDLE").getInt("FLAG", 0);
        position = getIntent().getBundleExtra("BUNDLE").getInt("POSITION", 0);
        mSeekNetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (FLAG == NetWork_Adapter.LOGIN) {
            info = MyApplication.mApplication.getRcuInfoList().get(position);
            name.setText(info.getCanCpuName());
        } else {
            info = MyApplication.mApplication.getSeekRcuInfos().get(position);
            name.setText(info.getName());
        }
        net_ID.setText(info.getDevUnitID());
        net_Pass.setText(info.getDevUnitPass());
        IP.setText(info.getIpAddr());
        Ip_mask.setText(info.getSubMask());
        GetWay.setText(info.getGateWay());
        Server.setText(info.getCenterServ());
        if (FLAG == NetWork_Adapter.LOGIN)
            NetWork_EditName.setVisibility(View.VISIBLE);
            NetWork_EditName.setOnClickListener(new View.OnClickListener() {
                private TextView mDialogAddSceneOk;
                private TextView mDialogAddSceneCancle;
                private EditText mDialogAddSceneName;
                private TextView mTitleName;
                private TextView mTitle;
                MyHandler handler = new MyHandler();
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(NetInfoActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                    dialog.setContentView(R.layout.dialog_addscene);
                    dialog.show();
                    mDialogAddSceneName = (EditText) dialog.findViewById(R.id.dialog_addScene_name);
                    mDialogAddSceneCancle = (TextView) dialog.findViewById(R.id.dialog_addScene_cancle);
                    mDialogAddSceneOk = (TextView) dialog.findViewById(R.id.dialog_addScene_ok);
                    mTitleName = (TextView) dialog.findViewById(R.id.title_name);
                    mTitle = (TextView) dialog.findViewById(R.id.title);
                    mTitle.setText("修改模块名称");
                    mTitleName.setText("模块名称 :");
                    mDialogAddSceneOk.setText("确定");
                    mDialogAddSceneCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    mDialogAddSceneOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Net_AddorDel_Helper.editNew(handler,MyApplication.mApplication.getRcuInfoList(),
                                    position, NetInfoActivity.this, mDialogAddSceneName,
                                    MyApplication.mApplication.getRcuInfoList().get(position).getDevUnitID(),
                                    MyApplication.mApplication.getRcuInfoList().get(position).getDevUnitPass());
                        }
                    });
                }
            });
    }
   class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initData();
        }
    }
}
