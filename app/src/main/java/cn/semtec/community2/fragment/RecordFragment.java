package cn.semtec.community2.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

import cn.etsoft.smarthome.R;
import cn.semtec.community2.MyApplication;
import cn.semtec.community2.activity.BaseActivity;
import cn.semtec.community2.activity.RecordPicActivity;
import cn.semtec.community2.model.MyHttpUtil;
import cn.semtec.community2.tool.Constants;
import cn.semtec.community2.util.CatchUtil;
import cn.semtec.community2.util.ToastUtil;

public class RecordFragment extends Fragment {
    private View layout;
    private Dialog buffer;
    private ListView listView;
    private ArrayList<HashMap<String, String>> mlist;
    private View left, right;
    private TextView tv_num;
    private int num = 1;
    private MyAdapter adapter;
    private final int listSize = 12;
    private String url_path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_record, null);
        listView = (ListView) layout.findViewById(R.id.listView);
        left = layout.findViewById(R.id.left);
        right = layout.findViewById(R.id.right);
        tv_num = (TextView) layout.findViewById(R.id.tv_num);
        mlist = new ArrayList<>();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        getData();
        setListener();
        return layout;
    }

    private void setListener() {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num--;
                    tv_num.setText("" + num);
                    getData();
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlist.size() < listSize) {
                    return;
                }
                num++;
                tv_num.setText("" + num);
                getData();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RecordPicActivity.class);
                HashMap<String, String> map = mlist.get(position);
                intent.putExtra("date", map.get("time"));
                intent.putExtra("device", map.get("lockName"));
                intent.putExtra("name", map.get("userName"));
                intent.putExtra("photoUrl", url_path + map.get("photoUrl"));
                startActivity(intent);
            }
        });
    }

    public void getData() {
        if (MyApplication.houseProperty == null)
            return;
        String url = Constants.CONTENT_LOG + "?houseId=" + MyApplication.houseProperty.houseId + "&pageNum=" + listSize +
                "&userPage=" + num;
        MyHttpUtil http = new MyHttpUtil(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String mResult = responseInfo.result;
                try {
                    // 获得回传的 json字符串
                    JSONObject jo = new JSONObject(mResult);
                    // 0为成功 <0为系统异常 其他待定
                    if (jo.getInt("returnCode") == 0) {
                        JSONArray ja = jo.getJSONArray("object");
                        url_path = jo.getJSONObject("args").getString("photoSerAdr");
                        mlist.clear();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject o = ja.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("time", o.getString("time"));
                            map.put("userName", o.getString("userName"));
                            map.put("lockName", o.getString("lockName"));
                            map.put("photoUrl", o.getString("photoUrl"));
                            mlist.add(map);
                        }
                        cancelProgress();
                        adapter.notifyDataSetChanged();
                    } else {
                        cancelProgress();
                        LogUtils.i(jo.getString("msg"));
                    }
                } catch (JSONException e) {
                    cancelProgress();
                    CatchUtil.catchM(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                cancelProgress();
                LogUtils.i("网络异常" + msg);
                ToastUtil.s(BaseActivity.instance, "网络异常");
            }
        });
        http.send();
        showProgress();
    }

    private class MyAdapter extends BaseAdapter {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_record_item, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            HashMap<String, String> map = mlist.get(position);
            String d = map.get("time").trim();
            holder.tv_type.setText(map.get("userName"));
            holder.tv_date.setText(d.substring(0, 10));
            holder.tv_time.setText(d.substring(10, d.length()));

            return convertView;
        }

        private class ViewHolder {
            private ImageView image;
            private TextView tv_type;
            private TextView tv_date;
            private TextView tv_time;
        }
    }

    protected void showProgress() {
        if (buffer == null) {
            buffer = new AlertDialog.Builder(getActivity()).create();
            buffer.setCanceledOnTouchOutside(false);
        }
        buffer.show();
        buffer.getWindow().setContentView(R.layout.buffer_bar);
    }

    protected void cancelProgress() {
        if (buffer != null) {
            buffer.cancel();
        }
    }
}
