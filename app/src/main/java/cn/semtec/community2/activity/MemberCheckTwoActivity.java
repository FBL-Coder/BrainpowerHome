package cn.semtec.community2.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class MemberCheckTwoActivity extends MyBaseActivity implements View.OnClickListener {
    private View btn_back, btn_commit;
    private TextView line1_1, line1_2;//第一排
    private View line2_2_1, ll2_1;
    private TextView line2_1_2; //第一排选择第一个时  第二排显示
    private String date_end;//到期时间
    private Bundle bundle; //包含id
    Calendar c;
    private int userState = 2; //住户类型 1:永久住户 2:临时住户
    private DatePickerDialog dialog_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_check_two);
        bundle = getIntent().getExtras();
        c = Calendar.getInstance();
        setView();
        setListener();
    }

    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        btn_commit = findViewById(R.id.btn_commit);
        line1_1 = (TextView) findViewById(R.id.line1_1);
        line1_2 = (TextView) findViewById(R.id.line1_2);

        ll2_1 = findViewById(R.id.ll2_1);
        line2_2_1 = findViewById(R.id.line2_2_1);

        line2_1_2 = (TextView) findViewById(R.id.line2_1_2);

        dialog_end = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String m;
                String d;
                monthOfYear++;
                if (monthOfYear >= 10) {
                    m = "" + monthOfYear;
                } else {
                    m = "0" + monthOfYear;
                }
                if (dayOfMonth >= 10) {
                    d = "" + dayOfMonth;
                } else {
                    d = "0" + dayOfMonth;
                }
                date_end = "" + year + "-" + m + "-" + d;
                line2_1_2.setText(date_end);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog_end.setCanceledOnTouchOutside(false);
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        line1_1.setOnClickListener(this);
        line1_2.setOnClickListener(this);
        line2_1_2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_commit:
                if (date_end == null) {
                    ToastUtil.s(this, "请选择居住时间");
                    return;
                }
                sendToNET();
                break;
            case R.id.line1_1:
                line1_1.setBackgroundResource(R.drawable.small_btn_checked);
                line1_1.setTextColor(getResources().getColor(R.color.white1));
                line1_2.setBackgroundResource(R.drawable.small_btn);
                line1_2.setTextColor(getResources().getColor(R.color.text999));
                line2_2_1.setVisibility(View.INVISIBLE);
                ll2_1.setVisibility(View.VISIBLE);
                date_end = null;
                line2_1_2.setText("结束时间");
                userState = 2;

                break;
            case R.id.line1_2:
                line1_1.setBackgroundResource(R.drawable.small_btn);
                line1_1.setTextColor(getResources().getColor(R.color.text999));
                line1_2.setBackgroundResource(R.drawable.small_btn_checked);
                line1_2.setTextColor(getResources().getColor(R.color.white1));
                line2_2_1.setVisibility(View.VISIBLE);
                ll2_1.setVisibility(View.INVISIBLE);
                date_end = "2999-12-31";
                userState = 1;
                break;
            case R.id.line2_1_2:
                dialog_end.show();
                break;
        }
    }

    private String parseDateNow() {
        String m = (c.get(Calendar.MONTH) + 1) >= 10 ? "" + (c.get(Calendar.MONTH) + 1) : "0" + (c.get(Calendar.MONTH) + 1);
        String d = c.get(Calendar.DAY_OF_MONTH) >= 10 ? "" + c.get(Calendar.DAY_OF_MONTH) : "0" + c.get(Calendar.DAY_OF_MONTH);
        return c.get(Calendar.YEAR) + "-" + m + "-" + d;
    }

    private void sendToNET() {
        try {
            JSONObject json = new JSONObject();
            json.put("id", bundle.getString("id"));
            json.put("userState", userState);
            json.put("expireTime", date_end);
            json.put("houseId", null);

            RequestParams params = new RequestParams();
            params.addHeader("Content-type", "application/json; charset=utf-8");
            params.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            params.setBodyEntity(entity);

            String url = Constants.CONTENT_REQUEST_APPROVED;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    cancelProgress();
                    String mResult = responseInfo.result.toString();
                    try {
                        JSONObject jo = new JSONObject(mResult);
                        if (jo.getInt("returnCode") == 0) {
                            ToastUtil.s(MemberCheckTwoActivity.this, "审核成功");
                            finish();
                        } else {
                            ToastUtil.s(MemberCheckTwoActivity.this, jo.getString("msg"));
                            LogUtils.i(jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                        ToastUtil.s(MemberCheckTwoActivity.this, getString(R.string.data_abnormal));
                        LogUtils.i(getString(R.string.data_abnormal));
                    }
                }

                @Override
                public void onFailure(HttpException error, String msg) {
                    cancelProgress();
                    LogUtils.i(getString(R.string.net_abnormal) + msg);
                    ToastUtil.s(MemberCheckTwoActivity.this, getString(R.string.net_abnormal));
                }
            });
            showProgress();
            httpUtil.send();
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }
}
