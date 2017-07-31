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
        mLoginId = getViewById(R.id.login_id);
        mLoginPass = getViewById(R.id.login_pass);
        mLoginBtn = getViewById(R.id.login_btn);
        mRegister = getViewById(R.id.register_btn);
    }

    @Override
    public void initData() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.finishAllActivity();
    }
}
