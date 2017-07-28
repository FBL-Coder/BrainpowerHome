package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Control_Curtain_Adapter;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制—窗帘
 */

public class CurtarnControlFragment extends BaseFragment {

    private GridView mSceneSet_Girdview;
    private Control_Curtain_Adapter mCurtainAdapter;
    private String mRoomName = "全部";
    private List<WareCurtain> mCurtain_Room;
    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mSceneSet_Girdview = findViewById(R.id.Control_Fragment_GridView);
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
            mCurtainAdapter = new Control_Curtain_Adapter(mActivity, mCurtain_Room);
            mSceneSet_Girdview.setAdapter(mCurtainAdapter);
        } else
            mCurtainAdapter.notifyDataSetChanged(mCurtain_Room);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_control;
    }


    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mRoomName = SceneSetHelper.getRoomName();
            initDev();
        }
    }
}

