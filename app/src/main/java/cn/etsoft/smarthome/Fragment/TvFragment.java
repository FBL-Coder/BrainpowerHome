package cn.etsoft.smarthome.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareTv;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class TvFragment extends Fragment implements View.OnClickListener {

    private Button choose, one, two, three, four, five, six, seven, eight, nine, zero, hundred, last;
    private ImageView add, subtract, up, down;
    private WareTv wareTv;
    private byte[] devBuff;
    private boolean IsCanClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    private void initEvent() {

    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        if (MyApplication.getWareData().getTvs() != null && MyApplication.getWareData().getTvs().size() > 0) {
            wareTv = MyApplication.getWareData().getTvs().get(0);
            initEvent();
            IsCanClick = true;
        }else{
            Toast.makeText(getActivity(), "没有找到可控电视", Toast.LENGTH_SHORT).show();
        }
        choose = (Button) view.findViewById(R.id.tv_switch);
        one = (Button) view.findViewById(R.id.tv_one);
        two = (Button) view.findViewById(R.id.tv_two);
        three = (Button) view.findViewById(R.id.tv_three);
        four = (Button) view.findViewById(R.id.tv_four);
        five = (Button) view.findViewById(R.id.tv_five);
        six = (Button) view.findViewById(R.id.tv_six);
        seven = (Button) view.findViewById(R.id.tv_seven);
        eight = (Button) view.findViewById(R.id.tv_eight);
        nine = (Button) view.findViewById(R.id.tv_nine);
        zero = (Button) view.findViewById(R.id.tv_zero);
        hundred = (Button) view.findViewById(R.id.tv_hundred);
        last = (Button) view.findViewById(R.id.tv_last);
        choose.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        hundred.setOnClickListener(this);
        last.setOnClickListener(this);
        add = (ImageView) view.findViewById(R.id.tv_jia);
        subtract = (ImageView) view.findViewById(R.id.tv_jian);
        up = (ImageView) view.findViewById(R.id.tv_shang);
        down = (ImageView) view.findViewById(R.id.tv_xia);
        add.setOnClickListener(this);
        subtract.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (IsCanClick) {
            switch (v.getId()) {
                case R.id.tv_switch:

                    break;
                case R.id.tv_one:

                    break;
                case R.id.tv_two:

                    break;
                case R.id.tv_three:

                    break;
                case R.id.tv_four:

                    break;
                case R.id.tv_five:

                    break;
                case R.id.tv_six:

                    break;
                case R.id.tv_seven:

                    break;
                case R.id.tv_eight:

                    break;
                case R.id.tv_nine:

                    break;
                case R.id.tv_zero:

                    break;
                case R.id.tv_hundred:
                    break;
                case R.id.tv_last:
                    break;
                case R.id.tv_jia:

                    break;
                case R.id.tv_jian:

                    break;
                case R.id.tv_shang:

                    break;
                case R.id.tv_xia:

                    break;
            }
        }
    }
}
