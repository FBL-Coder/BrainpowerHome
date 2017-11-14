package cn.etsoft.smarthome.Activity.UserInterFace;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.io.UnsupportedEncodingException;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/11/14.
 * 用户界面  地暖
 */

public class Flo_Control extends Activity implements View.OnClickListener {

    private String mCancpuid;
    private int mDevid;

    private WareFloorHeat mFloorHeat;
    private TextView mControlGridViewItemName;
    private ImageView mFieSwitch;
    private ImageView mControlGridViewItemIV;
    private TextView mControlGridViewItemTemp;
    private ImageView mFloorheatTempAdd;
    private TextView mFloorheatTempSet;
    private ImageView mFloorheatTempDown;
    private ImageView mAirControlClose;
    private int floorHeatTemp = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_flo_control);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 4 || datType == 35 ||datType == 6) {
                    initData();
                }
            }
        });
        super.onResume();
    }

    private void initData() {
        mCancpuid = getIntent().getStringExtra("cancpuid");
        mDevid = getIntent().getIntExtra("devid", 0);
        for (int j = 0; j < MyApplication.getWareData().getFloorHeat().size(); j++) {
            if (mCancpuid.equals(MyApplication.getWareData().getFloorHeat().get(j).getDev().getCanCpuId())
                    && mDevid == MyApplication.getWareData().getFloorHeat().get(j).getDev().getDevId()) {
                mFloorHeat = MyApplication.getWareData().getFloorHeat().get(j);
            }
        }
        floorHeatTemp = mFloorHeat.getTempset();
        mFloorheatTempSet.setText(mFloorHeat.getTempset() + "℃");

        if (mFloorHeat.getbOnOff() == 0) {
            mFieSwitch.setImageResource(R.drawable.show_off);
            mControlGridViewItemIV.setImageResource(R.drawable.floorheat_close);
        } else {
            mFieSwitch.setImageResource(R.drawable.show_on);
            mControlGridViewItemIV.setImageResource(R.drawable.floorheat_open);
        }

        mControlGridViewItemTemp.setText("当前温度 :\n" + mFloorHeat.getTempget() + "℃");
        mControlGridViewItemName.setText(mFloorHeat.getDev().getDevName());
        mFloorheatTempSet.setText(mFloorHeat.getTempset() + "℃");
    }

    private void initView() {
        mControlGridViewItemName = (TextView) findViewById(R.id.Control_GridView_Item_Name);
        mFieSwitch = (ImageView) findViewById(R.id.fie_Switch);
        mFieSwitch.setOnClickListener(this);
        mControlGridViewItemIV = (ImageView) findViewById(R.id.Control_GridView_Item_IV);
        mControlGridViewItemTemp = (TextView) findViewById(R.id.Control_GridView_Item_Temp);
        mFloorheatTempAdd = (ImageView) findViewById(R.id.floorheat_temp_add);
        mFloorheatTempAdd.setOnClickListener(this);
        mFloorheatTempSet = (TextView) findViewById(R.id.floorheat_temp_set);
        mFloorheatTempDown = (ImageView) findViewById(R.id.floorheat_temp_down);
        mFloorheatTempDown.setOnClickListener(this);
        mAirControlClose = (ImageView) findViewById(R.id.air_control_close);
        mAirControlClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String DevName = "";
        String RoomName = "";
        String chn_str;
        MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
        switch (v.getId()) {
            case R.id.fie_Switch:
                if (mFloorHeat.getbOnOff() == 1)
                    SendDataUtil.controlDev(mFloorHeat.getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_close.getValue());
                else
                    SendDataUtil.controlDev(mFloorHeat.getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_open.getValue());
                break;
            case R.id.floorheat_temp_add:
                if (mFloorHeat.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开设备");
                    return;
                }

                try {
                    DevName = CommonUtils.bytesToHexString(mFloorHeat.getDev().getDevName().getBytes("GB2312"));
                } catch (UnsupportedEncodingException e) {
                    DevName = "";
                }
                try {
                    RoomName = CommonUtils.bytesToHexString(mFloorHeat.getDev().getRoomName().getBytes("GB2312"));
                } catch (UnsupportedEncodingException e) {
                    RoomName = "";
                }
                chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                        "\"datType\":" + 6 + "," +
                        "\"subType1\":0," +
                        "\"subType2\":0," +
                        "\"canCpuID\":\"" + mFloorHeat.getDev().getCanCpuId() + "\"," +
                        "\"devType\":" + mFloorHeat.getDev().getType() + "," +
                        "\"devID\":" + mFloorHeat.getDev().getDevId() + "," +
                        "\"bOnOff\":" + mFloorHeat.getbOnOff() + "," +
                        "\"tempget\":" + mFloorHeat.getTempget() + "," +
                        "\"devName\":" + "\"" + DevName + "\"," +
                        "\"roomName\":" + "\"" + RoomName + "\"," +
                        "\"tempset\":" + ++floorHeatTemp + "," +
                        "\"autoRun\":" + 0 + "," +
                        "\"cmd\":" + 1 + "," +
                        "\"powChn\":" + mFloorHeat.getDev().getPowChn() + "}";
                MyApplication.mApplication.getUdpServer().send(chn_str, 6);
                break;
            case R.id.floorheat_temp_down:
                if (mFloorHeat.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开设备");
                    return;
                }
                try {
                    DevName = CommonUtils.bytesToHexString(mFloorHeat.getDev().getDevName().getBytes("GB2312"));
                } catch (UnsupportedEncodingException e) {
                    DevName = "";
                }
                try {
                    RoomName = CommonUtils.bytesToHexString(mFloorHeat.getDev().getRoomName().getBytes("GB2312"));
                } catch (UnsupportedEncodingException e) {
                    RoomName = "";
                }
                chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                        "\"datType\":" + 6 + "," +
                        "\"subType1\":0," +
                        "\"subType2\":0," +
                        "\"canCpuID\":\"" + mFloorHeat.getDev().getCanCpuId() + "\"," +
                        "\"devType\":" + mFloorHeat.getDev().getType() + "," +
                        "\"devID\":" + mFloorHeat.getDev().getDevId() + "," +
                        "\"bOnOff\":" + mFloorHeat.getbOnOff() + "," +
                        "\"tempget\":" + mFloorHeat.getTempget() + "," +
                        "\"devName\":" + "\"" + DevName + "\"," +
                        "\"roomName\":" + "\"" + RoomName + "\"," +
                        "\"tempset\":" + --floorHeatTemp + "," +
                        "\"autoRun\":" + mFloorHeat.getAutoRun() + "," +
                        "\"cmd\":" + 1 + "," +
                        "\"powChn\":" + mFloorHeat.getDev().getPowChn() + "}";
                MyApplication.mApplication.getUdpServer().send(chn_str, 6);
                break;
            case R.id.air_control_close:
                finish();
                break;
        }
    }
}
