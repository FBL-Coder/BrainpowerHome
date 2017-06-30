package cn.etsoft.smarthome.Fragment.SetAddDev;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.AdvancedSetting.SetAddDevActivity;
import cn.etsoft.smarthome.Adapter.GridView.SetAddDev_Air_Adapter;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SetAddDevHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——空调
 */

public class AirSetAddDevFragment extends BaseFragment {

    private GridView mSceneSet_Girdview;
    private ImageView mSceneSet_IsSelectDev;
    private SetAddDev_Air_Adapter mAirAdapter;
    private String mRoomName = "全部";
    private List<WareAirCondDev> mAir_Room;
    private boolean IsShowSelect = false;

    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mSceneSet_Girdview = findViewById(R.id.SceneSet_Fragment_GridView);
        mSceneSet_IsSelectDev = findViewById(R.id.SceneSet_IsSelectDev);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        initDev();
    }

    private void initDev() {

        SetAddDevActivity.setmSetAddDevCircleClickListener(new SetAddDevActivity.SetAddDevCircleClickListener() {
            @Override
            public void SetAddDevClickPosition(int DevType, String RoomName) {
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
            mAirAdapter = new SetAddDev_Air_Adapter(SetAddDevHelper.getAddDevs(), mActivity, mAir_Room, IsShowSelect);
            mSceneSet_Girdview.setAdapter(mAirAdapter);
        } else
            mAirAdapter.notifyDataSetChanged(mAir_Room, SetAddDevHelper.getAddDevs(), IsShowSelect);
    }

    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SetAddDevHelper.getRoomName();
            initDev();
        }
    }

    @Override
    protected void setListener() {
        mSceneSet_IsSelectDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsShowSelect = !IsShowSelect;
                if (IsShowSelect)
                    mSceneSet_IsSelectDev.setImageResource(R.drawable.ic_launcher_round);
                else mSceneSet_IsSelectDev.setImageResource(R.drawable.ic_launcher);
                initDev();

            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_sceneset;
    }

}

