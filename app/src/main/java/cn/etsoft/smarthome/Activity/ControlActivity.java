package cn.etsoft.smarthome.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.net.Socket;
import java.util.List;

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
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设备控制页面
 */

public class ControlActivity extends BaseActivity {

    private CircleMenuLayout layout;
    private RelativeLayout CircleMenuLayout_RL, SceneSet_Info;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private Fragment mLightFragment, mAirFragment,
            mTVFragment, mTvUpFragment, mCurFragment, mVideoFragment, mDoorFragment,
            mFreshAirFragment, mSocketFragment, mFloorHeatFragment;
    private int DevType = 3, OutCircleposition = 0;
    private String RoomName = "";
    private TextView DevType_TV, RoomName_TV;
    private ImageView Control_Back;

    @Override
    public void initView() {
        setLayout(R.layout.activity_control);
    }

    @Override
    public void initData() {

        layout = getViewById(R.id.SceneSet_CircleMenu);
        CircleMenuLayout_RL = getViewById(R.id.CircleMenuLayout_RL);
        SceneSet_Info = getViewById(R.id.SceneSet_Info);
        Control_Back = getViewById(R.id.Control_Back);
        DevType_TV = getViewById(R.id.DevType);
        RoomName_TV = getViewById(R.id.RoomName);
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
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);
        initEvent();


    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                if (mControlDevClickListener != null)
                    mControlDevClickListener.ControlClickPosition(DevType, Data_InnerCircleList.get(position).getTitle());
                RoomName = Data_InnerCircleList.get(position).getTitle();
                ControlHelper.setRoomName(RoomName);
                RoomName_TV.setText(RoomName);
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                DevType = position;
                OuterCircleClick(ControlActivity.this, position, ControlHelper.getRoomName());
                if (mControlDevClickListener != null)
                    mControlDevClickListener.ControlClickPosition(position, ControlHelper.getRoomName());
                if (DevType == 0)
                    DevType_TV.setText("空调");
                if (DevType == 1)
                    DevType_TV.setText("电视");
                if (DevType == 2)
                    DevType_TV.setText("机顶盒");
                if (DevType == 3)
                    DevType_TV.setText("灯光");
                if (DevType == 4)
                    DevType_TV.setText("窗帘");
                if (DevType == 7)
                    DevType_TV.setText("新风");
                if (DevType == 8)
                    DevType_TV.setText("插座");
                if (DevType == 9)
                    DevType_TV.setText("地暖");
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        RoomName = "全部";
        ControlHelper.setRoomName(RoomName);
        bundle.putString("RoomName", "全部");
        mLightFragment = new LightControlFragment();
        mLightFragment.setArguments(bundle);
        transaction.replace(R.id.SceneSet_Info, mLightFragment);
        transaction.commit();
        DevType = 3;
        DevType_TV.setText("灯光");
        RoomName_TV.setText("全部");
    }

    /**
     * 外圆菜单 点击事件
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
                mTVFragment = new TVControlFragment();
                mTVFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mTVFragment);
                break;
            case 2:
                mTvUpFragment = new TvUpControlFragment();
                mTvUpFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mTvUpFragment);
                break;
            case 3:
                mLightFragment = new LightControlFragment();
                mLightFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mLightFragment);
                break;
            case 4:
                mCurFragment = new CurtarnControlFragment();
                mCurFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mCurFragment);
                break;
            case 5:
                mVideoFragment = new VideoControlFragment();
                mVideoFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mVideoFragment);
                break;
            case 6:
                mDoorFragment = new DoorControlFragment();
                mDoorFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mDoorFragment);
                break;
            case 7:
                mFreshAirFragment = new FreshAirFragment();
                mFreshAirFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mFreshAirFragment);
                break;
            case 8:
                mSocketFragment = new SocketFragment();
                mSocketFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mSocketFragment);
                break;
            case 9:
                mFloorHeatFragment = new FloorHeatFragment();
                mFloorHeatFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mFloorHeatFragment);
                break;

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
