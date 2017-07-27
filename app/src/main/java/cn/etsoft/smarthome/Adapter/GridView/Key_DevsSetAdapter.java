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

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareKeyOpItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Key_DevsSetHelper;
import cn.etsoft.smarthome.View.RotateBtn.RotateControButton;

/**
 * Author：FBL  Time： 2017/6/26.
 * 按键配设备 设置——设备适配器
 */

public class Key_DevsSetAdapter extends BaseAdapter {

    private Context mContext;
    private List<WareDev> roomDevs;
    private List<WareDev> listData;
    private int keys_position;
    private int keyinpur_position;
    private List<String> texts;
    private List<WareKeyOpItem> keyOpItems;
    private int DevType = 0;
    private boolean isShowSelect;

    public Key_DevsSetAdapter(int devType, int keyinpur_position, int keys_position, Context context, List<WareDev> roomDevs, boolean isShowSelect) {
        this.keys_position = keys_position;
        this.roomDevs = roomDevs;
        this.keyinpur_position = keyinpur_position;
        DevType = devType;
        listData = new ArrayList<>();
        mContext = context;
        this.isShowSelect = isShowSelect;
        IsShowSelect(isShowSelect);
    }


    public void notifyDataSetChanged(int devType, int keyinpur_position, int keys_position, Context context, List<WareDev> roomDevs, boolean isShowSelect) {
        this.roomDevs = roomDevs;
        this.keys_position = keys_position;
        this.keyinpur_position = keyinpur_position;
        this.isShowSelect = isShowSelect;
        DevType = devType;
        super.notifyDataSetChanged();
        IsShowSelect(isShowSelect);
    }

    public void notifyDataSetChanged(List<WareDev> roomDevs) {
        listData = roomDevs;
        super.notifyDataSetChanged();
    }

    public void IsShowSelect(boolean IsShowSelect) {
        keyOpItems = Key_DevsSetHelper.getInput_key_data();
        listData = new ArrayList<>();

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
        //给所有设备和按键关联的赋值
        for (int j = 0; j < roomDevs.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < keyOpItems.size(); i++) {
                if (keyOpItems.get(i).getDevId() == roomDevs.get(j).getDevId()
                        && keyOpItems.get(i).getDevType() == roomDevs.get(j).getType()
                        && keyOpItems.get(i).getOut_cpuCanID().equals(roomDevs.get(j).getCanCpuId())) {
                    roomDevs.get(j).setSelect(true);
                    roomDevs.get(j).setCmd(keyOpItems.get(i).getKeyOpCmd());
                    Log.e("KeyOpCmd", keyOpItems.get(i).getKeyOpCmd() + "");
                    isContain = true;
                }
            }
            if (!isContain) {
                roomDevs.get(j).setSelect(false);
            }
        }
        if (IsShowSelect) {
            for (int i = 0; i < roomDevs.size(); i++) {
                if (roomDevs.get(i).isSelect())
                    listData.add(roomDevs.get(i));
            }
        } else
            listData.addAll(roomDevs);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.girdview_devs_item, null);
            viewHoler.mName = (TextView) convertView.findViewById(R.id.text_list_item);
            viewHoler.mIV = (ImageView) convertView.findViewById(R.id.img_list_item);
            viewHoler.rotateControButton = (RotateControButton) convertView.findViewById(R.id.RotateControButton);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHoler) convertView.getTag();

        if (listData.get(position).isSelect()) {
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher_round);
            viewHoler.rotateControButton.setCanTouch(true);
        } else {
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
            viewHoler.rotateControButton.setCanTouch(false);
        }

        if (DevType == 0) {
            viewHoler.rotateControButton.setTitle("开关", "设备命令", "温度-");
            viewHoler.rotateControButton.setTemp(0, 5, listData.get(position).getCmd(), texts);
        } else if (DevType == 3) {
            viewHoler.rotateControButton.setTitle("打开", "设备命令", "变亮");
            viewHoler.rotateControButton.setTemp(0, 5, listData.get(position).getCmd(), texts);
        } else if (DevType == 4) {
            viewHoler.rotateControButton.setTitle("未设置", "设备命令", "停止");
            viewHoler.rotateControButton.setTemp(0, 4, listData.get(position).getCmd(), texts);
        }
        viewHoler.mName.setText(listData.get(position).getDevName());

        viewHoler.rotateControButton.setOnTempChangeListener(new RotateControButton.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                if (listData.get(position).isSelect()) {
                    listData.get(position).setCmd(temp);
                    for (int i = 0; i < keyOpItems.size(); i++) {
                        if (listData.get(position).getDevId() == keyOpItems.get(i).getDevId()
                                && listData.get(position).getCanCpuId().equals(keyOpItems.get(i).getOut_cpuCanID())
                                && listData.get(position).getType() == keyOpItems.get(i).getDevType()) {
                            keyOpItems.get(i).setKeyOpCmd(temp);
                        }
                    }
                }
            }
        });

        viewHoler.mIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.get(position).isSelect()) {
                    listData.get(position).setSelect(false);
                    listData.get(position).setCmd(0);
                    for (int i = 0; i < keyOpItems.size(); i++) {
                        if (listData.get(position).getDevId() == keyOpItems.get(i).getDevId()
                                && listData.get(position).getCanCpuId().equals(keyOpItems.get(i).getOut_cpuCanID())
                                && listData.get(position).getType() == keyOpItems.get(i).getDevType()) {
                            keyOpItems.remove(i);
                        }
                    }
                } else {
                    listData.get(position).setSelect(true);
                    if (keyOpItems.size() == 0) {
                        WareKeyOpItem item = new WareKeyOpItem();
                        item.setKey_cpuCanID(MyApplication.getWareData().getKeyInputs().get(keyinpur_position).getCanCpuID());
                        item.setDevId(listData.get(position).getDevId());
                        item.setDevType(listData.get(position).getType());
                        item.setOut_cpuCanID(listData.get(position).getCanCpuId());
                        item.setIndex(keys_position);
                        item.setKeyOpCmd(0);
                        keyOpItems.add(item);
                    } else {
                        boolean isHave = false;
                        for (int i = 0; i < keyOpItems.size(); i++) {
                            if (listData.get(position).getDevId() == keyOpItems.get(i).getDevId()
                                    && listData.get(position).getCanCpuId().equals(keyOpItems.get(i).getOut_cpuCanID())
                                    && listData.get(position).getType() == keyOpItems.get(i).getDevType()) {
                                isHave = true;
                            }
                        }
                        if (!isHave) {
                            WareKeyOpItem item = new WareKeyOpItem();
                            item.setKey_cpuCanID(MyApplication.getWareData().getKeyInputs().get(keyinpur_position).getCanCpuID());
                            item.setDevId(listData.get(position).getDevId());
                            item.setDevType(listData.get(position).getType());
                            item.setOut_cpuCanID(listData.get(position).getCanCpuId());
                            item.setIndex(keys_position);
                            item.setKeyOpCmd(0);
                            keyOpItems.add(item);
                        }
                    }
                }
                notifyDataSetChanged(roomDevs);
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
