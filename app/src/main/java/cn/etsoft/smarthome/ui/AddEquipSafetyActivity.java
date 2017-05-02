package cn.etsoft.smarthome.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.ListViewAdapter;
import cn.etsoft.smarthome.fragment_safety.Main_ApplianceFragment;
import cn.etsoft.smarthome.fragment_safety.Main_CurtainFragment;
import cn.etsoft.smarthome.fragment_safety.Main_DoorFragment;
import cn.etsoft.smarthome.fragment_safety.Main_LightFragment;
import cn.etsoft.smarthome.fragment_safety.Main_SocketFragment;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;

/**
 * Created by Say GoBay on 2017/4/20.
 * 高级设置-安防设置-防区添加设备页面
 */
public class AddEquipSafetyActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentTransaction transaction;
    //全部房间
    private int DEVS_ALL_ROOM = -1;
    private ListViewAdapter listViewAdapter;
    private int room_position = 0;
    private ListView safety_listView;
    private RadioGroup radioGroup_sceneSet;
    private Fragment main_LightFragment, main_CurtainFragment, main_ApplianceFragment, main_SocketFragment, main_DoorFragment;
    private Button confirm;
    private String[] title = {"", "灯光", "窗帘", "家电", "插座", "门锁"};
    private String type_name;
    private Dialog mDialog;
    private Handler handler;
    private int safety_position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_equipment);
        //进度条
        initDialog("初始化数据中...");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
                //初始化控件
                initView();
                //初始化房间的ListView
                initListView_room();
                //初始化设备的RadioGroup
                initRadioGroup();
            }
        };
        ReadWrite();
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

    /**
     * 初始化控件
     */
    private void initView() {
        safety_listView = (ListView) findViewById(R.id.safety_listView);
        radioGroup_sceneSet = (RadioGroup) findViewById(R.id.radioGroup_sceneSet);
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        safety_position = getIntent().getIntExtra("safety_position", 0);
    }

    /**
     * 初始化房间的ListView
     */
    private void initListView_room() {
        listViewAdapter = new ListViewAdapter(this);
        //默认选中条目
        listViewAdapter.changeSelected(room_position + 1);
        safety_listView.setAdapter(listViewAdapter);
        safety_listView.setItemsCanFocus(true);//让ListView的item获得焦点
        safety_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式
        //点击监听
        safety_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewAdapter.changeSelected(position);//刷新
                transaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();//给具体设备类型带入房间id
                room_position = position - 1;
                bundle.putInt("safety_position", safety_position);
                if (position == 0) {
                    bundle.putInt("room_position", DEVS_ALL_ROOM);
                } else {
                    bundle.putInt("room_position", position - 1);
                }
                selectDevType(bundle);
                transaction.commit();
            }
        });
        //房间ListView选中监听
        safety_listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            case R.id.confirm:
                //限制最多只能添加4个设备
                if (MyApplication.getWareData().getResult_safety().getSec_info_rows().get(safety_position).getRun_dev_item().size() < 5) {
                    MyApplication.mInstance.setSafety_data_dev();
                    this.finish();
                }else {
                    ToastUtil.showToast(this,"最多只能添加4个设备");
                }
                break;
        }
    }

    /**
     * 初始化设备的RadioGroup
     */
    private void initRadioGroup() {
        radioGroup_sceneSet = (RadioGroup) findViewById(R.id.radioGroup_sceneSet);
        //初始设备类型
        Bundle bundle = new Bundle();//给具体设备类型带入房间id
        transaction = this.getSupportFragmentManager().beginTransaction();
        main_LightFragment = new Main_LightFragment();
        bundle.putInt("room_position", room_position);
        bundle.putInt("safety_position", safety_position);
        main_LightFragment.setArguments(bundle);
        type_name = title[1];
        ((RadioButton) findViewById(R.id.light)).setChecked(true);
        transaction.replace(R.id.home_safety, main_LightFragment);
        transaction.commit();
        radioGroup_sceneSet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = AddEquipSafetyActivity.this.getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();//给具体设备类型带入房间id
                bundle.putInt("room_position", room_position);
                bundle.putInt("safety_position", safety_position);
                switch (checkedId) {
                    case R.id.light:
                        main_LightFragment = new Main_LightFragment();
                        main_LightFragment.setArguments(bundle);
                        transaction.replace(R.id.home_safety, main_LightFragment);
                        type_name = title[1];
                        break;
                    case R.id.curtain:
                        main_CurtainFragment = new Main_CurtainFragment();
                        main_CurtainFragment.setArguments(bundle);
                        transaction.replace(R.id.home_safety, main_CurtainFragment);
                        type_name = title[2];
                        break;
                    case R.id.appliance:
                        main_ApplianceFragment = new Main_ApplianceFragment();
                        main_ApplianceFragment.setArguments(bundle);
                        transaction.replace(R.id.home_safety, main_ApplianceFragment);
                        type_name = title[3];
                        break;
                    case R.id.socket:
                        main_SocketFragment = new Main_SocketFragment();
                        main_SocketFragment.setArguments(bundle);
                        transaction.replace(R.id.home_safety, main_SocketFragment);
                        type_name = title[4];
                        break;
                    case R.id.door:
                        main_DoorFragment = new Main_DoorFragment();
                        main_DoorFragment.setArguments(bundle);
                        transaction.replace(R.id.home_safety, main_DoorFragment);
                        type_name = title[5];
                        break;
                }
                transaction.commit();
            }
        });
    }

    /**
     * 选择设备类型
     * @param bundle
     */
    private void selectDevType(Bundle bundle) {
        if (type_name.equals(title[1])) {
            main_LightFragment = new Main_LightFragment();
            main_LightFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.light)).setChecked(true);
            transaction.replace(R.id.home_safety, main_LightFragment);
        } else if (type_name.equals(title[2])) {
            main_CurtainFragment = new Main_CurtainFragment();
            main_CurtainFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.curtain)).setChecked(true);
            transaction.replace(R.id.home_safety, main_CurtainFragment);
        } else if (type_name.equals(title[3])) {
            main_ApplianceFragment = new Main_ApplianceFragment();
            main_ApplianceFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.appliance)).setChecked(true);
            transaction.replace(R.id.home_safety, main_ApplianceFragment);
        } else if (type_name.equals(title[4])) {
            main_SocketFragment = new Main_SocketFragment();
            main_SocketFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.socket)).setChecked(true);
            transaction.replace(R.id.home_safety, main_SocketFragment);
        } else if (type_name.equals(title[5])) {
            main_DoorFragment = new Main_DoorFragment();
            main_DoorFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.door)).setChecked(true);
            transaction.replace(R.id.home_safety, main_DoorFragment);
        }
    }
}
