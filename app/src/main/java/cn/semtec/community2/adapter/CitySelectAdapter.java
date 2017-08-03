package cn.semtec.community2.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CitySelectAdapter extends BaseAdapter {

    private List<HashMap<String, String>> list;
    private LayoutInflater inflater;
    //需要居中显示
    private Boolean isGridView;


    public CitySelectAdapter(List<HashMap<String, String>> list, Context context, Boolean isGridView) {
        super();
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.isGridView = isGridView;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String city = list.get(position).get("city");
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
            holder = new ViewHolder();

            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            if (isGridView) {
                holder.text.setGravity(Gravity.CENTER);
            }

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.text.setText(city);
        return convertView;
    }
    public class ViewHolder {
        TextView text;
    }

}