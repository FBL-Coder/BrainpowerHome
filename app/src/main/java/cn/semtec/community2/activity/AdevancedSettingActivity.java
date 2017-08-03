package cn.semtec.community2.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.squirrel.squirrelCallImpl;

import java.util.Calendar;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.SharedPreferenceUtil;
import cn.semtec.community2.util.ToastUtil;

public class AdevancedSettingActivity extends MyBaseActivity implements View.OnClickListener {

    private Switch btnSwitchDND;
    private Switch btnSwitchCP;
    private Switch btn_switch_auto;
    private ImageView btn_back;
    private View btn_from, btn_to;
    private TextView tv_from, tv_to;
    private SharedPreferenceUtil preference;
    private MyHttpUtil httpUtil;
    private View tab_cp;
    private View tv_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adevanced_setting);
        preference = MyApplication.getSharedPreferenceUtil();
        setView();
        getData();
        setListener();
    }

    private void setView() {
        btnSwitchDND = (Switch) findViewById(R.id.btn_switch_dnd);
        btnSwitchCP = (Switch) findViewById(R.id.btn_switch_cp);
        btn_switch_auto = (Switch) findViewById(R.id.btn_switch_auto);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_from = findViewById(R.id.btn_from);
        btn_to = findViewById(R.id.btn_to);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_to = (TextView) findViewById(R.id.tv_to);
        String fromTime = preference.getString(MyApplication.cellphone + "DNDFrom");
        String toTime = preference.getString(MyApplication.cellphone + "DNDTo");
        if (fromTime.length() > 4) {
            tv_from.setText(fromTime);
        } else {
            tv_from.setText("23:00");
            if (MyApplication.cellphone != null) {
                preference.putString(MyApplication.cellphone + "DNDFrom", tv_from.getText().toString());
            }
        }
        if (toTime.length() > 4) {
            tv_to.setText(toTime);
        } else {
            tv_to.setText("06:00");
            if (MyApplication.cellphone != null) {
                preference.putString(MyApplication.cellphone + "DNDTo", tv_to.getText().toString());
            }
        }

        tab_cp = findViewById(R.id.tab_cp);
        tv_tip = findViewById(R.id.tv_tip);
        if (MyApplication.houseProperty == null || MyApplication.houseProperty.userType != 1) {
            tab_cp.setVisibility(View.GONE);
            tv_tip.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_from.setOnClickListener(this);
        btn_to.setOnClickListener(this);

        btnSwitchDND.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // setchecked()也会触发监听 判断是否为点击
                if (!buttonView.isPressed()) {
                    return;
                }
                // checked true是关闭
                if (isChecked) {
                    // 如果是关闭的 则打开
                    preference.putBoolean(MyApplication.cellphone + "isDND", true);
                    ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.usertool_DND_on));
                } else {
                    preference.putBoolean(MyApplication.cellphone + "isDND", false);
                    ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.usertool_DND_off));
                }
            }
        });

        btnSwitchCP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // setchecked()也会触发监听 判断是否为点击
                if (!buttonView.isPressed()) {
                    return;
                }
                // checked true是关闭
                if (isChecked) {
                    sendToNET(1);
                } else {
                    sendToNET(0);
                }
            }
        });
        btn_switch_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // setchecked()也会触发监听 判断是否为点击
                if (!buttonView.isPressed()) {
                    return;
                }
                // checked true是关闭
                if (isChecked) {
                    // 如果是关闭的 则打开
                    preference.putBoolean(MyApplication.cellphone + "isAuto", true);
//                    ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.usertool_DND_on));
                } else {
                    preference.putBoolean(MyApplication.cellphone + "isAuto", false);
//                    ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.usertool_DND_off));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_from:
                showFromTimeCheck();
                break;
            case R.id.btn_to:
                showToTimeCheck();
                break;
        }
    }

    private void showFromTimeCheck() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String strHour = hourOfDay >= 10 ? String.valueOf(hourOfDay) : "0" + hourOfDay;
                String strMinute = minute >= 10 ? String.valueOf(minute) : "0" + minute;
                String time = strHour + ":" + strMinute;
                tv_from.setText(time);
                if (MyApplication.cellphone != null) {
                    preference.putString(MyApplication.cellphone + "DNDFrom", time);
                }
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void showToTimeCheck() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String strHour = hourOfDay >= 10 ? String.valueOf(hourOfDay) : "0" + hourOfDay;
                String strMinute = minute >= 10 ? String.valueOf(minute) : "0" + minute;
                String time = strHour + ":" + strMinute;
                tv_to.setText(time);
                if (MyApplication.cellphone != null) {
                    preference.putString(MyApplication.cellphone + "DNDTo", time);
                }
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void getData() {
        if (preference.getBoolean(MyApplication.cellphone + "isDND")) {
            btnSwitchDND.setChecked(true);
        } else {
            btnSwitchDND.setChecked(false);
        }
        if (preference.getBoolean(MyApplication.cellphone + "isAuto")) {
            btn_switch_auto.setChecked(true);
        } else {
            btn_switch_auto.setChecked(false);
        }
        if (MyApplication.houseProperty != null && MyApplication.houseProperty.userType == 1) {
            if (preference.contains(MyApplication.houseProperty.houseId + "phonecalltag")) {
                if (preference.getBoolean(MyApplication.houseProperty.houseId + "phonecalltag")) {
                    btnSwitchCP.setChecked(true);
                } else {
                    btnSwitchCP.setChecked(false);
                }
            } else {
                getPhonecalltag();
            }
        }
    }

    private void getPhonecalltag() {
        String url = Constants.CONTENT_PHONECALLTAG + "?houseId=" + MyApplication.houseProperty.houseId;
        httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String mResult = responseInfo.result.toString();
                try {
                    JSONObject jo = new JSONObject(mResult);
                    if (jo.getInt("returnCode") == 0) {
                        LogUtils.i("下载成功");
                        JSONObject args = (JSONObject) jo.get("args");
                        int tag = args.getInt("tag");
                        if (tag == 0) {
                            btnSwitchCP.setChecked(false);
                            preference.putBoolean(MyApplication.houseProperty.houseId + "phonecalltag", false);
                        } else {
                            btnSwitchCP.setChecked(true);
                            preference.putBoolean(MyApplication.houseProperty.houseId + "phonecalltag", true);
                        }
                    } else {
                        LogUtils.i("下载失败" + jo.getString("msg"));
                    }
                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                    LogUtils.i(getString(R.string.data_abnormal));
                }
            }

            public void onFailure(HttpException error, String msg) {
                LogUtils.i(getString(R.string.net_abnormal) + msg);
                ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.net_abnormal));
                finish();
            }
        });
        httpUtil.send();
    }

    private void sendToNET(final int tag) {
        try {
            RequestParams params = new RequestParams();
            params.addBodyParameter("tag", String.valueOf(tag));
            params.addBodyParameter("houseId", MyApplication.houseProperty.houseId);
            String url = Constants.CONTENT_PHONECALLTAG;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String mResult = responseInfo.result.toString();
                    try {
                        JSONObject jo = new JSONObject(mResult);
                        if (jo.getInt("returnCode") == 0) {
                            if (tag == 0) {
                                preference.putBoolean(MyApplication.houseProperty.houseId + "phonecalltag", false);
                                ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.usertool_CP_off));
                            } else {
                                preference.putBoolean(MyApplication.houseProperty.houseId + "phonecalltag", true);
                                ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.usertool_CP_on));
                            }
                            //给我有权限的 每台门禁发送消息
                            sendMessage();
                        } else {
                            LogUtils.i("操作失败" + jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                        LogUtils.i(getString(R.string.data_abnormal));
                        ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.data_abnormal));
                    }
                }

                public void onFailure(HttpException error, String msg) {
                    LogUtils.i(getString(R.string.net_abnormal) + msg);
                    ToastUtil.s(AdevancedSettingActivity.this, getString(R.string.net_abnormal));
                }
            });
            httpUtil.send();
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }

    private void sendMessage() {
        String s = MyApplication.getSharedPreferenceUtil().getString("access");
        for (int i = 0; i < VideoFragment.mlist.size(); i++) {
            String obj_sipnum = VideoFragment.mlist.get(i).get("obj_sipnum");
            ((squirrelCallImpl) getApplication()).squirrelSendMessage(obj_sipnum,
                    MyApplication.houseProperty.sipaddr,
                    squirrelCallImpl.serverport,
                    "SquirrelTelUpdate", 200);
        }
    }
}
