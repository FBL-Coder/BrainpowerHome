package cn.semtec.community2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.etsoft.smarthome.R;

/**
 * 动态密码开门适配器
 */
public class AuthorizationAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mlist;
    public AuthorizationAdapter(Context context,ArrayList<HashMap<String, String>> mlist){
        this.mContext = context;
        this.mlist = mlist;
    }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.authorization_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_pass = (TextView) convertView.findViewById(R.id.tv_pass);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = mlist.get(position);
//        String d = map.get("date").trim();
        holder.tv_name.setText(map.get("name"));
        holder.tv_date.setText(map.get("date"));
        holder.tv_pass.setText(map.get("password"));

        return convertView;
    }

    private class ViewHolder {
        private TextView tv_name;
        private TextView tv_date;
        private TextView tv_pass;
    }
}
