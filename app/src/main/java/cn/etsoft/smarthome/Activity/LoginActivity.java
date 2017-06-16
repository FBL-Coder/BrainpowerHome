package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.Http_Result;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.HTTPRequest_BackCode;
import cn.etsoft.smarthome.Utils.NewHttpPort;

/**
 * Author：FBL  Time： 2017/6/16.
 */

public class LoginActivity extends BaseActivity {


    private EditText mLoginId, mLoginPass;
    private Button mLoginBtn;
    private Gson gson;
    private String input_id;
    private String input_pass;

    @SuppressLint("WrongViewCast")
    @Override
    public void initView() {
        setLayout(R.layout.activity_login);
        mLoginId = (EditText) findViewById(R.id.login_id);
        mLoginPass = (EditText) findViewById(R.id.login_pass);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
    }

    @Override
    public void initData() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_id = mLoginId.getText().toString();
                input_pass = mLoginPass.getText().toString();
                if (!(HTTPRequest_BackCode.id_rule.matcher(input_id).matches() && HTTPRequest_BackCode.pass_rule.matcher(input_pass).matches())) {
                    ToastUtil.showText("账号或密码输入人不正确");
                    return;
                }
                Map<String, String> param = new HashMap<>();
                param.put("userName", input_id);
                param.put("passwd", input_pass);
                OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.LOGIN, param, new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        gson = new Gson();
                        Http_Result result = gson.fromJson(resultDesc.getResult(), Http_Result.class);

                        if (result.getCode() == HTTPRequest_BackCode.LOGIN_OK) {
                            //TODO 登陆成功
                            setRcuInfoList(result);
                        } else if (result.getCode() == HTTPRequest_BackCode.LOGIN_ERROR) {
                            //TODO 登陆失败
                        }
                        Log.i("LOGIN", resultDesc.getResult());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                        //TODO 登陆失败
                    }
                });
            }
        });
    }

    public void setRcuInfoList(Http_Result result) {

        AppSharePreferenceMgr.put(this, GlobalVars.USERID_SHAREPREFERENCE, input_id);
        AppSharePreferenceMgr.put(this, GlobalVars.USERPASSWORD_SHAREPREFERENCE, input_pass);

        if (result == null)
            return;

        List<RcuInfo> rcuInfos = new ArrayList<>();
        for (int i = 0; i < result.getData().size(); i++) {
            RcuInfo rcuInfo = new RcuInfo();
            rcuInfo.setCanCpuName(result.getData().get(i).getCanCpuName());
            rcuInfo.setDevUnitID(result.getData().get(i).getDevUnitID());
            rcuInfo.setDevUnitPass(result.getData().get(i).getDevPass());
            rcuInfos.add(rcuInfo);
        }
//        List<RcuInfo> json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
//        }.getType());
        AppSharePreferenceMgr.put(this, GlobalVars.RCUINFOLIST_SHAREPREFERENCE, gson.toJson(rcuInfos));

    }

}
