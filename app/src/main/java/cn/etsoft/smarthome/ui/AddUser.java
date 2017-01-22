package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by fbl on 16-12-23.
 */
public class AddUser extends Activity implements View.OnClickListener {

    TextView back, add_ok;
    EditText add_username, add_password;
    int ADDOK = 0;
    String id;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initView();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_add_user.getValue()) {
                    if (MyApplication.getWareData().getLogin_result() == ADDOK) {
                        ToastUtil.showToast(AddUser.this, "注册成功");
                        Bundle bundle = new Bundle();
                        bundle.putString("id",id);
                        bundle.putString("pass",pass);
                        setResult(0,getIntent().putExtra("bundle",bundle));
                        finish();
                    } else {
                        ToastUtil.showToast(AddUser.this, "注册失败");
                    }
                }
            }
        });
    }

    private void initView() {
        back = (TextView) findViewById(R.id.back);
        add_ok = (TextView) findViewById(R.id.add_ok);
        add_username = (EditText) findViewById(R.id.add_username);
        add_password = (EditText) findViewById(R.id.add_password);
        back.setOnClickListener(this);
        add_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add_ok:
                id = add_username.getText().toString();
                pass = add_password.getText().toString();

                if ("".equals(id) || "".equals(pass)) {
                    ToastUtil.showToast(this, "请输入账号和密码！");
                    return;
                }
                //        发送：
//        {
//            "userName": "hwp",
//                "passwd": "000000",
//                "datType": 61,
//                "subType1": 0,
//                "subType2": 0
//        }
//        返回：
//        {
//            "userName": "hwp",
//                "passwd": "000000",
//                "datType": 61,
//                "subType1": 0,
//                "subType2": 0/1   //0表示成功，1表示失败
//        }

                MyApplication.addUserData(id, pass);
                break;
        }
    }
}
