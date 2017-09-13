package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.ControlActivity;
import cn.etsoft.smarthome.Adapter.GridView.Control_FloorHeat_Adapter;
import cn.etsoft.smarthome.Adapter.GridView.Control_FreshAir_Adapter;
import cn.etsoft.smarthome.Adapter.GridView.Control_Light_Adapter;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/8/29.
 * 地暖控制 fragment
 */

public class FloorHeatFragment extends BaseFragment {
    private GridView mGirdview;
    private String mRoomName = "全部";
    private List<WareFloorHeat> mfloorHeat_Room;
    private String DEVS_ALL_ROOM = "全部";
    private Control_FloorHeat_Adapter mAdapter;

    @Override
    protected void initView() {
        mGirdview = findViewById(R.id.Control_Fragment_GridView);


        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if ((datType == 3 && subtype2 == 9) || (datType == 6 && subtype2 == 1))
                    MyApplication.mApplication.dismissLoadDialog();
                initDev();
            }
        });
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        initDev();
    }

    private void initDev() {
        List<WareFloorHeat> floorHeats = MyApplication.getWareData().getFloorHeat();
        //房间内的灯集合
        mfloorHeat_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mfloorHeat_Room.addAll(floorHeats);
        } else {
            for (int i = 0; i < floorHeats.size(); i++) {
                if (floorHeats.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mfloorHeat_Room.add(floorHeats.get(i));
                }
            }
        }
        if (mAdapter == null) {
            mAdapter = new Control_FloorHeat_Adapter(mActivity, mfloorHeat_Room);
            mGirdview.setAdapter(mAdapter);
        } else
            mAdapter.notifyDataSetChanged(mfloorHeat_Room);

    }

    @Override
    protected void setListener() {
        ControlActivity.setControlDevListener(new ControlActivity.ControlDevListener() {
            @Override
            public void UpData(String roomname) {
                mRoomName = roomname;
                initDev();
            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_control;
    }


    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SceneSetHelper.getRoomName();
            initDev();
        }
    }
}
