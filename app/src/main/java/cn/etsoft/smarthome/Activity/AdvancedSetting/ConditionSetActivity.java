package cn.etsoft.smarthome.Activity.AdvancedSetting;

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
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.ConditionSet_DevAdapter;
import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.ConditionSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 环境触发器页面
 */

public class ConditionSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mConditionSaveBtn, mConditionType, mConditionWay, mConditionSet_AddDev;
    private EditText mConditionName, mConditionChuFaZhi;
    private ImageView mShiNeng;
    private GridView mConditionGirdView;
    private ConditionSet_DevAdapter mAdapter;
    private Condition_Event_Bean.EnvEventRowsBean mBean;
    private boolean IsNoData = true;
    private boolean IsOpenShiNeng = false;
    private int mConditionPosition = 0;
    private int CirclePosition = 0;
    private boolean IsCanClick = false;
    private PopupWindow popupWindow;
    private List<String> Event_type;
    private List<String> Event_Way;


    @Override
    public void initView() {
        setLayout(R.layout.activity_conditionset);
        layout = getViewById(R.id.ConditionSet_CircleMenu);
        mConditionSaveBtn = getViewById(R.id.ConditionSet_Save_Btn);
        mShiNeng = getViewById(R.id.ConditionSet_ShiNeng);
        mConditionName = getViewById(R.id.ConditionSet_Name);
        mConditionChuFaZhi = getViewById(R.id.ConditionSet_ChuFaZhi);
        mConditionType = getViewById(R.id.ConditionSet_Type);
        mConditionWay = getViewById(R.id.ConditionSet_Way);
        mConditionSet_AddDev = getViewById(R.id.ConditionSet_AddDev);
        mConditionGirdView = getViewById(R.id.ConditionSet_GirdView);


        mShiNeng.setOnClickListener(this);
        mConditionSaveBtn.setOnClickListener(this);
        mConditionType.setOnClickListener(this);
        mConditionSet_AddDev.setOnClickListener(this);
        mConditionWay.setOnClickListener(this);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getEnvEvents.getValue())/*27*/ {
                    IsNoData = false;
                    WareDataHliper.initCopyWareData().startCopyConditionData();
                    initCondition();
                }
                if (datType == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_editEnvEvents.getValue())/*29*/ {
                    ToastUtil.showText("添加成功");
                    WareDataHliper.initCopyWareData().startCopyConditionData();
                    initCondition();
                }
            }
        });

    }

    @Override
    public void initData() {

        if (WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().size() == 0) {
            ToastUtil.showText("没有定时器数据");
            return;
        } else {

            initCondition();
        }
    }

    private void initCondition() {
        Event_Way = new ArrayList<>();
        Event_Way.add("大于阀值");
        Event_Way.add("小于阀值");
        Event_type = new ArrayList<>();
        Event_type.add("温度触发");
        Event_type.add("湿度触发");
        Event_type.add("P2.5触发");
        Data_OuterCircleList = ConditionSetHelper.initSceneCircleOUterData(IsCanClick,CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                CirclePosition = position;
                mConditionPosition = position % WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().size();
                mBean = WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().get(mConditionPosition);

                mConditionName.setText("");
                mConditionName.setHint(mBean.getEventName());
                if (mBean == null || mBean.getRun_dev_item().size() == 0) {
                    mShiNeng.setImageResource(R.drawable.ic_launcher);
                    mConditionChuFaZhi.setHint("输入触发值");
                    mConditionWay.setText("选择触发方式");
                    mConditionType.setText("选择触发类别");
                } else {
                    mConditionName.setText(mBean.getEventName());
                    mConditionChuFaZhi.setText(mBean.getValTh() + "");
                    if (mBean.getValid() == 1)
                        mShiNeng.setImageResource(R.drawable.ic_launcher_round);
                    else mShiNeng.setImageResource(R.drawable.ic_launcher);
                    mConditionWay.setText(Event_Way.get(mBean.getThType()));
                    mConditionType.setText(Event_type.get(mBean.getEnvType()));
                }

                if (mAdapter == null)
                    mAdapter = new ConditionSet_DevAdapter(mBean.getRun_dev_item(), ConditionSetActivity.this);
                else mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                mConditionGirdView.setAdapter(mAdapter);
            }
        });

        mConditionGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBean.getRun_dev_item().get(position).getBOnOff() == 0)
                    mBean.getRun_dev_item().get(position).setBOnOff(1);
                else mBean.getRun_dev_item().get(position).setBOnOff(0);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
            }
        });
        mConditionGirdView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mBean.getRun_dev_item().remove(position);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                return true;
            }
        });
    }


    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.popupwindow_equipment_listview, null);
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

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (IsNoData && WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows().size() == 0) {
            ToastUtil.showText("获取数据异常，请稍后在试");
            return;
        }
        if (!IsCanClick) {
            ToastUtil.showText("请先选择定时器！");
            return;
        }
        switch (v.getId()) {
            case R.id.ConditionSet_Save_Btn://保存
                ConditionSetHelper.SaveCondition(ConditionSetActivity.this, mConditionName, mConditionChuFaZhi, IsOpenShiNeng, mConditionWay
                        , mConditionType, Event_Way, Event_type, mConditionPosition, mBean.getRun_dev_item());
                break;
            case R.id.ConditionSet_ShiNeng: //使能开关
                IsOpenShiNeng = !IsOpenShiNeng;
                if (IsOpenShiNeng) mShiNeng.setImageResource(R.drawable.ic_launcher_round);
                else mShiNeng.setImageResource(R.drawable.ic_launcher);
                break;
            case R.id.ConditionSet_Type: //触发器类型
                initRadioPopupWindow(mConditionType, Event_type);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.ConditionSet_AddDev: //添加设备
                Intent intent = new Intent(ConditionSetActivity.this, SetAddDevActivity.class);
                intent.putExtra("name", "Condition");
                intent.putExtra("position", mConditionPosition);
                startActivityForResult(intent, 0);

                break;
            case R.id.ConditionSet_Way: //触发方式
                initRadioPopupWindow(mConditionWay, Event_Way);
                popupWindow.showAsDropDown(v, 0, 0);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged(WareDataHliper.initCopyWareData().getConditionEvent().getenvEvent_rows()
                .get(mConditionPosition).getRun_dev_item());
    }
}
