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

    public Key_DevsSetAdapter(int keyinpur_position, int keys_position, Context context, List<WareDev> roomDevs, boolean isShowSelect) {
        this.keys_position = keys_position;
        this.roomDevs = roomDevs;
        this.keyinpur_position = keyinpur_position;
        listData = new ArrayList<>();
        mContext = context;
        texts = new ArrayList<>();
        texts.add("关闭");
        texts.add("半关");
        texts.add("中间");
        texts.add("半开");
        texts.add("打开");
        IsShowSelect(isShowSelect);
    }


    public void notifyDataSetChanged(int keyinpur_position, int keys_position, Context context, List<WareDev> roomDevs, boolean isShowSelect) {
        this.roomDevs = roomDevs;
        this.keys_position = keys_position;
        this.keyinpur_position = keyinpur_position;
        super.notifyDataSetChanged();
        IsShowSelect(isShowSelect);
    }

    public void IsShowSelect(boolean IsShowSelect) {
        keyOpItems = Key_DevsSetHelper.getInput_key_data();
        listData = new ArrayList<>();
        //给所有设备和按键关联的赋值
        for (int j = 0; j < roomDevs.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < keyOpItems.size(); i++) {
                if (keyOpItems.get(i).getDevId() == roomDevs.get(j).getDevId()
                        && keyOpItems.get(i).getDevType() == roomDevs.get(j).getType()
                        && keyOpItems.get(i).getDevUnitID().equals(roomDevs.get(j).getCanCpuId())) {
                    roomDevs.get(j).setSelect(true);
                    roomDevs.get(j).setbOnOff(keyOpItems.get(i).getKeyOpCmd());
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
        } else {
            viewHoler.mIV.setImageResource(R.drawable.ic_launcher);
        }

        viewHoler.rotateControButton.setTemp(0, 4, 0, texts);
        viewHoler.mName.setText(listData.get(position).getDevName());

        viewHoler.rotateControButton.setOnTempChangeListener(new RotateControButton.OnTempChangeListener() {
            @Override
            public void change(int temp) {
                ToastUtil.showText("点击返回" + texts.get(temp));
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
