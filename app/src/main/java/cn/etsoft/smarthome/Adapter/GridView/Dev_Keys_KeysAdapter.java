package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaygoo.widget.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.Out_List_printcmd;
import cn.etsoft.smarthome.Domain.PrintCmd;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Dev_KeysSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.CommonUtils;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/26.
 * 设备配按键 设置——按键适配器
 */

public class Dev_Keys_KeysAdapter extends BaseAdapter {

    private List<Out_List_printcmd> listData_all;
    private Context mContext;
    private List<PrintCmd> listData;
    private int position_keyinput;

    public Dev_Keys_KeysAdapter(List<Out_List_printcmd> listData_all, Context context, int position_keyinput, boolean isShowSelect) {
        this.listData_all = listData_all;
        this.position_keyinput = position_keyinput;
        mContext = context;
        IsShowSelect(isShowSelect);
    }


    public void notifyDataSetChanged(List<Out_List_printcmd> listData_all, int position_keyinput, boolean mIsShowSelect) {
        this.listData_all = listData_all;
        this.position_keyinput = position_keyinput;
        super.notifyDataSetChanged();
        IsShowSelect(mIsShowSelect);
    }

    public void IsShowSelect(boolean IsShowSelect) {
        if (IsShowSelect) {
            listData = new ArrayList<>();
            for (int i = 0; i < listData_all.size(); i++) {
                if (MyApplication.getWareData().getKeyInputs().get(position_keyinput).getDevUnitID().equals(listData_all.get(i).getUnitid())) {
                    for (int j = 0; j < listData_all.get(i).getPrintCmds().size(); j++) {
                        if (listData_all.get(i).getPrintCmds().get(j).isSelect())
                            listData.add(listData_all.get(i).getPrintCmds().get(j));
                    }
                }
            }
        } else {
            listData = new ArrayList<>();
            for (int i = 0; i < Dev_KeysSetHelper.getListData_all().size(); i++) {
                if (MyApplication.getWareData().getKeyInputs().get(position_keyinput).getDevUnitID().equals(Dev_KeysSetHelper.getListData_all().get(i).getUnitid())) {
                    listData = Dev_KeysSetHelper.getListData_all().get(i).getPrintCmds();
                }
            }
        }
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHoler viewHoler = null;
        if (convertView == null) {
            viewHoler = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.girdview_keys_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.text_list_item);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.img_list_item);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        if (listData.get(position).isSelect()) {
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
        } else {
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
        }
        viewHoler.mName.setText(listData.get(position).getKeyname());

        return convertView;
    }

    class ViewHoler {
        ImageView mIV;
        TextView mName;
    }
}
