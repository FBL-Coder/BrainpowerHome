package cn.etsoft.smarthome.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.fragment_main.Main_ApplianceFragment;
import cn.etsoft.smarthome.fragment_main.Main_ControlFragment;
import cn.etsoft.smarthome.fragment_main.Main_CurtainFragment;
import cn.etsoft.smarthome.fragment_main.Main_DoorFragment;
import cn.etsoft.smarthome.fragment_main.Main_LightFragment;
import cn.etsoft.smarthome.fragment_main.Main_SafetyFragment;
import cn.etsoft.smarthome.fragment_main.Main_SceneFragment;
import cn.etsoft.smarthome.fragment_main.Main_SocketFragment;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.ListViewAdapter;
import cn.etsoft.smarthome.pullmi.entity.WareDev;

/**
 * Created by Say GoBay on 2016/11/28.
 * 主页
 */
public class MainActivity extends FragmentActivity {
    private RadioGroup radioGroup_main;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment main_LightFragment, main_SceneFragment, main_CurtainFragment, main_ApplianceFragment, main_SocketFragment, main_DoorFragment, main_SafetyFragment, main_ControlFragment;
    private ListView listView_main;
    private String[] title = {"主页", "灯光", "情景", "窗帘", "家电", "插座", "门锁", "安防", "监控"};
    private Button homepage;
    private List<String> text;
    private List<WareDev> mWareDev_room;
    private int room_position = 0;
    private String type_name;
    //全部房间
    private int DEV_ALL_ROOM = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化ListView
        initListView();
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //按钮"主页"
        homepage = (Button) findViewById(R.id.homepage);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragmentManager = getSupportFragmentManager();
        radioGroup_main = (RadioGroup) findViewById(R.id.radioGroup_main);
        transaction = fragmentManager.beginTransaction();

        //设备类型 从首页获取到的
        type_name = getIntent().getStringExtra("title").toString();
        //房间集合
        text = MyApplication.getRoom_list();
        mWareDev_room = new ArrayList<>();

        //初始化RadioGroup
        initRadioGroup();
    }

    /**
     * 初始化RadioGroup
     */
    private void initRadioGroup() {
        //给具体设备类型带入房间id
        Bundle bundle = new Bundle();
        if (type_name.equals(title[1])) {
            listView_main.setVisibility(View.VISIBLE);
            main_LightFragment = new Main_LightFragment();
            bundle.putInt("room_position", room_position);
            main_LightFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.light)).setChecked(true);
            transaction.replace(R.id.home, main_LightFragment);
            transaction.commit();
        } else if (type_name.equals(title[2])) {
            //情景模块没有房间选择
            listView_main.setVisibility(View.GONE);
            main_SceneFragment = new Main_SceneFragment();
            bundle.putInt("room_position", room_position);
            main_SceneFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.scene)).setChecked(true);
            transaction.replace(R.id.home, main_SceneFragment);
            transaction.commit();
        } else if (type_name.equals(title[3])) {
            listView_main.setVisibility(View.VISIBLE);
            main_CurtainFragment = new Main_CurtainFragment();
            bundle.putInt("room_position", room_position);
            main_CurtainFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.curtain)).setChecked(true);
            transaction.replace(R.id.home, main_CurtainFragment);
            transaction.commit();
        } else if (type_name.equals(title[4])) {
            listView_main.setVisibility(View.VISIBLE);
            main_ApplianceFragment = new Main_ApplianceFragment();
            bundle.putInt("room_position", room_position);
            main_ApplianceFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.appliance)).setChecked(true);
            transaction.replace(R.id.home, main_ApplianceFragment);
            transaction.commit();
        } else if (type_name.equals(title[5])) {
            listView_main.setVisibility(View.VISIBLE);
            main_SocketFragment = new Main_SocketFragment();
            bundle.putInt("room_position", room_position);
            main_SocketFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.socket)).setChecked(true);
            transaction.replace(R.id.home, main_SocketFragment);
            transaction.commit();
        } else if (type_name.equals(title[6])) {
            listView_main.setVisibility(View.VISIBLE);
            main_DoorFragment = new Main_DoorFragment();
            bundle.putInt("room_position", room_position);
            main_DoorFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.door)).setChecked(true);
            transaction.replace(R.id.home, main_DoorFragment);
            transaction.commit();
        } else if (type_name.equals(title[7])) {
            listView_main.setVisibility(View.VISIBLE);
            main_SafetyFragment = new Main_SafetyFragment();
            bundle.putInt("room_position", room_position);
            main_SafetyFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.safety)).setChecked(true);
            transaction.replace(R.id.home, main_SafetyFragment);
            transaction.commit();
        } else if (type_name.equals(title[8])) {
            listView_main.setVisibility(View.VISIBLE);
            main_ControlFragment = new Main_ControlFragment();
            bundle.putInt("room_position", room_position);
            main_ControlFragment.setArguments(bundle);
            ((RadioButton) findViewById(R.id.control)).setChecked(true);
            transaction.replace(R.id.home, main_ControlFragment);
            transaction.commit();
        }
        radioGroup_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                //给具体设备类型带入房间id
                bundle.putInt("room_position", room_position);
                switch (checkedId) {
                    case R.id.light:
                        listView_main.setVisibility(View.VISIBLE);
                        main_LightFragment = new Main_LightFragment();
                        main_LightFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_LightFragment);
                        type_name = title[1];
                        break;
                    case R.id.scene:
                        listView_main.setVisibility(View.GONE);
                        main_SceneFragment = new Main_SceneFragment();
                        main_SceneFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SceneFragment);
                        type_name = title[2];
                        break;
                    case R.id.curtain:
                        listView_main.setVisibility(View.VISIBLE);
                        main_CurtainFragment = new Main_CurtainFragment();
                        main_CurtainFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_CurtainFragment);
                        type_name = title[3];
                        break;
                    case R.id.appliance:
                        listView_main.setVisibility(View.VISIBLE);
                        main_ApplianceFragment = new Main_ApplianceFragment();
                        main_ApplianceFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_ApplianceFragment);
                        type_name = title[4];
                        break;
                    case R.id.socket:
                        listView_main.setVisibility(View.VISIBLE);
                        main_SocketFragment = new Main_SocketFragment();
                        main_SocketFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SocketFragment);
                        type_name = title[5];
                        break;
                    case R.id.door:
                        listView_main.setVisibility(View.VISIBLE);
                        main_DoorFragment = new Main_DoorFragment();
                        main_DoorFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_DoorFragment);
                        type_name = title[6];
                        break;
                    case R.id.safety:
                        listView_main.setVisibility(View.VISIBLE);
                        main_SafetyFragment = new Main_SafetyFragment();
                        main_SafetyFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SafetyFragment);
                        type_name = title[7];
                        break;
                    case R.id.control:
                        listView_main.setVisibility(View.VISIBLE);
                        main_ControlFragment = new Main_ControlFragment();
                        main_ControlFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_ControlFragment);
                        type_name = title[8];
                        break;
                }
                transaction.commit();
            }
        });
    }

    ListViewAdapter listViewAdapter;

    /**
     * 初始化ListView
     */
    private void initListView() {
        //房间名  从首页获取到的
        room_position = getIntent().getIntExtra("room_position", 0);

        listView_main = (ListView) findViewById(R.id.listView_main);
        listViewAdapter = new ListViewAdapter(this);
        //默认选中条目
        listViewAdapter.changeSelected(room_position + 1);
        listView_main.setAdapter(listViewAdapter);
        //让ListView的item获得焦点
        listView_main.setItemsCanFocus(true);
        //单选模式
        listView_main.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //点击监听
        listView_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //刷新
                listViewAdapter.changeSelected(position);
                transaction = fragmentManager.beginTransaction();
                //给具体设备类型带入房间id
                Bundle bundle = new Bundle();
                //因为有"全部"，当 position = 0时，是"全部"，room_position = -1；
                room_position = position - 1;
                if (position == 0) {
                    //全部
                    bundle.putInt("room_position", DEV_ALL_ROOM);
                    if (type_name.equals(title[1])) {
                        main_LightFragment = new Main_LightFragment();
                        main_LightFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.light)).setChecked(true);
                        transaction.replace(R.id.home, main_LightFragment);
                    } else if (type_name.equals(title[2])) {
                        main_SceneFragment = new Main_SceneFragment();
                        main_SceneFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.scene)).setChecked(true);
                        transaction.replace(R.id.home, main_SceneFragment);
                    } else if (type_name.equals(title[3])) {
                        main_CurtainFragment = new Main_CurtainFragment();
                        main_CurtainFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.curtain)).setChecked(true);
                        transaction.replace(R.id.home, main_CurtainFragment);
                    } else if (type_name.equals(title[4])) {
                        main_ApplianceFragment = new Main_ApplianceFragment();
                        main_ApplianceFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.appliance)).setChecked(true);
                        transaction.replace(R.id.home, main_ApplianceFragment);
                    } else if (type_name.equals(title[5])) {
                        main_SocketFragment = new Main_SocketFragment();
                        main_SocketFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.socket)).setChecked(true);
                        transaction.replace(R.id.home, main_SocketFragment);
                    } else if (type_name.equals(title[6])) {
                        main_DoorFragment = new Main_DoorFragment();
                        main_DoorFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.door)).setChecked(true);
                        transaction.replace(R.id.home, main_DoorFragment);
                    } else if (type_name.equals(title[7])) {
                        main_SafetyFragment = new Main_SafetyFragment();
                        main_SafetyFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.safety)).setChecked(true);
                        transaction.replace(R.id.home, main_SafetyFragment);
                    } else if (type_name.equals(title[8])) {
                        main_ControlFragment = new Main_ControlFragment();
                        main_ControlFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.control)).setChecked(true);
                        transaction.replace(R.id.home, main_ControlFragment);
                    }
                } else {
                    //其他房间
                    bundle.putInt("room_position", position - 1);
                    if (type_name.equals(title[1])) {
                        main_LightFragment = new Main_LightFragment();
                        main_LightFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.light)).setChecked(true);
                        transaction.replace(R.id.home, main_LightFragment);
                    } else if (type_name.equals(title[2])) {
                        main_SceneFragment = new Main_SceneFragment();
                        main_SceneFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.scene)).setChecked(true);
                        transaction.replace(R.id.home, main_SceneFragment);
                    } else if (type_name.equals(title[3])) {
                        main_CurtainFragment = new Main_CurtainFragment();
                        main_CurtainFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.curtain)).setChecked(true);
                        transaction.replace(R.id.home, main_CurtainFragment);
                    } else if (type_name.equals(title[4])) {
                        main_ApplianceFragment = new Main_ApplianceFragment();
                        main_ApplianceFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.appliance)).setChecked(true);
                        transaction.replace(R.id.home, main_ApplianceFragment);
                    } else if (type_name.equals(title[5])) {
                        main_SocketFragment = new Main_SocketFragment();
                        main_SocketFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.socket)).setChecked(true);
                        transaction.replace(R.id.home, main_SocketFragment);
                    } else if (type_name.equals(title[6])) {
                        main_DoorFragment = new Main_DoorFragment();
                        main_DoorFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.door)).setChecked(true);
                        transaction.replace(R.id.home, main_DoorFragment);
                    } else if (type_name.equals(title[7])) {
                        main_SafetyFragment = new Main_SafetyFragment();
                        main_SafetyFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.safety)).setChecked(true);
                        transaction.replace(R.id.home, main_SafetyFragment);
                    } else if (type_name.equals(title[8])) {
                        main_ControlFragment = new Main_ControlFragment();
                        main_ControlFragment.setArguments(bundle);
                        ((RadioButton) findViewById(R.id.control)).setChecked(true);
                        transaction.replace(R.id.home, main_ControlFragment);
                    }
                }
                transaction.commit();
            }

        });
        //选中监听
        listView_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //刷新
                listViewAdapter.changeSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}
