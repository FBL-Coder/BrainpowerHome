package cn.etsoft.smarthome.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.ui.ParlourFourActivity;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class InPutFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ScrollView sv;
    private ListView lv;
    private int[] image = {R.drawable.ketingsijian, R.drawable.chufangsanjian,
            R.drawable.ketingchuanglian, R.drawable.ketingkongtiao,
            R.drawable.weishengjiansijan, R.drawable.guodaoliangjian,
            R.drawable.xiuxianquyijan, R.drawable.erzinverfangzi,
            R.drawable.erzinverfangzi,};
    private int[] hui = {R.drawable.huijiantou, R.drawable.huijiantou, R.drawable.huijiantou,
            R.drawable.huijiantou, R.drawable.huijiantou, R.drawable.huijiantou,
            R.drawable.huijiantou, R.drawable.huijiantou, R.drawable.huijiantou};
    private String[] title = {"一楼客厅四键", "厨房三键", "一楼客厅窗帘", "一楼客厅空调", "一楼卫生间四键", "二楼过道两键", "休闲区一键", "二楼儿子房四键", "二楼女儿房四键"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        //初始化ListView
        initListView(view);
        return view;
    }

    /**
     * 初始化ListView
     */
    private void initListView(View view) {
        lv = (ListView) view.findViewById(R.id.group_lv);
        sv = (ScrollView) view.findViewById(R.id.group_sv);
        sv.smoothScrollTo(0, 0);
//        lv.setAdapter(new SceneAdapter(image, title, hui, getActivity()));
//        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ParlourFourActivity.class);
        intent.putExtra("title", title[position]);
        startActivity(intent);
    }
}
