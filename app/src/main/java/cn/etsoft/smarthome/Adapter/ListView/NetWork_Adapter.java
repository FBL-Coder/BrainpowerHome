package cn.etsoft.smarthome.Adapter.ListView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/22.
 * 联网模块设置 页面
 */

public class NetWork_Adapter extends BaseAdapter {
    private List<RcuInfo> list;
    private Context mContext;

    public NetWork_Adapter(Context context) {
        mContext = context;
        list = MyApplication.mApplication.getRcuInfoList();
    }

    @Override
    public void notifyDataSetChanged() {
        list = MyApplication.mApplication.getRcuInfoList();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHoler viewHoler = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_network, null);
            viewHoler = new ViewHoler();
            viewHoler.title = (TextView) convertView.findViewById(R.id.NetWork_title);
            viewHoler.title_ID = (TextView) convertView.findViewById(R.id.NetWork_title_id);
            viewHoler.Select = (ImageView) convertView.findViewById(R.id.NetWork_Checked);
            viewHoler.net_ID = (TextView) convertView.findViewById(R.id.NetWork_ID);
            viewHoler.net_Pass = (TextView) convertView.findViewById(R.id.NetWork_Pass);
            viewHoler.name = (EditText) convertView.findViewById(R.id.NetWork_Name);
            viewHoler.IP = (EditText) convertView.findViewById(R.id.NetWork_Ip);
            viewHoler.Ip_mask = (EditText) convertView.findViewById(R.id.NetWork_Ip_Mask);
            viewHoler.GetWay = (EditText) convertView.findViewById(R.id.NetWork_GetWay);
            viewHoler.Server = (EditText) convertView.findViewById(R.id.NetWork_Server);
            viewHoler.ShowInfo = (ImageView) convertView.findViewById(R.id.NetWork_ShowInfo);
            viewHoler.NetWork_Info = (LinearLayout) convertView.findViewById(R.id.NetWork_Info);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        viewHoler.title.setText(list.get(position).getCanCpuName());
        viewHoler.title_ID.setText(list.get(position).getDevUnitID());
        viewHoler.name.setHint(list.get(position).getCanCpuName());
        viewHoler.net_ID.setText(list.get(position).getDevUnitID());
        viewHoler.net_Pass.setText(list.get(position).getDevUnitPass());
        viewHoler.IP.setHint(list.get(position).getIpAddr());
        viewHoler.Ip_mask.setHint(list.get(position).getMacAddr());
        viewHoler.GetWay.setHint(list.get(position).getGateWay());
        viewHoler.Server.setHint(list.get(position).getCenterServ());

        if (list.get(position).getDevUnitID().equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "")))
            viewHoler.Select.setImageResource(R.drawable.selected);
        else viewHoler.Select.setImageResource(R.drawable.noselect);

        viewHoler.Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVars.getDevid().equals(list.get(position).getDevUnitID()))
                    ToastUtil.showText("联网模块正在使用中！");
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("提示 :");
                    dialog.setMessage("您是否要切换联网模块？");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppSharePreferenceMgr.put(GlobalVars.RCUINFOID_SHAREPREFERENCE, list.get(position).getDevUnitID());
                            MyApplication.setNewWareData();
                            SendDataUtil.getNetWorkInfo();
                            notifyDataSetChanged();
                            dialog.dismiss();
                            ToastUtil.showText("切换成功，请求数据已发送！");
                        }
                    });
                    dialog.create().show();
                }
            }
        });


        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.ShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHoler.NetWork_Info.getVisibility() == View.VISIBLE)
                    finalViewHoler.NetWork_Info.setVisibility(View.GONE);
                else finalViewHoler.NetWork_Info.setVisibility(View.VISIBLE);
            }
        });

        viewHoler.NetWork_Info.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHoler {
        TextView title, title_ID, net_ID, net_Pass;
        EditText name, IP, Ip_mask, GetWay, Server;
        ImageView Select, ShowInfo;
        LinearLayout NetWork_Info;
    }
}
