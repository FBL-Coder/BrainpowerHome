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
import cn.etsoft.smarthome.adapter_scene2.SceneAdapter_dev;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;
import cn.etsoft.smarthome.ui.SceneSetActivity2;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置-控制设置-按键情景—按键
 */
public class SceneFragment_dev extends Fragment {
    private GridView gridView_light;
    private int room_position = 0;
    private int sceneid = 0;
    private int dev_position = 0;
    private boolean ISCHOOSE = false;
    private List<WareDev> list_Dev;
    private List<String> room_list;
    private LayoutInflater inflater;
    private SceneAdapter_dev sceneAdapter_dev;
    private List<WareSceneDevItem> items ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        this.inflater = inflater;
        initGridView(view);
        SceneSetActivity2.setOnGetIsChooseListener(new SceneSetActivity2.OnGetIsChooseListener() {
            @Override
            public void getOutChoose(boolean ischoose) {
                ISCHOOSE = ischoose;
                upData(ISCHOOSE);
            }
        });
        return view;
    }

//    private void initData() {
//        if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
//            ToastUtil.showToast(getActivity(), "没有收到输入板信息");
//            return;
//        }
//        keyAdapter_keyscene = new KeyAdapter_keyscene(getActivity(), sceneid, room_position, ISCHOOSE);
//        gridView_light.setAdapter(keyAdapter_keyscene);
//        gridView_light.setSelector(new ColorDrawable(Color.TRANSPARENT));
//    }


    /**
     * 初始化控件
     *
     * @param
     */
    private void initGridView(View view) {
        room_position = getArguments().getInt("room_position", room_position);
        sceneid = getArguments().getInt("sceneid", sceneid);
        dev_position = getArguments().getInt("dev_position", dev_position);
        ISCHOOSE = getArguments().getBoolean("ISCHOOSE", ISCHOOSE);
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
        upData(ISCHOOSE);
    }

    //房间内所有设备；
    List<WareDev> Dev_room_all;
    //房间内指定类型设备
    List<WareDev> Dev_room;

    /**
     * 加载数据
     *
     * @param isClose
     */
    public void upData(boolean isClose) {
        for (int i = 0; i < MyApplication.getWareData_Scene().getSceneEvents().size(); i++) {
            if (sceneid == MyApplication.getWareData_Scene().getSceneEvents().get(i).getEventld()) {
                items = MyApplication.getWareData_Scene().getSceneEvents().get(i).getItemAry();
                break;
            }
        }
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
        if (items == null) {
            items = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData_Scene().getSceneEvents().size(); i++) {
                if (sceneid == MyApplication.getWareData_Scene().getSceneEvents().get(i).getEventld()) {
                    MyApplication.getWareData_Scene().getSceneEvents().get(i).setItemAry(items);
                    break;
                }
            }
        }
        //给所有设备和情景关联的赋值
        for (int j = 0; j < Dev_room.size(); j++) {
            boolean isContain = false;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getDevID() == Dev_room.get(j).getDevId()
                        && items.get(i).getUid().equals(Dev_room.get(j).getCanCpuId())
                        && items.get(i).getDevType() == Dev_room.get(j).getType()) {
                    Dev_room.get(j).setSelect(true);
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
        if (sceneAdapter_dev == null) {
            sceneAdapter_dev = new SceneAdapter_dev(Dev_room, getActivity(), inflater, sceneid);
            gridView_light.setAdapter(sceneAdapter_dev);
        } else {
            sceneAdapter_dev.notifyDataSetChanged(Dev_room);
        }
    }
}
