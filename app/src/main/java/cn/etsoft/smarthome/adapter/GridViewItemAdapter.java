package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/11/25.
 */
public class GridViewItemAdapter extends BaseAdapter {
    private Context context;
    private int[] image = {R.drawable.dp5, R.drawable.qj, R.drawable.cr, R.drawable.jd, R.drawable.cz, R.drawable.ms, R.drawable.fh, R.drawable.jk};
    private String[] text = {"灯光", "情景", "窗帘", "家电", "插座", "门锁", "安防", "监控"};

    public GridViewItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public Object getItem(int position) {
        return image[position];
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
            convertView = LayoutInflater.from(this.context).inflate(R.layout.grid_view_item, null, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(image[position]);
        viewHolder.textView.setText(text[position]);

        return convertView;
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }

}
