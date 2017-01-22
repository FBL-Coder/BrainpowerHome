package cn.semtec.community2.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class HouseBindingTwoActivity extends MyBaseActivity implements View.OnClickListener {

    private View btn_back, btn_next;
    private EditText et_phone, et_name;
    private View iv_refresh, tips, check;
    private GridView gridView;
    private ArrayList<Map<String, String>> mlist;
    private GridViewAdapter adapter;
    private String houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_binding_two);
        setView();
        setListener();
    }


    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_name = (EditText) findViewById(R.id.et_name);

        iv_refresh = findViewById(R.id.iv_refresh);
        tips = findViewById(R.id.tips);
        check = findViewById(R.id.check);

        gridView = (GridView) findViewById(R.id.gridView);
        mlist = new ArrayList<>();
        adapter = new GridViewAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                houseId = mlist.get(position).get("houseId");
                for (int i = 0; i < parent.getCount(); i++) {
                    TextView v = (TextView) parent.getChildAt(i);
                    if (position == i) {//当前选中的Item改变背景颜色
                        v.setBackgroundResource(R.drawable.small_btn_checked);
                        v.setTextColor(getResources().getColor(R.color.white1));
                    } else {
                        v.setBackgroundResource(R.drawable.small_btn);
                        v.setTextColor(getResources().getColor(R.color.text999));
                    }
                }
            }
        });
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                String cellphone = et_phone.getText().toString();
                String name = et_name.getText().toString();

                Pattern p = Pattern.compile("^1\\d{10}$");
                Matcher m = p.matcher(cellphone);
                if (!m.matches()) {
                    ToastUtil.s(this, getString(R.string.regist_error));
                    return;
                }
                if (name.length() < 2) {
                    ToastUtil.s(this, "请输入姓名");
                    return;
                }
                if (houseId == null) {
                    ToastUtil.s(this, "请选择房产");
                    return;
                }
                sendToNET(cellphone, name);
                break;
            case R.id.iv_refresh:
                String phone = et_phone.getText().toString();
                Pattern p1 = Pattern.compile("^1\\d{10}$");
                Matcher m1 = p1.matcher(phone);
                if (!m1.matches()) {
                    ToastUtil.s(this, getString(R.string.regist_error));
                    return;
                }
                getData(phone);
                break;
        }
    }

    private void sendToNET(String cellphone, String name) {
        try {
            JSONObject json = new JSONObject();
            json.put("ownerCellPhone", cellphone);
            json.put("userName", name);
            json.put("houseId", houseId);

            RequestParams params = new RequestParams();
            params.addHeader("Content-type", "application/json; charset=utf-8");
            params.setHeader("Accept", "application/json");
            HttpEntity entity;
            try {
                entity = new StringEntity(json.toString(), "UTF-8");
                params.setBodyEntity(entity);
            } catch (UnsupportedEncodingException e1) {
                CatchUtil.catchM(e1);
            }
            String url = Constants.BINDHOUSEBYOWNER;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(
                                ResponseInfo<String> responseInfo) {
                            cancelProgress();
                            String mResult = responseInfo.result.toString();
                            try {
                                // 获得回传的 json字符串
                                JSONObject jo = new JSONObject(mResult);
                                // 0为成功 <0为系统异常 其他待定
                                if (jo.getInt("returnCode") == 0) {
                                    ToastUtil.s(HouseBindingTwoActivity.this, "绑定申请成功");
                                    LogUtils.i("绑定申请成功");
                                    finish();
                                } else {
                                    ToastUtil.s(HouseBindingTwoActivity.this, jo.getString("msg"));
                                    LogUtils.i(jo.getString("msg"));
                                }
                            } catch (Exception e) {
                                CatchUtil.catchM(e);
                            }
                        }

                        @Override
                        public void onFailure(HttpException error,
                                              String msg) {
                            cancelProgress();
                            ToastUtil.s(HouseBindingTwoActivity.this, getString(R.string.net_abnormal));
                            LogUtils.i(getString(R.string.net_abnormal) + msg);
                        }
                    });
            httpUtil.send();
            showProgress();
        } catch (JSONException e) {
            CatchUtil.catchM(e);
        }
    }

    private void getData(String cellphone) {
        String url = Constants.CONTENT_GETHOUSE + "?ownerCellPhone=" + cellphone;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String mResult = responseInfo.result.toString();
                try {
                    JSONObject obj = new JSONObject(mResult);
                    if (obj.getInt("returnCode") == 0) {
                        mlist.clear();
                        JSONArray ja = obj.getJSONArray("object");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject o = ja.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("houseName", o.getString("houseName"));
                            map.put("houseId", o.getString("houseId"));
                            mlist.add(map);
                        }
                        adapter.notifyDataSetChanged();
                        tips.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.VISIBLE);
                    } else {
                        ToastUtil.s(HouseBindingTwoActivity.this, obj.getString("msg"));
                        LogUtils.i(obj.getString("msg"));
                        tips.setVisibility(View.INVISIBLE);
                        check.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                    tips.setVisibility(View.INVISIBLE);
                    check.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                cancelProgress();
                ToastUtil.s(HouseBindingTwoActivity.this, getString(R.string.net_abnormal));
                LogUtils.i(getString(R.string.net_abnormal) + s);
                tips.setVisibility(View.INVISIBLE);
            }
        });
        httpUtil.send();
        tips.setVisibility(View.VISIBLE);
        check.setVisibility(View.INVISIBLE);
        houseId = null;
    }

    private class GridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, String> map = mlist.get(position);
            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.textview1, null);
            tv.setText(map.get("houseName"));
            return tv;
        }
    }
}
