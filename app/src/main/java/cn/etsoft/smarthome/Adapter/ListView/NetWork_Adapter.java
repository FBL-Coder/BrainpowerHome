package cn.etsoft.smarthome.Adapter.ListView;

import android.app.Activity;
import android.app.Dialog;
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

import cn.etsoft.smarthome.Domain.GlobalVars;
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

    public NetWork_Adapter(Activity context) {
        mContext = context;
        list = MyApplication.mApplication.getRcuInfoList();
        adapter = this;
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
            viewHoler.name = (TextView) convertView.findViewById(R.id.NetWork_Name);
            viewHoler.IP = (TextView) convertView.findViewById(R.id.NetWork_Ip);
            viewHoler.Ip_mask = (TextView) convertView.findViewById(R.id.NetWork_Ip_Mask);
            viewHoler.GetWay = (TextView) convertView.findViewById(R.id.NetWork_GetWay);
            viewHoler.Server = (TextView) convertView.findViewById(R.id.NetWork_Server);
            viewHoler.ShowInfo = (ImageView) convertView.findViewById(R.id.NetWork_ShowInfo);
            viewHoler.NetWork_Info = (LinearLayout) convertView.findViewById(R.id.NetWork_Info);
            viewHoler.NetWork_EditName = (ImageView) convertView.findViewById(R.id.NetWork_EditName);
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

        final ViewHoler finalViewHoler = viewHoler;
        viewHoler.ShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHoler.NetWork_Info.getVisibility() == View.VISIBLE)
                    finalViewHoler.NetWork_Info.setVisibility(View.GONE);
                else finalViewHoler.NetWork_Info.setVisibility(View.VISIBLE);
            }
        });
        viewHoler.NetWork_EditName.setOnClickListener(new View.OnClickListener() {
            private TextView mDialogAddSceneOk;
            private TextView mDialogAddSceneCancle;
            private EditText mDialogAddSceneName;
            private TextView mTitleName;
            private TextView mTitle;

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                dialog.setContentView(R.layout.dialog_addscene);
                dialog.show();
                mDialogAddSceneName = (EditText) dialog.findViewById(R.id.dialog_addScene_name);
                mDialogAddSceneCancle = (TextView) dialog.findViewById(R.id.dialog_addScene_cancle);
                mDialogAddSceneOk = (TextView) dialog.findViewById(R.id.dialog_addScene_ok);
                mTitleName = (TextView) dialog.findViewById(R.id.title_name);
                mTitle = (TextView) dialog.findViewById(R.id.title);
                mTitle.setText("修改模块名称");
                mTitleName.setText("模块名称 :");
                mDialogAddSceneOk.setText("确定");
                mDialogAddSceneCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                mDialogAddSceneOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Net_AddorDel_Helper.editNew(adapter,list,position,mContext, mDialogAddSceneName,
                                list.get(position).getDevUnitID(), list.get(position).getDevUnitPass());
                    }
                });
            }
        });

        viewHoler.NetWork_Info.setVisibility(View.GONE);
        return convertView;
    }

    class ViewHoler {
        TextView title, title_ID, net_ID, net_Pass;
        TextView name, IP, Ip_mask, GetWay, Server;
        ImageView Select, ShowInfo, NetWork_EditName;
        LinearLayout NetWork_Info;
    }
}
