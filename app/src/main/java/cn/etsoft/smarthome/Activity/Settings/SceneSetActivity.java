package cn.etsoft.smarthome.Activity.Settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Adapter.RecyclerView.SceneSet_ScenesAdapter;
import cn.etsoft.smarthome.Fragment.Control.LightControlFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.AirSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.CurtarnSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.FloorHeatFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.FreshAirFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.LightSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.TVSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.TvUpSceneFragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.ControlHelper;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设置情景页面
 */

public class SceneSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private RecyclerView mSceneSetScenes;
    private TextView mSceneSet_Add_Btn, mSceneSetTestBtn, mSceneSetSaveBtn, mNull_tv;
    private SceneSet_ScenesAdapter mScenesAdapter;
    private Fragment mLightFragment, mAirFragment, mTVFragment,
            mTvUpFragment, mCurFragment, mFreshAirFragment, mFloorHeatFragment;
    private int ScenePosition = 0, DevType = -1;
    private String RoomName = "";
    private boolean IsNoData = true;

    @Override
    public void initView() {
        setLayout(R.layout.activity_sceneset);

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();
                if (datType == 22) {
                    IsNoData = false;
                    WareDataHliper.initCopyWareData().startCopySceneData();
                    initData();
                }
                if (datType == 23) {
                    ToastUtil.showText("添加成功");
                    WareDataHliper.initCopyWareData().startCopySceneData();
                    initData();
                }
                if (datType == 24) {
                    ToastUtil.showText("保存成功");
                }
                if (datType == 25) {
                    WareDataHliper.initCopyWareData().startCopySceneData();
                    initData();
                    ToastUtil.showText("删除成功");
                }
            }
        });

        if (MyApplication.getWareData().getSceneEvents() != null && MyApplication.getWareData().getSceneEvents().size() > 0)
            IsNoData = false;

        mSceneSet_Add_Btn = getViewById(R.id.SceneSet_Add_Btn);
        mSceneSetTestBtn = getViewById(R.id.SceneSet_Test_Btn);
        mSceneSetSaveBtn = getViewById(R.id.SceneSet_Save_Btn);
        mNull_tv = getViewById(R.id.null_tv);

        mSceneSet_Add_Btn.setOnClickListener(this);
        mSceneSetTestBtn.setOnClickListener(this);
        mSceneSetSaveBtn.setOnClickListener(this);

        mSceneSetScenes = getViewById(R.id.SceneSet_Scenes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSceneSetScenes.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
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

        mScenesAdapter = new SceneSet_ScenesAdapter(WareDataHliper.initCopyWareData().getCopyScenes());
        mSceneSetScenes.setAdapter(mScenesAdapter);
        initEvent();
    }

    @Override
    public void onClick(View v) {
        if (IsNoData) {
            ToastUtil.showText("数据未加载成功，不可操作！");
            return;
        }
        switch (v.getId()) {
            case R.id.SceneSet_Add_Btn:
                SceneSetHelper.AddScene(this);
                break;
            case R.id.SceneSet_Test_Btn:
                break;
            case R.id.SceneSet_Save_Btn:
                SceneSetHelper.saveScene(this, ScenePosition);
                break;
        }
    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                if (IsNoData) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                mNull_tv.setVisibility(View.GONE);
                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(ScenePosition, DevType, Data_InnerCircleList.get(position).getTitle());
                RoomName = Data_InnerCircleList.get(position).getTitle();
                SceneSetHelper.setRoomName(RoomName);
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                if (IsNoData) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                mNull_tv.setVisibility(View.GONE);
                DevType = position % 10;
                OuterCircleClick(SceneSetActivity.this, position, RoomName);
                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(ScenePosition, position % 8, RoomName);
            }
        });

        mScenesAdapter.setOnItemClick(new SceneSet_ScenesAdapter.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(position, DevType, RoomName);
                ScenePosition = position;
            }

            @Override
            public void OnItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SceneSetActivity.this);
                builder.setTitle("提示 :");
                builder.setMessage("是否删除此情景？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MyApplication.mApplication.showLoadDialog(SceneSetActivity.this);
                        try {
                            SendDataUtil.deleteScene(WareDataHliper.initCopyWareData().getCopyScenes().get(position));
                        } catch (Exception e) {
                            Log.e("SETSCENE", "onClick: " + e);
                        }
                    }
                });
                builder.create().show();
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        RoomName = "全部";
        ControlHelper.setRoomName(RoomName);
        bundle.putString("RoomName", "全部");
        bundle.putInt("ScenePosition", ScenePosition);
        mLightFragment = new LightSceneFragment();
        mLightFragment.setArguments(bundle);
        transaction.replace(R.id.SceneSet_Info, mLightFragment);
        transaction.commit();
        DevType = 3;
    }

    /**
     * 外圆菜单 点击事件
     */
    public void OuterCircleClick(FragmentActivity activity, int position, String RoomName) {
//        if ("".equals(RoomName)) {
//            ToastUtil.showText("请先选择房间");
//            return;
//        }
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
        if (mFloorHeatFragment != null)
            transaction.hide(mFloorHeatFragment);
        Bundle bundle = new Bundle();
        bundle.putString("RoomName", RoomName);
        bundle.putInt("ScenePosition", ScenePosition);
        switch (position) {
            case 0:
            case 10:
                if (mAirFragment == null) {
                    mAirFragment = new AirSceneFragment();
                    mAirFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mAirFragment);
                } else transaction.show(mAirFragment);
                break;
            case 1:
            case 11:
                if (mTVFragment == null) {
                    mTVFragment = new TVSceneFragment();
                    mTVFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mTVFragment);
                } else transaction.show(mTVFragment);
                break;
            case 2:
            case 12:
                if (mTvUpFragment == null) {
                    mTvUpFragment = new TvUpSceneFragment();
                    mTvUpFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mTvUpFragment);
                } else transaction.show(mTvUpFragment);
                break;
            case 3:
            case 13:
                if (mLightFragment == null) {
                    mLightFragment = new LightSceneFragment();
                    mLightFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mLightFragment);
                } else transaction.show(mLightFragment);
                break;
            case 4:
            case 14:
                if (mCurFragment == null) {
                    mCurFragment = new CurtarnSceneFragment();
                    mCurFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mCurFragment);
                } else transaction.show(mCurFragment);
                break;
            case 5:
                break;
            case 6:

                break;
            case 7:
                if (mFreshAirFragment == null) {
                    mFreshAirFragment = new FreshAirFragment();
                    mFreshAirFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mFreshAirFragment);
                } else transaction.show(mFreshAirFragment);

                break;
            case 9:
                if (mFloorHeatFragment == null) {
                    mFloorHeatFragment = new FloorHeatFragment();
                    mFloorHeatFragment.setArguments(bundle);
                    transaction.add(R.id.SceneSet_Info, mFloorHeatFragment);
                } else transaction.show(mFloorHeatFragment);

                break;
        }
        transaction.commit();
    }

    //点击情景，房间，设备。触发回调，刷新界面
    public static SceneSetSceneClickListener mSceneSetSceneClickListener;

    public static void setmSceneSetSceneClickListener(SceneSetSceneClickListener mSceneSetSceneClickListener) {
        SceneSetActivity.mSceneSetSceneClickListener = mSceneSetSceneClickListener;
    }

    public interface SceneSetSceneClickListener {
        void SceneClickPosition(int ScenePosition, int DevType, String RoomName);
    }
}
