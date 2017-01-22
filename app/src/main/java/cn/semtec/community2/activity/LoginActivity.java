package cn.semtec.community2.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import cn.semtec.community2.MyApplication;
import cn.semtec.community2.model.LoginHelper;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.ToastUtil;
import cn.semtec.community2.view.PanningView;
import cn.etsoft.smarthome.R;

public class LoginActivity extends MyBaseActivity implements OnClickListener {

    private Button btn_login;
    private TextView btn_regist;
    private EditText et_account;
    private EditText et_password;
    private TextView btn_forget, btn_tourist;
    private PanningView image;

    private SharedPreferenceUtil preference;
    private String cellphone;
    private String password;
    public static LoginActivity instace;

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MyHttpUtil.SUCCESS0:
                    // 跳转到 主面板BaseActivity
                    Intent intent1 = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent1);
                    finish();
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
        instace = this;
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
        image.startPanning();
    }

    @Override
    protected void onStop() {
        super.onStop();
        image.stopPanning();
    }

    private void setView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_regist = (TextView) findViewById(R.id.btn_regist);
        btn_forget = (TextView) findViewById(R.id.btn_forget);
        btn_tourist = (TextView) findViewById(R.id.btn_tourist);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);

        image = (PanningView) findViewById(R.id.image);
        image.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.login_bg));
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
            case R.id.btn_regist:
                Intent intent2 = new Intent(this, RegistActivity.class);
                startActivityForResult(intent2, 100);
                break;
            case R.id.btn_forget:
                Intent intent3 = new Intent(this, RepickPasswordActivity.class);
                startActivity(intent3);
                break;
            case R.id.btn_tourist:
                Intent intent4 = new Intent(this, BaseActivity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        instace = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            Bundle b = data.getExtras();
            String cellphone = b.getString("cellphone");
            String password = b.getString("password");
            LoginHelper loginHelper = new LoginHelper(handler);
            loginHelper.loginServer(cellphone, password);
            showProgress();
        }
    }
}
