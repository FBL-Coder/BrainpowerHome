package cn.semtec.community2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Register_Helper;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.TimeCountUtil;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class RegistActivity extends MyBaseActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_password, et_password_again;
    private EditText et_verify;
    private TextView btn_verify;
    private TextView btn_commit;
    //    private CheckBox check_clause;
//    private View tv_clause;
    private String cellphone;
    private String verify;
    private String password;
    private Intent intent;
    private int ADDOK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setView();
        setListener();
        intent = getIntent();
    }

    private void setView() {
        et_phone = (EditText) findViewById(R.id.register_id);
        et_verify = (EditText) findViewById(R.id.register_verify);
        btn_verify = (TextView) findViewById(R.id.register_verify_btn);
        et_password = (EditText) findViewById(R.id.register_pass);
        et_password_again = (EditText) findViewById(R.id.register_againpass);
        btn_commit = (TextView) findViewById(R.id.register_btn);

    }

    private void setListener() {
        btn_verify.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_verify_btn:
                getVerifycode();
                break;
            case R.id.register_btn:
                if (!CheckInput()) {
                    break;
                }
                sendToNET();
                break;
        }

    }

    private boolean CheckInput() {
        cellphone = et_phone.getText().toString();
        verify = et_verify.getText().toString();
        password = et_password.getText().toString();

        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(cellphone);
        if (!m.matches()) {
            ToastUtil.showText(getString(R.string.regist_error));
            return false;
        }
        if (verify.length() < 4) {
            ToastUtil.showText(getString(R.string.repickpassword_verify_error));
            return false;
        }
        Pattern w = Pattern.compile("\\w{6,12}");
        if (!w.matcher(password).matches()) {
            ToastUtil.showText(getString(R.string.changePassword_pwtips));
            return false;
        }
        return true;
    }

    //注册
    private void sendToNET() {
        try {
            JSONObject user = new JSONObject();
            user.put("cellphone", cellphone);
            user.put("password", password);

            JSONObject json = new JSONObject();
            json.put("cellphone", cellphone);
            json.put("smscode", verify);
            json.put("user", user);

            RequestParams params = new RequestParams();
            params.addHeader("Content-type", "application/json; charset=utf-8");
            params.setHeader("Accept", "application/json");
            HttpEntity entity;
            try {
                entity = new StringEntity(json.toString(), "UTF-8");
                params.setBodyEntity(entity);
            } catch (UnsupportedEncodingException e1) {
                CatchUtil.catchM(e1);
            }
            String url = Constants.CONTENT_NEW;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    cancelProgress();
                    String mResult = responseInfo.result.toString();
                    try {
                        JSONObject jo = new JSONObject(mResult);
                        if (jo.getInt("returnCode") == ADDOK) {
                            //添加智能家居后台用户注册
                            Register_Helper.register_helper.register(RegistActivity.this, et_phone, et_password, et_password);
                        } else {
                            ToastUtil.showText(jo.getString("msg"));
                            LogUtils.i(jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                        ToastUtil.showText(getString(R.string.data_abnormal));
                        LogUtils.i(getString(R.string.data_abnormal));
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    cancelProgress();
                    LogUtils.i(getString(R.string.net_abnormal) + msg);
                    ToastUtil.showText(getString(R.string.net_abnormal));
                }

            });
            showProgress();
            httpUtil.send();
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }

    // 获取验证码
    private void getVerifycode() {
        cellphone = et_phone.getText().toString();
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(cellphone);
        if (!m.matches()) {
            ToastUtil.showText(getString(R.string.regist_error));
            return;
        }
        //倒计时
        TimeCountUtil timeCountUtil = new TimeCountUtil(this, 60000, 1000, btn_verify);
        timeCountUtil.start();
        String url = Constants.CONTENT_CELLPHONE + cellphone + Constants.CONTENT_VERIFYCODE;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        cancelProgress();
                        if (responseInfo.statusCode == 200) {
                            String mResult = responseInfo.result.toString();
                            Log.i(TAG, "onSuccess: " + mResult);
                            //获得回传的 json字符串
                            JSONObject jo;
                            try {
                                jo = new JSONObject(mResult);
                                //0为成功  <0为系统异常  其他待定
                                if (jo.getInt("returnCode") == 0) {
                                    JSONObject args = (JSONObject) jo.get("args");
                                    if (!args.isNull("smscode")) {
                                        String smscode = args.getString("smscode");
                                        ToastUtil.showText(smscode);
                                        et_verify.setText(smscode);
                                    }
                                } else {
                                    ToastUtil.showText(jo.getString("msg"));
                                }
                            } catch (JSONException e) {
                                CatchUtil.catchM(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        cancelProgress();
                        ToastUtil.showText(getString(R.string.net_abnormal));
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                    }
                });
        http.send();
        showProgress();
    }
}
