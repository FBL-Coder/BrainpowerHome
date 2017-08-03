package cn.semtec.community2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.MessageActivity;
import cn.semtec.community2.activity.MessageUrlActivity;

public class MessageActivityAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> mList;
    public ArrayList<String> selectList;
    //	判断是不是第一个数据  ，另注意下拉刷新需要 重新设空
    public Date pubTime = null;
    @SuppressLint("SimpleDateFormat")
    DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
    @SuppressLint("SimpleDateFormat")
    DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    DateFormat df3 = new SimpleDateFormat("EEEE");
    private SpannableString span;

    public MessageActivityAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mList = list;
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_body = (TextView) convertView.findViewById(R.id.tv_body);
            holder.btn_dot = (ImageView) convertView.findViewById(R.id.btn_dot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> map = mList.get(position);
        final long l = (Long) map.get("date");
        Date d = new Date(l);
        //该item不是第一个  且 与上一个时间相差小于5min
        if (pubTime != null && d.getTime() - pubTime.getTime() <= 300000) {
            holder.tv_time.setText("");
        } else {
            pubTime = d;
            Calendar nowTime = Calendar.getInstance();
            nowTime.setTime(new Date());
            Calendar dTime = Calendar.getInstance();
            dTime.setTime(d);
            String date = "";
            //设置时间
            if (dTime.get(Calendar.DAY_OF_YEAR) == nowTime.get(Calendar.DAY_OF_YEAR)) {
                date = df2.format(d);
            } else if (dTime.get(Calendar.WEEK_OF_YEAR) == nowTime.get(Calendar.WEEK_OF_YEAR)) {
                date = df3.format(d) + " " + df2.format(d);
            } else {
                date = df1.format(d) + "  " + df2.format(d);
            }

            holder.tv_time.setText(date);
        }
        // 设置图标
        if (MessageActivity.isManage) {
            holder.btn_dot.setImageResource(R.drawable.message_select_false);
            holder.btn_dot.setVisibility(View.VISIBLE);
            holder.tv_body.setEnabled(false);
            if (selectList == null) {
                selectList = new ArrayList<String>();
            } else {
                selectList.clear();
            }
            holder.btn_dot.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (selectList.contains(String.valueOf(l))) {
                                selectList.remove(String.valueOf(l));
                                holder.btn_dot.setImageResource(R.drawable.message_select_false);
                            } else {
                                selectList.add(String.valueOf(l));
                                holder.btn_dot.setImageResource(R.drawable.message_select_true);
                            }
                        }
                    });
        } else {
            holder.btn_dot.setVisibility(View.GONE);
            holder.tv_body.setEnabled(true);
        }

        //设置文本
        final String url = (String) map.get("url");
        if (url.equals("") || url == null) {
            holder.tv_body.setText((String) map.get("content"));
        } else {
            String b = (String) map.get("content") + "\n" + "点击查看详情";
            holder.tv_body.setText(b);
            span = new SpannableString(holder.tv_body.getText().toString());
            span.setSpan(new ForegroundColorSpan(Color.BLUE), b.length() - 6, b.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tv_body.setText(span);
            holder.tv_body.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, MessageUrlActivity.class);
                            intent.putExtra("url", url);
                            context.startActivity(intent);
                        }
                    });
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_time;
        public TextView tv_body;
        public ImageView btn_dot;
    }

}
