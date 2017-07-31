package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.ControlActivity;
import cn.etsoft.smarthome.Activity.Settings.SceneSetActivity;
import cn.etsoft.smarthome.Adapter.GridView.Control_Tv_Adapter;
import cn.etsoft.smarthome.Adapter.GridView.SceneSet_TV_Adapter;
import cn.etsoft.smarthome.Domain.WareTv;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——电视
 */

public class TVControlFragment extends BaseFragment {

    private GridView mSceneSet_Girdview;
    private Control_Tv_Adapter mTvAdapter;
    private String mRoomName = "全部";
    private List<WareTv> mTv_Room;
    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mSceneSet_Girdview = findViewById(R.id.Control_Fragment_GridView);
        mSceneSet_Girdview.setBackgroundResource(R.drawable.ic_launcher);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
//        initDev();
    }

    public void initDev() {
        List<WareTv> tvs;
        tvs = MyApplication.getWareData().getTvs();
        //房间内的灯集合
        mTv_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mTv_Room.addAll(tvs);
        } else {
            for (int i = 0; i < tvs.size(); i++) {
                if (tvs.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mTv_Room.add(tvs.get(i));
                }
            }
        }
        if (mTvAdapter == null) {
            mTvAdapter = new Control_Tv_Adapter( mActivity, mTv_Room);
            mSceneSet_Girdview.setAdapter(mTvAdapter);
        } else
            mTvAdapter.notifyDataSetChanged(mTv_Room);
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
