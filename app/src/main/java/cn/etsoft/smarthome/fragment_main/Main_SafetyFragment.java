package cn.etsoft.smarthome.fragment_main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.ServiceSubclass;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.adapter_main.SafetyAdapter;
import cn.etsoft.smarthome.domain.Safety_Data;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDatePicker;

/**
 * Created by Say GoBay on 2016/11/28.
 * 暗安防模块
 */
public class Main_SafetyFragment extends Fragment implements View.OnClickListener {
    private LayoutInflater inflater;
    private TextView safety;
    private ImageView screen;
    private CustomDatePicker time_start, time_end;
    private ListView listView;
    private List<String> safetyName;
    private PopupWindow popupWindow;
    private int safety_position = 0;
    private SafetyAdapter safetyAdapter;
    private Safety_Data safety_Data;
    //所有报警信息
    private List<Safety_Data.Data_Data> data_data_all;
    //防区的报警信息
    private List<Safety_Data.Data_Data> data_data_safety;
    //时间区间内的防区的报警信息
    private List<Safety_Data.Data_Data> data_data;
    private List<String> getTime;
    private List<Long> Time;
    private DatePickerDialog mDatePickerDialog;
    private DatePicker mDatePicker;
    private Dialog mDialog;
    private long startTime = 0, endTime = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //解决弹出键盘压缩布局的问题
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.fragment_safety_home, container, false);
        this.inflater = inflater;
        initDialog("初始化数据中...");
        safety_Data = Dtat_Cache.readFile_safety(true);
        //初始化控件
        initView(view);
        //加载数据
        initData();
        ServiceSubclass.setOnGetSafetyDataListener(new ServiceSubclass.OnGetSafetyDataListener() {
            @Override
            public void getSafetyData() {
                safety_Data = Dtat_Cache.readFile_safety(true);
                initData();
            }
        });
        return view;
    }

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(getActivity());
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
     * 初始化控件
     */
    private void initView(View view) {
        time_start = (CustomDatePicker) view.findViewById(R.id.time_start);
        time_end = (CustomDatePicker) view.findViewById(R.id.time_end);
        safety = (TextView) view.findViewById(R.id.safety);
        screen = (ImageView) view.findViewById(R.id.screen);
        listView = (ListView) view.findViewById(R.id.listView);
        if (MyApplication.getWareData().getResult_safety() == null || MyApplication.getWareData().getResult_safety().getSec_info_rows().size() == 0)
            return;
        safetyName = new ArrayList<>();
        safetyName.add("全部");
        for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
            safetyName.add(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecName());
        }
        safety.setText(safetyName.get(safety_position));
        safety.setOnClickListener(this);
        screen.setOnClickListener(this);
        time_start.setDividerColor(0xff63C4E8);
        time_start.setPickerMargin(5);
        time_end.setDividerColor(0xff63C4E8);
        time_end.setPickerMargin(5);
    }

    /**
     * 加载数据
     */
    private void initData() {
        if (safety_Data == null) {
            ToastUtil.showToast(getActivity(), "没有检索到信息");
            mDialog.dismiss();
        } else {
            initData_safety(safety_position, safety_Data, startTime, endTime);
        }
    }

    String start = "", end = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safety:
                initRadioPopupWindow(safety, safetyName);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.screen:
                if ("".equals(time_start.getYear()) || "".equals(time_start.getMonth() + 1) || "".equals(time_start.getDayOfMonth()) || "".equals(time_end.getYear()) || "".equals(time_end.getMonth() + 1) || "".equals(time_end.getDayOfMonth())) {
                    ToastUtil.showToast(getActivity(), "请输入检索时间");
                    return;
                }
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//                    startTime = simpleDateFormat.parse(time_start.getText().toString()).getTime();
//                    endTime = simpleDateFormat.parse(time_end.getText().toString()).getTime();
//                    if (startTime > endTime) {
//                        ToastUtil.showToast(getActivity(), "截止日期不能小于起始日期");
//                        return;
//                    }
//                } catch (ParseException e) {
//                }
                if ((time_start.getMonth() + 1) < 10) {
                    start = String.valueOf(time_start.getYear() + "" + ("0" + (time_start.getMonth() + 1)) + "" + time_start.getDayOfMonth());
                } else {
                    start = String.valueOf(time_start.getYear() + "" + (time_start.getMonth() + 1) + "" + time_start.getDayOfMonth());
                }
                startTime = Integer.parseInt(start);
                if ((time_start.getMonth() + 1) < 10) {
                    end = String.valueOf(time_end.getYear() + "" + ("0" + (time_end.getMonth() + 1)) + "" + time_end.getDayOfMonth());
                } else {
                    end = String.valueOf(time_end.getYear() + "" + (time_end.getMonth() + 1) + "" + time_end.getDayOfMonth());
                }
                endTime = Integer.parseInt(end);
                if (startTime > endTime) {
                    ToastUtil.showToast(getActivity(), "截止日期不能小于起始日期");
                    return;
                }
                initData();
                break;
        }
    }

    /**
     * 检索报警信息
     *
     * @param safety_position
     * @param safety_Data
     * @param startTime
     * @param endTime
     */
    String month_get, day_get;

    private void initData_safety(int safety_position, Safety_Data safety_Data, long startTime, long endTime) {
        data_data_all = safety_Data.getDatas();
        if (data_data_all == null) {
            data_data_all = new ArrayList<>();
        } else {
            data_data_all = safety_Data.getDatas();
        }
        data_data_safety = new ArrayList<>();
        data_data = new ArrayList<>();

        if (startTime == 0 || endTime == 0) {
            data_data = data_data_all;
        } else {
            if (safety_position == 0) {//全部防区
                data_data_safety = data_data_all;
                //获取到的信息的时间
                getTime();
                //获取数据
                judge(startTime, endTime);
            } else {//各个防区
                if ((safety_position - 1) >= data_data_all.size()) {
                    ToastUtil.showToast(getActivity(), "没有检索到信息");
                    return;
                }
                for (int i = 0; i < data_data_all.size(); i++) {
                    if (safety_position == data_data_all.get(i).getId()) {
                        data_data_safety.add(data_data_all.get(i));
                    }
                }
                //获取到的信息的时间
                getTime();
                //获取数据
                judge(startTime, endTime);
            }
        }
        safetyAdapter = new SafetyAdapter(data_data, getActivity(), inflater);
        listView.setAdapter(safetyAdapter);
        mDialog.dismiss();
    }

    /**
     * 获取到的信息的时间
     *
     * @return
     */
    private List<Long> getTime() {
        getTime = new ArrayList<>();
        Time = new ArrayList<>();
        for (int i = 0; i < data_data_safety.size(); i++) {
            if (data_data_safety.get(i).getMonth() < 10) {
                month_get = 0 + "" + data_data_safety.get(i).getMonth();
            } else {
                month_get = "" + data_data_safety.get(i).getMonth();
            }
            if (data_data_safety.get(i).getDay() < 10) {
                day_get = 0 + "" + data_data_safety.get(i).getDay();
            } else {
                day_get = "" + data_data_safety.get(i).getDay();
            }
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Long a = simpleDateFormat.parse(data_data_safety.get(i).getYear() + "-" + month_get + "-" + day_get).getTime();
//                getTime.add(String.valueOf(a));
//            } catch (ParseException e) {
//            }
//            Time.add(Long.valueOf(getTime.get(i)));
            getTime.add(data_data_safety.get(i).getYear() + "" + month_get + "" + day_get);
            Time.add(Long.valueOf(getTime.get(i)));

        }
        return Time;
    }

    //获取数据
    private List<Safety_Data.Data_Data> judge(long startTime, long endTime) {
        boolean match = false;
        for (int i = 0; i < Time.size(); i++) {
            if (startTime <= Time.get(i) && Time.get(i) <= endTime) {
                data_data.add(data_data_safety.get(i));
                match = true;
            }
        }
        if (!match) {
            data_data = new ArrayList<>();
            ToastUtil.showToast(getActivity(), "没有检索到信息");
        }
        return data_data;
    }

    /**
     * 选择日期
     *
     * @param view
     */
    private void initDatePickerDialog(final View view) {
        Date date = new Date();
        mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i("=====", "=====" + year + "=====" + monthOfYear + "=====" + dayOfMonth);
            }
        }, date.getYear(), date.getMonth(), date.getDay());
        mDatePickerDialog.show();
        View inflate = LayoutInflater.from(getActivity()).inflate(
                R.layout.datepicker_layout, null);
        mDatePicker = (DatePicker) inflate.findViewById(R.id.datePicker);
        mDatePicker.setCalendarViewShown(false);
        Button btn_ok = (Button) inflate.findViewById(R.id.time_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TIME", "年" + mDatePicker.getYear() + "月" + (mDatePicker.getMonth() + 1) + "日" + mDatePicker.getDayOfMonth());
                ((TextView) view).setText(mDatePicker.getYear() + "-" + (mDatePicker.getMonth() + 1) + "-" + mDatePicker.getDayOfMonth());
                mDatePickerDialog.dismiss();
            }
        });
        Button btn_cancel = (Button) inflate.findViewById(R.id.time_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.dismiss();
            }
        });
        mDatePickerDialog.setContentView(inflate);
    }


    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(getActivity(), R.layout.popupwindow_equipment_listview, null);
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
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, getActivity());
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                safety_position = position;
                popupWindow.dismiss();
            }
        });
        //popupWindow页面之外可点
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
