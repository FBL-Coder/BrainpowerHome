package cn.etsoft.smarthome.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class CurtainFragment extends Fragment implements View.OnClickListener {
    private TextView open, half, close;
    private WareCurtain wareCurtain;
    byte[] devBuff;
    private boolean IsCanClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_curtain, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        open = (TextView) view.findViewById(R.id.curtain_open);
        half = (TextView) view.findViewById(R.id.curtain_half);
        close = (TextView) view.findViewById(R.id.curtain_close);
        if (MyApplication.getWareData().getCurtains() != null && MyApplication.getWareData().getCurtains().size() > 1) {
            IsCanClick = true;
        } else {
            Toast.makeText(getActivity(), "没有找到可控制窗帘", Toast.LENGTH_SHORT).show();
        }
        open.setOnClickListener(this);
        half.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (IsCanClick) {
            switch (v.getId()) {
                case R.id.curtain_open:
                    break;
                case R.id.curtain_half:
                    break;
                case R.id.curtain_close:
                    break;
            }
        }
    }

}
