package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.ControlActivity;
import cn.etsoft.smarthome.Adapter.GridView.Control_Air_Adapter;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.ControlHelper;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制——空调
 */

public class AirControlFragment extends BaseFragment {

    private GridView mAir_Girdview;
    private Control_Air_Adapter mAirAdapter;
    private String mRoomName = "全部";
    private List<WareAirCondDev> mAir_Room;
    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mAir_Girdview = findViewById(R.id.Control_Fragment_GridView);
        mAir_Girdview.setNumColumns(1);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 4)
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

        ControlActivity.setmControlDevClickListener(new ControlActivity.ControlDevClickListener() {
            @Override
            public void ControlClickPosition(int DevType, String RoomName) {
                mRoomName = RoomName;
                initDev();
            }
        });

        List<WareAirCondDev> airs;
        airs = MyApplication.getWareData().getAirConds();
        //房间内的灯集合
        mAir_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mAir_Room.addAll(airs);
        } else {
            for (int i = 0; i < airs.size(); i++) {
                if (airs.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mAir_Room.add(airs.get(i));
                }
            }
        }
        if (mAirAdapter == null) {
            mAirAdapter = new Control_Air_Adapter( mActivity, mAir_Room);
            mAir_Girdview.setAdapter(mAirAdapter);
        } else
            mAirAdapter.notifyDataSetChanged(mAir_Room);
        TextView nulltv= findViewById(R.id.null_tv);
        mAir_Girdview.setEmptyView(nulltv);
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = ControlHelper.getRoomName();
            initDev();
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_control;
    }

}

