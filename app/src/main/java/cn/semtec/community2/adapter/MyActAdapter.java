package cn.semtec.community2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.BaseActivity;
import cn.semtec.community2.activity.CodeActivity;
import cn.semtec.community2.entity.HouseProperty;
import cn.semtec.community2.fragment.VideoFragment;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

/**
 * Created by Ladystyle005 on 2016/8/2.
 */
public class MyActAdapter extends BaseAdapter {
    private final AlertDialog checkDialog;
    private Context context;

    public MyActAdapter(Context context) {
        this.context = context;
        checkDialog = new AlertDialog.Builder(context).create();
    }

    @Override
    public int getCount() {
        return MyApplication.houseList.size();
    }

    @Override
    public Object getItem(int position) {
        return MyApplication.houseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        //获取楼栋数据时  储存houid记录
        MyApplication.getSharedPreferenceUtil().putString("houseId_last", MyApplication.houseProperty.houseId);
        View layout = LayoutInflater.from(context).inflate(R.layout.activity_my_item, null);
        try {
            final HouseProperty house = MyApplication.houseList.get(position);
            ((TextView) layout.findViewById(R.id.tv_community)).setText(house.communityName);
            if (position != 0) {
                layout.findViewById(R.id.tv_community).setBackgroundResource(R.drawable.my_item_top_corner);
                TextView isCheck = (TextView) layout.findViewById(R.id.isCheck);
                isCheck.setText("切换至该房产");
                isCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkDialog.setCanceledOnTouchOutside(false);
                        View dia = LayoutInflater.from(context).inflate(R.layout.tips_alert, null);
                        dia.findViewById(R.id.btn_agree).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Collections.swap(MyApplication.houseList, position, 0);
                                notifyDataSetChanged();
                                MyApplication.houseProperty = MyApplication.houseList.get(0);
                                getPublicClock();
                                checkDialog.dismiss();
                            }
                        });
                        dia.findViewById(R.id.btn_disagree).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkDialog.dismiss();
                            }
                        });
                        checkDialog.show();
                        checkDialog.getWindow().setContentView(dia);
                    }
                });
            }

            ((TextView) layout.findViewById(R.id.tv_block)).
                    setText(house.blockName);
            ((TextView) layout.findViewById(R.id.tv_init)).
                    setText(house.buildName);
            ((TextView) layout.findViewById(R.id.tv_house)).
                    setText(house.roomName);
            if (house.userType == 3) {
                layout.findViewById(R.id.layout_code).setVisibility(View.GONE);
            } else {
                layout.findViewById(R.id.layout_code).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CodeActivity.class);
                        intent.putExtra("houseId", house.houseId);
                        context.startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
        return layout;
    }

    public static void getPublicClock() {
        String url = Constants.CONTENT_GET_CLOCK + "?houseId=" + MyApplication.houseProperty.houseId;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String mResult = responseInfo.result;
                try {
                    // 获得回传的 json字符串
                    JSONObject jo = new JSONObject(mResult);
                    // 0为成功 <0为系统异常 其他待定
                    if (jo.getInt("returnCode") == 0) {
                        JSONArray access = jo.getJSONArray("object");
                        try {
                            VideoFragment.mlist.clear();
                            for (int i = 0; i < access.length(); i++) {
                                JSONObject obj = (JSONObject) access.get(i);
                                String obj_name = obj.getString("name");
                                String obj_sipnum = obj.getString("sipnum");
                                String obj_id = obj.getString("id");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("obj_name", obj_name);
                                map.put("obj_name", obj_name);
                                map.put("obj_sipnum", obj_sipnum);
                                map.put("obj_id", obj_id);
                                VideoFragment.mlist.add(map);
                            }
                        } catch (JSONException e) {
                            CatchUtil.catchM(e);
                        }
                    } else {
                        LogUtils.i(jo.getString("msg"));
                    }
                } catch (JSONException e) {
                    CatchUtil.catchM(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                LogUtils.i("网络异常" + msg);
                ToastUtil.s(BaseActivity.instance, "网络异常");
            }
        });
        http.send();
    }
}
