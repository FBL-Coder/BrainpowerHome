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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.view.Circle_Progress;

/**
 * Created by fbl on 16-11-17.
 */
public class Devs_Detail_Activity extends Activity implements View.OnClickListener {

    private TextView dev_type, dev_room, dev_save, dev_back, title, dev_way;
    private EditText dev_name;
    private ImageView back;
    private WareDev dev;
    private int id;
    private PopupWindow popupWindow;
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
        setContentView(R.layout.dec_detail_activity);
        initView();
    }

    /**
     * 初始化组件以及数据
     */
    private void initView() {
        id = getIntent().getIntExtra("id", 0);
        dev = MyApplication.getWareData().getDevs().get(id);

        title = (TextView) findViewById(R.id.tv_home);
        dev_type = (TextView) findViewById(R.id.dev_type);
        dev_room = (TextView) findViewById(R.id.dev_room);
        dev_save = (TextView) findViewById(R.id.dev_save);
        dev_back = (TextView) findViewById(R.id.dev_back);
        dev_name = (EditText) findViewById(R.id.dev_name);
        dev_way = (TextView) findViewById(R.id.dev_way);
        back = (ImageView) findViewById(R.id.back);


        title.setText(dev.getDevName());
        dev_name.setText(dev.getDevName());
        dev_room.setText(dev.getRoomName());

        if (dev.getType() == 0) {
            dev_type.setText("空调");
            for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                if (MyApplication.getWareData().getAirConds().get(i).getDev().getDevId() == dev.getDevId()) {
                    dev_way.setText(MyApplication.getWareData().getAirConds().get(i).getPowChn() + "");
                }
            }
        } else if (dev.getType() == 1) {
            dev_type.setText("电视");
            dev_way.setText("无");
            dev_way.setClickable(false);
        } else if (dev.getType() == 2) {
            dev_type.setText("机顶盒");
            dev_way.setText("无");
            dev_way.setClickable(false);
        } else if (dev.getType() == 3) {
            dev_type.setText("灯光");
            for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                if (MyApplication.getWareData().getLights().get(i).getDev().getDevId() == dev.getDevId()) {
                    dev_way.setText(MyApplication.getWareData().getLights().get(i).getPowChn() + "");
                }
            }
        } else if (dev.getType() == 4) {
            dev_type.setText("窗帘");
            for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                if (MyApplication.getWareData().getCurtains().get(i).getDev().getDevId() == dev.getDevId()) {
                    dev_way.setText(MyApplication.getWareData().getCurtains().get(i).getPowChn() + "");
                }
            }
        }

        dev_way.setOnClickListener(this);
        dev_save.setOnClickListener(this);
        dev_back.setOnClickListener(this);
        back.setOnClickListener(this);
        dev_room.setOnClickListener(this);
    }


    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final int type, final List<String> text) {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);

        // 创建PopupWindow实例

        popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 320, 120);

        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (type == 0) {
                    dev_room.setText(text.get(position));
                } else if (type == 1) {
                    dev_way.setText(text.get(position));
                }
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

    @Override
    public void onClick(View v) {
        int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
        switch (v.getId()) {

            case R.id.back:
            case R.id.dev_back:
                finish();
                break;

            case R.id.dev_save:

                if (dev.getType() == 0) {
                    for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                        if (MyApplication.getWareData().getAirConds().get(i).getDev().getDevId() == dev.getDevId()) {
                            MyApplication.getWareData().getAirConds().get(i).setPowChn(Integer.parseInt(dev_way.getText().toString()));
                        }
                    }
                } else if (dev.getType() == 3) {
                    dev_type.setText("灯光");
                    for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                        if (MyApplication.getWareData().getLights().get(i).getDev().getDevId() == dev.getDevId()) {
                            MyApplication.getWareData().getLights().get(i).setPowChn((byte) Integer.parseInt(dev_way.getText().toString()));
                        }
                    }
                } else if (dev.getType() == 4) {
                    dev_type.setText("窗帘");
                    for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                        if (MyApplication.getWareData().getCurtains().get(i).getDev().getDevId() == dev.getDevId()) {
                            MyApplication.getWareData().getCurtains().get(i).setPowChn(Integer.parseInt(dev_way.getText().toString()));
                        }
                    }
                }
                dev.setDevName(dev_name.getText().toString());
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
                        "\"canCpuID\":\"" + dev.getCanCpuId() + "\"," +
                        "\"devType\":" + dev.getType() + "," +
                        "\"devID\":" + +dev.getDevId() + "," +
                        "\"devName\":" + "\"" + Sutf2Sgbk(dev_name.getText().toString()) + "\"," +
                        "\"roomName\":" + "\"" + Sutf2Sgbk(dev_room.getText().toString()) + "\"," +
                        "\"powChn\":" + dev_way.getText().toString() + "," +
                        "\"cmd\":" + 1 + "}";

                MyApplication.sendMsg(chn_str);
                initDialog("正在保存...");
                MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
                    @Override
                    public void upDataWareData(int what) {
                        if (what == 6) {
                            if (mDialog != null)
                                mDialog.dismiss();
                            finish();
                        }
                    }
                });


                break;
            case R.id.dev_room:
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
                    initPopupWindow(0, home_text);
                    popupWindow.showAsDropDown(v, -widthOff, 0);
                }
                break;
            case R.id.dev_way:


                List<Integer> list_voard_cancpuid = new ArrayList<>();
                if (dev.getType() == 0) {
                    for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
                        if (dev.getCanCpuId()
                                .equals(MyApplication.getWareData().getAirConds().get(i).getDev().getCanCpuId()))
                            list_voard_cancpuid.add(MyApplication.getWareData().getAirConds().get(i).getPowChn());
                    }
                } else if (dev.getType() == 3) {
                    for (int i = 0; i < MyApplication.getWareData().getLights().size(); i++) {
                        if (dev.getCanCpuId()
                                .equals(MyApplication.getWareData().getLights().get(i).getDev().getCanCpuId()))
                            list_voard_cancpuid.add((int) MyApplication.getWareData().getLights().get(i).getPowChn());
                    }
                } else if (dev.getType() == 4) {
                    for (int i = 0; i < MyApplication.getWareData().getCurtains().size(); i++) {
                        if (dev.getCanCpuId()
                                .equals(MyApplication.getWareData().getCurtains().get(i).getDev().getCanCpuId()))
                            list_voard_cancpuid.add(MyApplication.getWareData().getCurtains().get(i).getPowChn());
                    }
                }

                List<String> list_coard_ok = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    list_coard_ok.add(i + "");
                }

                for (int i = 0; i < list_voard_cancpuid.size(); i++) {
                    for (int j = 0; j < list_coard_ok.size(); j++) {
                        if (Integer.parseInt(list_coard_ok.get(j)) == list_voard_cancpuid.get(i)) {
                            list_coard_ok.remove(j);
                        }
                    }
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(1, list_coard_ok);
                    popupWindow.showAsDropDown(v, -widthOff, 0);
                }
                break;
        }
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
