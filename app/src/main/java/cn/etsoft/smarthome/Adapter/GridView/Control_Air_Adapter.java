package cn.etsoft.smarthome.Adapter.GridView;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.lantouzi.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/26.
 * 控制页面——空调 适配器
 */

public class Control_Air_Adapter extends BaseAdapter {

    private Context mContext;
    private List<WareAirCondDev> mAirs;
    private List<String> temp_texts = new ArrayList<>();
    private List<String> spead_texts = new ArrayList<>();
    private int cmdValue, modelValue;

    public Control_Air_Adapter(Context context, List<WareAirCondDev> airs) {
        mAirs = airs;
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareAirCondDev> mAirs) {
        this.mAirs = mAirs;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAirs.size();
    }

    @Override
    public Object getItem(int position) {
        return mAirs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    int position_flag;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHoler = null;
        position_flag = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_control_airs_item, null);
            viewHoler = new ViewHolder(convertView);
            convertView.setTag(viewHoler);
        } else viewHoler = (ViewHolder) convertView.getTag();

        //初始化开关
        if (mAirs.get(position).getbOnOff() == 0) {
            viewHoler.mAirSwitch.setImageResource(R.drawable.switch_icon);
            viewHoler.mAirSwitchTv.setText("关闭");
        } else {
            viewHoler.mAirSwitchTv.setText("打开");
            viewHoler.mAirSwitch.setImageResource(R.drawable.switch_open);
        }

        final ViewHolder finalViewHoler = viewHoler;

        viewHoler.mAirName.setText(mAirs.get(position).getDev().getDevName());
        viewHoler.mAirNowTemp.setText(mAirs.get(position).getSelTemp() + "℃");
        for (int i = 16; i < 31; i++) {
            temp_texts.add(i + "");
        }

        spead_texts.add("低档");
        spead_texts.add("中档");
        spead_texts.add("高档");
        spead_texts.add("自动");
        if (mAirs.get(position).getSelTemp() < 16 || mAirs.get(position).getSelTemp() > 30)
            mAirs.get(position).setSelTemp(16);
        viewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp() - 16);
        viewHoler.mHorizontalSelectTemp.setItems(temp_texts);

        viewHoler.mHorizontalSelectTemp.setAdditionCenterMark("℃");
        viewHoler.mHorizontalSelectTemp.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int i) {
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int i) {
                ToastUtil.showText(temp_texts.get(i));
                SendDataUtil.controlDev(mAirs.get(position).getDev(), getAirCmdTempstr(Integer.parseInt(temp_texts.get(i))));
            }
        });
        viewHoler.mHorizontalSelectSpead.setItems(spead_texts);
        viewHoler.mHorizontalSelectSpead.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int i) {
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int i) {
                ToastUtil.showText(spead_texts.get(i));
                SendDataUtil.controlDev(mAirs.get(position).getDev(), i + 2);
            }
        });

        if (mAirs.get(position).getSelMode() == 1) {
            viewHoler.mAirTocool.setTextColor(Color.WHITE);
            viewHoler.mAirToheat.setTextColor(Color.GREEN);
            viewHoler.mAirXeransis.setTextColor(Color.WHITE);
            viewHoler.mAirSwingFlap.setTextColor(Color.WHITE);
        } else if (mAirs.get(position).getSelMode() == 2) {
            viewHoler.mAirTocool.setTextColor(Color.GREEN);
            viewHoler.mAirToheat.setTextColor(Color.WHITE);
            viewHoler.mAirXeransis.setTextColor(Color.WHITE);
            viewHoler.mAirSwingFlap.setTextColor(Color.WHITE);
        } else if (mAirs.get(position).getSelMode() == 3) {
            viewHoler.mAirTocool.setTextColor(Color.WHITE);
            viewHoler.mAirToheat.setTextColor(Color.WHITE);
            viewHoler.mAirXeransis.setTextColor(Color.GREEN);
            viewHoler.mAirSwingFlap.setTextColor(Color.WHITE);
        } else if (mAirs.get(position).getSelMode() == 4) {
            viewHoler.mAirTocool.setTextColor(Color.WHITE);
            viewHoler.mAirToheat.setTextColor(Color.WHITE);
            viewHoler.mAirXeransis.setTextColor(Color.WHITE);
            viewHoler.mAirSwingFlap.setTextColor(Color.GREEN);
        }
        SwitchClick(position, viewHoler, finalViewHoler);

        TempClick(position, viewHoler, finalViewHoler);

        SpeadClick(position, viewHoler, finalViewHoler);

        viewHoler.mAirMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mAirs.get(position).getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                switch (checkedId) {
                    case R.id.air_tocool://制冷
                        modelValue = UdpProPkt.E_AIR_MODE.e_air_cool.getValue();
                        break;
                    case R.id.air_toheat://制热
                        modelValue = UdpProPkt.E_AIR_MODE.e_air_hot.getValue();
                        break;
                    case R.id.air_xeransis://除湿
                        modelValue = UdpProPkt.E_AIR_MODE.e_air_dry.getValue();
                        break;
                    case R.id.air_swing_flap://扫风
                        modelValue = UdpProPkt.E_AIR_MODE.e_air_wind.getValue();
                        break;
                }
                int value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
            }
        });

        return convertView;
    }

    //风速事件
    private void SpeadClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirTospeadSam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelSpd() < 2) {
                    ToastUtil.showText("不能再低了");
                    return;
                }
                cmdValue = mAirs.get(position).getSelSpd() - 1;
                int value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
                finalViewHoler.mHorizontalSelectSpead.selectIndex(mAirs.get(position).getSelSpd() - 1);
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() + 100, v.getY(), 0);
                finalViewHoler.mHorizontalSelectSpead.onSingleTapUp(event);
            }
        });
        viewHoler.mAirTospeadBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelSpd() > 5) {
                    ToastUtil.showText("不能再高了");
                    return;
                }
                cmdValue = mAirs.get(position).getSelSpd() + 1;
                int value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
                finalViewHoler.mHorizontalSelectSpead.selectIndex(mAirs.get(position).getSelSpd() + 1);
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() - 150, v.getY(), 0);
                finalViewHoler.mHorizontalSelectSpead.onSingleTapUp(event);
            }
        });
    }

    //温度事件
    private void TempClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirTempDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelTemp() < 16) {
                    ToastUtil.showText("不能再低了");
                    return;
                }
                if (mAirs.get(position).getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                cmdValue = mAirs.get(position).getSelTemp() - 1;
                int value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
                finalViewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp());
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() + 100, v.getY(), 0);
                finalViewHoler.mHorizontalSelectTemp.onSingleTapUp(event);
            }
        });

        viewHoler.mAirTempAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAirs.get(position).getSelTemp() > 30) {
                    ToastUtil.showText("不能再高了");
                    return;
                }
                if (mAirs.get(position).getbOnOff() == 0) {
                    ToastUtil.showText("请先打开空调");
                    return;
                }
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                cmdValue = mAirs.get(position).getSelTemp() + 1;
                int value = (modelValue << 5) | cmdValue;
                SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
                finalViewHoler.mHorizontalSelectTemp.selectIndex(mAirs.get(position).getSelTemp());
                MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis()
                        , SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, v.getX() - 100, v.getY(), 0);
                finalViewHoler.mHorizontalSelectTemp.onSingleTapUp(event);
            }
        });
    }

    //开关
    private void SwitchClick(final int position, ViewHolder viewHoler, final ViewHolder finalViewHoler) {
        viewHoler.mAirSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
                if (mAirs.get(position).getbOnOff() == 0) {
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();
                    int value = (modelValue << 5) | cmdValue;
                    SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
                } else {
                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();
                    int value = (modelValue << 5) | cmdValue;
                    SendDataUtil.controlDev(mAirs.get(position).getDev(), value);
                }
            }
        });
    }

    public int getAirCmdTempstr(int curValue) {
        int cmdValue = 0;
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

    public static class ViewHolder {
        public View rootView;
        public TextView mAirName;
        public TextView mAirNowTemp;
        public ImageView mAirNowSpead;
        public ImageView mAirTempDown;
        public ImageView mAirSwitch;
        public WheelView mHorizontalSelectTemp;
        public ImageView mAirTempAdd;
        public ImageView mAirTospeadSam;
        public WheelView mHorizontalSelectSpead;
        public ImageView mAirTospeadBig;
        public RadioButton mAirTocool;
        public RadioButton mAirToheat;
        public RadioButton mAirXeransis;
        public RadioButton mAirSwingFlap;
        private RadioGroup mAirMode;
        private TextView mAirSwitchTv;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mAirName = (TextView) rootView.findViewById(R.id.air_name);
            this.mAirNowTemp = (TextView) rootView.findViewById(R.id.air_now_temp);
            this.mAirNowSpead = (ImageView) rootView.findViewById(R.id.air_now_spead);
            this.mAirTempDown = (ImageView) rootView.findViewById(R.id.air_temp_down);
            this.mAirSwitch = (ImageView) rootView.findViewById(R.id.air_switch);
            this.mHorizontalSelectTemp = (WheelView) rootView.findViewById(R.id.HorizontalSelect_temp);
            this.mAirTempAdd = (ImageView) rootView.findViewById(R.id.air_temp_add);
            this.mAirTospeadSam = (ImageView) rootView.findViewById(R.id.air_tospead_sam);
            this.mHorizontalSelectSpead = (WheelView) rootView.findViewById(R.id.HorizontalSelect_spead);
            this.mAirTospeadBig = (ImageView) rootView.findViewById(R.id.air_tospead_big);
            this.mAirTocool = (RadioButton) rootView.findViewById(R.id.air_tocool);
            this.mAirToheat = (RadioButton) rootView.findViewById(R.id.air_toheat);
            this.mAirXeransis = (RadioButton) rootView.findViewById(R.id.air_xeransis);
            this.mAirSwingFlap = (RadioButton) rootView.findViewById(R.id.air_swing_flap);
            this.mAirMode = (RadioGroup) rootView.findViewById(R.id.air_mode);
            this.mAirSwitchTv = (TextView) rootView.findViewById(R.id.air_switch_tv);
        }

    }
}
