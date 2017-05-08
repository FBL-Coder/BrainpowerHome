package cn.etsoft.smarthome.fragment_setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.ui.Equipment_control;
import cn.etsoft.smarthome.ui.GroupActivity2;
import cn.etsoft.smarthome.ui.SafetyActivity;
import cn.etsoft.smarthome.ui.SceneSetActivity2;

/**
 * Created by Say GoBay on 2016/11/25.
 * 设置-高级设置
 */
public class Setting_OtherSetFragment extends Fragment implements View.OnClickListener {

    private TextView tv_dev, tv_other, tv_scene,tv_safety;

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
        tv_dev.setOnClickListener(this);
        tv_other.setOnClickListener(this);
        tv_scene.setOnClickListener(this);
        tv_safety.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dev:
                startActivity(new Intent(getActivity(), Equipment_control.class));
                break;
            case R.id.tv_other:
//                startActivity(new Intent(getActivity(), GroupActivity.class));
                startActivity(new Intent(getActivity(), GroupActivity2.class));
                break;
            case R.id.tv_scene:
//                startActivity(new Intent(getActivity(), SceneSetActivity.class));
                startActivity(new Intent(getActivity(), SceneSetActivity2.class));
                break;
            case R.id.tv_safety:
                //查询联网模块的防区信息
                String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                        ",\"datType\":32" +
                        ",\"subType1\":3" +
                        ",\"subType2\":255" +
                        "}";
                MyApplication.sendMsg(ctlStr);
                startActivity(new Intent(getActivity(), SafetyActivity.class));
                break;

        }
    }
}
