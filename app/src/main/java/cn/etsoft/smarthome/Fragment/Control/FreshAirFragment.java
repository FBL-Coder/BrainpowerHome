package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.ControlActivity;
import cn.etsoft.smarthome.Adapter.GridView.Control_FreshAir_Adapter;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/8/
 * 新风控制   Fragment
 *
 */

public class FreshAirFragment extends BaseFragment {
    private GridView mGirdview;
    private String mRoomName = "全部";
    private List<WareFreshAir> mFreshAir_Rooms;
    private String DEVS_ALL_ROOM = "全部";
    private Control_FreshAir_Adapter mAdapter;

    @Override
    protected void initView() {
        mGirdview = findViewById(R.id.Control_Fragment_GridView);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 3 && subtype2 == 7){
                    initDev();
                }
            }
        });
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        initDev();
    }

    private void initDev() {
        List<WareFreshAir> freshAirs;
        freshAirs = MyApplication.getWareData().getFreshAirs();
        //房间内的灯集合
        mFreshAir_Rooms = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mFreshAir_Rooms.addAll(freshAirs);
        } else {
            for (int i = 0; i < freshAirs.size(); i++) {
                if (freshAirs.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mFreshAir_Rooms.add(freshAirs.get(i));
                }
            }
        }
        if (mAdapter == null) {
            mAdapter = new Control_FreshAir_Adapter(mActivity, mFreshAir_Rooms);
            mGirdview.setAdapter(mAdapter);
        } else
            mAdapter.notifyDataSetChanged(mFreshAir_Rooms);
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
