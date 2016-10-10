package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.widget.SwipeItemLayout;

/**
 * Created by Say GoBay on 2016/8/30.
 */
public class SwipeAdapter extends BaseAdapter {
    private Context mContext = null;
    private IClick mListener;

    public SwipeAdapter(Context context,IClick listener) {
        this.mContext = context;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if (contentView == null) {
            View contentView1 = LayoutInflater.from(mContext).inflate(R.layout.equipmentdeploy_listview_item, null);
            View contentView2 = LayoutInflater.from(mContext).inflate(R.layout.equipmentdeploy_listview_item2, null);
            viewHolder = new ViewHolder();
            viewHolder.choose = (TextView) contentView1.findViewById(R.id.deploy_choose);
            viewHolder.delete = (TextView) contentView2.findViewById(R.id.deploy_delete);
            contentView = new SwipeItemLayout(contentView1, contentView2, null, null);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
            viewHolder.choose.setOnClickListener(mListener);
            viewHolder.choose.setTag(position);
            viewHolder.delete.setOnClickListener(mListener);
            viewHolder.delete.setTag(position);
        }
        return contentView;
    }

    class ViewHolder {
        TextView choose,delete;
    }
}

