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
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;

/**
 * Created by Say GoBay on 2016/8/22.
 */
public class STBFragment extends Fragment implements View.OnClickListener {

    private Button choose, chooseTv, one, two, three, four, five, six, seven, eight, nine, zero, hundred, last;
    private ImageView add, subtract, up, down;
    private WareSetBox wareSetBox;
    private byte[] devBuff;
    private boolean IsCanClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stb, container, false);
        //初始化控件
        initView(view);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        if (MyApplication.getWareData().getStbs() != null && MyApplication.getWareData().getStbs().size() > 0) {
            wareSetBox = MyApplication.getWareData().getStbs().get(0);
            initEvent();
            IsCanClick = true;
        } else {
            Toast.makeText(getActivity(), "没有找到可控机顶盒", Toast.LENGTH_SHORT).show();
        }
        choose = (Button) view.findViewById(R.id.stb_switch);
        chooseTv = (Button) view.findViewById(R.id.stb_switch);
        one = (Button) view.findViewById(R.id.stb_one);
        two = (Button) view.findViewById(R.id.stb_two);
        three = (Button) view.findViewById(R.id.stb_three);
        four = (Button) view.findViewById(R.id.stb_four);
        five = (Button) view.findViewById(R.id.stb_five);
        six = (Button) view.findViewById(R.id.stb_six);
        seven = (Button) view.findViewById(R.id.stb_seven);
        eight = (Button) view.findViewById(R.id.stb_eight);
        nine = (Button) view.findViewById(R.id.stb_nine);
        zero = (Button) view.findViewById(R.id.stb_zero);
        hundred = (Button) view.findViewById(R.id.stb_hundred);
        last = (Button) view.findViewById(R.id.stb_last);
        choose.setOnClickListener(this);
        chooseTv.setOnClickListener(this);
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
        add = (ImageView) view.findViewById(R.id.stb_jia);
        subtract = (ImageView) view.findViewById(R.id.stb_jian);
        up = (ImageView) view.findViewById(R.id.stb_shang);
        down = (ImageView) view.findViewById(R.id.stb_xia);
        add.setOnClickListener(this);
        subtract.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
    }

    private void initEvent() {
    }

    @Override
    public void onClick(View v) {
        if (IsCanClick) {

            String str_Fixed = "{\"devUnitID\":\"37ffdb05424e323416702443\"" +
                    ",\"datType\":4" +
                    ",\"subType1\":0" +
                    ",\"subType2\":0" +
                    ",\"canCpuID\":" + MyApplication.getWareData().getStbs().get(0).getDev().getCanCpuId() +
                    ".\"devType\":" + MyApplication.getWareData().getStbs().get(0).getDev().getType() +
                    ".\"devID\":" + MyApplication.getWareData().getStbs().get(0).getDev().getDevId();
            int Value = -1;
            switch (v.getId()) {
                case R.id.stb_switch_tv:
                    Value = UdpProPkt.E_TV_CMD.e_tv_offOn.getValue();
                    break;
                case R.id.stb_switch:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_offOn.getValue();
                    break;
                case R.id.stb_one:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num1.getValue();
                    break;
                case R.id.stb_two:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num2.getValue();
                    break;
                case R.id.stb_three:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num3.getValue();
                    break;
                case R.id.stb_four:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num4.getValue();
                    break;
                case R.id.stb_five:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num5.getValue();
                    break;
                case R.id.stb_six:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num6.getValue();
                    break;
                case R.id.stb_seven:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num7.getValue();
                    break;
                case R.id.stb_eight:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num8.getValue();
                    break;
                case R.id.stb_nine:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num9.getValue();
                    break;
                case R.id.stb_zero:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_num0.getValue();
                    break;
                case R.id.stb_hundred:
                    break;
                case R.id.stb_last:
                    break;
                case R.id.stb_jia:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_numVInc.getValue();
                    break;
                case R.id.stb_jian:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_numLf.getValue();
                    break;
                case R.id.stb_shang:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_numUp.getValue();
                    break;
                case R.id.stb_xia:
                    Value = UdpProPkt.E_TVUP_CMD.e_tvUP_numDn.getValue();
                    break;
            }
            if (Value != -1) {
                str_Fixed = str_Fixed +
                        ".\"cmd:" + Value + "}";
                CommonUtils.sendMsg(str_Fixed);
            }
        }
    }
}
