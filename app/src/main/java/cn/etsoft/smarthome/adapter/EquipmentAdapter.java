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

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.EquipmentBean;

/**
 * Created by Say GoBay on 2016/8/30.
 */
public class EquipmentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<EquipmentBean> listViewItems;

    public EquipmentAdapter(String[] title, int[] image, Context context) {
        super();
        listViewItems = new ArrayList<EquipmentBean>();
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < title.length; i++) {
            EquipmentBean item = new EquipmentBean(title[i], image[i]);
            listViewItems.add(item);
        }
    }

    @Override
    public int getCount() {
        if (null != listViewItems) {
            return listViewItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.equipment_listview_item, null);
            viewHolder = new ViewHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.equipment_tv);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(listViewItems.get(position).getTitleId());
        viewHolder.image.setImageResource(listViewItems.get(position).getImageId());
        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public ImageView image;
    }
}
