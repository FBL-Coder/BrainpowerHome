package cn.etsoft.smarthome.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Fragment.Control.AirControlFragment;
import cn.etsoft.smarthome.Fragment.Control.CurtarnControlFragment;
import cn.etsoft.smarthome.Fragment.Control.LightControlFragment;
import cn.etsoft.smarthome.Fragment.Control.TVControlFragment;
import cn.etsoft.smarthome.Fragment.Control.TvUpControlFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.TVSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.TvUpSceneFragment;
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
    private Fragment mLightFragment, mAirFragment, mTVFragment, mTvUpFragment, mCurFragment;
    private int DevType = 0, OutCircleposition = -1;
    private String RoomName = "";
    private boolean isCanClick = false;

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
        Data_OuterCircleList = SceneSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = SceneSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        initEvent();
    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                RoomName = Data_InnerCircleList.get(position).getTitle();
                SceneSetHelper.setRoomName(RoomName);
                if (isCanClick && OutCircleposition != -1)
                    OuterCircleClick(ControlActivity.this, OutCircleposition, RoomName);
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                OutCircleposition = position;
                OuterCircleClick(ControlActivity.this, position, RoomName);
                DevType = position % 8;
                isCanClick = true;
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
        Bundle bundle = new Bundle();
        bundle.putString("RoomName", RoomName);
        switch (position) {
            case 0:
            case 8:
                if (mAirFragment == null) {
                    mAirFragment = new AirControlFragment();
                    mAirFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mAirFragment);
                } else transaction.show(mAirFragment);
                break;
            case 1:
            case 9:
                if (mTVFragment == null) {
                    mTVFragment = new TVControlFragment();
                    mTVFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mTVFragment);
                } else transaction.show(mTVFragment);
                break;
            case 2:
            case 10:
                if (mTvUpFragment == null) {
                    mTvUpFragment = new TvUpControlFragment();
                    mTvUpFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mTvUpFragment);
                } else transaction.show(mTvUpFragment);
                break;
            case 3:
            case 11:
                if (mLightFragment == null) {
                    mLightFragment = new LightControlFragment();
                    mLightFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mLightFragment);
                } else transaction.show(mLightFragment);
                break;
            case 4:
            case 12:
                if (mCurFragment == null) {
                    mCurFragment = new CurtarnControlFragment();
                    mCurFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mCurFragment);
                } else transaction.show(mCurFragment);
                break;
            case 5:
                break;
            case 6:
            case 14:

                break;
        }
        transaction.commit();
    }
}
