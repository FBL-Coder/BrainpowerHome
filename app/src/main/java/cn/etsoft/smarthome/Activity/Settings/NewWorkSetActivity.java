package cn.etsoft.smarthome.Activity.Settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import cn.etsoft.smarthome.Domain.SearchNet;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Net_AddorDel_Helper;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/7/10.
 * 联网模块设置界面
 */

public class NewWorkSetActivity extends BaseActivity {
    private TextView mNetmoduleAdd;
    private ListView mNetmoduleListview;
    private TextView mDialogCancle, mDialogOk, mSousuo;
    private EditText mDialogName, mDialogID, mDialogPass;
    private NewModuleHandler mNewModuleHandler = new NewModuleHandler(this);
    private Gson gson = new Gson();
    private NetWork_Adapter mAdapter;
    private int mDeleteNet_Position = -1;

    @Override
    public void initView() {

        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleImageBtn(true, R.drawable.back_image_select, false, 0);

        setLayout(R.layout.activity_set_network);

        mNetmoduleListview = getViewById(R.id.NewWork_set_netmodule_listview);
        mNetmoduleAdd = getViewById(R.id.NewWork_set_netmodule_add);
        mSousuo = getViewById(R.id.NewWork_set_netmodule_Sousuo);
    }


    private void initLIstview() {
        if (mAdapter == null)
            mAdapter = new NetWork_Adapter(this);
        else mAdapter.notifyDataSetChanged();
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (GlobalVars.getDevid().equals(MyApplication.mApplication.getRcuInfoList().get(position).getDevUnitID()))
                    ToastUtil.showText("联网模块正在使用中！");
                else {
                    android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(NewWorkSetActivity.this);
                    dialog.setTitle("提示 :");
                    dialog.setMessage("您是否要使用此联网模块？");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE,
                                    MyApplication.mApplication.getRcuInfoList().get(position).getDevUnitID());
                            MyApplication.setNewWareData();
                            SendDataUtil.getNetWorkInfo();
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
                            finish();
                        }
                    });
                    dialog.create().show();
                }
            }
        });
        mNetmoduleListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NewWorkSetActivity.this);
                builder.setTitle("删除");
                builder.setMessage("您是否要删除联网模块？");
                builder.setNegativeButton("不要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mDeleteNet_Position = position;
                        MyApplication.mApplication.showLoadDialog(NewWorkSetActivity.this);
                        Net_AddorDel_Helper.deleteNew(mNewModuleHandler,
                                NewWorkSetActivity.this,
                                MyApplication.mApplication.getRcuInfoList().get(position).getDevUnitID());
                    }
                });
                builder.create().show();
                return true;
            }
        });
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backEvent();
            }
        });


        mSousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NewWorkSetActivity.this, SeekActivity.class), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int position = data.getIntExtra("yes", -1);
            if (position != -1) {
                if (position == -5) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    SearchNet net = MyApplication.getWareData().getSeekNets().get(position);
                    Log.i("SeekNet", "onActivityResult: " + net.getRcu_rows().get(0).getName() + "--"
                            + net.getRcu_rows().get(0).getCanCpuID() + "--" + net.getRcu_rows().get(0).getDevUnitPass());
                    Net_AddorDel_Helper.addNew(mNewModuleHandler, NewWorkSetActivity.this,
                            net.getRcu_rows().get(0).getName(), net.getRcu_rows().get(0).getCanCpuID(),
                            net.getRcu_rows().get(0).getDevUnitPass());
                }
            }
        }
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
                Net_AddorDel_Helper.addNew(mNewModuleHandler,
                        NewWorkSetActivity.this, mDialogName.getText().toString(),
                        mDialogID.getText().toString(), mDialogPass.getText().toString());
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

    public static class NewModuleHandler extends Handler {

        WeakReference<NewWorkSetActivity> weakReference;

        public NewModuleHandler(NewWorkSetActivity fragment) {
            weakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null) {
                if (msg.what == Net_AddorDel_Helper.ADDNEWMODULE_OK) {
                    // 添加成功
                    RcuInfo info = new RcuInfo();
                    info.setDevUnitID(weakReference.get().mDialogID.getText().toString());
                    info.setCanCpuName(weakReference.get().mDialogName.getText().toString());
                    info.setDevUnitPass(weakReference.get().mDialogPass.getText().toString());

                    List<RcuInfo> json_rcuinfolist = MyApplication.mApplication.getRcuInfoList();
                    json_rcuinfolist.add(info);
                    AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, weakReference.get().gson.toJson(json_rcuinfolist));
                    weakReference.get().initLIstview();
                    ToastUtil.showText("添加成功");
                } else if (msg.what == Net_AddorDel_Helper.DELNEWMODULE_OK) {
                    MyApplication.mApplication.dismissLoadDialog();
                    List<RcuInfo> list = MyApplication.mApplication.getRcuInfoList();
                    list.remove(weakReference.get().mDeleteNet_Position);
                    MyApplication.mApplication.setRcuInfoList(list);
                    weakReference.get().initLIstview();
                    ToastUtil.showText("删除成功");
                    //删除成功
                } else if (msg.what == Net_AddorDel_Helper.EDITNEWMODULE_OK) {
                    //修改成功
                    MyApplication.mApplication.dismissLoadDialog();
                }
            }
        }
    }
}
