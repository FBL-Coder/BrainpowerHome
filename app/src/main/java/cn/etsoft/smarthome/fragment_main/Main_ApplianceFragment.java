package cn.etsoft.smarthome.fragment_main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_main.ApplianceAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2016/11/28.
 * 空调模块
 */
public class Main_ApplianceFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView listView_appliance;
    private ApplianceAdapter listViewAdapter;
    private List<WareDev> AllDevs;
    private List<String> room_list;
    private int room_position = 0;
    private int DEVS_ALL_ROOM = -1;
    private LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appliance, container, false);
        this.inflater = inflater;
        //初始化空件
        initEvent();
        initListView(view);
        return view;
    }

    /**
     * 数据监听
     */
    private void initEvent() {
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 4)
                    //更新数据
                    upData();
            }
        });
    }

    /**
     * 初始化空件
     */
    private void initListView(View view) {
        listView_appliance = (ListView) view.findViewById(R.id.listView_appliance);
        if (MyApplication.getWareData().getAirConds().size() == 0)
            return;
        upData();
    }

    /**
     * 数据加载；
     */
    public void upData() {
        AllDevs = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            AllDevs.add(MyApplication.getWareData().getDevs().get(i));
        }
        //房间内的灯集合
        List<WareDev> AllDevs_room = new ArrayList<>();
        //房间集合
        room_list = MyApplication.getRoom_list();
        //房间id；
        room_position = getArguments().getInt("room_position", 0);
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

        if (listViewAdapter != null) {
            listViewAdapter.notifyDataSetChanged();
        } else {
            listViewAdapter = new ApplianceAdapter(AllDevs_room, getActivity(), inflater);
            listView_appliance.setAdapter(listViewAdapter);
        }
        listView_appliance.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
