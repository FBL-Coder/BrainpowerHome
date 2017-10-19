package cn.etsoft.smarthome.Activity.Settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.etsoft.smarthome.Activity.HomeActivity;
import cn.etsoft.smarthome.Adapter.ListView.NetWork_Adapter;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.Domain.SearchNet;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.HTTPRequest_BackCode;
import cn.etsoft.smarthome.UiHelper.Net_AddorDel_Helper;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import cn.etsoft.smarthome.Utils.SendDataUtil;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/7/10.
 * 联网模块设置界面
 */

public class NewWorkSetActivity extends BaseActivity {
    private TextView mNetmoduleAdd, mTitleName, mDialogAddSceneName,
            mDialogAddSceneCancle, mDialogAddSceneOk, mTitle;
    private ListView mNetmoduleListview, mNewWorksousuolistview;
    private TextView mDialogCancle, mDialogOk, mSousuo;
    private LinearLayout add_ref_LL;
    private EditText mDialogName, mDialogID, mDialogPass;
    private NewModuleHandler mNewModuleHandler = new NewModuleHandler(this);
    private Gson gson = new Gson();
    private NetWork_Adapter mAdapter, mSeekAdapter;
    private int mDeleteNet_Position = -1;

    @Override
    public void initView() {

        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleImageBtn(true, R.drawable.back_image_select, true, R.drawable.refrush_1);
        setLayout(R.layout.activity_set_network);
        mNetmoduleListview = getViewById(R.id.NewWork_set_netmodule_listview);
        mNewWorksousuolistview = getViewById(R.id.NewWork_set_sousuo_listview);
        mNetmoduleAdd = getViewById(R.id.NewWork_set_netmodule_add);
        add_ref_LL = getViewById(R.id.add_ref_LL);
        mSousuo = getViewById(R.id.NewWork_set_netmodule_Sousuo);
        mTitle = (TextView) findViewById(R.id.title);
        mTitleName = (TextView) findViewById(R.id.title_name);
        mDialogAddSceneName = (EditText) findViewById(R.id.dialog_addScene_name);
        mDialogAddSceneCancle = (TextView) findViewById(R.id.dialog_addScene_cancle);
        mDialogAddSceneOk = (TextView) findViewById(R.id.dialog_addScene_ok);
        if (MyApplication.mApplication.isVisitor()) {
            getRightImage().setVisibility(View.GONE);
            add_ref_LL.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化账号下的两网模块列表
     */
    private void initListview() {
        if (mAdapter == null)
            mAdapter = new NetWork_Adapter(this, MyApplication.mApplication.getRcuInfoList(), NetWork_Adapter.LOGIN);
        else mAdapter.notifyDataSetChanged();
        mNetmoduleListview.setAdapter(mAdapter);
    }

    @Override
    public void initData() {

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (MyApplication.mApplication.isSeekNet() && datType == 0) {
                    MyApplication.mApplication.setSeekNet(false);
                    MyApplication.mApplication.dismissLoadDialog();
                    initSeekListView();
                }
            }
        });
        initSeekList();

        initListview();
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewWorkSetActivity.this);
                builder.setTitle("提示");
                builder.setMessage("您确定要刷新联网模块列表？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        refNetLists();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
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
                if (!MyApplication.mApplication.isCanChangeNet()) {
                    ToastUtil.showText("正在加载数据，请稍后再试...");
                    return;
                }
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
                            dialog.dismiss();
                            MyApplication.mApplication.showLoadDialog(NewWorkSetActivity.this, false);
                            AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE,
                                    MyApplication.mApplication.getRcuInfoList().get(position).getDevUnitID());

                            WareData wareData = (WareData) Data_Cache.readFile((String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""));
                            if (wareData == null) {
                                MyApplication.setNewWareData();
                                GlobalVars.setIsLAN(true);
                                MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
                                    @Override
                                    public void upDataWareData(int datType, int subtype1, int subtype2) {
                                        if (datType == 0) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Thread.sleep(2000);
                                                        MyApplication.mApplication.dismissLoadDialog();
                                                        startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
                                                        finish();
                                                    } catch (InterruptedException e) {
                                                        MyApplication.mApplication.dismissLoadDialog();
                                                        startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
                                                        finish();
                                                    }
                                                }
                                            }).start();
                                        }
                                    }
                                });
                                SendDataUtil.getNetWorkInfo();
                            } else {
                                SendDataUtil.getNetWorkInfo();
                                MyApplication.mApplication.dismissLoadDialog();
                                MyApplication.mWareData = wareData;
                                startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
                                finish();
                            }
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
                MyApplication.mApplication.getUdpServer().sendSeekNet();
                MyApplication.mApplication.showLoadDialog(NewWorkSetActivity.this);
            }
        });
    }


    /**
     * 搜索的联网模块在使用前需要输入密码确认
     *
     * @param SeekListData
     */
    private void showPassDialog(final List<RcuInfo> SeekListData, final int position) {
        final Dialog dialog = new Dialog(NewWorkSetActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        dialog.setContentView(R.layout.dialog_addscene);
        dialog.show();
        mDialogAddSceneName = (EditText) dialog.findViewById(R.id.dialog_addScene_name);
        mDialogAddSceneCancle = (TextView) dialog.findViewById(R.id.dialog_addScene_cancle);
        mDialogAddSceneOk = (TextView) dialog.findViewById(R.id.dialog_addScene_ok);
        mTitleName = (TextView) dialog.findViewById(R.id.title_name);
        mTitle = (TextView) dialog.findViewById(R.id.title);
        mTitle.setText("模块密码");
        mDialogAddSceneName.setHint("请输入当前模块密码");
        mTitleName.setText("模块密码 :");
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
                String pass = mDialogAddSceneName.getText().toString();
                if (!pass.equals(SeekListData.get(position).getDevUnitPass())) {
                    ToastUtil.showText("密码不合适，请重新输入");
                } else {
                    ClickUseNet(position);
                }
            }
        });
    }

    /**
     * 准备使用此联网模块==跳转页面前的准备
     *
     * @param position
     */
    private void ClickUseNet(int position) {
        MyApplication.mApplication.showLoadDialog(NewWorkSetActivity.this, false);
        AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE,
                MyApplication.mApplication.getSeekRcuInfos().get(position).getDevUnitID());
        MyApplication.setNewWareData();
        GlobalVars.setIsLAN(true);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                MyApplication.mApplication.dismissLoadDialog();
                                startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
                                finish();
                            } catch (InterruptedException e) {
                                MyApplication.mApplication.dismissLoadDialog();
                                startActivity(new Intent(NewWorkSetActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
                    }).start();
                }
            }
        });
        SendDataUtil.getNetWorkInfo();
    }


    /**
     * 初始化搜索到的联网模快列表--之前搜索过，就直接显示
     */
    private void initSeekList() {
        List<RcuInfo> SeekData = MyApplication.mApplication.getSeekRcuInfos();
        if (SeekData.size() == 0)
            return;
        SeekNetClick(SeekData);
    }

    /**
     * 初始化搜索联网模块
     */
    private void initSeekListView() {
        List<SearchNet> rcuInfo_SeekNet = MyApplication.getWareData().getSeekNets();
        List<RcuInfo> SeekListData = new ArrayList<>();
        for (int i = 0; i < rcuInfo_SeekNet.size(); i++) {
            RcuInfo info = new RcuInfo();
            info.setName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getName())));
            info.setCenterServ(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getCenterServ());
            info.setbDhcp(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getBDhcp());
            info.setDevUnitID(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getCanCpuID());
            info.setDevUnitPass(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getPass());
            info.setGateWay(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getGateway());
            info.setHwVversion(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getHwVersion());
            info.setSubMask(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getSubMask());
            info.setIpAddr(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getIpAddr());
            info.setMacAddr(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getMacAddr());
            info.setRoomNum(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getRoomNum());
            info.setSoftVersion(rcuInfo_SeekNet.get(i).getRcu_rows().get(0).getSoftVersion());
            SeekListData.add(info);
        }
        MyApplication.mApplication.setSeekRcuInfos(SeekListData);
        if (SeekListData.size() == 1) {
            mSeekAdapter = new NetWork_Adapter(this, SeekListData, NetWork_Adapter.SEEK);
            mNewWorksousuolistview.setAdapter(mSeekAdapter);
            showPassDialog(SeekListData, 0);
        } else {
            SeekNetClick(SeekListData);
        }
    }

    /**
     * 点击搜索列表
     * @param seekListData
     */
    private void SeekNetClick(final List<RcuInfo> seekListData) {
        mSeekAdapter = new NetWork_Adapter(this, seekListData, NetWork_Adapter.SEEK);
        mNewWorksousuolistview.setAdapter(mSeekAdapter);
        mNewWorksousuolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!MyApplication.mApplication.isCanChangeNet()) {
                    ToastUtil.showText("正在加载数据，请稍后再试...");
                    return;
                }
                if (GlobalVars.getDevid().equals(MyApplication.mApplication.getSeekRcuInfos().get(position).getDevUnitID()))
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
                            dialog.dismiss();
                            showPassDialog(seekListData, position);
                        }
                    });
                    dialog.create().show();
                }
            }
        });
    }


    /**
     * 刷新联网模快列表
     */
    private void refNetLists() {
        MyApplication.mApplication.showLoadDialog(NewWorkSetActivity.this);
        Map<String, String> param = new HashMap<>();
        param.put("userName", GlobalVars.getUserid());
        param.put("passwd", (String) AppSharePreferenceMgr.
                get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""));
        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.NETLISTS, param, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                Log.i("LOGIN", resultDesc.getResult());
                MyApplication.mApplication.dismissLoadDialog();
                super.onSuccess(resultDesc);
                Log.i(TAG, "onSuccess: " + resultDesc.getResult());
                gson = new Gson();
                Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);

                if (result.getCode() == HTTPRequest_BackCode.LOGIN_OK) {
                    // 刷新成功
                    setRcuInfoList(result);
                    ToastUtil.showText("操作成功");
                    initData();
                } else {
                    // 刷新失败
                    ToastUtil.showText("操作失败，请稍后再试");
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Log.i(TAG, "刷新失败: " + code + "****" + message);
                //登陆失败
                MyApplication.mApplication.dismissLoadDialog();
                ToastUtil.showText("刷新失败，网络不可用或服务器异常");
            }
        });
    }


    /**
     * 刷新后更改界面数据
     * @param result
     */
    public void setRcuInfoList(Http_Result result) {
        if (result == null)
            return;

        List<RcuInfo> rcuInfos = new ArrayList<>();
        for (int i = 0; i < result.getData().size(); i++) {
            RcuInfo rcuInfo = new RcuInfo();
            rcuInfo.setCanCpuName(result.getData().get(i).getCanCpuName());
            rcuInfo.setDevUnitID(result.getData().get(i).getDevUnitID());
            rcuInfo.setDevUnitPass(result.getData().get(i).getDevPass());
            rcuInfo.setOnLine(result.getData().get(i).isOnline());
            rcuInfos.add(rcuInfo);
        }
        AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, gson.toJson(rcuInfos));
    }

    /**
     * 初始化添加联网模快
     * @param dialog
     */
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
                    MyApplication.mApplication.dismissLoadDialog();
                    // 添加成功
                    weakReference.get().refNetLists();
                } else if (msg.what == Net_AddorDel_Helper.DELNEWMODULE_OK) {
                    MyApplication.mApplication.dismissLoadDialog();
                    weakReference.get().refNetLists();
                    //删除成功
                } else if (msg.what == Net_AddorDel_Helper.EDITNEWMODULE_OK) {
                    //修改成功
                    MyApplication.mApplication.dismissLoadDialog();
                    weakReference.get().refNetLists();
                }
            }
        }
    }
}
