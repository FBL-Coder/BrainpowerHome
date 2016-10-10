package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.IClick;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.adapter.SwipeAdapter;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SwipeListView;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class EquipmentDeployActivity extends Activity implements View.OnClickListener {
    private TextView mTitle, add;
    private ListView lv1;
    private SwipeListView lv = null;
    private PopupWindow popupWindow;
    private String[] text = {"控制命令1", "控制命令2", "控制命令3", "控制命令4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipmentdeploy_listview);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        initView();
        //初始化listView
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
        add = (TextView) findViewById(R.id.equipment_out_tv);
        add.setOnClickListener(this);
    }

    /**
     * 初始化listView
     */
    private void initListView() {
        lv = (SwipeListView) findViewById(R.id.equipment_out_lv);
        SwipeAdapter adapter = new SwipeAdapter(this, mListener);
        lv.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.equipment_out_tv:
                startActivity(new Intent(EquipmentDeployActivity.this, AddActivity.class));
                break;
        }
    }

    /**
     * 初始化自定义PopupWindow
     */
    private void initPopupWindow() {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv),200,325);
        popupWindow.setContentView(customView);
        lv1 = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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

    /**
     * 实现类，响应按钮点击事件
     */
    private IClick mListener = new IClick() {
        @Override
        public void listViewItemClick(int position, View v) {
            int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 20;
            switch (v.getId()) {
                case R.id.deploy_choose:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    } else {
                        initPopupWindow();
                        popupWindow.showAsDropDown(v, -widthOff, 0);
                    }
                    break;
                case R.id.deploy_delete:
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(EquipmentDeployActivity.this);
                    builder.setTitle("提示 :");
                    builder.setMessage("您确定要删除此条目吗？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };
}
