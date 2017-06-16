package cn.etsoft.smarthome.adapter_group3;

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
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/9/1.
 * 高级设置--控制设置--按键配设备--显示设备的Fragment的适配器
 */
public class InputAdapter_dev extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private int index;
    private List<String> on_off_name1;
    private List<String> on_off_name2;
    private List<String> on_off_name3;
    private List<String> on_off_name4;
    private List<String> on_off_name5;
    private List<WareDev> dev_list;


    public InputAdapter_dev(List<WareDev> dev_list, Context context, LayoutInflater inflater, int index) {
        this.dev_list = dev_list;
        this.context = context;
        this.index = index;
        mInflater = inflater;
    }

    public void notifyDataSetChanged(List<WareDev> dev_list) {
        this.dev_list = dev_list;
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
    WareSetBox TvUP;
    WareLight Light;
    WareCurtain Curtain;
    List<WareAirCondDev> AicList;
    List<WareTv> TVList;
    List<WareLight> lightList;
    List<WareSetBox> tbsList;
    List<WareCurtain> curtainList;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
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

        viewHolder.title.setText(dev_list.get(position).getDevName());
        if (dev_list.get(position).getType() == 0) {
            AicList = MyApplication.getWareData().getAirConds();
            for (int i = 0; i < AicList.size(); i++) {
                if (dev_list.get(position).getDevId() == AicList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(AicList.get(i).getDev().getCanCpuId())) {
                    AirCondDev = AicList.get(i);
                }
            }
            //设备默认图标或者是默认为关闭状态
            if (dev_list.get(position).isSelect()) {
                viewHolder.mark.setImageResource(R.drawable.selected);
                for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                    if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(dev_list.get(position).getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == dev_list.get(position).getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == dev_list.get(position).getType()) {
                        switch (MyApplication.mInstance.getInput_key_data().get(i).getKeyOpCmd()) {
                            case 0:
                                viewHolder.on_off.setText("未设置");
                                break;
                            case 1:
                                viewHolder.on_off.setText("开关");
                                break;
                            case 2:
                                viewHolder.on_off.setText("模式");
                                break;
                            case 3:
                                viewHolder.on_off.setText("风速");
                                break;
                            case 4:
                                viewHolder.on_off.setText("温度+");
                                break;
                            case 5:
                                viewHolder.on_off.setText("温度—");
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
                        initPopupWindow(viewHolder.on_off, on_off_name1, position);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                });
            } else {
                viewHolder.mark.setImageResource(R.drawable.select);
                viewHolder.on_off.setText("开关");
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                    }
                });
            }
            viewHolder.appliance.setImageResource(R.drawable.kongtiao1);
            on_off_name1 = new ArrayList<>();
            on_off_name1.add(0, "未设置");
            on_off_name1.add(1, "开关");
            on_off_name1.add(2, "模式");
            on_off_name1.add(3, "风速");
            on_off_name1.add(4, "温度+");
            on_off_name1.add(5, "温度-");
            click(viewHolder, position);
        } else if (dev_list.get(position).getType() == 1) {
            TVList = MyApplication.getWareData().getTvs();
            for (int i = 0; i < TVList.size(); i++) {
                if (dev_list.get(position).getDevId() == TVList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(TVList.get(i).getDev().getCanCpuId())) {
                    TV = TVList.get(i);
                }
            }
            //设备默认图标  或者是默认为关闭状态
            if (dev_list.get(position).isSelect()) {
                viewHolder.mark.setImageResource(R.drawable.selected);
                switch (dev_list.get(position).getbOnOff()) {
                    case 0:
                        viewHolder.on_off.setText("未设置");
                        break;
                    case 1:
                        viewHolder.on_off.setText("电源");
                        break;
                    case 2:
                        viewHolder.on_off.setText("音量+");
                        break;
                    case 3:
                        viewHolder.on_off.setText("音量-");
                        break;
                    case 4:
                        viewHolder.on_off.setText("频道+");
                        break;
                    case 5:
                        viewHolder.on_off.setText("频道-");
                        break;
                    case 6:
                        viewHolder.on_off.setText("V/AV");
                        break;
                    default:
                        viewHolder.on_off.setText("未知");
                        break;
                }
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initPopupWindow(viewHolder.on_off, on_off_name2, position);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                });
            } else {
                viewHolder.mark.setImageResource(R.drawable.select);
                viewHolder.on_off.setText("电源");
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                    }
                });
            }
            viewHolder.appliance.setImageResource(R.drawable.ds);
            on_off_name2 = new ArrayList<>();
            on_off_name2.add(0, "未设置");
            on_off_name2.add(1, "电源");
            on_off_name2.add(2, "音量+");
            on_off_name2.add(3, "音量-");
            on_off_name2.add(4, "频道+");
            on_off_name2.add(5, "频道-");
            on_off_name2.add(6, "V/AV");
            click(viewHolder, position);
        } else if (dev_list.get(position).getType() == 2) {
            tbsList = MyApplication.getWareData().getStbs();
            for (int i = 0; i < tbsList.size(); i++) {
                if (dev_list.get(position).getDevId() == tbsList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(tbsList.get(i).getDev().getCanCpuId())) {
                    TvUP = tbsList.get(i);
                }
            }
            //设备默认图标  或者是默认为关闭状态
            if (dev_list.get(position).isSelect()) {
                viewHolder.mark.setImageResource(R.drawable.selected);
                switch (dev_list.get(position).getbOnOff()) {
                    case 0:
                        viewHolder.on_off.setText("未设置");
                        break;
                    case 1:
                        viewHolder.on_off.setText("电源");
                        break;
                    case 2:
                        viewHolder.on_off.setText("音量+");
                        break;
                    case 3:
                        viewHolder.on_off.setText("音量—");
                        break;
                    case 4:
                        viewHolder.on_off.setText("频道+");
                        break;
                    case 5:
                        viewHolder.on_off.setText("频道—");
                        break;
                    default:
                        viewHolder.on_off.setText("未知");
                        break;
                }
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initPopupWindow(viewHolder.on_off, on_off_name3, position);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                });
            } else {
                viewHolder.mark.setImageResource(R.drawable.select);
                viewHolder.on_off.setText("电源");
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                    }
                });
            }
            //设备默认图标  或者是默认为关闭状态
            viewHolder.appliance.setImageResource(R.drawable.jidinghe1);
            on_off_name3 = new ArrayList<>();
            on_off_name3.add(0, "未设置");
            on_off_name3.add(1, "电源");
            on_off_name3.add(2, "音量+");
            on_off_name3.add(3, "音量-");
            on_off_name3.add(4, "频道+");
            on_off_name3.add(5, "频道-");
            click(viewHolder, position);
        }else if (dev_list.get(position).getType() == 3) {
            lightList = MyApplication.getWareData().getLights();
            for (int i = 0; i < lightList.size(); i++) {
                if (dev_list.get(position).getDevId() == lightList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(lightList.get(i).getDev().getCanCpuId())) {
                    Light = lightList.get(i);
                }
            }
            if (dev_list.get(position).isSelect()) {
                viewHolder.mark.setImageResource(R.drawable.selected);
                for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                    if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(dev_list.get(position).getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == dev_list.get(position).getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == dev_list.get(position).getType()) {
                        switch (MyApplication.mInstance.getInput_key_data().get(i).getKeyOpCmd()) {
                            case 0:
                                viewHolder.on_off.setText("未设置");
                                break;
                            case 1:
                                viewHolder.on_off.setText("打开");
                                break;
                            case 2:
                                viewHolder.on_off.setText("关闭");
                                break;
                            case 3:
                                viewHolder.on_off.setText("开关");
                                break;
                            case 4:
                                viewHolder.on_off.setText("变亮");
                                break;
                            case 5:
                                viewHolder.on_off.setText("变暗");
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
                        initPopupWindow(viewHolder.on_off, on_off_name4, position);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                });
            } else {
                viewHolder.mark.setImageResource(R.drawable.select);
                viewHolder.on_off.setText("打开");
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                    }
                });
            }
            //设备默认图标  或者是默认为关闭状态
            viewHolder.appliance.setImageResource(R.drawable.light);
            on_off_name4 = new ArrayList<>();
            on_off_name4.add(0, "未设置");
            on_off_name4.add(1, "打开");
            on_off_name4.add(2, "关闭");
            on_off_name4.add(3, "开关");
            on_off_name4.add(4, "变亮");
            on_off_name4.add(5, "变暗");
            click(viewHolder, position);
        }else if (dev_list.get(position).getType() == 4) {
            curtainList = MyApplication.getWareData().getCurtains();
            for (int i = 0; i < curtainList.size(); i++) {
                if (dev_list.get(position).getDevId() == curtainList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(curtainList.get(i).getDev().getCanCpuId())) {
                    Curtain = curtainList.get(i);
                }
            }
            if (dev_list.get(position).isSelect()){
                viewHolder.mark.setImageResource(R.drawable.selected);
                for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                    if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(dev_list.get(position).getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == dev_list.get(position).getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == dev_list.get(position).getType()) {
                        switch (MyApplication.mInstance.getInput_key_data().get(i).getKeyOpCmd()){
                            case 0:
                                viewHolder.on_off.setText("未设置");
                                break;
                            case 1:
                                viewHolder.on_off.setText("打开");
                                break;
                            case 2:
                                viewHolder.on_off.setText("关闭");
                                break;
                            case 3:
                                viewHolder.on_off.setText("停止");
                                break;
                            case 4:
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
                        initPopupWindow(viewHolder.on_off, on_off_name5, position);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                });
            } else {
                viewHolder.mark.setImageResource(R.drawable.select);
                viewHolder.on_off.setText("打开");
                viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                    }
                });
            }
            //设备默认图标  或者是默认为关闭状态
            viewHolder.appliance.setImageResource(R.drawable.quanguan);
            on_off_name5 = new ArrayList<>();
            on_off_name5.add(0, "未设置" );
            on_off_name5.add(1, "打开" );
            on_off_name5.add(2, "关闭" );
            on_off_name5.add(3, "停止" );
            on_off_name5.add(4, "开关停");
            click(viewHolder, position);
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView appliance, mark;
        TextView title, on_off;
    }

    public void click(final ViewHolder viewHolder, final int position) {
        viewHolder.mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dev_list.get(position).isSelect()) {
                    for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                        if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(dev_list.get(position).getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == dev_list.get(position).getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == dev_list.get(position).getType()) {
                            MyApplication.mInstance.getInput_key_data().remove(i);
                            viewHolder.mark.setImageResource(R.drawable.select);
                            dev_list.get(position).setSelect(false);
                        }
                    }
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showToast(context, "设备未选中，选中才能操作...");
                        }
                    });
                } else {
                    WareKeyOpItem item = new WareKeyOpItem();
                    item.setDevType((byte) dev_list.get(position).getType());
                    item.setDevId((byte) dev_list.get(position).getDevId());
                    item.setDevUnitID(dev_list.get(position).getCanCpuId());
                    item.setIndex((byte) index);
                    item.setKeyOp((byte) 1);
                    item.setKeyOpCmd((byte) 0);
                    MyApplication.mInstance.getInput_key_data().add(item);
                    viewHolder.mark.setImageResource(R.drawable.selected);
                    dev_list.get(position).setSelect(true);
                    viewHolder.on_off.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (dev_list.get(position).getType() == 0) {
                                initPopupWindow(viewHolder.on_off, on_off_name1, position);
                            }else if (dev_list.get(position).getType() == 1) {
                                initPopupWindow(viewHolder.on_off, on_off_name2, position);
                            }else if (dev_list.get(position).getType() == 2) {
                                initPopupWindow(viewHolder.on_off, on_off_name3, position);
                            }else if (dev_list.get(position).getType() == 3) {
                                initPopupWindow(viewHolder.on_off, on_off_name4, position);
                            }else if (dev_list.get(position).getType() == 4) {
                                initPopupWindow(viewHolder.on_off, on_off_name5, position);
                            }
                            popupWindow.showAsDropDown(v,0, 0);
                        }
                    });
                }

            }
        });
    }

    private PopupWindow popupWindow;

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int position_dev) {
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 250);
        popupWindow.setContentView(customView);
        final ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, context);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                for (int i = 0; i < MyApplication.mInstance.getInput_key_data().size(); i++) {
                    if (MyApplication.mInstance.getInput_key_data().get(i).getDevUnitID().equals(dev_list.get(position_dev).getCanCpuId()) && MyApplication.mInstance.getInput_key_data().get(i).getDevId() == dev_list.get(position_dev).getDevId() && MyApplication.mInstance.getInput_key_data().get(i).getDevType() == dev_list.get(position_dev).getType()) {
                        MyApplication.mInstance.getInput_key_data().get(i).setKeyOpCmd((byte) position);
                    }
                }
                dev_list.get(position_dev).setbOnOff((byte) position);
                popupWindow.dismiss();
            }
        });
        //popupWindow页面之外可点
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