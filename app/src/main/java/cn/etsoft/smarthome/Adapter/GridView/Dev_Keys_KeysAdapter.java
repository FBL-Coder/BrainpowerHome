package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
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
import cn.etsoft.smarthome.View.RotateBtn.RotateControButton;

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
    private List<String> texts;
    private int DevType = 0;
    private boolean isShowSelect;

    public Dev_Keys_KeysAdapter(int devType, List<Out_List_printcmd> listData_all, Context context, int position_keyinput, boolean isShowSelect) {
        this.listData_all = listData_all;
        this.position_keyinput = position_keyinput;
        this.isShowSelect = isShowSelect;
        DevType = devType;
        mContext = context;
        IsShowSelect(isShowSelect);
    }

    public void notifyDataSetChanged(int devType, List<Out_List_printcmd> listData_all, int position_keyinput, boolean mIsShowSelect) {
        this.listData_all = listData_all;
        DevType = devType;
        this.position_keyinput = position_keyinput;
        this.isShowSelect = mIsShowSelect;
        super.notifyDataSetChanged();
        IsShowSelect(mIsShowSelect);
    }

    @Override
    public void notifyDataSetChanged() {
        IsShowSelect(isShowSelect);
        super.notifyDataSetChanged();
    }

    public void IsShowSelect(boolean IsShowSelect) {

        if (DevType == 0) {
            texts = new ArrayList<>();
            texts.add("未设置");
            texts.add("开关");
            texts.add("模式");
            texts.add("风速");
            texts.add("温度+");
            texts.add("温度-");
        } else if (DevType == 3) {
            texts = new ArrayList<>();
            texts.add("未设置");
            texts.add("打开");
            texts.add("关闭");
            texts.add("开关");
            texts.add("变暗");
            texts.add("变亮");
        } else if (DevType == 4) {
            texts = new ArrayList<>();
            texts.add("未设置");
            texts.add("打开");
            texts.add("关闭");
            texts.add("开关停");
            texts.add("停止");
        }
        if (IsShowSelect) {
            listData = new ArrayList<>();
            for (int i = 0; i < listData_all.size(); i++) {
                if (MyApplication.getWareData().getKeyInputs().get(position_keyinput).getCanCpuID().equals(listData_all.get(i).getUnitid())) {
                    for (int j = 0; j < listData_all.get(i).getPrintCmds().size(); j++) {
                        if (listData_all.get(i).getPrintCmds().get(j).isSelect())
                            listData.add(listData_all.get(i).getPrintCmds().get(j));
                    }
                }
            }
        } else {
            listData = new ArrayList<>();
            for (int i = 0; i < Dev_KeysSetHelper.getListData_all().size(); i++) {
                if (MyApplication.getWareData().getKeyInputs().get(position_keyinput).getCanCpuID().equals(Dev_KeysSetHelper.getListData_all().get(i).getUnitid())) {
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
            viewHoler.rotateControButton = (RotateControButton) convertView.findViewById(R.id.RotateControButton);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        if (listData.get(position).isSelect()) {
            viewHoler.mIV.setImageResource(R.drawable.select_ok);
            viewHoler.rotateControButton.setCanTouch(true);
        } else {
            viewHoler.mIV.setImageResource(R.drawable.select_no);
            viewHoler.rotateControButton.setCanTouch(false);
        }

        //点击按钮选择关联设备并且设置相关命令

        if (DevType == 0) {
            if (listData.get(position).getKey_cmd() > 5)
                listData.get(position).setKey_cmd(0);
            viewHoler.rotateControButton.setTitle("开关", "按键命令", "温度-");
            viewHoler.rotateControButton.setTemp(0, 5, listData.get(position).getKey_cmd(), texts);
        } else if (DevType == 3) {
            if (listData.get(position).getKey_cmd() > 5)
                listData.get(position).setKey_cmd(0);
            viewHoler.rotateControButton.setTitle("打开", "按键命令", "变亮");
            viewHoler.rotateControButton.setTemp(0, 5, listData.get(position).getKey_cmd(), texts);
        } else if (DevType == 4) {
            if (listData.get(position).getKey_cmd() > 4)
                listData.get(position).setKey_cmd(0);
            viewHoler.rotateControButton.setTitle("打开", "按键命令", "停止");
            viewHoler.rotateControButton.setTemp(0, 4, listData.get(position).getKey_cmd(), texts);
        }

        viewHoler.mName.setText(listData.get(position).getKeyname());
        viewHoler.rotateControButton.setOnTempChangeListener(new RotateControButton.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                if (listData.get(position).isSelect()) {
                    listData.get(position).setKey_cmd(temp);
                }
            }
        });
        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.get(position).isSelect()) {
                    listData.get(position).setSelect(false);
                    listData.get(position).setKey_cmd(0);
                } else {
                    listData.get(position).setSelect(true);
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHoler {
        ImageView mIV;
        TextView mName;
        RotateControButton rotateControButton;
    }
}
