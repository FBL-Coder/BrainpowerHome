package cn.semtec.community2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.etsoft.smarthome.R;

public class MemberManageAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, String>> mList;
    private LayoutInflater inflater;
    private Context context;

    /**
     * @param context
     * @param list
     */
    public MemberManageAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.context = context;
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

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.member_manage_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = (HashMap<String, String>) getItem(position);
        String name = map.get("name");
        String houseowner = map.get("houseowner");
        String userState = map.get("userState");
        String expireTime = map.get("expireTime");
        String cellphone = map.get("cellphone");
        StringBuffer sb = new StringBuffer();
        sb.append(houseowner + ": " + cellphone);
//        if (name == null || name.equals("") || name.equals("null")) {
//            sb.append("(未填写)");
//        } else {
//            sb.append(name);
//        }
        if (houseowner.equals("成员")) {
            sb.append("(" + userState + ")");
        }
        holder.tv_name.setText(sb.toString());
        if (expireTime.contains("2999")) {
            holder.tv_type.setText("居住时限: 永久");
        } else {
            holder.tv_type.setText("居住时限: " + expireTime);
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_name;
        public TextView tv_type;
    }

}
