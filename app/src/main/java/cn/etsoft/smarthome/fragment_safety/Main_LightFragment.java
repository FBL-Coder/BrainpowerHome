package cn.etsoft.smarthome.fragment_safety;

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
import cn.etsoft.smarthome.adapter_safety.LightAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareLight;

/**
 * Created by Say GoBay on 2016/11/28.
 * 灯光模块
 */
public class Main_LightFragment extends Fragment {

    private GridView gridView_light;
    private int room_position = 0;
    private List<String> room_list;
    private LightAdapter gridViewAdapter;
    private View view;
    private LayoutInflater inflater;
    private List<WareLight> lights;
    //全部房间
    private int DEVS_ALL_ROOM = -1;
    private int safety_position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, container, false);
        //初始化控件
        this.inflater = inflater;
        initGridView();
        upData();
        return view;
    }

    List<WareLight> light_room;

    public void upData() {
        lights = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
            lights.add(MyApplication.getWareData().getLights().get(i));
        }
        //房间内的灯集合
        light_room = new ArrayList<>();
        //房间集合
        room_list = MyApplication.getRoom_list();
        //根据房间id获取设备；
        if (room_position == DEVS_ALL_ROOM) {
            light_room = lights;
        } else {
            for (int i = 0; i < lights.size(); i++) {
                if (lights.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
                    light_room.add(lights.get(i));
                }
            }
        }
        final List<WareLight> light_room_click = light_room;
        if (gridViewAdapter == null) {
            gridViewAdapter = new LightAdapter(light_room, getActivity(), inflater, safety_position,true);
            gridView_light.setAdapter(gridViewAdapter);
        } else {
            gridViewAdapter.notifyDataSetChanged(light_room);
        }
//        gridView_light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (sceneid < 2) {
//                    ToastUtil.showToast(getActivity(), "全开、全关模式不可操作");
//                    return;
//                }
//
//                for (int i = 0; i < MyApplication.getWareData_Scene().getLights().size(); i++) {
//                    if (light_room.get(position).getDev().getDevId() == MyApplication.getWareData_Scene().getLights().get(i).getDev().getDevId() && light_room.get(position).getDev().getCanCpuId().equals(MyApplication.getWareData_Scene().getLights().get(i).getDev().getCanCpuId())) {
//                        if (light_room_click.get(position).getbOnOff() == 0) {
//                            light_room.get(position).setbOnOff((byte) 1);
//                            MyApplication.getWareData_Scene().getLights().get(i).setbOnOff((byte) 1);
//                        } else {
//                            light_room.get(position).setbOnOff((byte) 0);
//                            MyApplication.getWareData_Scene().getLights().get(i).setbOnOff((byte) 0);
//                        }
//                        gridViewAdapter.notifyDataSetChanged(light_room);
//                    }
//                }
//            }
//        });
    }

    /**
     * 初始化控件
     *
     * @param
     */
    private void initGridView() {
        //房间id；
        room_position = getArguments().getInt("room_position");
        safety_position = getArguments().getInt("safety_position");
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
    }

}
