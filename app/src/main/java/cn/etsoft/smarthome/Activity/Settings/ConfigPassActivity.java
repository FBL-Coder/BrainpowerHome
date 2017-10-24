package cn.etsoft.smarthome.Activity.Settings;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.GlobalVars;


/**
 * Author：FBL  Time： 2017/10/19.
 * 配置密码 帮助页面(修改密码，重置密码)
 */

public class ConfigPassActivity extends Activity implements View.OnClickListener {

    private ImageView mCancle;
    private TextView mChangePass;
    private TextView mResetPass;
    private TextView mConfigHelpTvOldpass;
    private EditText mConfigHelpEtOldpass;
    private EditText mConfigHelpNewpass;
    private TextView mConfigHelpOk;
    private LinearLayout mConfigHelpLlChange;

    private int selectnum = 0, CHANGE = 1, RESET = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_pass_help);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mCancle = (ImageView) findViewById(R.id.Cancle);
        mCancle.setOnClickListener(this);
        mChangePass = (TextView) findViewById(R.id.ChangePass);
        mChangePass.setOnClickListener(this);
        mResetPass = (TextView) findViewById(R.id.ResetPass);
        mResetPass.setOnClickListener(this);
        mConfigHelpTvOldpass = (TextView) findViewById(R.id.Config_Help_tv_oldpass);
        mConfigHelpEtOldpass = (EditText) findViewById(R.id.Config_Help_et_oldpass);
        mConfigHelpNewpass = (EditText) findViewById(R.id.Config_Help_newpass);
        mConfigHelpOk = (TextView) findViewById(R.id.Config_Help_ok);
        mConfigHelpOk.setOnClickListener(this);
        mConfigHelpLlChange = (LinearLayout) findViewById(R.id.Config_Help_ll_Change);
        initSelect();
    }

    private void initSelect() {
        if (selectnum != 0)
            mConfigHelpLlChange.setVisibility(View.VISIBLE);
        if (selectnum == CHANGE) {
            mConfigHelpTvOldpass.setText("旧密码：");
            mConfigHelpEtOldpass.setHint("请输入旧密码");
            mConfigHelpEtOldpass.setInputType(InputType.TYPE_CLASS_NUMBER);
            mChangePass.setBackgroundResource(R.color.main_blue1);
            mChangePass.setTextColor(Color.WHITE);
            mResetPass.setBackgroundColor(Color.WHITE);
            mResetPass.setTextColor(getResources().getColor(R.color.main_blue1));
        } else if (selectnum == RESET) {
            mConfigHelpTvOldpass.setText("登陆密码：");
            mConfigHelpEtOldpass.setHint("请输入登陆密码");
            mConfigHelpEtOldpass.setInputType(InputType.TYPE_NULL);
            mResetPass.setBackgroundResource(R.color.main_blue1);
            mResetPass.setTextColor(Color.WHITE);
            mChangePass.setBackgroundColor(Color.WHITE);
            mChangePass.setTextColor(getResources().getColor(R.color.main_blue1));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Cancle:
                finish();
                break;
            case R.id.ChangePass:
                selectnum = CHANGE;
                initSelect();
                break;
            case R.id.ResetPass:
                selectnum = RESET;
                initSelect();
                break;
            case R.id.Config_Help_ok:
                if ("".equals(mConfigHelpEtOldpass.getText().toString())
                        || mConfigHelpEtOldpass.getText().toString().length() < 5
                        || "".equals(mConfigHelpNewpass.getText().toString())
                        || mConfigHelpNewpass.getText().toString().length() < 5) {
                    ToastUtil.showText("密码至少5位");
                    return;
                }
                if (selectnum == CHANGE) {
                    if (mConfigHelpEtOldpass.getText().toString()
                            .equals(AppSharePreferenceMgr
                                    .get(GlobalVars.CONFIG_PASS_SHAREPREFERENCE, ""))) {
                        AppSharePreferenceMgr.put(GlobalVars.CONFIG_PASS_SHAREPREFERENCE,
                                mConfigHelpNewpass.getText().toString());
                        succeedDialog((String) AppSharePreferenceMgr
                                .get(GlobalVars.CONFIG_PASS_SHAREPREFERENCE, ""),CHANGE);
                    } else {
                        ToastUtil.showText("旧密码不正确，请重新输入！");
                    }
                } else if (selectnum == RESET) {
                    if (mConfigHelpEtOldpass.getText().toString()
                            .equals(AppSharePreferenceMgr
                                    .get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""))) {
                        AppSharePreferenceMgr.put(GlobalVars.CONFIG_PASS_SHAREPREFERENCE,
                                mConfigHelpNewpass.getText().toString());
                        succeedDialog((String) AppSharePreferenceMgr
                                .get(GlobalVars.CONFIG_PASS_SHAREPREFERENCE, ""),RESET);
                    } else {
                        ToastUtil.showText("登陆密码不正确，请重新输入！");
                    }
                }
                break;
        }
    }

    public void succeedDialog(String pass, int Flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (Flag == CHANGE)
            builder.setMessage("密码修改成功\n" + pass);
        if (Flag == RESET)
            builder.setMessage("密码重置成功\n" + pass);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
}
