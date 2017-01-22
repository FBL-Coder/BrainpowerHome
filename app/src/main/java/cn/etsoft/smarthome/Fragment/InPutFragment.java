package cn.etsoft.smarthome.fragment;

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
import cn.etsoft.smarthome.adapter.BoardInOutAdapter;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.ui.ParlourFourActivity;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class InPutFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ScrollView sv;
    private ListView lv;

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
        if(MyApplication.getWareData().getKeyInputs().size() > 0) {
            lv.setAdapter(new BoardInOutAdapter(getActivity(), null, MyApplication.getWareData().getKeyInputs(), UdpProPkt.E_BOARD_TYPE.e_board_keyInput.getValue()));
            lv.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ParlourFourActivity.class);
        intent.putExtra("title", MyApplication.getWareData().getKeyInputs().get(position).getBoardName());
        intent.putExtra("uid", MyApplication.getWareData().getKeyInputs().get(position).getDevUnitID());
        startActivity(intent);
    }
}
