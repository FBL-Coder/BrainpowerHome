package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.adapter_group.ListView_LeftAdapter;
import cn.etsoft.smarthome.adapter_group.ListView_RightAdapter;
import cn.etsoft.smarthome.domain.PrintCmd;
import cn.etsoft.smarthome.fragment_group.InPutFragment;
import cn.etsoft.smarthome.fragment_group.OutPutFragment;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2017/3/17.
 */
public class GroupActivity extends FragmentActivity implements View.OnClickListener {

    private TextView deploy_input, deploy_output;
    private ListView input_listView, output_listView;
    private ListView_LeftAdapter ListView_LeftAdapter;
    private ListView_RightAdapter ListView_RightAdapter;
    private PopupWindow popupWindow;
    private List<String> input_name;
    private List<String> board_name;
    private int input_position = 0;
    private int board_position = 0;
    private List<WareDev> devs;
    private WareDev wareDev;
    private FragmentTransaction transaction;
    private Fragment inputFragment, outputFragment;
    private boolean IsLeftClick = false, IsRightClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group2);
        MyApplication.mInstance.setGroupActivity(GroupActivity.this);
        //初始化控件
        initView();
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (MyApplication.getWareData().getKeyInputs().size() == 0 && MyApplication.getWareData().getBoardChnouts().size() == 0) {
            deploy_input.setText("无");
            deploy_output.setText("无");
            ToastUtil.showToast(GroupActivity.this,"没有数据");
            return;
        }
        input_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().size(); i++) {
            input_name.add(MyApplication.getWareData().getKeyInputs().get(i).getBoardName());
        }
        board_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getBoardChnouts().size(); i++) {
            board_name.add(MyApplication.getWareData().getBoardChnouts().get(i).getBoardName());
        }
        if (input_name.size() > 0)
            deploy_input.setText(input_name.get(0));
        if (board_name.size() > 0)
            deploy_output.setText(board_name.get(0));
        //输入板ListView
        initListView_left();
        //输出板ListView
        initListView_right();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        deploy_input = (TextView) findViewById(R.id.deploy_input);
        deploy_output = (TextView) findViewById(R.id.deploy_output);
        input_listView = (ListView) findViewById(R.id.input_listView);
        output_listView = (ListView) findViewById(R.id.output_listView);
        deploy_input.setOnClickListener(this);
        deploy_output.setOnClickListener(this);
    }

    /**
     * 输入板ListView
     */
    List<String> keyname_list;
    private void initListView_left() {
        String[] keyname = MyApplication.getWareData().getKeyInputs().get(input_position).getKeyName();
        final String uid = MyApplication.getWareData().getKeyInputs().get(input_position).getDevUnitID();
        if (keyname.length == 0)
            return;
        keyname_list = new ArrayList<>();
        for (int i = 0; i < keyname.length; i++) {
            keyname_list.add(keyname[i]);
        }
        ListView_LeftAdapter = new ListView_LeftAdapter(this, keyname_list);
        //默认选中条目
        ListView_LeftAdapter.changeSelected(0);
        input_listView.setAdapter(ListView_LeftAdapter);
        input_listView.setItemsCanFocus(true);//让ListView的item获得焦点
        input_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式
        transaction = getSupportFragmentManager().beginTransaction();
        IsLeftClick = true;
        inputFragment = new InPutFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("key_index", 0);
        bundle.putString("uid", uid);
        inputFragment.setArguments(bundle);
        transaction.replace(R.id.deploy, inputFragment);
        transaction.commit();

        //点击监听
        input_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IsLeftClick = true;
                ListView_LeftAdapter.changeSelected(position);//刷新
                inputFragment = new InPutFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("key_index", position);
                bundle.putString("uid", uid);
                transaction = getSupportFragmentManager().beginTransaction();
                inputFragment.setArguments(bundle);
                transaction.replace(R.id.deploy, inputFragment);
                transaction.commit();
            }
        });
        //房间ListView选中监听
        input_listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListView_LeftAdapter.changeSelected(position);//刷新
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //输出模块添加按键
        input_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (IsRightClick) {
                    PrintCmd item = new PrintCmd();
                    item.setIndex(position);
                    item.setKeyname(keyname_list.get(position));
                    item.setKeyAct_num(2);
                    item.setKey_cmd(0);
                    item.setDevType(wareDev.getType());
                    item.setDevUnitID(wareDev.getCanCpuId());
                    onLongClick_outputData.AddDevs(item, position);
                }
                return true;
            }
        });
    }


    public OnLongClick_OutputData onLongClick_outputData;

    public void setOnLongClick_outputData(OnLongClick_OutputData onLongClick_outputData) {
        this.onLongClick_outputData = onLongClick_outputData;
    }

    public interface OnLongClick_OutputData {
        void AddDevs(PrintCmd item, int index);
    }


    public OnLongClick_inputData onLongClick_inputData;

    public void setOnLongClick_inputData(OnLongClick_inputData onLongClick_inputData) {
        this.onLongClick_inputData = onLongClick_inputData;
    }

    public interface OnLongClick_inputData {
        void AddInput(WareKeyOpItem item);
    }

    List<String> LTitle;
    String uid;

    /**
     * 输出板ListView
     */
    private void initListView_right() {
        uid = MyApplication.getWareData().getBoardChnouts().get(board_position).getDevUnitID();
        LTitle = new ArrayList<>();
        int size = MyApplication.getWareData().getDevs().size();
        if (size > 0) {
            devs = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (uid.equals(MyApplication.getWareData().getDevs().get(i).getCanCpuId())) {
                    LTitle.add(MyApplication.getWareData().getDevs().get(i).getDevName());
                    devs.add(MyApplication.getWareData().getDevs().get(i));
                }
            }
        }
        ListView_RightAdapter = new ListView_RightAdapter(this, LTitle);
        //默认选中条目
        ListView_RightAdapter.changeSelected(0);
        output_listView.setAdapter(ListView_RightAdapter);
        output_listView.setItemsCanFocus(true);//让ListView的item获得焦点
        output_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式
        //点击监听
        output_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IsRightClick = true;
                ListView_RightAdapter.changeSelected(position);//刷新
                wareDev = devs.get(position);
                outputFragment = new OutPutFragment();
                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                bundle.putInt("devType", devs.get(position).getType());
                bundle.putInt("devID", devs.get(position).getDevId());
                transaction = getSupportFragmentManager().beginTransaction();
                outputFragment.setArguments(bundle);
                transaction.replace(R.id.deploy, outputFragment);
                transaction.commit();
            }
        });
        //房间ListView选中监听
        output_listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListView_RightAdapter.changeSelected(position);//刷新
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //输入模块添加设备
        output_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (IsLeftClick) {
                    WareKeyOpItem item = new WareKeyOpItem();
                    item.setDevId((byte) devs.get(position).getDevId());
                    item.setDevType((byte) devs.get(position).getType());
                    item.setDevUnitID(devs.get(position).getCanCpuId());
                    item.setKeyOpCmd((byte) 0);
                    item.setKeyOp((byte) 3);
                    onLongClick_inputData.AddInput(item);
                }
                return true;
            }
        });
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int tag) {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        customView.setFocusable(true);
        customView.setFocusableInTouchMode(true);
        customView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 200, 300);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (tag == 1) {
                    input_position = position;
                    initListView_left();
                } else {
                    board_position = position;
                    initListView_right();
                }
                popupWindow.dismiss();
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

    @Override
    public void onClick(View v) {
        int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
        switch (v.getId()) {
            case R.id.deploy_input:
                initPopupWindow(deploy_input, input_name, 1);
                popupWindow.showAsDropDown(v, -widthOff, 0);
                break;
            case R.id.deploy_output:
                initPopupWindow(deploy_output, board_name, 2);
                popupWindow.showAsDropDown(v, -widthOff, 0);
                break;
        }
    }
}
