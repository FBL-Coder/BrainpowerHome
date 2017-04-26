package cn.etsoft.smarthome.fragment_setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.utils.ToastUtil;

/**
 * Created by Say GoBay on 2016/11/25.
 * 联网模块详情
 */
public class Setting_ModuleDetailFragment extends Fragment implements View.OnClickListener{

    TextView devUnitID, roomNum, macAddr, tvsave;
    private int id;
    private EditText name, devUnitPass, IpAddr, SubMask, GateWay, centerServ;
    private RcuInfo rcuinfo;

    private RadioGroup group;
    private RadioButton yes, no;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.work_set_activity, container, false);
        initView();
        return view;
    }

    int bDhcp = -1;

    private void initView() {

        if(MyApplication.getWareData().getRcuInfos() == null || MyApplication.getWareData().getRcuInfos().size() == 0){
            ToastUtil.showToast(getActivity(),"没有数据！");
            return;
        }
        rcuinfo =  MyApplication.mInstance.getRcuInfo();
        bDhcp = rcuinfo.getbDhcp();

        devUnitID = (TextView) view.findViewById(R.id.work_devUnitID);
        devUnitPass = (EditText) view.findViewById(R.id.work_devUnitPass);
        name = (EditText) view.findViewById(R.id.work_name);
        IpAddr = (EditText) view.findViewById(R.id.work_IpAddr);
        SubMask = (EditText) view.findViewById(R.id.work_SubMask);
        GateWay = (EditText) view.findViewById(R.id.work_GateWay);
        centerServ = (EditText) view.findViewById(R.id.work_centerServ);

        roomNum = (TextView) view.findViewById(R.id.work_roomNum);
        macAddr = (TextView) view.findViewById(R.id.work_macAddr);
        tvsave = (TextView) view.findViewById(R.id.work_save);

        group = (RadioGroup) view.findViewById(R.id.y_n_Group);
        yes = (RadioButton) view.findViewById(R.id.work_yes);
        no = (RadioButton) view.findViewById(R.id.work_no);

        tvsave.setOnClickListener(this);

        devUnitID.setText(rcuinfo.getDevUnitID());
        devUnitPass.setText(rcuinfo.getDevUnitPass().substring(0,8));
        name.setText(rcuinfo.getName());
        IpAddr.setText(rcuinfo.getIpAddr());
        SubMask.setText(rcuinfo.getSubMask());
        GateWay.setText(rcuinfo.getGateWay());
        centerServ.setText(rcuinfo.getCenterServ());
        roomNum.setText(rcuinfo.getRoomNum());
        macAddr.setText(rcuinfo.getMacAddr());

        if (rcuinfo.getbDhcp() == 0) {
            no.setChecked(true);
        } else {
            yes.setChecked(true);
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.work_yes:
                        bDhcp = 1;
                        break;
                    case R.id.work_no:
                        bDhcp = 0;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_save:

//            {
//                "devUnitID": "37ffdb05424e323416702443",
//                    "datType": 1,
//                    "subType1": 0,
//                    "subType2": 0,
//                    "canCpuID": "37ffdb05424e323416702443",
//                    "devUnitPass": "16072443",
//                    "name": "6666",
//                    "IpAddr": "192.168.0.102",
//                    "SubMask": "255.255.255.0",
//                    "Gateway": "192.168.0.1",
//                    "centerServ": "192.168.1.114",
//                    "roomNum": "0000",
//                    "macAddr": "00502a040248",
//                    "SoftVersion": 0,
//                    "HwVersion": 0,
//                    "bDhcp": 0
//            }
                String newname = name.getText().toString();
                String newpass = devUnitPass.getText().toString();
                String newip = IpAddr.getText().toString();
                String newSubmask = SubMask.getText().toString();
                String newGateway = GateWay.getText().toString();
                String newcenterServ = centerServ.getText().toString();

                rcuinfo.setName(newname);
                rcuinfo.setDevUnitPass(newpass);
                rcuinfo.setIpAddr(newip);
                rcuinfo.setSubMask(newSubmask);
                rcuinfo.setGateWay(newGateway);
                rcuinfo.setCenterServ(newcenterServ);
                rcuinfo.setbDhcp(bDhcp);

                MyApplication.getWareData().getRcuInfos().set(id, rcuinfo);

                final String chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                        "\"datType\":" + 1 + "," +
                        "\"subType1\":0," +
                        "\"subType2\":0," +
                        "\"canCpuID\":\"" + rcuinfo.getDevUnitID() + "\"," +
                        "\"devUnitPass\":\"" + newpass + "\"," +
                        "\"name\":" + "\"" + Sutf2Sgbk(newname) + "\"," +
                        "\"IpAddr\":" + "\"" + newip + "\"," +
                        "\"SubMask\":" + "\"" + newSubmask + "\"," +
                        "\"Gateway\":" + "\"" + newGateway + "\"," +
                        "\"centerServ\":" + "\"" + newcenterServ + "\"," +
                        "\"roomNum\":" + "\"" + rcuinfo.getRoomNum() + "\"," +
                        "\"macAddr\":" + "\"" + rcuinfo.getMacAddr() + "\"," +
                        "\"SoftVersion\":" + 0 + "," +
                        "\"HwVersion\":" + 0 + "," +
                        "\"bDhcp\":" + bDhcp + "}";

                MyApplication.sendMsg(chn_str);
                break;
        }
    }

    public String Sutf2Sgbk(String string) {

        byte[] data = {0};
        try {
            data = string.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);
        return str_gb;
    }
}
