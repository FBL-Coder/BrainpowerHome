package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpRequest;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.HTTPRequest_BackCode;
import cn.etsoft.smarthome.UiHelper.Login_Helper;
import cn.etsoft.smarthome.UiHelper.New_AddorDel_Helper;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/16.
 * 登陆界面
 */

public class LoginActivity extends BaseActivity {

    private EditText mLoginId, mLoginPass;
    private Button mLoginBtn;
    private TextView mRegister;

    @SuppressLint("WrongViewCast")
    @Override
    public void initView() {
        setLayout(R.layout.activity_login);
        mLoginId =  getViewById(R.id.login_id);
        mLoginPass =  getViewById(R.id.login_pass);
        mLoginBtn =  getViewById(R.id.login_btn);
        mRegister =  getViewById(R.id.register_btn);

    }

    @Override
    public void initData() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
//                final OkHttpClient client = new OkHttpClient();
//                final Request request = new Request.Builder()
////                        .url("http://192.168.1.220:8080")
//                        .url("http://192.168.1.220:25000/index.php/api/index/index")
//                        .build();
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Response response = null;
//                        try {
//                            response = client.newCall(request).execute();
//                            Log.i("NETWORK", response.code() + "");
//                            if (response.code() == 200) {
//                                Log.i(TAG, "onSuccess: " + response.body().string());
//                            } else {
//                                Log.i(TAG, "onFailure: " + response.code()+response.body().string());
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        Looper.prepare();
//                                        ToastUtil.showText("数据加载失败！网络服务器异常...");
//                                        Looper.loop();
//                                    }
//                                }.start();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
                Login_Helper.login_helper.login(LoginActivity.this, mLoginId, mLoginPass);
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == 0) {
            Bundle bundle = data.getBundleExtra("bundle");
            mLoginId.setText(bundle.getString("ID"));
            mLoginPass.setText(bundle.getString("PASS"));
        }
    }



//    @Override
//    protected void onRestart() {
//        if ("".equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE,""))){
//            finish();
//        }else {
//            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
//            finish();
//        }
//        super.onRestart();
//    }


}
