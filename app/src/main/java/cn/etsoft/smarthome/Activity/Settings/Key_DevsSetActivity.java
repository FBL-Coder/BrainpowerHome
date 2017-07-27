package cn.etsoft.smarthome.Activity.Settings;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Key_DevsSetAdapter;
import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Adapter.RecyclerView.Key_Devs_KeysAdapter;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Key_DevsSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;
import cn.etsoft.smarthome.View.SlideGridView;

/**
 * Author：FBL  Time： 2017/6/22.
 * 按键配设备 页面 ——输入页面
 */

public class Key_DevsSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private RecyclerView mKeyDevs_Keys;
    private SlideGridView mKeyDevs_Devs;
    private TextView mKeyDevs_KeyBoards, mKeyDevs_TestBtn, mKeyDevs_SaveBtn;
    //设备适配器
    private Key_DevsSetAdapter mKeyDevsKeysAdapter;
    //按键适配器
    private Key_Devs_KeysAdapter KeysAdapter;
    private PopupWindow popupWindow;
    private int DevType = -1;
    private String RoomName = "";
    private boolean IsNoData = true;
    private boolean OuterCircleClick = false;

    //按键名
    private List<String> keyName_list;
    //输入板名
    private List<String> InputKey_names;
    //输入板ID
    private String CanCupID;

    //房间内指定类型设备
    private List<WareDev> mRoomDevs;


    private int position_keyinput = 0, devPosition = 0, KeyPosition = 0;

    @Override
    public void initView() {
        setLayout(R.layout.activity_key_devs_set);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();

                if (datType == 11) {
                    IsNoData = false;
                    Key_DevsSetHelper.setInput_key_data(MyApplication.getWareData().getKeyOpItems());
                }
                if (datType == 12 && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    ToastUtil.showText("保存成功");
                    //保存成功之后将备用数据结果置空
                    MyApplication.getWareData().setResult(null);
                }
            }
        });

        mKeyDevs_KeyBoards = getViewById(R.id.Key_DevsSet_KeyBoards);
        mKeyDevs_TestBtn = getViewById(R.id.Key_DevsSet_Test_Btn);
        mKeyDevs_SaveBtn = getViewById(R.id.Key_DevsSet_Save_Btn);
        mKeyDevs_Devs = getViewById(R.id.Key_DevsSet_Keys);

        mKeyDevs_KeyBoards.setOnClickListener(this);
        mKeyDevs_TestBtn.setOnClickListener(this);
        mKeyDevs_SaveBtn.setOnClickListener(this);

        mKeyDevs_Keys = getViewById(R.id.Key_DevsSet_Devs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mKeyDevs_Keys.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public void initData() {
        WareDataHliper.initCopyWareData().startCopySceneData();
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        if (MyApplication.getWareData().getKeyInputs().size() == 0) {
            mKeyDevs_KeyBoards.setText("无");
            return;
        }

        initRecycleView();
        layout = getViewById(R.id.Key_DevsSet_CircleMenu);
        Data_OuterCircleList = Key_DevsSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = Key_DevsSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        InputKey_names = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().size(); i++) {
            InputKey_names.add(MyApplication.getWareData().getKeyInputs().get(i).getBoardName());
        }
        //设置按键板名称
        if (InputKey_names.size() > 0)
            mKeyDevs_KeyBoards.setText(InputKey_names.get(0));
        RecyclerViewClick();
        initEvent();
    }


    private void initRecycleView() {
        MyApplication.mApplication.showLoadDialog(Key_DevsSetActivity.this);
        int KeyCnt = MyApplication.getWareData().getKeyInputs().get(position_keyinput).getKeyCnt();
        String[] keyName = MyApplication.getWareData().getKeyInputs().get(position_keyinput).getKeyName();
        CanCupID = MyApplication.getWareData().getKeyInputs().get(position_keyinput).getCanCpuID();
        //获取输入板对应设备的数据
        SendDataUtil.getKeyItemInfo(0, CanCupID);
        if (keyName.length == 0)
            return;
        //按键名称集合
        keyName_list = new ArrayList<>();
        if (KeyCnt > keyName.length) {
            for (int i = 0; i < KeyCnt; i++) {
                if (i >= keyName.length) {
                    keyName_list.add("按键" + i);
                } else {
                    keyName_list.add(keyName[i]);
                }
            }
        } else {
            for (int i = 0; i < KeyCnt; i++) {
                keyName_list.add(keyName[i]);
            }
        }
        Log.e("按键名称", String.valueOf(keyName_list));
        KeysAdapter = new Key_Devs_KeysAdapter(keyName_list);
        mKeyDevs_Keys.setAdapter(KeysAdapter);
    }

    @Override
    public void onClick(View v) {
        if (IsNoData) {
            ToastUtil.showText("数据未加载成功，不可操作！");
            return;
        }
        if (mRoomDevs == null || mRoomDevs.size() == 0 || DevType == -1) {
            ToastUtil.showText("请选择房间和设备类型");
            return;
        }
        switch (v.getId()) {
            case R.id.Key_DevsSet_KeyBoards: //按键板
                initRadioPopupWindow(v, InputKey_names);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.Key_DevsSet_Test_Btn: // 测试
                break;
            case R.id.Key_DevsSet_Save_Btn: // 保存
                List<WareDev> devs = new ArrayList<>();
                for (int i = 0; i < mRoomDevs.size(); i++) {
                    if (mRoomDevs.get(i).getType() == DevType)
                        devs.add(mRoomDevs.get(i));
                }
                Key_DevsSetHelper.Save(this, CanCupID, position_keyinput, devs);
                break;
        }
    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                if (IsNoData) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                RoomName = Data_InnerCircleList.get(position).getTitle();
                Key_DevsSetHelper.setRoomName(RoomName);
                mRoomDevs = Key_DevsSetHelper.getRoomDev(RoomName);

                if (OuterCircleClick) {
                    List<WareDev> devs = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            devs.add(mRoomDevs.get(i));
                    }
                    if (mKeyDevsKeysAdapter == null)
                        mKeyDevsKeysAdapter = new Key_DevsSetAdapter(DevType, position_keyinput, KeyPosition,
                                Key_DevsSetActivity.this, devs, false);
                    else
                        mKeyDevsKeysAdapter.notifyDataSetChanged(DevType, position_keyinput, KeyPosition, Key_DevsSetActivity.this, devs, false);
                    mKeyDevs_Devs.setAdapter(mKeyDevsKeysAdapter);
                }
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                if (IsNoData) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                if ("".equals(RoomName)) {
                    return;
                }
                OuterCircleClick = true;
                DevType = position % 8;

                List<WareDev> devs = new ArrayList<>();
                for (int i = 0; i < mRoomDevs.size(); i++) {
                    if (mRoomDevs.get(i).getType() == DevType)
                        devs.add(mRoomDevs.get(i));
                }
                if (mKeyDevsKeysAdapter == null)
                    mKeyDevsKeysAdapter = new Key_DevsSetAdapter(DevType, position_keyinput, KeyPosition, Key_DevsSetActivity.this, devs, false);
                else
                    mKeyDevsKeysAdapter.notifyDataSetChanged(DevType, position_keyinput, KeyPosition, Key_DevsSetActivity.this, devs, false);
                mKeyDevs_Devs.setAdapter(mKeyDevsKeysAdapter);
            }
        });
    }

    private void RecyclerViewClick() {
        KeysAdapter.setOnItemClick(new Key_Devs_KeysAdapter.Dev_KeysSetViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                KeyPosition = position;
                SendDataUtil.getKeyItemInfo(position, CanCupID);
                MyApplication.mApplication.showLoadDialog(Key_DevsSetActivity.this);
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.listview_popupwindow_equipment, null);
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
        popupWindow = new PopupWindow(customView, view_parent.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                popupWindow.dismiss();
                position_keyinput = position;
                initRecycleView();
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


}
