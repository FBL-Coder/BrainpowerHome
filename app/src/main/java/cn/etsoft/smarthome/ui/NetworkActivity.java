package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.widget.CustomDialog;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class NetworkActivity extends Activity implements View.OnClickListener{
    private TextView mTitle,networking;
    private ImageView add;
    private EditText name,id,pwd;
    private Button sure,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        initView();
    }
    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText(getIntent().getStringExtra("title"));
    }
    /**
     * 初始化控件
     */
    private void initView() {
        networking = (TextView) findViewById(R.id.networking_title);
        networking.setText(getIntent().getStringExtra("title"));
        add = (ImageView) findViewById(R.id.network_add);
        add.setOnClickListener(this);
    }
    /**
     * 初始化自定义dialog
     */
    CustomDialog dialog;
    public void getDialog() {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_network);
        dialog.show();
        name = (EditText) dialog.findViewById(R.id.network_et_name);
        id = (EditText) dialog.findViewById(R.id.network_et_id);
        pwd = (EditText) dialog.findViewById(R.id.network_et_pwd);
        sure = (Button) dialog.findViewById(R.id.network_btn_sure);
        cancel = (Button) dialog.findViewById(R.id.network_btn_cancel);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.network_add:
                getDialog();
                break;
            case R.id.network_btn_sure:
                break;
            case R.id.network_btn_cancel:
                dialog.dismiss();
                break;
        }

    }
}
