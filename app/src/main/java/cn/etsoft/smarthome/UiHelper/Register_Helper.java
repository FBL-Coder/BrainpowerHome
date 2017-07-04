package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.Utils.NewHttpPort;

/**
 * Author：FBL  Time： 2017/6/19.
 * 注册辅助类
 */

public class Register_Helper {

    public static Register_Helper register_helper = new Register_Helper();

    public void register(final Activity activity, EditText mRegisterId, EditText mRegisterPass) {
        final String id_input = mRegisterId.getText().toString();
        final String pass_input = mRegisterPass.getText().toString();
        if (!(HTTPRequest_BackCode.id_rule.matcher(id_input).matches() && HTTPRequest_BackCode.pass_rule.matcher(pass_input).matches())) {
            ToastUtil.showText("账号或密码输入人不正确");
            return;
        }
        MyApplication.mApplication.showLoadDialog(activity);

        Map<String, String> param = new HashMap<>();
        param.put("userName", id_input);
        param.put("passwd", pass_input);
        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.REGISTER, param, new HttpCallback() {
            @Override
            public void onSuccess(ResultDesc resultDesc) {
                super.onSuccess(resultDesc);
                MyApplication.mApplication.dismissLoadDialog();
                Log.i("REGISTER", resultDesc.getResult());
                Gson gson = new Gson();
                Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);
                if (result.getCode() == HTTPRequest_BackCode.REGISTER_OK){
                    // 注册成功
                    Intent intent = activity.getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",id_input);
                    bundle.putString("PASS",pass_input);
                    intent.putExtra("bundle",bundle);
                    activity.setResult(0,activity.getIntent());
                    register_helper = null;
                    activity.finish();
                }else if (result.getCode() == HTTPRequest_BackCode.REGISTER_EXIST){
                    //账号已存在
                    ToastUtil.showText("账号已存在，请重新输入");

                }else {
                    //注册失败
                    ToastUtil.showText("注册失败，请稍后再试");
                }
            }

            @Override
            public void onFailure(int code, String message) {
                super.onFailure(code, message);
                //注册失败
                MyApplication.mApplication.dismissLoadDialog();
                ToastUtil.showText("注册失败，网络不可用或服务器异常");
            }
        });
    }
}
