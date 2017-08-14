package cn.etsoft.smarthome.Adapter.ListView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.SearchNet;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/8/14.
 */

public class SeekListAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<SearchNet> list;

    public SeekListAdapter(Activity activity) {
        mActivity = activity;
        list = MyApplication.getWareData().getSeekNets();
    }

    public void notifyDataSetChanged() {
        list = MyApplication.getWareData().getSeekNets();
        super.notifyDataSetChanged();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_seeknet, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.mSeekNetTitle.setText(list.get(position).getRcu_rows().get(0).getName());
        viewHolder.mSeekNetTitleId.setText(list.get(position).getRcu_rows().get(0).getCanCpuID());
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mSeekNetTitle;
        public TextView mSeekNetTitleId;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mSeekNetTitle = (TextView) rootView.findViewById(R.id.SeekNet_title);
            this.mSeekNetTitleId = (TextView) rootView.findViewById(R.id.SeekNet_title_id);
        }

    }
}
