package cn.etsoft.smarthome;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;

import java.net.DatagramSocket;
import java.net.SocketException;

import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
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
    public void getGwData(WareData wareData) {
        //CommonUtils.getDevInfo();
        final String getDevStr = "{\"devUnitID\":\"37ffdb05424e323416702443\"," +
                "\"devPass\":\"16072443\"," +
                "\"datType\":3," +
                "\"subType1\":0," +
                "\"subType2\":0}";

        CommonUtils.sendMsg(getDevStr);


        final String getSceneStr = "{\"devUnitID\":\"37ffdb05424e323416702443\"," +
                "\"devPass\":\"16072443\"," +
                "\"datType\":22," +
                "\"subType1\":0," +
                "\"subType2\":0}";

        CommonUtils.sendMsg(getSceneStr);
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
