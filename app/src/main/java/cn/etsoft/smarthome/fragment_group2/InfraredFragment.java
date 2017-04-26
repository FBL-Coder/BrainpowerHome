package cn.etsoft.smarthome.fragment_group2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.etsoft.smarthome.R;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class InfraredFragment extends Fragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_output, container, false);
        return view;
    }

}
