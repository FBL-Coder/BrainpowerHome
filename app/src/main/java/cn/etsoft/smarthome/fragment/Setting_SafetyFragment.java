package cn.etsoft.smarthome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/11/25.
 * 安防设置
 */
public class Setting_SafetyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safety,container,false);
        //初始化控件
        return view;
    }
}
