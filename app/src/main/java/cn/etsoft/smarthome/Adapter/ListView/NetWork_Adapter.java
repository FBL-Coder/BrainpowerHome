package cn.etsoft.smarthome.Adapter.ListView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;

import java.util.List;

import cn.etsoft.smarthome.Activity.Settings.NetInfoActivity;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Net_AddorDel_Helper;

/**
 * Author：FBL  Time： 2017/6/22.
 * 联网模块设置 页面
 */

public class NetWork_Adapter extends BaseAdapter {
    private List<RcuInfo> list;
    private Activity mContext;
    private NetWork_Adapter adapter;
    public static int SEEK = 1, LOGIN = 2;
    private int FLAG = 0;

    public NetWork_Adapter(Activity context, List<RcuInfo> list, int flag) {
        mContext = context;
        this.list = list;
        adapter = this;
        FLAG = flag;
    }

    public void notifyDataSetChanged(List<RcuInfo> list) {
        super.notifyDataSetChanged();
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHoler viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_network, null);
            viewHoler = new ViewHoler();
            viewHoler.title = (TextView) convertView.findViewById(R.id.NetWork_title);
            viewHoler.title_ID = (TextView) convertView.findViewById(R.id.NetWork_title_id);
            viewHoler.Select = (ImageView) convertView.findViewById(R.id.NetWork_Checked);
            viewHoler.ShowInfo = (ImageView) convertView.findViewById(R.id.NetWork_ShowInfo);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();


        if (FLAG == SEEK) {
            viewHoler.title.setText(list.get(position).getName());
            viewHoler.ShowInfo.setImageResource(R.drawable.noonline);
        } else {
            if ("".equals(list.get(position).getCanCpuName())) {
                viewHoler.title.setText(list.get(position).getName());
            } else viewHoler.title.setText(list.get(position).getCanCpuName());
            if (list.get(position).isOnLine())
                viewHoler.ShowInfo.setImageResource(R.drawable.online);
            else viewHoler.ShowInfo.setImageResource(R.drawable.noonline);
        }
        viewHoler.title_ID.setText(list.get(position).getDevUnitID());

        if (list.get(position).getDevUnitID().equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "")))
            viewHoler.Select.setImageResource(R.drawable.selected);
        else viewHoler.Select.setImageResource(R.drawable.noselect);

        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.ShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NetInfoActivity.class);
                Bundle bundle = new Bundle();
                if (FLAG == SEEK)
                    bundle.putInt("FLAG", SEEK);
                else
                    bundle.putInt("FLAG", LOGIN);
                bundle.putInt("POSITION", position);
                intent.putExtra("BUNDLE", bundle);
                mContext.startActivityForResult(intent,0);
            }
        });

        return convertView;
    }

    class ViewHoler {
        TextView title, title_ID;
        ImageView Select, ShowInfo;
    }
}
