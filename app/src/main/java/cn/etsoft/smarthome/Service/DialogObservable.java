package cn.etsoft.smarthome.Service;

import java.util.Observable;

/**
 * 观察者，通知dialog更新
 * Author：FBL  Time： 2017/11/3.
 */
public class DialogObservable  extends Observable {
    public void showDialog(String msg){
        setChanged();
        notifyObservers(msg);
    }
}
