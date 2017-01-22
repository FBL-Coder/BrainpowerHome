package cn.semtec.community2.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class MemberManageTwoActivity extends MyBaseActivity implements View.OnClickListener {
    private View btn_back, btn_delete;
    private TextView btn_up;
    private AlertDialog dialog;
    private String id;    //被查看用户的id
    private String houseowner; //被查看用户的 类型 ， 用于判断是否是户主

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage_two);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        houseowner = bundle.getString("houseowner");
        setView();
        setListener();
    }


    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        btn_delete = findViewById(R.id.btn_delete);
        btn_up = (TextView) findViewById(R.id.btn_up);
        dialog = new AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);

        if (MyApplication.houseProperty.userType == 1) {
            btn_up.setVisibility(View.VISIBLE); //登陆用户是业主 并且 被查看用户不是户主的情况下 ，可设置为户主
        } else {
            btn_up.setVisibility(View.INVISIBLE);
        }
        if (houseowner.equals("户主")) {
            btn_up.setText("取消户主权限");
        }

    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_delete:
                View layout = getLayoutInflater().inflate(R.layout.tips_alert, null);
                ((TextView) layout.findViewById(R.id.textView)).setText("您确定删除该成员？删除后该成员将无法享受物业服务！");
                layout.findViewById(R.id.btn_agree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMember(id);
                        dialog.dismiss();
                    }
                });
                layout.findViewById(R.id.btn_disagree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.getWindow().setContentView(layout);
                break;
            case R.id.btn_up:
                View layout1 = getLayoutInflater().inflate(R.layout.tips_alert, null);
                if (houseowner.equals("户主")) {
                    ((TextView) layout1.findViewById(R.id.textView)).setText("确定取消户主?");
                } else {
                    ((TextView) layout1.findViewById(R.id.textView)).setText("设该用户设为户主，该用户将有权审批删除普通用户!");
                }
                layout1.findViewById(R.id.btn_agree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upMember(id);
                        dialog.dismiss();
                    }
                });
                layout1.findViewById(R.id.btn_disagree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.getWindow().setContentView(layout1);
                break;
        }
    }

    //删除用户
    private void deleteMember(String id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        String url = Constants.CONTENT_GOVERN_DELETE;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cancelProgress();
                String mResult = responseInfo.result.toString();
                try {
                    JSONObject jo = new JSONObject(mResult);
                    if (jo.getInt("returnCode") == 0) {
                        LogUtils.i("操作成功");
                        ToastUtil.s(MemberManageTwoActivity.this, "操作成功");
                        finish();
                    } else {
                        LogUtils.i("操作失败" + jo.getString("msg"));
                        ToastUtil.s(MemberManageTwoActivity.this, "操作失败" + jo.getString("msg"));
                    }
                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                    LogUtils.i("数据异常");
                    ToastUtil.s(MemberManageTwoActivity.this, "数据异常");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                cancelProgress();
                LogUtils.i("网络异常" + msg);
                ToastUtil.s(MemberManageTwoActivity.this, getString(R.string.net_abnormal));
            }
        });
        httpUtil.send();
        showProgress();
    }

    private void upMember(String id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        String url = Constants.CONTENT_GOVERN_SETHOLDER;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cancelProgress();
                String mResult = responseInfo.result.toString();
                try {
                    JSONObject jo = new JSONObject(mResult);
                    if (jo.getInt("returnCode") == 0) {
                        LogUtils.i("操作成功");
                        ToastUtil.s(MemberManageTwoActivity.this, "操作成功");
                    } else {
                        LogUtils.i("操作失败" + jo.getString("msg"));
                        ToastUtil.s(MemberManageTwoActivity.this, "操作失败" + jo.getString("msg"));
                    }
                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                    LogUtils.i("数据异常");
                    ToastUtil.s(MemberManageTwoActivity.this, "数据异常");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                cancelProgress();
                LogUtils.i("网络异常" + msg);
                ToastUtil.s(MemberManageTwoActivity.this, getString(R.string.net_abnormal));
            }
        });
        httpUtil.send();
        showProgress();
    }
}
