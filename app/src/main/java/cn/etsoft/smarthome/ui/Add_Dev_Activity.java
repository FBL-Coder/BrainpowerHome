package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareBoardChnout;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;

/**
 * Created by fbl on 16-11-17.
 */
public class Add_Dev_Activity extends Activity implements View.OnClickListener {

    private TextView title, add_dev_type, add_dev_room, add_dev_board, add_dev_save, add_dev_way;
    private EditText add_dev_name, edit_dev_room;
    private ImageView back, edit_roomname;
    private PopupWindow popupWindow;
    private List<String> Board_text;
    private List<WareBoardChnout> list_board;
    private List<String> home_text;
    private List<String> type_text;
    private boolean IsSave = true;
    private Dialog mDialog;
    private PopupWindowAdapter_channel popupWindowAdapter_channel;
    private TreeMap<Integer, Boolean> map = new TreeMap<>();// 存放已被选中的CheckBox
    private List<Integer> list_channel;
    private int data_save;
    private List<String> message_save;
    private List<WareDev> dev_all;
    private int board_position = 0;
    private int type_position = 0;

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(Add_Dev_Activity.this);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dev);
        initView();
//        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
//            @Override
//            public void upDataWareData(int what) {
//                if (what == 5) {
//                    if (mDialog != null)
//                        mDialog.dismiss();
//                    if (MyApplication.getWareData().getAddDev_result() != null
//                            && MyApplication.getWareData().getAddDev_result().getSubType1() == 1 && MyApplication.getWareData().getAddDev_result().getSubType2() == 1) {
//                        AddDevControl_Result result = MyApplication.getWareData().getAddDev_result();
//
//                        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
//                            if (result.getDev_rows().get(0).getDevID() == MyApplication.getWareData().getDevs().get(i).getDevId() &&
//                                    result.getDev_rows().get(0).getCanCpuID().equals(MyApplication.getWareData().getDevs().get(i).getCanCpuId()) &&
//                                    result.getDev_rows().get(0).getDevType() == MyApplication.getWareData().getDevs().get(i).getType()) {
//                                return;
//                            }
//                        }
//                        ToastUtil.showToast(Add_Dev_Activity.this, "添加成功");
//                        WareDev dev1 = new WareDev();
//                        if (result.getDev_rows().get(0).getDevType() == 0) {
//                            WareAirCondDev dev = new WareAirCondDev();
//                            dev.setPowChn(result.getDev_rows().get(0).getPowChn());
//                            dev1.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getDevName())));
//                            dev1.setType((byte) result.getDev_rows().get(0).getDevType());
//                            dev1.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getRoomName())));
//                            dev1.setCanCpuId(result.getDev_rows().get(0).getCanCpuID());
//                            dev1.setDevId((byte) result.getDev_rows().get(0).getDevID());
//                            dev.setDev(dev1);
//                            dev.setbOnOff((byte) result.getDev_rows().get(0).getBOnOff());
//                            MyApplication.getWareData().getDevs().add(dev1);
//                            MyApplication.getWareData().getAirConds().add(dev);
//                        } else if (result.getDev_rows().get(0).getDevType() == 3) {
//                            WareLight light = new WareLight();
//                            light.setPowChn((byte) result.getDev_rows().get(0).getPowChn());
//                            dev1.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getDevName())));
//                            dev1.setType((byte) result.getDev_rows().get(0).getDevType());
//                            dev1.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getRoomName())));
//                            dev1.setCanCpuId(result.getDev_rows().get(0).getCanCpuID());
//                            dev1.setDevId((byte) result.getDev_rows().get(0).getDevID());
//                            light.setDev(dev1);
//                            light.setbOnOff((byte) result.getDev_rows().get(0).getBOnOff());
//                            MyApplication.getWareData().getDevs().add(dev1);
//                            MyApplication.getWareData().getLights().add(light);
//                        } else if (result.getDev_rows().get(0).getDevType() == 4) {
//                            WareCurtain curtain = new WareCurtain();
//                            curtain.setPowChn((byte) result.getDev_rows().get(0).getPowChn());
//                            dev1.setDevName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getDevName())));
//                            dev1.setType((byte) result.getDev_rows().get(0).getDevType());
//                            dev1.setRoomName(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(result.getDev_rows().get(0).getRoomName())));
//                            dev1.setCanCpuId(result.getDev_rows().get(0).getCanCpuID());
//                            dev1.setDevId((byte) result.getDev_rows().get(0).getDevID());
//                            curtain.setDev(dev1);
//                            curtain.setbOnOff((byte) result.getDev_rows().get(0).getBOnOff());
//                            MyApplication.getWareData().getDevs().add(dev1);
//                            MyApplication.getWareData().getCurtains().add(curtain);
//                        }
//                        finish();
//                    } else {
//                        ToastUtil.showToast(Add_Dev_Activity.this, "添加失败");
//                        return;
//                    }
//                }
//            }
//        });
    }

    private void initView() {

        title = (TextView) findViewById(R.id.tv_home);
        add_dev_type = (TextView) findViewById(R.id.add_dev_type);
        add_dev_room = (TextView) findViewById(R.id.add_dev_room);
        add_dev_save = (TextView) findViewById(R.id.add_dev_save);
        add_dev_board = (TextView) findViewById(R.id.add_dev_board);
        add_dev_name = (EditText) findViewById(R.id.add_dev_name);
        edit_dev_room = (EditText) findViewById(R.id.edit_dev_room);
        add_dev_way = (TextView) findViewById(R.id.add_dev_way);
        back = (ImageView) findViewById(R.id.back);
        edit_roomname = (ImageView) findViewById(R.id.edit_roomname);

        edit_roomname.setOnClickListener(this);
        back.setOnClickListener(this);
        add_dev_type.setOnClickListener(this);
        add_dev_room.setOnClickListener(this);
        add_dev_board.setOnClickListener(this);
        add_dev_save.setOnClickListener(this);
        add_dev_way.setOnClickListener(this);


        Board_text = new ArrayList<>();
        list_board = MyApplication.getWareData().getBoardChnouts();
        for (int i = 0; i < list_board.size(); i++) {
            Board_text.add(list_board.get(i).getBoardName());
        }

        // 创建PopupWindow实例
        home_text = new ArrayList<>();
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

//        List<Integer> list_voard_cancpuid = new ArrayList<>();
//        if (type_position == 0) {
//            for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
//                list_voard_cancpuid.add(MyApplication.getWareData().getAirConds().get(i).getPowChn());
//            }
//        } else if (type_position == 3) {
//            for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
//                list_voard_cancpuid.add((int) MyApplication.getWareData().getLights().get(i).getPowChn());
//            }
//        } else if (type_position == 4) {
//            for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
//                list_voard_cancpuid.add(MyApplication.getWareData().getCurtains().get(i).getPowChn());
//            }
//        }

//        List<Integer> list_way_ok_light = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            list_way_ok_light.add(i);
//        }
//
//        for (int i = 0; i < list_voard_cancpuid.size(); i++) {
//            list_way_ok_light.remove(i);
//        }
        type_text = new ArrayList<>();
        type_text.add("空调");
        type_text.add("灯光");
        type_text.add("窗帘");
        title.setText("添加设备");
        if (type_text != null && type_text.size() != 0 &&
                Board_text != null && Board_text.size() != 0 &&
                home_text != null && home_text.size() != 0) {
            add_dev_type.setText(type_text.get(0));
            add_dev_board.setText(Board_text.get(0));
            add_dev_room.setText(home_text.get(0));
        } else {
            ToastUtil.showToast(Add_Dev_Activity.this, "没有数据");
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_roomname:
                if (add_dev_room.getVisibility() == View.VISIBLE) {
                    edit_dev_room.setVisibility(View.VISIBLE);
                    add_dev_room.setVisibility(View.GONE);
                } else {
                    edit_dev_room.setVisibility(View.GONE);
                    add_dev_room.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.add_dev_type:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(type_text, 1);
                    popupWindow.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.add_dev_room:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(home_text, 2);
                    popupWindow.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.add_dev_board:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(Board_text, 3);
                    popupWindow.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.add_dev_way:

                List<Integer> list_voard_cancpuid = new ArrayList<>();
                if (type_position == 0) {
                    for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                        if (list_board.size() > board_position && list_board.get(board_position).getDevUnitID().equals(MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId())) {
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
                } else if (type_position == 3) {
                    for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                        if (list_board.size() > board_position && list_board.get(board_position).getDevUnitID().equals(MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId())) {
                            //TODO  3968
                            int PowChn = MyApplication.getWareData().getLights().get(i).getPowChn();
                            list_voard_cancpuid.add(PowChn);
                        }
                    }
                } else if (type_position == 4) {
                    for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                        if (list_board.size() > board_position && list_board.get(board_position).getDevUnitID().equals(MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId())) {
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
                        ToastUtil.showToast(this, "没有可用通道");
                    } else {
                        initPopupWindow_channel(v, list_channel);
                        popupWindow.showAsDropDown(v, 0, 0);
                    }
                }
                break;
            case R.id.add_dev_save:
//            {
//                "devUnitID": "37ffdb05424e323416702443",
//                    "datType": 5,
//                    "subType1": 0,
//                    "subType2": 0,
//                    "canCpuID": "31ffdf054257313827502543",
//                    "devType": 3,
//                    "devName": "b5c636360000000000000000",
//                    "roomName": "ceb4b6a8d2e5000000000000",
//                    "powChn":	6
//            }
                String name = add_dev_name.getText().toString();
                String type = add_dev_type.getText().toString();
                String room = "";
                if (add_dev_room.getVisibility() == View.VISIBLE)
                    room = add_dev_room.getText().toString();
                else
                    room = edit_dev_room.getText().toString();
                String board = add_dev_board.getText().toString();
//                final String way = add_dev_way.getText().toString();

                if ("空调".equals(type)) {
                    if (message_save.size() > 5) {
                        ToastUtil.showToast(Add_Dev_Activity.this, "空调通道不能超过5个");
                        return;
                    }
                } else if ("灯光".equals(type)) {
                    if (message_save.size() > 1) {
                        ToastUtil.showToast(Add_Dev_Activity.this, "灯光通道不能超过1个");
                        return;
                    } else {
                        int PowChn = data_save;
                        String PowChnList = Integer.toBinaryString(PowChn);
                        PowChnList = reverseString(PowChnList);
                        List<Integer> index_list = new ArrayList<>();
                        for (int j = 0; j < PowChnList.length(); j++) {
                            if (PowChnList.charAt(j) == '1') {
                                index_list.add(j);
                            }
                        }
                        data_save = index_list.get(0);
                    }
                } else if ("窗帘".equals(type)) {
                    if (message_save.size() > 3) {
                        ToastUtil.showToast(Add_Dev_Activity.this, "窗帘通道不能超过3个");
                        return;
                    }
                }

                if ("".equals(name) || "".equals(type) || "".equals(room) || "".equals(board) || "".equals(data_save))
                    Toast.makeText(Add_Dev_Activity.this, "信息输入不完整", Toast.LENGTH_SHORT).show();
                else {
                    int type_index = 0;
                    if (type_text.indexOf(type) == 1)
                        type_index = 3;
                    else if (type_text.indexOf(type) == 2)
                        type_index = 4;
//                    int way_int = Integer.parseInt(way);
                    //----待和服务器交互；
                    final String chn_str = "{" +
                            "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                            "\"datType\":" + 5 + "," +
                            "\"subType1\":0," +
                            "\"subType2\":0," +
                            "\"canCpuID\":\"" + MyApplication.getWareData().getBoardChnouts().get(Board_text.indexOf(board)).getDevUnitID() + "\"," +
                            "\"devType\":" + type_index + "," +
                            "\"devName\":" + "\"" + Sutf2Sgbk(name) + "\"," +
                            "\"roomName\":" + "\"" + Sutf2Sgbk(room) + "\"," +
                            "\"powChn\":" + data_save + "}";

                    MyApplication.sendMsg(chn_str);
                    initDialog("正在添加...");
                    finish();
                }
                break;
        }
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
     *
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
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final List<String> text, final int type) {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);

        // 创建PopupWindow实例
        if (type == 1)
            popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 320, 160);
        else if (type == 2)
            popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 320, 300);
        else if (type == 3)
            popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 320, 120);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1) {
                    add_dev_type.setText(text.get(position));
                    if (position == 0) {
                        type_position = 0;
                    } else if (position == 1) {
                        type_position = 3;
                    } else if (position == 2) {
                        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                            if (add_dev_room.getText().equals(MyApplication.getWareData().getCurtains().get(i).getDev().getRoomName())) {
                                ToastUtil.showToast(Add_Dev_Activity.this, "此房间已有窗帘，换个房间");
                                IsSave = false;
                                popupWindow.dismiss();
                                type_position = 4;
                                return;
                            }
                        }
                        type_position = 4;
                    }
                } else if (type == 2) {
                    if ("窗帘".equals(add_dev_type.getText())) {
                        String roomName = text.get(position);
                        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                            if (roomName.equals(MyApplication.getWareData().getCurtains().get(i).getDev().getRoomName())) {
                                ToastUtil.showToast(Add_Dev_Activity.this, "此房间已有窗帘，换个房间");
                                IsSave = false;
                                popupWindow.dismiss();
                                return;
                            }
                        }
                        if (!IsSave) {
                            popupWindow.dismiss();
                            add_dev_room.setText(roomName);
                        }
                    } else {
                        add_dev_room.setText(text.get(position));
                    }
                } else if (type == 3) {
                    add_dev_board.setText(text.get(position));
                    board_position = position;
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
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow_channel(final View textView, List<Integer> list_channel) {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview2, null);
        customView.setBackgroundResource(R.drawable.selectbg);
        popupWindow = new PopupWindow(customView, textView.getWidth(), 300);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        Button time_ok = (Button) customView.findViewById(R.id.time_ok);
        Button time_cancel = (Button) customView.findViewById(R.id.time_cancel);
        popupWindowAdapter_channel = new PopupWindowAdapter_channel(this, list_channel);
        list_pop.setAdapter(popupWindowAdapter_channel);
        time_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] data = new int[12];
                String message = "";
                if (map.keySet().toArray().length == 0) {
                    ToastUtil.showToast(Add_Dev_Activity.this, "请选择设备通道");
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
                message_save = new ArrayList<>();
                for (int i = 0; i < map.keySet().toArray().length; i++) {
                    message += String.valueOf(map.keySet().toArray()[i]) + ".";
                    message_save.add(String.valueOf(map.keySet().toArray()[i]));
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

    public String Sutf2Sgbk(String string) {

        byte[] data = {0};
//        byte[] dataname = new byte[12];
        try {
            data = string.getBytes("GB2312");
//            System.arraycopy(dataname,0,data,0,data.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);
        return str_gb;
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null)
            mDialog.dismiss();
        super.onDestroy();
    }
}


