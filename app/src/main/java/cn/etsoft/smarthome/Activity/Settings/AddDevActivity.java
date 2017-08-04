package cn.etsoft.smarthome.Activity.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareBoardChnout;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.View.PopupWindow.MultiChoicePopWindow;

import static cn.etsoft.smarthome.UiHelper.TimerSetHelper.reverseString;

/**
 * Author：FBL  Time： 2017/7/24.
 */

public class AddDevActivity extends BaseActivity implements View.OnClickListener {

    //969426423464226
    private static final String TAG = "AddDevActivity";
    private int BOARD = 0, ROOM = 1, TYPE = 2;
    private EditText mAddDevName, mAddDevEditRoom;
    private TextView mAddDevOutBoard, mAddDevRoom, mAddDevType, mAddDevWay, mAddDevSave, mAddDevCancle;
    private ImageView mAddDevShowInputRoom;
    private PopupWindow popupWindow;
    private MultiChoicePopWindow mMultiChoicePopWindow;
    private List<WareBoardChnout> list_board;
    private List<String> OutBoardNames;
    private List<String> DevTypes;
    private List<Integer> DevWays;
    private int board_position = -1;
    private int type_position = -1;
    private int room_position = -1;
    private String Save_DevName;
    private String Save_Roomname;

    private AlertDialog.Builder builder;

    @Override
    public void initView() {
        setLayout(R.layout.activity_adddevs);
        mAddDevOutBoard = getViewById(R.id.AddDev_OutBoard);
        mAddDevRoom = getViewById(R.id.AddDev_Room);
        mAddDevType = getViewById(R.id.AddDev_Type);
        mAddDevWay = getViewById(R.id.AddDev_Way);
        mAddDevSave = getViewById(R.id.AddDev_Save);
        mAddDevCancle = getViewById(R.id.AddDev_cancle);
        mAddDevName = getViewById(R.id.AddDev_Name);
        mAddDevEditRoom = getViewById(R.id.AddDev_EditRoom);
        mAddDevShowInputRoom = getViewById(R.id.AddDev_ShowInputRoom);

        mAddDevOutBoard.setOnClickListener(this);
        mAddDevRoom.setOnClickListener(this);
        mAddDevType.setOnClickListener(this);
        mAddDevWay.setOnClickListener(this);
        mAddDevSave.setOnClickListener(this);
        mAddDevCancle.setOnClickListener(this);
        mAddDevShowInputRoom.setOnClickListener(this);
    }

    @Override
    public void initData() {
        OutBoardNames = new ArrayList<>();
        DevTypes = new ArrayList<>();
        DevTypes.add("空调");
        DevTypes.add("灯光");
        DevTypes.add("窗帘");
        DevWays = new ArrayList<>();
        list_board = MyApplication.getWareData().getBoardChnouts();
        for (int i = 0; i < list_board.size(); i++) {
            OutBoardNames.add(list_board.get(i).getBoardName());
        }
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 5 && subtype1 == 1) {
                    MyApplication.mApplication.dismissLoadDialog();
                    if (subtype2 != 1) {
                        ToastUtil.showText("添加失败");
                        return;
                    }
                    ToastUtil.showText("添加成功");
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddDev_cancle:
                finish();
                break;
            case R.id.AddDev_Save:

                builder = new AlertDialog.Builder(this);
                builder.setMessage("是否保存设置？");
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //  保存修改
                        int Save_DevWay = 0;
                        //设备名数据处理
                        Save_DevName = mAddDevName.getText().toString();
                        if ("".equals(Save_DevName) || Save_DevName.length() > 6) {
                            ToastUtil.showText("设备名称过长");
                            return;
                        }
                        try {
                            Save_DevName = CommonUtils.bytesToHexString(Save_DevName.getBytes("GB2312"));
                        } catch (UnsupportedEncodingException e) {
                            ToastUtil.showText("设备名称不合适");
                            return;
                        }
                        //房间名数据处理
                        if (mAddDevRoom.getVisibility() == View.VISIBLE)
                            Save_Roomname = mAddDevRoom.getText().toString();
                        else {
                            Save_Roomname = mAddDevEditRoom.getText().toString();
                            if (Save_Roomname.length() > 6) {
                                ToastUtil.showText("房间名过长");
                                return;
                            }
                        }
                        if ("".equals(Save_Roomname)) {
                            ToastUtil.showText("房间名为空");
                            return;
                        }
                        try {
                            Save_Roomname = CommonUtils.bytesToHexString(Save_Roomname.getBytes("GB2312"));
                        } catch (UnsupportedEncodingException e) {
                            ToastUtil.showText("房间名称不合适");
                            return;
                        }
                        if (type_position == 0) {
                            for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                                //设备通道 保存数据处理
                                String Way_Str = mAddDevWay.getText().toString();
                                String[] WayStr_ok = Way_Str.split("、");
                                if (WayStr_ok.length == 0) {
                                    ToastUtil.showText("请选择通道");
                                    return;
                                } else {
                                    if (WayStr_ok.length > 5) {//135
                                        ToastUtil.showText("空调最多5个通道");
                                        return;
                                    }
                                    String Way = "";
                                    for (int j = 0; j < 12; j++) {
                                        boolean IsEnter = false;
                                        for (int k = 0; k < WayStr_ok.length; k++) {
                                            if (j == Integer.parseInt(WayStr_ok[k]) - 1) {
                                                Way += "1";
                                                IsEnter = true;
                                            }
                                        }
                                        if (!IsEnter) {
                                            Way += "0";
                                        }
                                    }
                                    Save_DevWay = Integer.parseInt(new StringBuffer(Way).reverse().toString(), 2);
                                }
                            }
                        } else if (type_position == 1) {
                            //设备通道 保存数据处理
                            String Way_Str = mAddDevWay.getText().toString();
                            Save_DevWay = Integer.parseInt(Way_Str) - 1;
                        } else if (type_position == 2) {
                            for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                                //设备通道 保存数据处理
                                String Way_Str = mAddDevWay.getText().toString();
                                String[] WayStr_ok = Way_Str.split("、");
                                if (WayStr_ok.length == 0) {
                                    ToastUtil.showText("请选择通道");
                                    return;
                                } else {
                                    if (WayStr_ok.length > 3) {//135
                                        ToastUtil.showText("窗帘最多3个通道");
                                        return;
                                    }
                                    String Way = "";
                                    for (int j = 0; j < 12; j++) {
                                        boolean IsEnter = false;
                                        for (int k = 0; k < WayStr_ok.length; k++) {
                                            if (j == Integer.parseInt(WayStr_ok[k]) - 1) {
                                                Way += "1";
                                                IsEnter = true;
                                            }
                                        }
                                        if (!IsEnter) {
                                            Way += "0";
                                        }
                                    }
                                    Save_DevWay = Integer.parseInt(new StringBuffer(Way).reverse().toString(), 2);
                                }
                            }
                        }
                        String chn_str = "";
                        chn_str = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                "\"datType\":" + 5 + "," +
                                "\"subType1\":0," +
                                "\"subType2\":0," +
                                "\"canCpuID\":\"" + list_board.get(board_position).getDevUnitID() + "\"," +
                                "\"devType\":" + type_position + "," +
                                "\"devName\":" + "\"" + Save_DevName + "\"," +
                                "\"roomName\":" + "\"" + Save_Roomname + "\"," +
                                "\"powChn\":" + Save_DevWay + "}";
//                        Log.i(TAG, "onClick: " + chn_str);
                        MyApplication.setAddOrEditDevName(Save_DevName);
                        MyApplication.setAddOrEditRoomName(Save_Roomname);
                        MyApplication.mApplication.getUdpServer().send(chn_str);
                        MyApplication.mApplication.showLoadDialog(AddDevActivity.this);
                    }
                });
                builder.setNegativeButton("不要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.AddDev_OutBoard:
                initRadioPopupWindow(v, OutBoardNames, BOARD);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.AddDev_Room:
                initRadioPopupWindow(v, MyApplication.getWareData().getRooms(), ROOM);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.AddDev_ShowInputRoom:
                if (mAddDevRoom.getVisibility() == View.VISIBLE) {
                    mAddDevRoom.setVisibility(View.GONE);
                    mAddDevEditRoom.setVisibility(View.VISIBLE);
                } else {
                    mAddDevRoom.setVisibility(View.VISIBLE);
                    mAddDevEditRoom.setVisibility(View.GONE);
                }
                break;
            case R.id.AddDev_EditRoom:
                break;
            case R.id.AddDev_Type:
                initRadioPopupWindow(v, DevTypes, TYPE);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.AddDev_Way:
                if (type_position == -1) {
                    ToastUtil.showText("请选择设备类型");
                    return;
                }
                if (board_position == -1) {
                    ToastUtil.showText("请选择输出板");
                    return;
                }
                List<Integer> list_voard_cancpuid = new ArrayList<>();
                if (type_position == 0) {
                    for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                        WareAirCondDev airCondDev = MyApplication.getWareData().getAirConds().get(i);

                        if (list_board.get(board_position).getDevUnitID().equals(airCondDev.getDev().getCanCpuId())) {
                            int PowChn = airCondDev.getPowChn();
                            String PowChnList = Integer.toBinaryString(PowChn);
                            PowChnList = new StringBuffer(PowChnList).reverse().toString();
                            List<Integer> index_list = new ArrayList<>();
                            for (int j = 0; j < PowChnList.length(); j++) {
                                if (PowChnList.charAt(j) == '1') {
                                    index_list.add(j + 1);
                                }
                            }
                            list_voard_cancpuid.addAll(index_list);
                        }
                    }
                } else if (type_position == 1) {
                    for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {

                        if (list_board.get(board_position).getDevUnitID().equals(
                                MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId())) {
                            int PowChn = MyApplication.getWareData().getLights().get(i).getPowChn() + 1;
                            list_voard_cancpuid.add(PowChn);
                        }
                    }
                } else if (type_position == 2) {
                    for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                        if (list_board.get(board_position).getDevUnitID().equals(
                                MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId())) {
                            int PowChn = MyApplication.getWareData().getCurtains().get(i).getPowChn();
                            String PowChnList = Integer.toBinaryString(PowChn);
                            PowChnList = new StringBuffer(PowChnList).reverse().toString();
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
                List<Integer> list_channel = new ArrayList<>();
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
                boolean[] isSelect = new boolean[list_channel.size()];
                if (list_channel.size() == 0) {
                    ToastUtil.showText("没有可用通道");
                } else {
                    initWayDialog(this, (TextView) v, list_channel, isSelect);
                }
                break;
        }

    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text, final int Flag) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(AddDevActivity.this, R.layout.listview_popupwindow_equipment, null);
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
        popupWindow = new PopupWindow(customView, view_parent.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, AddDevActivity.this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (Flag == BOARD)
                    board_position = position;
                else if (Flag == ROOM)
                    room_position = position;
                else if (Flag == TYPE)
                    type_position = position;
                popupWindow.dismiss();
            }
        });
        //popupwindow页面之外可点
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
     * 选择通道
     */
    public void initWayDialog(Context context, final TextView view, List<Integer> Way, boolean[] isSelect) {
        final List<String> Ways_str = new ArrayList<>();
        for (int i = 0; i < Way.size(); i++) {
            Ways_str.add(Way.get(i) + "");
        }
        if (mMultiChoicePopWindow == null)
            mMultiChoicePopWindow = new MultiChoicePopWindow(context, view, Ways_str, isSelect);
        else {
            mMultiChoicePopWindow.upDataFlag(isSelect);
            mMultiChoicePopWindow.upParentView(view);
            mMultiChoicePopWindow.upList(Ways_str);
            mMultiChoicePopWindow.UpDataView();
        }
        mMultiChoicePopWindow.setTitle("选择通道");
        mMultiChoicePopWindow.setOnOKButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
                int size = selItems.length;
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    if (selItems[i]) {
                        stringBuffer.append(Ways_str.get(i) + "、");
                    }
                }
                if (stringBuffer.toString().length() == 0)
                    view.setText("点击选择通道");
                String stringBuffer_str = stringBuffer.toString();
                stringBuffer_str = stringBuffer_str.substring(0, stringBuffer_str.lastIndexOf("、"));
                view.setText(stringBuffer_str.toString());
            }
        });
        mMultiChoicePopWindow.show();
    }
}
