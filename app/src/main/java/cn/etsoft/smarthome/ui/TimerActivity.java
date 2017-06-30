package cn.etsoft.smarthome.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.domain.Timer_Data;
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
import cn.etsoft.smarthome.widget.MultiChoicePopWindow;

import static cn.etsoft.smarthome.R.id.tv_equipment_parlour;

/**
 * 定时器界面
 * Created by F-B-L on 2017/5/18.
 */
public class TimerActivity extends FragmentActivity implements View.OnClickListener {

    private Button btn_save;
    private TextView tv_enabled, tv_time_week, tv_time_start, tv_time_end, tv_week_repeat, add_dev_timer,
            tv_all_network, tv_text_parlour, add_dev_Layout_close;
    private int[] image = new int[]{R.drawable.kongtiao, R.drawable.tv_0, R.drawable.jidinghe, R.drawable.dengguang, R.drawable.chuanglian};
    private EditText et_name;
    private GridView gridView_timer;
    private ListView add_dev_Layout_lv;
    private LinearLayout add_dev_Layout_ll;
    private RecyclerView RecyclerView_timer;
    private Dialog mDialog;
    private RecyclerViewAdapter_Timer adapter_Timer;
    private PopupWindow popupWindow;
    private MultiChoicePopWindow mMultiChoicePopWindow;
    private TimePicker mTimePicker;
    private TimePickerDialog mTimePickerDialog;
    private GridViewAdapter_Timer gridViewAdapter_Timer;
    private int DATA_TO_NETWORK;
    //添加设备房间position；
    private int home_position;
    //定时器位置position
    private int Timer_position;
    private List<String> home_text;
    private List<WareDev> dev;
    private List<WareDev> mWareDev;
    private List<Timer_Data.TimerEventRowsBean.RunDevItemBean> common_dev;
    private TimerActivity.EquipmentAdapter Equipadapter;
    private boolean IsHaveData;
    String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
            ",\"datType\":17" +
            ",\"subType1\":0" +
            ",\"subType2\":0" +
            "}";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决弹出键盘压缩布局的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_timer);
        initDialog("初始化数据中...");
        //初始化组件
        initView();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 17) {
                    IsHaveData = true;
                    if(mDialog!= null)
                    mDialog.dismiss();
                    //初始化RecycleView
                    initRecycleView(Timer_position);
                    initGridView(Timer_position);
                    initData(Timer_position);
                    if (Timer_position != 0){
                        ToastUtil.showToast(TimerActivity.this,"保存成功");
                    }
                }
                if(what == 19){
                    MyApplication.sendMsg(ctlStr);
                    if (Timer_position == 0){
                        ToastUtil.showToast(TimerActivity.this,"保存成功");
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
        btn_save = (Button) findViewById(R.id.save);
        tv_enabled = (TextView) findViewById(R.id.enabled);
        tv_time_week = (TextView) findViewById(R.id.time_week);
        tv_time_start = (TextView) findViewById(R.id.time_start);
        tv_time_end = (TextView) findViewById(R.id.time_end);
        tv_week_repeat = (TextView) findViewById(R.id.week_repeat);
        tv_all_network = (TextView) findViewById(R.id.all_network);
        add_dev_timer = (TextView) findViewById(R.id.add_dev_timer);
        et_name = (EditText) findViewById(R.id.name);
        gridView_timer = (GridView) findViewById(R.id.gridView_timer);

        //添加设备布局
        add_dev_Layout_lv = (ListView) findViewById(R.id.equipment_loyout_lv);
        add_dev_Layout_close = (TextView) findViewById(R.id.equipment_close);
        tv_text_parlour = (TextView) findViewById(tv_equipment_parlour);
        add_dev_Layout_ll = (LinearLayout) findViewById(R.id.add_dev_Layout_ll);
        add_dev_Layout_close.setOnClickListener(this);
        tv_text_parlour.setOnClickListener(this);


        btn_save.setOnClickListener(this);
        add_dev_timer.setOnClickListener(this);
        tv_enabled.setOnClickListener(this);
        tv_time_week.setOnClickListener(this);
        tv_time_start.setOnClickListener(this);
        tv_time_end.setOnClickListener(this);
        tv_week_repeat.setOnClickListener(this);
        tv_all_network.setOnClickListener(this);
        et_name.setOnClickListener(this);

        RecyclerView_timer = (RecyclerView) findViewById(R.id.RecyclerView_timer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView_timer.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    /**
     * 初始化GridView
     */
    public void initGridView(int RecycleViewPosition) {
        common_dev = MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(RecycleViewPosition).getRun_dev_item();
        gridViewAdapter_Timer = new GridViewAdapter_Timer(common_dev);
        gridView_timer.setAdapter(gridViewAdapter_Timer);

        gridView_timer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                common_dev.remove(position);
                gridViewAdapter_Timer.notifyDataSetChanged(common_dev);
                return true;
            }
        });
        gridView_timer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (common_dev.get(position).getBOnOff() == 0)
                    common_dev.get(position).setBOnOff(1);
                else
                    common_dev.get(position).setBOnOff(0);
                gridViewAdapter_Timer.notifyDataSetChanged(common_dev);
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
        if (MyApplication.getWareData().getTimer_data().getTimerEvent_rows() == null && MyApplication.getWareData().getTimer_data().getTimerEvent_rows().size() == 0)
            return;
        et_name.setText("");
        et_name.setHint(MyApplication.getWareData().getTimer_data()
                .getTimerEvent_rows().get(timer_position).getTimerName());
        if (MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(timer_position).getRun_dev_item() == null
                || MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(timer_position).getRun_dev_item().size() == 0) {
//            tv_enabled.setText("禁用");
//            tv_time_start.setText("点击选择时间");
//            tv_time_week.setText("点击选择星期");
//            tv_time_end.setText("点击选择时间");
//            tv_week_repeat.setText("否");
//            tv_all_network.setText("是");
            ToastUtil.showToast(this,"该定时器下没有设备");
        }
//        else {
            if (MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(timer_position).getValid() == 1)
                tv_enabled.setText("启用");
            else tv_enabled.setText("禁用");

            List<Integer> data_start = MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(timer_position).getTimSta();
            String startTime = data_start.get(0) + " : " + data_start.get(1);
            tv_time_start.setText(startTime);

            int weekSelect_10 = data_start.get(3);
            String weekSelect_2 = reverseString(Integer.toBinaryString(weekSelect_10));
            String weekSelect_2_data = "";
            for (int i = 0; i < weekSelect_2.toCharArray().length; i++) {
                if (weekSelect_2.toCharArray()[i] == '1')
                    weekSelect_2_data += " " + (i + 1);
            }
            tv_time_week.setText("星期集： " + weekSelect_2_data + "");

            List<Integer> data_end = MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(timer_position).getTimEnd();
            String endtime = data_end.get(0) + " : " + data_end.get(1);
            tv_time_end.setText(endtime);

            if (data_end.get(3) == 1) tv_week_repeat.setText("是");
            else tv_week_repeat.setText("否");
            tv_all_network.setText("是");
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
            ToastUtil.showToast(TimerActivity.this, "获取数据异常，请稍后在试");
            return;
        }
        switch (v.getId()) {
            case R.id.save://保存
                try {
                    Timer_Data time_data = new Timer_Data();
                    List<Timer_Data.TimerEventRowsBean> timerEvent_rows = new ArrayList<>();
                    Timer_Data.TimerEventRowsBean bean = new Timer_Data.TimerEventRowsBean();
                    bean.setDevCnt(common_dev.size());
                    bean.setEventId(MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(Timer_position).getEventId());
                    bean.setRun_dev_item(common_dev);
                    if ("".equals(et_name.getText().toString())) {
                        bean.setTimerName(CommonUtils.bytesToHexString(MyApplication.getWareData().getTimer_data().getTimerEvent_rows().get(Timer_position).getTimerName().getBytes("GB2312")));
                    }else {
                        try {
                            //触发器名称
                            bean.setTimerName(CommonUtils.bytesToHexString(et_name.getText().toString().getBytes("GB2312")));
                        } catch (UnsupportedEncodingException e) {
                            ToastUtil.showToast(TimerActivity.this, "定时器名称不合适");
                            return;
                        }
                    }

                    List<Integer> time_Data_start = new ArrayList<>();
                    String time_start = tv_time_start.getText().toString();
                    String time_end = tv_time_end.getText().toString();
                    if ("点击选择时间".equals(time_start) || "点击选择时间".equals(time_end)){
                        ToastUtil.showToast(TimerActivity.this, "请选择时间");
                        return;
                    }
                    time_Data_start.add(Integer.parseInt(time_start.substring(0, time_start.indexOf(" : "))));
                    time_Data_start.add(Integer.parseInt(time_start.substring(time_start.indexOf(" : ") + 3)));
                    time_Data_start.add(0);
                    if(str2num(tv_time_week.getText().toString()) == 0){
                        ToastUtil.showToast(TimerActivity.this, "请选择星期");
                        return;
                    }
                    time_Data_start.add(str2num(tv_time_week.getText().toString()));
                    bean.setTimSta(time_Data_start);
                    List<Integer> time_Data_end = new ArrayList<>();
                    time_Data_end.add(Integer.parseInt(time_end.substring(0, time_end.indexOf(" : "))));
                    time_Data_end.add(Integer.parseInt(time_end.substring(time_end.indexOf(" : ") + 3)));
                    time_Data_end.add(0);

                    if ("是".equals(tv_week_repeat.getText().toString()))
                        time_Data_end.add(1);
                    else
                        time_Data_end.add(0);
                    bean.setTimEnd(time_Data_end);

                    if (time_Data_start.get(0)> time_Data_end.get(0)){
                        ToastUtil.showToast(TimerActivity.this, "开始时间不能比结束时间迟");
                        return;
                    }

                    if ("启用".equals(tv_enabled.getText().toString()))
                        bean.setValid(1);
                    else
                        bean.setValid(0);
                    timerEvent_rows.add(bean);
                    time_data.setDatType(19);
                    time_data.setDevUnitID(GlobalVars.getDevid());
                    time_data.setItemCnt(1);
                    time_data.setSubType1(0);
                    time_data.setSubType2(0);
                    time_data.setTimerEvent_rows(timerEvent_rows);
                    Gson gson = new Gson();
                    Log.e("0000", gson.toJson(time_data));
                    initDialog("保存数据中...");
                    MyApplication.sendMsg(gson.toJson(time_data));
                } catch (Exception e){
                    if (mDialog!= null)
                        mDialog.dismiss();
                    Log.e("保存定时器数据", "保存数据异常"+e);
                    ToastUtil.showToast(this, "保存数据异常,请检查数据是否合适");
                }
                break;
            case R.id.add_dev_timer://点击添加设备按钮事件

                //添加页面的item点击，以及listview的初始化
                Equipadapter = new TimerActivity.EquipmentAdapter(initGridViewData(home_position), this);
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
                                    Toast.makeText(TimerActivity.this, "设备已存在！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (tag) {
                            if (common_dev.size() == 4) {
                                ToastUtil.showToast(TimerActivity.this, "定时设备最多4个！");
                                return;
                            }
                            Timer_Data.TimerEventRowsBean.RunDevItemBean bean = new Timer_Data.TimerEventRowsBean.RunDevItemBean();
                            bean.setDevID(item.getDevId());
                            bean.setBOnOff(item.getbOnOff());
                            bean.setDevType(item.getType());
                            bean.setCanCpuID(item.getCanCpuId());
                            common_dev.add(bean);
                            if (gridViewAdapter_Timer != null)
                                gridViewAdapter_Timer.notifyDataSetChanged(common_dev);
                            else {
                                gridViewAdapter_Timer = new GridViewAdapter_Timer(common_dev);
                                gridView_timer.setAdapter(gridViewAdapter_Timer);
                            }
                        }
                    }
                });

                add_dev_Layout_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if (common_dev.size() > 0) {
                            common_dev.remove(position);
                            if (gridViewAdapter_Timer != null)
                                gridViewAdapter_Timer.notifyDataSetChanged(common_dev);
                            else {
                                gridViewAdapter_Timer = new GridViewAdapter_Timer(common_dev);
                                gridView_timer.setAdapter(gridViewAdapter_Timer);
                            }
                        }
                        return true;
                    }
                });
                add_dev_Layout_ll.setVisibility(View.VISIBLE);
                break;
            case R.id.enabled: //使能开关
                List<String> Enabled = new ArrayList<>();
                Enabled.add("禁用");
                Enabled.add("启用");
                initRadioPopupWindow(tv_enabled, Enabled);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.time_week: //星期时间
                initWeekDialog(tv_time_week);
                break;
            case R.id.time_start://开始时间
                initTimePickerDialog(tv_time_start);
                break;
            case R.id.time_end:// 结束时间
                initTimePickerDialog(tv_time_end);
                break;
            case R.id.week_repeat://是否周重复
                List<String> Week_repeat = new ArrayList<>();
                Week_repeat.add("否");
                Week_repeat.add("是");
                initRadioPopupWindow(tv_week_repeat, Week_repeat);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.all_network://是否开开全网
                List<String> All_network = new ArrayList<>();
                All_network.add("否");
                All_network.add("是");
                initRadioPopupWindow(tv_all_network, All_network);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.tv_equipment_parlour://添加设备 选择房间
                initRadioPopupWindow(tv_text_parlour, home_text);
                popupWindow.showAsDropDown(v, 0, 0);

                break;
            case R.id.equipment_close: //关闭  添加设备界面
                add_dev_Layout_ll.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 得到字符串中的数字和
     *
     * @param str
     * @return
     */
    public int str2num(String str) {
        str = str.replaceAll("\\D", "");
        List<Integer> number = new ArrayList<>();
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                number.add(Integer.valueOf(String.valueOf(str.charAt(i))));
            }
        }
        String s = "";
        byte[] week_byte = new byte[8];
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < number.size(); i++) {
                if (j == number.get(i) - 1) {
                    week_byte[j] = 1;
                }
            }
            s += week_byte[j];
        }
        s = reverseString(s);
        return Integer.valueOf(s, 2);
    }


    /**
     * 倒置字符串
     *
     * @param str
     * @return
     */
    public static String reverseString(String str) {
        char[] arr = str.toCharArray();
        int middle = arr.length >> 1;//EQ length/2
        int limit = arr.length - 1;
        for (int i = 0; i < middle; i++) {
            char tmp = arr[i];
            arr[i] = arr[limit - i];
            arr[limit - i] = tmp;
        }
        return new String(arr);
    }

    /**
     * 选择星期dialog
     *
     * @param view 显示控件
     */
    public void initWeekDialog(final View view) {
        List<String> Time_week = new ArrayList<>();
        Time_week.add("星期一");
        Time_week.add("星期二");
        Time_week.add("星期三");
        Time_week.add("星期四");
        Time_week.add("星期五");
        Time_week.add("星期六");
        Time_week.add("星期日");
        if (mMultiChoicePopWindow == null)
            mMultiChoicePopWindow = new MultiChoicePopWindow(this, tv_time_week,
                    Time_week, new boolean[7]);
        mMultiChoicePopWindow.setTitle("请选择星期");
        mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
                int size = selItems.length;
                int data_to_network = 0;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    if (selItems[i]) {
                        stringBuffer.append(i + 1 + " ");
                        data_to_network += Math.pow(2, i);
                    }
                }
                DATA_TO_NETWORK = data_to_network;
                ((TextView) view).setText("星期集：" + stringBuffer.toString());
            }
        });
        mMultiChoicePopWindow.show();
    }

    /**
     * 初始化 时间选择器
     *
     * @param view 时间选择完成后显示时间的控件
     */
    public void initTimePickerDialog(final View view) {
        Time time = new Time();
        time.setToNow();
        mTimePickerDialog = new TimePickerDialog(TimerActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Log.i("=====", "=====END==hour=====" + hourOfDay + "====END===minute=======" + minute);
            }
        }, time.HOUR, time.MINUTE, true);
        mTimePickerDialog.show();
        View inflate = LayoutInflater.from(this).inflate(
                R.layout.timepicker_layout, null);
        mTimePicker = (TimePicker) inflate.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        Button btn_ok = (Button) inflate.findViewById(R.id.time_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TIME", "小时" + mTimePicker.getCurrentHour() + "分钟" + mTimePicker.getCurrentMinute());
                ((TextView) view).setText(mTimePicker.getCurrentHour() + " : " + mTimePicker.getCurrentMinute());
                mTimePickerDialog.dismiss();
            }
        });
        Button btn_cancel = (Button) inflate.findViewById(R.id.time_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.dismiss();
            }
        });
        mTimePickerDialog.setContentView(inflate);
    }

    /**
     * 初始化防区名称
     */
    private void initRecycleView(int timer_position) {
        mDialog.dismiss();
        adapter_Timer = new RecyclerViewAdapter_Timer(MyApplication.getWareData().getTimer_data());
        adapter_Timer.setSelectPosition(timer_position);
        RecyclerView_timer.setAdapter(adapter_Timer);

        adapter_Timer.setOnItemClick(new RecyclerViewAdapter_Timer.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = MyApplication.getWareData().getTimer_data().getTimerEvent_rows().size();
                if (listSize > 0) {
                    Timer_position = position;
                    initData(position);
                    initGridView(position);
                }
            }

            @Override
            public void OnItemLongClick(View view, int position) {

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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 200);
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
                        Equipadapter = new TimerActivity.EquipmentAdapter(dev, TimerActivity.this);
                        gridView_timer.setAdapter(Equipadapter);
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
    static class RecyclerViewAdapter_Timer extends RecyclerView.Adapter<RecyclerViewAdapter_Timer.SceneViewHolder> {
        private List<Timer_Data.TimerEventRowsBean> list;
        private int mPosition = 0;
        private int[] image = {R.drawable.zaijiamoshi, R.drawable.waichumoshi,
                R.drawable.yingyuanmoshi, R.drawable.jiuqingmoshi,
                R.drawable.huikemoshi};
        private SceneViewHolder.OnItemClick onItemClick;

        public RecyclerViewAdapter_Timer(Timer_Data result) {
            if (list == null)
                list = new ArrayList<>();
            for (int i = 0; i < result.getTimerEvent_rows().size(); i++) {
                list.add(result.getTimerEvent_rows().get(i));
            }
        }

        public void setSelectPosition(int timer_position){
            mPosition = timer_position;
        }

        public void setOnItemClick(RecyclerViewAdapter_Timer.SceneViewHolder.OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        @Override
        public RecyclerViewAdapter_Timer.SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, null);
            RecyclerViewAdapter_Timer.SceneViewHolder holder = new RecyclerViewAdapter_Timer.SceneViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter_Timer.SceneViewHolder holder, final int position) {
            if (mPosition == position) {
                holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
            } else {
                holder.itemView.setBackgroundResource(R.color.color_00000000);  //其他项背景
            }
            holder.iv.setImageResource(image[position % 5]);
            holder.tv.setText(list.get(position).getTimerName());
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

        static class SceneViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;

            public SceneViewHolder(View itemView) {
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
    class GridViewAdapter_Timer extends BaseAdapter {

        private List<Timer_Data.TimerEventRowsBean.RunDevItemBean> timer_list;

        GridViewAdapter_Timer(List<Timer_Data.TimerEventRowsBean.RunDevItemBean> list) {
            timer_list = list;
        }

        public void notifyDataSetChanged(List<Timer_Data.TimerEventRowsBean.RunDevItemBean> list) {
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
                convertView = LayoutInflater.from(TimerActivity.this).inflate(R.layout.gridview_item_user, null);
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
            TimerActivity.EquipmentAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.equipment_listview_item, null);
                viewHolder = new TimerActivity.EquipmentAdapter.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.equipment_tv);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (TimerActivity.EquipmentAdapter.ViewHolder) convertView.getTag();
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
