package cn.etsoft.smarthome.NetMessage;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.Utils.GlobalVars;

/**
 * Author：FBL  Time： 2017/6/9.
 */

public class WebSocket_Client {
    //WebSocket_Client 和 address
    private WebSocketClient mWebSocketClient;
    private String address = "ws://123.206.104.89:25002";
    private Handler mHandler;

    /**
     * 初始化WebSocketClient
     */
    public void initSocketClient(final Handler handler) throws URISyntaxException {
        mHandler = handler;
        if (mWebSocketClient == null) {
            mWebSocketClient = new WebSocketClient(new URI(address)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    //连接成功
                    Message message = handler.obtainMessage();
                    message.what = MyApplication.mApplication.WS_OPEN_OK;
                    handler.sendMessage(message);
                }

                @Override
                public void onMessage(String s) {
                    //服务端消息
                    Message message = handler.obtainMessage();
                    message.what = MyApplication.mApplication.WS_DATA_OK;
                    message.obj = s;
                    handler.sendMessage(message);
                }

                @Override
                public void onClose(int i, String s, boolean remote) {
                    //连接关闭
                    closeConnect();
                    Message message = handler.obtainMessage();
                    message.what = MyApplication.mApplication.WS_CLOSE;
                    message.obj = s;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    //连接错误
                    Message message = handler.obtainMessage();
                    message.what = MyApplication.mApplication.WS_Error;
                    message.obj = e;
                    handler.sendMessage(message);
                }
            };
        }
    }

    //连接
    public void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mWebSocketClient.connectBlocking();
                } catch (InterruptedException e) {
                    Message message = mHandler.obtainMessage();
                    message.what = MyApplication.mApplication.WS_Error;
                    message.obj = e;
                    mHandler.sendMessage(message);
                }
            }
        }.start();
    }

    //断开连接
    private void closeConnect() {
        try {
            mWebSocketClient.close();
        } catch (Exception e) {
            Message message = mHandler.obtainMessage();
            message.what = MyApplication.mApplication.WS_Error;
            message.obj = e;
            mHandler.sendMessage(message);
        } finally {
            mWebSocketClient = null;
        }
    }

    /**
     * 发送消息
     */
    public void sendMsg(String msg) {
        try {
            mWebSocketClient.send(msg);
        } catch (Exception e) {
            Message message = mHandler.obtainMessage();
            message.what = MyApplication.mApplication.WS_Error;
            message.obj = e;
            mHandler.sendMessage(message);
        }
    }
}
