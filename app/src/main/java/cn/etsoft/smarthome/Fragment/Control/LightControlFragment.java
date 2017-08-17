package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.ControlActivity;
import cn.etsoft.smarthome.Adapter.GridView.Control_Light_Adapter;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制——灯光
 */

public class LightControlFragment extends BaseFragment {

    private GridView mSceneSet_Light;
    private Control_Light_Adapter mLightAdapter;
    private String mRoomName = "全部";
    private List<WareLight> mLight_Room;
    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mSceneSet_Light = findViewById(R.id.Control_Fragment_GridView);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 4) {
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

    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SceneSetHelper.getRoomName();
            initDev();
        }
    }

    private void initDev() {

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
            mLightAdapter = new Control_Light_Adapter(mActivity, mLight_Room);
            mSceneSet_Light.setAdapter(mLightAdapter);
        } else
            mLightAdapter.notifyDataSetChanged(mLight_Room);

        TextView nulltv= findViewById(R.id.null_tv);
        mSceneSet_Light.setEmptyView(nulltv);
        mSceneSet_Light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                if (mLight_Room.get(position).getbOnOff() == 0)
                    SendDataUtil.controlDev(mLight_Room.get(position).getDev(), 0);
                else SendDataUtil.controlDev(mLight_Room.get(position).getDev(), 1);
            }
        });
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_control;
    }

}

