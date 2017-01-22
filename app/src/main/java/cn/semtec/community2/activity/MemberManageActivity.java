package cn.semtec.community2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.adapter.MemberManageAdapter;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class MemberManageActivity extends MyBaseActivity {

    private TextView tv_title;
    private ListView listView;
    private MemberManageAdapter adapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    cancelProgress();
                    break;
                case 1:
                    showProgress();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage);
        setView();
        adapter = new MemberManageAdapter(MemberManageActivity.this, mList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShowData();
    }

    private void setView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (MyApplication.houseProperty == null || MyApplication.houseProperty.userType == 3) {
            tv_title.setText("成员查看");
        } else {
            tv_title.setText("成员管理");
        }
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (MyApplication.houseProperty != null && MyApplication.houseProperty.userType != 3
                        && !mList.get(position).get("houseowner").equals("业主")) {
                    Intent i = new Intent(MemberManageActivity.this, MemberManageTwoActivity.class);
                    i.putExtra("id", mList.get(position).get("id"));
                    i.putExtra("houseowner", mList.get(position).get("houseowner"));
                    startActivity(i);
                }
            }
        });
    }

    private void getShowData() {
        String url = Constants.CONTENT_GOVERN_FAMILY + "?houseId=" + MyApplication.houseProperty.houseId;
        MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.GET, url,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        cancelProgress();
                        String mResult = responseInfo.result.toString();
                        try {
                            JSONObject jo = new JSONObject(mResult);

                            if (jo.getInt("returnCode") == 0) {
                                JSONArray ja = jo.getJSONArray("object");
                                mList.clear();
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject obj = ja.getJSONObject(i);
                                    if (obj.getString("cellphone").equals(MyApplication.cellphone)) {
                                        continue;
                                    }
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id", obj.getString("id"));
                                    map.put("cellphone", obj.getString("cellphone"));
                                    map.put("name", obj.getString("name"));
                                    map.put("houseowner", obj.getString("houseowner"));
                                    map.put("userState", obj.getString("userState"));
                                    map.put("expireTime", obj.getString("expireTime"));
                                    mList.add(map);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                ToastUtil.s(MemberManageActivity.this, jo.getString("msg"));
                                LogUtils.i(jo.getString("msg"));
                            }
                        } catch (JSONException e) {
                            CatchUtil.catchM(e);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                        ToastUtil.s(MemberManageActivity.this, getString(R.string.net_abnormal));
                        cancelProgress();
                    }
                });
        showProgress();
        httpUtil.send();
    }
}
