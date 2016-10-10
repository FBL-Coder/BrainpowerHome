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

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;

/**
 * 作者：FBL  时间： 2016/9/5.
 */
public class LoginActivity extends Activity {

    private Button lobin_btn;
    private EditText login_id, login_pass;
    private boolean isNewGw = true;
    private List<RcuInfo> mRcuInfos;

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

//            for (int i = 0; i < mRcuInfos.size(); i++) {
//                if (CommonUtils.hexStringToBytes(id).equals(mRcuInfos.get(i).getDevUnitID())
//                        && Arrays.equals(pass.getBytes(), mRcuInfos.get(i).getDevUnitPass())) {
//                    Toast.makeText(this, "当前联网模块已存在，请重新输入", 0).show();
//                    isNewGw = false;
//                    break;
//                } else {
//                    isNewGw = true;
//                }
//            }
                if (isNewGw) {
                    mRcuInfos = new ArrayList<>();
                    mRcuInfos.add(info);
                    Gson gson = new Gson();
                    String str = gson.toJson(mRcuInfos);
                    SharedPreferences sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("list", str);
                    editor.commit();
                }

                startActivity(new Intent(LoginActivity.this,WelcomeActivity.class));
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
