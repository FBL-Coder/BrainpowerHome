package cn.etsoft.smarthome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/11/25.
 * 系统信息
 */
public class Setting_SystemFragment extends Fragment {
    private TextView app,engine,firmware,hardware;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system,container,false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        app = (TextView) view.findViewById(R.id.app);
        engine = (TextView) view.findViewById(R.id.engine);
        firmware = (TextView) view.findViewById(R.id.firmware);
        hardware = (TextView) view.findViewById(R.id.hardware);
    }
}
