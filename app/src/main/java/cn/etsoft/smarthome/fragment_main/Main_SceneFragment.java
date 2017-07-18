package cn.etsoft.smarthome.fragment_main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_main.SceneAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;

/**
 * Created by Say GoBay on 2016/11/28.
 * 主页-情景模块
 */
public class Main_SceneFragment extends Fragment {

    private GridView gridView_light;
    private LayoutInflater inflater;
    private SceneAdapter sceneAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        view.setBackgroundResource(R.drawable.bj_group3);
        this.inflater = inflater;
        //初始化GridView
        initGridView(view);
        return view;
    }

    /**
     * 初始化GridView
     *
     * @param view
     */
    long TimeExit = 0;

    private void initGridView(View view) {
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);

        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 22) {
                    if (MyApplication.getWareData().getSceneEvents() != null
                            && MyApplication.getWareData().getSceneEvents().size() > 0) {
                        sceneAdapter = new SceneAdapter(MyApplication.getWareData().getSceneEvents(), getActivity(), inflater);
                        gridView_light.setAdapter(sceneAdapter);
                    } else {
                        Toast.makeText(getActivity(), "没有找到情景模式", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (MyApplication.getWareData().getSceneEvents() != null && MyApplication.getWareData().getSceneEvents().size() > 0) {
            sceneAdapter = new SceneAdapter(MyApplication.getWareData().getSceneEvents(), getActivity(), inflater);
            gridView_light.setAdapter(sceneAdapter);
        } else {
            Toast.makeText(getActivity(), "没有找到情景模式", Toast.LENGTH_SHORT).show();
        }
        gridView_light.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (System.currentTimeMillis() - TimeExit > 500) {
                    MyApplication.mInstance.getSp().play(MyApplication.mInstance.getMusic(), 1, 1, 0, 0, 1);
                    TimeExit = System.currentTimeMillis();

                    String exec_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                            ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_exeSceneEvents.getValue() +
                            ",\"subType1\":0" +
                            ",\"subType2\":0" +
                            ",\"eventId\":" + MyApplication.getWareData().getSceneEvents().get(position).getEventld() + "}";
                    MyApplication.sendMsg(exec_str);
                    MyApplication.mInstance.setDispose35(true);
                }
            }
        });
    }



    @Override
    public void onStop() {
        super.onStop();
    }
}