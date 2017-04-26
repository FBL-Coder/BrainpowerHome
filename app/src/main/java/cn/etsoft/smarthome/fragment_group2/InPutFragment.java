package cn.etsoft.smarthome.fragment_group2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.ListViewAdapter;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.adapter_group2.RecyclerViewAdapter;
import cn.etsoft.smarthome.domain.Save_Quipment;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class InPutFragment extends Fragment implements View.OnClickListener {
    private android.support.v7.widget.RecyclerView RecyclerView;
    private TextView equip_input;
    private Button input_save;
    private ImageView input_choose;
    private ListView input_listView_room;
    private RadioGroup radioGroup_sceneSet;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Fragment main_LightFragment, main_CurtainFragment, main_ApplianceFragment, main_SocketFragment, main_DoorFragment, main_SafetyFragment, main_ControlFragment;
    private PopupWindow popupWindow;
    //全部房间
    private int DEVS_ALL_ROOM = -1;
    private ListViewAdapter listViewAdapter;
    private int room_position = 0;
    private List<String> input_name;
    private int input_position = 0;
    private int DATTYPE_SET = 12;
    private FragmentTransaction transaction;
    private View view_p;
    private Handler handler;
    private boolean IsClose = false;
    private Dialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_p = inflater.inflate(R.layout.fragment_input2, container, false);
        RecyclerView = (android.support.v7.widget.RecyclerView) view_p.findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView.setLayoutManager(layoutManager);
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 11) {
                    MyApplication.mInstance.setInput_key_data(MyApplication.getWareData().getKeyOpItems());
                    onGetKeyInputDataListener.getKeyInputData();
                }
                if (what == 12 && MyApplication.getWareData_Scene().getResult() != null
                        && MyApplication.getWareData_Scene().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData_Scene().setResult(null);
                }
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mDialog != null)
                    mDialog.dismiss();
                //初始化控件
                initView(view_p);
                //初始化数据
                initData();
                //初始化房间的ListView
                initListView_room();
                //初始化设备的RadioGroup
                initRadioGroup(view_p);
            }
        };
        initDialog("正在加载...");
        ReadWrite();
        return view_p;
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
    private void initView(View view) {
        equip_input = (TextView) view.findViewById(R.id.equip_input);
        input_save = (Button) view.findViewById(R.id.input_save);
        input_choose = (ImageView) view.findViewById(R.id.input_choose);
        input_listView_room = (ListView) view.findViewById(R.id.input_listView_room);
        equip_input.setOnClickListener(this);
        input_save.setOnClickListener(this);
        input_choose.setOnClickListener(this);
    }

    /**
     * 初始化输入板数据
     */
    private void initData() {
        if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
            equip_input.setText("无");
            ToastUtil.showToast(getActivity(), "没有输入板");
            return;
        }
        input_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getKeyInputs().size(); i++) {
            input_name.add(MyApplication.getWareData_Scene().getKeyInputs().get(i).getBoardName());
        }
        if (input_name.size() > 0)
            equip_input.setText(input_name.get(0));
        //输入板ListView
        initRecycleView(view_p);
    }

    /**
     * 输入板ListView
     */
    List<String> keyname_list;

    private int index;
    private String uid;

    private void initRecycleView(View view) {
        String[] keyname = MyApplication.getWareData_Scene().getKeyInputs().get(input_position).getKeyName();
        uid = MyApplication.getWareData_Scene().getKeyInputs().get(input_position).getDevUnitID();
        MyApplication.getKeyItemInfo(0, uid);
        initDialog("初始化按键数据中...");
        if (keyname.length == 0)
            return;
        keyname_list = new ArrayList<>();
        for (int i = 0; i < keyname.length; i++) {
            keyname_list.add(keyname[i]);
        }
        recyclerViewAdapter = new RecyclerViewAdapter(keyname_list);
        RecyclerView.setAdapter(recyclerViewAdapter);
        //点击监听
        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                index = position;
                MyApplication.getKeyItemInfo(index, uid);
                initDialog("初始化按键数据中...");
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 初始化房间的ListView
     */
    private void initListView_room() {
        listViewAdapter = new ListViewAdapter(getActivity());
        //默认选中条目
        listViewAdapter.changeSelected(1);
        input_listView_room.setAdapter(listViewAdapter);
        input_listView_room.setItemsCanFocus(true);//让ListView的item获得焦点
        input_listView_room.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//单选模式
        //点击监听
        input_listView_room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewAdapter.changeSelected(position);//刷新
                if (position == 0)
                    room_position = DEVS_ALL_ROOM;
                else
                    room_position = position;
                onGetRoomListener.getRoomposition(position);
            }
        });
        //房间ListView选中监听
        input_listView_room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listViewAdapter.changeSelected(position);//刷新
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 初始化设备的RadioGroup
     */
    private void initRadioGroup(View view) {
        radioGroup_sceneSet = (RadioGroup) view.findViewById(R.id.radioGroup_sceneSet);
        //初始设备类型
        Bundle bundle = new Bundle();//给具体设备类型带入房间id
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        main_LightFragment = new Main_LightFragment();
        bundle.putInt("room_position", room_position);
        bundle.putInt("index", index);
        bundle.putBoolean("isClose", IsClose);
        main_LightFragment.setArguments(bundle);
        ((RadioButton) view.findViewById(R.id.light)).setChecked(true);
        transaction.replace(R.id.home, main_LightFragment);
        transaction.commit();

        radioGroup_sceneSet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();//给具体设备类型带入房间id
                bundle.putInt("room_position", room_position);
                bundle.putInt("index", index);
                bundle.putBoolean("isClose", IsClose);
                switch (checkedId) {
                    case R.id.light:
                        main_LightFragment = new Main_LightFragment();
                        main_LightFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_LightFragment);
                        break;
                    case R.id.curtain:
                        main_CurtainFragment = new Main_CurtainFragment();
                        main_CurtainFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_CurtainFragment);
                        break;
                    case R.id.appliance:
                        main_ApplianceFragment = new Main_ApplianceFragment();
                        main_ApplianceFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_ApplianceFragment);
                        break;
                    case R.id.socket:
                        main_SocketFragment = new Main_SocketFragment();
                        main_SocketFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SocketFragment);
                        break;
                    case R.id.door:
                        main_DoorFragment = new Main_DoorFragment();
                        main_DoorFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_DoorFragment);
                        break;
                    case R.id.safety:
                        main_SafetyFragment = new Main_SafetyFragment();
                        main_SafetyFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_SafetyFragment);
                        break;
                    case R.id.control:
                        main_ControlFragment = new Main_ControlFragment();
                        main_ControlFragment.setArguments(bundle);
                        transaction.replace(R.id.home, main_ControlFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int widthOff = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
        switch (v.getId()) {
            case R.id.equip_input:
                initPopupWindow(equip_input, input_name, 1);
                popupWindow.showAsDropDown(v, -widthOff, 0);
                break;
            case R.id.input_choose:
                if (IsClose) {
                    IsClose = false;
                    input_choose.setImageResource(R.drawable.off);
                } else {
                    IsClose = true;
                    input_choose.setImageResource(R.drawable.on);
                }
                onIsCloseListener.getIsClose(IsClose);
                break;
            case R.id.input_save:
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(getActivity());
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
                        List<WareKeyOpItem> keyOpItems = MyApplication.mInstance.getInput_key_data();
                        if (keyOpItems.size() == 0)
                            return;
                        Save_Quipment save_quipment = new Save_Quipment();
                        List<Save_Quipment.key_Opitem_Rows> list_kor = new ArrayList<>();
                        for (int i = 0; i < keyOpItems.size(); i++) {
                            Save_Quipment.key_Opitem_Rows key_opitem_rows = save_quipment.new key_Opitem_Rows();
                            key_opitem_rows.setOut_cpuCanID(keyOpItems.get(i).getDevUnitID());
                            key_opitem_rows.setDevID(keyOpItems.get(i).getDevId());
                            key_opitem_rows.setDevType(keyOpItems.get(i).getDevType());
                            key_opitem_rows.setKeyOp(keyOpItems.get(i).getKeyOp());
                            key_opitem_rows.setKeyOpCmd(keyOpItems.get(i).getKeyOpCmd());
                            list_kor.add(key_opitem_rows);
                        }
                        save_quipment.setDevUnitID(GlobalVars.getDevid());
                        save_quipment.setDatType(DATTYPE_SET);
                        save_quipment.setKey_cpuCanID(uid);
                        save_quipment.setKey_opitem(keyOpItems.size());
                        save_quipment.setKey_index(index);
                        save_quipment.setSubType1(0);
                        save_quipment.setSubType2(0);
                        save_quipment.setKey_opitem_rows(list_kor);
                        Gson gson = new Gson();
                        System.out.println(gson.toJson(save_quipment));
                        MyApplication.sendMsg(gson.toJson(save_quipment).toString());
                    }
                });
                builder.create().show();
                break;
        }
    }

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(getActivity());
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
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int tag) {
        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(getActivity(), R.layout.popupwindow_equipment_listview, null);
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), 200, 300);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, getActivity());
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_p, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                input_position = position;
                initRecycleView(view_p);
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

    private static OnIsCloseListener onIsCloseListener;
    private static OnGetRoomListener onGetRoomListener;
    private static OnGetKeyInputDataListener onGetKeyInputDataListener;

    public static void setOnIsCloseListener(OnIsCloseListener onisCloseListener) {
        onIsCloseListener = onisCloseListener;
    }

    public static void setOnGetRoomListener(OnGetRoomListener ongetRoomListener) {
        onGetRoomListener = ongetRoomListener;
    }

    public static void setOnGetKeyInputDataListener(OnGetKeyInputDataListener ongetKeyInputDataListener) {
        onGetKeyInputDataListener = ongetKeyInputDataListener;
    }

    //是否为只看存在设备模式接口
    interface OnIsCloseListener {
        void getIsClose(boolean isClose);
    }

    //房间变化接口
    public interface OnGetRoomListener {
        void getRoomposition(int room_position_click);
    }

    //获取数据接口
    interface OnGetKeyInputDataListener {
        void getKeyInputData();
    }
}
