package cn.etsoft.smarthome.Fragment.SetAddDev;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.AdvancedSetting.SceneSetActivity;
import cn.etsoft.smarthome.Activity.AdvancedSetting.SetAddDevActivity;
import cn.etsoft.smarthome.Adapter.GridView.SceneSet_Curtain_Adapter;
import cn.etsoft.smarthome.Adapter.GridView.SetAddDev_Curtain_Adapter;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.UiHelper.SetAddDevHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——窗帘
 */

public class CurtarnSetAddDevFragment extends BaseFragment {

    private GridView mSceneSet_Girdview;
    private ImageView mSceneSet_IsSelectDev;
    private SetAddDev_Curtain_Adapter mCurtainAdapter;
    private int mScenePosition = 0, mDevType = 0;
    private String mRoomName = "全部";
    private List<WareCurtain> mCurtain_Room;
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

        List<WareCurtain> Curtains;
        Curtains = MyApplication.getWareData().getCurtains();
        //房间内的灯集合
        mCurtain_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mCurtain_Room.addAll(Curtains);
        } else {
            for (int i = 0; i < Curtains.size(); i++) {
                if (Curtains.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mCurtain_Room.add(Curtains.get(i));
                }
            }
        }
        if (mCurtainAdapter == null) {
            mCurtainAdapter = new SetAddDev_Curtain_Adapter(SetAddDevHelper.getAddDevs(), mActivity, mCurtain_Room, IsShowSelect);
            mSceneSet_Girdview.setAdapter(mCurtainAdapter);
        } else
            mCurtainAdapter.notifyDataSetChanged(mCurtain_Room, SetAddDevHelper.getAddDevs(), IsShowSelect);
    }

    @Override
    protected void setListener() {
        mSceneSet_IsSelectDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsShowSelect = !IsShowSelect;
                if (IsShowSelect)
                    mSceneSet_IsSelectDev.setImageResource(R.drawable.ic_launcher);
                else mSceneSet_IsSelectDev.setImageResource(R.drawable.ic_launcher_round);
                initDev();

            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_sceneset;
    }


    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mRoomName = SetAddDevHelper.getRoomName();
            initDev();
        }
    }
}

