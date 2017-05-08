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
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/11/25.
 * 情景设置——家电
 */
public class ApplianceAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private List<WareDev> dev_list;
    private byte sceneid = 0;
    boolean IsScene = true;
    private List<String> on_off_name1;
    private List<String> on_off_name2;
    private List<String> on_off_name3;
    private List<WareSceneDevItem> items = null;

    public ApplianceAdapter(List<WareDev> list, Context context, LayoutInflater inflater, byte sceneid, boolean IsScene) {
        this.context = context;
        this.sceneid = sceneid;
        this.IsScene = IsScene;
        mInflater = inflater;
        dev_list = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getSceneEvents().size(); i++) {
            if (sceneid == MyApplication.getWareData_Scene().getSceneEvents().get(i).getEventld()) {
                items = MyApplication.getWareData_Scene().getSceneEvents().get(i).getItemAry();
                break;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 0 || list.get(i).getType() == 1 || list.get(i).getType() == 2)
                dev_list.add(list.get(i));
        }
    }


    public void notifyDataSetChanged(List<WareDev> list) {
        dev_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 0 || list.get(i).getType() == 1 || list.get(i).getType() == 2)
                dev_list.add(list.get(i));
        }
        IsScene = false;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null != dev_list) {
            return dev_list.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return dev_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    WareAirCondDev AirCondDev;
    WareTv TV;
    WareSetBox TVUP;
    List<WareAirCondDev> AicList;
    List<WareTv> TVList;
    List<WareSetBox> tbsList;

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        boolean isContan = false;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_item_light2, null, false);
            viewHolder = new ViewHolder();
            viewHolder.appliance = (ImageView) convertView.findViewById(R.id.appliance);
            viewHolder.mark = (ImageView) convertView.findViewById(R.id.mark);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.on_off = (TextView) convertView.findViewById(R.id.on_off);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(dev_list.get(position).getDevName());

        if (dev_list.size() < 1)
            return convertView;

        if (dev_list.get(position).getType() == 0) {
            AicList = MyApplication.getWareData_Scene().getAirConds();
            for (int i = 0; i < AicList.size(); i++) {
                if (dev_list.get(position).getDevId() == AicList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(AicList.get(i).getDev().getCanCpuId())) {
                    AirCondDev = AicList.get(i);
                }
            }
            //设备默认图标  或者是默认为关闭状态
            viewHolder.appliance.setImageResource(R.drawable.kongtiao1);
            on_off_name1 = new ArrayList<>();
            on_off_name1.add(0, "关");
            on_off_name1.add(1, "开");
            on_off_name1.add(2, "温度+");
            on_off_name1.add(3, "温度-");
            on_off_name1.add(4, "低风");
            on_off_name1.add(5, "中风");
            on_off_name1.add(6, "高风");
            viewHolder.on_off.setText("关");
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                }
            });
        } else if (dev_list.get(position).getType() == 1) {
            TVList = MyApplication.getWareData_Scene().getTvs();
            for (int i = 0; i < TVList.size(); i++) {
                if (dev_list.get(position).getDevId() == TVList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(TVList.get(i).getDev().getCanCpuId())) {
                    TV = TVList.get(i);
                }
            }
            //设备默认图标  或者是默认为关闭状态
            viewHolder.appliance.setImageResource(R.drawable.ds);
            on_off_name2 = new ArrayList<>();
            on_off_name2.add(0, "关");
            on_off_name2.add(1, "开");
            on_off_name2.add(2, "音量+");
            on_off_name2.add(3, "音量-");
            on_off_name2.add(4, "频道+");
            on_off_name2.add(5, "频道-");
            on_off_name2.add(6, "V/AV");
            viewHolder.on_off.setText("关");
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                }
            });
        } else if (dev_list.get(position).getType() == 2) {
            tbsList = MyApplication.getWareData_Scene().getStbs();
            tbsList = MyApplication.getWareData_Scene().getStbs();
            for (int i = 0; i < tbsList.size(); i++) {
                if (dev_list.get(position).getDevId() == tbsList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(tbsList.get(i).getDev().getCanCpuId())) {
                    TVUP = tbsList.get(i);
                }
            }
            //设备默认图标  或者是默认为关闭状态
            viewHolder.appliance.setImageResource(R.drawable.jidinghe1);
            on_off_name3 = new ArrayList<>();
            on_off_name3.add(0, "关");
            on_off_name3.add(1, "开");
            on_off_name3.add(2, "音量+");
            on_off_name3.add(3, "音量-");
            on_off_name3.add(4, "频道+");
            on_off_name3.add(5, "频道-");
            viewHolder.on_off.setText("关");
            viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                }
            });
        }

        if (items == null) {
            items = new ArrayList<>();
            for (int i = 0; i < dev_list.size(); i++) {
                if (dev_list.get(position).getType() == 0) {
                    viewHolder.appliance.setImageResource(R.drawable.kongtiao1);
                    viewHolder.mark.setImageResource(R.drawable.select);
                    viewHolder.on_off.setText("关");
                } else if (dev_list.get(position).getType() == 1) {
                    viewHolder.appliance.setImageResource(R.drawable.tv1);
                    viewHolder.mark.setImageResource(R.drawable.select);
                    viewHolder.on_off.setText("关");
                } else if (dev_list.get(position).getType() == 2) {
                    viewHolder.appliance.setImageResource(R.drawable.jidinghe1);
                    viewHolder.mark.setImageResource(R.drawable.select);
                    viewHolder.on_off.setText("关");
                }
            }
            for (int i = 0; i < MyApplication.getWareData_Scene().getSceneEvents().size(); i++) {
                if (sceneid == MyApplication.getWareData_Scene().getSceneEvents().get(i).getEventld()) {
                    MyApplication.getWareData_Scene().getSceneEvents().get(i).setItemAry(items);
                    break;
                }
            }
        } else {
            if (dev_list.get(position).getType() == 0) {
                for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                    if (items.get(i).getDevType() == 0) {
                        if (items.get(i).getDevID() == dev_list.get(position).getDevId() && items.get(i).getUid().equals(dev_list.get(position).getCanCpuId())) {
                            AicList = MyApplication.getWareData_Scene().getAirConds();
                            for (int j = 0; j < AicList.size(); j++) {
                                if (AicList.get(j).getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        AicList.get(j).getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    isContan = true;
                                    dev_list.get(position).setSelect(true);
                                    viewHolder.mark.setImageResource(R.drawable.selected);
                                    if (items.get(i).getbOnOff() == 0) {
                                        viewHolder.on_off.setText("关");
                                    } else if (items.get(i).getbOnOff() == 1) {
                                        viewHolder.on_off.setText("开");
                                    } else if (items.get(i).getbOnOff() == 2) {
                                        viewHolder.on_off.setText("温度+");
                                    }else if (items.get(i).getbOnOff() == 3) {
                                        viewHolder.on_off.setText("温度—");
                                    } else if (items.get(i).getbOnOff() == 4) {
                                        viewHolder.on_off.setText("低风");
                                    }else if (items.get(i).getbOnOff() == 5) {
                                        viewHolder.on_off.setText("中风");
                                    } else if (items.get(i).getbOnOff() == 6) {
                                        viewHolder.on_off.setText("高风");
                                    }
                                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initPopupWindow(viewHolder.on_off, on_off_name1, position);
                                            popupWindow.showAsDropDown(v, -150, -80);
                                        }
                                    });
                                }
                            }
                            if (!isContan) {
                                viewHolder.on_off.setText("关");
                                dev_list.get(position).setSelect(true);
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
                    }
                }
            } else if (dev_list.get(position).getType() == 1) {
                for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                    if (items.get(i).getDevType() == 1) {
                        if (items.get(i).getDevID() == dev_list.get(position).getDevId() && items.get(i).getUid().equals(dev_list.get(position).getCanCpuId())) {
                            TVList = MyApplication.getWareData_Scene().getTvs();
                            for (int j = 0; j < TVList.size(); j++) {
                                if (TVList.get(j).getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        TVList.get(j).getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    isContan = true;
                                    dev_list.get(position).setSelect(true);
                                    viewHolder.mark.setImageResource(R.drawable.selected);
                                    if (items.get(i).getbOnOff() == 0) {
                                        viewHolder.on_off.setText("关");
                                    } else if (items.get(i).getbOnOff() == 1) {
                                        viewHolder.on_off.setText("开");
                                    }else if (items.get(i).getbOnOff() == 2) {
                                        viewHolder.on_off.setText("音量+");
                                    } else if (items.get(i).getbOnOff() == 3) {
                                        viewHolder.on_off.setText("音量—");
                                    }else if (items.get(i).getbOnOff() == 4) {
                                        viewHolder.on_off.setText("频道+");
                                    } else if (items.get(i).getbOnOff() == 5) {
                                        viewHolder.on_off.setText("频道—");
                                    }else if (items.get(i).getbOnOff() == 6) {
                                        viewHolder.on_off.setText("V/AV");
                                    }
                                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initPopupWindow(viewHolder.on_off, on_off_name2, position);
                                            popupWindow.showAsDropDown(v, -150, -80);
                                        }
                                    });
                                }
                            }
                            if (!isContan) {
                                viewHolder.on_off.setText("关");
                                dev_list.get(position).setSelect(true);
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
                    }
                }
            }  else if (dev_list.get(position).getType() == 2) {
                for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                    if (items.get(i).getDevType() == 2) {
                        if (items.get(i).getDevID() == dev_list.get(position).getDevId() && items.get(i).getUid().equals(dev_list.get(position).getCanCpuId())) {
                            tbsList = MyApplication.getWareData_Scene().getStbs();
                            for (int j = 0; j < tbsList.size(); j++) {
                                if (tbsList.get(j).getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        tbsList.get(j).getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    isContan = true;
                                    dev_list.get(position).setSelect(true);
                                    viewHolder.mark.setImageResource(R.drawable.selected);
                                    if (items.get(i).getbOnOff() == 0) {
                                        viewHolder.on_off.setText("关");
                                    } else if (items.get(i).getbOnOff() == 1) {
                                        viewHolder.on_off.setText("开");
                                    }else if (items.get(i).getbOnOff() == 2) {
                                        viewHolder.on_off.setText("音量+");
                                    } else if (items.get(i).getbOnOff() == 3) {
                                        viewHolder.on_off.setText("音量—");
                                    }else if (items.get(i).getbOnOff() == 4) {
                                        viewHolder.on_off.setText("频道+");
                                    } else if (items.get(i).getbOnOff() == 5) {
                                        viewHolder.on_off.setText("频道—");
                                    }
                                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initPopupWindow(viewHolder.on_off, on_off_name3, position);
                                            popupWindow.showAsDropDown(v, -150, -80);
                                        }
                                    });
                                }
                            }
                            if (!isContan) {
                                viewHolder.on_off.setText("关");
                                dev_list.get(position).setSelect(true);
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
                    }
                }
            }
        }

        viewHolder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sceneid < 2) {
                    ToastUtil.showToast(context, "全开、全关模式不可操作");
                    return;
                }
                if (dev_list.get(position).isSelect()) {
                    viewHolder.mark.setImageResource(R.drawable.select);
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToast(context, "设备未选中，选中才能操作...");

                        }
                    });
                    dev_list.get(position).setSelect(false);
                    for (int i = 0; i < items.size(); i++) {
                        if (dev_list.get(position).getDevId() == items.get(i).getDevID()
                                && dev_list.get(position).getType() == items.get(i).getDevType()
                                && dev_list.get(position).getCanCpuId().equals(items.get(i).getUid())) {
                            items.remove(i);
                        }
                    }
                } else {
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    WareSceneDevItem item = new WareSceneDevItem();
                    item.setbOnOff((byte) 0);
                    item.setDevID((byte) dev_list.get(position).getDevId());
                    item.setDevType((byte) dev_list.get(position).getType());
                    item.setUid(dev_list.get(position).getCanCpuId());
                    items.add(item);
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dev_list.get(position).getType() == 0) {
                                initPopupWindow(viewHolder.on_off, on_off_name1, position);
                            }else if (dev_list.get(position).getType() == 1) {
                                initPopupWindow(viewHolder.on_off, on_off_name2, position);
                            }else if (dev_list.get(position).getType() == 2) {
                                initPopupWindow(viewHolder.on_off, on_off_name3, position);
                            }
                            popupWindow.showAsDropDown(v, -150, -80);
                        }
                    });
                    dev_list.get(position).setSelect(true);
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
                    if (dev_list.get(position_scene).getDevId() == items.get(i).getDevID()
                            && dev_list.get(position_scene).getType() == items.get(i).getDevType()
                            && dev_list.get(position_scene).getCanCpuId().equals(items.get(i).getUid())) {
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
