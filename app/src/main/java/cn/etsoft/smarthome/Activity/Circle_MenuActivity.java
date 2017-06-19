package cn.etsoft.smarthome.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：FBL  Time： 2017/6/9.
 */

public class Circle_MenuActivity extends Activity {
    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);
        layout = (CircleMenuLayout) findViewById(R.id.RL_view);
        initData();
        layout.Init();
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        initEvent();


    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {

                Toast.makeText(Circle_MenuActivity.this, Data_InnerCircleList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                Toast.makeText(Circle_MenuActivity.this, Data_OuterCircleList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 初始化转盘数据
     */
    private void initData() {
        Data_OuterCircleList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            if (i == 0 || i == 8) event.setTitle("机顶盒");
            if (i == 1 || i == 9) event.setTitle("灯光");
            if (i == 2 || i == 10) event.setTitle("空调");
            if (i == 3 || i == 11) event.setTitle("插座");
            if (i == 4 || i == 12) event.setTitle("门禁");
            if (i == 5 || i == 13) event.setTitle("监控");
            if (i == 6 || i == 14) event.setTitle("窗帘");
            if (i == 7) event.setTitle("电视");
            Data_OuterCircleList.add(event);
        }
        Data_InnerCircleList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            if (i == 0 || i == 8) event.setTitle("客厅");
            if (i == 1 || i == 9) event.setTitle("厨房");
            if (i == 2 || i == 10) event.setTitle("卫生间");
            if (i == 3 || i == 11) event.setTitle("卧室");
            if (i == 4 || i == 12) event.setTitle("走廊");
            if (i == 5 || i == 13) event.setTitle("阳台");
            if (i == 6 || i == 14) event.setTitle("餐厅");
            if (i == 7) event.setTitle("楼梯");
            Data_InnerCircleList.add(event);
        }
    }
}
