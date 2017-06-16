package cn.etsoft.smarthome.fragment_main;

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
import cn.etsoft.smarthome.adapter_main.LightAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareLight;

/**
 * Created by Say GoBay on 2016/11/28.
 * 主页-灯光模块
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
    private int DEV_ALL_ROOM = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light, container, false);
        //初始化控件
        this.inflater = inflater;
        //初始化GridView
        initGridView();
        //更新数据
        upData();
        return view;
    }

    private void initEvent() {
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 35 && MyApplication.mInstance.isDispose35())
                    //更新数据
                    upData();
                if (what == 4)
                    //更新数据
                    upData();
            }
        });
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
        //房间是"全部"时，是所有的灯
        if (room_position == DEV_ALL_ROOM) {
            light_room = lights;
        } else {
            //是其他房间时，是对应房间里的灯
            if (lights.size() > 0)
                for (int i = 0; i < lights.size(); i++) {
                    if (lights.get(i).getDev().getRoomName().equals(room_list.get(room_position))) {
                        light_room.add(lights.get(i));
                    }
                }
        }

        final List<WareLight> light_room_click = light_room;
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
        if (gridViewAdapter == null) {
            gridViewAdapter = new LightAdapter(light_room, getActivity(), inflater);
            gridView_light.setAdapter(gridViewAdapter);
        } else {
            gridViewAdapter.notifyDataSetChanged(light_room);
        }

        gridView_light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);

                    if (light_room_click.get(position).getbTuneEn() == 0) {
                        String ctlStr;
                        if (light_room_click.get(position).getbOnOff() == 0) {
                            ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                    ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                                    ",\"subType1\":0" +
                                    ",\"subType2\":0" +
                                    ",\"canCpuID\":\"" + light_room.get(position).getDev().getCanCpuId() +
                                    "\",\"devType\":" + light_room.get(position).getDev().getType() +
                                    ",\"devID\":" + light_room.get(position).getDev().getDevId() +
                                    ",\"cmd\":0" +
                                    "" +
                                    "}";
                            MyApplication.sendMsg(ctlStr);
                        } else {
                            ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                    ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                                    ",\"subType1\":0" +
                                    ",\"subType2\":0" +
                                    ",\"canCpuID\":\"" + light_room.get(position).getDev().getCanCpuId() +
                                    "\",\"devType\":" + light_room.get(position).getDev().getType() +
                                    ",\"devID\":" + light_room.get(position).getDev().getDevId() +
                                    ",\"cmd\":1" +
                                    "}";
                            MyApplication.sendMsg(ctlStr);
                        }
                    }
            }
        });
    }

    /**
     * 初始化GridView
     *
     * @param
     */
    private void initGridView() {
        initEvent();
        //房间id；
        room_position = getArguments().getInt("room_position");
        if (MyApplication.getWareData().getLights() != null && MyApplication.getWareData().getLights().size() > 1) {
            upData();
        }
    }
}
