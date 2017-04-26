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
import cn.etsoft.smarthome.adapter_scene2.CurtainAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;

/**
 * Created by Say GoBay on 2016/11/28.
 * 情景设置——窗帘模块
 */
public class Main_CurtainFragment extends Fragment {
    private GridView gridView;
    private LayoutInflater inflater;
    private List<String> room_list;
    private int room_position = 0;
    private List<WareCurtain> Curtains;
    private byte sceneid = 0;
    private CurtainAdapter gridViewAdapter;
    private int DEVS_ALL_ROOM = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_socket, container, false);
        this.inflater = inflater;
        //初始化控件
        initGridView(view);
        upData();
        return view;
    }

    List<WareCurtain> Curtains_room;

    public void upData() {
        Curtains = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getCurtains().size(); i++) {
            Curtains.add(MyApplication.getWareData_Scene().getCurtains().get(i));
        }
        //房间内的灯集合
        Curtains_room = new ArrayList<>();
        //房间集合
        room_list = MyApplication.getRoom_list();
        //房间id；
        room_position = getArguments().getInt("room_position", 0);

        final List<WareCurtain> curtains_room_click = Curtains_room;
        //长按情景删除
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (sceneid<2){
//                    ToastUtil.showToast(getActivity(),"全开、全关模式不可操作");
//                    return;
//                }
//                for (int i = 0; i < MyApplication.getWareData_Scene().getCurtains().size(); i++) {
//                    if (Curtains_room.get(position).getDev().getDevId() == MyApplication.getWareData_Scene().getCurtains().get(i).getDev().getDevId() && Curtains_room.get(position).getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getCurtains().get(i).getDev().getCanCpuId())) {
//                        if (curtains_room_click.get(position).getbOnOff() == 0) {
//                            MyApplication.getWareData_Scene().getCurtains().get(i).setbOnOff((byte) 1);
//                            Curtains_room.get(position).setbOnOff((byte) 1);
//                        } else {
//                            MyApplication.getWareData_Scene().getCurtains().get(i).setbOnOff((byte) 0);
//                            Curtains_room.get(position).setbOnOff((byte) 0);
//                        }
//                        gridViewAdapter.notifyDataSetChanged(Curtains_room);
//                    }
//                }
//            }
//        });

        //根据房间id获取设备；
        if (room_position == DEVS_ALL_ROOM) {
            Curtains_room = Curtains;
        } else {
            for (int i = 0; i < Curtains.size(); i++) {
                if (Curtains.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
                    Curtains_room.add(Curtains.get(i));
                }
            }
        }
        gridViewAdapter = new CurtainAdapter(Curtains_room, getActivity(), inflater, sceneid,true);
        gridView.setAdapter(gridViewAdapter);
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initGridView(View view) {
        //房间id；
        room_position = getArguments().getInt("room_position");
        sceneid = getArguments().getByte("sceneid");
        gridView = (GridView) view.findViewById(R.id.gridView);
    }
}
