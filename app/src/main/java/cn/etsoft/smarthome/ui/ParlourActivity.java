package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.GridViewAdapter2;
import cn.etsoft.smarthome.adapter.ParlourGridViewAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.widget.CustomDialog;

/**
 * Created by Say GoBay on 2016/8/24.
 */
public class ParlourActivity extends Activity {
    private TextView mTitle;
    private GridView gridView;
    private String[] text = {"窗帘", "空调", "电视", "新风", "门锁", "阀门"};
    private int[] pic1 = {R.drawable.blind2, R.drawable.blind4, R.drawable.blind5};
    private String[] des1 = {"全开", "半开", "全关"};
    private int[] pic2 = {R.drawable.air_choose1, R.drawable.air_wind1, R.drawable.air_makecool1, R.drawable.air_makehot2, R.drawable.air_high2, R.drawable.air_middle1, R.drawable.air_low1};
    private String[] des2 = {"开关", "扫风", "制冷", "制热", "风速高", "风速中", "风速低"};
    private Button save;
    private ImageView dialog_back;
    private ParlourGridViewAdapter parlourGridViewAdapter;
    private List<WareDev> listViewItems;
    private String RoomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parlour);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化标题栏
        initTitleBar();
        //初始化GridView
        initGridView();

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                initGridView();
                super.handleMessage(msg);
            }
        };
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData() {
                Message message = mHandler.obtainMessage();
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        RoomName = getIntent().getStringExtra("title");
        mTitle.setText(RoomName);
    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        gridView = (GridView) findViewById(R.id.parlour_gv);
        listViewItems = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            if (RoomName.equals(MyApplication.getWareData().getDevs().get(i).getRoomName()))
                listViewItems.add(MyApplication.getWareData().getDevs().get(i));
            else
                continue;
        }

        if (parlourGridViewAdapter != null) {
            parlourGridViewAdapter.notifyDataSetChanged();
        } else {
            parlourGridViewAdapter = new ParlourGridViewAdapter(this, listViewItems);
            gridView.setAdapter(parlourGridViewAdapter);
        }
        gridView.setSelector(R.drawable.selector_gridview_item);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getDialog(position);
            }
        });
    }


    /**
     * 初始化自定义dialog
     */
    CustomDialog dialog;

    public void getDialog(int position) {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_parlour_curtain);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        //初始化DialogGridView
        initDialogGridView(position);
    }

    /**
     * 初始化DialogGridView
     */
    private void initDialogGridView(int position) {
        gridView = (GridView) dialog.findViewById(R.id.parlour_dialog_gv);
        switch (position) {
            case 0:
                gridView.setAdapter(new GridViewAdapter2(pic1, des1, this));
                break;
            case 1:
                gridView.setAdapter(new GridViewAdapter2(pic2, des2, this));
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
        gridView.setSelector(R.drawable.selector_gridview_item);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        dialog_back = (ImageView) dialog.findViewById(R.id.dialog_back);
        dialog_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
