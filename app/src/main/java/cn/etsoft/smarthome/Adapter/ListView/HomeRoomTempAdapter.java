package cn.etsoft.smarthome.Adapter.ListView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.etsoft.smarthome.Domain.RoomTempBean;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/8/7.
 */

public class HomeRoomTempAdapter extends BaseAdapter {

    private Activity activity;
    private RoomTempBean mRoomTempBean;

    public HomeRoomTempAdapter(Activity activity) {
        mRoomTempBean = MyApplication.mApplication.getRoomTempBean();
        this.activity = activity;
    }

    public void notifyDataSetChanged() {
        mRoomTempBean = MyApplication.mApplication.getRoomTempBean();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mRoomTempBean.getRcu_rows().size();
    }

    @Override
    public Object getItem(int position) {
        return mRoomTempBean.getRcu_rows().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_home_room_temp_item, null,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
       viewHolder.mRoomName.setText(mRoomTempBean.getRcu_rows().get(position).getRoomName());
       viewHolder.mHomeTemp.setText(mRoomTempBean.getRcu_rows().get(position).getTempVal()+"");
       viewHolder.mHomeHum.setText(mRoomTempBean.getRcu_rows().get(position).getHumidity()+"%");
       viewHolder.mHomePm25.setText(mRoomTempBean.getRcu_rows().get(position).getPm25()+"");
       viewHolder.mHomePm10.setText(mRoomTempBean.getRcu_rows().get(position).getPm10()+"");

        return convertView;
    }

    static class ViewHolder {
        View view;
        TextView mRoomName;
        TextView mHomeTemp;
        TextView mHomeHum;
        TextView mHomePm25;
        TextView mHomePm10;

        ViewHolder(View view) {
            this.view = view;
            this.mRoomName = (TextView) view.findViewById(R.id.room_name);
            this.mHomeTemp = (TextView) view.findViewById(R.id.home_temp);
            this.mHomeHum = (TextView) view.findViewById(R.id.home_hum);
            this.mHomePm25 = (TextView) view.findViewById(R.id.home_pm25);
            this.mHomePm10 = (TextView) view.findViewById(R.id.home_pm10);
        }
    }
}
