package cn.etsoft.smarthome;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareBoardChnout;
import cn.etsoft.smarthome.pullmi.entity.WareBoardKeyInput;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareFreshAir;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareLock;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.entity.WareValve;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;

/**
 * The type Udp service.
 */
public class udpService extends Service {

    private List<Callback> list;

    private WareData wareData;
    private String TAG = this.getClass().getName();

    private boolean isFreshData = false;
    private boolean isFreshGw = false;
    private boolean isDownLoad_OK = false;

    private static final int REFSH_DATA_MSG = 0;
    private static final int GET_GWINFO_MSG = 1;
    private static final int OUTTIME_DOWNLOAD = 1111;
    private static final int OUTTIME_INITUID = 1000;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        list = new ArrayList<Callback>();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return new LocalBinder();
    }

    public final class LocalBinder extends Binder {

        public udpService getService() {
            return udpService.this;
        }
    }

    public void addCallback(Callback callback) {
        list.add(callback);
    }

    public interface Callback {
        //获取数据接口
        public void getWareData(WareData wareData);

        //获取网关接口
        public void getGwData(WareData wareData);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case REFSH_DATA_MSG:
                    Log.i(TAG, "handleMessage: ---list.size()-->" + list.size());
                    // 遍历集合，通知所有的实现类，即activity
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).getWareData(wareData);
                    }
                    break;
                case GET_GWINFO_MSG:
                    // 遍历集合，通知所有的实现类，即activity
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).getGwData(wareData);
                    }
            }
        }
    };

    public void runUdpServer(final Handler handler) {
        LogUtils.LOGE("", ">>>rev data ");

        try {
            Thread.sleep(2000);
            Handler handler1 = MyApplication.mInstance.getAllHandler();
            Message message = handler1.obtainMessage(OUTTIME_INITUID);
            handler1.sendMessage(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!isDownLoad_OK) {
            new Thread(new Runnable() {//查询数据是否加载，否则继续发送数据包
                @Override
                public void run() {
                    try {
                        do {
                            Thread.sleep(4000);
                            Handler handler1 = MyApplication.mInstance.getAllHandler();
                            Message message = handler1.obtainMessage(OUTTIME_DOWNLOAD);
                            handler1.sendMessage(message);
                        } while (!isDownLoad_OK);

                    } catch (InterruptedException e) {
                        Log.i("Exception", "异常信息:" + e);
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {//执行接收数据接口，有数据，则执行；
            @Override
            public void run() {
                wareData = new WareData();

                byte[] lMsg = new byte[1024];
                DatagramPacket packet = new DatagramPacket(lMsg, lMsg.length);
                try {
                    while (true) {
                         MyApplication.mInstance.getSocket().receive(packet);
                        // 处理消息
                        String info = new String(packet.getData(), 0, packet.getLength());
                        Log.i("接收信息：", info);
                        extractData(info, handler);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Error a) {
                    a.printStackTrace();
                } finally {
                    if (MyApplication.mInstance.getSocket() != null) {
                        MyApplication.mInstance.getSocket().close();
                    }
                }
            }
        }).start();
    }

    public void extractData(String info, Handler mhandler) {
        // TODO Auto-generated method stub
        int datType = 0;
        int subType2 = 0;
        int subType1 = 0;

        isDownLoad_OK = true;

        switch (datType) {
            case 0:// e_udpPro_getRcuinfo
                if (subType2 == 1) {
                    //setRcuInfo(udpProData);
                    isFreshGw = true;
                }
                break;
            case 3: // getDevsInfo
                if (subType1 == 1) {
                    //getDevsInfo(udpProData);
                    // 去掉mWareDev重复的
                    //List<WareDev> mWareDevs = CommonUtils.removeDuplicateWareDev(wareData.getDevs());
                    //wareData.setDevs(mWareDevs);
                    isFreshData = true;
                    isDownLoad_OK = true;
                    Message message = mhandler.obtainMessage();
                    if (mhandler != null) {
                        mhandler.sendMessage(message);
                    }
                }
                break;
            case 4: // ctrlDev
                if (subType1 == 1) {
                    //refreshDevData(udpProData);
                    isFreshData = true;
                }
                break;
            case 8:
                if (subType2 == 0) {
                    //getkeyOutBoard(udpProData);
                    isFreshData = true;
                }
                if (subType2 == 1) {
                    //getKyeInputBoard(udpProData);
                    isFreshData = true;
                }

                break;
            case 11: // e_udpPro_getKeyOpItems
                if (subType1 == 1) {
                    //getKeyOpItem(udpProData, subType2);
                }
                break;
            case 12: // e_udpPro_setKeyOpItems
                if (subType1 == 1) {
                    //myApp.getHandler().sendEmptyMessage(MSG_SETKEYITEM_INFO);
                }
                break;
            case 13: // e_udpPro_delKeyOpItems
                if (subType1 == 1) {
                    //myApp.getHandler().sendEmptyMessage(MSG_DELKEYITEM_INFO);
                }
                break;
            case 14: // e_udpPro_getChnOpitems
                if (subType1 == 1) {
                    //getChnOpItem(udpProData);
                }
                break;
            case 15: // e_udpPro_setChnOpitems
                if (subType1 == 1) {
                /*if (getChnOpItemReply(udpProData))
                    myApp.getHandler().sendEmptyMessage(MSG_SETCHNOPITEM_INFO);*/
                }
                break;
            case 22: // e_udpPro_getSceneEvents
                if (subType2 == 1) {
                    // LogUtils.LOGE(TAG, "获取到情景模式数据");
                    //getSceneEvents(udpProData);
                    isFreshData = true;
                }
                break;
            case 23: // e_udpPro_addSceneEvents
                if (subType2 == 1) {
                    // mSceneDevs.clear();
                    // setSceneEvents(udpProData);
                }
                break;
            case 24: // e_udpPro_editSceneEvents
                if (subType2 == 1) {
                    // mSceneDevs.clear();
                    //setSceneEvents(udpProData);
                }
                break;
            case 25: // e_udpPro_delSceneEvents
                if (subType2 == 1) {
                    //delSceneEvents(udpProData);
                }
                break;
            case 26: // e_udpPro_exeSceneEvents
                if (subType2 == 1) {
                    // setSceneEvents(udpProData);

                    isFreshData = true;
                }
                break;
            case 35:// e_udpPro_chns_status
                //ctrlDevReply(udpProData);
                isFreshData = true;
                break;
        }

        if (isFreshData && datType != 2) {
            handler.sendMessage(handler.obtainMessage(REFSH_DATA_MSG, wareData));
            MyApplication.mInstance.getAllHandler().sendMessage(MyApplication.mInstance.getAllHandler().obtainMessage());
            Dtat_Cache.writeFile(MyApplication.getWareData());
            isFreshData = false;
        }
        if (isFreshGw && datType != 2) {
            handler.sendMessage(handler.obtainMessage(GET_GWINFO_MSG, wareData));
            isFreshGw = false;
        }
    }
}
