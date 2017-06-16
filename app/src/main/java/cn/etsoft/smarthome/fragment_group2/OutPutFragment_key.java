package cn.etsoft.smarthome.fragment_group2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_group2.KeyAdapter_output;
import cn.etsoft.smarthome.domain.Out_List_printcmd;
import cn.etsoft.smarthome.domain.PrintCmd;
import cn.etsoft.smarthome.pullmi.entity.WareChnOpItem;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置-控制设置-输出—按键
 */
public class OutPutFragment_key extends Fragment {
    private FragmentActivity mActivity;
    private LayoutInflater inflater;
    private GridView gridView_light;
    private String uid;
    private KeyAdapter_output keyAdapter_output;
    private int input_position = 0;
    private List<String> keyname_list;
    private List<WareChnOpItem> ChnOpItem_list;
    private WareChnOpItem ChnOpItem;
    private int KEY_ACTION_UP = 1;
    private boolean ISCHOOSE = false;
    private int position_keyinput = 0, devtype = 0;
    private List<PrintCmd> listData;
    private List<Out_List_printcmd> listData_all;
    private Dialog mDialog;

    public  OutPutFragment_key(FragmentActivity activity){
        mActivity =activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        //初始化控件
        this.inflater = inflater;
        OutPutFragment.setOnGetOutKeyDataListeener(new OutPutFragment.OnGetOutKeyDataListeener() {
            @Override
            public void getOutKeyData() {
                initData();
            }
        });
        OutPutFragment.setOnGetIsChooseListener(new OutPutFragment.OnGetIsChooseListener() {
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
        listData_all = MyApplication.mInstance.getOut_key_data();
        if (listData_all == null || listData_all.size() == 0) {
            return;
        }
        if (ISCHOOSE) {
            listData = new ArrayList<>();
            for (int i = 0; i < listData_all.size(); i++) {
                if (MyApplication.getWareData().getKeyInputs().get(position_keyinput).getDevUnitID().equals(listData_all.get(i).getUnitid())) {
                    for (int j = 0; j < listData_all.get(i).getPrintCmds().size(); j++) {
                        if (listData_all.get(i).getPrintCmds().get(j).isSelect())
                            listData.add(listData_all.get(i).getPrintCmds().get(j));
                    }
                }
            }
        } else {
            for (int i = 0; i < listData_all.size(); i++) {
                if (MyApplication.getWareData().getKeyInputs().get(position_keyinput).getDevUnitID().equals(listData_all.get(i).getUnitid())) {
                    listData = listData_all.get(i).getPrintCmds();
                }
            }
        }
        if (listData == null)
            listData = new ArrayList<>();
        if (keyAdapter_output != null) {
            keyAdapter_output.notifyDataSetChanged(listData);
        } else {
            keyAdapter_output = new KeyAdapter_output(getActivity(), listData);
        }
        gridView_light.setAdapter(keyAdapter_output);
    }


    /**
     * 初始化控件
     *
     * @param
     */
    private void initGridView(View view) {
        position_keyinput = getArguments().getInt("keyinput_position", 0);
        devtype = getArguments().getInt("devtype", 0);
        ISCHOOSE = getArguments().getBoolean("ISCHOOSE", false);
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
        initData();
    }
}
