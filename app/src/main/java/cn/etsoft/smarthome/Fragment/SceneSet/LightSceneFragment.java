package cn.etsoft.smarthome.Fragment.SceneSet;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.Settings.SceneSetActivity;
import cn.etsoft.smarthome.Adapter.GridView.SceneSet_Light_Adapter;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——灯光
 */

public class LightSceneFragment extends BaseFragment {

    private GridView mSceneSet_Light;
    private ImageView mSceneSet_IsSelectDev;
    private SceneSet_Light_Adapter mLightAdapter;
    private int mScenePosition = 0, mDevType = 0;
    private String mRoomName = "全部";
    private List<WareLight> mLight_Room;
    private boolean IsShowSelect = false;

    private String DEVS_ALL_ROOM = "全部";
    private TextView mNull_tv;


    @Override
    protected void initView() {
        mSceneSet_Light = findViewById(R.id.SceneSet_Fragment_GridView);
        mSceneSet_IsSelectDev = findViewById(R.id.SceneSet_IsSelectDev);
        mNull_tv = findViewById(R.id.null_tv);
        mSceneSet_Light.setEmptyView(mNull_tv);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        mScenePosition = arguments.getInt("ScenePosition", 0);
        initDev();
    }

    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SceneSetHelper.getRoomName();
            initDev();
        }
    }
    private void initDev() {
        SceneSetActivity.setmSceneSetSceneClickListener(new SceneSetActivity.SceneSetSceneClickListener() {
            @Override
            public void SceneClickPosition(int ScenePosition, int DevType, String RoomName) {
                mScenePosition = ScenePosition;
                mDevType = DevType;
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
            mLightAdapter = new SceneSet_Light_Adapter(mScenePosition, mActivity, mLight_Room, IsShowSelect);
            mSceneSet_Light.setAdapter(mLightAdapter);
        } else
            mLightAdapter.notifyDataSetChanged(mLight_Room, mScenePosition, IsShowSelect);
    }

    @Override
    protected void setListener() {
        mSceneSet_IsSelectDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsShowSelect = !IsShowSelect;
                if (IsShowSelect)
                    mSceneSet_IsSelectDev.setImageResource(R.drawable.show_on);
                else mSceneSet_IsSelectDev.setImageResource(R.drawable.show_off);
                initDev();

            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_sceneset;
    }

}

