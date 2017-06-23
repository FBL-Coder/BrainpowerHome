package cn.etsoft.smarthome.Adapter.ListView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class NewModuleFragment_Adapter extends BaseAdapter {
    private List<RcuInfo> list;
    private Context mContext;

    public NewModuleFragment_Adapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_single_select, null);
            viewHoler = new ViewHoler();
            viewHoler.view = (TextView) convertView.findViewById(R.id.list_item_title);
            viewHoler.imageView = (ImageView) convertView.findViewById(R.id.list_item_checked);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        viewHoler.view.setText(list.get(position).getCanCpuName());
        if (list.get(position).getDevUnitID().equals(AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, "")))
            viewHoler.imageView.setImageResource(R.drawable.ic_launcher_round);
        else viewHoler.imageView.setImageResource(R.drawable.ic_launcher);

        viewHoler.imageView.setOnClickListener(new View.OnClickListener() {
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
        return convertView;
    }
    class ViewHoler {
        TextView view;
        ImageView imageView;
    }
}
