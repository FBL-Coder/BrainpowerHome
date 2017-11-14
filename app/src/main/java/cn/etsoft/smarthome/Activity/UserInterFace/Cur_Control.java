package cn.etsoft.smarthome.Activity.UserInterFace;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/11/14.
 * 用户界面  窗帘
 */

public class Cur_Control extends Activity implements View.OnClickListener {

    /**
     * asdasdad
     */
    private TextView mControlGridViewItemName;
    private ImageView mControlGridViewItemIV;
    private ImageView mOpen;
    private ImageView mStop;
    private ImageView mClose;
    private ImageView air_control_close;

    private String mCancpuid;

    private int mDevid;

    private WareCurtain mCurtain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur_user_control);
        initView();
        initData();
    }

    private void initData() {
        mCancpuid = getIntent().getStringExtra("cancpuid");
        mDevid = getIntent().getIntExtra("devid", 0);
        for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
            if (mCancpuid.equals(MyApplication.getWareData().getCurtains().get(j).getDev().getCanCpuId())
                    && mDevid == MyApplication.getWareData().getCurtains().get(j).getDev().getDevId()) {
                mCurtain = MyApplication.getWareData().getCurtains().get(j);
            }
        }
        mControlGridViewItemName.setText(mCurtain.getDev().getDevName());
    }
    private void initView() {
        mControlGridViewItemName = (TextView) findViewById(R.id.Control_GridView_Item_Name);
        mControlGridViewItemIV = (ImageView) findViewById(R.id.Control_GridView_Item_IV);
        air_control_close = (ImageView) findViewById(R.id.air_control_close);
        mOpen = (ImageView) findViewById(R.id.open);
        mOpen.setOnClickListener(this);
        mStop = (ImageView) findViewById(R.id.stop);
        mStop.setOnClickListener(this);
        mClose = (ImageView) findViewById(R.id.close);
        mClose.setOnClickListener(this);
        air_control_close.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
        switch (v.getId()) {
            default:
                break;
            case R.id.open:
                SendDataUtil.controlDev(mCurtain.getDev(), UdpProPkt.E_CURT_CMD.e_curt_offOn.getValue());
                break;
            case R.id.stop:
                SendDataUtil.controlDev(mCurtain.getDev(), UdpProPkt.E_CURT_CMD.e_curt_stop.getValue());
                break;
            case R.id.close:
                SendDataUtil.controlDev(mCurtain.getDev(), UdpProPkt.E_CURT_CMD.e_curt_offOff.getValue());
                break;
            case R.id.air_control_close:
                finish();
                break;
        }
    }
}
