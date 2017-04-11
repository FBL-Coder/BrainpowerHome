package cn.etsoft.smarthome.fragment_scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.DoorAdapter;

/**
 * Created by Say GoBay on 2016/11/28.
 * 门锁模块
 */
public class Main_DoorFragment extends Fragment {
    private GridView gridView;
    private int[] apploance = {R.drawable.lock};
    private String[] title = {"门锁控制器"};
    private int[] on = {R.drawable.off};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_socket,container,false);
        //初始化控件
        initGridView(view);
        return view;
    }
    /**
     * 初始化控件
     * @param view
     */
    private void initGridView(View view) {
        gridView = (GridView) view.findViewById(R.id.gridView);
        DoorAdapter gridViewAdapter = new DoorAdapter(apploance,title,on,getActivity());
        gridView.setAdapter(gridViewAdapter);
    }
}
