package cn.semtec.community2.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.adapter.MyActAdapter;
import cn.semtec.community2.zxing.activity.CaptureActivity;

public class MyActivity extends MyBaseActivity implements View.OnClickListener {

    private View btn_back, btn_add;
    private AlertDialog dialog;
    private View layout;
    private ListView listView;
    private MyActAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        setView();
        setListener();
    }

    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        btn_add = findViewById(R.id.btn_add);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MyActAdapter(this);
        listView.setAdapter(adapter);
        initDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                dialog.show();
                dialog.getWindow().setContentView(layout);
                break;
            case R.id.btn_back:
                finish();
                break;

            case R.id.tab_1:
                dialog.dismiss();
                Intent intent2 = new Intent(this, HouseBindingOneActivity.class);
                startActivity(intent2);
                break;
            case R.id.tab_2:
                dialog.dismiss();
                Intent intent3 = new Intent(this, HouseBindingTwoActivity.class);
                startActivity(intent3);
                dialog.dismiss();
                break;
            case R.id.tab_3:
                Intent intent4 = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent4, 0);
                dialog.dismiss();
                break;
            case R.id.close:
                dialog.dismiss();
                break;
        }
    }

    private void initDialog() {
        layout = getLayoutInflater().inflate(R.layout.activity_my_dialog, null);
        dialog = new AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);
        layout.findViewById(R.id.tab_1).setOnClickListener(this);
        layout.findViewById(R.id.tab_2).setOnClickListener(this);
        layout.findViewById(R.id.tab_3).setOnClickListener(this);
        layout.findViewById(R.id.close).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            BaseActivity.instance.bindHouseByQRcode(scanResult);
        }
    }
}
