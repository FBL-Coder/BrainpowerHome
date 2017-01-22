package cn.semtec.community2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.semtec.community2.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.database.DBhelper;
import cn.semtec.community2.view.SliderView;

public class MessageListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<HashMap<String, Object>> mList;
    @SuppressLint("SimpleDateFormat")
    private DateFormat df = new SimpleDateFormat("HH:mm");

    public MessageListAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = data;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = mList.get(position);
        final String comefrom = (String) map.get("comefrom");
        String body = (String) map.get("body");
        String time = df.format(new Date((Long) map.get("time")));
        final int newsCount = (Integer) map.get("newsCount");
        ViewHolder holder;
        SliderView slideView = (SliderView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.activity_message_list_item, null);

            slideView = new SliderView(mContext);
            slideView.setContentView(itemView);

            holder = new ViewHolder();
            holder.tv_dot = (TextView) slideView.findViewById(R.id.tv_dot);
            holder.tv_head = (TextView) slideView.findViewById(R.id.tv_head);
            holder.tv_body = (TextView) slideView.findViewById(R.id.tv_body);
            holder.tv_time = (TextView) slideView.findViewById(R.id.tv_time);
            holder.deleteHolder = (ViewGroup) slideView.findViewById(R.id.holder);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        slideView.shrink();
        if (newsCount > 0) {
//            holder.tv_dot.setText(String.valueOf(newsCount));
            holder.tv_dot.setVisibility(View.VISIBLE);
        } else {
            holder.tv_dot.setVisibility(View.INVISIBLE);
        }
        holder.tv_time.setText(time);
        holder.tv_head.setText(comefrom);
        holder.tv_body.setText(body);
        holder.deleteHolder.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mList.remove(position);
                        notifyDataSetChanged();

                        SQLiteDatabase db = MyApplication.getDB();
                        db.delete(DBhelper.MESSAGE, DBhelper.MESSAGE_FROM + "=? and " + DBhelper.MESSAGE_ACCOUNT + "=?",
                                new String[]{comefrom, MyApplication.cellphone});
                        db.close();
                    }
                }
        );
        return slideView;
    }

    private class ViewHolder {
        public TextView tv_dot;
        public TextView tv_head;
        public TextView tv_body;
        public TextView tv_time;
        public ViewGroup deleteHolder;

    }


}
