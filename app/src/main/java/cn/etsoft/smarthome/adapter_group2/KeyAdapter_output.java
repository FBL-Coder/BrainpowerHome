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

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.domain.PrintCmd;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/9/1.
 * 情景设置——灯光
 */
public class KeyAdapter_output extends BaseAdapter {
    private Context context;
    List<PrintCmd> listData;
    int mSelect = 0;   //选中项
    private List<String> on_off_name;

    public KeyAdapter_output(Context context, List<PrintCmd> listData) {
        this.listData = listData;
        this.context = context;
    }

    public void notifyDataSetChanged(List<PrintCmd> listData) {
        this.listData = listData;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != listData) {
            return listData.size();
        } else {
            return 0;
        }
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_item_light2, null, false);
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.mark = (ImageView) convertView.findViewById(R.id.mark);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.on_off = (TextView) convertView.findViewById(R.id.on_off);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(listData.get(position).getKeyname());
        //初始化PopupWindow数据
        initpopData(position);
        if (listData.get(position).isSelect()) {
            viewHolder.mark.setImageResource(R.drawable.selected);
            viewHolder.on_off.setText(on_off_name.get(listData.get(position).getKey_cmd()));
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPopupWindow(viewHolder.on_off, on_off_name, position);
                    popupWindow.showAsDropDown(v, -150, -80);
                }
            });
        } else {
            viewHolder.mark.setImageResource(R.drawable.select);
            viewHolder.on_off.setText(on_off_name.get(0));
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(context, "没选中不可操作");
                }
            });
        }
        viewHolder.appliance.setImageResource(R.drawable.key);

        viewHolder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.get(position).isSelect()) {
                    viewHolder.mark.setImageResource(R.drawable.select);
                    listData.get(position).setSelect(false);
                    listData.get(position).setKey_cmd(0);
                    viewHolder.on_off.setText(on_off_name.get(0));
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToast(context, "没选中不可操作");
                        }
                    });
                } else {
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    listData.get(position).setSelect(true);
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

    /**
     * 初始化PopupWindow数据
     *
     * @param position
     */
    private void initpopData(int position) {
        if (listData.get(position).getDevType() == 0) {
            on_off_name = new ArrayList<>();
            on_off_name.add(0, "未设置");
            on_off_name.add(1, "开关");
            on_off_name.add(2, "模式");
            on_off_name.add(3, "风速");
            on_off_name.add(4, "温度+");
            on_off_name.add(5, "温度-");
        } else if (listData.get(position).getDevType() == 1) {
            on_off_name = new ArrayList<>();
            on_off_name.add(0, "未设置");
            on_off_name.add(1, "打开");
            on_off_name.add(1, "关闭");
        } else if (listData.get(position).getDevType() == 2) {
            on_off_name = new ArrayList<>();
            on_off_name.add(0, "未设置");
            on_off_name.add(1, "打开");
            on_off_name.add(1, "关闭");
        } else if (listData.get(position).getDevType() == 3) {
            on_off_name = new ArrayList<>();
            on_off_name.add(0, "未设置");
            on_off_name.add(1, "打开");
            on_off_name.add(2, "关闭");
            on_off_name.add(3, "开关");
            on_off_name.add(4, "变亮");
            on_off_name.add(4, "变暗");
        } else if (listData.get(position).getDevType() == 4) {
            on_off_name = new ArrayList<>();
            on_off_name.add(0, "未设置" );
            on_off_name.add(1, "打开" );
            on_off_name.add(2, "关闭" );
            on_off_name.add(3, "停止" );
            on_off_name.add(3, "开关停");
        }
    }

    public void changeSelected(int position) { //刷新方法
        if (position != mSelect) {
            mSelect = position;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView appliance, mark;
        TextView title, on_off;
    }

    private PopupWindow popupWindow;

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int position_item) {
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
                listData.get(position_item).setKey_cmd(position);
//                for (int i = 0; i < items.size(); i++) {
//                    if (dev_list.get(position_scene).getDevId() == items.get(i).getDevID()
//                            && dev_list.get(position_scene).getType() == items.get(i).getDevType()
//                            && dev_list.get(position_scene).getCanCpuId().equals(items.get(i).getUid())) {
//                        items.get(i).setbOnOff((byte) position);
//                    }
//                }
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
