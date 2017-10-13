package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.etsoft.smarthome.Activity.HomeActivity;
import cn.etsoft.smarthome.Activity.Settings.NewWorkSetActivity;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import cn.etsoft.smarthome.Utils.SendDataUtil;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/19.
 * 登录辅助类
 */

public class Login_Helper {
    private Gson gson;
    private String input_id;
    private String input_pass;
    private Activity mContext;
    public static Login_Helper login_helper = new Login_Helper();

    public void login(final Activity mContext, EditText id, EditText pass) {
        this.mContext = mContext;
        input_id = id.getText().toString();
        input_pass = pass.getText().toString();
        if (!(HTTPRequest_BackCode.id_rule.matcher(input_id).matches() && HTTPRequest_BackCode.pass_rule.matcher(input_pass).matches())) {
            ToastUtil.showText("账号或密码输入人不正确");
            return;
        }
        MyApplication.mApplication.showLoadDialog(mContext);
        Map<String, String> param = new HashMap<>();
        param.put("userName", input_id);
        param.put("passwd", input_pass);
        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.LOGIN, param, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                Log.i("LOGIN", resultDesc.getResult());
                MyApplication.mApplication.dismissLoadDialog();
                super.onSuccess(resultDesc);
                Log.i(TAG, "onSuccess: " + resultDesc.getResult());
                gson = new Gson();
                Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);

                if (result.getCode() == HTTPRequest_BackCode.LOGIN_OK) {
                    // 登陆成功
                    ToastUtil.showText("登陆成功");
                    setRcuInfoList(result);
                } else if (result.getCode() == HTTPRequest_BackCode.LOGIN_ERROR) {
                    // 登陆失败
                    ToastUtil.showText("登陆失败，请稍后再试");
                } else if (result.getCode() == HTTPRequest_BackCode.LOGIN_USER_NOTFIND) {
                    //用户不存在
                    ToastUtil.showText("登陆失败，用户不存在");
                } else if (result.getCode() == HTTPRequest_BackCode.LOGIN_ERROR_Exception) {
                    //服务器查询失败
                    ToastUtil.showText("登陆失败，服务器查询失败");
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                Log.i(TAG, "onFailure: " + code + "****" + message);
                //登陆失败
                MyApplication.mApplication.dismissLoadDialog();
                ToastUtil.showText("登陆失败，网络不可用或服务器异常");
            }
        });
    }

    public void setRcuInfoList(Http_Result result) {

        AppSharePreferenceMgr.put(GlobalVars.USERID_SHAREPREFERENCE, input_id);
        AppSharePreferenceMgr.put(GlobalVars.USERPASSWORD_SHAREPREFERENCE, input_pass);

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
        if (rcuInfos.size() > 0)
//            mContext.startActivity(new Intent(mContext, NewWorkSetActivity.class));
//        else {
            AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE, rcuInfos.get(0).getDevUnitID());
        AppSharePreferenceMgr.put(GlobalVars.LOGIN_SHAREPREFERENCE, true);
        mContext.startActivity(new Intent(mContext, HomeActivity.class));
//        }
        mContext.finish();
    }
}
