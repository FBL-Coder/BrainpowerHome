package cn.etsoft.smarthome.fragment_group3;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_group3.KeyAdapter_keyscene;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置-控制设置-按键情景—按键
 */
public class KeySceneFragment_key extends Fragment {
    private GridView gridView_light;
    private KeyAdapter_keyscene keyAdapter_keyscene;
    private boolean ISCHOOSE = false;
    private int position_keyinput = 0;
    private int sceneid = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        //发送消息
        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"datType\":58" +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                "}";
        MyApplication.sendMsg(ctlStr);
        cn.etsoft.smarthome.fragment_group3.KeySceneFragment.setOnGetKeySceneDataListeener(new cn.etsoft.smarthome.fragment_group3.KeySceneFragment.OnGetKeySceneDataListeener() {
            @Override
            public void getKeySceneData() {
                initData();
            }
        });
        cn.etsoft.smarthome.fragment_group3.KeySceneFragment.setOnGetIsChooseListener(new cn.etsoft.smarthome.fragment_group3.KeySceneFragment.OnGetIsChooseListener() {
            @Override
            public void getOutChoose(boolean ischoose) {
                ISCHOOSE = ischoose;
                initData();
            }
        });
        initGridView(view);
        return view;
    }

    private void initData() {
        if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
            ToastUtil.showToast(getActivity(), "没有收到输入板信息");
            return;
        }
        keyAdapter_keyscene = new KeyAdapter_keyscene(getActivity(), sceneid,position_keyinput,ISCHOOSE);
        gridView_light.setAdapter(keyAdapter_keyscene);
        gridView_light.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    /**
     * 初始化控件
     * @param
     */
    private void initGridView(View view) {
        position_keyinput = getArguments().getInt("keyinput_position", position_keyinput);
        sceneid = getArguments().getInt("sceneid", sceneid);
        ISCHOOSE = getArguments().getBoolean("ISCHOOSE", ISCHOOSE);
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
    }
}
