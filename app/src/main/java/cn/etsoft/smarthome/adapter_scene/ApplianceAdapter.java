package cn.etsoft.smarthome.adapter_scene;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
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
    private Context context;
    private LayoutInflater inflater;
    private List<WareDev> dev_list;
    private int eventId;

    public ApplianceAdapter(List<WareDev> list, Context context, LayoutInflater inflater, int eventId) {
        this.context = context;
        this.inflater = inflater;
        this.eventId = eventId;
        dev_list = new ArrayList<>();
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
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dev_list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.from(this.context).inflate(R.layout.listview_appliance, null, false);
            viewHolder.text_appliance = (TextView) convertView.findViewById(R.id.text_appliance);
            viewHolder.text_temp = (TextView) convertView.findViewById(R.id.text_temp);
            viewHolder.btn1 = (Button) convertView.findViewById(R.id.btn1);
            viewHolder.btn1.setBackgroundResource(R.drawable.rect_red);
            viewHolder.btn2 = (Button) convertView.findViewById(R.id.btn2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_appliance.setText(dev_list.get(position).getDevName());
        viewHolder.btn1.setVisibility(View.VISIBLE);
        viewHolder.btn2.setVisibility(View.VISIBLE);
        viewHolder.btn1.setText("关");
        viewHolder.btn2.setText("开");

        List<WareSceneDevItem> items = null;
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
            if (eventId == MyApplication.getWareData().getSceneEvents().get(i).getEventld()) {
                items = MyApplication.getWareData().getSceneEvents().get(i).getItemAry();
                break;
            }
        }
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
        } else if (dev_list.get(position).getType() == 1) {
            TVList = MyApplication.getWareData_Scene().getTvs();
            for (int i = 0; i < TVList.size(); i++) {
                if (dev_list.get(position).getDevId() == TVList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(TVList.get(i).getDev().getCanCpuId())) {
                    TV = TVList.get(i);
                }
            }
        } else if (dev_list.get(position).getType() == 2) {
            tbsList = MyApplication.getWareData_Scene().getStbs();
            for (int i = 0; i < tbsList.size(); i++) {
                if (dev_list.get(position).getDevId() == tbsList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(tbsList.get(i).getDev().getCanCpuId())) {
                    TVUP = tbsList.get(i);
                }
            }
        }
        if (items == null) {
            for (int i = 0; i < dev_list.size(); i++) {
                //设备默认图标  或者是默认为关闭状态
                viewHolder.btn1.setBackgroundResource(R.drawable.rect_red);
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
                                    AicList.get(j).setbOnOff((byte) 1);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
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
                                    TVList.get(j).setbOnOff((byte) 1);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                }
            } else if (dev_list.get(position).getType() == 2) {
                for (int i = 0; i < items.size(); i++) {//根据设给的数据判断状态以及显示图标
                    if (items.get(i).getDevType() == 2) {
                        if (items.get(i).getDevID() == dev_list.get(position).getDevId() && items.get(i).getUid().equals(dev_list.get(position).getCanCpuId())) {
                            tbsList = MyApplication.getWareData_Scene().getStbs();
                            for (int j = 0; j < tbsList.size(); j++) {
                                if (tbsList.get(j).getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        tbsList.get(j).getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    tbsList.get(j).setbOnOff((byte) 1);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                }
            }

        }

        //点击关按钮操作
        viewHolder.btn1.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (eventId < 2) {
                    ToastUtil.showToast(context, "全开、全关模式不可操作");
                    return;
                }
                if (dev_list.get(position).getType() == 0) {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getAirConds().size(); i++) {
                        if (AirCondDev.getDev().getDevId() == MyApplication.getWareData_Scene().getAirConds().get(i).getDev().getDevId() &&
                                AirCondDev.getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getAirConds().get(i).getDev().getCanCpuId())) {
                            for (int j = 0; j < dev_list.size(); j++) {
                                if (AirCondDev.getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        AirCondDev.getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    MyApplication.getWareData_Scene().getAirConds().get(i).setbOnOff((byte) 0);
                                    AirCondDev.setbOnOff((byte) 0);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_red);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                } else if (dev_list.get(position).getType() == 1) {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getTvs().size(); i++) {
                        if (TV.getDev().getDevId() == MyApplication.getWareData_Scene().getTvs().get(i).getDev().getDevId() &&
                                TV.getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getTvs().get(i).getDev().getCanCpuId())) {
                            for (int j = 0; j < dev_list.size(); j++) {
                                if (TV.getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        TV.getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    MyApplication.getWareData_Scene().getTvs().get(i).setbOnOff((byte) 0);
                                    TV.setbOnOff((byte) 0);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_red);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                } else if (dev_list.get(position).getType() == 2) {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getStbs().size(); i++) {
                        if (TVUP.getDev().getDevId() == MyApplication.getWareData_Scene().getStbs().get(i).getDev().getDevId() &&
                                TVUP.getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getStbs().get(i).getDev().getCanCpuId())) {
                            for (int j = 0; j < dev_list.size(); j++) {
                                if (TVUP.getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        TVUP.getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    MyApplication.getWareData_Scene().getStbs().get(i).setbOnOff((byte) 0);
                                    TVUP.setbOnOff((byte) 0);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_red);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                }
            }
        });


        //点击开按钮操作
        viewHolder.btn2.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (eventId < 2) {
                    ToastUtil.showToast(context, "全开、全关模式不可操作");
                    return;
                }
                if (dev_list.get(position).getType() == 0) {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getAirConds().size(); i++) {
                        if (AirCondDev.getDev().getDevId() == MyApplication.getWareData_Scene().getAirConds().get(i).getDev().getDevId() &&
                                AirCondDev.getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getAirConds().get(i).getDev().getCanCpuId())) {
                            for (int j = 0; j < dev_list.size(); j++) {
                                if (AirCondDev.getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        AirCondDev.getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    MyApplication.getWareData_Scene().getAirConds().get(i).setbOnOff((byte) 1);
                                    AirCondDev.setbOnOff((byte) 1);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                } else if (dev_list.get(position).getType() == 1) {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getTvs().size(); i++) {
                        if (TV.getDev().getDevId() == MyApplication.getWareData_Scene().getTvs().get(i).getDev().getDevId() &&
                                TV.getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getTvs().get(i).getDev().getCanCpuId())) {
                            for (int j = 0; j < dev_list.size(); j++) {
                                if (TV.getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        TV.getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    MyApplication.getWareData_Scene().getTvs().get(i).setbOnOff((byte) 1);
                                    TV.setbOnOff((byte) 1);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                } else if (dev_list.get(position).getType() == 2) {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getStbs().size(); i++) {
                        if (TVUP.getDev().getDevId() == MyApplication.getWareData_Scene().getStbs().get(i).getDev().getDevId() &&
                                TVUP.getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getStbs().get(i).getDev().getCanCpuId())) {
                            for (int j = 0; j < dev_list.size(); j++) {
                                if (TVUP.getDev().getDevId() == dev_list.get(position).getDevId() &&
                                        TVUP.getDev().getCanCpuId().equals(dev_list.get(position).getCanCpuId())) {
                                    MyApplication.getWareData_Scene().getStbs().get(i).setbOnOff((byte) 1);
                                    TVUP.setbOnOff((byte) 1);
                                    viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                                    viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                                }
                            }
                        }
                    }
                }
            }
        });
        return convertView;
    }
    static class ViewHolder {
        public Button btn1, btn2;
        public TextView text_appliance, text_temp;
    }
}
