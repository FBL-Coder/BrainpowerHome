package cn.etsoft.smarthome.Fragment.SetAddDev;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.AdvancedSetting.SetAddDevActivity;
import cn.etsoft.smarthome.Adapter.GridView.SetAddDev_Light_Adapter;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SetAddDevHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——灯光
 */

public class LightSetAddDevFragment extends BaseFragment {

    private GridView mSceneSet_Light;
    private ImageView mSceneSet_IsSelectDev;
    private SetAddDev_Light_Adapter mLightAdapter;
    private int mScenePosition = 0, mDevType = 0;
    private String mRoomName = "全部";
    private List<WareLight> mLight_Room;
    private boolean IsShowSelect = false;

    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mSceneSet_Light = findViewById(R.id.SceneSet_Fragment_GridView);
        mSceneSet_IsSelectDev = findViewById(R.id.SceneSet_IsSelectDev);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        initDev();
    }

    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SetAddDevHelper.getRoomName();
            initDev();
        }
    }
    private void initDev() {
        SetAddDevActivity.setmSetAddDevCircleClickListener(new SetAddDevActivity.SetAddDevCircleClickListener() {
            @Override
            public void SetAddDevClickPosition(int DevType, String RoomName) {
                mRoomName = RoomName;
                initDev();
            }
        });


        List<WareLight> lights;
        lights = MyApplication.getWareData().getLights();
        //房间内的灯集合
        mLight_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mLight_Room.addAll(lights);
        } else {
            for (int i = 0; i < lights.size(); i++) {
                if (lights.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mLight_Room.add(lights.get(i));
                }
            }
        }
        if (mLightAdapter == null) {
            mLightAdapter = new SetAddDev_Light_Adapter(SetAddDevHelper.getAddDevs(), mActivity, mLight_Room, IsShowSelect);
            mSceneSet_Light.setAdapter(mLightAdapter);
        } else
            mLightAdapter.notifyDataSetChanged(mLight_Room, SetAddDevHelper.getAddDevs(), IsShowSelect);
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

}

