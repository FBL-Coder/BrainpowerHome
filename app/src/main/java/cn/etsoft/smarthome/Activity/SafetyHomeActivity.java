package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.etsoft.smarthome.Adapter.ListView.SafetyRecordAdapter;
import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.Safety_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.Data_Cache;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/8/7.
 * 首页安防记录
 */

public class SafetyHomeActivity extends BaseActivity implements View.OnClickListener {
    private ListView mSafetyRecord;
    private TextView mSafetySetNow;
    private ImageView mSafetyTime;
    private ImageView mSafetySelect;
    private Safety_Data mSafetyData_All;
    private List<Safety_Data.Safety_Time> ShowListData;
    private SafetyRecordAdapter Safetyadapter;
    private PopupWindow popupWindow, popupWindow_Buche;
    private TextView mSafetyHomeStartTime, mSafetyHomeEndTime,
            mSafetyHomeSelect, mSafetyHomeOk, mSafetyHomeCancle;
    private List<String> mSafetyType, mSafetyBuCheType;
    private int[] starttime_input, endtime_input;
    private TextView mSafetyHomeBuCheSelect, mSafetyHomeBuCheBuFang, mSafetyHomeBuChesCheFang;

    @Override
    public void initView() {

        mSafetyData_All = Data_Cache.readFile_safety(GlobalVars.getDevid(), true);
        setTitleViewVisible(true, R.color.color_4489CA);
        setTitleText("报警记录", 20, Color.WHITE);
        setTitleImageBtn(true, R.drawable.back_image_select, false, 0);
        setLayout(R.layout.activity_safety_home);
        mSafetyRecord = getViewById(R.id.Safety_Record);
        mSafetySetNow = getViewById(R.id.SafetySet_Now);
        mSafetyTime = getViewById(R.id.Safety_Time);
        mSafetySelect = getViewById(R.id.Safety_Select);
    }

    @Override
    public void initData() {
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 32 && subtype1 == 2) {
                    MyApplication.mApplication.dismissLoadDialog();
                    mSafetyData_All = Data_Cache.readFile_safety(GlobalVars.getDevid(), true);
                    if (mSafetyData_All == null)
                        mSafetyData_All = new Safety_Data();
                    if (Safetyadapter == null)
                        Safetyadapter = new SafetyRecordAdapter(mSafetyData_All.getSafetyTime(), SafetyHomeActivity.this);
                    else Safetyadapter.notifyDataSetChanged(mSafetyData_All.getSafetyTime());
                }
                if (datType == 32 && subtype1 == 1) {
                    MyApplication.mApplication.dismissLoadDialog();
                    AppSharePreferenceMgr.put(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, subtype2);
                    ToastUtil.showText("操作成功");
                    if (0 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
                        mSafetySetNow.setText("当前布撤状态 : 24小时布防");
                    if (1 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
                        mSafetySetNow.setText("当前布撤状态 : 在家布防");
                    if (2 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
                        mSafetySetNow.setText("当前布撤状态 : 外出布防");
                    if (255 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
                        mSafetySetNow.setText("当前布撤状态 : 撤防状态");
                }
            }
        });

        mSafetyType = new ArrayList<>();
        mSafetyType.add("全部");
        mSafetyType.add("24小时布防");
        mSafetyType.add("在家布防");
        mSafetyType.add("外出布防");
        mSafetyBuCheType = new ArrayList<>();
        mSafetyBuCheType.add("24小时布防");
        mSafetyBuCheType.add("在家布防");
        mSafetyBuCheType.add("外出布防");

        if (mSafetyData_All == null)
            mSafetyData_All = new Safety_Data();
        Safetyadapter = new SafetyRecordAdapter(mSafetyData_All.getSafetyTime(), this);
        mSafetyRecord.setAdapter(Safetyadapter);

        if (0 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
            mSafetySetNow.setText("当前布撤状态 : 24小时布防");
        if (1 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
            mSafetySetNow.setText("当前布撤状态 : 在家布防");
        if (2 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
            mSafetySetNow.setText("当前布撤状态 : 外出布防");
        if (255 == (int) AppSharePreferenceMgr.get(GlobalVars.SAFETY_TYPE_SHAREPREFERENCE, 0))
            mSafetySetNow.setText("当前布撤状态 : 撤防状态");

        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSafetyTime.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                initRadioPopupWindow(v);
            }
        });
        mSafetySelect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                initSafetyPopupWindow(v);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Safety_Home_StartTime:
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                        starttime_input = new int[3];
                        starttime_input[0] = i;
                        starttime_input[1] = i1;
                        starttime_input[2] = i2;
                        mSafetyHomeStartTime.setText(i + "年" + (i1 + 1) + "月" + i2);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "dataPicker");
                break;
            case R.id.Safety_Home_EndTime:
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                        endtime_input = new int[3];
                        endtime_input[0] = i;
                        endtime_input[1] = i1;
                        endtime_input[2] = i2;
                        mSafetyHomeEndTime.setText(i + "年" + (i1 + 1) + "月" + i2);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "dataPicker");
                break;
            case R.id.Safety_Home_Select:
                ShowSafetyDialog(mSafetyHomeSelect, mSafetyType);
                break;
            case R.id.Safety_Home_Ok:
                if ("".equals(mSafetyHomeStartTime.getText().toString()) || "".equals(mSafetyHomeEndTime.getText().toString())) {
                    ToastUtil.showText("请选择时间");
                    return;
                }
                String start, end;
                if ((starttime_input[1] + 1) < 10) {
                    if (starttime_input[2] < 10)
                        start = String.valueOf(starttime_input[0] + "0" + (starttime_input[1] + 1) + "0" + starttime_input[2]);
                    else
                        start = String.valueOf(starttime_input[0] + "0" + (starttime_input[1] + 1) + starttime_input[2]);
                } else {
                    if (starttime_input[2] < 10)
                        start = String.valueOf(starttime_input[0] + (starttime_input[1] + 1) + "0" + starttime_input[2]);
                    else
                        start = String.valueOf(starttime_input[0] + (starttime_input[1] + 1) + starttime_input[2]);
                }
                int startTime = Integer.parseInt(start);

                if ((endtime_input[1] + 1) < 10) {
                    if (endtime_input[2] < 10)
                        end = String.valueOf(endtime_input[0] + "0" + (endtime_input[1] + 1) + "0" + endtime_input[2]);
                    else
                        end = String.valueOf(endtime_input[0] + "0" + (endtime_input[1] + 1) + endtime_input[2]);
                } else {
                    if (endtime_input[2] < 10)
                        end = String.valueOf(endtime_input[0] + (endtime_input[1] + 1) + "0" + endtime_input[2]);
                    else
                        end = String.valueOf(endtime_input[0] + (endtime_input[1] + 1) + endtime_input[2]);
                }
                int endTime = Integer.parseInt(end);

                if (startTime > endTime) {
                    ToastUtil.showText("截止日期不能小于起始日期");
                    return;
                }

                ShowListData = new ArrayList<>();
                for (int i = 0; i < mSafetyData_All.getSafetyTime().size(); i++) {
                    Safety_Data.Safety_Time time = mSafetyData_All.getSafetyTime().get(i);
                    String Time_run;
                    if (time.getMonth() < 10) {
                        if (time.getDay() < 10)
                            Time_run = String.valueOf(time.getYear() + "0" + time.getMonth() + "0" + time.getDay());
                        else
                            Time_run = String.valueOf(time.getYear() + "0" + time.getMonth() + time.getDay());
                    } else {
                        if (time.getDay() < 10)
                            Time_run = String.valueOf(time.getYear() + time.getMonth() + "0" + time.getDay());
                        else
                            Time_run = String.valueOf(time.getYear() + time.getMonth() + time.getDay());
                    }
                    int run_time = Integer.parseInt(Time_run);

                    if (mSafetyType.get(0).equals(mSafetyHomeSelect.getText())) {
                        if (startTime < run_time && endTime > run_time) {
                            ShowListData.add(time);
                        }
                    } else if (mSafetyType.get(1).equals(mSafetyHomeSelect.getText())) {
                        if (startTime < run_time && endTime > run_time && time.getSafetyBean().getSecType() == 0) {
                            ShowListData.add(time);
                        }
                    } else if (mSafetyType.get(2).equals(mSafetyHomeSelect.getText())) {
                        if (startTime < run_time && endTime > run_time && time.getSafetyBean().getSecType() == 1) {
                            ShowListData.add(time);
                        }
                    } else if (mSafetyType.get(3).equals(mSafetyHomeSelect.getText())) {
                        if (startTime < run_time && endTime > run_time && time.getSafetyBean().getSecType() == 2) {
                            ShowListData.add(time);
                        }
                    }
                }
                if (Safetyadapter == null)
                    Safetyadapter = new SafetyRecordAdapter(ShowListData, SafetyHomeActivity.this);
                else Safetyadapter.notifyDataSetChanged(ShowListData);
                mSafetyRecord.setAdapter(Safetyadapter);
                popupWindow.dismiss();
                break;
            case R.id.Safety_Home_Cancle:
                popupWindow.dismiss();
                break;
            //不撤防设置
            case R.id.Safety_Home_BuChe_Select:
                ShowSafetyDialog(mSafetyHomeBuCheSelect, mSafetyBuCheType);
                break;
            case R.id.Safety_Home_BuChe_BuFang:
                int type = -1;
                if ("".equals(mSafetyHomeBuCheSelect.getText().toString())) {
                    ToastUtil.showText("请选择布防类型");
                    return;
                } else if ("24小时布防".equals(mSafetyHomeBuCheSelect.getText().toString())) {
                    type = 0;
                } else if ("在家布防".equals(mSafetyHomeBuCheSelect.getText().toString())) {
                    type = 1;
                } else if ("外出布防".equals(mSafetyHomeBuCheSelect.getText().toString())) {
                    type = 2;
                }
                SendDataUtil.setBuFangSafetyInfo(type);
                popupWindow_Buche.dismiss();
                MyApplication.mApplication.showLoadDialog(SafetyHomeActivity.this);
                break;
            case R.id.Safety_Home_BuChes_CheFang:
                SendDataUtil.setCheFangSafetyInfo();
                popupWindow_Buche.dismiss();
                MyApplication.mApplication.showLoadDialog(SafetyHomeActivity.this);
                break;

        }
    }

    /**
     * 选择安防类型Dialog
     *
     * @param textView 点击组件
     */

    private void ShowSafetyDialog(final TextView textView, final List<String> text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_popupwindow_safety);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ListView listView = (ListView) dialog.findViewById(R.id.popupWindow_equipment_lv);
        TextView cancle = (TextView) dialog.findViewById(R.id.cancle);
        listView.setBackgroundResource(R.drawable.background);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, SafetyHomeActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(text.get(position));
                dialog.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 筛选条件
     */

    private void initRadioPopupWindow(final View view_parent) {
        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.popupwindow_safety_home, null);
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
        if (popupWindow == null)
            popupWindow = new PopupWindow(customView, 800, 400);
        popupWindow.setContentView(customView);
        mSafetyHomeStartTime = (TextView) customView.findViewById(R.id.Safety_Home_StartTime);
        mSafetyHomeEndTime = (TextView) customView.findViewById(R.id.Safety_Home_EndTime);
        mSafetyHomeSelect = (TextView) customView.findViewById(R.id.Safety_Home_Select);
        mSafetyHomeOk = (TextView) customView.findViewById(R.id.Safety_Home_Ok);
        mSafetyHomeCancle = (TextView) customView.findViewById(R.id.Safety_Home_Cancle);

        mSafetyHomeSelect.setText(mSafetyType.get(0));

        mSafetyHomeStartTime.setOnClickListener(this);
        mSafetyHomeEndTime.setOnClickListener(this);
        mSafetyHomeSelect.setOnClickListener(this);
        mSafetyHomeOk.setOnClickListener(this);
        mSafetyHomeCancle.setOnClickListener(this);
        if (!popupWindow.isShowing())
            popupWindow.showAtLocation(view_parent, Gravity.CENTER, 0, 0);
        else popupWindow.dismiss();
    }

    /**
     * 布撤防
     */

    private void initSafetyPopupWindow(final View view_parent) {
        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.popupwindow_safety_buche, null);
        customView.setFocusable(true);
        customView.setFocusableInTouchMode(true);
        customView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow_Buche != null) {
                        popupWindow_Buche.dismiss();
                    }
                }
                return false;
            }
        });

        mSafetyHomeBuCheSelect = (TextView) customView.findViewById(R.id.Safety_Home_BuChe_Select);
        mSafetyHomeBuCheBuFang = (TextView) customView.findViewById(R.id.Safety_Home_BuChe_BuFang);
        mSafetyHomeBuChesCheFang = (TextView) customView.findViewById(R.id.Safety_Home_BuChes_CheFang);
        mSafetyHomeBuCheSelect.setOnClickListener(this);
        mSafetyHomeBuCheBuFang.setOnClickListener(this);
        mSafetyHomeBuChesCheFang.setOnClickListener(this);
        // 创建PopupWindow实例
        if (popupWindow_Buche == null)
            popupWindow_Buche = new PopupWindow(customView, 800, 300);
        popupWindow_Buche.setContentView(customView);
        if (!popupWindow_Buche.isShowing())
            popupWindow_Buche.showAtLocation(view_parent, Gravity.CENTER, 0, 0);
        else popupWindow_Buche.dismiss();
    }

}
