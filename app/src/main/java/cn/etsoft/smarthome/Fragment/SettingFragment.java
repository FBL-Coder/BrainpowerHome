package cn.etsoft.smarthome.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.ui.GroupActivity;
import cn.etsoft.smarthome.ui.NetworkActivity;
import cn.etsoft.smarthome.ui.RoomActivity;
import cn.etsoft.smarthome.ui.SceneActivity;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private TextView networking, room, scene, group;
    private String[] title = {"联网模块", "房间设置", "情景设置", "组合设置"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        networking = (TextView) view.findViewById(R.id.setting_networking);
        room = (TextView) view.findViewById(R.id.setting_room);
        scene = (TextView) view.findViewById(R.id.setting_scene);
        group = (TextView) view.findViewById(R.id.setting_group);
        networking.setText(title[0]);
        room.setText(title[1]);
        scene.setText(title[2]);
        group.setText(title[3]);
        networking.setOnClickListener(this);
        room.setOnClickListener(this);
        scene.setOnClickListener(this);
        group.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.setting_networking:
                intent = new Intent(getActivity(), NetworkActivity.class);
                intent.putExtra("title",title[0]);
                startActivity(intent);
                break;
            case R.id.setting_room:
                intent = new Intent(getActivity(), RoomActivity.class);
                intent.putExtra("title",title[1]);
                startActivity(intent);
                break;
            case R.id.setting_scene:
                intent = new Intent(getActivity(), SceneActivity.class);
                intent.putExtra("title",title[2]);
                startActivity(intent);
                break;
            case R.id.setting_group:
                intent = new Intent(getActivity(), GroupActivity.class);
                intent.putExtra("title",title[3]);
                startActivity(intent);
                break;
        }
    }
}
