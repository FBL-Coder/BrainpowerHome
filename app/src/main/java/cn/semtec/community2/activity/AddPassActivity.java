package cn.semtec.community2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.linphone.squirrel.squirrelCallImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.util.PickerViewAnimateUtil;
import cn.semtec.community2.util.ToastUtil;
import cn.semtec.community2.util.Util;
import cn.semtec.community2.view.TimePickerView;
import cn.semtec.community2.view.WheelView;

public class AddPassActivity extends Activity implements View.OnClickListener {
    public static AddPassActivity instance = null;
    private ImageView btn_back;
    private Button btn_get_pass;
    private TextView tv_build;
    private TextView textView5;
    // 对应textview 的 扩大范围版，用于点击监听
    private View tab_4;
    private View tab_5;
    //
    private String buildnum;
    // dialog 数据
    // 单元内所有 楼栋的数据集合
    private ArrayList<HashMap<String, String>> build_obj = new ArrayList<HashMap<String, String>>();
    public ArrayList<Integer> yearAL = new ArrayList<>();
    public ArrayList<Integer> monthAL = new ArrayList<>();
    public ArrayList<Integer> dayAL = new ArrayList<>();
    public int monthIndex = 0;
    private String pass;
    private TimePickerView pvTime;
    private String sendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass);
        build_obj = VideoFragment.mlist;
        setView();
        setListener();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        instance = this;

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
//        selectTime();


    }

    private void selectTime() {
        monthIndex = 0;
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        yearAL.clear();
        monthAL.clear();
        dayAL.clear();
        for (int i = 0;i < 8;i++){
            String date = PickerViewAnimateUtil.getLongTime(i);
            String[] s = date.split("-");
            int endYear = Integer.parseInt(s[0]);
            int endMonth = Integer.parseInt(s[1]);
            int endDay = Integer.parseInt(s[2]);
            if (!monthAL.contains(endMonth)){
                monthAL.add(endMonth);
            }
            if (!yearAL.contains(endYear)){
                yearAL.add(endYear);
            }
            dayAL.add(endDay);
        }
        pvTime.setRange(calendar.get(Calendar.YEAR), yearAL.get(yearAL.size() - 1), calendar.get(Calendar.MONTH) + 1, monthAL.get(monthAL.size() - 1), calendar.get(Calendar.DAY_OF_MONTH), dayAL.get(dayAL.size() - 1));
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                textView5.setText(PickerViewAnimateUtil.getTime(date));
                sendText = PickerViewAnimateUtil.getTime2(date);
            }
        });
    }

    private void setView() {
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_get_pass = (Button) findViewById(R.id.btn_get_pass);
        tv_build = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);

        tab_4 = findViewById(R.id.tab_4);
        tab_5 = findViewById(R.id.tab_5);

    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_get_pass.setOnClickListener(this);
        tab_4.setOnClickListener(this);
        tab_5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_get_pass:
                if ( check()){
                    pass = Util.getRandowNumber();
                    String text = pass+sendText+MyApplication.houseProperty.sipnum;
                            ((squirrelCallImpl) getApplication()).squirrelSendMessage(buildnum, MyApplication.houseProperty.sipaddr,
                                    squirrelCallImpl.serverport, text, 400);
                }
                break;

            case R.id.tab_4:
                if (build_obj.size() > 0 ) {
                    new WheelViewHelper(build_obj, tv_build);
                } else {
                    ToastUtil.s(this, "暂时没有门禁机");
                }
                break;
            case R.id.tab_5:
                selectTime();
                pvTime.show();
                break;
            default:
                break;
        }
    }

    private boolean check(){
        if ( buildnum == null ){
            ToastUtil.s(AddPassActivity.this,"请选择门禁机");
            return false;
        }
        if ( null == (textView5.getText().toString())|| "".equals(textView5.getText().toString())){
            ToastUtil.s(AddPassActivity.this,"请选择密码有效期");
            return false;
        }
        return true;
    }

    private class WheelViewHelper {
        private WheelView wv;

        public WheelViewHelper(final ArrayList<HashMap<String, String>> data, final TextView view) {
            View outerView = getLayoutInflater().inflate(
                    R.layout.wheelview, null);
            wv = (WheelView) outerView.findViewById(R.id.wheelView1);
            // 从对象集合 取出name 建立集合  用于wheelview 的参数
            ArrayList<String> name = new ArrayList<String>();
            for (int i = 0; i < data.size(); i++) {
                name.add((String) data.get(i).get("obj_name"));
            }
            //设置 数据   建立dialog
            if (name.size() > 0) {
                wv.setData(name);
                wv.setDefault(0);

                new AlertDialog.Builder(AddPassActivity.this).setView(outerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i = getSelected();
                                if (i == -1)
                                    return;
                                HashMap<String, String> select = data.get(i);
                                view.setText((String) select.get("obj_name"));
                                //判断是哪个控件开启的Dialog  下载 赋值不同的数据
                                if (view == tv_build) {
                                    buildnum = (String) select.get("obj_sipnum");
                                }
                            }

                        }).show();
            } else {
//                ToastUtil.s(HouseBindingOneActivity.this, "数据错误");
            }
        }

        // 获取选中的index
        public int getSelected() {
            return wv.getSelected();
        }

        // 获取选中的item内容
        public String getSelectedText() {
            return wv.getSelectedText();
        }

        // 设置默认选中项的index
        public void setDefault(int index) {
            wv.setDefault(index);
        }
    }

    public void setNewData(){
        ContentValues values = new ContentValues();
        values.put(DBhelper.DYNAMIC_DATE, textView5.getText().toString());
        values.put(DBhelper.DYNAMIC_NAME, tv_build.getText().toString());
        values.put(DBhelper.DYNAMIC_PASSWORD, pass);

        SQLiteDatabase db = MyApplication.getDB();
        db.insert(DBhelper.DYNAMIC, null, values);
//        ToastUtil.s(this, getString(cn.semtec.community2.R.string.calling_saved));
        db.close();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (instance != null){
            instance = null;
        }
    }
}
