package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(this);
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_dev);

        initView();
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
        int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
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
                        if (list_board.size() > board_position && list_board.get(board_position).getDevUnitID()
                                .equals(MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId()))
                            list_voard_cancpuid.add(MyApplication.getWareData().getAirConds().get(i).getPowChn());
                    }
                } else if (type_position == 3) {
                    for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                        if (list_board.size() > board_position && list_board.get(board_position).getDevUnitID()
                                .equals(MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId()))
                            list_voard_cancpuid.add((int) MyApplication.getWareData().getLights().get(i).getPowChn());
                    }
                } else if (type_position == 4) {
                    for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                        if (list_board.size() > board_position && list_board.get(board_position).getDevUnitID()
                                .equals(MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId()))
                            list_voard_cancpuid.add(MyApplication.getWareData().getCurtains().get(i).getPowChn());
                    }
                }

                List<String> list_way_ok = new ArrayList<>();
                if (type_position == 0) {
                    for (int i = 1; i < 17; i++) {
                        list_way_ok.add(i + "");
                    }
                } else {
                    for (int i = 1; i < 13; i++) {
                        list_way_ok.add(i + "");
                    }
                }

                for (int i = 0; i < list_voard_cancpuid.size(); i++) {
                    for (int j = 0; j < list_way_ok.size(); j++) {
                        if (Integer.parseInt(list_way_ok.get(j)) == list_voard_cancpuid.get(i)) {
                            list_way_ok.remove(j);
                        }
                    }
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(list_way_ok, 4);
                    popupWindow.showAsDropDown(v, 0, 0);
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

                if (!IsSave) {
                    ToastUtil.showToast(Add_Dev_Activity.this, "一个房间只能有一个窗帘");
                    return;
                }
                String name = add_dev_name.getText().toString();
                String type = add_dev_type.getText().toString();
                String room = "";
                if (add_dev_room.getVisibility() == View.VISIBLE)
                    room = add_dev_room.getText().toString();
                else
                    room = edit_dev_room.getText().toString();
                String board = add_dev_board.getText().toString();
                final String way = add_dev_way.getText().toString();


                if ("".equals(name) || "".equals(type) || "".equals(room) || "".equals(board) || "".equals(way))
                    Toast.makeText(Add_Dev_Activity.this, "信息输入不完整", Toast.LENGTH_SHORT).show();
                else {
                    int type_index = 0;
                    if (type_text.indexOf(type) == 1)
                        type_index = 3;
                    else if (type_text.indexOf(type) == 2)
                        type_index = 4;

                    int way_int = Integer.parseInt(way);
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
                            "\"powChn\":" + way_int + "}";

                    MyApplication.sendMsg(chn_str);
                    initDialog("正在添加...");
                    MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
                        @Override
                        public void upDataWareData(int what) {
                            if (what == 5)
                                if (mDialog != null)
                                    mDialog.dismiss();
                                finish();
                        }
                    });
                }
                break;
        }
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private int board_position = 0;
    private int type_position = 0;

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
        else if (type == 4)
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
                    if (position == 0)
                        type_position = 0;
                    else if (position == 1)
                        type_position = 3;
                    else if (position == 2) {
                        for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                            if (add_dev_room.getText().equals(MyApplication.getWareData().getCurtains().get(i).getDev().getRoomName())) {
                                ToastUtil.showToast(Add_Dev_Activity.this, "一个房间只能有一个窗帘");
                                IsSave = false;
                                popupWindow.dismiss();
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
                                ToastUtil.showToast(Add_Dev_Activity.this, "一个房间只能有一个窗帘");
                                IsSave = false;
                                popupWindow.dismiss();
                                return;
                            }
                        }

                    } else {
                        add_dev_room.setText(text.get(position));
                    }
                } else if (type == 3) {
                    add_dev_board.setText(text.get(position));
                    board_position = position;
                } else if (type == 4) {
                    add_dev_way.setText(text.get(position));
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
}


