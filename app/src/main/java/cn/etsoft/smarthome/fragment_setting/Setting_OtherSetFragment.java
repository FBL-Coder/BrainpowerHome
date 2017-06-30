package cn.etsoft.smarthome.fragment_setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.ui.ConditionEventActivity;
import cn.etsoft.smarthome.ui.Equipment_control2;
import cn.etsoft.smarthome.ui.GroupActivity2;
import cn.etsoft.smarthome.ui.GroupSetActivity;
import cn.etsoft.smarthome.ui.SafetyActivity;
import cn.etsoft.smarthome.ui.SceneSetActivity2;
import cn.etsoft.smarthome.ui.TimerActivity;

/**
 * Created by Say GoBay on 2016/11/25.
 * 设置-高级设置
 */
public class Setting_OtherSetFragment extends Fragment implements View.OnClickListener {

    private Activity mActivity;
    private TextView tv_dev, tv_other, tv_scene,tv_safety,tv_time,tv_condition;
    private TextView gourp_set;

    public Setting_OtherSetFragment(Activity activity){
        mActivity =activity;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frag_other, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        tv_dev = (TextView) view.findViewById(R.id.tv_dev);
        tv_other = (TextView) view.findViewById(R.id.tv_other);
        tv_scene = (TextView) view.findViewById(R.id.tv_scene);
        tv_safety = (TextView) view.findViewById(R.id.tv_safety);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_condition = (TextView) view.findViewById(R.id.tv_condition);
        gourp_set = (TextView) view.findViewById(R.id.gourp_set);
        tv_dev.setOnClickListener(this);
        tv_other.setOnClickListener(this);
        tv_scene.setOnClickListener(this);
        tv_safety.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_condition.setOnClickListener(this);
        gourp_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dev:
//                startActivity(new Intent(mActivity, Equipment_control.class));
                startActivity(new Intent(mActivity, Equipment_control2.class));
                break;
            case R.id.tv_other:
//                startActivity(new Intent(mActivity, GroupActivity.class));
                startActivity(new Intent(mActivity, GroupActivity2.class));
                break;
            case R.id.tv_scene:
//                startActivity(new Intent(mActivity, SceneSetActivity.class));
                startActivity(new Intent(mActivity, SceneSetActivity2.class));
                break;
            case R.id.tv_safety:
                startActivity(new Intent(mActivity, SafetyActivity.class));
                break;
            case R.id.tv_time:
                startActivity(new Intent(mActivity, TimerActivity.class));
                break;
            case R.id.tv_condition:
                startActivity(new Intent(mActivity, ConditionEventActivity.class));
                break;
            case R.id.gourp_set:
                startActivity(new Intent(mActivity, GroupSetActivity.class));
                break;

        }
    }
}
