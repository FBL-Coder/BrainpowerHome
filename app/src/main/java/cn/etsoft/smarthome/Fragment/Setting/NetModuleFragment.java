package cn.etsoft.smarthome.Fragment.Setting;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.ListView.NewModuleFragment_Adapter;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.New_AddorDel_Helper;

/**
 * Author：FBL  Time： 2017/6/22.
 */

public class NetModuleFragment extends BaseFragment {

    private TextView mNetmoduleAdd;
    private ListView mNetmoduleListview;
    private TextView mDialogCancle, mDialogOk;
    private EditText mDialogName, mDialogID, mDialogPass;
    private NewModuleHandler mNewModuleHandler = new NewModuleHandler(this);
    private Gson gson = new Gson();
    private NewModuleFragment_Adapter mAdapter;


    @Override
    public void initData(Bundle arguments) {
        initLIstview();
    }

    private void initLIstview() {
        mAdapter = new NewModuleFragment_Adapter(mActivity);
        mNetmoduleListview.setAdapter(mAdapter);

    }

    @Override
    protected void initView() {
        mNetmoduleListview = findViewById(R.id.fragment_set_netmodule_listview);
        mNetmoduleAdd = findViewById(R.id.fragment_set_netmodule_add);
    }

    @Override
    protected void setListener() {
        mNetmoduleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(mActivity, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
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
                New_AddorDel_Helper.addNew(mNewModuleHandler, mActivity, mDialogName, mDialogID, mDialogPass);
            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_set_netmoudle;
    }


    static class NewModuleHandler extends Handler {

        WeakReference<NetModuleFragment> weakReference;

        public NewModuleHandler(NetModuleFragment fragment) {
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
