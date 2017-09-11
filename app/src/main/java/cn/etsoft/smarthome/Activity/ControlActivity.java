package cn.etsoft.smarthome.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Fragment.Control.AirControlFragment;
import cn.etsoft.smarthome.Fragment.Control.CurtarnControlFragment;
import cn.etsoft.smarthome.Fragment.Control.FreshAirFragment;
import cn.etsoft.smarthome.Fragment.Control.LightControlFragment;
import cn.etsoft.smarthome.Fragment.Control.TVControlFragment;
import cn.etsoft.smarthome.Fragment.Control.TvUpControlFragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设备控制页面
 */

public class ControlActivity extends BaseActivity {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private Fragment mLightFragment, mAirFragment, mTVFragment, mTvUpFragment, mCurFragment,mFreshAirFragment;
    private int DevType = 0, OutCircleposition = -1;
    private String RoomName = "";
    private TextView mNull_tv;

    @Override
    public void initView() {
        setLayout(R.layout.activity_control);
    }

    @Override
    public void initData() {
        WareDataHliper.initCopyWareData().startCopySceneData();
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        layout = getViewById(R.id.SceneSet_CircleMenu);
        mNull_tv = getViewById(R.id.null_tv);
        Data_OuterCircleList = SceneSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = SceneSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        if ("".equals(RoomName) || OutCircleposition == -1) {
            mNull_tv.setVisibility(View.VISIBLE);
        }
        initEvent();
    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                RoomName = Data_InnerCircleList.get(position).getTitle();
                SceneSetHelper.setRoomName(RoomName);
                mNull_tv.setVisibility(View.GONE);
                if (controlDevListenerList.size() != 0)
                    for (int i = 0; i < controlDevListenerList.size(); i++) {
                        controlDevListenerList.get(i).UpData(RoomName);
                    }
                OuterCircleClick(ControlActivity.this, DevType, RoomName);
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                OutCircleposition = position;
                mNull_tv.setVisibility(View.GONE);
                DevType = position % 10;
                OuterCircleClick(ControlActivity.this, DevType, RoomName);
            }
        });
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

        if (mTVFragment != null)
            transaction.hide(mTVFragment);
        if (mTvUpFragment != null)
            transaction.hide(mTvUpFragment);
        if (mCurFragment != null)
            transaction.hide(mCurFragment);
        if (mAirFragment != null)
            transaction.hide(mAirFragment);
        if (mLightFragment != null)
            transaction.hide(mLightFragment);
        if (mFreshAirFragment != null)
            transaction.hide(mFreshAirFragment);
        Bundle bundle = new Bundle();
        bundle.putString("RoomName", RoomName);
        switch (position) {
            case 0:
                if (mAirFragment == null) {
                    mAirFragment = new AirControlFragment();
                    mAirFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mAirFragment);
                } else transaction.show(mAirFragment);
                break;
            case 1:
                if (mTVFragment == null) {
                    mTVFragment = new TVControlFragment();
                    mTVFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mTVFragment);
                } else transaction.show(mTVFragment);
                break;
            case 2:
            case 5:
            case 6:
                if (mTvUpFragment == null) {
                    mTvUpFragment = new TvUpControlFragment();
                    mTvUpFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mTvUpFragment);
                } else transaction.show(mTvUpFragment);
                break;
            case 7:
                if (mFreshAirFragment == null) {
                    mFreshAirFragment = new FreshAirFragment();
                    mFreshAirFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mFreshAirFragment);
                } else transaction.show(mFreshAirFragment);
                break;
            case 3:
                if (mLightFragment == null) {
                    mLightFragment = new LightControlFragment();
                    mLightFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mLightFragment);
                } else transaction.show(mLightFragment);
                break;
            case 4:
                if (mCurFragment == null) {
                    mCurFragment = new CurtarnControlFragment();
                    mCurFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mCurFragment);
                } else transaction.show(mCurFragment);
                break;
        }
        transaction.commit();
    }

    public static ControlDevListener controlDevListener;

    public static List<ControlDevListener> controlDevListenerList = new ArrayList<>();

    public static void setControlDevListener(ControlDevListener controlDevListener) {
        ControlActivity.controlDevListener = controlDevListener;
        controlDevListenerList.add(controlDevListener);
    }

    public interface ControlDevListener {
        void UpData(String roomname);
    }
}
