package cn.etsoft.smarthome.adapter_main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class CurtainAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareCurtain> list_curtain;
    private Context context;

    public CurtainAdapter(List<WareCurtain> list_curtain, Context context, LayoutInflater mInflater) {
        this.list_curtain = list_curtain;
        this.context = context;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        if (null != list_curtain) {
            return list_curtain.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list_curtain.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private long TimeExit = 0;
    private long TimeExit1 = 0;
    private long TimeExit2 = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_curtain, null);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.time = (ImageView) convertView.findViewById(R.id.time);
            viewHolder.on = (ImageView) convertView.findViewById(R.id.on);
            viewHolder.left = (ImageView) convertView.findViewById(R.id.left);
            viewHolder.right = (ImageView) convertView.findViewById(R.id.right);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.pattern = (TextView) convertView.findViewById(R.id.pattern);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(list_curtain.get(position).getDev().getDevName());
        viewHolder.appliance.setImageResource(R.drawable.tcq);

        viewHolder.time.setImageResource(R.drawable.time);
        viewHolder.pattern.setText("定时");
        viewHolder.on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() - TimeExit > 1000) {
                    MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                    TimeExit = System.currentTimeMillis();
                    int Value = UdpProPkt.E_CURT_CMD.e_curt_stop.getValue();

                    String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                            ",\"datType\":4" +
                            ",\"subType1\":0" +
                            ",\"subType2\":0" +
                            ",\"canCpuID\":\"" + list_curtain.get(position).getDev().getCanCpuId() + "\"" +
                            ",\"devType\":" + list_curtain.get(position).getDev().getType() +
                            ",\"devID\":" + list_curtain.get(position).getDev().getDevId();
                    str_Fixed = str_Fixed +
                            ",\"cmd\":" + Value + "}";
                    MyApplication.sendMsg(str_Fixed);
                }
            }
        });
        viewHolder.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - TimeExit1 > 1000) {
                    MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                    TimeExit1 = System.currentTimeMillis();
                    int Value = UdpProPkt.E_CURT_CMD.e_curt_offOn.getValue();

                    String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                            ",\"datType\":4" +
                            ",\"subType1\":0" +
                            ",\"subType2\":0" +
                            ",\"canCpuID\":\"" + list_curtain.get(position).getDev().getCanCpuId() + "\"" +
                            ",\"devType\":" + list_curtain.get(position).getDev().getType() +
                            ",\"devID\":" + list_curtain.get(position).getDev().getDevId();
                    str_Fixed = str_Fixed +
                            ",\"cmd\":" + Value + "}";
                    MyApplication.sendMsg(str_Fixed);
                }
            }
        });
        viewHolder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - TimeExit2 > 1000) {
                    MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                    TimeExit2 = System.currentTimeMillis();
                    int Value = UdpProPkt.E_CURT_CMD.e_curt_offOff.getValue();

                    String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                            ",\"datType\":4" +
                            ",\"subType1\":0" +
                            ",\"subType2\":0" +
                            ",\"canCpuID\":\"" + list_curtain.get(position).getDev().getCanCpuId() + "\"" +
                            ",\"devType\":" + list_curtain.get(position).getDev().getType() +
                            ",\"devID\":" + list_curtain.get(position).getDev().getDevId();
                    str_Fixed = str_Fixed +
                            ",\"cmd\":" + Value + "}";
                    MyApplication.sendMsg(str_Fixed);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView appliance, time, on, left, right;
        TextView title, pattern;
    }
}
