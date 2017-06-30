package cn.etsoft.smarthome.fragment_group3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_group3.InputAdapter_dev;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;

/**
 * Created by Say GoBay on 2017/6/6.
 * 高级设置--控制设置--按键配设备--显示设备的Fragment
 */
public class InputFragment_dev extends Fragment {
    private FragmentActivity mActivity;
    private int room_position = 0;
    private int dev_position = 0;
    private int index = 0;
    private View view;
    private LayoutInflater inflater;
    private InputAdapter_dev inputAdapter_dev;
    private GridView gridView_light;
    private List<WareKeyOpItem> keyOpItems;
    private boolean IsClose = false;
    private List<WareDev> list_Dev;
    private List<String> room_list;

    public InputFragment_dev(FragmentActivity activity) {
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light2, container, false);
        this.inflater = inflater;
        //初始化控件
        initGridView(view);
        //父Fragment获取按键数据后的回调
        cn.etsoft.smarthome.fragment_group3.InPutFragment.setOnGetKeyInputDataListener(new cn.etsoft.smarthome.fragment_group3.InPutFragment.OnGetKeyInputDataListener() {
            @Override
            public void getKeyInputData() {
                keyOpItems = MyApplication.mInstance.getInput_key_data();
                if (keyOpItems == null)
                    keyOpItems = new ArrayList<>();
                upData(IsClose);
            }
        });

        //父Fragment点击是否显示不包含的设备回调
        cn.etsoft.smarthome.fragment_group3.InPutFragment.setOnIsCloseListener(new cn.etsoft.smarthome.fragment_group3.InPutFragment.OnIsCloseListener() {
            @Override
            public void getIsClose(boolean isClose_) {
                IsClose = isClose_;
                upData(IsClose);
            }
        });
        return view;
    }

    /**
     * 初始化控件
     * @param
     */
    private void initGridView(View view) {
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
        //房间id；
        room_position = getArguments().getInt("room_position");
        index = getArguments().getInt("index");
        dev_position = getArguments().getInt("dev_position");
        IsClose = getArguments().getBoolean("IsClose");
        keyOpItems = MyApplication.mInstance.getInput_key_data();
        if (keyOpItems == null)
            keyOpItems = new ArrayList<>();
        upData(IsClose);
    }

    //房间内所有设备；
    List<WareDev> Dev_room_all;
    //房间内指定类型设备
    List<WareDev> Dev_room;

    /**
     * 加载数据
     * @param isClose
     */
    public void upData(boolean isClose) {
        list_Dev = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            list_Dev.add(MyApplication.getWareData().getDevs().get(i));
        }
        //房间内的设备集合
        Dev_room_all = new ArrayList<>();
        //房间内指定类型设备
        Dev_room = new ArrayList<>();
        //房间集合
        room_list = new ArrayList<>();
        //给房间集合加上“全部”
        room_list.add("全部");
        for (int i = 0; i < MyApplication.getRoom_list().size(); i++) {
            room_list.add(MyApplication.getRoom_list().get(i));
        }
        //根据房间id获取设备
        if (room_position == 0) {
            Dev_room_all = list_Dev;
        } else {
            for (int i = 0; i < list_Dev.size(); i++) {
                if (list_Dev.get(i).getRoomName().equals(room_list.get(room_position))) {
                    Dev_room_all.add(list_Dev.get(i));
                }
            }
        }
        //根据房间id和设备类型获取对应设备
        if (dev_position == 0) {
            for (int i = 0; i < Dev_room_all.size(); i++) {
                if (Dev_room_all.get(i).getType() == 3)
                    Dev_room.add(Dev_room_all.get(i));
            }
        } else if (dev_position == 1) {
            for (int i = 0; i < Dev_room_all.size(); i++) {
                if (Dev_room_all.get(i).getType() == 4)
                    Dev_room.add(Dev_room_all.get(i));
            }
        } else if (dev_position == 2)
            for (int i = 0; i < Dev_room_all.size(); i++) {
                if (Dev_room_all.get(i).getType() == 0 || Dev_room_all.get(i).getType() == 1 || Dev_room_all.get(i).getType() == 2)
                    Dev_room.add(Dev_room_all.get(i));
            }
        //给所有设备和按键关联的赋值
        for (int j = 0; j < Dev_room.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < keyOpItems.size(); i++) {
                if (keyOpItems.get(i).getDevId() == Dev_room.get(j).getDevId()
                        && keyOpItems.get(i).getDevType() == Dev_room.get(j).getType()
                        && keyOpItems.get(i).getDevUnitID().equals(Dev_room.get(j).getCanCpuId())) {
                    Dev_room.get(j).setSelect(true);
                    Dev_room.get(j).setbOnOff(keyOpItems.get(i).getKeyOpCmd());
                    Log.e("KeyOpCmd", keyOpItems.get(i).getKeyOpCmd() + "");
                    isContain = true;
                }
            }
            if (!isContain) {
                Dev_room.get(j).setSelect(false);
            }
        }
        if (isClose) {
            for (int i = 0; i < Dev_room.size(); ) {
                if (!Dev_room.get(i).isSelect()) {
                    Dev_room.remove(i);
                } else {
                    i++;
                }
            }
        }
        if (inputAdapter_dev == null) {
            inputAdapter_dev = new InputAdapter_dev(Dev_room, mActivity, inflater, index);
            gridView_light.setAdapter(inputAdapter_dev);
        } else {
            inputAdapter_dev.notifyDataSetChanged(Dev_room);
        }
    }
}
