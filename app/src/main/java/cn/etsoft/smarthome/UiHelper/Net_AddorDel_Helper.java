package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.etsoft.smarthome.Adapter.ListView.NetWork_Adapter;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.Utils.NewHttpPort;

/**
 * Author：FBL  Time： 2017/6/19.
 * 联网模块设置  辅助类
 */

public class Net_AddorDel_Helper {

    public static int ADDNEWMODULE_OK = 11;
    public static int EDITNEWMODULE_OK = 21;
    public static int DELNEWMODULE_OK = 31;

    /**
     * 添加联网模块
     *
     * @param name
     * @param id
     * @param pass
     */
    public static void addNew(final Handler handler, Activity activity, String name, String id, String pass) {

        if (name.isEmpty() || name.length() > 7) {
            ToastUtil.showText("模块名称过长或为空");
            return;
        }
        if (id.isEmpty()) {
            ToastUtil.showText("模块ID不能为空");
            return;
        }
        try {
            if (pass.isEmpty()) {
                ToastUtil.showText("模块密码不能为空");
                return;
            }
        } catch (Exception e) {
            pass = "";
        }
        MyApplication.mApplication.showLoadDialog(activity);
        Map<String, String> param = new HashMap<>();
        param.put("userName", (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, ""));
        param.put("passwd", (String) AppSharePreferenceMgr.get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""));
        param.put("devUnitID", id);
        param.put("canCpuName", name);
        param.put("devPass", pass);
        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.ADDNETMODULE, param, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                MyApplication.mApplication.dismissLoadDialog();
                Log.i("NEWMODULE", resultDesc.getResult());
                Gson gson = new Gson();
                Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);
                if (result.getCode() == HTTPRequest_BackCode.RCUINFO_OK) {
                    Message message = handler.obtainMessage();
                    message.what = ADDNEWMODULE_OK;
                    handler.sendMessage(message);
                    //添加成功
                    ToastUtil.showText("联网模块添加成功");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_ERROR) {
                    //添加失败
                    ToastUtil.showText("联网模块添加失败，模块ID已存在");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_ERROR_Exception) {
                    //请求失败
                    ToastUtil.showText("联网模块添加失败，请求异常");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_NETERROR) {
                    //执行失败
                    ToastUtil.showText("联网模块添加失败，服务器执行未成功");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_VERIFY_ERROR) {
                    // 添加失败  验证失败
                    ToastUtil.showText("联网模块认证失败");
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                // 添加失败
                MyApplication.mApplication.dismissLoadDialog();
                ToastUtil.showText("联网模块添加失败");
            }
        });
    }


    /**
     * 修改联网模块
     *
     * @param name
     */
    public static void editNew(final Handler handler,final List<RcuInfo> list, final int position, Activity activity, EditText name, String devUnitID, String devPass) {
        final String name_input = name.getText().toString();

        if (name_input.isEmpty() || name_input.length() > 7) {
            ToastUtil.showText("模块名称不合适");
            return;
        }
        if (devUnitID.isEmpty() || devPass.isEmpty()) {
            ToastUtil.showText("模块ID和模块密码不能为空");
            return;
        }
        MyApplication.mApplication.showLoadDialog(activity);
        Map<String, String> param = new HashMap<>();
        param.put("userName", (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, ""));
        param.put("passwd", (String) AppSharePreferenceMgr.get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""));
        param.put("devUnitID", devUnitID);
        param.put("canCpuName", name_input);
        param.put("devPass", devPass);
        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.EDITNETMODULE, param, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                MyApplication.mApplication.dismissLoadDialog();
                Log.i("NEWMODULE", resultDesc.getResult());
                Gson gson = new Gson();
                Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);
                if (result.getCode() == HTTPRequest_BackCode.RCUINFO_OK) {
                    //修改成功
                    list.get(position).setCanCpuName(name_input);
                    MyApplication.mApplication.setRcuInfoList(list);
                    handler.sendMessage(handler.obtainMessage());
                    ToastUtil.showText("联网模块修改成功");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_ERROR) {
                    // 修改失败
                    ToastUtil.showText("联网模块修改失败，模块ID不存在");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_ERROR_Exception) {
                    // 请求失败
                    ToastUtil.showText("联网模块修改失败，请求异常");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_NETERROR) {
                    // 执行失败
                    ToastUtil.showText("联网模块修改失败，服务器执行未成功");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_VERIFY_ERROR) {
                    // 修改失败  验证失败
                    ToastUtil.showText("联网模块修改验证失败");
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                // 修改失败
                MyApplication.mApplication.dismissLoadDialog();
                ToastUtil.showText("联网模块修改失败");
            }
        });
    }


    /**
     * 删除联网模块
     *
     * @param id
     */
    public static void deleteNew(final Handler handler, Activity activity, String id) {
        MyApplication.mApplication.showLoadDialog(activity);

        Map<String, String> param = new HashMap<>();
        param.put("userName", (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, ""));
        param.put("passwd", (String) AppSharePreferenceMgr.get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""));
        param.put("devUnitID", id);

        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.DELETENETMODULE, param, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                MyApplication.mApplication.dismissLoadDialog();
                Log.i("NEWMODULE", resultDesc.getResult());
                Gson gson = new Gson();
                Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);
                if (result.getCode() == HTTPRequest_BackCode.RCUINFO_OK) {
                    // 删除成功
                    Message message = handler.obtainMessage();
                    message.what = DELNEWMODULE_OK;
                    handler.sendMessage(message);
                    ToastUtil.showText("联网模块删除成功");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_ERROR) {
                    // 删除失败
                    ToastUtil.showText("联网模块删除失败，模块ID不存在");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_ERROR_Exception) {
                    // 请求失败
                    ToastUtil.showText("联网模块删除失败，请求异常");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_NETERROR) {
                    // 执行失败
                    ToastUtil.showText("联网模块删除失败，服务器执行未成功");
                } else if (result.getCode() == HTTPRequest_BackCode.RCUINFO_VERIFY_ERROR) {
                    // 删除失败  验证失败
                    ToastUtil.showText("联网模块删除失败");
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                // 删除失败
                MyApplication.mApplication.dismissLoadDialog();
                ToastUtil.showText("联网模块删除失败");
            }
        });
    }
}
