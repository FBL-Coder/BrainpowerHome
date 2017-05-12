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
import cn.etsoft.smarthome.adapter.GridViewAdapter_safety;
import cn.etsoft.smarthome.domain.SetSafetyResult.SecInfoRowsBean.RunDevItemBean;

/**
 * Created by Say GoBay on 2016/11/28.
 * 高级设置——安防模块的Fragment页面
 */
public class SafetyFragment extends Fragment {
    private GridView gridView_light;
    //安防位置
    private int safety_position = 0;
    //设备
    private List<RunDevItemBean> run_dev_item;
    private GridViewAdapter_safety gridViewAdapter_safety;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        //初始化控件
        initGridView(view);
        //加载数据
        upData();
        MyApplication.mInstance.setOnGetWareDataListener_safety(
                new MyApplication.OnGetWareDataListener_safety() {
            @Override
            public void upDataWareData() {
                upData();
            }
        });
        return view;
    }

    /**
     * 初始化控件
     * @param
     */
    private void initGridView(View view) {
        gridView_light = (GridView) view.findViewById(R.id.gridView_light);
        safety_position = getArguments().getInt("safety_position");
    }

    /**
     * 加载数据
     */
    public void upData() {
        run_dev_item = MyApplication.getWareData().getResult_safety().getSec_info_rows().get(safety_position).getRun_dev_item();
//        if (gridViewAdapter_safety != null) {
//            gridViewAdapter_safety.notifyDataSetChanged(run_dev_item);
//        } else {
            if (run_dev_item == null)
                run_dev_item = new ArrayList<>();
            gridViewAdapter_safety = new GridViewAdapter_safety(run_dev_item, getActivity(),safety_position);
            gridView_light.setAdapter(gridViewAdapter_safety);
//        }
    }
}
