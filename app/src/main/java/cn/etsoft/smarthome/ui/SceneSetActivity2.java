package cn.etsoft.smarthome.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.adapter.RecyclerViewAdapter;
import cn.etsoft.smarthome.fragment_scene2.SceneFragment_dev;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2017/3/14.
 * 高级设置-情景设置的Activity页面
 */
public class SceneSetActivity2 extends FragmentActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private List<WareSceneEvent> event;
    private RecyclerViewAdapter recyclerAdapter;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private int room_position = 0;
    private ImageView iv_cancel;
    private EditText name;
    private Button sure, cancel, saveScene;
    private byte sceneid = 0;
    private Dialog mDialog;
    private Handler handler;
    private TextView input_room, devType_input;
    private ImageView input_choose;
    private List<String> home_text;
    private List<String> devType;
    private int dev_position = 0;
    private Fragment sceneFragment_dev;
    private boolean ISCHOOSE = false;
    private PopupWindow popupWindow;


    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(this);
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
            }
        };
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
        setContentView(R.layout.activity_sceneset2);
        //进度条
        initDialog("初始化数据中...");
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
                //初始化控件
                initView();
                //初始化情景的ListView
                initHorizontalListView();
                //初始化房间
                initRoom();
                //初始化设备类型
                initDev();
                //初始化Fragment
                initFragment();
            }
        };

        ReadWrite();

        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 23) {
                    ToastUtil.showToast(SceneSetActivity2.this, "添加成功");
                    ReadWrite();
                }
                if (what == 24) {
                    ToastUtil.showToast(SceneSetActivity2.this, "保存成功");
                }
                if (what == 25) {
                    initHorizontalListView();
                    ToastUtil.showToast(SceneSetActivity2.this, "删除成功");
                }
            }
        });
    }

    private void ReadWrite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dtat_Cache.writeFile(GlobalVars.getDevid(), MyApplication.getWareData());
                MyApplication.setWareData_Scene((WareData) Dtat_Cache.readFile(GlobalVars.getDevid()));
                handler.sendMessage(handler.obtainMessage());
            }
        }).start();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        saveScene = (Button) findViewById(R.id.sceneSet_save);
        saveScene.setOnClickListener(this);
        input_choose = (ImageView) findViewById(R.id.input_choose);
        input_choose.setOnClickListener(this);
        input_room = (TextView) findViewById(R.id.input_room);
        devType_input = (TextView) findViewById(R.id.devType_input);
        event = new ArrayList<>();
    }

    /**
     * 初始化房间
     */
    private void initRoom() {
        home_text = new ArrayList<>();
        //给房间集合里加上“全部”
        home_text.add("全部");
        for (int i = 0; i < MyApplication.getRoom_list().size(); i++) {
            home_text.add(MyApplication.getRoom_list().get(i));
        }
        input_room.setText(home_text.get(room_position));
        input_room.setOnClickListener(this);
    }

    /**
     * 初始化设备类型
     */
    private void initDev() {
        devType = new ArrayList<>();
        devType.add(0, "灯光");
        devType.add(1, "窗帘");
        devType.add(2, "家电");
        devType_input.setText(devType.get(dev_position));
        devType_input.setOnClickListener(this);
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        Bundle bundle = new Bundle();
        transaction = getSupportFragmentManager().beginTransaction();
        sceneFragment_dev = new SceneFragment_dev();
        bundle.putInt("room_position", room_position);
        bundle.putInt("sceneid", sceneid);
        bundle.putInt("dev_position", dev_position);
        //是否打开只看关联设备模式
        bundle.putBoolean("ISCHOOSE", ISCHOOSE);
        sceneFragment_dev.setArguments(bundle);
        transaction.replace(R.id.home, sceneFragment_dev);
        transaction.commit();
    }

    /**
     * 初始化情景的ListView
     */
    private void initHorizontalListView() {
        event.clear();
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
            event.add(MyApplication.getWareData().getSceneEvents().get(i));
        }
        event.add(new WareSceneEvent());
        recyclerAdapter = new RecyclerViewAdapter(event);
        mRecyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnItemClick(new RecyclerViewAdapter.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = event.size();
                if (listSize > 0) {
                    if (position < listSize - 1) {
                        sceneid = MyApplication.getWareData().getSceneEvents().get(position).getEventld();
                        transaction = getSupportFragmentManager().beginTransaction();
                        sceneFragment_dev = new SceneFragment_dev();
                        Bundle bundle = new Bundle();
                        bundle.putInt("room_position", room_position);
                        bundle.putInt("sceneid", sceneid);
                        bundle.putInt("dev_position", dev_position);
                        //是否打开只看关联设备模式
                        bundle.putBoolean("ISCHOOSE", ISCHOOSE);
                        sceneFragment_dev.setArguments(bundle);
                        transaction.replace(R.id.home, sceneFragment_dev);
                        transaction.commit();
                    } else {
                        if (MyApplication.getWareData().getSceneEvents().size() == 8) {
                            ToastUtil.showToast(SceneSetActivity2.this, "最多添加8个情景模式");
                            return;
                        }
                        getDialog();
                    }
                } else {
                    ToastUtil.showToast(SceneSetActivity2.this, "数据异常");
                }
            }

            //长按删除情景
            @Override
            public void OnItemLongClick(View view, final int position) {
                int listSize = event.size();
                if (listSize > 0) {
                    if (position < 2) {
                        ToastUtil.showToast(SceneSetActivity2.this, "全开、全关模式不可删除");
                        return;
                    } else if (position < listSize - 1) {
                        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(SceneSetActivity2.this);
                        builder.setTitle("提示 :");
                        builder.setMessage("您确定删除此模式?");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                initDialog("正在删除...");
                                byte sceneid = MyApplication.getWareData().getSceneEvents().get(position).getEventld();
                                String name = MyApplication.getWareData().getSceneEvents().get(position).getSceneName();
                                //删除情景模式
                                del_scene(sceneid, name);
                            }
                        });
                        builder.create().show();
                    } else {
                        return;
                    }
                } else {
                    ToastUtil.showToast(SceneSetActivity2.this, "数据异常");
                }
            }
        });
    }

    /**
     * 初始化自定义dialog
     * 新增情景
     */
    CustomDialog dialog;

    public void getDialog() {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_sceneset);
        dialog.show();
        iv_cancel = (ImageView) dialog.findViewById(R.id.scene_iv_cancel);
        name = (EditText) dialog.findViewById(R.id.scene_et_name);
        sure = (Button) dialog.findViewById(R.id.scene_btn_sure);
        cancel = (Button) dialog.findViewById(R.id.scene_btn_cancel);
        iv_cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.scene_iv_cancel:
                dialog.dismiss();
                break;
            //房间
            case R.id.input_room:
                initPopupWindow(input_room, home_text, 2);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            //设备类型
            case R.id.devType_input:
                initPopupWindow(devType_input, devType, 1);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.input_choose:
                if (ISCHOOSE) {
                    ISCHOOSE = false;
                    input_choose.setImageResource(R.drawable.off);
                } else {
                    ISCHOOSE = true;
                    input_choose.setImageResource(R.drawable.on);
                }
                onGetIsChooseListener.getOutChoose(ISCHOOSE);
                break;
            case R.id.scene_btn_sure:
                dialog.dismiss();
                initDialog("正在添加...");
                String data = name.getText().toString();
                if (!"".equals(data)) {
                    //新增情景模式
                    List<Integer> Scene_int = new ArrayList<>();
                    for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                        Scene_int.add((int) MyApplication.getWareData().getSceneEvents().get(i).getEventld());
                    }
                    List<Integer> Scene_id = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        Scene_id.add(i);
                    }
                    List<Integer> ID = new ArrayList<>();
                    for (int i = 0; i < Scene_id.size(); i++) {
                        if (!Scene_int.contains(Scene_id.get(i))) {
                            ID.add(Scene_id.get(i));
                        }
                    }
                    add_scene((byte) (int) ID.get(0), data);
                } else {
                    mDialog.dismiss();
                    ToastUtil.showToast(SceneSetActivity2.this, "请填写情景名称");
                }
                break;
            case R.id.scene_btn_cancel:
                dialog.dismiss();
                break;
            case R.id.sceneSet_save:
                if (sceneid < 2) {
                    ToastUtil.showToast(SceneSetActivity2.this, "全开、全关模式不可操作");
                    return;
                } else {
                    save();
                }
//                ReadWrite();
//                String abc = "";
//                for (int i = 0; i < MyApplication.getWareData().getAirConds().size(); i++) {
//                    abc += MyApplication.getWareData().getAirConds().get(i).getbOnOff() + ",";
//                }
//                Log.e("DATA", abc);
//                abc = "";
//                for (int i = 0; i < MyApplication.getWareData_Scene().getAirConds().size(); i++) {
//                    abc += MyApplication.getWareData_Scene().getAirConds().get(i).getbOnOff() + ",";
//                }
//                Log.e("DATA", abc);
                break;
        }

    }

    private static OnGetIsChooseListener onGetIsChooseListener;
    public static void setOnGetIsChooseListener(OnGetIsChooseListener ongetIsChooseListener) {
        onGetIsChooseListener = ongetIsChooseListener;
    }
    public interface OnGetIsChooseListener {
        void getOutChoose(boolean ischoose);
    }

    /**
     * 保存情景设置；
     */
    public void save() {

        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(SceneSetActivity2.this);
        builder.setTitle("提示 :");
        builder.setMessage("您要保存这些设置吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                initDialog("正在保存...");
                WareSceneEvent Sceneevent = null;
                try {
                    for (int i = 0; i < MyApplication.getWareData_Scene().getSceneEvents().size(); i++) {
                        if (sceneid == MyApplication.getWareData_Scene().getSceneEvents().get(i).getEventld()) {
                            Sceneevent = MyApplication.getWareData_Scene().getSceneEvents().get(i);
                            break;
                        }
                    }
//                for (int i = 0; i < Sceneevent.getItemAry().size(); i++) {
//                    Log.e("SCENE_COPY", "设备ID: " + Sceneevent.getItemAry().get(i).getDevID() + "设备开关: " + Sceneevent.getItemAry().get(i).getbOnOff());
//                }
//                WareSceneEvent Sceneevent_2 = null;
//                for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
//                    if (sceneid == MyApplication.getWareData().getSceneEvents().get(i).getEventld()) {
//                        Sceneevent_2 = MyApplication.getWareData().getSceneEvents().get(i);
//                        break;
//                    }
//                }
//                for (int i = 0; i < Sceneevent_2.getItemAry().size(); i++) {
//                    Log.e("SCENE", "设备ID: "+Sceneevent_2.getItemAry().get(i).getDevID()+"设备开关: "+ Sceneevent_2.getItemAry().get(i).getbOnOff());
//                }
                    String div;
                    String more_data = "";
                    String data_str = "";
                    div = ",";
                    for (int j = 0; j < Sceneevent.getItemAry().size(); j++) {
                        data_str = "{" +
                                "\"uid\":\"" + Sceneevent.getItemAry().get(j).getUid() + "\"," +
                                "\"devType\":" + Sceneevent.getItemAry().get(j).getDevType() + "," +
                                "\"devID\":" + Sceneevent.getItemAry().get(j).getDevID() + "," +
                                "\"bOnOff\":" + Sceneevent.getItemAry().get(j).getbOnOff() + "," +
                                "\"lmVal\":0," +
                                "\"rev2\":0," +
                                "\"rev3\":0," +
                                "\"param1\":0," +
                                "\"param2\":0}" + div;
                        more_data += data_str;
                    }
                    byte[] nameData = {0};
                    try {
                        nameData = Sceneevent.getSceneName().getBytes("GB2312");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String str_gb = CommonUtils.bytesToHexString(nameData);
                    Log.e("情景模式名称:%s", str_gb);
                    try {
                        more_data = more_data.substring(0, more_data.lastIndexOf(","));
                    } catch (Exception e) {
                        System.out.println(e + "");
                    }
                    //这就是要上传的字符串:data_hoad
                    String data_hoad = "{" +
                            "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                            "\"sceneName\":\"" + str_gb + "\"," +
                            "\"datType\":24" + "," +
                            "\"subType1\":0" + "," +
                            "\"subType2\":0" + "," +
                            "\"eventId\":" + Sceneevent.getEventld() + "," +
                            "\"devCnt\":" + Sceneevent.getItemAry().size() + "," +
                            "\"itemAry\":[" + more_data + "]}";
                    Log.e("情景模式测试:", data_hoad);
                    MyApplication.sendMsg(data_hoad);
                } catch (Exception e) {
                    mDialog.dismiss();
                    Log.e("Exception", e + "");
                    ToastUtil.showToast(SceneSetActivity2.this, "保存失败，数据异常");
                }
            }
        });
        builder.create().show();

    }

    /**
     * 新增情景模式
     *
     * @param sceneid
     * @param name
     */
    private void add_scene(byte sceneid, String name) {
        byte[] data = {0};
        try {
            data = name.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);
        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"sceneName\":\"" + str_gb + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_addSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + sceneid +
                ",\"devCnt\":" + 0 +
                ",\"itemAry\":[{" +
                "\"uid\":\"\"" +
                ",\"devType\":" + 0 +
                ",\"devID\":" + 0 +
                ",\"bOnOff\":" + 0 +
                ",\"lmVal\":0" +
                ",\"param1\":0" +
                ",\"param2\":0" +
                "}]}";
        LogUtils.LOGE("情景模式测试数据:", ctlStr);
        MyApplication.sendMsg(ctlStr);
    }

    /**
     * 删除情景模式
     *
     * @param sceneid
     * @param name
     */
    private void del_scene(byte sceneid, String name) {
        byte[] data = {0};
        try {
            data = name.getBytes("GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String str_gb = CommonUtils.bytesToHexString(data);
        LogUtils.LOGE("情景模式名称:%s", str_gb);
        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"sceneName\":\"" + str_gb + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delSceneEvents.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"eventId\":" + sceneid +
                ",\"devCnt\":" + 0 +
                ",\"itemAry\":[{" +
                "\"uid\":\"\"" +
                ",\"devType\":" + 0 +
                ",\"devID\":" + 0 +
                ",\"bOnOff\":" + 0 +
                ",\"lmVal\":0" +
                ",\"param1\":0" +
                ",\"param2\":0" +
                "}]}";
        LogUtils.LOGE("情景模式测试数据:", ctlStr);
        MyApplication.sendMsg(ctlStr);
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int tag) {
        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.popupwindow_equipment_listview, null);
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_p, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (tag == 2) {
                    room_position = position;
                    initFragment();
                } else if (tag == 1) {
                    dev_position = position;
                    initFragment();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK)
            return false;
        MyApplication.setWareData_Scene(null);
        finish();
        return false;
    }
}
