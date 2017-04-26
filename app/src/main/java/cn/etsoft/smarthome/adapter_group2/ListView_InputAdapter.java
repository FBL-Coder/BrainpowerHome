package cn.etsoft.smarthome.adapter_group2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/11/25.
 */
public class ListView_InputAdapter extends BaseAdapter {
    private Context context;
    private List<String> room_list;
    private List<String> room_list_ok;
    int mSelect = 0;   //选中项

    public ListView_InputAdapter(Context context) {
        room_list_ok = new ArrayList<>();
        room_list = MyApplication.getRoom_list();
        for (int i = 0; i < room_list.size() + 1; i++) {
            if (i == 0) room_list_ok.add("全部");
            else room_list_ok.add(room_list.get(i - 1));
        }
        this.context = context;
    }
    public ListView_InputAdapter(Context context, List<String> room_list) {
        room_list_ok = new ArrayList<>();
        room_list_ok = room_list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return room_list_ok.size();
    }

    @Override
    public Object getItem(int position) {
        return room_list_ok.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.listview_main, null, false);
            viewHolder.image_main = (ImageView) convertView.findViewById(R.id.image_main);
            viewHolder.image_main.setVisibility(View.GONE);
            viewHolder.text_main = (TextView) convertView.findViewById(R.id.text_main);
            viewHolder.text_main.setTextSize(18);
            viewHolder.text_main.setPadding(5,5,5,5);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.text_main.setText(room_list_ok.get(position));
        if (mSelect == position) {
            convertView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
        } else {
            convertView.setBackgroundResource(R.color.color_60A7D5_null);  //其他项背景
        }
        return convertView;
    }

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        public ImageView image_main;
        public TextView text_main;
    }
}
