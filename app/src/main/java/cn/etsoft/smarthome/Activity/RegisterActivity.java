package cn.etsoft.smarthome.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Register_Helper;
import cn.etsoft.smarthome.Utils.NewHttpPort;

/**
 * Author：FBL  Time： 2017/6/16.
 * 注册界面
 */

public class RegisterActivity extends BaseActivity {
    private EditText mRegisterId;
    private EditText mRegisterPass;
    private EditText mRegisterAgainPass;
    private Button mRegisterBtn;
    private Intent mIntent;
    @Override
    public void initView() {
        setLayout(R.layout.activity_register);
        mIntent = getIntent();
        mRegisterId =  getViewById(R.id.register_id);
        mRegisterPass =  getViewById(R.id.register_pass);
        mRegisterBtn =  getViewById(R.id.register_btn);
        mRegisterAgainPass =  getViewById(R.id.register_againpass);
    }

    @Override
    public void initData() {

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register_Helper.register_helper.register(RegisterActivity.this,mRegisterId,mRegisterPass,mRegisterAgainPass);
            }
        });
    }
}
