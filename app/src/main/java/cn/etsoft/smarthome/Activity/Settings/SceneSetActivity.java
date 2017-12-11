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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Adapter.RecyclerView.SceneSet_ScenesAdapter;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.Fragment.Control.LightControlFragment;
import cn.etsoft.smarthome.Fragment.Control.SocketFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.AirSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.CurtarnSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.DoorSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.FloorHeatFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.FreshAirFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.LightSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.TVSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.TvUpSceneFragment;
import cn.etsoft.smarthome.Fragment.SceneSet.VideoSceneFragment;
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
    private ImageView Control_Back;
    private TextView mSceneSet_Add_Btn, mSceneSetTestBtn, mSceneSetSaveBtn, mNull_tv, scene_safetytype;
    private SceneSet_ScenesAdapter mScenesAdapter;
    private RelativeLayout SceneSet_Info;
    private Fragment mLightFragment, mAirFragment, mTVFragment,
            mTvUpFragment, mCurFragment, mVideoFragment, mSocketFragment, mDoorFragemnt, mFreshAirFragment, mFloorHeatFragment;
    private int ScenePosition = 0, DevType = -1, longClickPsotion;
    private WareSceneEvent sceneEvent;
    private String RoomName = "";
    private boolean IsNoData = true;
    private PopupWindow popupWindow;
    private List<String> safetytypes;

    @Override
    public void initView() {
        setLayout(R.layout.activity_sceneset);

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {

                if (datType == 22) {
                    MyApplication.mApplication.dismissLoadDialog();
                    IsNoData = false;
                    WareDataHliper.initCopyWareData().startCopySceneData();
                    initData();
                }
                if (datType == 23) {
//                    SendDataUtil.getSceneInfo();
                    MyApplication.mApplication.dismissLoadDialog();
                    ToastUtil.showText("添加成功");
                    WareDataHliper.initCopyWareData().startCopySceneData();
                    initData();
                }
                if (datType == 24) {
                    MyApplication.mApplication.dismissLoadDialog();
                    ToastUtil.showText("保存成功");
                }
                if (datType == 25) {
                    MyApplication.mApplication.dismissLoadDialog();
                    WareDataHliper.initCopyWareData().startCopySceneData();
                    if (MyApplication.getWareData().getSceneEvents().size() != 0) {
                        if (ScenePosition != 0 && longClickPsotion <= ScenePosition)
                            ScenePosition -= 1;
                    } else ScenePosition = 0;
                    initData();
                    ToastUtil.showText("删除成功");
                }
            }
        });

        mSceneSet_Add_Btn = getViewById(R.id.SceneSet_Add_Btn);
        mSceneSetTestBtn = getViewById(R.id.SceneSet_Test_Btn);
        mSceneSetSaveBtn = getViewById(R.id.SceneSet_Save_Btn);
        Control_Back = getViewById(R.id.Control_Back);
        mNull_tv = getViewById(R.id.null_tv);
        SceneSet_Info = getViewById(R.id.SceneSet_Info);
        scene_safetytype = getViewById(R.id.scene_safetytype);

        mSceneSet_Add_Btn.setOnClickListener(this);
        mSceneSetTestBtn.setOnClickListener(this);
        mSceneSetSaveBtn.setOnClickListener(this);
        scene_safetytype.setOnClickListener(this);

        Control_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        safetytypes = new ArrayList<>();
        safetytypes.add("24小时布防");
        safetytypes.add("在家布防");
        safetytypes.add("外出布防");
        safetytypes.add("全部撤防");
        safetytypes.add("不关联");

        layout = getViewById(R.id.SceneSet_CircleMenu);
        Data_OuterCircleList = SceneSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = SceneSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);
        mScenesAdapter = new SceneSet_ScenesAdapter();
        mScenesAdapter.setselect(ScenePosition);
        mSceneSetScenes.setAdapter(mScenesAdapter);


        if (WareDataHliper.initCopyWareData().getCopyScenes().size() == 0) {
            IsNoData = true;
            SendDataUtil.getSceneInfo();
            SceneSet_Info.setVisibility(View.GONE);
        } else {
            IsNoData = false;
            SceneSet_Info.setVisibility(View.VISIBLE);
        }
        initEvent();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.SceneSet_Add_Btn:
                if (MyApplication.getWareData().getSceneEvents().size() == 10) {
                    ToastUtil.showText("自定义情景最多10个！");
                    return;
                }
                SceneSetHelper.AddScene(this);
                break;
            case R.id.SceneSet_Test_Btn:
                break;
            case R.id.scene_safetytype:
                initRadioPopupWindow(v, safetytypes);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.SceneSet_Save_Btn:
                if (IsNoData) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                SceneSetHelper.saveScene(this, ScenePosition);
                break;
        }
    }

    private void initEvent() {
        sceneEvent = WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition);
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                if (IsNoData) {
                    ToastUtil.showText("数据获取不成功或者没有数据，不可操作！");
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
                    ToastUtil.showText("数据获取不成功或者没有数据，不可操作！");
                    return;
                }
                mNull_tv.setVisibility(View.GONE);
                DevType = position % 10;
                OuterCircleClick(SceneSetActivity.this, position, RoomName);
                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(ScenePosition, position % 8, RoomName);
            }
        });

        mScenesAdapter.setselect(ScenePosition);
        mScenesAdapter.setOnItemClick(new SceneSet_ScenesAdapter.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(position, DevType, RoomName);
                ScenePosition = position;

                sceneEvent = WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition);

                if (0 == sceneEvent.getExeSecu())
                    scene_safetytype.setText("24小时布防");
                if (1 == sceneEvent.getExeSecu())
                    scene_safetytype.setText("在家布防");
                if (2 == sceneEvent.getExeSecu())
                    scene_safetytype.setText("外出布防");
                if (4 == sceneEvent.getExeSecu())
                    scene_safetytype.setText("不关联");
                if (255 == sceneEvent.getExeSecu())
                    scene_safetytype.setText("撤防状态");
            }

            @Override
            public void OnItemLongClick(View view, final int position) {
                longClickPsotion = position;
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
                        if (WareDataHliper.initCopyWareData().getCopyScenes().get(position).getEventId() == 10
                                || WareDataHliper.initCopyWareData().getCopyScenes().get(position).getEventId() == 11) {
                            ToastUtil.showText("此情景不可删除");
                            return;
                        }
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
        if (0 == WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition).getExeSecu())
            scene_safetytype.setText("24小时布防");
        if (1 == WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition).getExeSecu())
            scene_safetytype.setText("在家布防");
        if (2 == WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition).getExeSecu())
            scene_safetytype.setText("外出布防");
        if (3 == WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition).getExeSecu())
            scene_safetytype.setText("不关联");
        if (255 == WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition).getExeSecu())
            scene_safetytype.setText("撤防状态");

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

        Bundle bundle = new Bundle();
        bundle.putString("RoomName", RoomName);
        bundle.putInt("ScenePosition", ScenePosition);
        switch (position) {
            case 0:
                mAirFragment = new AirSceneFragment();
                mAirFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mAirFragment);
                break;
            case 1:
                mTVFragment = new TVSceneFragment();
                mTVFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mTVFragment);
                break;
            case 2:
                mTvUpFragment = new TvUpSceneFragment();
                mTvUpFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mTvUpFragment);
                break;
            case 3:
                mLightFragment = new LightSceneFragment();
                mLightFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mLightFragment);
                break;
            case 4:
                mCurFragment = new CurtarnSceneFragment();
                mCurFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mCurFragment);
                break;
            case 5:
                mVideoFragment = new VideoSceneFragment();
                mVideoFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mVideoFragment);
                break;
            case 6:
                mSocketFragment = new SocketFragment();
                mSocketFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mSocketFragment);
                break;
            case 7:
                mFreshAirFragment = new FreshAirFragment();
                mFreshAirFragment.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mFreshAirFragment);
                break;
            case 8:
                mDoorFragemnt = new DoorSceneFragment();
                mDoorFragemnt.setArguments(bundle);
                transaction.replace(R.id.SceneSet_Info, mDoorFragemnt);
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
    public static SceneSetSceneClickListener mSceneSetSceneClickListener;

    public static void setmSceneSetSceneClickListener(SceneSetSceneClickListener mSceneSetSceneClickListener) {
        SceneSetActivity.mSceneSetSceneClickListener = mSceneSetSceneClickListener;
    }

    public interface SceneSetSceneClickListener {
        void SceneClickPosition(int ScenePosition, int DevType, String RoomName);
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.listview_popupwindow_equipment, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        customView.setFocusable(true);
        customView.setFocusableInTouchMode(true);
        customView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(customView, view_parent.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                WareDataHliper.initCopyWareData().getCopyScenes().get(ScenePosition).setExeSecu(position == 3 ? 255 : position);
                popupWindow.dismiss();
            }
        });
        //popupwindow页面之外可点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }
}
