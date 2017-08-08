package cn.etsoft.smarthome.Adapter.ListView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.Domain.Safety_Data;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/8/7.
 */

public class SafetyRecordAdapter extends BaseAdapter {

    private List<Safety_Data.Safety_Time> safety_data;
    private Activity activity;

    public SafetyRecordAdapter(List<Safety_Data.Safety_Time> safety_data, Activity activity) {
        this.safety_data = safety_data;
        this.activity = activity;
    }

    public void notifyDataSetChanged(List<Safety_Data.Safety_Time> safety_data) {
        this.safety_data = safety_data;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return safety_data.size();
    }

    @Override
    public Object getItem(int position) {
        return safety_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_safety_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mItemName.setText("防区名称 : " + safety_data.get(position).getSafetyBean().getSecName());

        String time = "触发时间 : " + safety_data.get(position).getYear() + "年" + safety_data.get(position).getMonth() + "月" +
                safety_data.get(position).getDay() + "日" + safety_data.get(position).getH() + "时" +
                safety_data.get(position).getM() + "分" + safety_data.get(position).getS() + "秒";
        viewHolder.mItemTime.setText(time);

        if (safety_data.get(position).getSafetyBean().getSecType() == 0)
            viewHolder.mItemType.setText("安防类型 : 24小时布防");
        else if (safety_data.get(position).getSafetyBean().getSecType() == 1)
            viewHolder.mItemType.setText("安防类型 : 在家布防");
        else if (safety_data.get(position).getSafetyBean().getSecType() == 2)
            viewHolder.mItemType.setText("安防类型 : 外出布防");
        else if (safety_data.get(position).getSafetyBean().getSecType() == 255)
            viewHolder.mItemType.setText("安防类型 : 全部撤防");
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView mItemName;
        public TextView mItemTime;
        public TextView mItemType;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mItemName = (TextView) rootView.findViewById(R.id.item_name);
            this.mItemTime = (TextView) rootView.findViewById(R.id.item_time);
            this.mItemType = (TextView) rootView.findViewById(R.id.item_type);
        }

    }
}
