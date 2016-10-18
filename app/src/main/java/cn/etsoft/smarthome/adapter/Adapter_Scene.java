package cn.etsoft.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;

/**
 * Created by Say GoBay on 2016/9/1.
 */
public class Adapter_Scene extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareSceneEvent> mWareSceneEvents;
    byte[] devBuff;
    private Context context;


    public Adapter_Scene(List<WareSceneEvent> mWareSceneEvents, Context context) {
        this.mWareSceneEvents = mWareSceneEvents;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != mWareSceneEvents) {
            return mWareSceneEvents.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mWareSceneEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.light_gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (RelativeLayout) convertView.findViewById(R.id.light_gv_image);
            viewHolder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mWareSceneEvents.get(position).getEventld() == 0) {
            viewHolder.image.setBackgroundResource(R.drawable.scene18);//情景全开
        } else {
            viewHolder.image.setBackgroundResource(R.drawable.scene19);//情景全关
        }
        viewHolder.item_tv.setText(mWareSceneEvents.get(position).getSceneName());

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSceneEvents(1, position);
            }
        });

        viewHolder.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createSceneEvents(0, position);
                return true;
            }
        });

        return convertView;

    }
    private void createSceneEvents(int flag, int postion) {
        int pos = -1;
        for (int i = 0; i < mWareSceneEvents.size(); i++) {
            if (mWareSceneEvents.get(i).getEventld() == postion) {
                pos = i;
            }
        }
        if (pos == -1) {
            return;
        }
        if (flag == 0 && pos == 0) {
            Toast.makeText(context, "该情景模式不能删除", Toast.LENGTH_SHORT).show();
            return;
        }
        if (flag == 0 && pos == 1) {
            Toast.makeText(context, "该情景模式不能删除", Toast.LENGTH_SHORT).show();
            return;
        }



        if (flag == 0) {
//            GlobalVars.setSenddata(CommonUtils.preSendUdpProPkt(
//                    GlobalVars.getDstip(), CommonUtils.getLocalIp(),
//                    UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delSceneEvents.getValue(),
//                    0, 0, devBuff, devBuff.length));
            final String del_str = "";
            CommonUtils.sendMsg(del_str);
        } else if (flag == 1) {
//            GlobalVars.setSenddata(CommonUtils.preSendUdpProPkt(
//                    GlobalVars.getDstip(), CommonUtils.getLocalIp(),
//                    UdpProPkt.E_UDP_RPO_DAT.e_udpPro_exeSceneEvents.getValue(),
//                    0, 0, devBuff, devBuff.length));
            final String exec_str = "";
            CommonUtils.sendMsg(exec_str);
        }
    }
    private class ViewHolder {
        RelativeLayout image;
        TextView item_tv;
    }
}
