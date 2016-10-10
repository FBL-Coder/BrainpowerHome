package cn.etsoft.smarthome.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.Adapter_Lights;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareLight;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class LamplightFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView gridView;
    private List<WareLight> wareLight;
    private ImageView light_open_all, light_close_all;
    byte[] devBuff;
    private boolean IsCanClick = false;
    private Adapter_Lights adapter_lights;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lamplight, container, false);
        //初始化控件
        initGridView(view);
        return view;
    }


    private void initGridView(View view) {
        gridView = (GridView) view.findViewById(R.id.light_gv);
        light_open_all = (ImageView) view.findViewById(R.id.light_open_all);
        light_close_all = (ImageView) view.findViewById(R.id.light_close_all);

        if (MyApplication.getWareData().getLights() != null && MyApplication.getWareData().getLights().size() > 1) {
            initEvent();
            upData();
            IsCanClick = true;
        } else {
            Toast.makeText(getActivity(), "没有找到可控制灯具", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (IsCanClick) {
            if (wareLight.get(position).getbTuneEn() == 0) {
                if (wareLight.get(position).getbOnOff() == 0) {
                } else {

                }
            }
        }
    }

    private void initEvent() {
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData() {
                adapter_lights.notifyDataSetChanged();
            }
        });
    }

    private void upData() {
        wareLight = MyApplication.getWareData().getLights();
        adapter_lights = new Adapter_Lights(MyApplication.getWareData(), getActivity());
        gridView.setAdapter(adapter_lights);
        gridView.setSelector(R.drawable.selector_gridview_item);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (IsCanClick) {
            switch (v.getId()) {
                case R.id.light_open_all:
                    byte[] data = new byte[1];
                    data[0] = 0x01;
                    break;
                case R.id.light_close_all:
                    byte[] data1 = new byte[1];
                    data1[0] = 0x00;
                    break;
            }
        }
    }
}
