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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.ListViewAdapter;
import cn.etsoft.smarthome.adapter.RecyclerViewAdapter;
import cn.etsoft.smarthome.fragment_scene.Main_ApplianceFragment;
import cn.etsoft.smarthome.fragment_scene.Main_ControlFragment;
import cn.etsoft.smarthome.fragment_scene.Main_CurtainFragment;
import cn.etsoft.smarthome.fragment_scene.Main_DoorFragment;
import cn.etsoft.smarthome.fragment_scene.Main_LightFragment;
import cn.etsoft.smarthome.fragment_scene.Main_SafetyFragment;
import cn.etsoft.smarthome.fragment_scene.Main_SocketFragment;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2017/3/14.
 */
public class SceneSetActivity extends FragmentActivity implements View.OnClickListener {
    private Button sceneSet_back;
    private ListView sceneSet_listView;
    private RecyclerView mRecyclerView;
    private RadioGroup radioGroup_sceneSet;
    private List<WareSceneEvent> event;
    private RecyclerViewAdapter recyclerAdapter;
    private ListViewAdapter listViewAdapter;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String[] title = {"", "灯光", "窗帘", "家电", "插座", "门锁", "安防", "监控"};
    private String type_name;
    private int room_position = 0;
    private Fragment main_LightFragment, main_CurtainFragment, main_ApplianceFragment, main_SocketFragment, main_DoorFragment, main_SafetyFragment, main_ControlFragment;
    private ImageView iv_cancel;
    private EditText name;
    private Button sure, cancel, saveScene;
    private byte sceneid = 0;
    private Dialog mDialog;
    private Handler handler;

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
                //初始化房间的ListView
                initListView();
                //初始化设备的RadioGroup
                initRadioGroup();
            }
        };

        ReadWrite();

        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 23) {
                    //初始化情景的listView
                    initHorizontalListView();
                    ToastUtil.showToast(SceneSetActivity.this, "添加成功");
                }
                if (what == 24) {
                    ToastUtil.showToast(SceneSetActivity.this, "保存成功");
                }
                if (what == 25) {
                    initHorizontalListView();
                    ToastUtil.showToast(SceneSetActivity.this, "删除成功");
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
        transaction = fragmentManager.beginTransaction();
        sceneSet_back = (Button) findViewById(R.id.sceneSet_back);
        saveScene = (Button) findViewById(R.id.sceneSet_save);
        sceneSet_back.setOnClickListener(this);
        saveScene.setOnClickListener(this);
        event = new ArrayList<>();
    }

    /**
     * 初始化设备的RadioGroup
     */
    private void initRadioGroup() {
        radioGroup_sceneSet = (RadioGroup) findViewById(R.id.radioGroup_sceneSet);
        //初始设备类型
        Bundle bundle = new Bundle();//给具体设备类型带入房间id
        main_LightFragment = new Main_LightFragment();
        bundle.putInt("room_position", room_position);
        bundle.putByte("sceneid", (byte) 0);
        main_LightFragment.setArguments(bundle);
        type_name = title[1];
        ((RadioButton) findViewById(R.id.light)).setChecked(true);
        transaction.replace(R.id.home, main_LightFragment);
        transaction.commit();

        radioGroup_sceneSet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();//给具体设备类型带入房间id
                bundle.putInt("room_position", room_position);
                bundle.putByte("sceneid", sceneid);
                switch (checkedId) {
                    case R.id.light:
                        main_LightFragment = new Main_LightFragment();
                        main_LightFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_LightFragment);
                        type_name = title[1];
                        break;
                    case R.id.curtain:
                        main_CurtainFragment = new Main_CurtainFragment();
                        main_CurtainFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_CurtainFragment);
                        type_name = title[2];
                        break;
                    case R.id.appliance:
                        main_ApplianceFragment = new Main_ApplianceFragment();
                        main_ApplianceFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_ApplianceFragment);
                        type_name = title[3];
                        break;
                    case R.id.socket:
                        main_SocketFragment = new Main_SocketFragment();
                        main_SocketFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SocketFragment);
                        type_name = title[4];
                        break;
                    case R.id.door:
                        main_DoorFragment = new Main_DoorFragment();
                        main_DoorFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_DoorFragment);
                        type_name = title[5];
                        break;
                    case R.id.safety:
                        main_SafetyFragment = new Main_SafetyFragment();
                        main_SafetyFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SafetyFragment);
                        type_name = title[6];
                        break;
                    case R.id.control:
                        main_ControlFragment = new Main_ControlFragment();
                        main_ControlFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_ControlFragment);
                        type_name = title[7];
                        break;
                }
                transaction.commit();
            }
        });
    }

    /**
     * //初始化情景的ListView
     */
    private void initHorizontalListView() {
        event.clear();
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
            event.add(MyApplication.getWareData().getSceneEvents().get(i));
        }
        event.add(new WareSceneEvent());
        if (recyclerAdapter != null) {
            recyclerAdapter.notifyDataSetChanged();
        } else {
            recyclerAdapter = new RecyclerViewAdapter(event);
            mRecyclerView.setAdapter(recyclerAdapter);
        }

        recyclerAdapter.setOnItemClick(new RecyclerViewAdapter.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = event.size();
                if (listSize > 0) {
                    if (position < listSize - 1) {
                        sceneid = MyApplication.getWareData().getSceneEvents().get(position).getEventld();
                        List<WareSceneDevItem> items = null;
                        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                            if (sceneid == MyApplication.getWareData().getSceneEvents().get(i).getEventld()) {
                                items = MyApplication.getWareData().getSceneEvents().get(i).getItemAry();
                                break;
                            }
                        }

                        List<WareDev> weredev_interim = new ArrayList<>();
                        for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {
                            weredev_interim.add(MyApplication.getWareData_Scene().getDevs().get(i));
                        }

                        if (items != null) {
                            for (int a = 0; a < items.size(); a++) {//循环情景内的所有条目
                                for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {//循环本地所有设备
                                    WareDev dev = MyApplication.getWareData_Scene().getDevs().get(i);//拿到其中一个设备
                                    if (items.get(a).getDevID() == dev.getDevId() && items.get(a).getUid().equals(dev.getCanCpuId())
                                            && items.get(a).getDevType() == dev.getType()) {
                                        weredev_interim.remove(dev);
                                        if (dev.getType() == 0) {
                                            for (int j = 0; j < MyApplication.getWareData_Scene().getAirConds().size(); j++) {
                                                WareAirCondDev AirCondDev = MyApplication.getWareData_Scene().getAirConds().get(j);
                                                if (dev.getCanCpuId().equals(AirCondDev.getDev().getCanCpuId()) && dev.getDevId() == AirCondDev.getDev().getDevId()) {
                                                    if (items.get(a).getbOnOff() == 1) {
                                                        AirCondDev.setbOnOff((byte) 1);
                                                    }
                                                }
                                            }
                                        } else if (dev.getType() == 3) {
                                            for (int j = 0; j < MyApplication.getWareData_Scene().getLights().size(); j++) {
                                                WareLight Light = MyApplication.getWareData_Scene().getLights().get(j);
                                                if (dev.getCanCpuId().equals(Light.getDev().getCanCpuId()) && dev.getDevId() == Light.getDev().getDevId()) {
                                                    if (items.get(a).getbOnOff() == 1) {
                                                        Light.setbOnOff((byte) 1);
                                                    }
                                                }
                                            }
                                        } else if (dev.getType() == 4) {
                                            for (int j = 0; j < MyApplication.getWareData_Scene().getCurtains().size(); j++) {
                                                WareCurtain Curtain = MyApplication.getWareData_Scene().getCurtains().get(j);
                                                if (dev.getCanCpuId().equals(Curtain.getDev().getCanCpuId()) && dev.getDevId() == Curtain.getDev().getDevId()) {
                                                    if (items.get(a).getbOnOff() == 1) {
                                                        Curtain.setbOnOff((byte) 1);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (int a = 0; a < weredev_interim.size(); a++) {//循环情景内的所有条目
                                for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {//循环本地所有设备
                                    WareDev dev = MyApplication.getWareData_Scene().getDevs().get(i);//拿到其中一个设备
                                    if (weredev_interim.get(a).getDevId() == dev.getDevId() && weredev_interim.get(a).getCanCpuId().equals(dev.getCanCpuId())
                                            && weredev_interim.get(a).getType() == dev.getType()) {
                                        if (dev.getType() == 0) {
                                            for (int j = 0; j < MyApplication.getWareData_Scene().getAirConds().size(); j++) {
                                                WareAirCondDev AirCondDev = MyApplication.getWareData_Scene().getAirConds().get(j);
                                                if (dev.getCanCpuId().equals(AirCondDev.getDev().getCanCpuId()) && dev.getDevId() == AirCondDev.getDev().getDevId()) {
                                                    AirCondDev.setbOnOff((byte) 0);
                                                }
                                            }
                                        } else if (dev.getType() == 3) {
                                            for (int j = 0; j < MyApplication.getWareData_Scene().getLights().size(); j++) {
                                                WareLight Light = MyApplication.getWareData_Scene().getLights().get(j);
                                                if (dev.getCanCpuId().equals(Light.getDev().getCanCpuId()) && dev.getDevId() == Light.getDev().getDevId()) {
                                                    Light.setbOnOff((byte) 0);
                                                }
                                            }
                                        } else if (dev.getType() == 4) {
                                            for (int j = 0; j < MyApplication.getWareData_Scene().getCurtains().size(); j++) {
                                                WareCurtain Curtain = MyApplication.getWareData_Scene().getCurtains().get(j);
                                                if (dev.getCanCpuId().equals(Curtain.getDev().getCanCpuId()) && dev.getDevId() == Curtain.getDev().getDevId()) {
                                                    Curtain.setbOnOff((byte) 0);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {//循环所有设备
                                WareDev dev = MyApplication.getWareData_Scene().getDevs().get(i);//拿到其中一个设备
                                if (dev.getType() == 0) {
                                    for (int j = 0; j < MyApplication.getWareData_Scene().getAirConds().size(); j++) {
                                        WareAirCondDev AirCondDev = MyApplication.getWareData_Scene().getAirConds().get(j);
                                        AirCondDev.setbOnOff((byte) 0);
                                    }
                                } else if (dev.getType() == 3) {
                                    for (int j = 0; j < MyApplication.getWareData_Scene().getLights().size(); j++) {
                                        WareLight Light = MyApplication.getWareData_Scene().getLights().get(j);
                                        if (dev.getCanCpuId().equals(Light.getDev().getCanCpuId()) && dev.getDevId() == Light.getDev().getDevId()) {
                                            Light.setbOnOff((byte) 0);
                                        }
                                    }
                                } else if (dev.getType() == 4) {
                                    for (int j = 0; j < MyApplication.getWareData_Scene().getCurtains().size(); j++) {
                                        WareCurtain Curtain = MyApplication.getWareData_Scene().getCurtains().get(j);
                                        if (dev.getCanCpuId().equals(Curtain.getDev().getCanCpuId()) && dev.getDevId() == Curtain.getDev().getDevId()) {
                                            Curtain.setbOnOff((byte) 0);
                                        }
                                    }
                                }
                            }
                        }
                        //添加选择状态；
                        recyclerAdapter.notifyDataSetChanged();
                        //点击情景刷新界面显示
                        transaction = fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();//给具体设备类型带入房间id
                        bundle.putInt("room_position", room_position);
                        bundle.putByte("sceneid", sceneid);
                        selectDevType(bundle);
                        transaction.commit();
                    } else {
                        if (MyApplication.getWareData().getSceneEvents().size() == 8) {
                            ToastUtil.showToast(SceneSetActivity.this, "最多添加8个情景模式");
                            return;
                        }
                        getDialog();
                    }
                } else {
                    ToastUtil.showToast(SceneSetActivity.this, "数据异常");
                }
            }

            @Override
            public void OnItemLongClick(View view, final int position) {
                int listSize = event.size();
                if (listSize > 0) {
                    if (position < 2) {
                        ToastUtil.showToast(SceneSetActivity.this, "全开、全关模式不可删除");
                        return;
                    } else if (position < listSize - 1) {
                        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(SceneSetActivity.this);
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
                    ToastUtil.showToast(SceneSetActivity.this, "数据异常");
                }
            }
        });
    }


    /**
     * 选择设备类型
     *
     * @param bundle
     */
    private void selectDevType(Bundle bundle) {
        if (type_name.equals(title[1])) {
            main_LightFragment = new Main_LightFragment();
            main_LightFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.light)).setChecked(true);
            transaction.replace(R.id.home, main_LightFragment);
        } else if (type_name.equals(title[2])) {
            main_CurtainFragment = new Main_CurtainFragment();
            main_CurtainFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.curtain)).setChecked(true);
            transaction.replace(R.id.home, main_CurtainFragment);
        } else if (type_name.equals(title[3])) {
            main_ApplianceFragment = new Main_ApplianceFragment();
            main_ApplianceFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.appliance)).setChecked(true);
            transaction.replace(R.id.home, main_ApplianceFragment);
        } else if (type_name.equals(title[4])) {
            main_SocketFragment = new Main_SocketFragment();
            main_SocketFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.socket)).setChecked(true);
            transaction.replace(R.id.home, main_SocketFragment);
        } else if (type_name.equals(title[5])) {
            main_DoorFragment = new Main_DoorFragment();
            main_DoorFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.door)).setChecked(true);
            transaction.replace(R.id.home, main_DoorFragment);
        } else if (type_name.equals(title[6])) {
            main_SafetyFragment = new Main_SafetyFragment();
            main_SafetyFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.safety)).setChecked(true);
            transaction.replace(R.id.home, main_SafetyFragment);
        } else if (type_name.equals(title[7])) {
            main_ControlFragment = new Main_ControlFragment();
            main_ControlFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.control)).setChecked(true);
            transaction.replace(R.id.home, main_ControlFragment);
        }
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

    /**
     * 初始化房间的ListView
     */
    private void initListView() {
        sceneSet_listView = (ListView) findViewById(R.id.sceneSet_listView);
        List<String> room_list = MyApplication.getRoom_list();
        listViewAdapter = new ListViewAdapter(this, room_list);
        //默认选中条目
        listViewAdapter.changeSelected(0);
        sceneSet_listView.setAdapter(listViewAdapter);
        sceneSet_listView.setItemsCanFocus(true);//让ListView的item获得焦点
        sceneSet_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式
        //点击监听
        sceneSet_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewAdapter.changeSelected(position);//刷新
                transaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();//给具体设备类型带入房间id
                room_position = position;
                bundle.putInt("room_position", position);
                bundle.putByte("sceneid", sceneid);
                selectDevType(bundle);
                transaction.commit();
            }
        });
        //房间ListView选中监听
        sceneSet_listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listViewAdapter.changeSelected(position);//刷新
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.scene_iv_cancel:
                dialog.dismiss();
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
//                    for (int i = 0; i < ID.size(); i++) {
//                        Log.e("ID",ID.get(i)+"");
//                    }
                    add_scene((byte) (int) ID.get(0), data);
                } else {
                    mDialog.dismiss();
                    ToastUtil.showToast(SceneSetActivity.this, "请填写情景名称");
                }
                break;
            case R.id.scene_btn_cancel:
                dialog.dismiss();
                break;
            case R.id.sceneSet_save:
                save();
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
            case R.id.sceneSet_back:
                MyApplication.setWareData_Scene(null);
                finish();
                break;
        }

    }

    /**
     * 保存情景设置；
     */
    public void save() {

        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(SceneSetActivity.this);
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
                for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                    if (sceneid == MyApplication.getWareData().getSceneEvents().get(i).getEventld()) {
                        Sceneevent = MyApplication.getWareData().getSceneEvents().get(i);
                        break;
                    }
                }
                int num = 0;
                String div;
                String more_data = "";
                String data_str = "";
                for (int i = 0; i < MyApplication.getWareData_Scene().getDevs().size(); i++) {//循环所有设备
                    WareDev dev = MyApplication.getWareData_Scene().getDevs().get(i);//拿到其中一个设备
                    div = ",";
                    if (dev.getType() == 0) {
                        for (int j = 0; j < MyApplication.getWareData_Scene().getAirConds().size(); j++) {
                            WareAirCondDev AirCondDev = MyApplication.getWareData_Scene().getAirConds().get(j);
                            if (dev.getCanCpuId().equals(AirCondDev.getDev().getCanCpuId()) && dev.getDevId() == AirCondDev.getDev().getDevId()) {
                                if (AirCondDev.getbOnOff() == 1) {
                                    data_str = "{" +
                                            "\"uid\":\"" + dev.getCanCpuId() + "\"," +
                                            "\"devType\":" + dev.getType() + "," +
                                            "\"devID\":" + dev.getDevId() + "," +
                                            "\"bOnOff\":" + AirCondDev.getbOnOff() + "," +
                                            "\"lmVal\":0," +
                                            "\"rev2\":0," +
                                            "\"rev3\":0," +
                                            "\"param1\":0," +
                                            "\"param2\":0}" + div;
                                    num++;
                                }
                            }
                        }
                    } else if (dev.getType() == 1) {
                        for (int j = 0; j < MyApplication.getWareData_Scene().getTvs().size(); j++) {
                            WareTv tv = MyApplication.getWareData_Scene().getTvs().get(j);
                            if (dev.getCanCpuId().equals(tv.getDev().getCanCpuId()) && dev.getDevId() == tv.getDev().getDevId()) {
                                if (tv.getbOnOff() == 1) {
                                    data_str = "{" +
                                            "\"uid\":\"" + dev.getCanCpuId() + "\"," +
                                            "\"devType\":" + dev.getType() + "," +
                                            "\"devID\":" + dev.getDevId() + "," +
                                            "\"bOnOff\":" + tv.getbOnOff() + "," +
                                            "\"lmVal\":0," +
                                            "\"rev2\":0," +
                                            "\"rev3\":0," +
                                            "\"param1\":0," +
                                            "\"param2\":0}" + div;
                                    num++;

                                }
                            }
                        }
                    } else if (dev.getType() == 2) {
                        for (int j = 0; j < MyApplication.getWareData_Scene().getStbs().size(); j++) {
                            WareSetBox box = MyApplication.getWareData_Scene().getStbs().get(j);
                            if (dev.getCanCpuId().equals(box.getDev().getCanCpuId()) && dev.getDevId() == box.getDev().getDevId()) {
                                if (box.getbOnOff() == 1) {
                                    data_str = "{" +
                                            "\"uid\":\"" + dev.getCanCpuId() + "\"," +
                                            "\"devType\":" + dev.getType() + "," +
                                            "\"devID\":" + dev.getDevId() + "," +
                                            "\"bOnOff\":" + box.getbOnOff() + "," +
                                            "\"lmVal\":0," +
                                            "\"rev2\":0," +
                                            "\"rev3\":0," +
                                            "\"param1\":0," +
                                            "\"param2\":0}" + div;
                                    num++;
                                }
                            }
                        }
                    } else if (dev.getType() == 3) {
                        for (int j = 0; j < MyApplication.getWareData_Scene().getLights().size(); j++) {
                            WareLight Light = MyApplication.getWareData_Scene().getLights().get(j);
                            if (dev.getCanCpuId().equals(Light.getDev().getCanCpuId()) && dev.getDevId() == Light.getDev().getDevId()) {
                                if (Light.getbOnOff() == 1) {
                                    data_str = "{" +
                                            "\"uid\":\"" + dev.getCanCpuId() + "\"," +
                                            "\"devType\":" + dev.getType() + "," +
                                            "\"devID\":" + dev.getDevId() + "," +
                                            "\"bOnOff\":" + Light.getbOnOff() + "," +
                                            "\"lmVal\":0," +
                                            "\"rev2\":0," +
                                            "\"rev3\":0," +
                                            "\"param1\":0," +
                                            "\"param2\":0}" + div;
                                    num++;
                                }
                            }
                        }
                    } else if (dev.getType() == 4) {
                        for (int j = 0; j < MyApplication.getWareData_Scene().getCurtains().size(); j++) {
                            WareCurtain Curtain = MyApplication.getWareData_Scene().getCurtains().get(j);
                            if (dev.getCanCpuId().equals(Curtain.getDev().getCanCpuId()) && dev.getDevId() == Curtain.getDev().getDevId()) {
                                if (Curtain.getbOnOff() == 1) {
                                    data_str = "{" +
                                            "\"uid\":\"" + dev.getCanCpuId() + "\"," +
                                            "\"devType\":" + dev.getType() + "," +
                                            "\"devID\":" + dev.getDevId() + "," +
                                            "\"bOnOff\":" + Curtain.getbOnOff() + "," +
                                            "\"lmVal\":0," +
                                            "\"rev2\":0," +
                                            "\"rev3\":0," +
                                            "\"param1\":0," +
                                            "\"param2\":0}" + div;
                                    num++;
                                }
                            }
                        }
                    }
                    more_data += data_str;
                    data_str = "";
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
                        "\"devCnt\":" + num + "," +
                        "\"itemAry\":[" + more_data + "]}";
                Log.e("情景模式测试:", data_hoad);
                MyApplication.sendMsg(data_hoad);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK)
            return false;
        MyApplication.setWareData_Scene(null);
        finish();
        return false;
    }
}
