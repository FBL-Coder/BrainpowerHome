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
import cn.etsoft.smarthome.domain.PrintCmd;
import cn.etsoft.smarthome.pullmi.entity.Iclick_Tag;
import cn.etsoft.smarthome.widget.SwipeItemLayout;

/**
 * Created by Say GoBay on 2016/8/30.
 */
public class Swipe_CpnAdapter extends BaseAdapter {
    private Context mContext = null;
    List<PrintCmd> listData;
    String[] cmd_name = null;
    String[] key_act = null;
    private int[] image = new int[]{R.drawable.kongtiao, R.drawable.tv, R.drawable.jidinghe, R.drawable.dengguang, R.drawable.chuanglian};

    public Swipe_CpnAdapter(Context context, List<PrintCmd> listData) {
        this.mContext = context;
        this.listData = listData;
//        System.out.println(lst.get(0).getDevId() +"---------"+lst.get(1).getDevId() +"---------"+lst.get(2).getDevId());
    }

    @Override
    public int getCount() {
        if (listData != null)
            return listData.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {

        if (listData != null)
            return listData.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if (contentView == null) {
            View contentView1 = LayoutInflater.from(mContext).inflate(R.layout.equipmentdeploy_listview_item, null);
            View contentView2 = LayoutInflater.from(mContext).inflate(R.layout.equipmentdeploy_listview_item2, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) contentView1.findViewById(R.id.deploy_tv);
            viewHolder.deploy_iv = (ImageView) contentView1.findViewById(R.id.deploy_iv);
            viewHolder.choose = (TextView) contentView1.findViewById(R.id.deploy_choose);
            viewHolder.choose1 = (TextView) contentView1.findViewById(R.id.deploy_choose1);
            viewHolder.delete = (TextView) contentView2.findViewById(R.id.deploy_delete);
            contentView = new SwipeItemLayout(contentView1, contentView2, null, null);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        key_act = new String[]{ "按下", "弹起", "未设置"};
        if (listData.get(position).getDevType() == 0) {
            cmd_name = new String[]{"未设置", "开关", "模式", "风速", "温度+", "温度-"};
        } else if (listData.get(position).getDevType() == 3) {
            cmd_name = new String[]{"未设置", "打开", "关闭", "开关", "变暗", "变亮"};
        } else if (listData.get(position).getDevType() == 4) {
            cmd_name = new String[]{"未设置", "打开", "关闭", "停止", "开关停"};
        } else {
            cmd_name = new String[]{"未设置"};
        }


        viewHolder.title.setText(listData.get(position).getKeyname());
        viewHolder.choose.setText(cmd_name[listData.get(position).getKey_cmd()]);
        viewHolder.choose1.setText(key_act[listData.get(position).getKeyAct_num()]);

        Iclick_Tag tag = new Iclick_Tag();
        tag.setPosition(position);
        tag.setType(listData.get(position).getDevType());
        tag.setText(cmd_name);

        viewHolder.choose.setOnClickListener(listData.get(position).getListener());
        viewHolder.choose.setTag(tag);
        viewHolder.choose1.setOnClickListener(listData.get(position).getListener());
        viewHolder.choose1.setTag(tag);
        viewHolder.delete.setOnClickListener(listData.get(position).getListener());
        viewHolder.delete.setTag(tag);

        return contentView;
    }

    class ViewHolder {
        TextView choose, delete, choose1;
        TextView title;
        ImageView deploy_iv;
    }
}

