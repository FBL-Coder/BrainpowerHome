package cn.semtec.community2.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.etsoft.smarthome.R;

public class RepairsFragment extends Fragment {
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_repairs, null);
        return layout;
    }
}
