package cn.etsoft.smarthome.fragment_main;

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
import cn.etsoft.smarthome.adapter_main.CurtainAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;

/**
 * Created by Say GoBay on 2016/11/28.
 * 主页-窗帘模块
 */
public class Main_CurtainFragment extends Fragment {
    private GridView gridView;
    private LayoutInflater inflater;
    private List<String> room_list;
    private int room_position = 0;
    //所有窗帘
    private List<WareCurtain>Curtains;
    //对应房间的窗帘
    private List<WareCurtain> Curtains_room;
    //全部房间
    private int DEV_ALL_ROOM = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_socket, container, false);
        this.inflater = inflater;
        //初始化GridView
        initGridView(view);
        return view;
    }

    /**
     * 初始化GridView
     * @param view
     */
    private void initGridView(View view) {
        gridView = (GridView) view.findViewById(R.id.gridView);
        //加载数据
        upData();
    }

    /**
     * 加载数据
     */
    public void upData() {
        //房间集合
        room_list = MyApplication.getRoom_list();
        //房间id；
        room_position = getArguments().getInt("room_position", 0);

        Curtains = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
            Curtains.add(MyApplication.getWareData().getCurtains().get(i));
        }
        //房间内的窗帘集合
        Curtains_room = new ArrayList<>();
        //根据房间id获取设备；
        if (room_position == DEV_ALL_ROOM) {
            Curtains_room = Curtains;
        } else {
            for (int i = 0; i < Curtains.size(); i++) {
                if (Curtains.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
                    Curtains_room.add(Curtains.get(i));
                }
            }
        }
        CurtainAdapter gridViewAdapter = new CurtainAdapter(Curtains_room, getActivity(), inflater);
        gridView.setAdapter(gridViewAdapter);
    }
}
