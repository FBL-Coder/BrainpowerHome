package cn.semtec.community2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import cn.etsoft.smarthome.R;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;
import cn.semtec.community2.view.WheelView;

public class HouseBindingOneActivity extends MyBaseActivity implements View.OnClickListener {
    private View btn_back;
    private Button btn_next;
    private TextView tv_area;
    private TextView tv_community;
    private TextView tv_block;
    private TextView tv_build;

    private TextView tv_house;
    // 对应textview 的 扩大范围版，用于点击监听
    private View tab_1;
    private View tab_2;
    private View tab_3;
    private View tab_4;
    private View tab_5;
    //
    private String areacode;
    private String communitynum;
    private String blocknum;
    private String buildnum;
    private String house_id;
    private String communityid;
    // dialog 数据
    // 单元内所有 楼栋的数据集合
    private ArrayList<HashMap<String, Object>> blocks_obj = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> build_obj = new ArrayList<HashMap<String, Object>>();
    private ArrayList<HashMap<String, Object>> house_obj = new ArrayList<HashMap<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_binding_one);
        setView();
        setListener();
    }

    private void setView() {
        btn_back = findViewById(R.id.btn_back);
        btn_next = (Button) findViewById(R.id.btn_next);
        tv_area = (TextView) findViewById(R.id.textView1);
        tv_community = (TextView) findViewById(R.id.textView2);
        tv_block = (TextView) findViewById(R.id.textView3);
        tv_build = (TextView) findViewById(R.id.textView4);
        tv_house = (TextView) findViewById(R.id.textView5);

        tab_1 = findViewById(R.id.tab_1);
        tab_2 = findViewById(R.id.tab_2);
        tab_3 = findViewById(R.id.tab_3);
        tab_4 = findViewById(R.id.tab_4);
        tab_5 = findViewById(R.id.tab_5);
    }

    private void setListener() {
        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
        tab_4.setOnClickListener(this);
        tab_5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_next:
                if (areacode != null && communitynum != null && blocknum != null && buildnum != null && house_id != null) {
                    // 电话区号（4位）＋小区号（4位）+设备类型 (2位)+
                    // 楼栋号（4位）＋单元号（2位）＋层号（2位）＋房号（2位）＋设备号（1位） 共21位
                    /**
                     * String sipnum=areacode + communitynum + "03" + blocknum +
                     * buildnum + floor + room+"1"; 05990006
                     */
//                    if (areacode.length() < 4) {
//                        areacode = "0" + areacode;
//                    }
//                    sipnum = areacode + communitynum + "03" + blocknum + buildnum + housenum + "1";
                    sendToNET();
                } else {
                    ToastUtil.s(this, getString(R.string.regist_tip1));
                }
                break;

            case R.id.tab_1:
                // 启动cityselect 获取 area 数据集合 并返回选择的area
                Intent i1 = new Intent(this, CitySelectActivity.class);
                startActivityForResult(i1, 1);
                break;
            case R.id.tab_2:
                // 启动communityselect 获取 community 数据集合 并返回选择的community
                if (areacode != null && areacode != "") {
                    Intent i2 = new Intent(this, CommunitySelectActivity.class);
                    i2.putExtra("areacode", areacode);
                    startActivityForResult(i2, 2);
                } else {
                    ToastUtil.s(this, getString(R.string.regist_tip2));
                }
                break;
            case R.id.tab_3:
                if (blocks_obj.size() > 0 && communitynum != null && communitynum.length() >= 2) {
                    new WheelViewHelper(blocks_obj, tv_block);
                } else {
                    ToastUtil.s(this, getString(R.string.regist_tip3));
                }
                break;
            case R.id.tab_4:
                if (build_obj.size() > 0 && blocknum != null && blocknum.length() >= 2) {
                    new WheelViewHelper(build_obj, tv_build);
                } else {
                    ToastUtil.s(this, getString(R.string.regist_tip4));
                }
                break;
            case R.id.tab_5:
                if (buildnum != null && buildnum.length() >= 2) {
                    new WheelViewHelper(house_obj, tv_house);
                } else {
                    ToastUtil.s(this, getString(R.string.regist_tip5));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 1为启动CitySelectActivity 的返回
                case 1:
                    // 返回成功 获取返回city对象的String 并设置文本 记录区号
                    Bundle b = data.getExtras();
                    HashMap<String, String> c = (HashMap<String, String>) b.getSerializable("city");
                    tv_area.setText(c.get("city"));
                    areacode = c.get("areacode");
                    // 考虑到都选完后 再次选择高等级的选项 要清空数据
                    tv_community.setText("");
                    tv_block.setText("");
                    tv_build.setText("");
                    tv_house.setText("");

                    communitynum = null;
                    blocknum = null;
                    buildnum = null;
                    break;
                case 2:
                    // 返回成功 获取返回city对象的String 并设置文本 记录区号
                    Bundle b1 = data.getExtras();
                    HashMap<String, String> c1 = (HashMap<String, String>) b1.getSerializable("community");
                    tv_community.setText(c1.get("name"));
                    communityid = c1.get("id");
                    communitynum = c1.get("num");

                    tv_block.setText("");
                    tv_build.setText("");
                    tv_house.setText("");

                    blocknum = null;
                    buildnum = null;
                    // 用communityId获取楼栋的信息
                    getBlocks();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getBlocks() {
        String url = Constants.CONTENT_COMMUNITYID + communityid + Constants.CONTENT_BLOCKS;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                cancelProgress();
                if (responseInfo.statusCode == 200) {
                    String mResult = responseInfo.result.toString();
                    try {
                        // 获得回传的 json字符串
                        JSONObject jo = new JSONObject(mResult);
                        // 0为成功 <0为系统异常 其他待定
                        if (jo.getInt("returnCode") == 0) {
                            JSONArray object = jo.getJSONArray("object");
                            blocks_obj.clear();
                            // json数据 转到list集合中
                            for (int i = 0; i < object.length(); i++) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                JSONObject obj = (JSONObject) object.get(i);
                                LogUtils.i(obj.toString());
                                map.put("id", obj.getString("id"));
                                map.put("num", obj.getString("num"));
                                map.put("name", obj.getString("name"));
                                blocks_obj.add(map);
                            }

                        } else {
                            ToastUtil.s(HouseBindingOneActivity.this, jo.getString("msg"));
                            LogUtils.i(jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                cancelProgress();
                LogUtils.i(getString(R.string.net_abnormal) + msg);
                ToastUtil.s(HouseBindingOneActivity.this, getString(R.string.net_abnormal));
            }
        });
        http.send();
        showProgress();
    }

    private void getBuild(String blockid) {
        String url = Constants.CONTENT_BLOCKID + blockid + Constants.CONTENT_BUILDINGS;
        LogUtils.i("getBuild");
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
                                    JSONArray object = jo.getJSONArray("object");

                                    build_obj.clear();
                                    //json数据 转到list集合中
                                    for (int i = 0; i < object.length(); i++) {

                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        JSONObject obj = (JSONObject) object.get(i);
                                        LogUtils.i(obj.toString());
                                        map.put("id", obj.getString("id"));
                                        map.put("num", obj.getString("num"));
                                        map.put("name", obj.getString("name"));
                                        LogUtils.i((String) map.get("name"));
                                        build_obj.add(map);
                                    }

                                } else {
                                    LogUtils.i(jo.getString("msg"));
                                    ToastUtil.s(HouseBindingOneActivity.this, jo.getString("msg"));
                                }
                            } catch (JSONException e) {
                                CatchUtil.catchM(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        cancelProgress();
                        LogUtils.i("网络异常" + msg);
                        ToastUtil.s(HouseBindingOneActivity.this, getString(R.string.net_abnormal));
                    }
                });
        http.send();
        showProgress();
    }

    protected void getHouse(String buildid) {
        String url = Constants.CONTENT_BUILDID + buildid + Constants.CONTENT_HOUSES;
        LogUtils.i("getHouse");
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
                                    JSONArray object = jo.getJSONArray("object");
                                    house_obj.clear();
                                    //json数据 转到list集合中
                                    for (int i = 0; i < object.length(); i++) {
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        JSONObject obj = (JSONObject) object.get(i);
                                        LogUtils.i(obj.toString());
                                        map.put("id", obj.getString("id"));
                                        map.put("floor", obj.getString("floor"));
                                        map.put("room", obj.getString("room"));
                                        map.put("name", obj.getString("name"));
                                        LogUtils.i((String) map.get("name"));
                                        house_obj.add(map);
                                    }
                                } else {
                                    LogUtils.i(jo.getString("msg"));
                                    ToastUtil.s(HouseBindingOneActivity.this, jo.getString("msg"));
                                }
                            } catch (JSONException e) {
                                CatchUtil.catchM(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        cancelProgress();
                        LogUtils.i("网络异常" + msg);
                        ToastUtil.s(HouseBindingOneActivity.this, getString(R.string.net_abnormal));
                    }
                });
        http.send();
        showProgress();
    }

    private void sendToNET() {
        try {
            JSONObject json = new JSONObject();
            json.put("house_id", house_id);

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

            String url = Constants.BINDHOUSEBYHOUSE;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpRequest.HttpMethod.POST, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(
                                ResponseInfo<String> responseInfo) {
                            cancelProgress();
                            String mResult = responseInfo.result.toString();
                            try {
                                JSONObject jo = new JSONObject(mResult);
                                if (jo.getInt("returnCode") == 0) {
                                    ToastUtil.s(HouseBindingOneActivity.this, "绑定申请成功");
                                    LogUtils.i("绑定申请成功");
                                    finish();
                                } else {
                                    ToastUtil.s(HouseBindingOneActivity.this, jo.getString("msg"));
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
                            ToastUtil.s(HouseBindingOneActivity.this, getString(R.string.net_abnormal));
                            LogUtils.i(getString(R.string.net_abnormal) + msg);
                        }
                    });
            httpUtil.send();
            showProgress();
        } catch (JSONException e) {
            CatchUtil.catchM(e);
        }
    }

    private class WheelViewHelper {
        private WheelView wv;

        public WheelViewHelper(final ArrayList<HashMap<String, Object>> data, final TextView view) {
            View outerView = getLayoutInflater().inflate(
                    R.layout.wheelview, null);
            wv = (WheelView) outerView.findViewById(R.id.wheelView1);
            // 从对象集合 取出name 建立集合  用于wheelview 的参数
            ArrayList<String> name = new ArrayList<String>();
            for (int i = 0; i < data.size(); i++) {
                name.add((String) data.get(i).get("name"));
            }
            //设置 数据   建立dialog
            if (name.size() > 0) {
                wv.setData(name);
                wv.setDefault(0);

                new AlertDialog.Builder(HouseBindingOneActivity.this).setView(outerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int i = getSelected();
                                if (i == -1)
                                    return;
                                HashMap<String, Object> select = data.get(i);
                                view.setText((String) select.get("name"));
                                //判断是哪个控件开启的Dialog  下载 赋值不同的数据
                                if (view == tv_block) {
                                    blocknum = (String) select.get("num");
                                    tv_build.setText("");
                                    tv_house.setText("");

                                    buildnum = null;
                                    house_id = null;
                                    //网络取单元的数据
                                    getBuild((String) select.get("id"));
                                } else if (view == tv_build) {
                                    buildnum = (String) select.get("num");
                                    tv_house.setText("");
                                    house_id = null;
                                    //网络取房号的数据
                                    getHouse((String) select.get("id"));
                                } else if (view == tv_house) {
                                    house_id = "" + select.get("id");
                                }
                            }

                        }).show();
            } else {
                ToastUtil.s(HouseBindingOneActivity.this, "数据错误");
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
}
