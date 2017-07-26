package cn.etsoft.smarthome.Activity.Settings;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

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
 * 设置情景页面
 */

public class DevInfoActivity extends BaseActivity {

    private CircleMenuLayout layout;
    private GridView DevInfoGridView;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private int DevType = 0;
    private String RoomName = "";
    private DevInfosAdapter adapter;
    private boolean OuterCircleClick = false;
    private List<WareDev> mRoomDevs;
    private String DEVS_ALL_ROOM = "全部";

    @Override
    public void initView() {
        setLayout(R.layout.activity_devinfo);
        DevInfoGridView = getViewById(R.id.DevInfo_Info);

        setTitleViewVisible(true, R.color.blue);
        setTitleImageBtn(true, R.drawable.back_image_select, true, R.drawable.ic_launcher);
        setTitleText("设备编辑",20,R.color.white);
        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                // TODO  数据返回处理
                if (datType == 6 || datType == 7) {
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
                DevType = position % 8;
                OuterCircleClick = true;
                if ("".equals(RoomName)) {
                    return;
                }
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
        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(DevInfoActivity.this, AddDevActivity.class), 0);
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
        initData();
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
}
