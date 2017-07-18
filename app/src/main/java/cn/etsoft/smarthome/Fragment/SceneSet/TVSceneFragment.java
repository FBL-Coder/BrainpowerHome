package cn.etsoft.smarthome.Fragment.SceneSet;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseFragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Activity.Settings.SceneSetActivity;
import cn.etsoft.smarthome.Adapter.GridView.SceneSet_TV_Adapter;
import cn.etsoft.smarthome.Domain.WareTv;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/6/26.
 * 情景设置——电视
 */

public class TVSceneFragment extends BaseFragment {

    private GridView mSceneSet_Girdview;
    private ImageView mSceneSet_IsSelectDev;
    private SceneSet_TV_Adapter mTvAdapter;
    private int mScenePosition = 0, mDevType = 0;
    private String mRoomName = "全部";
    private List<WareTv> mTv_Room;
    private boolean IsShowSelect = false;

    private String DEVS_ALL_ROOM = "全部";


    @Override
    protected void initView() {
        mSceneSet_Girdview = findViewById(R.id.SceneSet_Fragment_GridView);
        mSceneSet_Girdview.setBackgroundResource(R.drawable.ic_launcher);
        mSceneSet_IsSelectDev = findViewById(R.id.SceneSet_IsSelectDev);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        mScenePosition = arguments.getInt("ScenePosition", 0);
//        initDev();
    }

    public void initDev() {
        SceneSetActivity.setmSceneSetSceneClickListener(new SceneSetActivity.SceneSetSceneClickListener() {
            @Override
            public void SceneClickPosition(int ScenePosition, int DevType, String RoomName) {
                mScenePosition = ScenePosition;
                mDevType = DevType;
                mRoomName = RoomName;
                initDev();
            }
        });
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
            mTvAdapter = new SceneSet_TV_Adapter(mScenePosition, mActivity, mTv_Room, IsShowSelect);
            mSceneSet_Girdview.setAdapter(mTvAdapter);
        } else
            mTvAdapter.notifyDataSetChanged(mTv_Room, mScenePosition, IsShowSelect);
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
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SceneSetHelper.getRoomName();
            initDev();

        }
    }
}
