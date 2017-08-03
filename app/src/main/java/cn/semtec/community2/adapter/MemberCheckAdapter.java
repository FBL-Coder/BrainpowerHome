package cn.semtec.community2.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.MemberCheckTwoActivity;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class MemberCheckAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> mList;
    private LayoutInflater inflater;
    private Context context;
    private Handler handler;


    public MemberCheckAdapter(Context context, ArrayList<HashMap<String, String>> list, Handler handler) {
        super();
        inflater = LayoutInflater.from(context);
        mList = list;
        this.context = context;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View btn_agree, btn_disagree;
        //两种类型 1是选择地址申请的 ，2是业主手机号申请的
        HashMap<String, String> map = (HashMap<String, String>) getItem(position);
        View v;
        v = inflater.inflate(R.layout.member_request_item1, null);
        ((TextView) v.findViewById(R.id.tv_community)).setText(map.get("communityName"));
        ((TextView) v.findViewById(R.id.tv_house)).setText(map.get("roomName"));
        ((TextView) v.findViewById(R.id.tv_phone)).setText(map.get("cellphone"));
        String time = map.get("createtime");
        ((TextView) v.findViewById(R.id.tv_date)).setText(time.substring(0, time.indexOf(" ")));
        btn_agree = v.findViewById(R.id.btn_agree);
        btn_disagree = v.findViewById(R.id.btn_disagree);
        String name = map.get("name");
        if (name == null || name.equals("null")) {
            v.findViewById(R.id.tv_name).setVisibility(View.GONE);
        } else {
            ((TextView) v.findViewById(R.id.tv_name)).setText(map.get("name"));
        }
        final String id = map.get("id");
        btn_agree.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MemberCheckTwoActivity.class);
                        intent.putExtra("id", id);
                        context.startActivity(intent);
                    }
                });
        btn_disagree.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendToNET(id, position);
                    }
                });
        return v;
    }

    //拒绝申请
    private void sendToNET(String id, final int position) {
        try {
            RequestParams params = new RequestParams();
            params.addBodyParameter("reject_id", id);

            String url = Constants.CONTENT_REQUEST_REJECTED;
            MyHttpUtil httpUtil = new MyHttpUtil(HttpMethod.POST, url, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    handler.sendEmptyMessage(0);
                    String mResult = responseInfo.result.toString();
                    try {
                        JSONObject jo = new JSONObject(mResult);
                        if (jo.getInt("returnCode") == 0) {
                            LogUtils.i("操作成功");
                            mList.remove(position);
                            notifyDataSetChanged();
                        } else {
                            ToastUtil.s(context, jo.getString("msg"));
                            LogUtils.i(jo.getString("msg"));
                        }
                    } catch (JSONException e) {
                        CatchUtil.catchM(e);
                        ToastUtil.s(context, context.getString(R.string.data_abnormal));
                    }
                }
                @Override
                public void onFailure(HttpException error, String msg) {
                    handler.sendEmptyMessage(0);
                    ToastUtil.s(context, context.getString(R.string.net_abnormal));
                    LogUtils.i("网络异常");
                }
            });
            httpUtil.send();
            handler.sendEmptyMessage(1);
        } catch (Exception e) {
            CatchUtil.catchM(e);
        }
    }
}
