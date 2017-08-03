package cn.semtec.community2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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

import cn.etsoft.smarthome.R;
import cn.semtec.community2.adapter.CommunitySelectAdapter;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class CommunitySelectActivity extends MyBaseActivity {

    private ListView listview;
    private CommunitySelectAdapter listviewAdapter;
    private ArrayList<HashMap<String, String>> allList;
    private Intent intent;
    private SearchView searchView;
    protected JSONArray object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_select);
        intent = getIntent();
        //社区列表数据
        allList = new ArrayList<HashMap<String, String>>();
//		下载数据
        getData();
        listview = (ListView) findViewById(R.id.listview);
        listviewAdapter = new CommunitySelectAdapter(allList, this);
        listview.setAdapter(listviewAdapter);


        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//						点击item 得到Community对象并返回到注册界面
                        HashMap<String, String> select = (HashMap<String, String>) listviewAdapter.getItem(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("community", select);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        LogUtils.i("finish");
                        CommunitySelectActivity.this.finish();

                    }
                });


        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(
                                searchView.getWindowToken(), 0);
                    }
                    searchView.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    allList.clear();
                    for (int i = 0; i < object.length(); i++) {

                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject obj = (JSONObject) object.get(i);
                        String communityName = obj.getString("name");
                        if (!communityName.contains(newText)) {
                            continue;
                        }
                        map.put("id", obj.getString("id"));
                        map.put("num", obj.getString("num"));
                        map.put("name", communityName);
                        allList.add(map);
                    }
                    listviewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    CatchUtil.catchM(e);
                }
                return true;
            }
        });
    }

    private void getData() {
        String areacode = intent.getStringExtra("areacode");
        String url = Constants.CONTENT_CITYAREACODE + areacode + Constants.CONTENT_COMMUNITIES;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        cancelProgress();
                        if (responseInfo.statusCode == 200) {
                            String mResult = responseInfo.result.toString();
                            try {
                                //获得回传的 json字符串
                                JSONObject jo = new JSONObject(mResult);
                                //0为成功  <0为系统异常  其他待定
                                if (jo.getInt("returnCode") == 0) {

                                    object = jo.getJSONArray("object");

                                    //json数据 转到list集合中
                                    for (int i = 0; i < object.length(); i++) {
                                        HashMap<String, String> map = new HashMap<String, String>();
                                        JSONObject obj = (JSONObject) object.get(i);
                                        String communityName = obj.getString("name");
                                        map.put("id", obj.getString("id"));
                                        map.put("num", obj.getString("num"));
                                        map.put("name", communityName);
                                        allList.add(map);
                                    }
                                } else {
                                    ToastUtil.s(CommunitySelectActivity.this, jo.getString("msg"));
                                    LogUtils.i(jo.getString("msg"));
                                }
                            } catch (JSONException e) {
                                CatchUtil.catchM(e);
                            }
                        }
                        listviewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.i(getString(R.string.net_abnormal) + msg);
                        ToastUtil.s(CommunitySelectActivity.this, getString(R.string.net_abnormal));
                        cancelProgress();
                    }
                });
        http.send();
        showProgress();
    }
}
