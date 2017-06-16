package cn.etsoft.smarthome.Adapter.GridView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.etsoft.smarthome.Domain.WareData;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.MyApplication;

/**
 * Author：FBL  Time： 2017/6/15.
 */

public class Home_ControlDev_Adapter extends BaseAdapter {

    private int mRoomPositin;
    private String mControlType;
    private List<WareDev> mDevList;

    public Home_ControlDev_Adapter(int roomPosition, String controlType) {
        mRoomPositin = roomPosition;
        mControlType = controlType;
        if ("情景".equals(controlType)) {

        } else {
            for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                WareDev dev = MyApplication.getWareData().getDevs().get(i);
                if ("灯光".equals(controlType) && dev.getType() == 3) {
                    mDevList.add(dev);
                }
                if ("家电".equals(controlType) && (dev.getType() == 0 || dev.getType() == 1 || dev.getType() == 2)) {
                    mDevList.add(dev);
                }
                if ("窗帘".equals(controlType) && dev.getType() == 4) {
                    mDevList.add(dev);
                }
                if ("门锁".equals(controlType) && dev.getType() == 3) {
                    mDevList.add(dev);
                }
                if ("监控".equals(controlType) && dev.getType() == 3) {
                    mDevList.add(dev);
                }
                if ("插座".equals(controlType) && dev.getType() == 3) {
                    mDevList.add(dev);
                }
            }
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
