package cn.semtec.community2.adapter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.BaseActivity;


/**
 * 设备列表listview的适配器
 *
 * @author Jason
 * @see BaseAdapter
 */
public class DeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> mLeDevices;
    private List<Integer> rssiLists = new ArrayList<Integer>();

	//    private ArrayList<String> deviceNames;
    private LayoutInflater mInflator;

    public DeviceListAdapter(Context context) {
        mLeDevices = new ArrayList<BluetoothDevice>();
        mInflator = LayoutInflater.from(context);
    }

    public void addDevice(BluetoothDevice device,int rssi) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
            rssiLists.add(rssi);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }
    
    public List<Integer> getRssiLists() {
		return rssiLists;
	}

    public void clear() {
        mLeDevices.clear();
        rssiLists.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.device_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        BluetoothDevice device = mLeDevices.get(i);
        String deviceName = device.getName();
        String name = "";
        if(BaseActivity.instance!= null){
        	int index = BaseActivity.instance.deviceList.indexOf(deviceName);
        	name = BaseActivity.instance.deviceNames.get(index);
        }else{
        	name = deviceName;
        }
        if (deviceName != null && deviceName.length() > 0){
//        	viewHolder.deviceName.setText(deviceName);
        	viewHolder.deviceName.setText(name);
        }
        viewHolder.deviceAddress.setText(device.getAddress());

        return view;
    }

    private class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

}

