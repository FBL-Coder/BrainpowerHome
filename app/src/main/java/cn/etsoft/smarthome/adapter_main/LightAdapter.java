package cn.etsoft.smarthome.adapter_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.entity.WareLight;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class LightAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareLight> light_room;
    private Context context;

    public LightAdapter(List<WareLight> light_room, Context context,LayoutInflater inflater) {
        this.light_room = light_room;
        this.context = context;
        mInflater = inflater;
    }

    public void notifyDataSetChanged(List<WareLight> light_room) {
        this.light_room = light_room;
        super.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (null != light_room) {
            return light_room.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
            return light_room.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_light, null);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.time = (ImageView) convertView.findViewById(R.id.time);
            viewHolder.on = (ImageView) convertView.findViewById(R.id.on);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.pattern = (TextView) convertView.findViewById(R.id.pattern);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(light_room.get(position).getDev().getDevName());

        if (light_room.get(position).getbOnOff() == 0){
            viewHolder.appliance.setImageResource(R.drawable.light);
        }else {
            viewHolder.appliance.setImageResource(R.drawable.lightk);
        }
        viewHolder.time.setImageResource(R.drawable.time);

        viewHolder.on.setImageResource(R.drawable.off);

        viewHolder.pattern.setText("定时模式");

        viewHolder.on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "暂不可用", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView appliance,time,on;
        TextView title,pattern;
    }
}
