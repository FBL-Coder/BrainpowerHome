package cn.etsoft.smarthome.Activity.AdvancedSetting;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Dev_Keys_KeysAdapter;
import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Adapter.RecyclerView.Dev_KeysSet_DevsAdapter;
import cn.etsoft.smarthome.Domain.Out_List_printcmd;
import cn.etsoft.smarthome.Domain.PrintCmd;
import cn.etsoft.smarthome.Domain.WareBoardChnout;
import cn.etsoft.smarthome.Domain.WareBoardKeyInput;
import cn.etsoft.smarthome.Domain.WareChnOpItem;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareKeyOpItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Dev_KeysSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设备配按键 页面 ——输出页面
 */

public class Dev_KeysSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private RecyclerView mDevKeys_Devs;
    private GridView mDevKeys_Keys;
    private TextView mDevKeys_KeyBoards, mDevKeys_TestBtn, mDevKeys_SaveBtn;
    private Dev_KeysSet_DevsAdapter mDevKeysDevsAdapter;
    private PopupWindow popupWindow;
    private int DevType = 0;
    private String RoomName = "";
    private boolean IsNoData = true;
    private boolean OuterCircleClick = false;

    private List<WareBoardKeyInput> wareBoardKeyInputs;
    private List<String> keynames;

    private Dev_Keys_KeysAdapter devsAdapter;


    //房间内指定类型设备
    private List<WareDev> mRoomDevs;


    private int position_keyinput = 0, devPosition = 0;

    @Override
    public void initView() {
        setLayout(R.layout.activity_dev_keys_set);
        IsNoData = false;
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();

                if (datType == 14) {
                    IsNoData = false;
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            //TODO 刷新界面

                            if (devsAdapter == null)
                                devsAdapter = new Dev_Keys_KeysAdapter(Dev_KeysSetHelper.getListData_all(), Dev_KeysSetActivity.this, position_keyinput, false);
                            else
                                devsAdapter.notifyDataSetChanged(Dev_KeysSetHelper.getListData_all(), position_keyinput, false);
                            mDevKeys_Keys.setAdapter(devsAdapter);
                        }
                    };
                    Dev_KeysSetHelper.InitKeyData(handler, mRoomDevs, devPosition);
                }
                if (datType == 15 && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    ToastUtil.showText("保存成功");
                    MyApplication.getWareData().setResult(null);
                }
            }
        });


        mDevKeys_KeyBoards = getViewById(R.id.Dev_KeysSet_KeyBoards);
        mDevKeys_TestBtn = getViewById(R.id.Dev_KeysSet_Test_Btn);
        mDevKeys_SaveBtn = getViewById(R.id.Dev_KeysSet_Save_Btn);
        mDevKeys_Keys = getViewById(R.id.Dev_KeysSet_Keys);

        mDevKeys_KeyBoards.setOnClickListener(this);
        mDevKeys_TestBtn.setOnClickListener(this);
        mDevKeys_SaveBtn.setOnClickListener(this);

        mDevKeys_Devs = getViewById(R.id.Dev_KeysSet_Devs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDevKeys_Devs.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public void initData() {
        WareDataHliper.initCopyWareData().startCopySceneData();
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        layout = getViewById(R.id.Dev_KeysSet_CircleMenu);
        Data_OuterCircleList = Dev_KeysSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = Dev_KeysSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        wareBoardKeyInputs = MyApplication.getWareData().getKeyInputs();
        keynames = new ArrayList<>();
        for (int i = 0; i < wareBoardKeyInputs.size(); i++) {
            keynames.add(wareBoardKeyInputs.get(i).getBoardName());
        }

        initEvent();
    }

    @Override
    public void onClick(View v) {
        if (IsNoData) {
            ToastUtil.showText("数据未加载成功，不可操作！");
            return;
        }
        switch (v.getId()) {
            case R.id.Dev_KeysSet_KeyBoards: //按键板
                initRadioPopupWindow(v, keynames);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.Dev_KeysSet_Test_Btn: // 测试
                break;
            case R.id.Dev_KeysSet_Save_Btn: // 保存
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
                Dev_KeysSetHelper.setRoomName(RoomName);
                mRoomDevs = Dev_KeysSetHelper.getRoomDev(RoomName);

                if (OuterCircleClick) {
                    List<WareDev> RecyclerViewDev = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            RecyclerViewDev.add(mRoomDevs.get(i));
                    }
                    if (mDevKeysDevsAdapter == null)
                        mDevKeysDevsAdapter = new Dev_KeysSet_DevsAdapter(RecyclerViewDev);
                    else mDevKeysDevsAdapter.upData(RecyclerViewDev);
                    mDevKeys_Devs.setAdapter(mDevKeysDevsAdapter);
                    RecyclerViewClick();
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

                List<WareDev> RecyclerViewDev = new ArrayList<>();
                for (int i = 0; i < mRoomDevs.size(); i++) {
                    if (mRoomDevs.get(i).getType() == DevType)
                        RecyclerViewDev.add(mRoomDevs.get(i));
                }
                if (mDevKeysDevsAdapter == null)
                    mDevKeysDevsAdapter = new Dev_KeysSet_DevsAdapter(RecyclerViewDev);
                else mDevKeysDevsAdapter.upData(RecyclerViewDev);
                mDevKeys_Devs.setAdapter(mDevKeysDevsAdapter);
                RecyclerViewClick();
            }
        });
    }

    private void RecyclerViewClick() {
        mDevKeysDevsAdapter.setOnItemClick(new Dev_KeysSet_DevsAdapter.Dev_KeysSetViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                devPosition = position;
                SendDataUtil.getChnItemInfo(mRoomDevs.get(position));
            }

            @Override
            public void OnItemLongClick(View view, final int position) {

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
                if (devsAdapter == null)
                    devsAdapter = new Dev_Keys_KeysAdapter(Dev_KeysSetHelper.getListData_all(), Dev_KeysSetActivity.this, position_keyinput, false);
                else
                    devsAdapter.notifyDataSetChanged(Dev_KeysSetHelper.getListData_all(), position_keyinput, false);
                mDevKeys_Keys.setAdapter(devsAdapter);
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
