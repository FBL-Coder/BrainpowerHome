package cn.etsoft.smarthome.NetMessage;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.abc.mybaseactivity.NetWorkListener.AppNetworkMgr;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.etsoft.smarthome.Domain.ChnOpItem_scene;
import cn.etsoft.smarthome.Domain.Condition_Event_Bean;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.GroupSet_Data;
import cn.etsoft.smarthome.Domain.RcuInfo;
import cn.etsoft.smarthome.Domain.SetEquipmentResult;
import cn.etsoft.smarthome.Domain.SetSafetyResult;
import cn.etsoft.smarthome.Domain.SetSafetyResult_alarm;
import cn.etsoft.smarthome.Domain.Timer_Data;
import cn.etsoft.smarthome.Domain.UserBean;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareBoardChnout;
import cn.etsoft.smarthome.Domain.WareBoardKeyInput;
import cn.etsoft.smarthome.Domain.WareChnOpItem;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareKeyOpItem;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.Domain.WareTv;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.Utils.SendDataUtil;

import static android.content.ContentValues.TAG;

/**
 * Author：FBL  Time： 2017/6/9.
 */

public class UDPServer implements Runnable {

    private static final int PORT = 25001;
    private static final int SEND_PORT = 8400;
    private InetAddress local;
    private byte[] msg = new byte[1024 * 10];
    private boolean life = true;
    private Handler mhandler;
    private WareData wareData;
    private DatagramSocket dSocket;

    //是否刷新数据
    private boolean isFreshData = false;

    public UDPServer(Handler handler) {
        mhandler = handler;
    }

    public boolean isLife() {
        return life;
    }

    public void setLife(boolean life) {
        this.life = life;
    }

    public void webSocketData(String info) {
        extractData(info);
    }

    @Override
    public void run() {
        wareData = MyApplication.getWareData();
        DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
        try {
            dSocket = new DatagramSocket(PORT);
            while (life) {
                dSocket.receive(dPacket);
                extractData(new String(dPacket.getData(), 0, dPacket.getLength()));
            }
        } catch (Exception e) {
            Message message = mhandler.obtainMessage();
            message.what = MyApplication.mApplication.UDP_NORECEIVE;
            mhandler.sendMessage(message);
        }
    }


    public void heartBeat() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (MyApplication.mApplication.isIsheartting()) {
                    MyApplication.mApplication.setIsheartting(false);
                    Message message = mhandler.obtainMessage();
                    message.what = MyApplication.mApplication.HEARTBEAT_RUN;
                    mhandler.sendMessage(message);
                } else {
                    Message message = mhandler.obtainMessage();
                    message.what = MyApplication.mApplication.HEARTBEAT_STOP;
                    mhandler.sendMessage(message);
                }
            }
        }, 20000, 100000);
    }


    public void send(final String msg) {

        int NETWORK = AppNetworkMgr.getNetworkState(MyApplication.mContext);
        if (NETWORK == 0) {
            ToastUtil.showText("请检查网络连接");
        } else if (NETWORK != 0 && NETWORK < 10) {
            Log.i("数据流量发送WebSocket", "WEB" + msg);
            MyApplication.mApplication.getWsClient().sendMsg(msg);
        } else {
            MyApplication.setOnUdpgetDataNoBackListener(new MyApplication.OnUdpgetDataNoBackListener() {
                @Override
                public void WSSendDatd(String msg) {
                    String jsonToServer = "{\"uid\":\"" + GlobalVars.getUserid() + "\",\"type\":\"forward\",\"data\":" + msg + "}";
                    if ("".equals(GlobalVars.getDevid())) {
                        return;
                    }
                    Log.i("Udp发送无返回，改WebSocket发送", "WEB" + jsonToServer);
                    MyApplication.mApplication.getWsClient().sendMsg(jsonToServer);

                    UdpSendMsg(msg);
                }
            });
            if (!GlobalVars.isIsLAN()) {
                if ("".equals(GlobalVars.getDevid())) {
                    return;
                }
                String jsonToServer = "{\"uid\":\"" + GlobalVars.getUserid() + "\",\"type\":\"forward\",\"data\":" + msg + "}";
                Log.i("发送WebSocket", "WEB" + jsonToServer);
                MyApplication.mApplication.getWsClient().sendMsg(jsonToServer);
            } else {
                UdpSendMsg(msg);
            }
        }
    }

    private void UdpSendMsg(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    local = InetAddress.getByName("localhost"); // 本机测试
                    int msg_len = msg == null ? 0 : msg.getBytes().length;
                    Message message = mhandler.obtainMessage();
                    message.obj = msg;
                    message.what = MyApplication.mApplication.UDP_NOBACK;
                    mhandler.sendMessage(message);
                    DatagramPacket dPacket = new DatagramPacket(msg.getBytes(), msg_len,
                            local, SEND_PORT);
                    Log.i("发送UDP", "UDP" + msg);
                    dSocket.send(dPacket);

                } catch (Exception e) {
                    Log.e("Exception", "UDP发送消息失败" + e + "");
                    Message message = mhandler.obtainMessage();
                    message.what = MyApplication.mApplication.UDP_NOSEND;
                    mhandler.sendMessage(message);
                }
            }
        }).start();
    }

    public static void show(String str) {

        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String sub;
        while (index < str.length()) {
            // java的字符不允许指定超过总的长度end
            if (str.length() <= index + maxLength) {
                sub = str.substring(index);
            } else {
                sub = str.substring(index, maxLength);
            }
            index += maxLength;
            Log.i("接收信息", sub.trim());
        }
    }

    //警报时间间隔
    long time = 0;

    public void extractData(String info) {

        int datType = 0;
        int subType2 = 0;
        int subType1 = 0;
        try {
            JSONObject jsonObject = new JSONObject(info);
            datType = jsonObject.getInt("datType");
            subType1 = jsonObject.getInt("subType1");
            subType2 = jsonObject.getInt("subType2");
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
        if (datType != 35)
            show(info);
        switch (datType) {
            case 0:// e_udpPro_getRcuinfo
                if (subType2 == 1) {
                    MyApplication.setNewWareData();
                    setRcuInfo(info);
                }
                break;
            case 2:// e_udpPro_getRcuinfo
                if (subType1 == 0 && subType2 == 0) {
                    MyApplication.mApplication.setIsheartting(true);
                    GlobalVars.setIsLAN(true);
                }
                break;
            case 3: // getDevsInfo
                if (subType1 == 1) {
//                    MyApplication.getWareData().getDevs().clear();
                    isFreshData = true;
                    getDevsInfo(info);
                    //删除重复的设备
//                    List<WareDev> devs = removeDuplicateDevs(wareData.getDevs());
//                    MyApplication.getWareData().setDevs(devs);
                    MyApplication.getWareData().setRooms(removeRoomNames(MyApplication.getWareData().getRooms()));
//                    Log.i("Devs_Size", "设备总数 :  " + MyApplication.getWareData().getDevs().size() + "");
//                        if (MyApplication.getWareData().getDevs().get(i).getType() == 3)
//                            Log.i("Devs_Size", "所有灯ID  :  " + MyApplication.getWareData().getDevs().get(i).getDevId());
//                    }
                    Message msg = mhandler.obtainMessage();
                    if (mhandler != null) {
                        mhandler.sendMessage(msg);
                    }
                    Log.i(TAG, "灯数:" + MyApplication.getWareData().getLights().size() + "");
                    Log.i(TAG, "空调:" + MyApplication.getWareData().getAirConds().size() + "");
                    Log.i(TAG, "设备:" + MyApplication.getWareData().getDevs().size() + "");
                }
                break;
            case 4: // ctrlDev
                if (subType1 == 1) {
                    isFreshData = true;
                    refreshDevData(info);
                }
                break;
            case 5: // adddev
                if (subType1 == 1) {
                    if (subType2 == 1) {
                        addDev_result(info);
                        isFreshData = true;
                    } else {
                        isFreshData = true;
                    }
                }
                break;
            case 6: // editdev
                if (subType1 == 1) {
                    if (subType2 == 1) {
                        editDev_result(info);
                        isFreshData = true;
                    } else {
                        isFreshData = true;
                    }
                }
                break;
            case 7: // delDev
                if (subType1 == 1) {
                    if (subType2 == 1) {
                        deleteDev_result(info);
                        isFreshData = true;
                    } else {
                        isFreshData = true;
                    }
                }
                break;
            case 8:
                if (subType2 == 0) {
                    isFreshData = true;
                    getkeyOutBoard(info);
                }
                if (subType2 == 1) {
                    isFreshData = true;
                    getKyeInputBoard(info);
                }
                break;
            case 11: // e_udpPro_getKeyOpItems
                if (subType1 == 1) {
                    isFreshData = true;
                    getKeyOpItem(info);
                }
                break;
            case 12: // e_udpPro_setKeyOpItems
                if (subType1 == 1) {
                    setKeyOpItem_result(info);
                    isFreshData = true;
                }
                break;
            case 13: // e_udpPro_delKeyOpItems
                if (subType1 == 1) {
                    delKeyOpItem_result(info);
                    isFreshData = true;
                }
                break;
            case 14: // e_udpPro_getChnOpitems
                if (subType1 == 1) {
                    isFreshData = true;
                    getChnOpItem(info);

                }
                break;
            case 15: // e_udpPro_setChnOpitems
                if (subType1 == 1) {
                    setKeyOpItem_result(info);
                    isFreshData = true;
                /*if (getChnOpItemReply(udpProData))
                    myApp.getHandler().sendEmptyMessage(MSG_SETCHNOPITEM_INFO);*/
                }
                break;
            case 16: // e_udpPro_setChnOpitems
//                if (subType1 == 1) {
//                    delChnOpItem_result(info);
//                    isFreshData = true;
//                /*if (getChnOpItemReply(udpProData))
//                    myApp.getHandler().sendEmptyMessage(MSG_SETCHNOPITEM_INFO);*/
//                }
                break;
            case 17: // 获取定时数据
                if (subType2 == 1) {
                    getTimerData(info);
                    isFreshData = true;

                }
                break;
            case 18: // e_udpPro_setChnOpitems
//                if (subType1 == 1) {
//                    delChnOpItem_result(info);
//                    isFreshData = true;
//                /*if (getChnOpItemReply(udpProData))
//                    myApp.getHandler().sendEmptyMessage(MSG_SETCHNOPITEM_INFO);*/
//                }
                break;
            case 19: // e_udpPro_setChnOpitems
                if (subType2 == 1) {
                    setTimerData_back(info);
                    isFreshData = true;
                }
                break;
            case 20: // e_udpPro_setChnOpitems
                if (subType1 == 1) {
                    delChnOpItem_result(info);
                    isFreshData = true;
                /*if (getChnOpItemReply(udpProData))
                    myApp.getHandler().sendEmptyMessage(MSG_SETCHNOPITEM_INFO);*/
                }
                break;
            case 22: // e_udpPro_getSceneEvents
                if (subType2 == 1) {
                    isFreshData = true;
                    getSceneEvents(info);

                }
                break;
            case 23: // e_udpPro_addSceneEvents
                if (subType2 == 1) {
                    isFreshData = true;
                    addSceneEvents(info);

                }
                break;
            case 24: // e_udpPro_editSceneEvents
                if (subType2 == 1) {
                    isFreshData = true;
                    getSceneEvents(info);
                }
                break;
            case 25: // e_udpPro_delSceneEvents
                if (subType2 == 1) {
                    isFreshData = true;
                    delSceneEvents(info);
                }
                break;
            case 26: // e_udpPro_exeSceneEvents
                if (subType2 == 1) {
                    // setSceneEvents(udpProData);
//                    isFreshData = true;
                }
                break;
            case 27: // 获取触发事件数据
                if (subType2 == 1) {
                    getConditiondata(info);
                    isFreshData = true;
                }
                break;
            case 29: // 保存触发事件数据
                if (subType2 == 1) {
                    isFreshData = true;
                }
                break;
            case 32://e_udpPro_security_info
                if (subType1 == 4 && subType2 == 255) {
                    //查询联网模块防区信息
                    setSafetyEvents(info);
                    isFreshData = true;
                } else if (subType1 == 5) {
                    //修改联网模块防区信息
                    safety_result(info);
                    isFreshData = true;
                } else if (subType1 == 2) {
                    //解决多个警报5秒自动消失问题
                    //防区报警信息
                    safety_alarm(info);
                    isFreshData = true;
                } else if (subType1 == 1) {
                    //布防、撤防
                    safety(info);
                    isFreshData = true;
                } else if (subType1 == 7 && subType2 == 1) {
                    //对码
                    isFreshData = true;
                }
                break;
            case 35:// e_udpPro_chns_status
//                ctrlDevReply(info);
                isFreshData = true;
                break;
            case 58: // e_udpPro_get_key2scene
                if (subType1 == 1) {
                    isFreshData = true;
                    getChnOpItem_scene(info);
                }
                break;
            case 59: // e_udpPro_set_key2scene
                if (subType1 == 1) {
                    setKeyOpItem_result(info);
                    isFreshData = true;
                }
                break;
            case 66: // trigger
                if (subType2 == 255) {
                    getGroupSetData(info);
                    isFreshData = true;
                }
                if (subType1 == 2 && subType2 == 1) {
                    setGroupSetData(info);
                    isFreshData = true;
                }
                break;
            case 86: // e_udpPro_getShortcutKey
                if (subType2 == 0) {
                    isFreshData = true;
                    getUserEvents(info);
                }
            case 100: // e_udpPro_getShortcutKey
                if (subType2 == 1) {
                    if (System.currentTimeMillis() - time > 10000) {
                        time = System.currentTimeMillis();
                        isFreshData = true;
                        Log.e("Socket", "链接失败");
                    }
                }
                break;
        }

        if (isFreshData && datType != 2) {
            Message message = mhandler.obtainMessage();
            message.what = MyApplication.mApplication.UDP_DATA_OK;
            message.obj = datType;
            message.arg1 = subType1;
            message.arg2 = subType2;
            mhandler.sendMessage(message);
            isFreshData = false;
        }
    }

    int netword_count = 0;
    long TimeExit;
    int sleep = 1;

    /**
     * 返回001的包处理联网模块信息
     */

    public void setRcuInfo(String info) {
        if (System.currentTimeMillis() - TimeExit > 200) {
            Log.i("SLEEP", System.currentTimeMillis() - TimeExit + "");
            TimeExit = System.currentTimeMillis();
        } else {
            try {
                Log.i("SLEEP", "................");
                Thread.sleep(sleep * 100);
            } catch (Exception e) {
            }
            TimeExit = System.currentTimeMillis();
        }
//        获取联网模块的返回数据类型；
//        {
//        "devUnitID":	"39ffdb05484d303430690543",
//                "datType":	0,
//                "subType1":	0,
//                "subType2":	1,
//                "rcu_rows":	[{
//            "canCpuID":	"39ffdb05484d303430690543",
//                    "devUnitPass":	"30690543�\b@",
//                    "canCpuName":	"我的板子",
//                    "name":	"",
//                    "IpAddr":	"",
//                    "SubMask":	"",
//                    "Gateway":	"",
//                    "centerServ":	"",
//                    "roomNum":	"",
//                    "macAddr":	"",
//                    "SoftVersion":	"",
//                    "HwVersion":	"",
//                    "bDhcp":	0
//        }]
//    }

        List<RcuInfo> json_list = new ArrayList<>();
        String json_rcuinfo_list = (String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, "");

        Gson gson = new Gson();
        RcuInfo info1 = new RcuInfo();
        boolean IsAllequals = true;//给的模块ID都不一样，为true；
        try {
            JSONObject jsonObject = new JSONObject(info);
            info1.setDevUnitID(jsonObject.getString("devUnitID"));

            JSONArray jsonArray = jsonObject.getJSONArray("rcu_rows");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            try {
                info1.setCanCpuName(jsonObject1.getString("canCpuName"));
            } catch (Exception e) {//服务器和本地单片机数据不一，解析异常
                Log.w("Exception", e + "");
            }
//            "uid":	"39ffd505484d303408650743",
//            "pass":	"39ffd505",
//            "name":	"cef7b0b2d1d0b7a2",
//            "IpAddr":	"192.168.0.102",
//            "SubMask":	"255.255.255.0",
//            "Gateway":	"192.168.0.1",
//            "centerServ":	"192.168.0.104",
//            "roomNum":	"",
//            "macAddr":	"00502a040506",
//            "SoftVersion":	"",
//            "HwVersion":	"",
//            "bDhcp":	0
            info1.setDevUnitID(jsonObject1.getString("uid"));
            info1.setDevUnitPass(jsonObject1.getString("pass"));
            info1.setName(jsonObject1.getString("name"));
            info1.setIpAddr(jsonObject1.getString("IpAddr"));
            info1.setSubMask(jsonObject1.getString("SubMask"));
            info1.setGateWay(jsonObject1.getString("Gateway"));
            info1.setCenterServ(jsonObject1.getString("centerServ"));
            info1.setRoomNum(jsonObject1.getString("roomNum"));
            info1.setMacAddr(jsonObject1.getString("macAddr"));
            info1.setSoftVersion(jsonObject1.getString("SoftVersion"));
            info1.setHwVversion(jsonObject1.getString("HwVersion"));
            info1.setbDhcp(jsonObject1.getInt("bDhcp"));

            if (!"".equals(json_rcuinfo_list) && json_rcuinfo_list != null && json_rcuinfo_list.length() > 0) {
                json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
                }.getType());
                for (int i = 0; i < json_list.size(); i++) {
                    if (jsonObject.getString("devUnitID").equals(json_list.get(i).getDevUnitID())) {
//                        本地001数据包  过来后，根据id判断数据为同一个，所以会将服务器发送的数据覆盖，从而将canCpuname重置！
//                        处理则根据值来赋值；
//                        json_list.get(i).setCanCpuName(info1.getCanCpuName());
                        json_list.get(i).setbDhcp(info1.getbDhcp());
                        json_list.get(i).setCenterServ(info1.getCenterServ());
                        json_list.get(i).setDevUnitID(info1.getDevUnitID());
                        json_list.get(i).setGateWay(info1.getGateWay());
                        json_list.get(i).setHwVversion(info1.getHwVversion());
                        json_list.get(i).setIpAddr(info1.getIpAddr());
                        json_list.get(i).setMacAddr(info1.getMacAddr());
                        json_list.get(i).setRoomNum(info1.getRoomNum());
                        json_list.get(i).setSubMask(info1.getSubMask());
                        json_list.get(i).setSoftVersion(info1.getSoftVersion());
                        IsAllequals = false;
                    }
                }
                if (IsAllequals) {
                    json_list.add(info1);
                }
            } else {
                json_list.add(info1);
            }
        } catch (JSONException e) {
            Log.w("Exception", e + "");
        }
        String str = gson.toJson(json_list);
        AppSharePreferenceMgr.put(GlobalVars.RCUINFOLIST_SHAREPREFERENCE, str);
        MyApplication.getWareData().setRcuInfos(json_list);
        if (netword_count == sleep)
            isFreshData = true;
        sleep++;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    SendDataUtil.getSceneInfo();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                SendDataUtil.getSafetyInfo();
                            } catch (InterruptedException e) {
                                SendDataUtil.getSafetyInfo();
                            }
                        }
                    }).start();
                } catch (InterruptedException e) {
                    SendDataUtil.getSceneInfo();
                }
            }
        }).start();


    }


    /**
     * 所有设备数据
     */
    public void getDevsInfo(String info) {
//        {
//            "devUnitID":	"39ffd505484d303408650743",
//                "datType":	3,
//                "subType1":	1,
//                "subType2":	4,
//                "dev_rows":	[{
//            "canCpuID":	"36ffd4054842373532790843",
//                    "devName":	"c8fdbedecdb7b4b0c1b1",
//                    "roomName":	"ced4cad2",
//                    "devType":	4,
//                    "devID":	3,
//                    "bOnOff":	0,
//                    "timRun":	4,
//                    "powChn":	4
//        }, {
//            "canCpuID":	"36ffd4054842373532790843",
//                    "devName":	"c8fdbedecdb7b4b0c1b1",
//                    "roomName":	"ced4cad2",
//                    "devType":	4,
//                    "devID":	5,
//                    "bOnOff":	0,
//                    "timRun":	4,
//                    "powChn":	0
//        }],
//            "curtain":	2
//        }

        int devnum_air = 0;
        int devnum_cur = 0;
        int devnum_box = 0;
        int devnum_tv = 0;
        int devnum_light = 0;
        int subType2 = 0;
        try {
            JSONObject jsonObject = new JSONObject(info);
            subType2 = jsonObject.getInt("subType2");
            if (subType2 == 0) {
                devnum_air = jsonObject.getInt("air");
                if (devnum_air > 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("dev_rows");
                    for (int i = 0; i < devnum_air; i++) {
                        WareAirCondDev airCondDev = new WareAirCondDev();
                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        WareDev dev = new WareDev();
                        dev.setCanCpuId(jsonobj.getString("canCpuID"));
                        dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                        dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        MyApplication.getWareData().getRooms().add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        dev.setDevId(jsonobj.getInt("devID"));
                        dev.setType(jsonobj.getInt("devType"));
                        dev.setbOnOff(jsonobj.getInt("bOnOff"));
                        airCondDev.setDev(dev);
                        airCondDev.setbOnOff(jsonobj.getInt("bOnOff"));
                        airCondDev.setSelTemp(jsonobj.getInt("selTemp"));
                        airCondDev.setSelSpd(jsonobj.getInt("selSpd"));
                        airCondDev.setPowChn(jsonobj.getInt("powChn"));
                        airCondDev.setSelDirect(jsonobj.getInt("selDirect"));
                        airCondDev.setRev1(jsonobj.getInt("rev1"));

                        boolean IsContain = false;
                        for (int j = 0; j < MyApplication.getWareData().getDevs().size(); j++) {
                            if (dev.getCanCpuId().equals(MyApplication.getWareData().getDevs().get(j).getCanCpuId())
                                    && dev.getDevId() == MyApplication.getWareData().getDevs().get(j).getDevId()
                                    && dev.getType() == MyApplication.getWareData().getDevs().get(j).getType()) {
                                IsContain = true;
                            }
                        }
                        if (!IsContain) {
                            MyApplication.getWareData().getDevs().add(dev);
                            MyApplication.getWareData().getAirConds().add(airCondDev);
                        }
                    }
                }
            }
            if (subType2 == 1) {
                devnum_tv = jsonObject.getInt("tv");
                if (devnum_tv > 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("dev_rows");
                    for (int i = 0; i < devnum_tv; i++) {
                        WareTv tv = new WareTv();

                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        WareDev dev = new WareDev();
                        dev.setCanCpuId(jsonobj.getString("canCpuID"));
                        dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                        dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        MyApplication.getWareData().getRooms().add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        dev.setDevId(jsonobj.getInt("devID"));
                        dev.setType(jsonobj.getInt("devType"));
                        tv.setDev(dev);

                        boolean IsContain = false;
                        for (int j = 0; j < MyApplication.getWareData().getDevs().size(); j++) {
                            if (dev.getCanCpuId().equals(MyApplication.getWareData().getDevs().get(j).getCanCpuId())
                                    && dev.getDevId() == MyApplication.getWareData().getDevs().get(j).getDevId()
                                    && dev.getType() == MyApplication.getWareData().getDevs().get(j).getType()) {
                                IsContain = true;
                            }
                        }
                        if (!IsContain) {
                            MyApplication.getWareData().getDevs().add(dev);
                            MyApplication.getWareData().getTvs().add(tv);
                        }
                    }
                }
            }
            if (subType2 == 2) {
                devnum_box = jsonObject.getInt("tvUP");
                if (devnum_box > 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("dev_rows");
                    for (int i = 0; i < devnum_box; i++) {
                        WareSetBox box = new WareSetBox();
                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        WareDev dev = new WareDev();
                        dev.setCanCpuId(jsonobj.getString("canCpuID"));
                        dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                        dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        MyApplication.getWareData().getRooms().add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        dev.setDevId(jsonobj.getInt("devID"));
                        dev.setType(jsonobj.getInt("devType"));
                        box.setDev(dev);

                        boolean IsContain = false;
                        for (int j = 0; j < MyApplication.getWareData().getDevs().size(); j++) {
                            if (dev.getCanCpuId().equals(MyApplication.getWareData().getDevs().get(j).getCanCpuId())
                                    && dev.getDevId() == MyApplication.getWareData().getDevs().get(j).getDevId()
                                    && dev.getType() == MyApplication.getWareData().getDevs().get(j).getType()) {
                                IsContain = true;
                            }
                        }
                        if (!IsContain) {
                            MyApplication.getWareData().getDevs().add(dev);
                            MyApplication.getWareData().getStbs().add(box);
                        }
                    }
                }
            }
            if (subType2 == 3) {
                devnum_light = jsonObject.getInt("light");
                if (devnum_light > 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("dev_rows");
                    for (int i = 0; i < devnum_light; i++) {
                        WareLight light = new WareLight();
                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        WareDev dev = new WareDev();
                        dev.setCanCpuId(jsonobj.getString("canCpuID"));
                        dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                        dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        MyApplication.getWareData().getRooms().add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        dev.setDevId(jsonobj.getInt("devID"));
                        dev.setType(jsonobj.getInt("devType"));
                        dev.setbOnOff(jsonobj.getInt("bOnOff"));

                        light.setDev(dev);
                        light.setbOnOff(jsonobj.getInt("bOnOff"));
                        light.setbTuneEn(jsonobj.getInt("bTuneEn"));
                        light.setLmVal(jsonobj.getInt("lmVal"));
                        light.setPowChn(jsonobj.getInt("powChn"));
                        boolean IsContain = false;
                        for (int j = 0; j < MyApplication.getWareData().getDevs().size(); j++) {
                            if (dev.getCanCpuId().equals(MyApplication.getWareData().getDevs().get(j).getCanCpuId())
                                    && dev.getDevId() == MyApplication.getWareData().getDevs().get(j).getDevId()
                                    && dev.getType() == MyApplication.getWareData().getDevs().get(j).getType()) {
                                IsContain = true;
                            }
                        }
                        if (!IsContain) {
                            MyApplication.getWareData().getDevs().add(dev);
                            MyApplication.getWareData().getLights().add(light);
                        }
                    }
                }
            }
            if (subType2 == 4) {
                devnum_cur = jsonObject.getInt("curtain");
                if (devnum_cur > 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("dev_rows");
                    for (int i = 0; i < devnum_cur; i++) {
                        WareCurtain curtain = new WareCurtain();
                        JSONObject jsonobj = jsonArray.getJSONObject(i);
                        WareDev dev = new WareDev();
                        dev.setCanCpuId(jsonobj.getString("canCpuID"));
                        dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                        dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        MyApplication.getWareData().getRooms().add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                        dev.setDevId(jsonobj.getInt("devID"));
                        dev.setType(jsonobj.getInt("devType"));
                        dev.setbOnOff(jsonobj.getInt("bOnOff"));

                        curtain.setDev(dev);
                        curtain.setbOnOff(jsonobj.getInt("bOnOff"));
                        curtain.setPowChn(jsonobj.getInt("powChn"));
                        curtain.setbOnOff(jsonobj.getInt("timRun"));

                        boolean IsContain = false;
                        for (int j = 0; j < MyApplication.getWareData().getDevs().size(); j++) {
                            if (dev.getCanCpuId().equals(MyApplication.getWareData().getDevs().get(j).getCanCpuId())
                                    && dev.getDevId() == MyApplication.getWareData().getDevs().get(j).getDevId()
                                    && dev.getType() == MyApplication.getWareData().getDevs().get(j).getType()) {
                                IsContain = true;
                            }
                        }
                        if (!IsContain) {
                            MyApplication.getWareData().getDevs().add(dev);
                            MyApplication.getWareData().getCurtains().add(curtain);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }

    /**
     * 单个控制设备返回状态数据
     */
    public void refreshDevData(String info) {

//        控制模块都得数据类型；
//        {
//            "devUnitID":	"37ffdb05424e323416702443",
//                "datType":	4,
//                "subType1":	1,
//                "subType2":	1,
//                "dev_rows":	[{
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"6b7400000000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	0,
//                    "devID":	0,
//                    "bOnOff":	0,
//                    "selMode":	0,
//                    "selTemp":	24,
//                    "selSpd":	5,
//                    "selDirect":	0,
//                    "rev1":	24,
//                    "powChn":	31
//        }]
//        }

        int devType = -1;

        try {
            JSONObject jsonObject = new JSONObject(info);

            JSONArray array = jsonObject.getJSONArray("dev_rows");

            devType = array.getJSONObject(0).getInt("devType");

            if (devType == 4) {
                WareCurtain curtain = new WareCurtain();
                JSONObject jsonobj = array.getJSONObject(0);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                dev.setbOnOff(jsonobj.getInt("bOnOff"));
                curtain.setDev(dev);
                curtain.setbOnOff(jsonobj.getInt("bOnOff"));
                curtain.setPowChn(jsonobj.getInt("powChn"));
//                MyApplication.getWareData().getCurtains().add(curtain);

                for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                    WareCurtain curtain1 = MyApplication.getWareData().getCurtains().get(i);
                    if (curtain1.getDev().getCanCpuId().equals(curtain.getDev().getCanCpuId())
                            && curtain1.getDev().getDevId() == curtain.getDev().getDevId()) {
                        MyApplication.getWareData().getCurtains().set(i, curtain);
                    }
                }
            } else if (devType == 0) {

                WareAirCondDev airCondDev = new WareAirCondDev();

                JSONObject jsonobj = array.getJSONObject(0);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                airCondDev.setDev(dev);
                airCondDev.setbOnOff(jsonobj.getInt("bOnOff"));
                airCondDev.setPowChn(jsonobj.getInt("powChn"));
                airCondDev.setRev1(jsonobj.getInt("rev1"));
                airCondDev.setSelDirect(jsonobj.getInt("selDirect"));
//                airCondDev.setSelMode(jsonobj.getInt("selMode"));
                airCondDev.setSelSpd(jsonobj.getInt("selSpd"));
                airCondDev.setSelTemp(jsonobj.getInt("selTemp"));

                for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                    WareAirCondDev air = MyApplication.getWareData().getAirConds().get(i);
                    if (air.getDev().getCanCpuId().equals(airCondDev.getDev().getCanCpuId())
                            && air.getDev().getDevId() == airCondDev.getDev().getDevId()) {
                        MyApplication.getWareData().getAirConds().set(i, airCondDev);
                    }
                }
            } else if (devType == 3) {

                WareLight wareLight = new WareLight();

                JSONObject jsonobj = array.getJSONObject(0);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                wareLight.setDev(dev);
                wareLight.setbOnOff(jsonobj.getInt("bOnOff"));
                wareLight.setPowChn(jsonobj.getInt("powChn"));
                wareLight.setbTuneEn(jsonobj.getInt("bTuneEn"));
                wareLight.setLmVal(jsonobj.getInt("lmVal"));
                for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                    WareLight light = MyApplication.getWareData().getLights().get(i);
                    if (light.getDev().getCanCpuId().equals(wareLight.getDev().getCanCpuId())
                            && light.getDev().getDevId() == wareLight.getDev().getDevId()) {
                        MyApplication.getWareData().getLights().set(i, wareLight);
                        Log.i("Light", wareLight.getDev().getDevId() + "");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }


    /**
     * 设备操作[添加]
     */
    public void addDev_result(String info) {
//        {
//        "devUnitID":	"39ffd505484d303408650743",
//                "datType":	5,
//                "subType1":	1,
//                "subType2":	1,
//                "dev_rows":	[{
//            "canCpuID":	"36ffd4054842373532790843",
//                    "devName":	"e3bedd2aa9",
//                    "roomName":	"6d9e7e61ee338e60f4338e60",
//                    "devType":	0,
//                    "devID":	0,
//                    "bOnOff":	0,
//                    "selTemp":	0,
//                    "selSpd":	0,
//                    "selDirect":	0,
//                    "rev1":	0,
//                    "powChn":	21
//        }]
//    }

        int devType = -1;
        try {
            JSONObject jsonObject = new JSONObject(info);

            JSONArray array = jsonObject.getJSONArray("dev_rows");

            devType = array.getJSONObject(0).getInt("devType");
            WareDev dev = null;
            if (devType == 4) {
                WareCurtain curtain = new WareCurtain();
                JSONObject jsonobj = array.getJSONObject(0);
                dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                dev.setbOnOff(jsonobj.getInt("bOnOff"));
                curtain.setDev(dev);
                curtain.setbOnOff(jsonobj.getInt("bOnOff"));
                curtain.setPowChn(jsonobj.getInt("powChn"));
                MyApplication.getWareData().getCurtains().add(curtain);

            } else if (devType == 0) {
                WareAirCondDev airCondDev = new WareAirCondDev();
                JSONObject jsonobj = array.getJSONObject(0);
                dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                airCondDev.setDev(dev);
                airCondDev.setbOnOff(jsonobj.getInt("bOnOff"));
                airCondDev.setPowChn(jsonobj.getInt("powChn"));
                airCondDev.setRev1(jsonobj.getInt("rev1"));
                airCondDev.setSelDirect(jsonobj.getInt("selDirect"));
                airCondDev.setSelSpd(jsonobj.getInt("selSpd"));
                airCondDev.setSelTemp(jsonobj.getInt("selTemp"));
                MyApplication.getWareData().getAirConds().add(airCondDev);
            } else if (devType == 3) {

                WareLight wareLight = new WareLight();

                JSONObject jsonobj = array.getJSONObject(0);
                dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                wareLight.setDev(dev);
                wareLight.setbOnOff(jsonobj.getInt("bOnOff"));
                wareLight.setPowChn(jsonobj.getInt("powChn"));
                wareLight.setbTuneEn(jsonobj.getInt("bTuneEn"));
                wareLight.setLmVal(jsonobj.getInt("lmVal"));
                MyApplication.getWareData().getLights().add(wareLight);
            }
            boolean isContain = false;
            for (int i = 0; i < MyApplication.getWareData().getRooms().size(); i++) {
                if (dev.getRoomName().equals(MyApplication.getWareData().getRooms()))
                    isContain = true;
            }
            if (!isContain)
                MyApplication.getWareData().getRooms().add(dev.getRoomName());
        } catch (Exception e) {
            isFreshData = false;
            System.out.println(e.toString());
        }

    }

    /**
     * 设备操作[编辑]
     */
    public void editDev_result(String info) {
//        {
//        "devUnitID":	"39ffd505484d303408650743",
//                "datType":	6,
//                "subType1":	1,
//                "subType2":	1,
//                "dev_rows":	[{
//            "canCpuID":	"36ffd4054842373532790843",
//                    "devName":	"e3bedd2aa9",
//                    "roomName":	"6d9e7e61ee338e60f4338e60",
//                    "devType":	0,
//                    "devID":	0,
//                    "bOnOff":	0,
//                    "selTemp":	0,
//                    "selSpd":	0,
//                    "selDirect":	0,
//                    "rev1":	0,
//                    "powChn":	21
//        }]
//    }
        refreshDevData(info);

    }

    /**
     * 设备操作[删除]
     */
    public void deleteDev_result(String info) {
//         {
//        "devUnitID":	"39ffd505484d303408650743",
//                "datType":	7,
//                "subType1":	1,
//                "subType2":	1,
//                "dev_rows":	[{
//            "canCpuID":	"36ffd4054842373532790843",
//                    "devName":	"",
//                    "roomName":	"",
//                    "devType":	0,
//                    "devID":	0,
//                    "bOnOff":	0,
//                    "selTemp":	0,
//                    "selSpd":	0,
//                    "selDirect":	0,
//                    "rev1":	0,
//                    "powChn":	0
//        }]
//    }
        int devType = -1;

        try {
            JSONObject jsonObject = new JSONObject(info);

            JSONArray array = jsonObject.getJSONArray("dev_rows");

            devType = array.getJSONObject(0).getInt("devType");

            if (devType == 4) {
                WareCurtain curtain = new WareCurtain();
                JSONObject jsonobj = array.getJSONObject(0);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                dev.setbOnOff(jsonobj.getInt("bOnOff"));
                curtain.setDev(dev);
//                MyApplication.getWareData().getCurtains().add(curtain);

                for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                    WareCurtain curtain1 = MyApplication.getWareData().getCurtains().get(i);
                    if (curtain1.getDev().getCanCpuId().equals(curtain.getDev().getCanCpuId())
                            && curtain1.getDev().getDevId() == curtain.getDev().getDevId()) {
                        MyApplication.getWareData().getCurtains().remove(i);
                        break;
                    }
                }
                for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                    WareDev dev1 = MyApplication.getWareData().getDevs().get(i);
                    if (dev1.getCanCpuId().equals(curtain.getDev().getCanCpuId())
                            && dev1.getDevId() == curtain.getDev().getDevId()) {
                        MyApplication.getWareData().getDevs().remove(i);
                        break;
                    }
                }
            } else if (devType == 0) {

                WareAirCondDev airCondDev = new WareAirCondDev();

                JSONObject jsonobj = array.getJSONObject(0);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                airCondDev.setDev(dev);
                for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                    WareAirCondDev air = MyApplication.getWareData().getAirConds().get(i);
                    if (air.getDev().getCanCpuId().equals(airCondDev.getDev().getCanCpuId())
                            && air.getDev().getDevId() == airCondDev.getDev().getDevId()) {
                        MyApplication.getWareData().getAirConds().remove(i);
                        break;
                    }
                }
                for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                    WareDev dev1 = MyApplication.getWareData().getDevs().get(i);
                    if (dev1.getCanCpuId().equals(airCondDev.getDev().getCanCpuId())
                            && dev1.getDevId() == airCondDev.getDev().getDevId()) {
                        MyApplication.getWareData().getDevs().remove(i);
                        break;
                    }
                }
            } else if (devType == 3) {

                WareLight wareLight = new WareLight();

                JSONObject jsonobj = array.getJSONObject(0);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                wareLight.setDev(dev);

                for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                    WareLight light = MyApplication.getWareData().getLights().get(i);
                    if (light.getDev().getCanCpuId().equals(wareLight.getDev().getCanCpuId())
                            && light.getDev().getDevId() == wareLight.getDev().getDevId()) {
                        MyApplication.getWareData().getLights().remove(i);
                        Log.i("Light", wareLight.getDev().getDevId() + "");
                        break;
                    }
                }
                for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                    WareDev dev1 = MyApplication.getWareData().getDevs().get(i);
                    if (dev1.getCanCpuId().equals(wareLight.getDev().getCanCpuId())
                            && dev1.getDevId() == wareLight.getDev().getDevId()) {
                        MyApplication.getWareData().getDevs().remove(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }


    /**
     * 输出板按键信息
     */
    public void getkeyOutBoard(String info) {

//        返回数据类型；
//        {
//            "devUnitID": "37ffdb05424e323416702443",
//                "datType": 8,
//                "subType1": 1,
//                "subType2": 0,
//                "chnout_rows": [
//            {
//                "canCpuID": "31ffdf054257313827502543",
//                    "boardName": "3132cda8b5c0303031000",
//                    "boardType": 0,
//                    "chnCnt": 12,
//                    "bOnline": 0,
//                    "chnName_rows": [
//                "cda8b5c0300000000",
//                        "cda8b5c0310000000",
//                        "cda8b5c0320000000",
//                        "cda8b5c0330000000",
//                        "cda8b5c0340000000",
//                        "cda8b5c0350000000",
//                        "cda8b5c0360000000",
//                        "cda8b5c0370000000",
//                        "cda8b5c0380000000",
//                        "cda8b5c0390000000",
//                        "cda8b5c03130000000",
//                        "cda8b5c03131000000"
//                ]
//            }
//            ],
//            "board": 1
//        }
        try {
            JSONObject jsonObject = new JSONObject(info);
            int board = jsonObject.getInt("board");
            if (board == 0) {
                isFreshData = false;
                return;
            }
            JSONArray array = jsonObject.getJSONArray("chnout_rows");
            for (int i = 0; i < array.length(); i++) {
                boolean isContains = false;
                WareBoardChnout chnout = new WareBoardChnout();
                JSONObject object = array.getJSONObject(i);
                chnout.setDevUnitID(object.getString("canCpuID"));
                chnout.setBoardName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(object.getString("boardName"))));
                chnout.setBoardType(object.getInt("boardType"));
                chnout.setbOnline(object.getInt("bOnline"));
                chnout.setChnCnt(object.getInt("chnCnt"));

                JSONArray array1 = object.getJSONArray("chnName_rows");
                String[] name = new String[array1.length()];
                for (int j = 0; j < array1.length(); j++) {
                    name[j] = array1.getString(j);
                }
                chnout.setChnName(name);

                if (MyApplication.getWareData().getBoardChnouts().size() > 0) {
                    for (int k = 0; k < MyApplication.getWareData().getBoardChnouts().size(); k++) {
                        if (chnout.getBoardName().equals(MyApplication.getWareData().getBoardChnouts().get(k).getBoardName()) && chnout.getDevUnitID().equals(MyApplication.getWareData().getBoardChnouts().get(k).getDevUnitID())) {
                            isContains = true;
                        }
                    }
                    if (!isContains)
                        MyApplication.getWareData().getBoardChnouts().add(chnout);
                    else
                        isFreshData = false;
                } else {
                    MyApplication.getWareData().getBoardChnouts().add(chnout);
                }
            }


        } catch (JSONException e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }

    /**
     * 获取输入板按键
     */
    public void getKyeInputBoard(String info) {

//        返回数据类型；
//        {
//        "devUnitID": "37ffdb05424e323416702443",
//                "datType": 8,
//                "subType1": 1,
//                "subType2": 1,
//                "keyinput_rows": [
//        {
//            "canCpuID": "50ff6c067184515640421267",
//                "boardName": "b0b4bcfcb0e53132363700",
//                "boardType": 1,
//                "keyCnt": 6,
//                "ledBkType": 0,
//                "keyName_rows": [
//                    "b0b4bcfc310000000",
//                    "b0b4bcfc320000000",
//                    "b0b4bcfc330000000",
//                    "b0b4bcfc340000000",
//                    "b0b4bcfc350000000",
//                    "b0b4bcfc360000000",
//            ],
//            "keyAllCtrlType_rows": []
//        }
//        ],
//        "keyinput": 1
//    }
        try {
            JSONObject jsonObject = new JSONObject(info);
            int keyinput = jsonObject.getInt("keyinput");
            if (keyinput == 0) {
                isFreshData = false;
                return;
            }

            JSONArray array = jsonObject.getJSONArray("keyinput_rows");
            for (int i = 0; i < array.length(); i++) {
                boolean isContains = false;
                WareBoardKeyInput input = new WareBoardKeyInput();
                JSONObject object = array.getJSONObject(i);
                input.setCanCpuID(object.getString("canCpuID"));
                input.setBoardName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(object.getString("boardName"))));
                input.setBoardType(object.getInt("boardType"));
                input.setKeyCnt(object.getInt("keyCnt"));
                input.setLedBkType(object.getInt("ledBkType"));

                JSONArray array1 = object.getJSONArray("keyName_rows");
                String[] name = new String[array1.length()];
                for (int j = 0; j < array1.length(); j++) {
                    try {
                        name[j] = CommonUtils.getGBstr(CommonUtils.hexStringToBytes(array1.getString(j)));
                    } catch (Exception e) {
                        name[j] = "";
                    }
                }
                input.setKeyName(name);

                if (MyApplication.getWareData().getKeyInputs().size() > 0) {
                    for (int k = 0; k < MyApplication.getWareData().getKeyInputs().size(); k++) {
                        if (MyApplication.getWareData().getKeyInputs().get(k).getBoardName().equals(input.getBoardName())
                                && input.getCanCpuID().equals(MyApplication.getWareData().getKeyInputs().get(k).getCanCpuID())) {
                            isContains = true;
                        }
                    }
                    if (!isContains)
                        MyApplication.getWareData().getKeyInputs().add(input);
                    else
                        isFreshData = false;
                } else {
                    MyApplication.getWareData().getKeyInputs().add(input);
                }
            }
        } catch (JSONException e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }


    /**
     * 获取输入板设备详情
     */
    public void getKeyOpItem(String info) {

//        返回数据类型；
//       {
//        "devUnitID":	"39ffd505484d303408650743",
//                "datType":	11,
//                "subType1":	1,
//                "subType2":	0,
//                "key_opitem_rows":	[{
//            "key_cpuCanID":	"48ff6c065087485725170287",
//                    "out_cpuCanID":	"36ffd4054842373532790843",
//                    "devType":	0,
//                    "devID":	0,
//                    "keyOpCmd":	0,
//                    "keyOp":	1
//        }, {
//            "key_cpuCanID":	"48ff6c065087485725170287",
//                    "out_cpuCanID":	"36ffd4054842373532790843",
//                    "devType":	3,
//                    "devID":	7,
//                    "keyOpCmd":	5,
//                    "keyOp":	1
//        }],
//        "key_opitem":	2
//    }
        MyApplication.getWareData().setKeyOpItems(new ArrayList<WareKeyOpItem>());
        try {
            JSONObject jsonObject = new JSONObject(info);
            JSONArray array = jsonObject.getJSONArray("key_opitem_rows");
            List<WareKeyOpItem> WareKeyOpItem = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                WareKeyOpItem opItem = new WareKeyOpItem();
                JSONObject object = array.getJSONObject(i);
                opItem.setOut_cpuCanID(object.getString("out_cpuCanID"));
                opItem.setKey_cpuCanID(object.getString("key_cpuCanID"));
                opItem.setDevType(object.getInt("devType"));
                opItem.setDevId(object.getInt("devID"));
                opItem.setKeyOpCmd(object.getInt("keyOpCmd"));
                opItem.setKeyOp(object.getInt("keyOp"));
                WareKeyOpItem.add(opItem);
            }
            MyApplication.getWareData().setKeyOpItems(WareKeyOpItem);

        } catch (JSONException e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }

    /**
     * 删除输入板设备
     */
    public void delKeyOpItem_result(String info) {
//        {
//            "devUnitID":	"37ffdb05424e323416702443",
//                "datType":	12,
//                "subType1":	1,
//                "subType2":	0,
//                "canCpuID":	"50ff6c067184515640421267",
//                "result":	1
//        }
        MyApplication.getWareData().getKeyOpItems().clear();
        Gson gson = new Gson();
        SetEquipmentResult result = gson.fromJson(info, SetEquipmentResult.class);
        MyApplication.getWareData().setResult(result);
    }

    /**
     * 删除*******
     */
    public void delChnOpItem_result(String info) {
//        {
//            "devUnitID":	"37ffdb05424e323416702443",
//                "datType":	12,
//                "subType1":	1,
//                "subType2":	0,
//                "canCpuID":	"50ff6c067184515640421267",
//                "result":	1
//        }
        MyApplication.getWareData().getChnOpItems().clear();
        Gson gson = new Gson();
        SetEquipmentResult result = gson.fromJson(info, SetEquipmentResult.class);
        MyApplication.getWareData().setResult(result);
    }

    /**
     * 修改联网模块防区信息
     */
    public void safety_result(String info) {
        try {
            MyApplication.getWareData().getChnOpItems().clear();
            Gson gson = new Gson();
            SetEquipmentResult result = gson.fromJson(info, SetEquipmentResult.class);
            MyApplication.getWareData().setResult(result);
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
    }

    //获取定时器数据
    public void getTimerData(String info) {
        MyApplication.getWareData().setTimer_data(null);
        Gson gson = new Gson();
        Timer_Data result = gson.fromJson(info, Timer_Data.class);
        MyApplication.getWareData().setTimer_data(result);
    }

    //保存定时器 数据返回
    public void setTimerData_back(String info) {
//        {
//            "devUnitID":	"39ffd805484d303416600643",
//                "datType":	19,
//                "subType1":	0,
//                "subType2":	1,
//                "timerEvent_rows":	[{
//            "timSta":	[9, 5, 0, 3],
//            "timEnd":	[13, 5, 0, 0],
//            "timerName":	"b6a8cab1c6f7310000000000",
//                    "devCnt":	3,
//                    "eventId":	1,
//                    "valid":	0,
//                    "rev3":	0,
//                    "run_dev_item":	[{
//                "canCpuID":	"36ffd7054842373507781843",
//                        "devID":	7,
//                        "devType":	3,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	0,
//                        "bOnOff":	1,
//                        "param1":	0,
//                        "param2":	0
//            }, {
//                "canCpuID":	"36ffd7054842373507781843",
//                        "devID":	8,
//                        "devType":	3,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	0,
//                        "bOnOff":	1,
//                        "param1":	0,
//                        "param2":	0
//            }, {
//                "canCpuID":	"36ffd7054842373507781843",
//                        "devID":	9,
//                        "devType":	3,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	0,
//                        "bOnOff":	1,
//                        "param1":	0,
//                        "param2":	0
//            }]
//        }],
//            "itemCnt":	1
//        }
    }

    //获取触发器数据
    public void getConditiondata(String info) {
        MyApplication.getWareData().setTimer_data(null);
        Gson gson = new Gson();
        Condition_Event_Bean result = gson.fromJson(info, Condition_Event_Bean.class);
        MyApplication.getWareData().setCondition_event_bean(result);
    }

    //撤防、布防
    public void safety(String info) {
        MyApplication.getWareData().getChnOpItems().clear();
        Gson gson = new Gson();
        SetEquipmentResult result = gson.fromJson(info, SetEquipmentResult.class);
        MyApplication.getWareData().setResult(result);
    }

    //防区警报信息
    public void safety_alarm(String info) {
        MyApplication.getWareData().getChnOpItems().clear();
        Gson gson = new Gson();
        SetSafetyResult_alarm result = gson.fromJson(info, SetSafetyResult_alarm.class);
        MyApplication.getWareData().setSafetyResult_alarm(result);
    }

    /**
     * 编辑输入板设备、编辑输出板按键、编辑按键情景
     */
    public void setKeyOpItem_result(String info) {
        Gson gson = new Gson();
        SetEquipmentResult result = gson.fromJson(info, SetEquipmentResult.class);
        MyApplication.getWareData().setResult(result);
    }

    /**
     * 获取组合设置信息
     */
    public void getGroupSetData(String info) {
        Gson gson = new Gson();
        Log.i("JSON", info);
        GroupSet_Data result = gson.fromJson(info, GroupSet_Data.class);
        MyApplication.getWareData().setmGroupSet_Data(result);
    }

    /**
     * 编辑组合设置
     */
    public void setGroupSetData(String info) {
//        {
//            "devUnitID":	"39ffd905484d303429620443",
//                "datType":	66,
//                "subType1":	2,
//                "subType2":	1,
//                "secs_trigger_rows":	[{
//            "triggerName":	"c6e6b9d6b99d38b9a9b9b9b9",
//                    "triggerSecs":	0,
//                    "triggerId":	0,
//                    "reportServ":	1,
//                    "valid":	1,
//                    "devCnt":	2,
//                    "run_dev_item":	[{
//                "canCpuID":	"36ffd7054842373507701843",
//                        "devID":	2,
//                        "devType":	3,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	0,
//                        "bOnOff":	1,
//                        "param1":	0,
//                        "param2":	0
//            }, {
//                "canCpuID":	"36ffd7054842373507701843",
//                        "devID":	3,
//                        "devType":	3,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	0,
//                        "bOnOff":	1,
//                        "param1":	0,
//                        "param2":	0
//            }]
//        }],
//            "itemCnt":	1
//        }

        try {
            JSONObject jsonObject = new JSONObject(info);
            JSONArray jsonArray = jsonObject.getJSONArray("secs_trigger_rows");
            GroupSet_Data.SecsTriggerRowsBean bean = new GroupSet_Data.SecsTriggerRowsBean();
            List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> list_dev = new ArrayList<>();
            GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean decbean = new GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean();

            for (int i = 0; i < MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().size(); i++) {
                if (MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(i).getTriggerId()
                        == jsonArray.getJSONObject(0).getInt("triggerId")) {
                    bean.setTriggerName(jsonArray.getJSONObject(0).getString("triggerName"));
                    bean.setReportServ(jsonArray.getJSONObject(0).getInt("reportServ"));
                    bean.setTriggerId(jsonArray.getJSONObject(0).getInt("triggerId"));
                    bean.setDevCnt(jsonArray.getJSONObject(0).getInt("devCnt"));
                    bean.setValid(jsonArray.getJSONObject(0).getInt("valid"));
                    JSONArray jsonArray_dev = jsonArray.getJSONObject(0).getJSONArray("run_dev_item");
                    for (int j = 0; j < jsonArray_dev.length(); j++) {
                        decbean.setCanCpuID(jsonArray_dev.getJSONObject(j).getString("canCpuID"));
                        decbean.setBOnOff(jsonArray_dev.getJSONObject(j).getInt("bOnOff"));
                        decbean.setDevID(jsonArray_dev.getJSONObject(j).getInt("devID"));
                        decbean.setDevType(jsonArray_dev.getJSONObject(j).getInt("devType"));
                        list_dev.add(decbean);
                    }
                    bean.setRun_dev_item(list_dev);
                    MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().set(i, bean);
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "数据异常" + e);
        }
    }

    /**
     * 查询联网模块防区信息
     */
    public void setSafetyEvents(String info) {
        Gson gson = new Gson();
        SetSafetyResult result = gson.fromJson(info, SetSafetyResult.class);
        MyApplication.getWareData().setResult_safety(result);
    }

    /**
     * 查询联网模块防区信息
     */
    public void getChnOpItem_scene(String info) {
        Gson gson = new Gson();
        ChnOpItem_scene result = gson.fromJson(info, ChnOpItem_scene.class);
        MyApplication.getWareData().setChnOpItem_scene(result);
    }


    /**
     * 获取按键详情
     */
    public void getChnOpItem(String info) {
//        {
//            "devUnitID":	"39ffd505484d303408650743",
//                "datType":	14,
//                "subType1":	1,
//                "subType2":	1,
//                "chn_opitem_rows":	[{
//            "key_cpuCanID":	"48ff6c065087485725170287",
//                    "keyDownValid":	0,
//                    "keyUpValid":	0,
//                    "rev1":	0,
//                    "keyUpCmd":	[0, 0, 0, 0, 0, 0, 0, 0],
//            "rev2":	0,
//                    "keyDownCmd":	[0, 0, 0, 0, 0, 0, 0, 0],
//            "rev3":	0
//        }],
//            "out_cpuCanID":	"36ffd4054842373532790843",
//                "devType":	3,
//                "devID":	2,
//                "chn_opitem":	1
//        }
        List<WareChnOpItem> inputs = new ArrayList<>();
        try {
            boolean isNull = true;
            JSONObject object = new JSONObject(info);
            JSONArray array = object.getJSONArray("chn_opitem_rows");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object_rows = array.getJSONObject(i);
                WareChnOpItem item = new WareChnOpItem();
                item.setCancupid(object_rows.getString("key_cpuCanID"));
                item.setKeyDownValid(object_rows.getInt("keyDownValid"));
                item.setKeyUpValid(object_rows.getInt("keyUpValid"));
                JSONArray array_up_cmd = object_rows.getJSONArray("keyUpCmd");
                int[] up_cmd = new int[8];
                for (int j = 0; j < array_up_cmd.length(); j++) {
                    up_cmd[j] = array_up_cmd.getInt(j);
                }
                item.setKeyUpCmd(up_cmd);

                JSONArray array_down_cmd = object_rows.getJSONArray("keyDownCmd");
                int[] down_cmd = new int[8];
                for (int j = 0; j < array_down_cmd.length(); j++) {
                    down_cmd[j] = array_down_cmd.getInt(j);
                }
                item.setKeyDownCmd(down_cmd);
                inputs.add(item);
                MyApplication.getWareData().setChnOpItems(inputs);
                isNull = false;
            }
            if (isNull) {
                MyApplication.getWareData().setChnOpItems(new ArrayList<WareChnOpItem>());
            }
        } catch (JSONException e) {
            System.out.println(e + "");
        }
    }

    /**
     * 获取情景
     */
    public void getSceneEvents(String info) {
//        情景模式返回数据类型；
//        {
//            "devUnitID":	"39ffd505484d303408650743",
//                "datType":	22,
//                "subType1":	0,
//                "subType2":	1,
//                "scene_rows":	[{
//            "sceneName":	"6868",
//                    "devCnt":	2,
//                    "eventId":	2,
//                    "itemAry":	[{
//                "canCpuID":	"36ffd4054842373532790843",
//                        "devID":	2,
//                        "devType":	3,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	32,
//                        "bOnOff":	0,
//                        "param1":	0,
//                        "param2":	32
//            }, {
//                "canCpuID":	"36ffd4054842373532790843",
//                        "devID":	0,
//                        "devType":	0,
//                        "lmVal":	0,
//                        "rev2":	0,
//                        "rev3":	0,
//                        "bOnOff":	0,
//                        "param1":	0,
//                        "param2":	0
//            }]
//        }],
//            "scene":	1
//        }
        try {
            JSONObject jsonObject = new JSONObject(info);

            JSONArray array = jsonObject.getJSONArray("scene_rows");

            WareSceneEvent event = new WareSceneEvent();
            JSONObject object = array.getJSONObject(0);

            boolean IsSceneIdExist = false;
            boolean IsDevIdExist = false;
            event.setEventId(object.getInt("eventId"));

            List<WareSceneEvent> events_exist = MyApplication.getWareData().getSceneEvents();
            WareSceneEvent event_exist = null;
            for (int i = 0; i < events_exist.size(); i++) {
                if (event.getEventId() == events_exist.get(i).getEventId()) {
                    //相同的情景ID
                    IsSceneIdExist = true;
                    event_exist = events_exist.get(i);
                    JSONArray itemAry = object.getJSONArray("itemAry");

                    if (event_exist.getItemAry().size() != 0 && itemAry.length() != 0)
                        //这两个相同的情景ID下的设备集合不为空

                        for (int j = 0; j < itemAry.length(); j++) {
                            JSONObject object2 = itemAry.getJSONObject(j);
                            WareSceneDevItem item = new WareSceneDevItem();
                            item.setbOnOff(object2.getInt("bOnOff"));
                            item.setDevID(object2.getInt("devID"));
                            item.setDevType(object2.getInt("devType"));
                            item.setCanCpuID(object2.getString("canCpuID"));
                            event.getItemAry().add(item);
                            for (int a = 0; a < event_exist.getItemAry().size(); a++) {
                                WareSceneDevItem item_exist = event_exist.getItemAry().get(a);
                                if (item.getCanCpuID().equals(item_exist.getCanCpuID()) &&
                                        item.getDevID() == item_exist.getDevID() &&
                                        item.getDevType() == item_exist.getDevType()) {
                                    //情景ID相同，并且设备属性页相同
                                    IsDevIdExist = true;
                                } else {
                                    //情景ID相同，但设备不同
                                    IsDevIdExist = false;

                                }
                            }
                        }
                }
            }

            if (IsSceneIdExist && IsDevIdExist)
                return;
            if (IsSceneIdExist && !IsDevIdExist) {
                event_exist.getItemAry().addAll(event.getItemAry());
                return;
            }
            if (!IsSceneIdExist && !IsDevIdExist) {
                event.setDevCnt(object.getInt("devCnt"));
                event.setSceneName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(object.getString("sceneName"))));
                int devCnt = object.getInt("devCnt");
                if (devCnt > 0) {
                    event.setItemAry(new ArrayList<WareSceneDevItem>());
                    JSONArray itemAry = object.getJSONArray("itemAry");
                    for (int j = 0; j < devCnt; j++) {
                        JSONObject object2 = itemAry.getJSONObject(j);
                        WareSceneDevItem item = new WareSceneDevItem();
                        item.setbOnOff(object2.getInt("bOnOff"));
                        item.setDevID(object2.getInt("devID"));
                        item.setDevType(object2.getInt("devType"));
                        item.setCanCpuID(object2.getString("canCpuID"));
                        event.getItemAry().add(item);
                    }
                }
                MyApplication.getWareData().getSceneEvents().add(event);
            }
            MyApplication.getWareData().getSceneEvents();
        } catch (Exception e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }


    /**
     * 添加情景
     */
    public void addSceneEvents(String info) {
//        情景模式返回数据类型；
//       {
//        "devUnitID":	"39ffd505484d303408650743",
//                "datType":	23,
//                "subType1":	0,
//                "subType2":	1,
//                "scene_rows":	[{
//            "sceneName":	"b9feb9fe",
//                    "devCnt":	0,
//                    "eventId":	4,
//                    "itemAry":	[]
//        }],
//        "scene":	1
//    }
        try {
            JSONObject jsonObject = new JSONObject(info);

            JSONArray array = jsonObject.getJSONArray("scene_rows");

            WareSceneEvent event = new WareSceneEvent();
            JSONObject object = array.getJSONObject(0);
            event.setSceneName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(object.getString("sceneName"))));
            event.setEventId(object.getInt("eventId"));
            JSONArray itemAry = object.getJSONArray("itemAry");

            for (int j = 0; j < itemAry.length(); j++) {
                JSONObject object2 = itemAry.getJSONObject(j);
                WareSceneDevItem item = new WareSceneDevItem();
                item.setbOnOff(object2.getInt("bOnOff"));
                item.setDevID(object2.getInt("devID"));
                item.setDevType(object2.getInt("devType"));
                item.setCanCpuID(object2.getString("canCpuID"));
                event.getItemAry().add(item);
            }
            MyApplication.getWareData().getSceneEvents().add(event);
        } catch (JSONException e) {
            isFreshData = false;
            System.out.println(e.toString());
        }

    }

    /**
     * 删除情景
     */

    public void delSceneEvents(String info) {
//        情景模式返回数据类型；
//        {
//            "devUnitID":	"39ffd505484d303408650743",
//                "datType":	25,
//                "subType1":	0,
//                "subType2":	1,
//                "scene_rows":	[{
//            "sceneName":	"e59388e59388",
//                    "devCnt":	0,
//                    "eventId":	4,
//                    "itemAry":	[]
//        }],
//            "scene":	1
//        }
        try {
            JSONObject jsonObject = new JSONObject(info);
            JSONArray array = jsonObject.getJSONArray("scene_rows");
            WareSceneEvent event = new WareSceneEvent();
            JSONObject object = array.getJSONObject(0);

            event.setEventId(object.getInt("eventId"));
            for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                if (MyApplication.getWareData().getSceneEvents().get(i).getEventId() == event.getEventId())
                    MyApplication.getWareData().getSceneEvents().remove(i);
            }
        } catch (Exception e) {
            isFreshData = false;
            System.out.println(e.toString());
        }
    }

    /**
     * 用户信息（用户界面）
     */
    public void getUserEvents(String info) {
//        情景模式返回数据类型；
//        scene_return
//        {
//            "devUnitID": "37ffdb05424e323416702443",
//                "datType": 22,
//                "subType1": 1,
//                "subType2": 0,
//                "scene_rows": [
//            {
//                "sceneName": "c8abbfaac4a3cabd00000000",
//                    "devCnt": 0,
//                    "eventID": 0
//            },
//            {
//                "sceneName": "c8abb9d8c4a3cabd00000000",
//                    "devCnt": 0,
//                    "eventID": 1
//            }
//            ],
//            "scene": 2
//        }
        Gson gson = new Gson();
        UserBean userBean = gson.fromJson(info, UserBean.class);
        MyApplication.getWareData().setUserBeen(userBean);


//        try {
//            JSONObject jsonObject = new JSONObject(info);
//
//            JSONArray array = jsonObject.getJSONArray("dev_rows");
//
//            List<UserBean> list = new ArrayList<>();
//
//            for (int i = 0; i < array.length(); i++) {
//
//                UserBean event = new UserBean();
//                JSONObject objcet = array.getJSONObject(i);
//
//                event.setUserName(objcet.getString("userName"));
//                event.setPasswd(objcet.getString("passwd"));
//                event.setDevUnitID(objcet.getString("devUnitID"));
//                event.setDev_rows(new ArrayList<UserBean.DevRowsBean>());
//                JSONArray dev_rows = objcet.getJSONArray("dev_rows");
//                for (int j = 0; j < dev_rows.length(); j++) {
//                    JSONObject object2 = dev_rows.getJSONObject(j);
//                    UserBean.DevRowsBean item = new UserBean.DevRowsBean();
//                    item.setCanCpuID(object2.getString("canCpuID"));
//                    item.setDevType(object2.getInt("devType"));
//                    item.setDevID(object2.getInt("devID"));
//                    event.getDev_rows().add(item);
//                }
//                list.add(event);
//            }
//            MyApplication.getWareData().setUserBeen((UserBean) list);
//        } catch (JSONException e) {
//            isFreshData = false;
//            System.out.println(e.toString());
//        }
    }

    /**
     * 情景执行返回
     */
    public void ctrlDevReply(String info) {

//        灯返回数据类型；
//        light:
//        {
//            "devUnitID":	"37ffdb05424e323416702443",
//                "datType":	35,
//                "subType1":	1,
//                "subType2":	1,
//                "light_rows":	[{
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c635350000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	5,
//                    "bOnOff":	1,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	5
//        }, {
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c636360000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	6,
//                    "bOnOff":	0,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	6
//        }, {
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c637000000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	7,
//                    "bOnOff":	1,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	7
//        }, {
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c638000000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	8,
//                    "bOnOff":	1,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	8
//        }, {
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c639000000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	9,
//                    "bOnOff":	1,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	9
//        }, {
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c631300000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	10,
//                    "bOnOff":	1,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	10
//        }, {
//            "canCpuID":	"31ffdf054257313827502543",
//                    "devName":	"b5c631310000000000000000",
//                    "roomName":	"ceb4b6a8d2e5000000000000",
//                    "devType":	3,
//                    "devID":	11,
//                    "bOnOff":	1,
//                    "bTuneEn":	0,
//                    "lmVal":	0,
//                    "powChn":	11
//        }],
//            "light":	7
//        }

        try {
            JSONObject jsonObject = new JSONObject(info);

            String devid = jsonObject.getString("devUnitID");

            //判断  设备信息是否为同一个模块
            try {
                if (!GlobalVars.getDevid().equals(devid))
                    return;
            } catch (Exception e) {
                return;
            }
            JSONArray array = jsonObject.getJSONArray("light_rows");
            int num = jsonObject.getInt("light");

            List<WareLight> lights = new ArrayList<>();

            for (int i = 0; i < num; i++) {
                WareLight light = new WareLight();
                JSONObject jsonobj = array.getJSONObject(i);
                WareDev dev = new WareDev();
                dev.setCanCpuId(jsonobj.getString("canCpuID"));
                dev.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("devName"))));
                dev.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(jsonobj.getString("roomName"))));
                dev.setDevId(jsonobj.getInt("devID"));
                dev.setType(jsonobj.getInt("devType"));
                dev.setbOnOff(jsonobj.getInt("bOnOff"));
                light.setDev(dev);
                light.setbOnOff(jsonobj.getInt("bOnOff"));
                light.setPowChn(jsonobj.getInt("powChn"));
                light.setLmVal(jsonobj.getInt("lmVal"));
                light.setbTuneEn(jsonobj.getInt("bTuneEn"));
                lights.add(light);
            }
            MyApplication.getWareData().setLights(lights);
        } catch (JSONException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * list去重；
     */
    private static List<WareDev> removeDuplicateDevs(List<WareDev> list) {
        Log.i(TAG, "removeDuplicateDevs: start");
        List<WareDev> devs = new ArrayList<WareDev>();

        for (int i = 0; i < list.size(); i++) {
            devs.add(list.get(i));
        }

        for (int i = 0; i < devs.size() - 1; i++) {
            for (int j = devs.size() - 1; j > i; j--) {
                if (devs.get(i).getCanCpuId().equals(devs.get(j).getCanCpuId())
                        && devs.get(i).getDevId() == devs.get(j).getDevId()
                        && devs.get(i).getType() == devs.get(j).getType()) {
                    if (devs.get(j).getType() == 0) {
                        for (int k = 0; k < MyApplication.getWareData().getAirConds().size(); k++) {
                            if (devs.get(j).getDevId() == MyApplication.getWareData().getAirConds().get(k).getDev().getDevId()
                                    && devs.get(j).getCanCpuId().equals(MyApplication.getWareData().getAirConds().get(k).getDev().getCanCpuId()))
                                MyApplication.getWareData().getAirConds().remove(k);
                        }
                    }
                    if (devs.get(j).getType() == 1) {
                        for (int k = 0; k < MyApplication.getWareData().getTvs().size(); k++) {
                            if (devs.get(j).getDevId() == MyApplication.getWareData().getTvs().get(k).getDev().getDevId()
                                    && devs.get(j).getCanCpuId().equals(MyApplication.getWareData().getTvs().get(k).getDev().getCanCpuId()))
                                MyApplication.getWareData().getTvs().remove(k);
                        }
                    }
                    if (devs.get(j).getType() == 2) {
                        for (int k = 0; k < MyApplication.getWareData().getStbs().size(); k++) {
                            if (devs.get(j).getDevId() == MyApplication.getWareData().getStbs().get(k).getDev().getDevId()
                                    && devs.get(j).getCanCpuId().equals(MyApplication.getWareData().getStbs().get(k).getDev().getCanCpuId()))
                                MyApplication.getWareData().getStbs().remove(k);
                        }
                    }
                    if (devs.get(j).getType() == 3) {
                        for (int k = 0; k < MyApplication.getWareData().getLights().size(); k++) {
                            if (devs.get(j).getDevId() == MyApplication.getWareData().getLights().get(k).getDev().getDevId()
                                    && devs.get(j).getCanCpuId().equals(MyApplication.getWareData().getLights().get(k).getDev().getCanCpuId()))
                                MyApplication.getWareData().getLights().remove(k);
                        }
                    }
                    if (devs.get(j).getType() == 4) {
                        for (int k = 0; k < MyApplication.getWareData().getCurtains().size(); k++) {
                            if (devs.get(j).getDevId() == MyApplication.getWareData().getCurtains().get(k).getDev().getDevId()
                                    && devs.get(j).getCanCpuId().equals(MyApplication.getWareData().getCurtains().get(k).getDev().getCanCpuId()))
                                MyApplication.getWareData().getCurtains().remove(k);
                        }
                    }
                    devs.remove(j);
                }
            }
        }
        Log.i(TAG, "removeDuplicateDevs: end");
        return devs;
    }

    private static List<String> removeRoomNames(List<String> list) {
        List<String> names = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            names.add(list.get(i));
        }

        for (int i = 0; i < names.size() - 1; i++) {
            for (int j = names.size() - 1; j > i; j--) {
                if (names.get(i).equals(names.get(j))) {
                    names.remove(j);
                }
            }
        }
        return names;
    }
}
