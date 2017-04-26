package cn.etsoft.smarthome.fragment_scene2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_scene2.ApplianceAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2016/11/28.
 * 情景设置——空调模块
 */
public class Main_ApplianceFragment extends Fragment {

    private GridView listView_appliance;
    private ApplianceAdapter listViewAdapter;
    private List<WareDev> AllAirConds;
    private List<String> room_list;
    private int room_position = 0;
    private LayoutInflater inflater;
    private byte sceneid = 0;
    private int DEVS_ALL_ROOM = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appliance2, container, false);
        this.inflater = inflater;
        initListView(view);
        upData();
        return view;
    }

    /**
     * 初始化空件
     */
    private void initListView(View view) {
        listView_appliance = (GridView) view.findViewById(R.id.listView_appliance);
        if (MyApplication.getWareData_Scene().getAirConds() == null || MyApplication.getWareData_Scene().getAirConds().size() == 0)
            return;
        upData();
    }

    /**
     * 数据加载；
     */
    List<WareDev> AllAirConds_room;

    public void upData() {
        AllAirConds = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {
            AllAirConds.add(MyApplication.getWareData_Scene().getDevs().get(i));
        }
        //房间内的空调集合
        AllAirConds_room = new ArrayList<>();
        //房间集合
        room_list = MyApplication.getRoom_list();
        //房间id；
        room_position = getArguments().getInt("room_position", 0);
        sceneid = getArguments().getByte("sceneid");
        //根据房间id获取设备；
        if (room_position == DEVS_ALL_ROOM) {
            AllAirConds_room = AllAirConds;
        } else {
            for (int i = 0; i < AllAirConds.size(); i++) {
                if (AllAirConds.get(i).getRoomName().equals(room_list.get(room_position))) {
                    AllAirConds_room.add(AllAirConds.get(i));
                }
            }
        }

        if (listViewAdapter != null) {
            listViewAdapter.notifyDataSetChanged();
        } else {
            listViewAdapter = new ApplianceAdapter(AllAirConds_room, getActivity(), inflater, sceneid ,true);
            listView_appliance.setAdapter(listViewAdapter);
        }
    }
}
