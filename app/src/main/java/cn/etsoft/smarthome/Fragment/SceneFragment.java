package cn.etsoft.smarthome.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.Adapter_Scene;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class SceneFragment extends Fragment {
    private GridView gridView;
    private boolean IsCanClick = false;
    private WareData wareData;
    private int OUTTIME_DOWNLOAD = 1111;
    private List<WareSceneEvent> mSceneEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initEvent();

        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        gridView = (GridView) view.findViewById(R.id.scene_gv);
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData() {
                if (MyApplication.getWareData().getSceneEvents() != null && MyApplication.getWareData().getSceneEvents().size() > 0) {
                    initEvent();
                    IsCanClick = true;
                    gridView.setAdapter(new Adapter_Scene(getListData(), getActivity()));
                    gridView.setSelector(R.drawable.selector_gridview_item);
                } else {
                    Toast.makeText(getActivity(), "没有找到情景模式", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (MyApplication.getWareData().getSceneEvents() != null && MyApplication.getWareData().getSceneEvents().size() > 0) {
            initEvent();
            IsCanClick = true;
            gridView.setAdapter(new Adapter_Scene(getListData(), getActivity()));
            gridView.setSelector(R.drawable.selector_gridview_item);
        } else {
            Toast.makeText(getActivity(), "没有找到情景模式", Toast.LENGTH_SHORT).show();
        }
    }

    private void initEvent() {
        wareData = MyApplication.getWareData();
    }

    private List<WareSceneEvent> getListData() {
        mSceneEvents = wareData.getSceneEvents();
        return mSceneEvents;
    }
}
