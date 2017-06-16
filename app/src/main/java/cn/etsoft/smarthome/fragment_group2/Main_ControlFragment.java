package cn.etsoft.smarthome.fragment_group2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置-控制设置-输入—监控
 */
public class Main_ControlFragment extends Fragment {
    private FragmentActivity mActivity;
    private ImageView control_back,refrush;
    private EditText username,password;
    public Main_ControlFragment(FragmentActivity activity){
        mActivity= activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control,container,false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        control_back = (ImageView) view.findViewById(R.id.control_back);
        refrush = (ImageView) view.findViewById(R.id.refrush);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
    }
}