package cn.etsoft.smarthome.Activity.Settings;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.etsoft.smarthome.Activity.HomeActivity;
import cn.etsoft.smarthome.Adapter.ListView.NetWork_Adapter;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.New_AddorDel_Helper;

/**
 * Author：FBL  Time： 2017/7/10.
 * 联网模块设置界面
 */

public class NewWorkSetActivity extends BaseActivity {
    private TextView mNetmoduleAdd,mNewWorksetlogout;
    private ListView mNetmoduleListview;
    private TextView mDialogCancle, mDialogOk;
    private EditText mDialogName, mDialogID, mDialogPass;
    private NewModuleHandler mNewModuleHandler = new NewModuleHandler(this);
    private Gson gson = new Gson();
    private NetWork_Adapter mAdapter;

    @Override
    public void initView() {

        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleImageBtn(true, R.drawable.back_image_select, false, 0);

        setLayout(R.layout.activity_set_network);

        mNetmoduleListview = getViewById(R.id.NewWork_set_netmodule_listview);
        mNetmoduleAdd = getViewById(R.id.NewWork_set_netmodule_add);
        mNewWorksetlogout = getViewById(R.id.NewWork_set_logout);
    }


    private void initLIstview() {
        mAdapter = new NetWork_Adapter(this);
        mNetmoduleListview.setAdapter(mAdapter);

    }

    @Override
    public void initData() {
        initLIstview();
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNetmoduleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(NewWorkSetActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                dialog.setContentView(R.layout.dialog_addnetmodule);
                dialog.setTitle("添加联网模块");
                dialog.show();
                initAddNetModuleDialog(dialog);
            }
        });

        mNetmoduleListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backEvent();
            }
        });

        mNewWorksetlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO  退出登录
            }
        });
    }

    private void initAddNetModuleDialog(final Dialog dialog) {
        mDialogName = (EditText) dialog.findViewById(R.id.dialog_addnetmodule_name);
        mDialogID = (EditText) dialog.findViewById(R.id.dialog_addnetmodule_id);
        mDialogPass = (EditText) dialog.findViewById(R.id.dialog_addnetmodule_pass);
        mDialogCancle = (TextView) dialog.findViewById(R.id.dialog_addnetmodule_cancle);
        mDialogOk = (TextView) dialog.findViewById(R.id.dialog_addnetmodule_ok);
        mDialogCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                New_AddorDel_Helper.addNew(mNewModuleHandler, NewWorkSetActivity.this, mDialogName, mDialogID, mDialogPass);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backEvent();
        super.onBackPressed();

    }

    private void backEvent() {
        if (!"".equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "")))
            startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
        finish();
    }

    static class NewModuleHandler extends Handler {

        WeakReference<NewWorkSetActivity> weakReference;

        public NewModuleHandler(NewWorkSetActivity fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null) {
                if (msg.what == New_AddorDel_Helper.ADDNEWMODULE_OK) {
                    // 添加成功
                    RcuInfo info = new RcuInfo();
                    info.setDevUnitID(weakReference.get().mDialogID.getText().toString());
                    info.setCanCpuName(weakReference.get().mDialogName.getText().toString());
                    info.setDevUnitPass(weakReference.get().mDialogPass.getText().toString());

                    List<RcuInfo> json_rcuinfolist = MyApplication.mApplication.getRcuInfoList();
                    json_rcuinfolist.add(info);
                    AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, weakReference.get().gson.toJson(json_rcuinfolist));
                    weakReference.get().initLIstview();
                    ToastUtil.showText("Handler返回 添加成功");
                } else if (msg.what == New_AddorDel_Helper.DELNEWMODULE_OK) {
                    //TODO 删除成功
                } else if (msg.what == New_AddorDel_Helper.EDITNEWMODULE_OK) {
                    //TODO 修改成功
                }
            }
        }
    }
}
