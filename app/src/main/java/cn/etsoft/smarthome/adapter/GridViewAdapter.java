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
import cn.etsoft.smarthome.domain.GridViewBean;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class GridViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<GridViewBean> listViewItems;

    public GridViewAdapter(int[] image, String[] title, Context context) {
        super();
        listViewItems = new ArrayList<GridViewBean>();
        mInflater = LayoutInflater.from(context);
        for (int i = 0; i < image.length; i++) {
            GridViewBean item = new GridViewBean( image[i] , title[i]);
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
            convertView = mInflater.inflate(R.layout.home_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.home_gv_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.home_gv_title);
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
