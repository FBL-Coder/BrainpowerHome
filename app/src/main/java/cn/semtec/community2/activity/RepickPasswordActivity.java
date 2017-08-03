package cn.semtec.community2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.TimeCountUtil;
import cn.semtec.community2.util.ToastUtil;

public class RepickPasswordActivity extends MyBaseActivity implements View.OnClickListener {

    private View btn_back;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_verify;
    private Button btn_verify;
    private View btn_commit;
    private String cellphone;
    private String verify;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repick_password);
        setView();
        setListener();
    }

    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_verify = (EditText) findViewById(R.id.et_verify);
        btn_verify = (Button) findViewById(R.id.btn_verify);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_commit = findViewById(R.id.btn_commit);
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_verify.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_verify:
                getVerifycode();
                break;
            case R.id.btn_commit:
                if (!CheckInput()) {
                    break;
                }
                sendToNET();
                break;
        }
    }

    //检查数据输入格式
    private boolean CheckInput() {
        cellphone = et_phone.getText().toString();
        verify = et_verify.getText().toString();
        password = et_password.getText().toString();

        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(cellphone);
        if (!m.matches()) {
            ToastUtil.s(this, getString(R.string.regist_error));
            return false;
        }
        if (verify.length() < 4) {
            ToastUtil.s(this, getString(R.string.repickpassword_verify_error));
            return false;
        }
        Pattern w = Pattern.compile("\\w{6,12}");
        if (!w.matcher(password).matches()) {
            ToastUtil.s(this, getString(R.string.changePassword_pwtips));
            return false;
        }
        return true;
    }

    private void sendToNET() {
        try {
            JSONObject json = new JSONObject();
            json.put("cellphone", cellphone);
            json.put("password", password);
            json.put("smscode", verify);

            RequestParams params = new RequestParams();
            params.addHeader("Content-type", "application/json; charset=utf-8");
            params.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            params.setBodyEntity(entity);

            String url = Constants.CONTENT_FORGET;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    cancelProgress();
                    String mResult = responseInfo.result.toString();
                    try {
                        JSONObject jo = new JSONObject(mResult);
                        if (jo.getInt("returnCode") == 0) {
                            ToastUtil.s(RepickPasswordActivity.this, getString(R.string.repickpassword_repick_s));
                            LogUtils.i(getString(R.string.repickpassword_repick_s));
                            MyApplication.getSharedPreferenceUtil().putString("cellphone", cellphone);
                            MyApplication.getSharedPreferenceUtil().putString("password", password);
                            finish();
                        } else {
                            ToastUtil.s(RepickPasswordActivity.this, jo.getString("msg"));
                            LogUtils.i(jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                        ToastUtil.s(RepickPasswordActivity.this, getString(R.string.data_abnormal));
                        LogUtils.i(getString(R.string.data_abnormal));
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    cancelProgress();
                    LogUtils.i(getString(R.string.net_abnormal) + msg);
                    ToastUtil.s(RepickPasswordActivity.this, getString(R.string.net_abnormal));
                }

            });
            showProgress();
            httpUtil.send();
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }


    //发送请求 获取验证码
    private void getVerifycode() {
        cellphone = et_phone.getText().toString();
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(cellphone);
        if (!m.matches()) {
            ToastUtil.s(this, getString(R.string.regist_error));
            return;
        }
        //倒计时
        TimeCountUtil timeCountUtil = new TimeCountUtil(this, 60000, 1000, btn_verify);
        timeCountUtil.start();
        //获取验证码
        String url = Constants.CONTENT_CELLPHONE + cellphone + Constants.CONTENT_VERIFYCODE;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (responseInfo.statusCode == 200) {
                            String mResult = responseInfo.result.toString();
                            //获得回传的 json字符串
                            JSONObject jo;
                            try {
                                jo = new JSONObject(mResult);
                                //0为成功  <0为系统异常  其他待定
                                if (jo.getInt("returnCode") == 0) {
                                    JSONObject args = (JSONObject) jo.get("args");

                                    if (!args.isNull("smscode")) {
                                        String smscode = args.getString("smscode");
                                        ToastUtil.l(getApplication(), smscode);
                                    }
                                } else {
                                    ToastUtil.s(getApplication(), jo.getString("msg"));
                                }
                            } catch (JSONException e) {
                                CatchUtil.catchM(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        ToastUtil.s(RepickPasswordActivity.this, getString(R.string.net_abnormal));
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                    }
                });
        http.send();
    }
}
