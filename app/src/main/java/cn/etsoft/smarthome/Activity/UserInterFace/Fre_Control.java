package cn.etsoft.smarthome.Activity.UserInterFace;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/11/14.
 * 用户界面  新风
 */

public class Fre_Control extends Activity implements View.OnClickListener {


    private String mCancpuid;

    private int mDevid;

    private WareFreshAir mFreshAir;
    private ImageView mItemIv, fie_Switch;
    private TextView mItemTv;
    private ImageView mLowFreshair;
    private ImageView mMinFreshair;
    private ImageView mHigFreshair;
    private ImageView mAirControlClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fie_control);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 4 || datType == 35) {
                    initData();
                }
            }
        });
        super.onResume();
    }

    private void initData() {
        mCancpuid = getIntent().getStringExtra("cancpuid");
        mDevid = getIntent().getIntExtra("devid", 0);
        for (int j = 0; j < MyApplication.getWareData().getFreshAirs().size(); j++) {
            if (mCancpuid.equals(MyApplication.getWareData().getFreshAirs().get(j).getDev().getCanCpuId())
                    && mDevid == MyApplication.getWareData().getFreshAirs().get(j).getDev().getDevId()) {
                mFreshAir = MyApplication.getWareData().getFreshAirs().get(j);
            }
        }
        mItemTv.setText(mFreshAir.getDev().getDevName());

        if (mFreshAir.getbOnOff() == 0) {
            mItemIv.setImageResource(R.drawable.freshair_close);
            fie_Switch.setImageResource(R.drawable.show_off);
        } else {
            fie_Switch.setImageResource(R.drawable.show_on);
            mItemIv.setImageResource(R.drawable.freshair_open);
        }
        if (mFreshAir.getSpdSel() == 2) {
            mLowFreshair.setImageResource(R.drawable.freshair_low_down);
            mMinFreshair.setImageResource(R.drawable.freshair_min);
            mHigFreshair.setImageResource(R.drawable.freshair_hig);
        } else if (mFreshAir.getSpdSel() == 3) {
            mLowFreshair.setImageResource(R.drawable.freshair_low);
            mMinFreshair.setImageResource(R.drawable.freshair_min_dwom);
            mHigFreshair.setImageResource(R.drawable.freshair_hig);
        } else if (mFreshAir.getSpdSel() == 4) {
            mLowFreshair.setImageResource(R.drawable.freshair_low);
            mMinFreshair.setImageResource(R.drawable.freshair_min);
            mHigFreshair.setImageResource(R.drawable.freshair_hig_down);
        } else {
            mLowFreshair.setImageResource(R.drawable.freshair_low);
            mMinFreshair.setImageResource(R.drawable.freshair_min);
            mHigFreshair.setImageResource(R.drawable.freshair_hig);
        }
    }

    private void initView() {
        mItemIv = (ImageView) findViewById(R.id.item_iv);
        mItemTv = (TextView) findViewById(R.id.item_tv);
        mLowFreshair = (ImageView) findViewById(R.id.low_freshair);
        fie_Switch = (ImageView) findViewById(R.id.fie_Switch);
        mLowFreshair.setOnClickListener(this);
        fie_Switch.setOnClickListener(this);
        mMinFreshair = (ImageView) findViewById(R.id.min_freshair);
        mMinFreshair.setOnClickListener(this);
        mHigFreshair = (ImageView) findViewById(R.id.hig_freshair);
        mHigFreshair.setOnClickListener(this);
        mAirControlClose = (ImageView) findViewById(R.id.air_control_close);
        mAirControlClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
        switch (v.getId()) {
            case R.id.fie_Switch:
                if (mFreshAir.getbOnOff() == 1) {
                    SendDataUtil.controlDev(mFreshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_close.getValue());
                } else {
                    SendDataUtil.controlDev(mFreshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_open.getValue());
                }
                break;
            case R.id.low_freshair:
                if (mFreshAir.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开设备");
                    return;
                }
                SendDataUtil.controlDev(mFreshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_spd_low.getValue());
                break;
            case R.id.min_freshair:
                if (mFreshAir.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开设备");
                    return;
                }
                SendDataUtil.controlDev(mFreshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_spd_mid.getValue());
                break;
            case R.id.hig_freshair:
                if (mFreshAir.getbOnOff() == 0) {
                    ToastUtil.showText("请先打开设备");
                    return;
                }
                SendDataUtil.controlDev(mFreshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_spd_high.getValue());
                break;
            case R.id.air_control_close:
                finish();
                break;
        }
    }
}
