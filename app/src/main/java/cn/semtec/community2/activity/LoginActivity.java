package cn.semtec.community2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.UUID;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.User;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.ui.WelcomeActivity;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.model.LoginHelper;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.ToastUtil;

public class LoginActivity extends MyBaseActivity implements OnClickListener {

    private Button btn_login;
    private TextView btn_regist;
    private EditText et_account;
    private EditText et_password;
    private TextView btn_forget, btn_tourist;
//    private PanningView image;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SharedPreferenceUtil preference;
    private String cellphone;
    private String password;
    public static LoginActivity instace;
    private String APPID;
    private User user;
    private int LOGIN_OK = 1;

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MyHttpUtil.SUCCESS0:
//                    // 跳转到 主面板BaseActivity
//                    Intent intent1 = new Intent(LoginActivity.this, BaseActivity.class);
//                    startActivity(intent1);
                    cn.etsoft.smarthome.MyApplication.mInstance.setSearch(false);
                    cn.etsoft.smarthome.MyApplication.sendUserData(cellphone, password);
//                    finish();
                    break;
                case MyHttpUtil.SUCCESSELSE:
                    ToastUtil.l(LoginActivity.this, getString(R.string.login_error));
                    cancelProgress();
                    break;
                case MyHttpUtil.CATCH:
                    ToastUtil.s(LoginActivity.this, getString(R.string.data_abnormal));
                    cancelProgress();
                    break;
                case MyHttpUtil.FAILURE:
                    ToastUtil.s(LoginActivity.this, getString(R.string.net_abnormal));
                    cancelProgress();
                    break;
                default:
                    cancelProgress();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("profile", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if ((sharedPreferences.getString("appid", "") == null || "".equals(sharedPreferences.getString("appid", ""))) && sharedPreferences.getString("appid", "").length() < 5) {
            UUID uuid = UUID.randomUUID();
            APPID = uuid.toString().replace("-", "");
            Log.i("APPID", "唯一识别码为:" + APPID);
            editor.putString("appid", APPID);
            editor.commit();
        }
        instace = this;


        cn.etsoft.smarthome.MyApplication.mInstance.setOnGetWareDataListener(new cn.etsoft.smarthome.MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_loginUser.getValue()) {
                    if (cn.etsoft.smarthome.MyApplication.getWareData().getLogin_result() == 0) {
                        cn.etsoft.smarthome.utils.ToastUtil.showToast(LoginActivity.this, "登陆成功");
                        user = new User();
                        user.setId(cellphone);
                        user.setPass(password);
                        Gson gson = new Gson();
                        String str = gson.toJson(user);
                        editor.putString("user", str);
                        editor.commit();
                    } else {
                        cn.etsoft.smarthome.utils.ToastUtil.showToast(LoginActivity.this, "登录失败");
                        return;
                    }


                }
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getRcuInfo.getValue()) {
                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class).putExtra("login", LOGIN_OK));
                    finish();
                }
            }
        });
        setView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        preference = MyApplication.getSharedPreferenceUtil();
        cellphone = preference.getString("cellphone");
        password = preference.getString("password");
        et_account.setText(cellphone);
        et_password.setText(password);
//        image.startPanning();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        image.stopPanning();
    }

    private void setView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_regist = (TextView) findViewById(R.id.btn_regist);
        btn_forget = (TextView) findViewById(R.id.btn_forget);
        btn_tourist = (TextView) findViewById(R.id.btn_tourist);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);

//        image = (PanningView) findViewById(R.id.image);
//        image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.login_bg));
    }


    private void setListener() {
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                cellphone = et_account.getText().toString();
                password = et_password.getText().toString();

                Pattern p = Pattern.compile("^1\\d{10}$");
                Pattern w = Pattern.compile("\\w{6,12}");
                if (!(p.matcher(cellphone).matches() && w.matcher(password).matches())) {
                    ToastUtil.s(this, getString(R.string.login_error1));
                    break;
                }
                LoginHelper loginHelper = new LoginHelper(handler);
                loginHelper.loginServer(cellphone, password);
                showProgress();
                break;
            case R.id.btn_forget:
                Intent intent3 = new Intent(this, RepickPasswordActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_regist:
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
            cn.etsoft.smarthome.MyApplication.mInstance.setOnGetWareDataListener(new cn.etsoft.smarthome.MyApplication.OnGetWareDataListener() {
                @Override
                public void upDataWareData(int what) {
                    if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_loginUser.getValue()) {
                        if (cn.etsoft.smarthome.MyApplication.getWareData().getLogin_result() == 0) {
                            cn.etsoft.smarthome.utils.ToastUtil.showToast(LoginActivity.this, "登陆成功");
                            Gson gson = new Gson();
                            String str = gson.toJson(user);
                            editor.putString("user", str);
                            editor.commit();
                        } else {
                            cn.etsoft.smarthome.utils.ToastUtil.showToast(LoginActivity.this, "登录失败");
                        }
                    }

                    if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getRcuInfo.getValue()) {
                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class).putExtra("login", LOGIN_OK));
                        finish();
                    }

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        instace = null;
        super.onDestroy();
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
