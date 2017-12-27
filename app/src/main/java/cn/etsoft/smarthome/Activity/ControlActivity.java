package cn.etsoft.smarthome.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Adapter.ListView.Control_dev_room_Adapter;
import cn.etsoft.smarthome.Adapter.RecyclerView.Control_Dev_TypeAdapter;
import cn.etsoft.smarthome.Fragment.Control.AirControlFragment;
import cn.etsoft.smarthome.Fragment.Control.CurtarnControlFragment;
import cn.etsoft.smarthome.Fragment.Control.DoorControlFragment;
import cn.etsoft.smarthome.Fragment.Control.FloorHeatFragment;
import cn.etsoft.smarthome.Fragment.Control.FreshAirFragment;
import cn.etsoft.smarthome.Fragment.Control.LightControlFragment;
import cn.etsoft.smarthome.Fragment.Control.SocketFragment;
import cn.etsoft.smarthome.Fragment.Control.TVControlFragment;
import cn.etsoft.smarthome.Fragment.Control.TvUpControlFragment;
import cn.etsoft.smarthome.Fragment.Control.VideoControlFragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.ControlHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设备控制页面
 */

public class ControlActivity extends BaseActivity {

    private RelativeLayout CircleMenuLayout_RL, SceneSet_Info;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private Fragment mLightFragment, mAirFragment,
            mTVFragment, mTvUpFragment, mCurFragment, mVideoFragment, mDoorFragment,
            mFreshAirFragment, mSocketFragment, mFloorHeatFragment;
    private int DevType = 3, OutCircleposition = 0;
    private String RoomName = "";
    private ImageView Control_Back;
    private RecyclerView recyclerView;
    private ListView listView;
    private Control_Dev_TypeAdapter typeAdapter;
    private Control_dev_room_Adapter room_adapter;

    @Override
    public void initView() {
        setLayout(R.layout.activity_control);
    }

    @Override
    public void initData() {

        recyclerView = getViewById(R.id.control_dev_type);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        listView = getViewById(R.id.control_list_room);
        SceneSet_Info = getViewById(R.id.SceneSet_Info);
        Control_Back = getViewById(R.id.Control_Back);
        Data_OuterCircleList = ControlHelper.initSceneCircleOUterData();
        Data_InnerCircleList = ControlHelper.initSceneCircleInnerData();

        Control_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WareDataHliper.initCopyWareData().startCopySceneData();
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }

        initEvent();
    }

    private void initEvent() {

        if (typeAdapter == null) {
            typeAdapter = new Control_Dev_TypeAdapter(Data_OuterCircleList);
            recyclerView.setAdapter(typeAdapter);
        } else typeAdapter.upData(Data_OuterCircleList);

        if (room_adapter == null) {
            room_adapter = new Control_dev_room_Adapter(Data_InnerCircleList, this);
            listView.setAdapter(room_adapter);
        } else room_adapter.notifyDataSetChanged(Data_InnerCircleList);

        typeAdapter.setOnItemClick(new Control_Dev_TypeAdapter.AdapterViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                DevType = position;
                OuterCircleClick(ControlActivity.this, position, ControlHelper.getRoomName());
                if (mControlDevClickListener != null)
                    mControlDevClickListener.ControlClickPosition(position, ControlHelper.getRoomName());
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mControlDevClickListener != null)
                    mControlDevClickListener.ControlClickPosition(DevType, Data_InnerCircleList.get(i).getTitle());
                RoomName = Data_InnerCircleList.get(i).getTitle();
                ControlHelper.setRoomName(RoomName);
                room_adapter.selected(i);
                room_adapter.notifyDataSetChanged(Data_InnerCircleList);
            }
        });
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        RoomName = "全部";
        ControlHelper.setRoomName(RoomName);
        bundle.putString("RoomName", "全部");
        room_adapter.selected(0);
        mLightFragment = new LightControlFragment();
        typeAdapter.setselectItem(1);
        mLightFragment.setArguments(bundle);
        transaction.replace(R.id.SceneSet_Info, mLightFragment);
        transaction.commit();
        DevType = 1;
    }

    /**
     * item点击事件
     */
    public void OuterCircleClick(FragmentActivity activity, int position, String RoomName) {
        if ("".equals(RoomName)) {
            ToastUtil.showText("请先选择房间");
            return;
        }
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("RoomName", RoomName);

        switch (position) {
            case 0:
                mAirFragment = new AirControlFragment();
                mAirFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mAirFragment);
                break;
            case 1:
                mLightFragment = new LightControlFragment();
                mLightFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mLightFragment);
                break;
            case 2:
                mCurFragment = new CurtarnControlFragment();
                mCurFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mCurFragment);
                break;
            case 3:
                mFreshAirFragment = new FreshAirFragment();
                mFreshAirFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mFreshAirFragment);
                break;
            case 4:
                mFloorHeatFragment = new FloorHeatFragment();
                mFloorHeatFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mFloorHeatFragment);
                break;

//            case 5:
//                mTVFragment = new TVControlFragment();
//                mTVFragment.setArguments(bundle);
//                transaction.replace(R.id.SceneSet_Info, mTVFragment);
//                break;
//            case 6:
//                mSocketFragment = new SocketFragment();
//                mSocketFragment.setArguments(bundle);
//                transaction.replace(R.id.SceneSet_Info, mSocketFragment);
//                break;
//            case 7:
//                mTvUpFragment = new TvUpControlFragment();
//                mTvUpFragment.setArguments(bundle);
//                transaction.replace(R.id.SceneSet_Info, mTvUpFragment);
//                break;
//            case 8:
//                mDoorFragment = new DoorControlFragment();
//                mDoorFragment.setArguments(bundle);
//                transaction.replace(R.id.SceneSet_Info, mDoorFragment);
//                break;
//            case 9:
//                mDoorFragment = new DoorControlFragment();
//                mDoorFragment.setArguments(bundle);
//                transaction.replace(R.id.SceneSet_Info, mDoorFragment);
//                break;
        }
        transaction.commit();
    }

    //点击情景，房间，设备。触发回调，刷新界面
    public static ControlDevClickListener mControlDevClickListener;

    public static void setmControlDevClickListener(ControlDevClickListener mControlDevClickListener) {
        ControlActivity.mControlDevClickListener = mControlDevClickListener;
    }

    public interface ControlDevClickListener {
        void ControlClickPosition(int DevType, String RoomName);
    }
}
