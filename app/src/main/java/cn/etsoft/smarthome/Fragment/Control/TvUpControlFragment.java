package cn.etsoft.smarthome.Fragment.Control;

import android.os.Bundle;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.ControlActivity;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.ControlHelper;

/**
 * Author：FBL  Time： 2017/6/29.
 */

public class TvUpControlFragment extends BaseFragment {
    private GridView mSceneSet_Girdview;
    private String mRoomName = "全部";
    private List<WareSetBox> mTvUp_Room;
    private String DEVS_ALL_ROOM = "全部";

    @Override
    protected void initView() {
        mSceneSet_Girdview = findViewById(R.id.Control_Fragment_GridView);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
//        initDev();
    }

    private void initDev() {
        ControlActivity.setmControlDevClickListener(new ControlActivity.ControlDevClickListener() {
            @Override
            public void ControlClickPosition(int DevType, String RoomName) {
                mRoomName = RoomName;
                initDev();
            }
        });
        List<WareSetBox> TvUps;
        TvUps = MyApplication.getWareData().getStbs();
        //房间内的灯集合
        mTvUp_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mTvUp_Room.addAll(TvUps);
        } else {
            for (int i = 0; i < TvUps.size(); i++) {
                if (TvUps.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mTvUp_Room.add(TvUps.get(i));
                }
            }
        }
//        if (mTvUpAdapter == null) {
//            mTvUpAdapter = new SceneSet_TvUp_Adapter(mScenePosition, mActivity, mTvUp_Room, IsShowSelect);
//            mSceneSet_Girdview.setAdapter(mTvUpAdapter);
//        } else
//            mTvUpAdapter.notifyDataSetChanged(mTvUp_Room, mScenePosition, IsShowSelect);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_control;
    }


    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = ControlHelper.getRoomName();
            initDev();
        }
    }
}
