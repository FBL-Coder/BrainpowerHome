package cn.etsoft.smarthome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.ui.Equipment_control;
import cn.etsoft.smarthome.ui.GroupActivity;
import cn.etsoft.smarthome.ui.SceneSetActivity;
import cn.etsoft.smarthome.ui.UserActivity;

/**
 * Created by Say GoBay on 2016/11/25.
 * 其他设置
 */
public class Setting_OtherSetFragment extends Fragment implements View.OnClickListener {

    private TextView tv_dev, tv_other, tv_secne,tv_user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_frag_other, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_dev = (TextView) view.findViewById(R.id.tv_dev);
        tv_other = (TextView) view.findViewById(R.id.tv_other);
        tv_secne = (TextView) view.findViewById(R.id.tv_secne);
        tv_user = (TextView) view.findViewById(R.id.tv_user);

        tv_dev.setOnClickListener(this);
        tv_other.setOnClickListener(this);
        tv_secne.setOnClickListener(this);
        tv_user.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dev:
                startActivity(new Intent(getActivity(), Equipment_control.class));
                break;
            case R.id.tv_other:
                startActivity(new Intent(getActivity(), GroupActivity.class));
                break;
            case R.id.tv_secne:
                startActivity(new Intent(getActivity(), SceneSetActivity.class));
                break;
            case R.id.tv_user:
                startActivity(new Intent(getActivity(), UserActivity.class));
                break;

        }
    }
}
