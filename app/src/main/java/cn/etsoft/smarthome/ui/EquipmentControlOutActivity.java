package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/8/26.
 */
public class EquipmentControlOutActivity extends Activity {
    private TextView mTitle;
    private ScrollView sv;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_out);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        //initView();
        //初始化ListView
        //initListView();
    }
    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText("新增按键控制");
    }
}
