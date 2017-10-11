package cn.etsoft.smarthome.Activity.Settings;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.DevInfosAdapter;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设备信息
 */

public class DevInfoActivity extends BaseActivity {

    private CircleMenuLayout layout;
    private GridView DevInfoGridView;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private int DevType = -1;
    private int roomSize = -1;
    private String RoomName = "";
    private DevInfosAdapter adapter;
    private boolean OuterCircleClick = false;
    private List<WareDev> mRoomDevs;
    private String DEVS_ALL_ROOM = "全部";
    private TextView mDevInfoAddDevs, mDevInfoNullData;

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                // 数据返回处理
                if (datType == 5 || datType == 6 || datType == 7) {
                    MyApplication.mApplication.dismissLoadDialog();
                    if (subtype2 != 1) {
                        ToastUtil.showText("操作失败");
                        return;
                    }
                    ToastUtil.showText("操作成功");
                    mRoomDevs = getRoomDev(RoomName);
                    if (mRoomDevs == null) return;
                    List<WareDev> gridviewDev = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            gridviewDev.add(mRoomDevs.get(i));
                    }
                    if (adapter == null) {
                        adapter = new DevInfosAdapter(gridviewDev, DevInfoActivity.this);
                        DevInfoGridView.setAdapter(adapter);
                    } else
                        adapter.notifyDataSetChanged(gridviewDev);
                }
                if (datType == 3 || datType == 4 || datType == 35
                        || (datType == 7 || subtype2 == 1)||(datType == 9 && subtype2 == 1)){
                    mRoomDevs = getRoomDev(RoomName);
                    if (mRoomDevs == null) return;
                    List<WareDev> gridviewDev = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            gridviewDev.add(mRoomDevs.get(i));
                    }
                    if (adapter == null) {
                        adapter = new DevInfosAdapter(gridviewDev, DevInfoActivity.this);
                        DevInfoGridView.setAdapter(adapter);
                    } else
                        adapter.notifyDataSetChanged(gridviewDev);
                }
            }
        });
    }

    @Override
    public void initView() {
        setLayout(R.layout.activity_devinfo);
        DevInfoGridView = getViewById(R.id.DevInfo_Info);
        mDevInfoAddDevs = getViewById(R.id.Dev_Info_AddDevs);
        mDevInfoNullData = getViewById(R.id.Dev_Info_NullData);
        DevInfoGridView.setEmptyView(mDevInfoNullData);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                // 数据返回处理
                if (datType == 5 || datType == 6 || datType == 7) {
                    MyApplication.mApplication.dismissLoadDialog();
                    if (subtype2 != 1) {
                        ToastUtil.showText("操作失败");
                        return;
                    }
                    ToastUtil.showText("操作成功");
                    mRoomDevs = getRoomDev(RoomName);
                    if (mRoomDevs == null) return;
                    List<WareDev> gridviewDev = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            gridviewDev.add(mRoomDevs.get(i));
                    }
                    if (adapter == null) {
                        adapter = new DevInfosAdapter(gridviewDev, DevInfoActivity.this);
                        DevInfoGridView.setAdapter(adapter);
                    } else
                        adapter.notifyDataSetChanged(gridviewDev);
                }
            }
        });
    }

    @Override
    public void initData() {
        WareDataHliper.initCopyWareData().startCopySceneData();
        mDevInfoAddDevs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomSize = MyApplication.getWareData().getRooms().size();
                startActivityForResult(new Intent(DevInfoActivity.this, AddDevActivity.class), 0);
            }
        });
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        layout = getViewById(R.id.DevInfo_CircleMenu);
        Data_OuterCircleList = SceneSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = SceneSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);
        if (DevType == -1 || "".equals(RoomName))
            mDevInfoNullData.setText("请先选择房间和设备类型");
        initEvent();
    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                RoomName = Data_InnerCircleList.get(position).getTitle();
                //内圈点击
                mRoomDevs = getRoomDev(RoomName);
                if (mRoomDevs == null) return;
                mDevInfoNullData.setText("没有数据");
                if (OuterCircleClick) {
                    List<WareDev> gridviewDev = new ArrayList<>();
                    for (int i = 0; i < mRoomDevs.size(); i++) {
                        if (mRoomDevs.get(i).getType() == DevType)
                            gridviewDev.add(mRoomDevs.get(i));
                    }
                    if (adapter == null) {
                        adapter = new DevInfosAdapter(gridviewDev, DevInfoActivity.this);
                        DevInfoGridView.setAdapter(adapter);
                    } else
                        adapter.notifyDataSetChanged(gridviewDev);
                }
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                DevType = position % 10;
                OuterCircleClick = true;
                if ("".equals(RoomName)) {
                    return;
                }
                mDevInfoNullData.setText("没有数据");
                mRoomDevs = getRoomDev(RoomName);
                if (mRoomDevs == null) return;
                List<WareDev> gridviewDev = new ArrayList<>();
                for (int i = 0; i < mRoomDevs.size(); i++) {
                    if (mRoomDevs.get(i).getType() == DevType)
                        gridviewDev.add(mRoomDevs.get(i));
                }
                if (adapter == null) {
                    adapter = new DevInfosAdapter(gridviewDev, DevInfoActivity.this);
                    DevInfoGridView.setAdapter(adapter);
                } else
                    adapter.notifyDataSetChanged(gridviewDev);
            }
        });
    }

    public List<WareDev> getRoomDev(String roomname) {
        List<WareDev> devs = new ArrayList<>();
        devs.addAll(MyApplication.getWareData().getDevs());
        //房间内的设备集合
        List<WareDev> RoomDevs = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(roomname)) {
            RoomDevs.addAll(devs);
        } else {
            for (int i = 0; i < devs.size(); i++) {
                if (devs.get(i).getRoomName().equals(roomname)) {
                    RoomDevs.add(devs.get(i));
                }
            }
        }
        return RoomDevs;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("DEV", "onActivityResult: ");

        if (roomSize < MyApplication.getWareData().getRooms().size()) {
            initData();
            DevInfoGridView.setAdapter(null);
            ToastUtil.showText("房间增加，请重新选择房间和类型");
        } else {
            OuterCircleClick = true;
            if ("".equals(RoomName)) {
                return;
            }
            mDevInfoNullData.setText("没有数据");
            mRoomDevs = getRoomDev(RoomName);
            if (mRoomDevs == null) return;
            List<WareDev> gridviewDev = new ArrayList<>();
            for (int i = 0; i < mRoomDevs.size(); i++) {
                if (mRoomDevs.get(i).getType() == DevType)
                    gridviewDev.add(mRoomDevs.get(i));
            }
            if (adapter == null) {
                adapter = new DevInfosAdapter(gridviewDev, DevInfoActivity.this);
                DevInfoGridView.setAdapter(adapter);
            } else
                adapter.notifyDataSetChanged(gridviewDev);
        }
    }
}
