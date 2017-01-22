package cn.etsoft.smarthome.adapter;

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
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private int[] image = {R.drawable.all, R.drawable.none, R.drawable.parlour,
            R.drawable.book, R.drawable.car, R.drawable.garden, R.drawable.kitchen, R.drawable.passage, R.drawable.bedroom};
    private List<String> room_list;
    private List<String> room_list_ok;
    int mSelect = 0;   //选中项

    public ListViewAdapter(Context context) {
        room_list_ok = new ArrayList<>();
        room_list = MyApplication.getRoom_list();
        for (int i = 0; i < room_list.size() + 1; i++) {
            if (i == 0) room_list_ok.add("全部");
            else room_list_ok.add(room_list.get(i - 1));
        }
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
            viewHolder.text_main = (TextView) convertView.findViewById(R.id.text_main);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (room_list_ok.get(position).contains("客")) {
            viewHolder.image_main.setImageResource(image[2]);
        } else if (room_list_ok.get(position).contains("餐")) {
            viewHolder.image_main.setImageResource(image[5]);
        } else if (room_list_ok.get(position).contains("卧")) {
            viewHolder.image_main.setImageResource(image[8]);
        } else if (room_list_ok.get(position).contains("卫")) {
            viewHolder.image_main.setImageResource(image[1]);
        } else if (room_list_ok.get(position).contains("阳")) {
            viewHolder.image_main.setImageResource(image[4]);
        } else if (room_list_ok.get(position).contains("厨")) {
            viewHolder.image_main.setImageResource(image[6]);
        } else if (room_list_ok.get(position).contains("书")) {
            viewHolder.image_main.setImageResource(image[3]);
        } else if (room_list_ok.get(position).contains("全部")) {
            viewHolder.image_main.setImageResource(image[0]);
        } else {
            viewHolder.image_main.setImageResource(image[position % 9]);
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
