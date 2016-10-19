package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;

/**
 * 作者：FBL  时间： 2016/9/5.
 */
public class LoginActivity extends Activity {

    private Button lobin_btn;
    private EditText login_id, login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        initEvent();

    }

    private void initEvent() {

        lobin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = login_id.getText().toString();
                String pass = login_pass.getText().toString();

                RcuInfo info = new RcuInfo();
                info.setDevUnitID(id);
                info.setDevUnitPass(pass);
                info.setName("");

                MyApplication.getWareData().getRcuInfos().add(info);
                Gson gson = new Gson();
                String str = gson.toJson(MyApplication.getWareData().getRcuInfos());
                SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("list", str);
                editor.commit();


                startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                finish();
            }
        });
    }

    private void initView() {
        lobin_btn = (Button) findViewById(R.id.lobin_btn);
        login_id = (EditText) findViewById(R.id.login_id);
        login_pass = (EditText) findViewById(R.id.login_pass);
    }
}
