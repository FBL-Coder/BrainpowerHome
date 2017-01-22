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
import cn.etsoft.smarthome.domain.GridViewBean2;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class GridViewAdapter_parlour extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<GridViewBean2> listViewItems;

    public GridViewAdapter_parlour(int[] image, String[] title, Context context) {
        super();
        listViewItems = new ArrayList<GridViewBean2>();
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < image.length; i++) {
            GridViewBean2 item = new GridViewBean2( image[i],title[i]);
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
            convertView = mInflater.inflate(R.layout.parlour_gridview_item2, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.light_gv_image2);
            viewHolder.title = (TextView) convertView.findViewById(R.id.light_gv_title2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageResource(listViewItems.get(position).getImageId());
        viewHolder.title.setText(listViewItems.get(position).getTitleId());
        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
    }
}
