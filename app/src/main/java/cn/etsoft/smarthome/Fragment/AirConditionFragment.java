package cn.etsoft.smarthome.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class AirConditionFragment extends Fragment implements View.OnClickListener {
    private TextView name, name1, temperature, temperature1, state, wind,
            choose, warming, cooling, windSweeper, refrigeration, heating, high, middle, low, action_mode, action_wind;
    private WareAirCondDev wareAirCondDev;
    byte[] devBuff;
    private static final int MSG_REFRSH_INFO = 1000;
    private int modelValue = 0, curValue = 0, cmdValue = 0;
    private boolean IsCanClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aircondition, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    private void initEvent() {

        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData() {
                upData();
            }
        });
    }

    private void upData() {
        if (MyApplication.getWareData().getAirConds().size() == 0) {
            return;
        }
        wareAirCondDev = MyApplication.getWareData().getAirConds().get(0);
        curValue = MyApplication.getWareData().getAirConds().get(0).getSelTemp();
        name.setText("空调名称");
        name1.setText(MyApplication.getWareData().getAirConds().get(0).getDev().getDevName());
        temperature.setText("当前温度 :" + 10 + "℃");
        temperature1.setText("设置温度 :" + MyApplication.getWareData().getAirConds().get(0).getSelTemp() + "℃");

        if (MyApplication.getWareData().getAirConds().get(0).getbOnOff() == 0) {
            state.setText("空调状态 : 关闭");
        } else {
            state.setText("空调状态 : 打开");
        }
        if (MyApplication.getWareData().getAirConds().get(0).getSelMode() == 1) {
            action_mode.setText("模式 : 制热");
        } else {
            action_mode.setText("模式 : 制冷");
        }

        if (MyApplication.getWareData().getAirConds().get(0).getSelDirect() == 28) {
            action_wind.setText("扫风 : 打开");
        } else {
            action_wind.setText("扫风 : 关闭");
        }
        Log.i("WelCome", MyApplication.getWareData().getAirConds().get(0).getSelSpd() + "");
        if (MyApplication.getWareData().getAirConds().get(0).getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdLow.getValue()) {
            wind.setText("风速 : 低风");
        } else if (MyApplication.getWareData().getAirConds().get(0).getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdMid.getValue()) {
            wind.setText("风速 : 中风");
        } else if (MyApplication.getWareData().getAirConds().get(0).getSelSpd()
                == UdpProPkt.E_AIR_CMD.e_air_spdHigh.getValue()) {
            wind.setText("风速 : 高风");
        }
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        name = (TextView) view.findViewById(R.id.condition_name);
        name1 = (TextView) view.findViewById(R.id.condition_name1);
        temperature = (TextView) view.findViewById(R.id.condition_temperature);
        temperature1 = (TextView) view.findViewById(R.id.condition_temperature1);
        state = (TextView) view.findViewById(R.id.condition_state);
        wind = (TextView) view.findViewById(R.id.condition_wind);
        choose = (TextView) view.findViewById(R.id.condition_switch);
        warming = (TextView) view.findViewById(R.id.condition_warming);
        cooling = (TextView) view.findViewById(R.id.condition_cooling);
        windSweeper = (TextView) view.findViewById(R.id.condition_windsweeper);
        refrigeration = (TextView) view.findViewById(R.id.condition_refrigeration);
        heating = (TextView) view.findViewById(R.id.condition_heating);
        high = (TextView) view.findViewById(R.id.condition_high);
        middle = (TextView) view.findViewById(R.id.condition_middle);
        low = (TextView) view.findViewById(R.id.condition_low);
        action_mode = (TextView) view.findViewById(R.id.action_mode);
        action_wind = (TextView) view.findViewById(R.id.action_wind);
        if (MyApplication.getWareData() != null) {
            if (MyApplication.getWareData().getAirConds() != null && MyApplication.getWareData().getAirConds().size() > 0) {
                upData();
                initEvent();
                IsCanClick = true;
            }
        } else {
            Toast.makeText(getActivity(), "没有找到可控空调", Toast.LENGTH_SHORT).show();
        }
        choose.setOnClickListener(this);
        warming.setOnClickListener(this);
        cooling.setOnClickListener(this);
        windSweeper.setOnClickListener(this);
        refrigeration.setOnClickListener(this);
        heating.setOnClickListener(this);
        high.setOnClickListener(this);
        middle.setOnClickListener(this);
        low.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        {
//            "devUnitID": "37ffdb05424e323416702443",
//                "datType": 4,
//                "subType1": 0,
//                "subType2": 0,
//                "canCpuID": "31ffdf054257313827502543",
//                "devType": 3,
//                "devID": 6,
//                "cmd": 1
//        }

        if (IsCanClick) {
            String str_Fixed = "{\"devUnitID\":\"37ffdb05424e323416702443\"" +
                    ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                    ",\"subType1\":0" +
                    ",\"subType2\":0" +
                    ",\"canCpuID\":\"" + MyApplication.getWareData().getAirConds().get(0).getDev().getCanCpuId() +
                    "\",\"devType\":" + MyApplication.getWareData().getAirConds().get(0).getDev().getType() +
                    ",\"devID\":" + MyApplication.getWareData().getAirConds().get(0).getDev().getDevId();
            switch (v.getId()) {
                case R.id.condition_switch:
                    if (wareAirCondDev.getbOnOff() == 0) {
                        cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                    } else {
                        cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                    }
                    break;
                case R.id.condition_warming:
                    //设置升温
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    curValue++;
                    if (curValue > 30) {
                        curValue = 30;
                    } else {
                        temperature1.setText("设置温度 :" + curValue + "℃");
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
                    }
                    break;
                case R.id.condition_cooling:
                    //设置降温
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    curValue--;
                    if (curValue < 14) {
                        curValue = 14;
                    } else {
                        temperature1.setText("设置温度 :" + curValue + "℃");
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
                    }
                    break;

                case R.id.condition_windsweeper:
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_drctLfRt1.getValue();
                    break;
                case R.id.condition_refrigeration:
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    modelValue = UdpProPkt.E_AIR_MODE.e_air_cool.getValue();
                    break;
                case R.id.condition_heating:
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    modelValue = UdpProPkt.E_AIR_MODE.e_air_hot.getValue();
                    break;
                case R.id.condition_high:
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_spdHigh.getValue();
                    break;
                case R.id.condition_middle:
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_spdMid.getValue();
                    break;
                case R.id.condition_low:
                    if (wareAirCondDev.getbOnOff() == UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue()) {
                        Toast.makeText(getActivity(), "请先开机，再操作", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_spdLow.getValue();
                    break;
            }
            int value = (modelValue << 5) | cmdValue;

            str_Fixed = str_Fixed +
                    ",\"cmd\":" + value + "}";
            CommonUtils.sendMsg(str_Fixed);
        }
    }
}
