package cn.etsoft.smarthome.fragment_group2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_group2.CurtainAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;

/**
 * Created by Say GoBay on 2016/11/28.
 * 情景设置——窗帘模块
 */
public class Main_CurtainFragment extends Fragment {
    private int room_position = 0;
    private int index = 0;
    private View view;
    private LayoutInflater inflater;
    private List<WareCurtain> curtains;
    private List<String> room_list;
    private List<WareCurtain> curtain_room;
    private CurtainAdapter gridViewAdapter;
    private int DEVS_ALL_ROOM = -1;
    private GridView gridView_light;
    private List<WareKeyOpItem> keyOpItems;
    private boolean isClose = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light2, container, false);
        //初始化控件
        this.inflater = inflater;
        initGridView(view);
        //父Fragment获取按键数据后的回调
        InPutFragment.setOnGetKeyInputDataListener(new InPutFragment.OnGetKeyInputDataListener() {
            @Override
            public void getKeyInputData() {
                keyOpItems = MyApplication.mInstance.getInput_key_data();
                if (keyOpItems == null)
                    keyOpItems = new ArrayList<>();
                upData(isClose);
            }
        });
        //父Fragment点击是否显示不包含的设备回调
        InPutFragment.setOnIsCloseListener(new InPutFragment.OnIsCloseListener() {
            @Override
            public void getIsClose(boolean isClose_) {
                isClose = isClose_;
                upData(isClose);
            }
        });
        //房间点击刷新界面回调；
        InPutFragment.setOnGetRoomListener(new InPutFragment.OnGetRoomListener() {
            @Override
            public void getRoomposition(int room_position_click) {
                if (room_position_click == 0) {
                    room_position = -1;
                } else {
                    room_position = room_position_click - 1;
                }
                upData(isClose);
            }
        });
        return view;
    }

    public void upData(boolean isClose) {

        curtains = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getCurtains().size(); i++) {
            curtains.add(MyApplication.getWareData_Scene().getCurtains().get(i));
        }
        for (int j = 0; j < curtains.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < keyOpItems.size(); i++) {
                if (keyOpItems.get(i).getDevId() == curtains.get(j).getDev().getDevId()
                        && keyOpItems.get(i).getDevType() == curtains.get(j).getDev().getType()
                        && keyOpItems.get(i).getDevType() == 4
                        && keyOpItems.get(i).getDevUnitID().equals(curtains.get(j).getDev().getCanCpuId())) {
                    curtains.get(j).getDev().setSelect(true);
                    curtains.get(j).setbOnOff(keyOpItems.get(i).getKeyOpCmd());
                    isContain = true;
                }
            }
            if (!isContain) {
                curtains.get(j).getDev().setSelect(false);
            }
        }
        if (isClose) {
            //房间内的灯集合
            curtain_room = new ArrayList<>();
            //房间集合
            room_list = MyApplication.getRoom_list();
            //根据房间id获取设备；
            if (room_position == DEVS_ALL_ROOM) {
                curtain_room = curtains;
            } else {
                for (int i = 0; i < curtains.size(); i++) {
                    if (curtains.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
                        curtain_room.add(curtains.get(i));
                    }
                }
            }
            for (int i = 0; i < curtain_room.size(); ) {
                if (!curtain_room.get(i).getDev().isSelect())
                    curtain_room.remove(i);
                else
                    i++;
            }
        } else {
            //房间内的灯集合
            curtain_room = new ArrayList<>();
            //房间集合
            room_list = MyApplication.getRoom_list();
            //根据房间id获取设备；
            if (room_position == DEVS_ALL_ROOM) {
                curtain_room = curtains;
            } else {
                for (int i = 0; i < curtains.size(); i++) {
                    if (curtains.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
                        curtain_room.add(curtains.get(i));
                    }
                }
            }
        }
        if (gridViewAdapter == null) {
            gridViewAdapter = new CurtainAdapter(curtain_room, getActivity(), inflater,index);
            gridView_light.setAdapter(gridViewAdapter);
        } else {
            gridViewAdapter.notifyDataSetChanged(curtain_room);
        }
        gridView_light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });


//        curtains = new ArrayList<>();
//        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
//            curtains.add(MyApplication.getWareData().getCurtains().get(i));
//        }
//        //房间内的灯集合
//        curtain_room = new ArrayList<>();
//        //房间集合
//        room_list = MyApplication.getRoom_list();
//        //根据房间id获取设备；
//        if (room_position == DEVS_ALL_ROOM) {
//            curtain_room = curtains;
//        } else {
//            for (int i = 0; i < curtains.size(); i++) {
//                if (curtains.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
//                    curtain_room.add(curtains.get(i));
//                }
//            }
//        }
//        final List<WareCurtain> curtain_room_clck = curtain_room;
//
//
//
//        if (gridViewAdapter == null) {
//            gridViewAdapter = new CurtainAdapter(curtain_room, getActivity(), inflater);
//            gridView_light.setAdapter(gridViewAdapter);
//        } else {
//            gridViewAdapter.notifyDataSetChanged(curtain_room);
//        }
//
//        gridView_light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//            }
//        });
    }

    /**
     * 初始化控件
     *
     * @param
     */
    private void initGridView(View view) {
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
        //房间id；
        room_position = getArguments().getInt("room_position");
        index = getArguments().getInt("index");
        isClose = getArguments().getBoolean("isClose");
        keyOpItems = MyApplication.mInstance.getInput_key_data();
        if (keyOpItems == null)
            keyOpItems = new ArrayList<>();
        upData(isClose);
    }
}
