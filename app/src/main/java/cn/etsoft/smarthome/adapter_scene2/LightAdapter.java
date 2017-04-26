package cn.etsoft.smarthome.adapter_scene2;

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
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/9/1.
 * 情景设置——灯光
 */
public class LightAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<WareLight> light_room;
    private Context context;
    private byte sceneid = 0;
    boolean IsScene = true;
    private List<String> on_off_name;
    private List<WareSceneDevItem> items = null;

    public LightAdapter(List<WareLight> light_room, Context context, LayoutInflater inflater, byte sceneid, boolean IsScene) {
        this.light_room = light_room;
        this.context = context;
        this.sceneid = sceneid;
        this.IsScene = IsScene;
        mInflater = inflater;
        for (int i = 0; i < MyApplication.getWareData_Scene().getSceneEvents().size(); i++) {
            if (sceneid == MyApplication.getWareData_Scene().getSceneEvents().get(i).getEventld()) {
                items = MyApplication.getWareData_Scene().getSceneEvents().get(i).getItemAry();
                break;
            }
        }
    }

    public void notifyDataSetChanged(List<WareLight> light_room) {
        this.light_room = light_room;
        IsScene = false;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != light_room) {
            return light_room.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return light_room.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
        if (items == null) {
            items = new ArrayList<>();
            for (int i = 0; i < light_room.size(); i++) {
                //设备默认图标  或者是默认为关闭状态
                viewHolder.appliance.setImageResource(R.drawable.light);
                viewHolder.mark.setImageResource(R.drawable.select);
            }
        } else {
            for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                if (items.get(i).getDevID() == light_room.get(position).getDev().getDevId()
                        && items.get(i).getUid().equals(light_room.get(position).getDev().getCanCpuId())
                        && items.get(i).getDevType() == light_room.get(position).getDev().getType()) {
                    isContan = true;
                    light_room.get(position).getDev().setSelect(true);
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    if (items.get(i).getbOnOff() == 0) {
                        viewHolder.on_off.setText("关");
                    } else if (items.get(i).getbOnOff() == 1) {
                        viewHolder.on_off.setText("开");
                    } else if (items.get(i).getbOnOff() == 2) {
                        viewHolder.on_off.setText("开关");
                    }
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initPopupWindow(viewHolder.on_off, on_off_name, position);
                            popupWindow.showAsDropDown(v, -150, -80);
                        }
                    });
                }
            }
            if (!isContan) {
                viewHolder.on_off.setText("关");
                light_room.get(position).getDev().setSelect(false);
                viewHolder.mark.setImageResource(R.drawable.select);
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                    }
                });
            }
            isContan = false;
        }
        viewHolder.title.setText(light_room.get(position).getDev().getDevName());
        viewHolder.appliance.setImageResource(R.drawable.light);
        on_off_name = new ArrayList<>();
        on_off_name.add(0, "关");
        on_off_name.add(1, "开");
        on_off_name.add(2, "开关");

        viewHolder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneid < 2) {
                    ToastUtil.showToast(context, "全开、全关模式不可操作");
                    return;
                }
                if (light_room.get(position).getDev().isSelect()) {
                    viewHolder.mark.setImageResource(R.drawable.select);
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                        }
                    });
                    light_room.get(position).getDev().setSelect(false);
                    for (int i = 0; i < items.size(); i++) {
                        if (light_room.get(position).getDev().getDevId() == items.get(i).getDevID()
                                && light_room.get(position).getDev().getType() == items.get(i).getDevType()
                                && light_room.get(position).getDev().getCanCpuId().equals(items.get(i).getUid())) {
                            items.remove(i);

                        }
                    }
                } else {
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setbOnOff((byte) 0);
                    item.setDevID((byte) light_room.get(position).getDev().getDevId());
                    item.setDevType((byte) light_room.get(position).getDev().getType());
                    item.setUid(light_room.get(position).getDev().getCanCpuId());
                    items.add(item);
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            initPopupWindow(viewHolder.on_off, on_off_name, position);
                            popupWindow.showAsDropDown(v, -150, -80);
                        }
                    });
                    light_room.get(position).getDev().setSelect(true);
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
    private void initPopupWindow(final View view_parent, final List<String> text, final int position_scene) {

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
                for (int i = 0; i < items.size(); i++) {
                    if (light_room.get(position_scene).getDev().getDevId() == items.get(i).getDevID()
                            && light_room.get(position_scene).getDev().getType() == items.get(i).getDevType()
                            && light_room.get(position_scene).getDev().getCanCpuId().equals(items.get(i).getUid())) {
                        items.get(i).setbOnOff((byte) position);
                    }
                }

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
