package cn.etsoft.smarthome.fragment_setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/11/25.
 * 远程IP
 */
public class Setting_LongIpFragment extends Fragment {
    private Activity mActivity;
    Button long_ip_save;
    EditText long_ip_num_1, long_ip_num_2, long_ip_num_3, long_ip_num_4;
    private View view;
    private SharedPreferences sharedPreferences;

    public Setting_LongIpFragment(Activity activity){
        mActivity = activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_longip, container, false);
        sharedPreferences = mActivity.getSharedPreferences("profile", Context.MODE_PRIVATE);
        initView();
        event();
        return view;
    }

    private void event() {
        long_ip_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = long_ip_num_1.getText().toString() + "." + long_ip_num_2.getText().toString() + "."
                        + long_ip_num_3.getText().toString() + "." + long_ip_num_4.getText().toString();
                GlobalVars.setDstip(ip);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("long_ip", ip);
                editor.commit();
//                MyApplication.setRcuDevIDtoLocal();
                ToastUtil.showToast(mActivity,"保存成功");
            }
        });
    }

    private void initView() {

        long_ip_save = (Button) view.findViewById(R.id.long_ip_save);
        long_ip_num_1 = (EditText) view.findViewById(R.id.long_ip_num_1);
        long_ip_num_2 = (EditText) view.findViewById(R.id.long_ip_num_2);
        long_ip_num_3 = (EditText) view.findViewById(R.id.long_ip_num_3);
        long_ip_num_4 = (EditText) view.findViewById(R.id.long_ip_num_4);

        long_ip_num_1.setFocusable(false);
        long_ip_num_1.setEnabled(false);
        long_ip_num_2.setFocusable(false);
        long_ip_num_2.setEnabled(false);
        long_ip_num_3.setFocusable(false);
        long_ip_num_3.setEnabled(false);
        long_ip_num_4.setFocusable(false);
        long_ip_num_4.setEnabled(false);

        String long_ip = sharedPreferences.getString("long_ip", "123.206.104.89");
        String num_1 = long_ip.substring(0,long_ip.indexOf("."));

        String long_ip_2 = long_ip.substring(long_ip.indexOf(".")+1);
        String num_2 = long_ip_2.substring(0, long_ip_2.indexOf("."));

        String long_ip_3 = long_ip_2.substring(long_ip.indexOf(".")+1);
        String num_3 = long_ip_3.substring(0, long_ip_3.indexOf("."));

        String num_4 = long_ip.substring(long_ip.lastIndexOf(".")+1);

        long_ip_num_1.setText(num_1);
        long_ip_num_2.setText(num_2);
        long_ip_num_3.setText(num_3);
        long_ip_num_4.setText(num_4);
    }
}
