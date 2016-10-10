package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class RoomActivity extends Activity {
    private TextView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        //初始化标题栏
        initTitleBar();
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText(getIntent().getStringExtra("title"));
    }
}
