package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/10/13.
 */
public class RoomActivity extends Activity{
    private ImageView back;
    private TextView title;
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
        private void initTitleBar(){
            back = (ImageView) findViewById(R.id.back);
            title = (TextView) findViewById(R.id.tv_home);
            title.setText(getIntent().getStringExtra("title"));
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
}
