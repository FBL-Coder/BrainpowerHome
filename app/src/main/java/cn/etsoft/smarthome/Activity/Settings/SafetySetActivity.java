package cn.etsoft.smarthome.Activity.Settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.SafetySet_DevAdapter;
import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.SetSafetyResult;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SafetySetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 安防 设置页面
 */

public class SafetySetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mSafetyType, mSafetyScene, mSafetySaveBtn, mSafetyDuiMa, mNull_tv;
    private EditText mSafetyName;
    private ImageView mShiNeng, Safety_Back, mSafety_AddDev;
    private GridView mSafetyGirdView;
    private SafetySet_DevAdapter mAdapter;
    private boolean IsNoData = true;
    private boolean IsShiNeng;
    private SetSafetyResult.SecInfoRowsBean mBean;
    private List<String> mSafety_State_List, mSafety_Scene_Name;
    private int mSafetyPosition = 0;
    private int CirclePosition = 0;
    private PopupWindow popupWindow;
    private List<WareSceneEvent> wareSceneEvent;
    private int ScenePosition = 255;


    @Override
    public void initView() {
        setLayout(R.layout.activity_safety);
        layout = getViewById(R.id.SafetySet_CircleMenu);
        mSafetySaveBtn = getViewById(R.id.SafetySet_Save_Btn);
        mShiNeng = getViewById(R.id.SafetySet_ShiNeng);
        mSafetyType = getViewById(R.id.SafetySet_Type);
        mSafetyScene = getViewById(R.id.SafetySet_Scene);
        Safety_Back = getViewById(R.id.Safety_Back);
        mSafety_AddDev = getViewById(R.id.SafetySet_AddDev);
        mSafetyDuiMa = getViewById(R.id.SafetySet_DuiMa);
        mSafetyName = getViewById(R.id.SafetySet_Name);
        mSafetyGirdView = getViewById(R.id.SafetySet_GirdView);
        mNull_tv = getViewById(R.id.null_tv);

        Safety_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSafetyGirdView.setEmptyView(mNull_tv);
        mShiNeng.setOnClickListener(this);
        mSafetySaveBtn.setOnClickListener(this);
        mSafetyType.setOnClickListener(this);
        mSafetyScene.setOnClickListener(this);
        mSafety_AddDev.setOnClickListener(this);
        mSafetyDuiMa.setOnClickListener(this);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 32) {
                    MyApplication.mApplication.dismissLoadDialog();
                    if (subtype1 == 5) {
                        ToastUtil.showText("保存成功");
                        //保存成功之后获取最新数据
                        MyApplication.getWareData().setResult(null);
                        SendDataUtil.getSafetyInfo();
                        return;
                    }
                    if (MyApplication.getWareData().getResult_safety() != null && MyApplication.getWareData().getResult_safety().getSubType2() == 255
                            && MyApplication.getWareData().getResult_safety().getSubType1() == 4) {
                        WareDataHliper.initCopyWareData().startCopySafetySetData();
                        IsNoData = false;
                        initSafety();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        if (MyApplication.getWareData().getResult_safety().getSec_info_rows().size() > 0) {
            WareDataHliper.initCopyWareData().startCopySafetySetData();
        }
        if (WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows().size() == 0) {
            ToastUtil.showText("没有安防数据");
            return;
        } else {
            initSafety();
        }
    }

    private void initSafety() {
        mSafety_State_List = new ArrayList<>();
        mSafety_State_List.add("24小时布防");
        mSafety_State_List.add("在家布防");
        mSafety_State_List.add("外出布防");
        mSafety_State_List.add("撤防状态");
        mSafety_Scene_Name = new ArrayList<>();
        wareSceneEvent = new ArrayList<>();
        WareSceneEvent event = new WareSceneEvent();
        event.setEventId(0);
        event.setSceneName("全开模式");
        WareSceneEvent event1 = new WareSceneEvent();
        event1.setEventId(1);
        event1.setSceneName("全关模式");
        wareSceneEvent.add(event);
        wareSceneEvent.add(event1);
        wareSceneEvent.addAll(MyApplication.getWareData().getSceneEvents());

        if (wareSceneEvent != null)
            for (int i = 0; i < wareSceneEvent.size(); i++) {
                mSafety_Scene_Name.add(wareSceneEvent.get(i).getSceneName());
            }
        mSafety_Scene_Name.add("无");
        Data_OuterCircleList = SafetySetHelper.initSceneCircleOUterData(CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                CirclePosition = position;
                mSafetyPosition = position % WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows().size();
                InitDataView();
            }
        });
        mSafetyGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > mBean.getRun_dev_item().size() - 1) return;

                if (mBean.getRun_dev_item().get(position).getBOnOff() == 0)
                    mBean.getRun_dev_item().get(position).setBOnOff(1);
                else mBean.getRun_dev_item().get(position).setBOnOff(0);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
            }
        });

        mSafetyGirdView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > mBean.getRun_dev_item().size() - 1) return false;
                mBean.getRun_dev_item().remove(position);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                return true;
            }
        });
        InitDataView();
    }

    /**
     * 默认数据，点击刷新数据
     */
    private void InitDataView() {
        mBean = WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows().get(mSafetyPosition);
        mNull_tv.setText(mBean.getSecName() + "  没有可用设备，请添加设备");
        mAdapter = new SafetySet_DevAdapter(mBean.getRun_dev_item(), SafetySetActivity.this);
        mSafetyGirdView.setAdapter(mAdapter);

        mSafetyName.setText("");
        mSafetyName.setHint(mBean.getSecName());

        //某一安防里的设备为空或长度为0时
        if (mBean.getRun_dev_item().size() == 0) {
            mShiNeng.setImageResource(R.drawable.show_off);
            mSafetyScene.setText("点击选择关联情景");
            mSafetyType.setText("点击选择安防状态");
        }

        if (mBean.getValid() == 1) {
            mShiNeng.setImageResource(R.drawable.show_on);
            IsShiNeng = true;
        } else {
            mShiNeng.setImageResource(R.drawable.show_off);
            IsShiNeng = false;
        }

        //布防类型是"撤防状态"
        if (mBean.getSecType() == 255)
            mSafetyType.setText(mSafety_State_List.get(3));
        else {//布防类型是"24小时布防"、"在家布防"、"外出布防"
            try {
                mSafetyType.setText(mSafety_State_List.get(mBean.getSecType()));
            } catch (Exception e) {
                mSafetyType.setText(mSafety_State_List.get(3));
            }
        }
        //情景
        if (wareSceneEvent != null)
            for (int i = 0; i < wareSceneEvent.size(); i++) {
                if (wareSceneEvent.get(i).getEventId()
                        == mBean.getSceneId()) {
                    mSafetyScene.setText(wareSceneEvent.get(i).getSceneName());
                    ScenePosition = i;
                    if (i == wareSceneEvent.size() - 1)
                        ScenePosition = 255;
                }
            }
        else
            mSafetyScene.setText("暂无情景数据");
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (IsNoData && WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows().size() == 0) {
            ToastUtil.showText("获取数据异常，请稍后在试");
            return;
        }
        switch (v.getId()) {
            case R.id.SafetySet_Save_Btn://保存
                SafetySetHelper.safetySet_Save(this, mSafetyName, IsShiNeng, ScenePosition,
                        mSafetyType, mSafety_State_List, mSafetyPosition, mBean.getRun_dev_item());

                break;
            case R.id.SafetySet_ShiNeng: //使能开关
                IsShiNeng = !IsShiNeng;
                if (IsShiNeng) mShiNeng.setImageResource(R.drawable.show_on);
                else mShiNeng.setImageResource(R.drawable.show_off);
                break;
            case R.id.SafetySet_DuiMa: //对码
                SafetySetHelper.safetySetDuiMa(this, mSafetyName, IsShiNeng, mSafetyScene, mSafetyType,
                        mSafety_State_List, mSafetyPosition, mBean.getRun_dev_item());
                MyApplication.mApplication.showLoadDialog(this);
                break;
            case R.id.SafetySet_Scene: //关联情景
                initRadioPopupWindow(v, mSafety_Scene_Name);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.SafetySet_Type: //选择布防类型
                initRadioPopupWindow(v, mSafety_State_List);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.SafetySet_AddDev: //添加设备
                Intent intent = new Intent(SafetySetActivity.this, SetAddDevActivity.class);
                intent.putExtra("name", "Safety");
                intent.putExtra("position", mSafetyPosition);
                startActivityForResult(intent, 0);
                break;
        }
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
                if (view_parent.getId() == R.id.SafetySet_Scene) {
                    ScenePosition = position;
                    if (position == text.size() - 1)
                        ScenePosition = 255;
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged(WareDataHliper.initCopyWareData().getSetSafetyResult().getSec_info_rows()
                .get(mSafetyPosition).getRun_dev_item());
    }
}
