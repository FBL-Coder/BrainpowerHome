package cn.etsoft.smarthome.Fragment.SceneSet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.AdvancedSetting.SceneSetActivity;
import cn.etsoft.smarthome.Adapter.GridView.SceneSet_Light_Adapter;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/26.
 */

public class LightSceneFragment extends BaseFragment {

    private GridView mSceneSet_Light;
    private SceneSet_Light_Adapter mLightAdapter;
    private SceneSet_Light_Handler mHandler = new SceneSet_Light_Handler(this);
    private int mScenePosition = 0, mDevType = 0, mRoomPositon = 0;
    private WareData mWareData_Copy;
    private List<WareLight> mLight_Room;

    private int DEVS_ALL_ROOM = 100;


    @Override
    protected void initView() {

        MyApplication.getWareData_Copy(mHandler);
        mSceneSet_Light = findViewById(R.id.SceneSet_Light);
    }

    @Override
    public void initData(Bundle arguments) {

        SceneSetActivity.setmSceneSetSceneClickListener(new SceneSetActivity.SceneSetSceneClickListener() {
            @Override
            public void SceneClickPosition(int ScenePosition, int DevType, int RoomPositon) {
                mScenePosition = ScenePosition;
                mDevType = DevType;
                mRoomPositon = RoomPositon;
            }
        });
    }

    private void initDev() {
        List<WareLight> lights;
        List<String> room_list;
        lights = mWareData_Copy.getLights();
        //房间内的灯集合
        mLight_Room = new ArrayList<>();
        //房间集合
        room_list = mWareData_Copy.getRooms();
        //根据房间id获取设备；
        if (mRoomPositon == DEVS_ALL_ROOM) {
            mLight_Room = lights;
        } else {
            for (int i = 0; i < lights.size(); i++) {
                if (lights.get(i).getDev().getRoomName().equals(room_list.get(mRoomPositon))) {
                    mLight_Room.add(lights.get(i));
                }
            }
        }

        mLightAdapter = new SceneSet_Light_Adapter(mScenePosition, mActivity,mLight_Room,false);
        mSceneSet_Light.setAdapter(mLightAdapter);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_sceneset_light;
    }

    static class SceneSet_Light_Handler extends Handler {
        WeakReference<LightSceneFragment> weakReference;

        public SceneSet_Light_Handler(LightSceneFragment fragment) {
            weakReference = new WeakReference<>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null) {
                if (msg.what == MyApplication.mApplication.WAREDATA_COPY) {
                    weakReference.get().mWareData_Copy = (WareData) msg.obj;
                    weakReference.get().initDev();
                }
            }
        }
    }
}
