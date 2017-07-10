package cn.etsoft.smarthome.UiHelper;

import android.os.Handler;
import android.util.Log;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.Out_List_printcmd;
import cn.etsoft.smarthome.Domain.PrintCmd;
import cn.etsoft.smarthome.Domain.UpBoardKeyData;
import cn.etsoft.smarthome.Domain.WareChnOpItem;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Author：FBL  Time： 2017/7/6.
 * 设备配按键页面   辅助类
 */

public class Dev_KeysSetHelper {


    //解决 fragment隐藏后  房间改变监听失效，显示后， 房间未更新；导致数据错误
    public static String RoomName;
    //所有按键板
    private static List<Out_List_printcmd> listData_all;

    public static String getRoomName() {
        return RoomName;
    }

    public static void setRoomName(String roomName) {
        RoomName = roomName;
    }


    public static List<Out_List_printcmd> getListData_all() {
        if (listData_all == null)
            listData_all = new ArrayList<>();
        return listData_all;
    }

    public static void setListData_all(List<Out_List_printcmd> listData_all) {
        Dev_KeysSetHelper.listData_all = listData_all;
    }
    /**
     * 初始化外部转盘数据
     */
    public static List<CircleDataEvent> initSceneCircleOUterData() {
        List<CircleDataEvent> Data_OuterCircleList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            if (i == 0 || i == 8) event.setTitle("空调");
            if (i == 1 || i == 9) event.setTitle("电视");
            if (i == 2 || i == 10) event.setTitle("机顶盒");
            if (i == 3 || i == 11) event.setTitle("灯光");
            if (i == 4 || i == 12) event.setTitle("窗帘");
            if (i == 5 || i == 13) event.setTitle("监控");
            if (i == 6 || i == 14) event.setTitle("门禁");
            if (i == 7) event.setTitle("插座");
            Data_OuterCircleList.add(event);
        }
        return Data_OuterCircleList;
    }

    /**
     * 初始化内部转盘数据
     */
    public static List<CircleDataEvent> initSceneCircleInnerData() {
        List<CircleDataEvent> Data_InnerCircleList = new ArrayList<>();
        switch (MyApplication.getWareData().getRooms().size()) {
            case 1:
                Data_InnerCircleList = initRoom(4, Data_InnerCircleList);
                break;
            case 2:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
            case 3:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
            case 4:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
        }
        return Data_InnerCircleList;
    }

    /**
     * 初始化转盘房间数据
     */
    public static List<CircleDataEvent> initRoom(int maxsize, List<CircleDataEvent> Data_InnerCircleList) {
        Data_InnerCircleList.clear();
        for (int i = 0; i < maxsize; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            event.setTitle(MyApplication.getWareData().getRooms().get(i % MyApplication.getWareData().getRooms().size()));
            Data_InnerCircleList.add(event);
        }
        return Data_InnerCircleList;
    }


    public static void InitKeyData(final Handler handler, final List<WareDev> Dev_room, final int devposition) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listData_all = new ArrayList<>();
                WareChnOpItem ChnOpItem = null;
                List<WareChnOpItem> ChnOpItem_list = new ArrayList<>();
                if (MyApplication.getWareData().getChnOpItems() != null && MyApplication.getWareData().getChnOpItems().size() > 0) {
                    for (int i = 0; i < MyApplication.getWareData().getChnOpItems().size(); i++) {
                        ChnOpItem_list.add(MyApplication.getWareData().getChnOpItems().get(i));
                    }
                }
                for (int j = 0; j < MyApplication.getWareData().getKeyInputs().size(); j++) {
                    List<String> list_Name = new ArrayList<>();
                    boolean isConcan = false;
                    for (int k = 0; k < ChnOpItem_list.size(); k++) {
                        if (ChnOpItem_list.get(k).getDevUnitID()
                                .equals(MyApplication.getWareData().getKeyInputs().get(j).getDevUnitID())) {
                            ChnOpItem = ChnOpItem_list.get(k);
                            for (int i = 0; i < MyApplication.getWareData().getKeyInputs().get(j).getKeyName().length; i++) {
                                list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
                            }
                            isConcan = true;
                        }
                    }
                    if (!isConcan) {
                        ChnOpItem = new WareChnOpItem();
                        ChnOpItem.setDevUnitID(MyApplication.getWareData().getKeyInputs().get(j).getDevUnitID());
                        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().get(j).getKeyName().length; i++) {
                            list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
                        }
                    }
                    List<PrintCmd> listData = new ArrayList<>();
                    for (int i = 0; i < ChnOpItem.getKeyUpCmd().length; i++) {
                        PrintCmd cmd = new PrintCmd();
                        cmd.setDevUnitID(ChnOpItem.getDevUnitID());
                        try {
                            cmd.setDevType(Dev_room.get(devposition).getType());
                        } catch (Exception e) {
                        }
                        cmd.setKey_cmd(ChnOpItem.getKeyUpCmd()[i]);
                        cmd.setKeyAct_num(1);
                        if (ChnOpItem.getKeyUpCmd()[i] > 0) cmd.setSelect(true);
                        else cmd.setSelect(false);
                        if (list_Name.size() == 0)
                            cmd.setKeyname("未知按键");
                        else {
                            try {
                                cmd.setKeyname(list_Name.get(i));
                            } catch (Exception e) {
                                if (i >= list_Name.size())
                                    cmd.setKeyname("按键" + i);
                            }
                        }
                        listData.add(cmd);
                    }
                    Out_List_printcmd list = new Out_List_printcmd();
                    list.setUnitid(ChnOpItem.getDevUnitID());
                    list.setPrintCmds(listData);
                    listData_all.add(list);
                }
                Dev_KeysSetHelper.setListData_all(listData_all);
                handler.sendMessage(handler.obtainMessage());
            }
        }).start();
    }


    private static List<WareDev> mDev_Room;
    private static String DEVS_ALL_ROOM = "全部";

    public static List<WareDev> getRoomDev(String roomname) {
        List<WareDev> devs = new ArrayList<>();
        devs.addAll(MyApplication.getWareData().getDevs());
        //房间内的设备集合
        mDev_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(roomname)) {
            mDev_Room.addAll(devs);
        } else {
            for (int i = 0; i < devs.size(); i++) {
                if (devs.get(i).getRoomName().equals(roomname)) {
                    mDev_Room.add(devs.get(i));
                }
            }
        }
        return mDev_Room;
    }

    public static void Save(WareDev dev) {
        try {
            listData_all = Dev_KeysSetHelper.getListData_all();
            for (int i = 0; i < listData_all.size(); i++) {
                List<PrintCmd> listData = listData_all.get(i).getPrintCmds();
                for (int j = 0; j < listData.size(); j++) {
                    if (listData.get(j).isSelect() && listData.get(j).getKey_cmd() == 0) {
                        ToastUtil.showText("存在未设置，请设置完成后保存");
                        return;
                    }
                }
            }
            //根据以上注释掉的数据结构，将已有数据已此格式寄存；
            UpBoardKeyData data = new UpBoardKeyData();//上传数据实体；
            List<UpBoardKeyData.ChnOpitemRowsBean> bean_list = new ArrayList<>();//按键板实体集合；

            for (int i = 0; i < listData_all.size(); i++) {
                UpBoardKeyData.ChnOpitemRowsBean bean = data.new ChnOpitemRowsBean();
                bean.setKey_cpuCanID(listData_all.get(i).getUnitid());
                byte[] Valid_up = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应位置；
                byte[] Cmd_up = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应的命令；
                byte[] Valid_down = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键的相应位置；
                byte[] Cmd_down = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键相应的命令
                List<PrintCmd> listData = listData_all.get(i).getPrintCmds();

                for (int j = 0; j < listData.size(); j++) {
                    if (listData.get(j).isSelect()) {
                        Cmd_up[j] = (byte) listData.get(j).getKey_cmd();
                        Valid_up[j] = 1;
                    } else {
                        Cmd_up[j] = 0;
                        Valid_up[j] = 0;
                    }
                }
                for (int j = 0; j < listData.size(); j++) {
                    Valid_down[j] = 0;
                    Cmd_down[j] = 0;
                }
                bean.setKeyDownValid(0);
                bean.setKeyUpValid(0);
                bean.setKeyDownCmd(Cmd_down);
                bean.setKeyUpCmd(Cmd_up);
                //因为数据传递时，高位、低位和现实中相反，so循环赋值；
                String down_v = "";
                for (int j = 0; j < Valid_up.length; j++) {
                    down_v += Valid_up[Valid_up.length - 1 - j];
                }
                //将改好的2#字符串转成10#；
                BigInteger bi_down = new BigInteger(down_v, 2);  //转换成BigInteger类型
                int v_down = Integer.parseInt(bi_down.toString(10)); //参数2指定的是转化成X进制，默认10进制
                bean.setKeyUpValid(v_down);
                bean_list.add(bean);
            }

            //将以上数据加入到上传实体中；
            data.setDevUnitID(GlobalVars.getDevid());
            data.setChn_opitem_rows(bean_list);
            data.setDatType(15);
            data.setSubType1(0);
            data.setSubType2(0);
            data.setDevType(dev.getType());
            data.setDevID(dev.getDevId());
            data.setOut_cpuCanID(dev.getCanCpuId());
            data.setChn_opitem(bean_list.size());

            Gson gson = new Gson();
            System.out.println(gson.toJson(data));
            MyApplication.mApplication.getUdpServer().send(gson.toJson(data).toString());
        } catch (Exception e) {
            Log.e("Exception", e + "");
            ToastUtil.showText("保存失败，请检查数据是否合适");
        }
    }
}
