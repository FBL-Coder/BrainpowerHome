package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Domain.UserBean;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.Domain.WareTv;

/**
 * Created by Say GoBay on 2016/9/1.
 * 用户界面UserActivity,UserFragment的适配器
 */
public class GridViewAdapter_User extends BaseAdapter {
    private UserBean bean;
    private UserBean MyData;
    private Context context;

    public GridViewAdapter_User(UserBean bean, Context context) {
        super();
        this.bean = bean;
        this.context = context;
        initData(bean);
    }

    private void initData(UserBean bean) {
        MyData = new UserBean();

        for (int i = 0; i < bean.getUser_bean().size(); i++) {
            if (bean.getUser_bean().get(i).isIsDev() == 1) {
                boolean isDevExist = false;
                for (int j = 0; j < MyApplication.getWareData().getDevs().size(); j++) {
                    WareDev dev = MyApplication.getWareData().getDevs().get(j);
                    if (bean.getUser_bean().get(i).getDevID() == dev.getDevId()
                            && bean.getUser_bean().get(i).getDevType() == dev.getType()
                            && bean.getUser_bean().get(i).getCanCpuID().equals(
                            dev.getCanCpuId()))
                        isDevExist = true;
                }
                if (!isDevExist) {
                    bean.getUser_bean().remove(i);
                    i--;
                }
            } else {
                boolean isSceneExist = false;
                for (int j = 0; j < MyApplication.getWareData().getSceneEvents().size(); j++) {
                    WareSceneEvent event = MyApplication.getWareData().getSceneEvents().get(j);
                    if (bean.getUser_bean().get(i).getEventId() == event.getEventId())
                        isSceneExist = true;
                }
                if (!isSceneExist) {
                    bean.getUser_bean().remove(i);
                    i--;
                }
            }
        }


        List<UserBean.UserBeanBean> beanBean = new ArrayList<>();
        MyData.setUser_bean(beanBean);
        MyData.getUser_bean().addAll(bean.getUser_bean());
        MyData.getUser_bean().add(new UserBean.UserBeanBean());
    }

    public void notifyDataSetChanged(UserBean bean) {
        super.notifyDataSetChanged();
        this.bean = bean;
        initData(bean);

    }

    @Override
    public int getCount() {
        return MyData.getUser_bean().size();
    }

    @Override
    public Object getItem(int position) {
        return MyData.getUser_bean().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item_user, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.equip_name);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.equip_type);
            viewHolder.state = (TextView) convertView.findViewById(R.id.equip_style);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position < bean.getUser_bean().size()) {
            UserBean.UserBeanBean beanBean = MyData.getUser_bean().get(position);
            if (beanBean.isIsDev() == 1) {
                int type_dev = beanBean.getDevType();
                if (type_dev == 0) {
                    for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                        WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                        if (beanBean.getCanCpuID().equals(AirCondDev.getDev().getCanCpuId())
                                && beanBean.getDevID() == AirCondDev.getDev().getDevId()) {
                            viewHolder.name.setText(AirCondDev.getDev().getDevName());
                            if (AirCondDev.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.kt_dev_item_close);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.kt_dev_item_open);
                                viewHolder.state.setText("开");
                            }

                        }
                    }
                } else if (type_dev == 1) {
                    for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                        WareTv tv = MyApplication.getWareData().getTvs().get(j);
                        if (beanBean.getCanCpuID().equals(tv.getDev().getCanCpuId())
                                && beanBean.getDevID() == tv.getDev().getDevId()) {
                            viewHolder.name.setText(tv.getDev().getDevName());
                            if (tv.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.tv_sam_icon);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.tv_sam_icon);
                                viewHolder.state.setText("开");
                            }
                        }
                    }
                } else if (type_dev == 2) {
                    for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                        WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                        if (beanBean.getCanCpuID().equals(box.getDev().getCanCpuId())
                                && beanBean.getDevID() == box.getDev().getDevId()) {
                            viewHolder.name.setText(box.getDev().getDevName());
                            if (box.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.tvup_icon);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.tvup_icon);
                                viewHolder.state.setText("开");
                            }
                        }
                    }
                } else if (type_dev == 3) {
                    for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                        WareLight Light = MyApplication.getWareData().getLights().get(j);
                        if (beanBean.getCanCpuID().equals(Light.getDev().getCanCpuId())
                                && beanBean.getDevID() == Light.getDev().getDevId()) {
                            viewHolder.name.setText(Light.getDev().getDevName());
                            if (Light.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.light_close);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.light_open);
                                viewHolder.state.setText("开");
                            }
                        }
                    }
                } else if (type_dev == 4) {
                    for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                        WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                        if (beanBean.getCanCpuID().equals(Curtain.getDev().getCanCpuId())
                                && beanBean.getDevID() == Curtain.getDev().getDevId()) {
                            viewHolder.name.setText(Curtain.getDev().getDevName());
                            if (Curtain.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.cl_dev_item_close);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.cl_dev_item_open);
                                viewHolder.state.setText("开");
                            }
                        }
                    }
                } else if (type_dev == 7) {
                    for (int j = 0; j < MyApplication.getWareData().getFreshAirs().size(); j++) {
                        WareFreshAir freshAir = MyApplication.getWareData().getFreshAirs().get(j);
                        if (beanBean.getCanCpuID().equals(freshAir.getDev().getCanCpuId())
                                && beanBean.getDevID() == freshAir.getDev().getDevId()) {
                            viewHolder.name.setText(freshAir.getDev().getDevName());
                            if (freshAir.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.freshair_close);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.freshair_open);
                                viewHolder.state.setText("开");
                            }
                        }
                    }
                } else if (type_dev == 9) {
                    for (int j = 0; j < MyApplication.getWareData().getFloorHeat().size(); j++) {
                        WareFloorHeat floorHeat = MyApplication.getWareData().getFloorHeat().get(j);
                        if (beanBean.getCanCpuID().equals(floorHeat.getDev().getCanCpuId())
                                && beanBean.getDevID() == floorHeat.getDev().getDevId()) {
                            viewHolder.name.setText(floorHeat.getDev().getDevName());
                            if (floorHeat.getbOnOff() == 0) {
                                viewHolder.type.setImageResource(R.drawable.floorheat_close);
                                viewHolder.state.setText("关");
                            } else {
                                viewHolder.type.setImageResource(R.drawable.floorheat_open);
                                viewHolder.state.setText("开");
                            }
                        }
                    }
                }
            } else {
                WareSceneEvent event = null;
                for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                    if (beanBean.getEventId() == MyApplication.getWareData().getSceneEvents().get(i).getEventId())
                        event = MyApplication.getWareData().getSceneEvents().get(i);
                }
                if (event != null) {
                    if (MyApplication.getWareData().getSceneEvents().size() > 0)
                        viewHolder.name.setText(event.getSceneName());
                    viewHolder.type.setImageResource(R.drawable.logo);
                    viewHolder.state.setText("点击执行");
                }
            }
        } else {
            viewHolder.name.setText("");
            viewHolder.state.setText("");
            viewHolder.type.setImageResource(R.drawable.btn_big_add);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView name, state;
        private ImageView type;
    }
}
