package cn.etsoft.smarthome.adapter_main;

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
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/11/25.
 */
public class ApplianceAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<WareDev> dev_list;

    public ApplianceAdapter(List<WareDev> list, Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
        dev_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType() == 0 || list.get(i).getType() == 1 || list.get(i).getType() == 2)
                dev_list.add(list.get(i));
        }
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
    List<WareAirCondDev> AicList;
    List<WareTv> TVList;
    WareTv tvs;
    WareSetBox boss;
    private int modelValue = 0, curValue = 0, cmdValue = 0;
    String str_Fixed;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.from(this.context).inflate(R.layout.listview_appliance, null, false);
            viewHolder.text_appliance = (TextView) convertView.findViewById(R.id.text_appliance);
            viewHolder.text_temp = (TextView) convertView.findViewById(R.id.text_temp);
            viewHolder.btn1 = (Button) convertView.findViewById(R.id.btn1);
            viewHolder.btn2 = (Button) convertView.findViewById(R.id.btn2);
            viewHolder.btn3 = (Button) convertView.findViewById(R.id.btn3);
            viewHolder.btn4 = (Button) convertView.findViewById(R.id.btn4);
            viewHolder.btn5 = (Button) convertView.findViewById(R.id.btn5);
            viewHolder.btn6 = (Button) convertView.findViewById(R.id.btn6);
            viewHolder.btn7 = (Button) convertView.findViewById(R.id.btn7);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (dev_list.size() < 1)
            return convertView;

        if (dev_list.get(position).getType() == 0) {
            AicList = MyApplication.getWareData().getAirConds();
            for (int i = 0; i < AicList.size(); i++) {
                if (dev_list.get(position).getDevId() == AicList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(AicList.get(i).getDev().getCanCpuId())) {
                    AirCondDev = AicList.get(i);
                }
            }
            viewHolder.text_appliance.setText(dev_list.get(position).getDevName());
            viewHolder.btn1.setVisibility(View.VISIBLE);
            viewHolder.btn2.setVisibility(View.VISIBLE);
            viewHolder.btn3.setVisibility(View.VISIBLE);
            viewHolder.btn4.setVisibility(View.VISIBLE);
            viewHolder.btn5.setVisibility(View.VISIBLE);
            viewHolder.btn6.setVisibility(View.VISIBLE);
            viewHolder.btn7.setVisibility(View.VISIBLE);

            viewHolder.btn1.setText("关");
            viewHolder.btn2.setText("开");
            viewHolder.btn4.setText("温度+");
            viewHolder.btn3.setText("温度-");
            viewHolder.btn5.setText("低风");
            viewHolder.btn6.setText("中风");
            viewHolder.btn7.setText("高风");

            if (AirCondDev.getbOnOff() == 0) {//是否是关机状态，是的话其他按钮背景为默认色；不是的话根据返回数据状态改变相应背景
                viewHolder.btn1.setBackgroundResource(R.drawable.rect_red);
                viewHolder.text_temp.setText("温度 : --°C");
                viewHolder.btn2.setBackgroundResource(R.drawable.rect_white1);
                viewHolder.btn3.setBackgroundResource(R.drawable.air_temp);
                viewHolder.btn4.setBackgroundResource(R.drawable.air_temp);
                viewHolder.btn7.setBackgroundResource(R.drawable.rect_white1);
                viewHolder.btn6.setBackgroundResource(R.drawable.rect_white1);
                viewHolder.btn5.setBackgroundResource(R.drawable.rect_white1);
            } else {
                viewHolder.btn1.setBackgroundResource(R.drawable.rect_white1);
                viewHolder.btn2.setBackgroundResource(R.drawable.rect_green2);
                viewHolder.btn3.setBackgroundResource(R.drawable.air_temp);
                viewHolder.btn4.setBackgroundResource(R.drawable.air_temp);
                viewHolder.text_temp.setText("温度 :" + AicList.get(position).getSelTemp() + "°C");

                if (AicList.get(position).getSelSpd()
                        == UdpProPkt.E_AIR_CMD.e_air_spdLow.getValue()) {
                    viewHolder.btn5.setBackgroundResource(R.drawable.rect_green2);
                    viewHolder.btn6.setBackgroundResource(R.drawable.rect_white1);
                    viewHolder.btn7.setBackgroundResource(R.drawable.rect_white1);
                } else if (AicList.get(position).getSelSpd()
                        == UdpProPkt.E_AIR_CMD.e_air_spdMid.getValue()) {
                    viewHolder.btn5.setBackgroundResource(R.drawable.rect_white1);
                    viewHolder.btn7.setBackgroundResource(R.drawable.rect_white1);
                    viewHolder.btn6.setBackgroundResource(R.drawable.rect_green2);
                } else if (AicList.get(position).getSelSpd()
                        == UdpProPkt.E_AIR_CMD.e_air_spdHigh.getValue()) {
                    viewHolder.btn7.setBackgroundResource(R.drawable.rect_green2);
                    viewHolder.btn6.setBackgroundResource(R.drawable.rect_white1);
                    viewHolder.btn5.setBackgroundResource(R.drawable.rect_white1);
                }
            }


        } else if (dev_list.get(position).getType() == 1) {

            List<WareTv> TvsList = MyApplication.getWareData().getTvs();
            for (int i = 0; i < TvsList.size(); i++) {
                if (dev_list.get(position).getDevId() == TvsList.get(i).getDev().getDevId() &&
                        dev_list.get(position).getCanCpuId().equals(TvsList.get(i).getDev().getCanCpuId())){
                    tvs = TvsList.get(i);
                }
            }
            viewHolder.text_temp.setVisibility(View.GONE);
            viewHolder.text_appliance.setText(dev_list.get(position).getDevName());
            viewHolder.btn1.setVisibility(View.VISIBLE);
            viewHolder.btn2.setVisibility(View.VISIBLE);
            viewHolder.btn3.setVisibility(View.VISIBLE);
            viewHolder.btn4.setVisibility(View.VISIBLE);
            viewHolder.btn5.setVisibility(View.VISIBLE);
            viewHolder.btn6.setVisibility(View.VISIBLE);
            viewHolder.btn7.setVisibility(View.GONE);

            viewHolder.btn1.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn2.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn3.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn4.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn5.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn6.setBackgroundResource(R.drawable.air_temp);

            viewHolder.btn1.setText("电源");
            viewHolder.btn2.setText("音量+");
            viewHolder.btn3.setText("音量-");
            viewHolder.btn4.setText("频道+");
            viewHolder.btn5.setText("频道-");
            viewHolder.btn6.setText("TV/AV");
        } else if (dev_list.get(position).getType() == 2) {
            viewHolder.text_temp.setVisibility(View.GONE);
            viewHolder.text_appliance.setText(dev_list.get(position).getDevName());
            viewHolder.btn1.setVisibility(View.VISIBLE);
            viewHolder.btn2.setVisibility(View.VISIBLE);
            viewHolder.btn3.setVisibility(View.VISIBLE);
            viewHolder.btn4.setVisibility(View.VISIBLE);
            viewHolder.btn5.setVisibility(View.VISIBLE);
            viewHolder.btn6.setVisibility(View.GONE);
            viewHolder.btn7.setVisibility(View.GONE);

            viewHolder.btn1.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn2.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn3.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn4.setBackgroundResource(R.drawable.air_temp);
            viewHolder.btn5.setBackgroundResource(R.drawable.air_temp);

            viewHolder.btn1.setText("电源");
            viewHolder.btn2.setText("音量+");
            viewHolder.btn3.setText("音量-");
            viewHolder.btn4.setText("频道+");
            viewHolder.btn5.setText("频道-");
        }


        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position);//获取当前点击发送的命令头
                int value = 0;
                if (dev_list.get(position).getType() == 0) {
                    if (IsAirOpen(position, AicList, false))
                        return;//判断空调是否关机，并提示用户；

                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                    value = (modelValue << 5) | cmdValue;

                } else if (dev_list.get(position).getType() == 1) {
                    value = UdpProPkt.E_TV_CMD.e_tv_offOn.getValue();
                } else if (dev_list.get(position).getType() == 2) {
                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_offOn.getValue();
                }

                str_Fixed = str_Fixed +
                        ",\"cmd\":" + value + "}";
                MyApplication.sendMsg(str_Fixed);
            }
        });
        viewHolder.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position);//获取当前点击发送的命令头
                int value = 0;
                if (dev_list.get(position).getType() == 0) {
                    if (!IsAirOpen(position, AicList, false))
                        return;//判断空调是否关机，并提示用户；
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                    value = (modelValue << 5) | cmdValue;
                } else if (dev_list.get(position).getType() == 1) {
                    value = UdpProPkt.E_TV_CMD.e_tv_numRt.getValue();
                } else if (dev_list.get(position).getType() == 2) {
                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_numRt.getValue();
                }
                str_Fixed = str_Fixed +
                        ",\"cmd\":" + value + "}";
                MyApplication.sendMsg(str_Fixed);

            }
        });

        viewHolder.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position);//获取当前点击发送的命令头
                int value = 1;
                if (dev_list.get(position).getType() == 0) {
                    //设置降温
                    if (IsAirOpen(position, AicList, true))
                        return;//判断空调是否关机，并提示用户；

                    for (int i = 0; i < AicList.size(); i++) {//获取当前空调的温度
                        if (dev_list.get(position).getDevId() == AicList.get(i).getDev().getDevId()
                                && dev_list.get(position).getType() == AicList.get(i).getDev().getType()) {
                            curValue = AicList.get(i).getSelTemp();
                        }
                    }
                    curValue--;
                    if (curValue < 14) {
                        curValue = 14;
                    } else {
                        cmdValue = getAirCmdTempstr(curValue);//获取温度命令
                    }
                    value = (modelValue << 5) | cmdValue;
                } else if (dev_list.get(position).getType() == 1) {
                    value = UdpProPkt.E_TV_CMD.e_tv_numLf.getValue();
                } else if (dev_list.get(position).getType() == 2) {
                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_numLf.getValue();
                }
                str_Fixed = str_Fixed +
                        ",\"cmd\":" + value + "}";
                MyApplication.sendMsg(str_Fixed);
            }
        });
        viewHolder.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position); //获取当前点击发送的命令头
                int value = 0;
                if (dev_list.get(position).getType() == 0) {

                    if (IsAirOpen(position, AicList, true))
                        return;//判断空调是否关机，并提示用户；
                    //设置升温
                    for (int i = 0; i < AicList.size(); i++) {//获取当前的温度
                        if (dev_list.get(position).getDevId() == AicList.get(i).getDev().getDevId()
                                && dev_list.get(position).getType() == AicList.get(i).getDev().getType()) {
                            curValue = AicList.get(i).getSelTemp();
                        }
                    }
                    curValue++;
                    if (curValue > 30) {
                        curValue = 30;
                    } else {
                        cmdValue = getAirCmdTempstr(curValue);//获取温度命令
                    }

                    value = (modelValue << 5) | cmdValue;

                } else if (dev_list.get(position).getType() == 1) {
                    value = UdpProPkt.E_TV_CMD.e_tv_numLf.getValue();
                } else if (dev_list.get(position).getType() == 2) {
                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_numLf.getValue();
                }

                str_Fixed = str_Fixed +
                        ",\"cmd\":" + value + "}";
                MyApplication.sendMsg(str_Fixed);
            }
        });
        viewHolder.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position); //获取当前点击发送的命令头
                int value = 0;
                if (dev_list.get(position).getType() == 0) {

                    if (IsAirOpen(position, AicList, true))
                        return;//判断空调是否关机，并提示用户；

                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_spdLow.getValue();
                    value = (modelValue << 5) | cmdValue;
                } else if (dev_list.get(position).getType() == 1) {
                    value = UdpProPkt.E_TV_CMD.e_tv_numDn.getValue();
                } else if (dev_list.get(position).getType() == 2) {
                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_numDn.getValue();
                }
                str_Fixed = str_Fixed +
                        ",\"cmd\":" + value + "}";
                MyApplication.sendMsg(str_Fixed);
            }
        });
        viewHolder.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position); //获取当前点击发送的命令头
                int value = 0;
                if (dev_list.get(position).getType() == 0) {
                    if (IsAirOpen(position, AicList, true))
                        return;//判断空调是否关机，并提示用户；

                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_spdMid.getValue();
                    value = (modelValue << 5) | cmdValue;

                } else if (dev_list.get(position).getType() == 1) {
                    value = UdpProPkt.E_TV_CMD.e_tv_numTvAv.getValue();
                } else if (dev_list.get(position).getType() == 2) {

                }
                str_Fixed = str_Fixed +
                        ",\"cmd\":" + value + "}";
                MyApplication.sendMsg(str_Fixed);
            }
        });
        viewHolder.btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                str_Fixed = getDevCmdstr(position); //获取当前点击发送的命令头

                if (dev_list.get(position).getType() == 0) {
                    if (IsAirOpen(position, AicList, true))
                        return;//判断空调是否关机，并提示用户；
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_spdHigh.getValue();

                    int value = (modelValue << 5) | cmdValue;
                    str_Fixed = str_Fixed +
                            ",\"cmd\":" + value + "}";
                    MyApplication.sendMsg(str_Fixed);
                } else if (dev_list.get(position).getType() == 1) {

                } else if (dev_list.get(position).getType() == 2) {

                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public Button btn1, btn2, btn3, btn4, btn5, btn6, btn7;
        public TextView text_appliance, text_temp;
    }

    /**
     * 判断空调状态（开/关）
     *
     * @param item  集合数引
     * @param list  集合
     * @param toast 是都提醒用户
     * @return
     */
    public boolean IsAirOpen(int item, List<WareAirCondDev> list, boolean toast) {
        for (int i = 0; i < list.size(); i++) {//获取当前的温度
            if (dev_list.get(item).getDevId() == list.get(i).getDev().getDevId()
                    && dev_list.get(item).getType() == list.get(i).getDev().getType()) {
                AirCondDev = list.get(i);
            }
        }
        if (AirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
            if (toast)
                ToastUtil.showToast(context, "请先开机，再操作");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 空调控制  头命令字符串
     *
     * @param i 集合树阴
     * @return
     */
    public String getDevCmdstr(int i) {
        String data = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"canCpuID\":\"" + dev_list.get(i).getCanCpuId() +
                "\",\"devType\":" + dev_list.get(i).getType() +
                ",\"devID\":" + dev_list.get(i).getDevId();
        return data;
    }

    /**
     * 空调控制 温度命令
     *
     * @param curValue 初始温度
     * @return
     */
    public int getAirCmdTempstr(int curValue) {
        switch (curValue) {
            case 14:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp14.getValue();
                break;
            case 15:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp15.getValue();
                break;
            case 16:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp16.getValue();
                break;
            case 17:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp17.getValue();
                break;
            case 18:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp18.getValue();
                break;
            case 19:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp19.getValue();
                break;
            case 20:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp20.getValue();
                break;
            case 21:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp21.getValue();
                break;
            case 22:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp22.getValue();
                break;
            case 23:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp23.getValue();
                break;
            case 24:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp24.getValue();
                break;
            case 25:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp25.getValue();
                break;
            case 26:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp26.getValue();
                break;
            case 27:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp27.getValue();
                break;
            case 28:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp28.getValue();
                break;
            case 29:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp29.getValue();
                break;
            case 30:
                cmdValue = UdpProPkt.E_AIR_CMD.e_air_temp30.getValue();
                break;
            default:
                break;
        }
        return cmdValue;
    }
}
