package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2017/3/14.
 */
public class UserActivity extends Activity {

    private TextView mTitle;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //初始化标题栏
        initTitleBar();
    }
    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText(getIntent().getStringExtra("title"));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
