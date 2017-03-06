package cn.semtec.community2.activity;

import android.content.Intent;
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

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.TimeCountUtil;
import cn.semtec.community2.util.ToastUtil;

public class RegistActivity extends MyBaseActivity implements View.OnClickListener {

    private View btn_back;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_verify;
    private Button btn_verify;
    private View btn_commit;
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
        setContentView(R.layout.activity_regist);
        setView();
        setListener();
        intent = getIntent();
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
                            cn.etsoft.smarthome.MyApplication.mInstance.setOnGetWareDataListener(new cn.etsoft.smarthome.MyApplication.OnGetWareDataListener() {
                                @Override
                                public void upDataWareData(int what) {
                                    if (what == UdpProPkt.E_UDP_RPO_DAT.e_add_user.getValue()) {
                                        if (cn.etsoft.smarthome.MyApplication.getWareData().getAddUser_reslut() == ADDOK) {

                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", cellphone);
                                            bundle.putString("pass", password);
                                            ToastUtil.s(RegistActivity.this, getString(R.string.regist_regist_s));
                                            LogUtils.i(getString(R.string.regist_regist_s));
                                            MyApplication.getSharedPreferenceUtil().putString("cellphone", cellphone);
                                            MyApplication.getSharedPreferenceUtil().putString("password", password);
                                            setResult(0, getIntent().putExtra("bundle", bundle));
                                            finish();
                                        } else if (cn.etsoft.smarthome.MyApplication.getWareData().getLogin_result() == 1) {
                                            cn.etsoft.smarthome.utils.ToastUtil.showToast(RegistActivity.this, "用户名已存在");
                                            return;
                                        } else {
                                            cn.etsoft.smarthome.utils.ToastUtil.showToast(RegistActivity.this, "注册失败");
                                            return;
                                        }
                                    }
                                }
                            });
                            cn.etsoft.smarthome.MyApplication.addUserData(cellphone, password);

                        } else {
                            ToastUtil.s(RegistActivity.this, jo.getString("msg"));
                            LogUtils.i(jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                        ToastUtil.s(RegistActivity.this, getString(R.string.data_abnormal));
                        LogUtils.i(getString(R.string.data_abnormal));
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    cancelProgress();
                    LogUtils.i(getString(R.string.net_abnormal) + msg);
                    ToastUtil.s(RegistActivity.this, getString(R.string.net_abnormal));
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
            ToastUtil.s(this, getString(R.string.regist_error));
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
                                        et_verify.setText(smscode);
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
                        cancelProgress();
                        ToastUtil.s(RegistActivity.this, getString(R.string.net_abnormal));
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                    }
                });
        http.send();
        showProgress();
    }
}