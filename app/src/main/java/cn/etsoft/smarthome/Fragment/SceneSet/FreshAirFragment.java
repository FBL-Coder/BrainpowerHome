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
import cn.etsoft.smarthome.Adapter.GridView.SceneSet_FreshAir_Adapter;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;

/**
 * Author：FBL  Time： 2017/8/29.
 * 情景设置   地暖   fragment
 */

public class FreshAirFragment extends BaseFragment {
    private GridView mGirdview;
    private String mRoomName = "全部";
    private List<WareFreshAir> mfloorHeat_Room;
    private String DEVS_ALL_ROOM = "全部";
    private SceneSet_FreshAir_Adapter mAdapter;
    private int mScenePosition = 0;
    private ImageView mSceneSet_IsSelectDev;
    private boolean IsShowSelect = false;
    private TextView mNull_tv;

    @Override
    protected void initView() {
        mGirdview = findViewById(R.id.SceneSet_Fragment_GridView);
        mSceneSet_IsSelectDev = findViewById(R.id.SceneSet_IsSelectDev);
        mNull_tv = findViewById(R.id.null_tv);
        mGirdview.setEmptyView(mNull_tv);
    }

    @Override
    public void initData(Bundle arguments) {
        mRoomName = arguments.getString("RoomName", "全部");
        initDev();
    }

    private void initDev() {
        List<WareFreshAir> freshAirs = MyApplication.getWareData().getFreshAirs();
        //房间内的灯集合
        mfloorHeat_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(mRoomName)) {
            mfloorHeat_Room.addAll(freshAirs);
        } else {
            for (int i = 0; i < freshAirs.size(); i++) {
                if (freshAirs.get(i).getDev().getRoomName().equals(mRoomName)) {
                    mfloorHeat_Room.add(freshAirs.get(i));
                }
            }
        }
        if (mAdapter == null) {
            mAdapter = new SceneSet_FreshAir_Adapter(mScenePosition, mActivity, mfloorHeat_Room,IsShowSelect);
            mGirdview.setAdapter(mAdapter);
        } else
            mAdapter.notifyDataSetChanged(mfloorHeat_Room,mScenePosition,IsShowSelect);

    }

    @Override
    protected void setListener() {
        SceneSetActivity.setmSceneSetSceneClickListener(new SceneSetActivity.SceneSetSceneClickListener() {
            @Override
            public void SceneClickPosition(int ScenePosition, int DevType, String RoomName) {
                mScenePosition = ScenePosition;
                mRoomName = RoomName;
                initDev();
            }
        });

        mSceneSet_IsSelectDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getWareData().getSceneEvents().size() == 0)
                    return;
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


    public void onHiddenChanged(boolean hidden) {

        super.onHiddenChanged(hidden);
        if (!hidden) {// 不在最前端界面显示
            mRoomName = SceneSetHelper.getRoomName();
            initDev();
        }
    }
}
