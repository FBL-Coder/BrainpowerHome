package cn.etsoft.smarthome.adapter_group2;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/9/1.
 * 情景设置——灯光
 */
public class CurtainAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareCurtain> curtain_room;
    private Context context;
    private int index = 0;
    private List<String> on_off_name;

    public CurtainAdapter(List<WareCurtain> curtain_room, Context context, LayoutInflater inflater,int index) {
        this.curtain_room = curtain_room;
        this.context = context;
        this.index = index;
        mInflater = inflater;
    }

    public void notifyDataSetChanged(List<WareCurtain> curtain_room) {
        this.curtain_room = curtain_room;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != curtain_room) {
            return curtain_room.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return curtain_room.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        boolean isContan = false;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_light2, null);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.mark = (ImageView) convertView.findViewById(R.id.mark);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.on_off = (TextView) convertView.findViewById(R.id.on_off);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(curtain_room.get(position).getDev().getDevName());
        viewHolder.appliance.setImageResource(R.drawable.quanguan);
        if (curtain_room.get(position).getDev().isSelect()){
            viewHolder.mark.setImageResource(R.drawable.selected);
            for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(curtain_room.get(position).getDev().getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == curtain_room.get(position).getDev().getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == curtain_room.get(position).getDev().getType()) {
                    switch (MyApplication.mInstance.getInput_key_data().get(i).getKeyOpCmd()){
                        case 0:
                            viewHolder.on_off.setText("打开");
                            break;
                        case 1:
                            viewHolder.on_off.setText("关闭");
                            break;
                        case 2:
                            viewHolder.on_off.setText("停止");
                            break;
                        case 3:
                            viewHolder.on_off.setText("开关停");
                            break;
                        default:
                            viewHolder.on_off.setText("未知");
                            break;
                    }
                }
            }
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPopupWindow(viewHolder.on_off, on_off_name, position);
                    popupWindow.showAsDropDown(v, -150, -80);
                }
            });
        }else {
            viewHolder.mark.setImageResource(R.drawable.select);
            viewHolder.on_off.setText("关闭");
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                }
            });
        }

        on_off_name = new ArrayList<>();
        on_off_name.add(0, "打开");
        on_off_name.add(1, "关闭");
        on_off_name.add(2, "停止");
        on_off_name.add(3, "开关停");
        viewHolder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curtain_room.get(position).getDev().isSelect()) {
                    for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                        if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(curtain_room.get(position).getDev().getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == curtain_room.get(position).getDev().getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == curtain_room.get(position).getDev().getType()) {
                            MyApplication.mInstance.getInput_key_data().remove(i);
                            viewHolder.mark.setImageResource(R.drawable.select);
                            curtain_room.get(position).getDev().setSelect(false);
                        }
                    }
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                        }
                    });
                }else {
                    WareKeyOpItem item = new WareKeyOpItem();
                    item.setDevType((byte) curtain_room.get(position).getDev().getType());
                    item.setDevId((byte) curtain_room.get(position).getDev().getDevId());
                    item.setDevUnitID(curtain_room.get(position).getDev().getCanCpuId());
                    item.setIndex((byte) index);
                    item.setKeyOp((byte) 1);
                    item.setKeyOpCmd((byte) 0);
                    MyApplication.mInstance.getInput_key_data().add(item);
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    curtain_room.get(position).getDev().setSelect(true);
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initPopupWindow(viewHolder.on_off, on_off_name, position);
                            popupWindow.showAsDropDown(v, -150, -80);
                        }
                    });
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView appliance, mark;
        TextView title, on_off;
    }

    private PopupWindow popupWindow;

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int position_curtain) {
        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(context, R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        customView.setFocusable(true);
        customView.setFocusableInTouchMode(true);
        customView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), 200, 300);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, context);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                    if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(curtain_room.get(position_curtain).getDev().getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == curtain_room.get(position_curtain).getDev().getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == curtain_room.get(position_curtain).getDev().getType()) {
                        MyApplication.mInstance.getInput_key_data().get(i).setKeyOpCmd((byte) position);
                    }
                }
                curtain_room.get(position_curtain).getDev().setbOnOff((byte) position);
                popupWindow.dismiss();
            }
        });
        //popupwindow页面之外可点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }
}
