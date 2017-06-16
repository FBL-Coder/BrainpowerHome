package cn.etsoft.smarthome.Activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.NewHttpPort;

/**
 * Author：FBL  Time： 2017/6/16.
 */

public class RegisterActivity extends BaseActivity {
    private EditText mRegisterId;
    private EditText mRegisterPass;
    private Button mRegisterBtn;

    @Override
    public void initView() {
        setLayout(R.layout.activity_register);
        mRegisterId = (EditText) findViewById(R.id.register_id);
        mRegisterPass = (EditText) findViewById(R.id.register_pass);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
    }

    @Override
    public void initData() {

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern id_rule = Pattern.compile("(^(13\\d|15[^4\\D]|17[13678]|18\\d)\\d{8}|170[^346\\D]\\d{7})$");
                Pattern pass_rule = Pattern.compile("\\w{6,12}");
                String id_input = mRegisterId.getText().toString();
                String pass_input = mRegisterPass.getText().toString();
                if (!(id_rule.matcher(id_input).matches()
                        && pass_rule.matcher(pass_input).matches())){
                    ToastUtil.showText("输入账号和密码不符合要求，请重新输入");
                    return;
                }

                Map<String ,String> param = new HashMap<>();
                param.put("userName",id_input);
                param.put("passwd",pass_input);
                OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.REGISTER, param, new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        Log.i("REGISTER",resultDesc.getResult());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        super.onFailure(code, message);
                    }
                });
            }
        });

    }

}
