package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.UUID;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.User;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/11/29.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText login_username, login_password;
    private TextView login, adduser;
    private int LOGIN_OK = 1;
    private String APPID;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etsoft_login);
        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //初始化控件
        initView();
        if ((sharedPreferences.getString("appid", "") == null || "".equals(sharedPreferences.getString("appid", ""))) && sharedPreferences.getString("appid", "").length() < 5) {
            UUID uuid = UUID.randomUUID();
            APPID = uuid.toString().replace("-", "");
            Log.i("APPID", "唯一识别码为:" + APPID);
            editor.putString("appid", APPID);
            editor.commit();
        }
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_loginUser.getValue()) {
                    dialog.dismiss();
                    if (MyApplication.getWareData().getLogin_result() == 0) {
                        ToastUtil.showToast(LoginActivity.this, "登陆成功");
                        Gson gson = new Gson();
                        String str = gson.toJson(user);
                        editor.putString("user", str);
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class).putExtra("login", LOGIN_OK));
                        finish();
                    } else {
                        ToastUtil.showToast(LoginActivity.this, "登录失败");
                    }
                }
            }
        });
    }
    /**
     * 初始化控件
     */
    private void initView() {
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        login = (TextView) findViewById(R.id.login);
        adduser = (TextView) findViewById(R.id.adduser);
        login.setOnClickListener(this);
        adduser.setOnClickListener(this);
    }

    ProgressDialog dialog;
    User user;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("正在登录...");
                dialog.show();
                String id = login_username.getText().toString();
                String pass = login_password.getText().toString();
                if ("".equals(id) || "".equals(pass)) {
                    ToastUtil.showToast(this, "请输入账号和密码！");
                    dialog.dismiss();
                    return;
                }
                user = new User();
                user.setId(id);
                user.setPass(pass);
                MyApplication.sendUserData(user.getId(), user.getPass());
                break;
            case R.id.adduser:
                startActivityForResult(new Intent(LoginActivity.this, cn.semtec.community2.activity.RegistActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getBundleExtra("bundle");
            String id = bundle.getString("id");
            String pass = bundle.getString("pass");
            login_username.setText(id);
            login_password.setText(pass);

            MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
                @Override
                public void upDataWareData(int what) {
                    if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_loginUser.getValue()) {
                        dialog.dismiss();
                        if (MyApplication.getWareData().getLogin_result() == 0) {
                            ToastUtil.showToast(LoginActivity.this, "登陆成功");
                            Gson gson = new Gson();
                            String str = gson.toJson(user);
                            editor.putString("user", str);
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, WelcomeActivity.class).putExtra("login", LOGIN_OK));
                            finish();
                        } else {
                            ToastUtil.showToast(LoginActivity.this, "登录失败");
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
