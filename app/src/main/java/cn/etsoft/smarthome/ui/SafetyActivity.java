package cn.etsoft.smarthome.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.domain.SetSafetyResult;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog;

import static cn.etsoft.smarthome.R.id.tv_equipment_parlour;

/**
 * 安防界面
 * Created by F-B-L on 2017/5/18.
 */
public class SafetyActivity extends FragmentActivity implements View.OnClickListener {

    private Button safety_save, safety_all_close, safety_all_open;
    private TextView safety_match_code, safety_enabled, safety_state, safety_scene, safety_type,
            add_dev_safety, add_dev_Layout_close, tv_text_parlour;
    private int[] image = new int[]{R.drawable.kongtiao, R.drawable.tv_0, R.drawable.jidinghe, R.drawable.dengguang, R.drawable.chuanglian};
    private ImageView close_open_safety;
    private EditText safety_name;
    private GridView gridView_safety;
    private ListView add_dev_Layout_lv;
    private LinearLayout add_dev_Layout_ll;
    private RecyclerView RecyclerView_timer;
    private Dialog mDialog;
    private RecyclerViewAdapter_Safety adapter_Timer;
    private PopupWindow popupWindow;
    private GridViewAdapter_Safety mGridViewAdapter_Safety;
    //添加设备房间position；
    private int home_position;
    //安防位置position
    private int Safety_position;
    private List<String> home_text;
    private List<WareDev> dev;
    private List<WareDev> mWareDev;
    private List<String> safety_state_data, safety_scene_name, safety_state_data1;
    private List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> common_dev;
    private SafetyActivity.EquipmentAdapter Equipadapter;
    private boolean IsHaveData;
    String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
            ",\"datType\":32" +
            ",\"subType1\":3" +
            ",\"subType2\":255" +
            "}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决弹出键盘压缩布局的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_safety);
        initDialog("初始化数据中...");
        //初始化组件
        initView();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 32) {
                    if (MyApplication.getWareData().getResult() != null && MyApplication.getWareData().getResult().getSubType1() == 5) {
                        ToastUtil.showToast(SafetyActivity.this, "保存成功");
                        //保存成功之后获取最新数据
                        MyApplication.getWareData().setResult(null);
                        MyApplication.sendMsg(ctlStr);
                        return;
                    }
                    if (MyApplication.getWareData().getResult_safety() != null && MyApplication.getWareData().getResult_safety().getSubType2() == 255 && MyApplication.getWareData().getResult_safety().getSubType1() == 4) {
                        IsHaveData = true;
                        initRecycleView(Safety_position);
                        initGridView(Safety_position);
                        initData(Safety_position);
                    }
                    if (MyApplication.getWareData().getResult() != null && MyApplication.getWareData().getResult().getSubType1() == 1 && MyApplication.getWareData().getResult().getSubType2() != 255) {
                        ToastUtil.showToast(SafetyActivity.this, "布防成功");
                        SharedPreferences sharedPreferences1 = getSharedPreferences("profile",
                                Context.MODE_PRIVATE);
                        sharedPreferences1.edit().putInt("safety_style", MyApplication.getWareData().getResult().getSubType2()).commit();
                        initData(Safety_position);
                    } else if (MyApplication.getWareData().getResult() != null && MyApplication.getWareData().getResult().getSubType1() == 1 && MyApplication.getWareData().getResult().getSubType2() == 255) {
                        ToastUtil.showToast(SafetyActivity.this, "撤防成功");
                        initData(Safety_position);
                    }
                }
            }
        });
        MyApplication.sendMsg(ctlStr);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        safety_save = (Button) findViewById(R.id.safety_save);
        safety_match_code = (TextView) findViewById(R.id.safety_match_code);
        safety_enabled = (TextView) findViewById(R.id.safety_enabled);
        safety_state = (TextView) findViewById(R.id.safety_state);
        safety_scene = (TextView) findViewById(R.id.safety_scene);
        safety_enabled = (TextView) findViewById(R.id.safety_enabled);
        add_dev_safety = (TextView) findViewById(R.id.add_dev_safety);
        safety_name = (EditText) findViewById(R.id.safety_name);

        gridView_safety = (GridView) findViewById(R.id.gridView_safety);

        //添加设备布局
        add_dev_Layout_lv = (ListView) findViewById(R.id.equipment_loyout_lv);
        add_dev_Layout_close = (TextView) findViewById(R.id.equipment_close);
        tv_text_parlour = (TextView) findViewById(tv_equipment_parlour);
        add_dev_Layout_ll = (LinearLayout) findViewById(R.id.add_dev_Layout_ll);

        safety_match_code.setOnClickListener(this);
        safety_enabled.setOnClickListener(this);
        safety_state.setOnClickListener(this);
        safety_scene.setOnClickListener(this);
        add_dev_safety.setOnClickListener(this);
        add_dev_Layout_close.setOnClickListener(this);
        tv_text_parlour.setOnClickListener(this);
        safety_save.setOnClickListener(this);


        RecyclerView_timer = (RecyclerView) findViewById(R.id.RecyclerView_timer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView_timer.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


        safety_state_data = new ArrayList<>();
        safety_state_data.add("24小时布防");
        safety_state_data.add("在家布防");
        safety_state_data.add("外出布防");
        safety_state_data.add("撤防状态");
        safety_state_data1 = new ArrayList<>();
        safety_state_data1.add("24小时布防");
        safety_state_data1.add("在家布防");
        safety_state_data1.add("外出布防");
        safety_scene_name = new ArrayList<>();
        if (MyApplication.getWareData().getSceneEvents() != null)
            for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                safety_scene_name.add(MyApplication.getWareData().getSceneEvents().get(i).getSceneName());
            }

        close_open_safety = (ImageView) findViewById(R.id.close_open_safety);
        close_open_safety.setOnClickListener(this);
    }

    /**
     * 初始化GridView
     */
    public void initGridView(int RecycleViewPosition) {
        common_dev = MyApplication.getWareData().getResult_safety().getSec_info_rows().get(RecycleViewPosition).getRun_dev_item();
        mGridViewAdapter_Safety = new GridViewAdapter_Safety(common_dev);
        gridView_safety.setAdapter(mGridViewAdapter_Safety);

        gridView_safety.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                common_dev.remove(position);
                mGridViewAdapter_Safety.notifyDataSetChanged(common_dev);
                return true;
            }
        });
        gridView_safety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (common_dev.get(position).getBOnOff() == 0)
                    common_dev.get(position).setBOnOff(1);
                else
                    common_dev.get(position).setBOnOff(0);
                mGridViewAdapter_Safety.notifyDataSetChanged(common_dev);
            }
        });
    }

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(this);
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (mDialog.isShowing()) {
                        handler.sendMessage(handler.obtainMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e + "");
                }
            }
        }).start();
    }

    /**
     * 初始化数据
     *
     * @param timer_position
     */
    public void initData(int timer_position) {
        home_text = MyApplication.getRoom_list();
        if (MyApplication.getWareData().getResult_safety() == null || MyApplication.getWareData().getResult_safety().getSec_info_rows() == null && MyApplication.getWareData().getResult_safety().getSec_info_rows().size() == 0)
            return;
        safety_name.setText("");
        safety_name.setHint(MyApplication.getWareData().getResult_safety()
                .getSec_info_rows().get(timer_position).getSecName());
        //某一安防里的设备为空或长度为0时
        if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(timer_position).getRun_dev_item() == null
                || MyApplication.getWareData().getResult_safety().getSec_info_rows().get(timer_position).getRun_dev_item().size() == 0) {
//            safety_enabled.setText("禁用");
//            safety_scene.setText("选择情景");
//            safety_state.setText("选择状态");
            ToastUtil.showToast(this,"该防区没有设备");
        }
//        else {
            if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(timer_position).getValid() == 1)
                safety_enabled.setText("启用");
            else safety_enabled.setText("禁用");
            //布防类型是"撤防状态"
            if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(timer_position).getSecType() == 255)
                safety_state.setText(safety_state_data.get(3));
            else {//布防类型是"24小时布防"、"在家布防"、"外出布防"
                try {
                    safety_state.setText(safety_state_data.get(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(timer_position).getSecType()));
                } catch (Exception e) {
                    safety_state.setText(safety_state_data.get(3));
                }
            }
            //情景
            if (MyApplication.getWareData().getSceneEvents() != null)
                for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                    if (MyApplication.getWareData().getSceneEvents().get(i).getEventld()
                            == MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSceneId()) {
                        safety_scene.setText(MyApplication.getWareData().getSceneEvents().get(i).getSceneName());
                    }
                }
            else
                safety_scene.setText("选择情景");
//        }
    }

    /**
     * 初始化GridView 数据
     *
     * @param home_position
     * @return
     */
    public List<WareDev> initGridViewData(int home_position) {
        dev = new ArrayList<>();
        mWareDev = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            mWareDev.add(MyApplication.getWareData().getDevs().get(i));
        }
        for (int i = 0; i < mWareDev.size(); i++) {
            if (home_text != null && home_text.size() > 0)
                if (mWareDev.get(i).getRoomName().equals(home_text.get(home_position)))
                    dev.add(mWareDev.get(i));
        }
        return dev;
    }

    @Override
    public void onClick(View v) {
        if (!IsHaveData) {
            ToastUtil.showToast(SafetyActivity.this, "获取数据异常，请稍后再试");
            return;
        }
        switch (v.getId()) {
            case R.id.safety_save://保存
                try {
                    SetSafetyResult safetyResult = new SetSafetyResult();
                    List<SetSafetyResult.SecInfoRowsBean> timerEvent_rows = new ArrayList<>();
                    SetSafetyResult.SecInfoRowsBean bean = new SetSafetyResult.SecInfoRowsBean();
                    bean.setSecDev(common_dev.size());
                    bean.setDevCnt(common_dev.size());
                    bean.setItemCnt(1);
                    bean.setSecId(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecId());
                    bean.setRun_dev_item(common_dev);
                    if ("".equals(safety_name.getText().toString())) {
                        bean.setSecName(CommonUtils.bytesToHexString(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecName().getBytes("GB2312")));
                    } else {
                        try {
                            //名称名称
                            bean.setSecName(CommonUtils.bytesToHexString(safety_name.getText().toString().getBytes("GB2312")));
                        } catch (UnsupportedEncodingException e) {
                            ToastUtil.showToast(SafetyActivity.this, "安防名称不合适");
                            return;
                        }
                    }
                    if ("启用".equals(safety_enabled.getText().toString()))
                        bean.setValid(1);
                    else
                        bean.setValid(0);
                    bean.setSecCode(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecCode());
                    //safety_state_data : 选择布防类型
                    //safety_state : 状态
                    for (int i = 0; i < safety_state_data.size(); i++) {
                        if (safety_state_data.get(i).equals(safety_state.getText().toString())) {
                            if (i == 3)
                                bean.setSecType(255);
                            else
                                bean.setSecType(i);
                        }
                    }

                    //关联情景
                    for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                        if (safety_scene.getText().toString().equals(MyApplication.getWareData().getSceneEvents().get(i).getSceneName()))
                            bean.setSceneId(i);
                    }
                    timerEvent_rows.add(bean);
                    safetyResult.setDatType(32);
                    safetyResult.setDevUnitID(GlobalVars.getDevid());
                    safetyResult.setSubType1(5);
                    safetyResult.setSubType2(Safety_position);
                    safetyResult.setItemCnt(1);
//                    safetyResult.setSubType2(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecId());
                    safetyResult.setSec_info_rows(timerEvent_rows);
                    Gson gson = new Gson();
                    Log.e("保存安防数据", gson.toJson(safetyResult));
                    initDialog("保存数据中...");
                    MyApplication.sendMsg(gson.toJson(safetyResult));
                } catch (Exception e) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    Log.e("保存安防数据", "保存数据异常" + e);
                    ToastUtil.showToast(this, "保存数据异常,请检查数据是否合适");
                }
                break;
            case R.id.add_dev_safety://点击添加设备按钮事件
                //添加页面的item点击，以及listView的初始化
                Equipadapter = new SafetyActivity.EquipmentAdapter(initGridViewData(home_position), this);
                add_dev_Layout_lv.setAdapter(Equipadapter);
                tv_text_parlour.setText(home_text.get(home_position));
                add_dev_Layout_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        WareDev item = dev.get(position);
                        boolean tag = true;
                        if (common_dev == null)
                            common_dev = new ArrayList<>();
                        if (common_dev.size() > 0) {
                            for (int i = 0; i < common_dev.size(); i++) {
                                if (common_dev.get(i).getDevType() == item.getType()
                                        && common_dev.get(i).getDevID() == item.getDevId()
                                        && common_dev.get(i).getCanCpuID().equals(item.getCanCpuId())) {
                                    tag = false;
                                    Toast.makeText(SafetyActivity.this, "设备已存在！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (tag) {
                            if (common_dev.size() == 4) {
                                ToastUtil.showToast(SafetyActivity.this, "安防设备最多4个！");
                                return;
                            }
                            SetSafetyResult.SecInfoRowsBean.RunDevItemBean bean = new SetSafetyResult.SecInfoRowsBean.RunDevItemBean();
                            bean.setDevID(item.getDevId());
                            bean.setBOnOff(item.getbOnOff());
                            bean.setDevType(item.getType());
                            bean.setCanCpuID(item.getCanCpuId());
                            common_dev.add(bean);
                            if (mGridViewAdapter_Safety != null)
                                mGridViewAdapter_Safety.notifyDataSetChanged(common_dev);
                            else {
                                mGridViewAdapter_Safety = new GridViewAdapter_Safety(common_dev);
                                gridView_safety.setAdapter(mGridViewAdapter_Safety);
                            }
                        }
                    }
                });

                add_dev_Layout_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if (common_dev.size() > 0) {
                            common_dev.remove(position);
                            if (mGridViewAdapter_Safety != null)
                                mGridViewAdapter_Safety.notifyDataSetChanged(common_dev);
                            else {
                                mGridViewAdapter_Safety = new GridViewAdapter_Safety(common_dev);
                                gridView_safety.setAdapter(mGridViewAdapter_Safety);
                            }
                        }
                        return true;
                    }
                });
                add_dev_Layout_ll.setVisibility(View.VISIBLE);
                break;
            case R.id.safety_enabled: //使能开关
                List<String> Enabled = new ArrayList<>();
                Enabled.add("禁用");
                Enabled.add("启用");
                initRadioPopupWindow(safety_enabled, Enabled,3);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.safety_match_code: //对码
//                ToastUtil.showToast(this,"功能正在建设中...");
//                break;
                try {
                    SetSafetyResult safetyResult = new SetSafetyResult();
                    List<SetSafetyResult.SecInfoRowsBean> timerEvent_rows = new ArrayList<>();
                    SetSafetyResult.SecInfoRowsBean bean = new SetSafetyResult.SecInfoRowsBean();
                    bean.setSecDev(common_dev.size());
                    bean.setDevCnt(common_dev.size());
                    bean.setItemCnt(1);
                    bean.setSecId(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecId());
                    bean.setRun_dev_item(common_dev);
                    if ("".equals(safety_name.getText().toString())) {
                        bean.setSecName(CommonUtils.bytesToHexString(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecName().getBytes("GB2312")));
                    } else {
                        try {
                            //名称名称
                            bean.setSecName(CommonUtils.bytesToHexString(safety_name.getText().toString().getBytes("GB2312")));
                        } catch (UnsupportedEncodingException e) {
                            ToastUtil.showToast(SafetyActivity.this, "安防名称不合适");
                            return;
                        }
                    }
                    if ("启用".equals(safety_enabled.getText().toString()))
                        bean.setValid(1);
                    else
                        bean.setValid(0);
                    bean.setSecCode(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(Safety_position).getSecCode());

                    for (int i = 0; i < safety_state_data.size(); i++) {
                        if (safety_state_data.get(i).equals(safety_state.getText().toString())) {
                            if (i == 3)
                                bean.setSecType(255);
                            else
                                bean.setSecType(i);
                        }
                    }

                    //关联情景
                    for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                        if (safety_scene.getText().toString().equals(MyApplication.getWareData().getSceneEvents().get(i).getSceneName()))
                            bean.setSceneId(i);
                    }
                    timerEvent_rows.add(bean);
                    safetyResult.setDatType(32);
                    safetyResult.setDevUnitID(GlobalVars.getDevid());
                    safetyResult.setSubType1(7);
                    safetyResult.setItemCnt(1);
                    safetyResult.setSubType2(0);
                    safetyResult.setSec_info_rows(timerEvent_rows);
                    Gson gson = new Gson();
                    Log.e("对码数据", gson.toJson(safetyResult));
                    initDialog("正在进行...");
                    MyApplication.sendMsg(gson.toJson(safetyResult));
                } catch (Exception e) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    Log.e("对码数据", "对码数据异常" + e);
                    ToastUtil.showToast(this, "对码数据异常,请检查数据是否合适");
                }
                break;
            case R.id.close_open_safety: //状态
                getDialog();
                break;
            case R.id.safety_state: //状态
                initRadioPopupWindow(safety_state, safety_state_data,4);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.safety_scene://关联情景
                initRadioPopupWindow(safety_scene, safety_scene_name,2);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.safety_type:// type
                initRadioPopupWindow(safety_type, safety_state_data1, 0);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.safety_all_close://全部撤防
                //TODO  全局撤防待完成
//                getSharedPreferences("profile",
//                        Context.MODE_PRIVATE).edit().putBoolean("IsDisarming", true).commit();
//                ToastUtil.showToast(this, "撤防成功");
//                dialog.dismiss();

                String ctlStr2 = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                        ",\"datType\":32" +
                        ",\"subType1\":0" +
                        ",\"subType2\":255" +
                        "}";
                MyApplication.sendMsg(ctlStr2);
                getSharedPreferences("profile",
                        Context.MODE_PRIVATE).edit().putBoolean("IsDisarming", true).commit();
                dialog.dismiss();
                break;
            case R.id.safety_all_open://全部布防
                //TODO  全局布防待完成
                int subType2 = 0;
                for (int i = 0; i < safety_state_data.size(); i++) {
                    if (safety_type.getText().toString().equals(safety_state_data.get(i)))
//                        if (i == 3)
//                            subType2 = 255;
//                        else subType2 = i;
                        subType2 = i;
                }
                String ctlStr1 = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                        ",\"datType\":32" +
                        ",\"subType1\":0" +
                        ",\"subType2\":" + subType2 +
                        "}";
                MyApplication.sendMsg(ctlStr1);
                getSharedPreferences("profile", Context.MODE_PRIVATE)
                        .edit().putBoolean("IsDisarming", false).commit();
                dialog.dismiss();
//                getSharedPreferences("profile", Context.MODE_PRIVATE)
//                        .edit().putBoolean("IsDisarming", false).commit();
//                dialog.dismiss();
                break;
            case R.id.tv_equipment_parlour://添加设备 选择房间
                initRadioPopupWindow(tv_text_parlour, home_text,1);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.equipment_close: //关闭  添加设备界面
                add_dev_Layout_ll.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 初始化防区名称
     */
    private void initRecycleView(int safety_position) {
        mDialog.dismiss();
        adapter_Timer = new RecyclerViewAdapter_Safety(MyApplication.getWareData().getResult_safety());
        adapter_Timer.setSelectPosition(safety_position);
        RecyclerView_timer.setAdapter(adapter_Timer);

        adapter_Timer.setOnItemClick(new RecyclerViewAdapter_Safety.SafetyViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = MyApplication.getWareData().getResult_safety().getSec_info_rows().size();
                if (listSize > 0) {
                    if (position < listSize) {
                        Safety_position = position;
                        initData(position);
                        initGridView(position);
                    } else {
                        getDialog();
                    }
                } else {
                    ToastUtil.showToast(SafetyActivity.this, "数据异常");
                }
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 初始化自定义dialog
     * 新增情景
     */
    CustomDialog dialog;

    public void getDialog() {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_safety1);
        dialog.show();
        safety_type = (TextView) dialog.findViewById(R.id.safety_type);
        safety_all_close = (Button) dialog.findViewById(R.id.safety_all_close);
        safety_all_open = (Button) dialog.findViewById(R.id.safety_all_open);
        safety_all_close.setOnClickListener(this);
        safety_all_open.setOnClickListener(this);
        safety_type.setOnClickListener(this);
        //全局布防类型
        int safety_type_position = getSharedPreferences("profile", Context.MODE_PRIVATE)
                .getInt("safety_style", 0);
        if (safety_type_position == 255)
            safety_type_position = 3;
        safety_type.setText(safety_state_data.get(safety_type_position));
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text, int tag) {

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
        if (tag == 0) {
            popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 150);
        } else {
            popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 200);
        }
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

                if (view_parent.getId() == R.id.tv_equipment_parlour) {
                    dev = new ArrayList<>();
                    for (int i = 0; i < mWareDev.size(); i++) {
                        if (mWareDev.get(i).getRoomName().equals(home_text.get(position)))
                            dev.add(mWareDev.get(i));
                    }
                    home_position = position;
                    if (Equipadapter != null)
                        Equipadapter.notifyDataSetChanged(dev);
                    else {
                        Equipadapter = new SafetyActivity.EquipmentAdapter(dev, SafetyActivity.this);
                        gridView_safety.setAdapter(Equipadapter);
                    }
                }
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

    /**
     * RecyclerView Adapter
     */
    static class RecyclerViewAdapter_Safety extends RecyclerView.Adapter<RecyclerViewAdapter_Safety.SafetyViewHolder> {
        private List<SetSafetyResult.SecInfoRowsBean> list;
        private int mPosition = 0;
        private int[] image = {R.drawable.zaijiamoshi, R.drawable.waichumoshi,
                R.drawable.yingyuanmoshi, R.drawable.jiuqingmoshi,
                R.drawable.huikemoshi};
        private SafetyViewHolder.OnItemClick onItemClick;

        public RecyclerViewAdapter_Safety(SetSafetyResult result) {
            if (list == null)
                list = new ArrayList<>();
            for (int i = 0; i < result.getSec_info_rows().size(); i++) {
                list.add(result.getSec_info_rows().get(i));
            }
        }

        public void setSelectPosition(int safety_position) {
            mPosition = safety_position;
        }

        public void setOnItemClick(RecyclerViewAdapter_Safety.SafetyViewHolder.OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        @Override
        public RecyclerViewAdapter_Safety.SafetyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, null);
            RecyclerViewAdapter_Safety.SafetyViewHolder holder = new RecyclerViewAdapter_Safety.SafetyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter_Safety.SafetyViewHolder holder, final int position) {

            if (position < list.size()) {
                if (mPosition == position) {
                    holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
                } else {
                    holder.itemView.setBackgroundResource(R.color.color_08143F);  //其他项背景
                }
                holder.iv.setImageResource(image[position % 5]);
                holder.tv.setText(list.get(position).getSecName());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        int pos = holder.getLayoutPosition();
                        mPosition = pos;
                        onItemClick.OnItemClick(holder.itemView, pos);
                        notifyDataSetChanged();
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClick != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClick.OnItemLongClick(holder.itemView, pos);
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class SafetyViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;

            public SafetyViewHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.img_list_item);
                tv = (TextView) itemView.findViewById(R.id.text_list_item);
            }

            public interface OnItemClick {
                void OnItemClick(View view, int position);

                void OnItemLongClick(View view, int position);
            }
        }
    }


    /**
     * GridView Adapter
     */
    class GridViewAdapter_Safety extends BaseAdapter {

        private List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> timer_list;

        GridViewAdapter_Safety(List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> list) {
            timer_list = list;
        }

        public void notifyDataSetChanged(List<SetSafetyResult.SecInfoRowsBean.RunDevItemBean> list) {
            timer_list = list;
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (timer_list == null)
                return 0;
            return timer_list.size();
        }

        @Override
        public Object getItem(int position) {
            if (timer_list == null)
                return null;
            return timer_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SafetyActivity.this).inflate(R.layout.gridview_item_user, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.equip_name);
                viewHolder.type = (ImageView) convertView.findViewById(R.id.equip_type);
                viewHolder.state = (TextView) convertView.findViewById(R.id.equip_style);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            int type_dev = timer_list.get(position).getDevType();
            if (type_dev == 0) {
                for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                    WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                    if (timer_list.get(position).getDevID() == AirCondDev.getDev().getDevId() &&
                            timer_list.get(position).getCanCpuID().endsWith(AirCondDev.getDev().getCanCpuId())) {
                        viewHolder.name.setText(AirCondDev.getDev().getDevName());
                        if (timer_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.kongtiao1);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.kongtiao2);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 1) {
                for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                    WareTv tv = MyApplication.getWareData().getTvs().get(j);
                    if (timer_list.get(position).getDevID() == tv.getDev().getDevId() &&
                            timer_list.get(position).getCanCpuID().endsWith(tv.getDev().getCanCpuId())) {
                        viewHolder.name.setText(tv.getDev().getDevName());
                        if (timer_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.ds);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.ds);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 2) {
                for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                    WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                    if (timer_list.get(position).getDevID() == box.getDev().getDevId() &&
                            timer_list.get(position).getCanCpuID().endsWith(box.getDev().getCanCpuId())) {
                        viewHolder.name.setText(box.getDev().getDevName());
                        if (timer_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.jidinghe);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.jidinghe);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 3) {
                for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                    WareLight Light = MyApplication.getWareData().getLights().get(j);
                    if (timer_list.get(position).getDevID() == Light.getDev().getDevId() &&
                            timer_list.get(position).getCanCpuID().endsWith(Light.getDev().getCanCpuId())) {
                        viewHolder.name.setText(Light.getDev().getDevName());
                        if (timer_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.light);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.lightk);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 4) {
                for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                    WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                    if (timer_list.get(position).getDevID() == Curtain.getDev().getDevId() &&
                            timer_list.get(position).getCanCpuID().endsWith(Curtain.getDev().getCanCpuId())) {
                        viewHolder.name.setText(Curtain.getDev().getDevName());
                        if (timer_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.quanguan);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.quankai);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            }

            return convertView;
        }

        private class ViewHolder {
            private TextView name, state;
            private ImageView type;
        }
    }

    /**
     * 添加设备然后设备列表适配器
     */
    class EquipmentAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<WareDev> listViewItems;

        public EquipmentAdapter(List<WareDev> title, Context context) {
            super();
            listViewItems = title;
            mInflater = LayoutInflater.from(context);
        }

        public void notifyDataSetChanged(List<WareDev> title) {
            listViewItems = title;
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (null != listViewItems)
                return listViewItems.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return listViewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            SafetyActivity.EquipmentAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.equipment_listview_item, null);
                viewHolder = new SafetyActivity.EquipmentAdapter.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.equipment_tv);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (SafetyActivity.EquipmentAdapter.ViewHolder) convertView.getTag();
            viewHolder.title.setText(listViewItems.get(position).getDevName());
            if (listViewItems.get(position).getType() == 0)
                viewHolder.image.setImageResource(image[0]);
            else if (listViewItems.get(position).getType() == 1)
                viewHolder.image.setImageResource(image[1]);
            else if (listViewItems.get(position).getType() == 2)
                viewHolder.image.setImageResource(image[2]);
            else if (listViewItems.get(position).getType() == 3)
                viewHolder.image.setImageResource(image[3]);
            else
                viewHolder.image.setImageResource(image[4]);
            return convertView;
        }

        public class ViewHolder {
            public TextView title;
            public ImageView image;
        }
    }
}
