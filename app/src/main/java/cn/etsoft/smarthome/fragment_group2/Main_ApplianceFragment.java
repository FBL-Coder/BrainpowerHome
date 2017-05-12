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
import cn.etsoft.smarthome.adapter_group2.ApplianceAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置-控制设置-输入—家电模块
 */
public class Main_ApplianceFragment extends Fragment {
    private int room_position = 0;
    private int index = 0;
    private View view;
    private LayoutInflater inflater;
    private List<WareDev> AllDevs;
    private List<WareDev> AllDevs_room;
    private List<String> room_list;
    private ApplianceAdapter listViewAdapter;
    private int DEVS_ALL_ROOM = -1;
    private List<WareKeyOpItem> keyOpItems;
    private boolean isClose = false;
    private GridView listView_appliance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appliance2, container, false);
        this.inflater = inflater;
        initGridView(view);
        //获取服务器数据刷新界面回调
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
            public void getRoomPosition(int room_position_click) {
                if (room_position_click == -1) {
                    room_position = -1;
                } else {
                    room_position = room_position_click;
                }
                upData(isClose);
            }
        });
        return view;
    }

    /**
     * 数据加载；
     */
    public void upData(boolean isClose) {

        if (MyApplication.getWareData_Scene().getDevs() != null && MyApplication.getWareData_Scene().getDevs().size() > 1) {
            AllDevs = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {
                AllDevs.add(MyApplication.getWareData_Scene().getDevs().get(i));
            }
        }
        for (int j = 0; j < AllDevs.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < keyOpItems.size(); i++) {
                if (keyOpItems.get(i).getDevId() == AllDevs.get(j).getDevId()
                        && keyOpItems.get(i).getDevType() == AllDevs.get(j).getType()
                        && keyOpItems.get(i).getDevUnitID().equals(AllDevs.get(j).getCanCpuId())) {
                    AllDevs.get(j).setSelect(true);
                    AllDevs.get(j).setbOnOff(keyOpItems.get(i).getKeyOpCmd());
                    isContain = true;
                }
            }
            if (!isContain) {
                AllDevs.get(j).setSelect(false);
            }
        }
        if (isClose) {
            //房间内的灯集合
            AllDevs_room = new ArrayList<>();
            //房间集合
            room_list = MyApplication.getRoom_list();
            //根据房间id获取设备；
            if (room_position == DEVS_ALL_ROOM) {
                AllDevs_room = AllDevs;
            } else {
                for (int i = 0; i < AllDevs.size(); i++) {
                    if (AllDevs.get(i).getRoomName().equals(room_list.get(room_position))) {
                        AllDevs_room.add(AllDevs.get(i));
                    }
                }
            }
            for (int i = 0; i < AllDevs_room.size(); ) {
                if (!AllDevs_room.get(i).isSelect())
                    AllDevs_room.remove(i);
                else
                    i++;
            }
        } else {
            //房间内的灯集合
            AllDevs_room = new ArrayList<>();
            //房间集合
            room_list = MyApplication.getRoom_list();
            //根据房间id获取设备；
            if (room_position == DEVS_ALL_ROOM) {
                AllDevs_room = AllDevs;
            } else {
                for (int i = 0; i < AllDevs.size(); i++) {
                    if (AllDevs.get(i).getRoomName().equals(room_list.get(room_position))) {
                        AllDevs_room.add(AllDevs.get(i));
                    }
                }
            }
        }
        if (listViewAdapter == null) {
            listViewAdapter = new ApplianceAdapter(AllDevs_room, getActivity(), inflater,index);
            listView_appliance.setAdapter(listViewAdapter);
        } else {
            listViewAdapter.notifyDataSetChanged(AllDevs_room);
        }
        listView_appliance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

//        //房间内的灯集合
//        List<WareDev> AllDevs_room = new ArrayList<>();
//        //房间集合
//        room_list = MyApplication.getRoom_list();
//        //房间id；
//        room_position = getArguments().getInt("room_position", 0);
//        //根据房间id获取设备；
//        if (room_position == DEVS_ALL_ROOM) {
//            AllDevs_room = AllDevs;
//        } else {
//            for (int i = 0; i < AllDevs.size(); i++) {
//                if (AllDevs.get(i).getRoomName().equals(room_list.get(room_position))) {
//                    AllDevs_room.add(AllDevs.get(i));
//                }
//            }
//        }
//
//        if (listViewAdapter != null) {
//            listViewAdapter.notifyDataSetChanged();
//        } else {
//            listViewAdapter = new ApplianceAdapter(AllDevs_room, getActivity(), inflater);
//            listView_appliance.setAdapter(listViewAdapter);
//        }
    }
    /**
     * 初始化控件
     *
     * @param
     */
    private void initGridView(View view) {
        listView_appliance = (GridView) view.findViewById(R.id.listView_appliance);
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
