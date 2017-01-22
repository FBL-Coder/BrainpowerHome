package cn.semtec.community2.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

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
import cn.semtec.community2.adapter.MemberCheckAdapter;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class MemberCheckActivity extends MyBaseActivity {

    private ListView listView;
    private ArrayList<HashMap<String, String>> mList;
    private MemberCheckAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
        setContentView(R.layout.activity_member_check);
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRequestData();
    }

    private void setView() {
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
        mList = new ArrayList<HashMap<String, String>>();
        adapter = new MemberCheckAdapter(MemberCheckActivity.this, mList, handler);
        listView.setAdapter(adapter);
    }

    private void getRequestData() {
        String url = Constants.CONTENT_REQUEST + "?houseId=" + MyApplication.houseProperty.houseId;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cancelProgress();
                String mResult = responseInfo.result.toString();
                try {
                    // 获得回传的 json字符串
                    JSONObject jo = new JSONObject(mResult);
                    // 0为成功 <0为系统异常 其他待定
                    if (jo.getInt("returnCode") == 0) {
                        JSONArray ja = jo.getJSONArray("object");
                        mList.clear();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject obj = ja.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", obj.getString("id"));
                            map.put("cellphone", obj.getString("cellphone"));
                            map.put("createtime", obj.getString("createtime"));
                            map.put("name", obj.getString("name"));
                            map.put("idnum", obj.getString("idnum"));
                            map.put("communityName", obj.getString("communityName"));
                            map.put("roomName", obj.getString("roomName"));
                            mList.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.s(MemberCheckActivity.this, jo.getString("msg"));
                        LogUtils.i(jo.getString("msg"));
                    }
                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LogUtils.i(getString(R.string.net_abnormal) + msg);
                ToastUtil.s(MemberCheckActivity.this, getString(R.string.net_abnormal));
                cancelProgress();
            }
        });
        http.send();
        showProgress();
    }
}
