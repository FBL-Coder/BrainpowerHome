package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.EquipmentAdapter;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class EquipmentControlActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView equipment, room;
    private TextView mTitle, TvEquipment, TvRoom;
    private ScrollView sv;
    private ListView lv;
    private String[] title = {"灯光7", "灯光8", "灯光9", "灯光10"};
    private int[] image = {R.drawable.select, R.drawable.select, R.drawable.selected, R.drawable.select,};
    private PopupWindow popupWindow;
    private String[] text = {"电视", "灯光", "空调", "窗帘"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        initView();
        //初始化ListView
        initListView();
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
        equipment = (ImageView) findViewById(R.id.equipment_lamplight);
        room = (ImageView) findViewById(R.id.equipment_parlour);
        TvEquipment = (TextView) findViewById(R.id.tv_equipment_lamplight);
        TvRoom = (TextView) findViewById(R.id.tv_equipment_parlour);
        equipment.setOnClickListener(this);
        room.setOnClickListener(this);
        TvEquipment.setText(text[0]);
        TvRoom.setText(text[0]);
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        lv = (ListView) findViewById(R.id.equipment_lv);
        sv = (ScrollView) findViewById(R.id.equipment_sv);
        sv.smoothScrollTo(0, 0);
        EquipmentAdapter adapter = new EquipmentAdapter(title, image, this);
        lv.setAdapter(adapter);
    }

    /**
     * 初始化自定义PopupWindow
     */
    private void initPopupWindow() {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv),
                200, 325
        );
        popupWindow.setContentView(customView);
        lv = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        //popupwindow页面之外可点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TvEquipment.setText(text[position]);
        TvRoom.setText(text[position]);
        popupWindow.dismiss();
    }


    @Override
    public void onClick(View v) {
        int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 11;
        switch (v.getId()) {
            case R.id.equipment_lamplight:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow();
                    popupWindow.showAsDropDown(v, -widthOff, 0);
                }
                break;
            case R.id.equipment_parlour:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow();
                    popupWindow.showAsDropDown(v, -widthOff, 0);
                }
                break;
        }

    }
}
