package cn.etsoft.smarthome.fragment_equipment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.IClick;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.domain.AddDevControl_Result;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.ui.Add_Dev_Activity;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2016/8/25.
 * 高级设置-控制设置-按键配设备
 */
public class EditDevFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity mActivity;
    private ListView equi_control;
    private TextView add_equi;
    private DevAdapter devAdapter;
    private List<WareDev> dev_all;
    private Dialog mDialog;
    private PopupWindow popupWindow;
    private View view_parent;
    private LayoutInflater inflater;
    private int cmdValue = 0, modelValue = 0;
    private String str_Fixed;
    private PopupWindowAdapter_channel popupWindowAdapter_channel;
    // Hashtable.keySet()降序 TreeMap.keySet()升序 HashMap.keySet()乱序 LinkedHashMap.keySet()原序
    private TreeMap<Integer, Boolean> map = new TreeMap<>();// 存放已被选中的CheckBox
    private List<Integer> list_channel;
    private int data_save;
    private List<String> message_save;
    private List<String> message_get;

    public EditDevFragment(FragmentActivity activity) {
        mActivity = activity;
    }

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(mActivity);
        //允许返回
        mDialog.setCancelable(true);
        //显示
        mDialog.show();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
            }
        };
        //加载数据进度条，5秒数据没加载出来自动消失
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (mDialog.isShowing()) {
                        handler.sendMessage(handler.obtainMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e + "");
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_parent = inflater.inflate(R.layout.fragment_edit_dev, container, false);
        this.inflater = inflater;
        //初始化控件
        initView(view_parent);
        //加载数据
        event();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 5 || what == 6 || what == 7) {
                    if (mDialog != null)
                        mDialog.dismiss();
                }
                if (what == 7) {
                    if (MyApplication.getWareData().getDev_result() != null
                            && MyApplication.getWareData().getDev_result().getSubType2() == 1) {

                        if (MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevType() == 0) {
                            for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                                if (MyApplication.getWareData().getAirConds().size() <= i && MyApplication.getWareData().getAirConds().get(i).getDev().getDevId()
                                        == MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevID()
                                        && MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId()
                                        .equals(MyApplication.getWareData().getDev_result().getDev_rows().get(0).getCanCpuID())) {
                                    MyApplication.getWareData().getAirConds().remove(i);
                                }
                            }
                        }
                        if (MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevType() == 3) {
                            for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                                if (MyApplication.getWareData().getLights().size() <= i && MyApplication.getWareData().getLights().get(i).getDev().getDevId()
                                        == MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevID()
                                        && MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId()
                                        .equals(MyApplication.getWareData().getDev_result().getDev_rows().get(0).getCanCpuID())) {
                                    MyApplication.getWareData().getLights().remove(i);
                                }
                            }
                        }
                        if (MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevType() == 4) {
                            for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                                if (MyApplication.getWareData().getCurtains().size() <= i && MyApplication.getWareData().getCurtains().get(i).getDev().getDevId()
                                        == MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevID()
                                        && MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId()
                                        .equals(MyApplication.getWareData().getDev_result().getDev_rows().get(0).getCanCpuID())) {
                                    MyApplication.getWareData().getCurtains().remove(i);
                                }
                            }
                        }

                        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("profile",
                                Context.MODE_PRIVATE);
                        Gson gson = new Gson();
                        String jsondata = sharedPreferences.getString(GlobalVars.getDevid(), "");

                        List<WareDev> common_dev = new ArrayList<>();
                        if (!jsondata.equals("")) {
                            common_dev = gson.fromJson(jsondata, new TypeToken<List<WareDev>>() {
                            }.getType());
                        }

                        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                            if (MyApplication.getWareData().getDevs().get(i).getType() == MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevType()
                                    && MyApplication.getWareData().getDevs().get(i).getDevId() == MyApplication.getWareData().getDev_result().getDev_rows().get(0).getDevID()
                                    && MyApplication.getWareData().getDevs().get(i).getCanCpuId().equals(MyApplication.getWareData().getDev_result().getDev_rows().get(0).getCanCpuID())) {

                                for (int j = 0; j < common_dev.size(); j++) {
                                    if (common_dev.get(j).getDevId() == MyApplication.getWareData().getDevs().get(i).getDevId() && common_dev.get(j).getType() == MyApplication.getWareData().getDevs().get(i).getType() && common_dev.get(j).getCanCpuId().equals(MyApplication.getWareData().getDevs().get(i).getCanCpuId())) {
                                        common_dev.remove(j);
                                    }
                                }
                                MyApplication.getWareData().getDevs().remove(i);
                            }
                        }
                        if (devAdapter != null)
                            devAdapter.notifyDataSetChanged();
                        else {
                            devAdapter = new DevAdapter(mListener);
                            equi_control.setAdapter(devAdapter);
                        }
                        String savdata = gson.toJson(common_dev);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString(GlobalVars.getDevid(), savdata);
                        edit.commit();
                        Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                }
                if (what == 5) {
                    if (MyApplication.getWareData().getAddDev_result() != null
                            && MyApplication.getWareData().getAddDev_result().getSubType1() == 1 && MyApplication.getWareData().getAddDev_result().getSubType2() == 1) {
                        AddDevControl_Result result = MyApplication.getWareData().getAddDev_result();

                        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                            if (result.getDev_rows().get(0).getDevID() == MyApplication.getWareData().getDevs().get(i).getDevId() &&
                                    result.getDev_rows().get(0).getCanCpuID().equals(MyApplication.getWareData().getDevs().get(i).getCanCpuId()) &&
                                    result.getDev_rows().get(0).getDevType() == MyApplication.getWareData().getDevs().get(i).getType()) {
                                return;
                            }
                        }
                        WareDev dev1 = new WareDev();
                        if (result.getDev_rows().get(0).getDevType() == 0) {
                            WareAirCondDev dev = new WareAirCondDev();
                            dev.setPowChn(result.getDev_rows().get(0).getPowChn());
                            dev1.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getDevName())));
                            dev1.setType((byte) result.getDev_rows().get(0).getDevType());
                            dev1.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getRoomName())));
                            dev1.setCanCpuId(result.getDev_rows().get(0).getCanCpuID());
                            dev1.setDevId((byte) result.getDev_rows().get(0).getDevID());
                            dev.setDev(dev1);
                            dev.setbOnOff((byte) result.getDev_rows().get(0).getBOnOff());
                            MyApplication.getWareData().getDevs().add(dev1);
                            MyApplication.getWareData().getAirConds().add(dev);
                        } else if (result.getDev_rows().get(0).getDevType() == 3) {
                            WareLight light = new WareLight();
                            light.setPowChn((byte) result.getDev_rows().get(0).getPowChn());
                            dev1.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getDevName())));
                            dev1.setType((byte) result.getDev_rows().get(0).getDevType());
                            dev1.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getRoomName())));
                            dev1.setCanCpuId(result.getDev_rows().get(0).getCanCpuID());
                            dev1.setDevId((byte) result.getDev_rows().get(0).getDevID());
                            light.setDev(dev1);
                            light.setbOnOff((byte) result.getDev_rows().get(0).getBOnOff());
                            MyApplication.getWareData().getDevs().add(dev1);
                            MyApplication.getWareData().getLights().add(light);
                        } else if (result.getDev_rows().get(0).getDevType() == 4) {
                            WareCurtain curtain = new WareCurtain();
                            curtain.setPowChn((byte) result.getDev_rows().get(0).getPowChn());
                            dev1.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getDevName())));
                            dev1.setType((byte) result.getDev_rows().get(0).getDevType());
                            dev1.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getRoomName())));
                            dev1.setCanCpuId(result.getDev_rows().get(0).getCanCpuID());
                            dev1.setDevId((byte) result.getDev_rows().get(0).getDevID());
                            curtain.setDev(dev1);
                            curtain.setbOnOff((byte) result.getDev_rows().get(0).getBOnOff());
                            MyApplication.getWareData().getDevs().add(dev1);
                            MyApplication.getWareData().getCurtains().add(curtain);
                        }
                        devAdapter = new DevAdapter(mListener);
                        equi_control.setAdapter(devAdapter);
                        Toast.makeText(mActivity, "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                }
                if (what == 6) {
                    if (MyApplication.getWareData().getSaveDev_result() != null
                            && MyApplication.getWareData().getSaveDev_result().getSubType1() == 1 && MyApplication.getWareData().getSaveDev_result().getSubType2() == 1) {
                        Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                }
                MyApplication.getWareData().setDev_result(null);
            }
        });
        return view_parent;
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        equi_control = (ListView) view.findViewById(R.id.equi_cont_list);
        add_equi = (TextView) view.findViewById(R.id.add_equi);
        message_save = new ArrayList<>();
    }

    /**
     * 加载数据
     */
    private void event() {
        dev_all = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            dev_all.add(MyApplication.getWareData().getDevs().get(i));
        }
        devAdapter = new DevAdapter(mListener);
        equi_control.setAdapter(devAdapter);
        add_equi.setOnClickListener(this);

    }

    // 定义变量，记录刷新前获得焦点的EditText所在的位置
    int mCurrentTouchedIndex = -1;

    class DevAdapter extends BaseAdapter {

        DevAdapter(IClick listener) {
            dev_all = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                dev_all.add(MyApplication.getWareData().getDevs().get(i));
            }
            mListener = listener;
        }

        @Override
        public void notifyDataSetChanged() {
            dev_all = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                dev_all.add(MyApplication.getWareData().getDevs().get(i));
            }
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return dev_all.size();
        }

        @Override
        public Object getItem(int position) {
            return dev_all.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.equipment_listview_control_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (EditText) convertView.findViewById(R.id.name);
                viewHolder.devType = (TextView) convertView.findViewById(R.id.devType);
                viewHolder.devRoom = (TextView) convertView.findViewById(R.id.devRoom);
                viewHolder.devGallery = (TextView) convertView.findViewById(R.id.devGallery);
                viewHolder.text = (TextView) convertView.findViewById(R.id.text);
                viewHolder.save = (TextView) convertView.findViewById(R.id.save);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.name.setText(dev_all.get(position).getDevName());
            if (dev_all.get(position).getType() == 0) {
                viewHolder.devType.setText("空调");
                for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                    if (MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId() == dev_all.get(position).getCanCpuId()) {
                        String message1 = "";
                        int PowChn = MyApplication.getWareData().getAirConds().get(i).getPowChn();
                        String PowChnList = Integer.toBinaryString(PowChn);
                        List<Integer> index_list = new ArrayList<>();
                        message_get = new ArrayList<>();
                        for (int j = 0; j < PowChnList.length(); j++) {
                            if (PowChnList.charAt(PowChnList.length() - j - 1) == '1') {
                                index_list.add(PowChnList.length() - j - 1);
                                message1 += j + 1 + ".";
                                message_get.add(String.valueOf(j + 1));
                            }
                        }
                        if (message_get.size() > 5) {
                            message1 = "";
                            for (int k = 0; k < 5; k++) {
                                message1 += message_get.get(k) + ".";
                            }
                        }
                        if (!"".equals(message1)) {
                            message1 = message1.substring(0, message1.lastIndexOf("."));
                        }
                        viewHolder.devGallery.setText(message1);
                    }
                }
            } else if (dev_all.get(position).getType() == 1) {
                viewHolder.devType.setText("电视");
                viewHolder.devGallery.setText("无");
                viewHolder.devGallery.setClickable(false);
            } else if (dev_all.get(position).getType() == 2) {
                viewHolder.devType.setText("机顶盒");
                viewHolder.devGallery.setText("无");
                viewHolder.devGallery.setClickable(false);
            } else if (dev_all.get(position).getType() == 3) {
                viewHolder.devType.setText("灯光");
                for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                    if (MyApplication.getWareData().getLights().get(i).getDev().getDevId() == dev_all.get(position).getDevId()) {
                        int PowChn = MyApplication.getWareData().getLights().get(i).getPowChn();
                        viewHolder.devGallery.setText(PowChn + "");
                    }
                }
            } else if (dev_all.get(position).getType() == 4) {
                viewHolder.devType.setText("窗帘");
                for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                    if (MyApplication.getWareData().getCurtains().get(i).getDev().getDevId() == dev_all.get(position).getDevId()) {
                        String message1 = "";
                        int PowChn = MyApplication.getWareData().getCurtains().get(i).getPowChn();

                        String PowChnList = Integer.toBinaryString(PowChn);
                        List<Integer> index_list = new ArrayList<>();
                        message_get = new ArrayList<>();
                        for (int j = 0; j < PowChnList.length(); j++) {
                            if (PowChnList.charAt(PowChnList.length() - j - 1) == '1') {
                                index_list.add(PowChnList.length() - j - 1);
                                message1 += j + 1 + ".";
                                message_get.add(String.valueOf(j + 1));
                            }
                        }
                        if (message_get.size() > 3) {
                            message1 = "";
                            for (int k = 0; k < 3; k++) {
                                message1 += message_get.get(k) + ".";
                            }
                        }
                        if (!"".equals(message1)) {
                            message1 = message1.substring(0, message1.lastIndexOf("."));
                        }
                        viewHolder.devGallery.setText(message1);
                    }
                }
            }
            viewHolder.devRoom.setText(dev_all.get(position).getRoomName());

            viewHolder.name.setOnClickListener(mListener);
            viewHolder.name.setTag(position);
            viewHolder.devType.setOnClickListener(mListener);
            viewHolder.devType.setTag(position);
            viewHolder.devRoom.setOnClickListener(mListener);
            viewHolder.devRoom.setTag(position);
            viewHolder.devGallery.setOnClickListener(mListener);
            viewHolder.devGallery.setTag(position);
            viewHolder.text.setOnClickListener(mListener);
            viewHolder.text.setTag(position);
            viewHolder.save.setOnClickListener(mListener);
            viewHolder.save.setTag(position);
            viewHolder.delete.setOnClickListener(mListener);
            viewHolder.delete.setTag(position);
            // 设置触摸事件（）
            viewHolder.name.setOnTouchListener(new OnEditTextTouched(position));
            viewHolder.name.clearFocus();
            if (position == mCurrentTouchedIndex) {
                // 如果该项中的EditText是要获取焦点的
                viewHolder.name.requestFocus();
            }
            return convertView;
        }

        private class ViewHolder {
            private EditText name;
            public TextView devType, devRoom, devGallery, text, save, delete;
        }
    }


    //在ListView中，点击EditText获得焦点时，会重新调用getView，此时EditText会失去焦点
    // 解决listView中的editText点击失去焦点的问题
    // ListView中EditText的触摸事件
    private class OnEditTextTouched implements View.OnTouchListener {
        private int position;

        public OnEditTextTouched(int position) {
            this.position = position;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mCurrentTouchedIndex = position;
            }
            return false;
        }
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private IClick mListener = new IClick() {
        @Override
        public void listViewItemClick(final int position, View v) {
            //布局文件是：E:\AS-work\SmartHome\app\src\main\res\layout\equipment_listview_control_item2.xml
            //v:listView条目中的控件；v.getParent()：控件的父布局；v.getParent().getParent()：listView条目
            View window = (View) v.getParent().getParent();
            EditText name = (EditText) window.findViewById(R.id.name);
            TextView devRoom = (TextView) window.findViewById(R.id.devRoom);
            TextView devGallery = (TextView) window.findViewById(R.id.devGallery);
            switch (v.getId()) {
                case R.id.devRoom:
                    final List<String> home_text = new ArrayList<>();
                    List<WareDev> mWareDev_room = new ArrayList<>();

                    for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
                        mWareDev_room.add(MyApplication.getWareData().getDevs().get(i));
                    }
                    for (int i = 0; i < mWareDev_room.size() - 1; i++) {
                        for (int j = mWareDev_room.size() - 1; j > i; j--) {
                            if (mWareDev_room.get(i).getRoomName().equals(mWareDev_room.get(j).getRoomName())) {
                                mWareDev_room.remove(j);
                            }
                        }
                    }
                    for (int i = 0; i < mWareDev_room.size(); i++) {
                        home_text.add(mWareDev_room.get(i).getRoomName());
                    }

                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    } else {
                        initPopupWindow(v, home_text);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                    break;
                case R.id.devGallery:
                    List<Integer> list_voard_cancpuid = new ArrayList<>();
                    if (dev_all.get(position).getType() == 0) {
                        for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                            if (dev_all.get(position).getCanCpuId()
                                    .equals(MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() != MyApplication.getWareData().getAirConds().get(i).getDev().getDevId()) {
                                //TODO  3968
                                int PowChn = MyApplication.getWareData().getAirConds().get(i).getPowChn();
                                String PowChnList = Integer.toBinaryString(PowChn);
                                PowChnList = reverseString(PowChnList);
                                List<Integer> index_list = new ArrayList<>();
                                for (int j = 0; j < PowChnList.length(); j++) {
                                    if (PowChnList.charAt(j) == '1') {
                                        index_list.add(j + 1);
                                    }
                                }
                                list_voard_cancpuid.addAll(index_list);
                            }
                        }
                    } else if (dev_all.get(position).getType() == 3) {
                        for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                            if (dev_all.get(position).getCanCpuId()
                                    .equals(MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() != MyApplication.getWareData().getLights().get(i).getDev().getDevId()) {
                                //TODO  3968
                                int PowChn = MyApplication.getWareData().getLights().get(i).getPowChn();
                                list_voard_cancpuid.add(PowChn);
                            }
                        }
                    } else if (dev_all.get(position).getType() == 4) {
                        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                            if (dev_all.get(position).getCanCpuId()
                                    .equals(MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() != MyApplication.getWareData().getCurtains().get(i).getDev().getDevId()) {
                                //TODO  3968
                                int PowChn = MyApplication.getWareData().getCurtains().get(i).getPowChn();
                                String PowChnList = Integer.toBinaryString(PowChn);
                                PowChnList = reverseString(PowChnList);
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
                    list_channel = new ArrayList<>();
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
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    } else {
                        if (list_channel.size() == 0) {
                            ToastUtil.showToast(mActivity, "没有可用通道");
                        } else {
                            initPopupWindow_channel(v, list_channel);
                            popupWindow.showAsDropDown(v, 0, 0);
                        }
                    }
                    break;
                case R.id.text:
                    int type_dev = dev_all.get(position).getType();
                    int value = 0;
                    if (type_dev == 0) {
                        for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                            WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                            if (dev_all.get(position).getCanCpuId().equals(AirCondDev.getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() == AirCondDev.getDev().getDevId()) {
                                if (AirCondDev.getbOnOff() == 1) {
                                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                                    value = (modelValue << 5) | cmdValue;
                                } else {
                                    cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                                    value = (modelValue << 5) | cmdValue;
                                }
                                str_Fixed = getDevCmdstr(position);
                                str_Fixed = str_Fixed +
                                        ",\"cmd\":" + value + "}";
                                MyApplication.sendMsg(str_Fixed);
                            }
                        }
                    } else if (type_dev == 1) {
                        for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                            WareTv tv = MyApplication.getWareData().getTvs().get(j);
                            if (dev_all.get(position).getCanCpuId().equals(tv.getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() == tv.getDev().getDevId()) {
                                if (tv.getbOnOff() == 0) {
                                    value = UdpProPkt.E_TV_CMD.e_tv_offOn.getValue();
                                } else {
                                    value = UdpProPkt.E_TV_CMD.e_tv_numRt.getValue();
                                }
                                str_Fixed = getDevCmdstr(position);
                                str_Fixed = str_Fixed +
                                        ",\"cmd\":" + value + "}";
                                MyApplication.sendMsg(str_Fixed);
                            }
                        }
                    } else if (type_dev == 2) {
                        for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                            WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                            if (dev_all.get(position).getCanCpuId().equals(box.getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() == box.getDev().getDevId()) {
                                if (box.getbOnOff() == 0) {
                                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_offOn.getValue();
                                } else {
                                    value = UdpProPkt.E_TVUP_CMD.e_tvUP_numRt.getValue();
                                }
                                str_Fixed = getDevCmdstr(position);
                                str_Fixed = str_Fixed +
                                        ",\"cmd\":" + value + "}";
                                MyApplication.sendMsg(str_Fixed);
                            }
                        }
                    } else if (type_dev == 3) {
                        String ctlStr;
                        for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                            WareLight Light = MyApplication.getWareData().getLights().get(j);
                            if (dev_all.get(position).getCanCpuId().equals(Light.getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() == Light.getDev().getDevId()) {
                                if (Light.getbOnOff() == 0) {
                                    ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                            ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                                            ",\"subType1\":0" +
                                            ",\"subType2\":0" +
                                            ",\"canCpuID\":\"" + Light.getDev().getCanCpuId() +
                                            "\",\"devType\":" + Light.getDev().getType() +
                                            ",\"devID\":" + Light.getDev().getDevId() +
                                            ",\"cmd\":0" +
                                            "" +
                                            "}";
                                    MyApplication.sendMsg(ctlStr);
                                } else {
                                    ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                            ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                                            ",\"subType1\":0" +
                                            ",\"subType2\":0" +
                                            ",\"canCpuID\":\"" + Light.getDev().getCanCpuId() +
                                            "\",\"devType\":" + Light.getDev().getType() +
                                            ",\"devID\":" + Light.getDev().getDevId() +
                                            ",\"cmd\":1" +
                                            "}";
                                    MyApplication.sendMsg(ctlStr);
                                }
                            }
                        }
                    } else if (type_dev == 4) {
                        for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                            WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                            if (dev_all.get(position).getCanCpuId().equals(Curtain.getDev().getCanCpuId())
                                    && dev_all.get(position).getDevId() == Curtain.getDev().getDevId()) {
                                if (Curtain.getbOnOff() == 1) {
                                    int Value = UdpProPkt.E_CURT_CMD.e_curt_offOn.getValue();
                                    String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                            ",\"datType\":4" +
                                            ",\"subType1\":0" +
                                            ",\"subType2\":0" +
                                            ",\"canCpuID\":\"" + Curtain.getDev().getCanCpuId() + "\"" +
                                            ",\"devType\":" + Curtain.getDev().getType() +
                                            ",\"devID\":" + Curtain.getDev().getDevId();
                                    str_Fixed = str_Fixed +
                                            ",\"cmd\":" + Value + "}";
                                    MyApplication.sendMsg(str_Fixed);
                                } else {
                                    int Value = UdpProPkt.E_CURT_CMD.e_curt_offOff.getValue();
                                    String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                            ",\"datType\":4" +
                                            ",\"subType1\":0" +
                                            ",\"subType2\":0" +
                                            ",\"canCpuID\":\"" + Curtain.getDev().getCanCpuId() + "\"" +
                                            ",\"devType\":" + Curtain.getDev().getType() +
                                            ",\"devID\":" + Curtain.getDev().getDevId();
                                    str_Fixed = str_Fixed +
                                            ",\"cmd\":" + Value + "}";
                                    MyApplication.sendMsg(str_Fixed);
                                }
                            }
                        }
                    }
                    break;
                case R.id.save:
                    if (dev_all.get(position).getType() == 0) {
                        for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                            if (MyApplication.getWareData().getAirConds().get(i).getDev().getDevId() == dev_all.get(position).getDevId()) {
                                String message = "";
                                int PowChn = MyApplication.getWareData().getAirConds().get(i).getPowChn();
                                String PowChnList = Integer.toBinaryString(PowChn);
                                List<Integer> index_list = new ArrayList<>();
                                for (int j = 0; j < PowChnList.length(); j++) {
                                    if (PowChnList.charAt(PowChnList.length() - j - 1) == '1') {
                                        index_list.add(PowChnList.length() - j - 1);
                                        message += j + 1 + ".";
                                    }
                                }
                                if (!"".equals(message)) {
                                    message = message.substring(0, message.lastIndexOf("."));
                                }
                                if (message.equals(devGallery.getText().toString())) {
                                    data_save = PowChn;
                                } else {
                                    if (message_save.size() > 5) {
                                        ToastUtil.showToast(mActivity, "空调通道不能超过5个");
                                        return;
                                    }
                                }
                                MyApplication.getWareData().getAirConds().get(i).setPowChn(data_save);
                            }
                        }
                    } else if (dev_all.get(position).getType() == 3) {
                        for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                            if (MyApplication.getWareData().getLights().get(i).getDev().getDevId() == dev_all.get(position).getDevId()) {
                                int PowChn = MyApplication.getWareData().getLights().get(i).getPowChn();
                                if ((PowChn + "").equals(devGallery.getText().toString())) {
                                    data_save = PowChn;
                                } else {
                                    if (message_save.size() > 1) {
                                        ToastUtil.showToast(mActivity, "灯光通道不能超过1个");
                                        return;
                                    }
                                }
                                MyApplication.getWareData().getLights().get(i).setPowChn((byte) data_save);
                            }
                        }
                    } else if (dev_all.get(position).getType() == 4) {
                        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                            if (MyApplication.getWareData().getCurtains().get(i).getDev().getDevId() == dev_all.get(position).getDevId()) {
                                String message = "";
                                int PowChn = MyApplication.getWareData().getCurtains().get(i).getPowChn();
                                String PowChnList = Integer.toBinaryString(PowChn);
                                List<Integer> index_list = new ArrayList<>();
                                for (int j = 0; j < PowChnList.length(); j++) {
                                    if (PowChnList.charAt(PowChnList.length() - j - 1) == '1') {
                                        index_list.add(PowChnList.length() - j - 1);
                                        message += j + 1 + ".";
                                    }
                                }
                                if (!"".equals(message)) {
                                    message = message.substring(0, message.lastIndexOf("."));
                                }
                                if (message.equals(devGallery.getText().toString())) {
                                    data_save = PowChn;
                                } else {
                                    if (message_save.size() > 3) {
                                        ToastUtil.showToast(mActivity, "窗帘通道不能超过3个");
                                        return;
                                    }
                                }
                                MyApplication.getWareData().getCurtains().get(i).setPowChn(data_save);
                            }
                        }
                    }
                    dev_all.get(position).setDevName(name.getText().toString());
                    dev_all.get(position).setRoomName(devRoom.getText().toString());
////                发送：
//            {
//                "devUnitID": "37ffdb05424e323416702443",
//                    "datType": 6,
//                    "subType1": 0,
//                    "subType2": 0,
//                    "canCpuID": "31ffdf054257313827502543",
//                    "devType": 3,
//                    "devID": 6,
//                    "devName": "b5c636360000000000000000",
//                    "roomName": "ceb4b6a8d2e5000000000000",
//                    "powChn":	6，
//                "cmd": 1
//            }

                    final String chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                            "\"datType\":" + 6 + "," +
                            "\"subType1\":0," +
                            "\"subType2\":0," +
                            "\"canCpuID\":\"" + dev_all.get(position).getCanCpuId() + "\"," +
                            "\"devType\":" + dev_all.get(position).getType() + "," +
                            "\"devID\":" + +dev_all.get(position).getDevId() + "," +
                            "\"devName\":" + "\"" + Sutf2Sgbk(name.getText().toString()) + "\"," +
                            "\"roomName\":" + "\"" + Sutf2Sgbk(devRoom.getText().toString()) + "\"," +
                            "\"powChn\":" + data_save + "," +
                            "\"cmd\":" + 1 + "}";
                    MyApplication.sendMsg(chn_str);
                    initDialog("正在保存...");
                    break;
                case R.id.delete:
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(mActivity);
                    builder.setTitle("提示 :");
                    builder.setMessage("您确定要删除此设备吗？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            final String chn_str = "{" +
                                    "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                    "\"datType\":" + 7 + "," +
                                    "\"subType1\":0," +
                                    "\"subType2\":0," +
                                    "\"canCpuID\":\"" + dev_all.get(position).getCanCpuId() + "\"," +
                                    "\"devType\":" + dev_all.get(position).getType() + "," +
                                    "\"devID\":" + dev_all.get(position).getDevId() + "," +
                                    "\"cmd\":" + 1 + "}";

                            MyApplication.sendMsg(chn_str);
                            initDialog("正在删除...");
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    View customView;

    private void initPopupWindow(final View textView, final List<String> text) {
        //获取自定义布局文件pop.xml的视图
        customView = view_parent.inflate(mActivity, R.layout.popupwindow_equipment_listview, null);
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), textView.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, mActivity);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_p, int position, long id) {
                TextView tv = (TextView) textView;
                tv.setText(text.get(position));
                popupWindow.dismiss();
            }
        });
        //popupWindow页面之外可点
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
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow_channel(final View textView, List<Integer> list_channel) {
        //获取自定义布局文件pop.xml的视图
        customView = view_parent.inflate(mActivity, R.layout.popupwindow_equipment_listview2, null);
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
        popupWindow = new PopupWindow(customView, textView.getWidth(), 300);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        Button time_ok = (Button) customView.findViewById(R.id.time_ok);
        Button time_cancel = (Button) customView.findViewById(R.id.time_cancel);
        popupWindowAdapter_channel = new PopupWindowAdapter_channel(mActivity, list_channel);
        list_pop.setAdapter(popupWindowAdapter_channel);
        time_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] data = new int[12];
                String message = "";
                if (map.keySet().toArray().length == 0) {
                    ToastUtil.showToast(mActivity, "请选择设备通道");
                    return;
                }
                for (int i = 0; i < 12; i++) {
                    for (int k = 0; k < map.keySet().toArray().length; k++) {
                        int key = (Integer) map.keySet().toArray()[k];
                        if (i == (key - 1)) {
                            data[i] = 1;
                            break;
                        } else data[i] = 0;
                    }
                }
                String data_str = "";
                for (int i = 0; i < data.length; i++) {
                    data_str += data[i];
                }
                for (int i = 0; i < map.keySet().toArray().length; i++) {
                    message += String.valueOf(map.keySet().toArray()[i]) + ".";

                    message_save.add(String.valueOf(map.keySet().toArray()[i]));
                    Log.i("测试", String.valueOf(message_save));
                }
                if (!"".equals(message)) {
                    message = message.substring(0, message.lastIndexOf("."));
                }
                data_save = str2num(data_str);
                TextView tv = (TextView) textView;
                tv.setText(message);
                popupWindow.dismiss();
                map.clear();
            }
        });
        time_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {
                    map.clear();
                }
                popupWindow.dismiss();
            }
        });
        //popupWindow页面之外可点
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
     * 得到字符串中的数字和
     * @param str
     * @return
     */
    public int str2num(String str) {
        str = reverseString(str);
        return Integer.valueOf(str, 2);
    }

    /**
     * 倒置字符串
     * @param str
     * @return
     */
    public static String reverseString(String str) {
        char[] arr = str.toCharArray();
        int middle = arr.length >> 1;//EQ length/2
        int limit = arr.length - 1;
        for (int i = 0; i < middle; i++) {
            char tmp = arr[i];
            arr[i] = arr[limit - i];
            arr[limit - i] = tmp;
        }
        return new String(arr);
    }

    /**
     * 防区的适配器
     */
    private class ViewHolder {
        public TextView text;
        public CheckBox checkBox;
    }

    private class PopupWindowAdapter_channel extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<Integer> list_channel;


        public PopupWindowAdapter_channel(Context context, List<Integer> list_channel) {
            mContext = context;
            this.list_channel = list_channel;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (null != list_channel) {
                return list_channel.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return list_channel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.popupwindow_listview_item2, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.popupWindow_equipment_tv);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.popupWindow_equipment_cb);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(String.valueOf(list_channel.get(position)));
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked == true) {
                        map.put(list_channel.get(position), true);
                    } else {
                        map.remove(list_channel.get(position));
                    }
                }
            });
            if (map != null && map.containsKey(list_channel.get(position))) {
                viewHolder.checkBox.setChecked(true);
            } else {
                viewHolder.checkBox.setChecked(false);
            }
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_equi:
                startActivity(new Intent(mActivity, Add_Dev_Activity.class));
                break;
        }
    }

    /**
     * 空调控制  头命令字符串
     * @return
     */
    public String getDevCmdstr(int position) {
        String data = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"canCpuID\":\"" + dev_all.get(position).getCanCpuId() +
                "\",\"devType\":" + dev_all.get(position).getType() +
                ",\"devID\":" + dev_all.get(position).getDevId();
        return data;
    }

    public String Sutf2Sgbk(String string) {
        byte[] data = {0};
        try {
            data = string.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);
        return str_gb;
    }
}
