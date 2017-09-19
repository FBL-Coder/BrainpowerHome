package cn.semtec.community2.activity;

import android.annotation.SuppressLint;
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

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.regex.Pattern;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.User;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Login_Helper;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.model.LoginHelper;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;

public class LoginActivity extends MyBaseActivity implements OnClickListener {

    private Button btn_login;
    private TextView btn_regist;
    private EditText et_account;
    private EditText et_password;
    private TextView btn_forget, btn_tourist;
    //    private PanningView image;
    private SharedPreferenceUtil preference;
    private String cellphone;
    private String password;
    public static LoginActivity instace;
    private User user;
    private int LOGIN_OK = 1;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MyHttpUtil.SUCCESS0:
                    cn.etsoft.smarthome.MyApplication.mApplication.setVisitor(false);
                    Login_Helper.login_helper.login(LoginActivity.this, et_account, et_password);
                    break;
                case MyHttpUtil.SUCCESSELSE:
                    ToastUtil.showText(getString(R.string.login_error));
                    cancelProgress();
                    break;
                case MyHttpUtil.CATCH:
                    ToastUtil.showText(getString(R.string.data_abnormal));
                    cancelProgress();
                    break;
                case MyHttpUtil.FAILURE:
                    ToastUtil.showText(getString(R.string.net_abnormal));
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
        initEvent();

    }


    public void initEvent() {
        instace = this;
        setView();
        setListener();
    }


    @Override
    protected void onResume() {
        initEvent();
        super.onResume();
    }


    private void setView() {
        btn_login = (Button) findViewById(R.id.login_btn);
        btn_regist = (TextView) findViewById(R.id.login_register_btn);
        btn_forget = (TextView) findViewById(R.id.login_forgetpass);
        btn_tourist = (TextView) findViewById(R.id.login_ourist);
        et_account = (EditText) findViewById(R.id.login_id);
        et_password = (EditText) findViewById(R.id.login_pass);
    }


    private void setListener() {
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_ourist:
                cn.etsoft.smarthome.MyApplication.mApplication.setVisitor(true);
                Data_Cache.writeFile(GlobalVars.getDevid(), new WareData());
                MyApplication.setNewWareData();
                GlobalVars.setDevid("");
                GlobalVars.setDevpass("");
                GlobalVars.setUserid("");
                AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE, "");
                AppSharePreferenceMgr.put(GlobalVars.USERID_SHAREPREFERENCE, "");
                AppSharePreferenceMgr.put(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0);
                AppSharePreferenceMgr.put(GlobalVars.USERPASSWORD_SHAREPREFERENCE, "");
                AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, "");
                Intent intent = new Intent(this, cn.etsoft.smarthome.Activity.SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.login_btn:
                cellphone = et_account.getText().toString();
                password = et_password.getText().toString();

                Pattern p = Pattern.compile("^1\\d{10}$");
                Pattern w = Pattern.compile("\\w{6,12}");
                if (!(p.matcher(cellphone).matches() && w.matcher(password).matches())) {
                    ToastUtil.showText(getString(R.string.login_error1));
                    break;
                }
                LoginHelper loginHelper = new LoginHelper(handler);
                loginHelper.loginServer(cellphone, password);
                showProgress();
                break;
            case R.id.login_forgetpass:
                Intent intent3 = new Intent(this, RepickPasswordActivity.class);
                startActivity(intent3);
                break;
            case R.id.login_register_btn:
                startActivityForResult(new Intent(LoginActivity.this, cn.semtec.community2.activity.RegistActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getBundleExtra("bundle");
            et_account.setText(bundle.getString("id"));
            et_password.setText(bundle.getString("pass"));
            cellphone = bundle.getString("id");
            password = bundle.getString("pass");
        }
    }

    @Override
    protected void onDestroy() {
        instace = null;
        Log.e("成功", "destory");
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

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
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
