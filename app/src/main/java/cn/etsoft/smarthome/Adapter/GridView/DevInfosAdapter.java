package cn.etsoft.smarthome.Adapter.GridView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import cn.etsoft.smarthome.Domain.GlobalVars;
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

/**
 * Author：FBL  Time： 2017/6/29.
 * 设备详情  设备适配器
 */

public class DevInfosAdapter extends BaseAdapter {

    private List<WareDev> Devs;
    private Activity mContext;
    private PopupWindow popupWindow;
    private List<String> RoomNames;
    private MultiChoicePopWindow mMultiChoicePopWindow;
    private AlertDialog.Builder builder;
    private List<String> mDevTypes;
    private List<String> mBoards;

    public DevInfosAdapter(List<WareDev> list, Activity context) {
        RoomNames = MyApplication.getWareData().getRooms();
        Devs = list;
        mDevTypes = new ArrayList<>();
        mDevTypes.add("空调");
        mDevTypes.add("灯光");
        mDevTypes.add("窗帘");
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.girdview_devinfoitem, null);
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
                            SendDataUtil.deleteDev(Devs.get(position));
                            MyApplication.mApplication.showLoadDialog(mContext);
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
                if (Devs.get(position).getType() == 0) {
                    for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                        if (Devs.get(position).getCanCpuId()
                                .equals(MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId())
                                && Devs.get(position).getDevId() != MyApplication.getWareData().getAirConds().get(i).getDev().getDevId()) {

                            int PowChn = MyApplication.getWareData().getAirConds().get(i).getPowChn();
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
                } else if (Devs.get(position).getType() == 3) {
                    for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                        if (Devs.get(position).getCanCpuId()
                                .equals(MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId())
                                && Devs.get(position).getDevId() != MyApplication.getWareData().getLights().get(i).getDev().getDevId()) {

                            int PowChn = MyApplication.getWareData().getLights().get(i).getPowChn() + 1;
                            list_voard_cancpuid.add(PowChn);
                        }
                    }
                    list_voard_cancpuid.size();
                } else if (Devs.get(position).getType() == 4) {
                    for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                        if (Devs.get(position).getCanCpuId()
                                .equals(MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId())
                                && Devs.get(position).getDevId() != MyApplication.getWareData().getCurtains().get(i).getDev().getDevId()) {

                            int PowChn = MyApplication.getWareData().getCurtains().get(i).getPowChn();
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
                            int Save_DevWay;
                            try {
                                Save_DevWay = Integer.parseInt(Devs.get(position).getPowChn());
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
                                        String[] WayStr_ok = Way_Str.split("、");
                                        if (WayStr_ok.length == 0) {
                                            ToastUtil.showText("请选择通道");
                                            return;
                                        } else {
                                            if (WayStr_ok.length > 5) {//135
                                                ToastUtil.showText("空调最多5个通道");
                                                return;
                                            }
                                            String Way = "";
                                            for (int j = 0; j < 12; j++) {
                                                boolean IsEnter = false;
                                                for (int k = 0; k < WayStr_ok.length; k++) {
                                                    if (j == Integer.parseInt(WayStr_ok[k]) - 1) {
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
                                Save_DevWay = Integer.parseInt(Way_Str) - 1;
                            } else if (Devs.get(position).getType() == 4) {
                                for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                                    if (MyApplication.getWareData().getCurtains().get(i).getDev().getDevId()
                                            == Devs.get(position).getDevId()
                                            && MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId()
                                            .equals(Devs.get(position).getCanCpuId())) {
                                        //设备通道 保存数据处理
                                        String Way_Str = finalViewHolder.mDevInfoEditWay.getText().toString();
                                        String[] WayStr_ok = Way_Str.split("、");
                                        if (WayStr_ok.length == 0) {
                                            ToastUtil.showText("请选择通道");
                                            return;
                                        } else {
                                            if (WayStr_ok.length > 3) {//135
                                                ToastUtil.showText("窗帘最多3个通道");
                                                return;
                                            }
                                            String Way = "";
                                            for (int j = 0; j < 12; j++) {
                                                boolean IsEnter = false;
                                                for (int k = 0; k < WayStr_ok.length; k++) {
                                                    if (j == Integer.parseInt(WayStr_ok[k]) - 1) {
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
                            }
                            String chn_str = "";
                            chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                    "\"datType\":" + 6 + "," +
                                    "\"subType1\":0," +
                                    "\"subType2\":0," +
                                    "\"canCpuID\":\"" + Devs.get(position).getCanCpuId() + "\"," +
                                    "\"devType\":" + Devs.get(position).getType() + "," +
                                    "\"devID\":" + +Devs.get(position).getDevId() + "," +
                                    "\"devName\":" + "\"" + Save_DevName + "\"," +
                                    "\"roomName\":" + "\"" + Save_Roomname + "\"," +
                                    "\"powChn\":" + Save_DevWay + "," +
                                    "\"cmd\":" + 1 + "}";
                            MyApplication.mApplication.getUdpServer().send(chn_str);
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
                    viewHolder.mDevInfoType.setText("空调");

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
                    viewHolder.mDevInfoEditType.setText("空调");
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
//                    viewHolder.mDevInfoType.setText("电视");
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
//                    viewHolder.mDevInfoType.setText("机顶盒");
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
                    WareLight light = lights.get(i);
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
                    viewHolder.mDevInfoType.setText("灯光");
                    viewHolder.mDevInfoWay.setText(light.getPowChn() + 1 + "");
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(light.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setText("");
                    viewHolder.mDevInfoEditName.setHint(light.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(light.getPowChn() + 1 + "");
                    viewHolder.mDevInfoEditType.setText("灯光");
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
                    //可视布局数据
                    viewHolder.mDevInfoName.setText(curtain.getDev().getDevName());
                    viewHolder.mDevInfoRoom.setText(curtain.getDev().getRoomName());
                    viewHolder.mDevInfoType.setText("窗帘");
                    viewHolder.mDevInfoWay.setText(curtain.getPowChn() + "");
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(curtain.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(curtain.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(curtain.getPowChn() + "");
                    viewHolder.mDevInfoEditType.setText("窗帘");
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
                    viewHolder.mDevInfoType.setText("新风");
                    viewHolder.mDevInfoWay.setText(freshAir.getPowChn() + "");
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(freshAir.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(freshAir.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(freshAir.getPowChn() + "");
                    viewHolder.mDevInfoEditType.setText("新风");
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
                    viewHolder.mDevInfoType.setText("地暖");
                    viewHolder.mDevInfoWay.setText(floorHeat.getPowChn() + "");
                    if ("".equals(BoardName))
                        viewHolder.mDevInfoOutBoard.setText("数据解析出错");
                    viewHolder.mDevInfoOutBoard.setText(BoardName);

                    //不可视布局数据
                    viewHolder.mDevInfoEditRoom.setText(floorHeat.getDev().getRoomName());
                    viewHolder.mDevInfoEditName.setHint(floorHeat.getDev().getDevName());
                    viewHolder.mDevInfoEditWay.setText(floorHeat.getPowChn() + "");
                    viewHolder.mDevInfoEditType.setText("地暖");
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
        public TextView mDevInfoName;
        public ImageView mDevInfoEdit;
        public TextView mDevInfoOutBoard;
        public TextView mDevInfoRoom;
        public TextView mDevInfoType;
        public TextView mDevInfoWay;
        public LinearLayout mDevInfoLook;
        public TextView mDevInfoEditRoom;
        public EditText mDevInfoEditName;
        public TextView mDevInfoEditType;
        public TextView mDevInfoEditWay;
        public LinearLayout mDevInfoEditLook;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mDevInfoDelete = (ImageView) rootView.findViewById(R.id.Dev_Info_Delete);
            this.mDevInfoName = (TextView) rootView.findViewById(R.id.Dev_Info_Name);
            this.mDevInfoEdit = (ImageView) rootView.findViewById(R.id.Dev_Info_Edit);
            this.mDevInfoOutBoard = (TextView) rootView.findViewById(R.id.Dev_Info_OutBoard);
            this.mDevInfoRoom = (TextView) rootView.findViewById(R.id.Dev_Info_Room);
            this.mDevInfoType = (TextView) rootView.findViewById(R.id.Dev_Info_Type);
            this.mDevInfoWay = (TextView) rootView.findViewById(R.id.Dev_Info_Way);
            this.mDevInfoLook = (LinearLayout) rootView.findViewById(R.id.Dev_Info_Look);
            this.mDevInfoEditRoom = (TextView) rootView.findViewById(R.id.Dev_Info_Edit_Room);
            this.mDevInfoEditName = (EditText) rootView.findViewById(R.id.Dev_Info_Edit_Name);
            this.mDevInfoEditType = (TextView) rootView.findViewById(R.id.Dev_Info_Edit_Type);
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
            }
        });
        mMultiChoicePopWindow.show();
    }

}
