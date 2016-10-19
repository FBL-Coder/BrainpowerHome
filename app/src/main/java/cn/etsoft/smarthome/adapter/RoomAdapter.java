package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class RoomAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareDev> mWareDev;

    private int[] image = {R.drawable.ketingsijian, R.drawable.chufangsanjian,
            R.drawable.ketingchuanglian, R.drawable.ketingkongtiao,
            R.drawable.weishengjiansijan, R.drawable.erzinverfangzi,
            R.drawable.ketingchuanglian, R.drawable.xiuxianquyijan};

    public RoomAdapter(Context context,List<WareDev> mWareDev) {
        this.mWareDev = mWareDev;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null != mWareDev) {
            return mWareDev.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mWareDev.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sceneset_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.sceneset_iv);
            viewHolder.title = (TextView) convertView.findViewById(R.id.sceneset_tv);
            viewHolder.hui = (ImageView) convertView.findViewById(R.id.sceneset_hui);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageResource(image[position]);
        viewHolder.title.setText(mWareDev.get(position).getRoomName());
        viewHolder.hui.setImageResource(R.drawable.huijiantou);
        return convertView;
    }

    public class ViewHolder {
        private ImageView image, hui;
        public TextView title;
    }
}
