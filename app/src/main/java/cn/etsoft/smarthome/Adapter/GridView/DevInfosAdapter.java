package cn.etsoft.smarthome.Adapter.GridView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareBoardChnout;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.PopupWindow.MultiChoicePopWindow;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Author：FBL  Time： 2017/6/29.
 * 设备详情  设备适配器(注.代码量较大,修改需谨慎)
 */

public class DevInfosAdapter extends BaseAdapter {

    private List<WareDev> Devs;
    private Activity mContext;
    private PopupWindow popupWindow;
    private List<String> RoomNames;
    private MultiChoicePopWindow mMultiChoicePopWindow;
    private AlertDialog.Builder builder;
    private List<String> mBoards;
    private String DEVS_ALL_ROOM = "全部";

    public DevInfosAdapter(List<WareDev> list, Activity context) {
        RoomNames = MyApplication.getWareData().getRooms();
        Devs = list;
        mBoards = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getBoardChnouts().size(); i++) {
            mBoards.add(MyApplication.getWareData().getBoardChnouts().get(i).getBoardName());
        }
        mContext = context;
    }

    public void notifyDataSetChanged(List<WareDev> list) {
        RoomNames = MyApplication.getWareData().getRooms();
        Devs = list;
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (Devs == null)
            return 0;
        return Devs.size();
    }

    @Override
    public Object getItem(int position) {
        if (Devs == null)
            return null;
        return Devs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.girdview_devinfoitem, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();
        //首先隐藏部分布局
        viewHolder.mDevInfoLook.setVisibility(View.VISIBLE);
        viewHolder.mDevInfoEditLook.setVisibility(View.GONE);
        viewHolder.mDevInfoEdit.setImageResource(R.drawable.edit_dev);

        final ViewHolder finalViewHolder = viewHolder;
        ShowView(position, viewHolder);
        EditClick(viewHolder, finalViewHolder, position);
        EditRoomClick(finalViewHolder);
        EditWayClick(position, finalViewHolder);
        DeleteClick(finalViewHolder, position);
        return convertView;
    }

    /**
     * 删除设备按钮
     */
    private void DeleteClick(final ViewHolder finalViewHolder, final int position) {
        finalViewHolder.mDevInfoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.mDevInfoLook.getVisibility() == View.VISIBLE) {
                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage("是否要删除此设备？");
                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 删除设备
                            try {
                                SendDataUtil.deleteDev(Devs.get(position));
                                MyApplication.mApplication.showLoadDialog(mContext);
                            } catch (Exception e) {
                                Log.e(TAG, "DeleteClick: " + e);
                                return;
                            }
                        }
                    });
                    builder.setNegativeButton("不要", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    finalViewHolder.mDevInfoLook.setVisibility(View.VISIBLE);
                    finalViewHolder.mDevInfoEditLook.setVisibility(View.GONE);
                    finalViewHolder.mDevInfoDelete.setImageResource(R.drawable.delete_edit_dev);
                    finalViewHolder.mDevInfoEdit.setImageResource(R.drawable.edit_dev);
                }
            }
        });
    }

    /**
     * 切换通道点击
     */
    private void EditWayClick(final int position, final ViewHolder finalViewHolder) {
        finalViewHolder.mDevInfoEditWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> list_voard_cancpuid = new ArrayList<>();

                for (int z = 0; z < MyApplication.getWareData().getDevs().size(); z++) {
                    WareDev dev = MyApplication.getWareData().getDevs().get(z);
                    if (!(dev.getType() == Devs.get(position).getType()
                            && dev.getDevId() == Devs.get(position).getDevId()
                            && dev.getCanCpuId().equals(Devs.get(position).getCanCpuId()))) {
                        if (dev.getType() == 0) {
                            for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                                WareAirCondDev airCondDev = MyApplication.getWareData().getAirConds().get(i);
                                if (dev.getDevId() == airCondDev.getDev().getDevId()
                                        && dev.getCanCpuId().equals(airCondDev.getDev().getCanCpuId())) {
                                    int PowChn = airCondDev.getPowChn();
                                    String PowChnList = Integer.toBinaryString(PowChn);
                                    PowChnList = new StringBuffer(PowChnList).reverse().toString();
                                    List<Integer> index_list = new ArrayList<>();
                                    for (int j = 0; j < PowChnList.length(); j++) {
                                        if (PowChnList.charAt(j) == '1') {
                                            index_list.add(j + 1);
                                        }
                                    }
                                    list_voard_cancpuid.addAll(index_list);
                                }
                            }
                        } else if (dev.getType() == 3) {
                            for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                                WareLight light = MyApplication.getWareData().getLights().get(i);
                                if (dev.getDevId() == light.getDev().getDevId()
                                        && dev.getCanCpuId().equals(light.getDev().getCanCpuId())) {
                                    int PowChn = light.getPowChn() + 1;
                                    list_voard_cancpuid.add(PowChn);
                                }
                            }
                        } else if (dev.getType() == 4) {
                            for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                                WareCurtain curtain = MyApplication.getWareData().getCurtains().get(i);
                                if (dev.getDevId() == curtain.getDev().getDevId()
                                        && dev.getCanCpuId().equals(curtain.getDev().getCanCpuId())) {
                                    int PowChn = curtain.getPowChn();
                                    String PowChnList = Integer.toBinaryString(PowChn);
                                    PowChnList = new StringBuffer(PowChnList).reverse().toString();
                                    List<Integer> index_list = new ArrayList<>();
                                    for (int j = 0; j < PowChnList.length(); j++) {
                                        if (PowChnList.charAt(j) == '1') {
                                            index_list.add(j + 1);
                                        }
                                    }
                                    list_voard_cancpuid.addAll(index_list);
                                }
                            }
                        } else if (dev.getType() == 7) {
                            for (int i = 0; i < MyApplication.getWareData().getFreshAirs().size(); i++) {
                                WareFreshAir freshAir = MyApplication.getWareData().getFreshAirs().get(i);
                                if (dev.getDevId() == freshAir.getDev().getDevId()
                                        && dev.getCanCpuId().equals(freshAir.getDev().getCanCpuId())) {
                                    list_voard_cancpuid.add(freshAir.getOnOffChn() + 1);
                                    list_voard_cancpuid.add(freshAir.getSpdHighChn() + 1);
                                    list_voard_cancpuid.add(freshAir.getSpdLowChn() + 1);
                                    list_voard_cancpuid.add(freshAir.getSpdMidChn() + 1);
                                }
                            }
                        } else if (dev.getType() == 9) {
                            for (int i = 0; i < MyApplication.getWareData().getFloorHeat().size(); i++) {
                                WareFloorHeat floorHeat = MyApplication.getWareData().getFloorHeat().get(i);
                                if (dev.getDevId() == floorHeat.getDev().getDevId()
                                        && dev.getCanCpuId().equals(floorHeat.getDev().getCanCpuId())) {
                                    int PowChn = floorHeat.getPowChn() + 1;
                                    list_voard_cancpuid.add(PowChn);
                                }
                            }
                        }
                    }
                }

                List<Integer> list_channel = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    list_channel.add(i);
                }

                for (int i = 0; i < list_voard_cancpuid.size(); i++) {
                    for (int j = 0; j < list_channel.size(); j++) {
                        if (list_channel.get(j) == list_voard_cancpuid.get(i)) {
                            list_channel.remove(j);
                        }
                    }
                }
                boolean[] isSelect = new boolean[list_channel.size()];
                if (list_channel.size() == 0) {
                    ToastUtil.showText("没有可用通道");
                } else {
//                        initPopupWindow_channel(v, list_channel);
//                        popupWindow.showAsDropDown(v, 0, 0);
                    initWayDialog(mContext, finalViewHolder.mDevInfoEditWay, list_channel, isSelect);
                }
            }
        });
    }


    /**
     * 切换房间名
     */
    private void EditRoomClick(ViewHolder finalViewHolder) {
        finalViewHolder.mDevInfoEditRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRadioPopupWindow(v, RoomNames);
                popupWindow.showAsDropDown(v, 0, 0);
            }
        });
    }

    /**
     * 编辑设备信息按钮
     */
    private void EditClick(ViewHolder viewHolder, final ViewHolder finalViewHolder, final int position) {
        viewHolder.mDevInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.mDevInfoLook.getVisibility() == View.VISIBLE) {
                    finalViewHolder.mDevInfoLook.setVisibility(View.GONE);
                    finalViewHolder.mDevInfoEditLook.setVisibility(View.VISIBLE);
                    finalViewHolder.mDevInfoEdit.setImageResource(R.drawable.save_edit_dev);
                    finalViewHolder.mDevInfoDelete.setImageResource(R.drawable.back_edit_dev);
                } else {
//                    finalViewHolder.mDevInfoLook.setVisibility(View.VISIBLE);
//                    finalViewHolder.mDevInfoEditLook.setVisibility(View.GONE);
//                    finalViewHolder.mDevInfoEdit.setImageResource(R.drawable.ic_launcher_round);

                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("保存");
                    builder.setMessage("是否保存设置？");
                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // 保存修改
                            String Save_DevName;
                            String Save_Roomname;
                            String[] WayStr_ok = null;
                            int Save_DevWay;
                            try {
                                Save_DevWay = Devs.get(position).getPowChn();
                            } catch (Exception e) {
                                Save_DevWay = 0;
                            }
                            //设备名数据处理
                            Save_DevName = finalViewHolder.mDevInfoEditName.getText().toString();
                            if ("".equals(Save_DevName))
                                Save_DevName = finalViewHolder.mDevInfoEditName.getHint().toString();
                            if (Save_DevName.length() > 6) {
                                ToastUtil.showText("设备名称过长");
                                return;
                            }
                            if ("".equals(Save_DevName)) {
                                ToastUtil.showText("请输入设备名称");
                                return;
                            }
                            try {
                                Save_DevName = CommonUtils.bytesToHexString(Save_DevName.getBytes("GB2312"));
                            } catch (UnsupportedEncodingException e) {
                                ToastUtil.showText("设备名称不合适");
                                return;
                            }
                            //房间名数据处理
                            Save_Roomname = finalViewHolder.mDevInfoEditRoom.getText().toString();
                            try {
                                Save_Roomname = CommonUtils.bytesToHexString(Save_Roomname.getBytes("GB2312"));
                            } catch (UnsupportedEncodingException e) {
                                ToastUtil.showText("房间名称不合适");
                                return;
                            }

                            if (Devs.get(position).getType() == 0) {
                                for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                                    if (MyApplication.getWareData().getAirConds().get(i).getDev().getDevId()
                                            == Devs.get(position).getDevId()
                                            && MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId()
                                            .equals(Devs.get(position).getCanCpuId())) {
                                        //设备通道 保存数据处理
                                        String Way_Str = finalViewHolder.mDevInfoEditWay.getText().toString();
                                        String[] WayStr_ok_air = Way_Str.split("、");
                                        if (WayStr_ok_air.length == 0) {
                                            ToastUtil.showText("请选择通道");
                                            return;
                                        } else {
                                            if (WayStr_ok_air.length != 5) {//135
                                                ToastUtil.showText("空调是5个通道");
                                                return;
                                            }
                                            String Way = "";
                                            for (int j = 0; j < 12; j++) {
                                                boolean IsEnter = false;
                                                for (int k = 0; k < WayStr_ok_air.length; k++) {
                                                    if (j == Integer.parseInt(WayStr_ok_air[k]) - 1) {
                                                        Way += "1";
                                                        IsEnter = true;
                                                    }
                                                }
                                                if (!IsEnter) {
                                                    Way += "0";
                                                }
                                            }
                                            Save_DevWay = Integer.parseInt(new StringBuffer(Way).reverse().toString(), 2);
                                        }
                                    }
                                }
                            } else if (Devs.get(position).getType() == 3) {
                                //设备通道 保存数据处理
                                String Way_Str = finalViewHolder.mDevInfoEditWay.getText().toString();
                                if (Way_Str.length() == 0) {
                                    ToastUtil.showText("请选择通道");
                                    return;
                                } else if (Way_Str.contains("、")) {
                                    ToastUtil.showText("灯光只能有一个通道");
                                    return;
                                }
                                Save_DevWay = Integer.parseInt(Way_Str) - 1;
                            } else if (Devs.get(position).getType() == 4) {
                                //设备通道 保存数据处理
                                String Way_Str = finalViewHolder.mDevInfoEditWay.getText().toString();
                                String[] WayStr_ok_air = Way_Str.split("、");
                                if (WayStr_ok_air.length == 0) {
                                    ToastUtil.showText("请选择通道");
                                    return;
                                } else {
                                    if (WayStr_ok_air.length != 2) {//135
                                        ToastUtil.showText("窗帘是2个通道");
                                        return;
                                    }
                                    String Way = "";
                                    for (int j = 0; j < 12; j++) {
                                        boolean IsEnter = false;
                                        for (int k = 0; k < WayStr_ok_air.length; k++) {
                                            if (j == Integer.parseInt(WayStr_ok_air[k]) - 1) {
                                                Way += "1";
                                                IsEnter = true;
                                            }
                                        }
                                        if (!IsEnter) {
                                            Way += "0";
                                        }
                                    }
                                    Save_DevWay = Integer.parseInt(new StringBuffer(Way).reverse().toString(), 2);
                                }
                            } else if (Devs.get(position).getType() == 7) {
                                //设备通道 保存数据处理
                                String Way_Str = finalViewHolder.mDevInfoEditWay.getText().toString();
                                WayStr_ok = Way_Str.split("、");
                                if (WayStr_ok.length != 4) {
                                    ToastUtil.showText("新风是4个通道");
                                    return;
                                }
                            } else if (Devs.get(position).getType() == 9) {
                                //设备通道 保存数据处理
                                String Way_Str = finalViewHolder.mDevInfoEditWay.getText().toString();
                                if (Way_Str.length() == 0) {
                                    ToastUtil.showText("请选择通道");
                                    return;
                                } else if (Way_Str.contains("、")) {
                                    ToastUtil.showText("地暖只能有一个通道");
                                    return;
                                }
                                Save_DevWay = Integer.parseInt(Way_Str) - 1;
                            }
                            String chn_str = "";
                            if (Devs.get(position).getType() == 7)
                                for (int i = 0; i < MyApplication.getWareData().getFreshAirs().size(); i++) {

                                    WareFreshAir freshAir = MyApplication.getWareData().getFreshAirs().get(i);

                                    if (Devs.get(position).getCanCpuId().equals(freshAir.getDev().getCanCpuId())
                                            && Devs.get(position).getType() == freshAir.getDev().getType()
                                            && Devs.get(position).getDevId() == freshAir.getDev().getDevId()) {
                                        chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                                "\"datType\":" + 6 + "," +
                                                "\"subType1\":0," +
                                                "\"subType2\":0," +
                                                "\"canCpuID\":\"" + freshAir.getDev().getCanCpuId() + "\"," +
                                                "\"devType\":" + freshAir.getDev().getType() + "," +
                                                "\"devID\":" + freshAir.getDev().getDevId() + "," +
                                                "\"devName\":" + "\"" + Save_DevName + "\"," +
                                                "\"roomName\":" + "\"" + Save_Roomname + "\"," +
                                                "\"spdLowChn\":" + (Integer.parseInt(WayStr_ok[1]) - 1) + "," +
                                                "\"spdMidChn\":" + (Integer.parseInt(WayStr_ok[2]) - 1) + "," +
                                                "\"spdHighChn\":" + (Integer.parseInt(WayStr_ok[3]) - 1) + "," +
                                                "\"autoRun\":" + freshAir.getValPm10() + "," +
                                                "\"valPm10\":" + 0 + "," +
                                                "\"valPm25\":" + 0 + "," +
                                                "\"cmd\":" + 1 + "," +
                                                "\"powChn\":" + (Integer.parseInt(WayStr_ok[0]) - 1) + "}";

                                    }
                                }
                            else if (Devs.get(position).getType() == 9)
                                for (int i = 0; i < MyApplication.getWareData().getFloorHeat().size(); i++) {
                                    WareFloorHeat floorHeat = MyApplication.getWareData().getFloorHeat().get(i);

                                    if (Devs.get(position).getType() == floorHeat.getDev().getType()
                                            && Devs.get(position).getDevId() == floorHeat.getDev().getDevId()
                                            && Devs.get(position).getCanCpuId().equals(floorHeat.getDev().getCanCpuId()))
                                        chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                                "\"datType\":" + 6 + "," +
                                                "\"subType1\":0," +
                                                "\"subType2\":0," +
                                                "\"canCpuID\":\"" + Devs.get(position).getCanCpuId() + "\"," +
                                                "\"devType\":" + Devs.get(position).getType() + "," +
                                                "\"devID\":" + Devs.get(position).getDevId() + "," +
                                                "\"devName\":" + "\"" + Save_DevName + "\"," +
                                                "\"roomName\":" + "\"" + Save_Roomname + "\"," +
                                                "\"bOnOff\":" + floorHeat.getDev().getbOnOff() + "," +
                                                "\"tempget\":" + floorHeat.getTempget() + "," +
                                                "\"tempset\":" + floorHeat.getTempset() + "," +
                                                "\"autoRun\":" + floorHeat.getAutoRun() + "," +
                                                "\"cmd\":" + 1 + "," +
                                                "\"powChn\":" + Save_DevWay + "}";
                                }
                            else
                                chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                        "\"datType\":" + 6 + "," +
                                        "\"subType1\":0," +
                                        "\"subType2\":0," +
                                        "\"canCpuID\":\"" + Devs.get(position).getCanCpuId() + "\"," +
                                        "\"devType\":" + Devs.get(position).getType() + "," +
                                        "\"devID\":" + Devs.get(position).getDevId() + "," +
                                        "\"devName\":" + "\"" + Save_DevName + "\"," +
                                        "\"roomName\":" + "\"" + Save_Roomname + "\"," +
                                        "\"powChn\":" + Save_DevWay + "," +
                                        "\"cmd\":" + 1 + "}";
//                            Log.i(TAG, "onClick: " + chn_str);
                            MyApplication.mApplication.getUdpServer().send(chn_str, 6);
                            MyApplication.mApplication.showLoadDialog(mContext);
                            finalViewHolder.mDevInfoDelete.setImageResource(R.drawable.delete_edit_dev);
                        }
                    });
                    builder.setNegativeButton("不要", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finalViewHolder.mDevInfoLook.setVisibility(View.VISIBLE);
                            finalViewHolder.mDevInfoEditLook.setVisibility(View.GONE);
                            finalViewHolder.mDevInfoEdit.setImageResource(R.drawable.edit_dev);
                            finalViewHolder.mDevInfoDelete.setImageResource(R.drawable.delete_edit_dev);
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    /**
     * item显示
     */
    private void ShowView(int position, final ViewHolder viewHolder) {
        viewHolder.mDevInfoLook.setVisibility(View.VISIBLE);
        viewHolder.mDevInfoEditLook.setVisibility(View.GONE);
        viewHolder.mDevInfoEdit.setImageResource(R.drawable.edit_dev);
        viewHolder.mDevInfoDelete.setImageResource(R.drawable.delete_edit_dev);
        if (Devs.get(position).getType() == 0) {
            List<WareAirCondDev> Airs = MyApplication.getWareData().getAirConds();
            for (int i = 0; i < Airs.size(); i++) {
                if (Devs.get(position).getCanCpuId().equals(Airs.get(i).getDev().getCanCpuId())
                        && Devs.get(position).getDevId() == Airs.get(i).getDev().getDevId() &&
                        Devs.get(position).getType() == Airs.get(i).getDev().getType()) {
                    WareAirCondDev Air = Airs.get(i);
                    String BoardName = "";
                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(j);
                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
                            BoardName = chnout.getBoardName();
                        }
                    }
                    //可视布局数据
                    viewHolder.mDevInfoName.setText(Air.getDev().getDevName());
                    viewHolder.mDevInfoRoom.setText(Air.getDev().getRoomName());
                    if (Air.getbOnOff() == 0) {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.kt_dev_item_close);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.kt_dev_item_close);
                    } else {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.kt_dev_item_open);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.kt_dev_item_open);
                    }
                    final WareAirCondDev airCondDev = Air;

                    viewHolder.mDevInfoTypeIVTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int cmdValue = 0;
                            if (airCondDev.getbOnOff() == 0) {
                                cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                            } else {
                                cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                            }
                            int value = (0 << 5) | cmdValue;
                            SendDataUtil.controlDev(airCondDev.getDev(), value);
                        }
                    });
                    int Way_num = MyApplication.getWareData().getAirConds().get(i).getPowChn();
                    String Way_str = new StringBuffer(Integer.toBinaryString(Way_num)).reverse().toString();
                    String Way_ok = "";
                    for (int j = 0; j < Way_str.length(); j++) {
                        if (Way_str.charAt(j) == '1') {
                            Way_ok += j + 1 + "、";
                        }
                    }
                    if (!"".equals(Way_ok))
                        Way_ok = Way_ok.substring(0, Way_ok.lastIndexOf("、"));
                    viewHolder.mDevInfoWay.setText(Way_ok);
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    viewHolder.mDevInfoEditRoom.setText(Air.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(Air.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(Way_ok);
                }
            }
        }
//        else if (Devs.get(position).getType() == 1) {
//            List<WareTv> tvs = MyApplication.getWareData().getTvs();
//            for (int i = 0; i < tvs.size(); i++) {
//                if (Devs.get(position).getCanCpuId().equals(tvs.get(i).getDev().getCanCpuId())
//                        && Devs.get(position).getDevId() == tvs.get(i).getDev().getDevId() &&
//                        Devs.get(position).getType() == tvs.get(i).getDev().getType()) {
//                    WareTv tv = tvs.get(i);
//                    String BoardName = "";
//                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
//                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(i);
//                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
//                            BoardName = chnout.getBoardName();
//                        }
//                    }
//                    //可视布局数据
//                    viewHolder.mDevInfoName.setText(tv.getDev().getDevName());
//                    viewHolder.mDevInfoRoom.setText(tv.getDev().getRoomName());
//                    viewHolder.mDevInfoTypeIV.setText("电视");
//                    viewHolder.mDevInfoWay.setText("设备不支持");
//                    if ("".equals(BoardName))
//                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
//                    viewHolder.mDevInfoOutBoard.setText(BoardName);
//
//                    //不可视布局数据
//                    viewHolder.mDevInfoEditEditRoom.setHint(tv.getDev().getRoomName());
//                    viewHolder.mDevInfoEditRoom.setText(tv.getDev().getRoomName());
//                    viewHolder.mDevInfoEditName.setHint(tv.getDev().getDevName());
//                    viewHolder.mDevInfoEditWay.setText("设备不支持");
//                    viewHolder.mDevInfoEditType.setText("电视");
//                }
//            }
//        }
//        else if (Devs.get(position).getType() == 2) {
//            List<WareSetBox> sbs = MyApplication.getWareData().getStbs();
//            for (int i = 0; i < sbs.size(); i++) {
//                if (Devs.get(position).getCanCpuId().equals(sbs.get(i).getDev().getCanCpuId())
//                        && Devs.get(position).getDevId() == sbs.get(i).getDev().getDevId() &&
//                        Devs.get(position).getType() == sbs.get(i).getDev().getType()) {
//                    WareSetBox sb = sbs.get(i);
//                    String BoardName = "";
//                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
//                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(i);
//                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
//                            BoardName = chnout.getBoardName();
//                        }
//                    }
//                    //可视布局数据
//                    viewHolder.mDevInfoName.setText(sb.getDev().getDevName());
//                    viewHolder.mDevInfoRoom.setText(sb.getDev().getRoomName());
//                    viewHolder.mDevInfoTypeIV.setText("机顶盒");
//                    viewHolder.mDevInfoWay.setText("设备不支持");
//                    if ("".equals(BoardName))
//                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
//                    viewHolder.mDevInfoOutBoard.setText(BoardName);
//
//                    //不可视布局数据
//                    viewHolder.mDevInfoEditEditRoom.setHint(sb.getDev().getRoomName());
//                    viewHolder.mDevInfoEditRoom.setText(sb.getDev().getRoomName());
//                    viewHolder.mDevInfoEditName.setHint(sb.getDev().getDevName());
//                    viewHolder.mDevInfoEditWay.setText("设备不支持");
//                    viewHolder.mDevInfoEditType.setText("机顶盒");
//                }
//            }
//        }
        else if (Devs.get(position).getType() == 3) {
            List<WareLight> lights = MyApplication.getWareData().getLights();
            for (int i = 0; i < lights.size(); i++) {
                if (Devs.get(position).getCanCpuId().equals(lights.get(i).getDev().getCanCpuId())
                        && Devs.get(position).getDevId() == lights.get(i).getDev().getDevId() &&
                        Devs.get(position).getType() == lights.get(i).getDev().getType()) {
                    final WareLight light = lights.get(i);
                    String BoardName = "";
                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(j);
                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
                            BoardName = chnout.getBoardName();
                        }
                    }
                    //可视布局数据
                    viewHolder.mDevInfoName.setText(light.getDev().getDevName());
                    viewHolder.mDevInfoRoom.setText(light.getDev().getRoomName());
                    if (light.getbOnOff() == 0) {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.light_close);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.light_close);
                    } else {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.light_open);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.light_open);
                    }
                    final WareLight light_click = light;
                    viewHolder.mDevInfoTypeIVTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (light_click.getbOnOff() == 0) {
                                SendDataUtil.controlDev(light_click.getDev(), 0);
                            } else {
                                SendDataUtil.controlDev(light_click.getDev(), 1);
                            }
                        }
                    });

                    viewHolder.mDevInfoWay.setText(light.getPowChn() + 1 + "");
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(light.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setText("");
                    viewHolder.mDevInfoEditName.setHint(light.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(light.getPowChn() + 1 + "");
                }
            }
        } else if (Devs.get(position).getType() == 4) {
            List<WareCurtain> curtains = MyApplication.getWareData().getCurtains();
            for (int i = 0; i < curtains.size(); i++) {
                if (Devs.get(position).getCanCpuId().equals(curtains.get(i).getDev().getCanCpuId())
                        && Devs.get(position).getDevId() == curtains.get(i).getDev().getDevId() &&
                        Devs.get(position).getType() == curtains.get(i).getDev().getType()) {
                    WareCurtain curtain = curtains.get(i);
                    String BoardName = "";
                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(j);
                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
                            BoardName = chnout.getBoardName();
                        }
                    }

                    int Way_num = MyApplication.getWareData().getCurtains().get(i).getPowChn();
                    String Way_str = new StringBuffer(Integer.toBinaryString(Way_num)).reverse().toString();
                    String Way_ok = "";
                    for (int j = 0; j < Way_str.length(); j++) {
                        if (Way_str.charAt(j) == '1') {
                            Way_ok += j + 1 + "、";
                        }
                    }
                    if (!"".equals(Way_ok))
                        Way_ok = Way_ok.substring(0, Way_ok.lastIndexOf("、"));
                    viewHolder.mDevInfoWay.setText(Way_ok);
                    //可视布局数据
                    viewHolder.mDevInfoName.setText(curtain.getDev().getDevName());
                    viewHolder.mDevInfoRoom.setText(curtain.getDev().getRoomName());

                    viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.chuanglian_sta);
                    viewHolder.mDevInfoEditIV.setImageResource(R.drawable.chuanglian_sta);
                    final WareCurtain chuanglian_fin = curtain;
                    viewHolder.mDevInfoTypeIVTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int num = (int) Math.random();
                            if (num == 0)
                                SendDataUtil.controlDev(chuanglian_fin.getDev(), UdpProPkt.E_CURT_CMD.e_curt_offOn.getValue());
                            else
                                SendDataUtil.controlDev(chuanglian_fin.getDev(), UdpProPkt.E_CURT_CMD.e_curt_offOff.getValue());
                        }
                    });

                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(curtain.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(curtain.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(Way_ok);
                }
            }
        } else if (Devs.get(position).getType() == 7) {//新风
            List<WareFreshAir> freshAirs = MyApplication.getWareData().getFreshAirs();
            for (int i = 0; i < freshAirs.size(); i++) {
                if (Devs.get(position).getCanCpuId().equals(freshAirs.get(i).getDev().getCanCpuId())
                        && Devs.get(position).getDevId() == freshAirs.get(i).getDev().getDevId() &&
                        Devs.get(position).getType() == freshAirs.get(i).getDev().getType()) {
                    WareFreshAir freshAir = freshAirs.get(i);
                    String BoardName = "";
                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(j);
                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
                            BoardName = chnout.getBoardName();
                        }
                    }
                    //可视布局数据
                    viewHolder.mDevInfoName.setText(freshAir.getDev().getDevName());
                    viewHolder.mDevInfoRoom.setText(freshAir.getDev().getRoomName());

                    if (freshAir.getbOnOff() == 0) {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.freshair_close);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.freshair_close);
                    } else {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.freshair_open);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.freshair_open);
                    }
                    final WareFreshAir freshAir_fin = freshAir;
                    viewHolder.mDevInfoTypeIVTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (freshAir_fin.getbOnOff() == 1) {
                                SendDataUtil.controlDev(freshAir_fin.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_close.getValue());
                            } else {
                                SendDataUtil.controlDev(freshAir_fin.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_open.getValue());
                            }
                        }
                    });

                    viewHolder.mDevInfoWay.setText((freshAir.getOnOffChn() + 1) + "、"
                            + (freshAir.getSpdLowChn() + 1) + "、" + (freshAir.getSpdMidChn() + 1)
                            + "、" + (freshAir.getSpdHighChn() + 1));
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(freshAir.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(freshAir.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText((freshAir.getOnOffChn() + 1) + "、"
                            + (freshAir.getSpdLowChn() + 1) + "、" + (freshAir.getSpdMidChn() + 1)
                            + "、" + (freshAir.getSpdHighChn() + 1));
                }
            }
        } else if (Devs.get(position).getType() == 9) {//地暖
            List<WareFloorHeat> floorHeats = MyApplication.getWareData().getFloorHeat();
            for (int i = 0; i < floorHeats.size(); i++) {
                if (Devs.get(position).getCanCpuId().equals(floorHeats.get(i).getDev().getCanCpuId())
                        && Devs.get(position).getDevId() == floorHeats.get(i).getDev().getDevId() &&
                        Devs.get(position).getType() == floorHeats.get(i).getDev().getType()) {
                    WareFloorHeat floorHeat = floorHeats.get(i);
                    String BoardName = "";
                    for (int j = 0; j < MyApplication.getWareData().getBoardChnouts().size(); j++) {
                        WareBoardChnout chnout = MyApplication.getWareData().getBoardChnouts().get(j);
                        if (Devs.get(position).getCanCpuId().equals(chnout.getDevUnitID())) {
                            BoardName = chnout.getBoardName();
                        }
                    }
                    //可视布局数据
                    viewHolder.mDevInfoName.setText(floorHeat.getDev().getDevName());
                    viewHolder.mDevInfoRoom.setText(floorHeat.getDev().getRoomName());

                    if (floorHeat.getbOnOff() == 0) {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.floorheat_close);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.floorheat_close);
                    } else {
                        viewHolder.mDevInfoTypeIV.setImageResource(R.drawable.floorheat_open);
                        viewHolder.mDevInfoEditIV.setImageResource(R.drawable.floorheat_open);
                    }
                    final WareFloorHeat floorHeat_fin = floorHeat;
                    viewHolder.mDevInfoTypeIVTest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (floorHeat_fin.getbOnOff() == 1) {
                                SendDataUtil.controlDev(floorHeat_fin.getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_close.getValue());
                            } else
                                SendDataUtil.controlDev(floorHeat_fin.getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_open.getValue());
                        }
                    });

                    viewHolder.mDevInfoWay.setText((floorHeat.getPowChn() + 1) + "");
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(floorHeat.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(floorHeat.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText((floorHeat.getPowChn() + 1) + "");
                }
            }
        }
    }

    /**
     * ViewHolder
     */
    public static class ViewHolder {
        public View rootView;
        public ImageView mDevInfoDelete;
        public ImageView mDevInfoTypeIV;
        public ImageView mDevInfoEditIV;
        public TextView mDevInfoName;
        public ImageView mDevInfoEdit;
        public TextView mDevInfoOutBoard;
        public TextView mDevInfoRoom;
        public TextView mDevInfoTypeIVTest;
        public TextView mDevInfoWay;
        public LinearLayout mDevInfoLook;
        public TextView mDevInfoEditRoom;
        public EditText mDevInfoEditName;
        public TextView mDevInfoEditWay;
        public LinearLayout mDevInfoEditLook;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mDevInfoDelete = (ImageView) rootView.findViewById(R.id.Dev_Info_Delete);
            this.mDevInfoTypeIV = (ImageView) rootView.findViewById(R.id.Dev_Info_Type_Iv);
            this.mDevInfoEditIV = (ImageView) rootView.findViewById(R.id.Dcv_Info_Edit_Type_IV);
            this.mDevInfoName = (TextView) rootView.findViewById(R.id.Dev_Info_Name);
            this.mDevInfoEdit = (ImageView) rootView.findViewById(R.id.Dev_Info_Edit);
            this.mDevInfoOutBoard = (TextView) rootView.findViewById(R.id.Dev_Info_OutBoard);
            this.mDevInfoRoom = (TextView) rootView.findViewById(R.id.Dev_Info_Room);
            this.mDevInfoTypeIVTest = (TextView) rootView.findViewById(R.id.Dev_Info_Edit_Type_Test);
            this.mDevInfoWay = (TextView) rootView.findViewById(R.id.Dev_Info_Way);
            this.mDevInfoLook = (LinearLayout) rootView.findViewById(R.id.Dev_Info_Look);
            this.mDevInfoEditRoom = (TextView) rootView.findViewById(R.id.Dev_Info_Edit_Room);
            this.mDevInfoEditName = (EditText) rootView.findViewById(R.id.Dev_Info_Edit_Name);
            this.mDevInfoEditWay = (TextView) rootView.findViewById(R.id.Dev_Info_Edit_Way);
            this.mDevInfoEditLook = (LinearLayout) rootView.findViewById(R.id.Dev_Info_Edit_Look);
        }
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(mContext, R.layout.listview_popupwindow_equipment, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        customView.setFocusable(true);
        customView.setFocusableInTouchMode(true);
        customView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                }
                return false;
            }
        });
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(customView, view_parent.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, mContext);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                popupWindow.dismiss();
            }
        });
        //popupwindow页面之外可点
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    /**
     * 选择通道
     */
    public void initWayDialog(Context context, final TextView view, List<Integer> Way, boolean[] isSelect) {
        final List<String> Ways_str = new ArrayList<>();
        for (int i = 0; i < Way.size(); i++) {
            Ways_str.add(Way.get(i) + "");
        }
        if (mMultiChoicePopWindow == null)
            mMultiChoicePopWindow = new MultiChoicePopWindow(context, view, Ways_str, isSelect);
        else {
            mMultiChoicePopWindow.upDataFlag(isSelect);
            mMultiChoicePopWindow.upParentView(view);
            mMultiChoicePopWindow.upList(Ways_str);
            mMultiChoicePopWindow.UpDataView();
        }
        mMultiChoicePopWindow.setTitle("选择通道");
        mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
                    int size = selItems.length;
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < size; i++) {
                        if (selItems[i]) {
                            stringBuffer.append(Ways_str.get(i) + "、");
                        }
                    }
                    if (stringBuffer.toString().length() == 0)
                        view.setText("点击选择通道");
                    String stringBuffer_str = stringBuffer.toString();
                    stringBuffer_str = stringBuffer_str.substring(0, stringBuffer_str.lastIndexOf("、"));
                    view.setText(stringBuffer_str.toString());
                } catch (Exception e) {
                    return;
                }
            }
        });
        mMultiChoicePopWindow.show();
    }
}
