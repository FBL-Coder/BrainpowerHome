package cn.semtec.community2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CommunitySelectAdapter extends BaseAdapter {

    private List<HashMap<String, String>> list;
    private LayoutInflater inflater;


    public CommunitySelectAdapter(List<HashMap<String, String>> list, Context context) {
        super();
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String community = list.get(position).get("name");
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();

            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.text.setText(community);
        return convertView;
    }

    public class ViewHolder {
        TextView text;
    }

}
