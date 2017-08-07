package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.Calendar;

import cn.etsoft.smarthome.Adapter.ListView.SafetyRecordAdapter;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.Safety_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.Data_Cache;

/**
 * Author：FBL  Time： 2017/8/7.
 */

public class SafetyHomeActivity extends BaseActivity {
    private ListView mSafetyRecord;
    private TextView mSafetySetNow;
    private ImageView mSafetyTime;
    private ImageView mSafetySelect;
    private Safety_Data safetyData;
    private SafetyRecordAdapter adapter;

    @Override
    public void initView() {

        safetyData = Data_Cache.readFile_safety(true);
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
                    safetyData = Data_Cache.readFile_safety(true);
                    if (adapter == null)
                        adapter = new SafetyRecordAdapter(safetyData.getSafetyTime(), SafetyHomeActivity.this);
                    else adapter.notifyDataSetChanged(safetyData.getSafetyTime());
                }
            }
        });
        adapter = new SafetyRecordAdapter(safetyData.getSafetyTime(), this);
        mSafetyRecord.setAdapter(adapter);

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
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
                        ToastUtil.showText(i + "/" + (i1 + 1) + "/" + i2);
                    }

                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "dataPicker");
            }
        });
    }
}
