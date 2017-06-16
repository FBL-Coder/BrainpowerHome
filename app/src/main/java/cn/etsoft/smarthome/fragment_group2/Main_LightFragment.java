package cn.etsoft.smarthome.fragment_group2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_group2.LightAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.pullmi.entity.WareLight;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置-控制设置-输入-灯光模块
 */
public class Main_LightFragment extends Fragment {
    private FragmentActivity mActivity;
    private int room_position = 0;
    private int index = 0;
    private View view;
    private LayoutInflater inflater;
    private List<WareLight> lights;
    private List<String> room_list;
    private List<WareLight> light_room;
    private LightAdapter gridViewAdapter;
    private int DEVS_ALL_ROOM = -1;
    private GridView gridView_light;
    private List<WareKeyOpItem> keyOpItems;
    private boolean isClose = false;
    private Dialog mDialog;

    public Main_LightFragment(FragmentActivity activity){
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
                    room_position = room_position_click ;
                }
                upData(isClose);
            }
        });
        return view;
    }

    public void upData(boolean isClose) {
        //所有灯光设备
        lights = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getLights().size(); i++) {
            lights.add(MyApplication.getWareData_Scene().getLights().get(i));
        }
        //给所有灯光设备和按键关联的赋值
        for (int j = 0; j < lights.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < keyOpItems.size(); i++) {
                if (keyOpItems.get(i).getDevId() == lights.get(j).getDev().getDevId()
                        && keyOpItems.get(i).getDevType() == lights.get(j).getDev().getType()
                        && keyOpItems.get(i).getDevType() == 3
                        && keyOpItems.get(i).getDevUnitID().equals(lights.get(j).getDev().getCanCpuId())) {
                    lights.get(j).getDev().setSelect(true);
                    lights.get(j).setbOnOff(keyOpItems.get(i).getKeyOpCmd());
                    Log.e("KeyOpCmd", keyOpItems.get(i).getKeyOpCmd() + "");
                    isContain = true;
                }
            }
            if (!isContain) {
                lights.get(j).getDev().setSelect(false);
            }
        }
        if (isClose) {
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
            for (int i = 0; i < light_room.size(); ) {
                if (!light_room.get(i).getDev().isSelect()) {
                    light_room.remove(i);
                } else {
                    i++;
                }
            }
        } else {
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
        }
        if (gridViewAdapter == null) {
            gridViewAdapter = new LightAdapter(light_room, mActivity, inflater, index);
            gridView_light.setAdapter(gridViewAdapter);
        } else {
            gridViewAdapter.notifyDataSetChanged(light_room);
        }
        gridView_light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
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
