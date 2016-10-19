package cn.etsoft.smarthome;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.net.DatagramSocket;
import java.net.SocketException;

import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.ui.WelcomeActivity;

/**
 * 作者：FBL  时间： 2016/8/30.
 */
public class MyApplication extends Application implements udpService.Callback {
    private MyServiceConn conn;
    public static MyApplication mInstance;
    private static WareData wareData;
    private Handler handler = null;
    private Handler mhandler = null;
    private udpService service;
    private static Context context;
    private WelcomeActivity activity;

    private DatagramSocket socket = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = MyApplication.this;
        /**
         * 初始化上下文；
         */
        MyApplication.context = getApplicationContext();

        GlobalVars.init(this.getApplicationContext());

        CommonUtils.CommonUtils_init();

        try {
            socket = new DatagramSocket(8080);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                jniUtils.udpServer();
            }
        }).start();

    }


    public DatagramSocket getSocket() {
        return socket;
    }


    public WelcomeActivity getActivity() {
        return activity;
    }

    public void setActivity(WelcomeActivity activity) {
        this.activity = activity;
    }

    public static Context getContext() {
        return MyApplication.context;
    }

    @Override
    public void getWareData(WareData wareData) {
        MyApplication.wareData = wareData;
        onGetWareDataListener.upDataWareData();
    }

    @Override
    public void getGwData() {

        final String getDevStr = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getDevsInfo.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";

        final String getSceneStr = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getSceneEvents.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";

        final String getChnBoardStr = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getBoards.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":" + UdpProPkt.E_BOARD_TYPE.e_board_chnOut.getValue() +
                " }";

        final String getKeyInputStr = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getBoards.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":" + UdpProPkt.E_BOARD_TYPE.e_board_keyInput.getValue() +
                " }";

        if (MyApplication.getWareData().getAirConds().size() == 0
                || MyApplication.getWareData().getLights().size() == 0) {
            CommonUtils.sendMsg(getDevStr);
        }

        if (MyApplication.getWareData().getSceneEvents().size() < 2) {
            CommonUtils.sendMsg(getSceneStr);
        }

        if (MyApplication.getWareData().getBoardChnouts().size() == 0) {
            CommonUtils.sendMsg(getChnBoardStr);
        }
        if (MyApplication.getWareData().getKeyInputs().size() == 0) {
            CommonUtils.sendMsg(getKeyInputStr);
        }

    }

    public static void setRcuDevIDtoLocal() {

        final String str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_boardCast.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";
        CommonUtils.sendMsg(str);
    }

    public static void getRcuInfo() {

        final String rcu_str = "{" +
                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getRcuInfo.getValue() + "," +
                "\"subType1\":0," +
                "\"subType2\":0}";

        CommonUtils.sendMsg(rcu_str);
    }

    public static void setWareData(WareData wareData) {
        MyApplication.wareData = wareData;
    }

    public static WareData getWareData() {
        return wareData;
    }

    public Handler getAllHandler() {
        return handler;
    }

    public void setAllHandler(Handler handler) {
        this.handler = handler;

    }

    public void setHandler(Handler handler) {
        this.mhandler = handler;
        conn = new MyServiceConn();
        bindService(new Intent(this, udpService.class), conn, BIND_AUTO_CREATE);
    }

    public final class MyServiceConn implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            // TODO Auto-generated method stub
            service = ((udpService.LocalBinder) binder).getService();
            service.runUdpServer(mhandler);
            // 将当前activity添加到接口集合中
            service.addCallback(MyApplication.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            service = null;
        }
    }


    private static OnGetWareDataListener onGetWareDataListener;

    public void setOnGetWareDataListener(OnGetWareDataListener Listener) {
        onGetWareDataListener = Listener;
    }

    public interface OnGetWareDataListener {
        void upDataWareData();
    }
}
