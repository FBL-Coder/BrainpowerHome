package cn.etsoft.smarthome.fragment_group3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.adapter_group3.RecyclerViewAdapter_Dev_output;
import cn.etsoft.smarthome.adapter_group3.RecyclerViewAdapter_equip;
import cn.etsoft.smarthome.domain.Out_List_printcmd;
import cn.etsoft.smarthome.domain.PrintCmd;
import cn.etsoft.smarthome.domain.UpBoardKeyData;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.WareChnOpItem;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SpacesItemDecoration;

/**
 * Created by Say GoBay on 2016/8/25.
 * 高级设置-控制设置-输出
 */
public class OutPutFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity mActivity;
    private TextView input_room, devType_input;
    private Button input_save;
    private ImageView input_choose;
    private android.support.v7.widget.RecyclerView RecyclerView, RecyclerView_equip;
    private RecyclerViewAdapter_Dev_output recyclerAdapter_light;
    private RecyclerViewAdapter_equip recyclerAdapter_equip;
    private FragmentTransaction transaction;
    private int room_position = 0;
    private FragmentManager fragmentManager;
    private PopupWindow popupWindow;
    //全部房间
    private int DEVS_ALL_ROOM = -1, BOARD_UP = 15;
    private Fragment outPutFragment_key;
    private List<WareDev> list_Dev;
    private List<WareChnOpItem> ChnOpItem_list;
    private WareChnOpItem ChnOpItem;
    private int KEY_ACTION_UP = 1;
    private int position_keyinput = 0, devPosition = 0;
    private List<PrintCmd> listData;
    private List<Out_List_printcmd> listData_all;
    private boolean ISCHOOSE = false;
    private Dialog mDialog;
    private Handler handler;
    private View view_parent;
    private boolean IsHaveData = false;
    private List<String> home_text;
    private List<String> devType;
    private int dev_position = 0;
    private String[] keyName;
    private int keyCnt;

    public OutPutFragment(FragmentActivity activity) {
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_parent = inflater.inflate(R.layout.fragment_output3, container, false);
        RecyclerView = (android.support.v7.widget.RecyclerView) view_parent.findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mActivity);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView.setLayoutManager(layoutManager1);
        RecyclerView.addItemDecoration(new SpacesItemDecoration(10));
        RecyclerView_equip = (android.support.v7.widget.RecyclerView) view_parent.findViewById(R.id.RecyclerView_equip);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mActivity);
        RecyclerView_equip.setLayoutManager(layoutManager2);
        RecyclerView_equip.addItemDecoration(new SpacesItemDecoration(10));
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mDialog != null)
                    mDialog.dismiss();
                //初始化控件
                initView(view_parent);
                //初始化房间
                initRoom();
                //初始化设备
                initDev();
                //加载设备
                initRecycleView_dev();
                initRecyclerView_input();
            }
        };

        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();

                if (what == 14) {
                    IsHaveData = true;
                    final Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            MyApplication.mInstance.setOut_key_data(listData_all);
                            try {
                                onGetOutKeyDataListeener.getOutKeyData();
                            } catch (Exception e) {
                                Log.e("Exception", e + "");
                            }
                        }
                    };
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listData_all = new ArrayList<>();
                            ChnOpItem_list = new ArrayList<>();
                            if (MyApplication.getWareData().getChnOpItems() != null && MyApplication.getWareData().getChnOpItems().size() > 0) {
                                for (int i = 0; i < MyApplication.getWareData().getChnOpItems().size(); i++) {
                                    ChnOpItem_list.add(MyApplication.getWareData().getChnOpItems().get(i));
                                }
                            }
                            for (int j = 0; j < MyApplication.getWareData().getKeyInputs().size(); j++) {
                                List<String> list_Name = new ArrayList<>();
                                boolean isConcan = false;
                                for (int k = 0; k < ChnOpItem_list.size(); k++) {
                                    if (ChnOpItem_list.get(k).getCpuid()
                                            .equals(MyApplication.getWareData().getKeyInputs().get(j).getCanCpuID())) {
                                        ChnOpItem = ChnOpItem_list.get(k);
                                        keyName = MyApplication.getWareData().getKeyInputs().get(j).getKeyName();
                                        keyCnt = MyApplication.getWareData().getKeyInputs().get(j).getKeyCnt();
                                        if (keyCnt > keyName.length) {
                                            for (int i = 0; i < keyCnt; i++) {
                                                if (i >= keyName.length) {
                                                    list_Name.add("按键" + i);
                                                } else {
                                                    list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
                                                }
                                            }
                                        } else {
                                            for (int i = 0; i < keyCnt; i++) {
                                                list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
                                            }
                                        }
//                                        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().get(j).getKeyName().length; i++) {
//                                            list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
//                                        }
                                        isConcan = true;
                                    }
                                }
                                if (!isConcan) {
                                    ChnOpItem = new WareChnOpItem();
                                    ChnOpItem.setCpuid(MyApplication.getWareData().getKeyInputs().get(j).getCanCpuID());
                                    keyName = MyApplication.getWareData().getKeyInputs().get(j).getKeyName();
                                    keyCnt = MyApplication.getWareData().getKeyInputs().get(j).getKeyCnt();
//                                    for (int i = 0; i < MyApplication.getWareData().getKeyInputs().get(j).getKeyName().length; i++) {
//                                        list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
//                                    }
                                    if (keyCnt > keyName.length) {
                                        for (int i = 0; i < keyCnt; i++) {
                                            if (i >= keyName.length) {
                                                list_Name.add("按键" + i);
                                            } else {
                                                list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
                                            }
                                        }
                                    } else {
                                        for (int i = 0; i < keyCnt; i++) {
                                            list_Name.add(MyApplication.getWareData().getKeyInputs().get(j).getKeyName()[i]);
                                        }
                                    }
                                }
                                listData = new ArrayList<>();
//                                for (int i = 0; i < ChnOpItem.getKeyUpCmd().length; i++) {
                                for (int i = 0; i < list_Name.size(); i++) {
                                    PrintCmd cmd = new PrintCmd();
                                    cmd.setDevUnitID(ChnOpItem.getCpuid());
                                    try {
                                        cmd.setDevType(Dev_room.get(devPosition).getType());
                                    } catch (Exception e) {
                                    }
                                    cmd.setKey_cmd(ChnOpItem.getKeyUpCmd()[i]);
                                    cmd.setKeyAct_num(KEY_ACTION_UP);
                                    if (ChnOpItem.getKeyUpCmd()[i] > 0) cmd.setSelect(true);
                                    else cmd.setSelect(false);
                                    if (list_Name.size() == 0)
                                        cmd.setKeyname("未知按键");
                                    else {
//                                        try {
//                                            cmd.setKeyname(list_Name.get(i));
//                                        } catch (Exception e) {
//                                            if (i >= list_Name.size())
//                                                cmd.setKeyname("按键" + i);
//                                        }
                                        cmd.setKeyname(list_Name.get(i));
                                    }
                                    listData.add(cmd);
                                }
                                Out_List_printcmd list = new Out_List_printcmd();
                                list.setUnitid(ChnOpItem.getCpuid());
                                list.setPrintCmds(listData);
                                listData_all.add(list);
                            }
                            handler.sendMessage(handler.obtainMessage());
                        }
                    }).start();
                }

                if (what == 15 && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                }
            }
        });
        initDialog("正在加载...");
        ReadWrite();
        return view_parent;
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
        mDialog = Circle_Progress.createLoadingDialog(mActivity);
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
    private void initView(View view) {
        input_save = (Button) view.findViewById(R.id.input_save);
        input_choose = (ImageView) view.findViewById(R.id.input_choose);
        devType_input = (TextView) view.findViewById(R.id.devType_input);
        input_room = (TextView) view.findViewById(R.id.input_room);
        input_choose.setOnClickListener(this);
        input_save.setOnClickListener(this);
    }

    /**
     * 初始化房间
     */
    private void initRoom() {
        home_text = new ArrayList<>();
        home_text.add("全部");
        for (int i = 0; i < MyApplication.getRoom_list().size(); i++) {
            home_text.add(MyApplication.getRoom_list().get(i));
        }
        input_room.setText(home_text.get(room_position));
        input_room.setOnClickListener(this);
    }

    /**
     * 初始化设备
     */
    private void initDev() {
        devType = new ArrayList<>();
        devType.add(0, "灯光");
        devType.add(1, "窗帘");
        devType.add(2, "家电");
        devType_input.setText(devType.get(dev_position));
        devType_input.setOnClickListener(this);
    }

    //房间内所有设备；
    List<WareDev> Dev_room_all;
    //房间内指定类型设备
    List<WareDev> Dev_room;

    private void initRecycleView_dev() {
        list_Dev = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            list_Dev.add(MyApplication.getWareData().getDevs().get(i));
        }
        //房间内的设备集合
        Dev_room_all = new ArrayList<>();
        //房间内指定类型设备
        Dev_room = new ArrayList<>();
        //房间集合
        home_text = new ArrayList<>();
        home_text.add("全部");
        for (int i = 0; i < MyApplication.getRoom_list().size(); i++) {
            home_text.add(MyApplication.getRoom_list().get(i));
        }
        //根据房间id获取设备；
        if (room_position == 0) {
            Dev_room_all = list_Dev;
        } else {
            for (int i = 0; i < list_Dev.size(); i++) {
                if (list_Dev.get(i).getRoomName().equals(home_text.get(room_position))) {
                    Dev_room_all.add(list_Dev.get(i));
                }
            }
        }
        if (dev_position == 0) {
            for (int i = 0; i < Dev_room_all.size(); i++) {
                if (Dev_room_all.get(i).getType() == 3)
                    Dev_room.add(Dev_room_all.get(i));
            }
        } else if (dev_position == 1) {
            for (int i = 0; i < Dev_room_all.size(); i++) {
                if (Dev_room_all.get(i).getType() == 4)
                    Dev_room.add(Dev_room_all.get(i));
            }
        } else if (dev_position == 2)
            for (int i = 0; i < Dev_room_all.size(); i++) {
                if (Dev_room_all.get(i).getType() == 0 || Dev_room_all.get(i).getType() == 1 || Dev_room_all.get(i).getType() == 2)
                    Dev_room.add(Dev_room_all.get(i));
            }
        try {
            MyApplication.getChnItemInfo(Dev_room.get(0).getCanCpuId(), Dev_room.get(0).getType(), Dev_room.get(0).getDevId());
            initDialog("加载数据中...");
        } catch (Exception e) {
        }
        recyclerAdapter_light = new RecyclerViewAdapter_Dev_output(Dev_room);
        RecyclerView.setAdapter(recyclerAdapter_light);
        recyclerAdapter_light.setOnItemClick(new RecyclerViewAdapter_Dev_output.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                transaction = getChildFragmentManager().beginTransaction();
                devPosition = position;
                outPutFragment_key = new OutPutFragment_key(mActivity);
                Bundle bundle = new Bundle();
                bundle.putInt("devtype", Dev_room.get(position).getType());
                bundle.putInt("keyinput_position", position_keyinput);
                bundle.putBoolean("ISCHOOSE", ISCHOOSE);

                MyApplication.getChnItemInfo(Dev_room.get(position).getCanCpuId(), Dev_room.get(position).getType(), Dev_room.get(position).getDevId());
                initDialog("加载数据中...");
                outPutFragment_key.setArguments(bundle);
                transaction.replace(R.id.home, outPutFragment_key);
                transaction.commit();
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }

    private int input_position = 0;
    private List<String> input_name;

    /**
     * 初始化设备
     */
    private void initRecyclerView_input() {
        if (MyApplication.getWareData().getKeyInputs().size() == 0) {
            ToastUtil.showToast(mActivity, "没有收到输入板数据");
            return;
        }
        input_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().size(); i++) {
            input_name.add(MyApplication.getWareData().getKeyInputs().get(i).getBoardName());
        }
//        for (int i = 0; i <9 ; i++) {
//            input_name.add("按键板028"+i);
//        }
        recyclerAdapter_equip = new RecyclerViewAdapter_equip(input_name);
        RecyclerView_equip.setAdapter(recyclerAdapter_equip);
        transaction = getChildFragmentManager().beginTransaction();
        outPutFragment_key = new OutPutFragment_key(mActivity);
        Bundle bundle = new Bundle();
        if (Dev_room != null && Dev_room.size() > 0)
            bundle.putInt("devtype", Dev_room.get(0).getType());
        bundle.putInt("keyinput_position", 0);
        bundle.putBoolean("ISCHOOSE", ISCHOOSE);
        outPutFragment_key.setArguments(bundle);
        transaction.replace(R.id.home, outPutFragment_key);
        transaction.commit();
        recyclerAdapter_equip.setOnItemClick(new RecyclerViewAdapter_equip.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                transaction = getChildFragmentManager().beginTransaction();
                outPutFragment_key = new OutPutFragment_key(mActivity);
                Bundle bundle = new Bundle();
                bundle.putInt("devtype", Dev_room.get(devPosition).getType());
                bundle.putInt("keyinput_position", position);
                bundle.putBoolean("ISCHOOSE", ISCHOOSE);
                position_keyinput = position;
                outPutFragment_key.setArguments(bundle);
                transaction.replace(R.id.home, outPutFragment_key);
                transaction.commit();
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int tag) {
        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(mActivity, R.layout.popupwindow_equipment_listview, null);
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 250);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, mActivity);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (tag == 1) {
                    room_position = position;
                } else if (tag == 2) {
                    dev_position = position;
                }
//                board_position = position;
                initRecycleView_dev();
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
        if (!IsHaveData) {
            ToastUtil.showToast(mActivity, "获取数据异常，请稍后在试");
            return;
        }
//        int widthOff = mActivity.getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
        switch (v.getId()) {
            //房间
            case R.id.input_room:
                initPopupWindow(input_room, home_text, 1);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            //设备类型
            case R.id.devType_input:
                initPopupWindow(devType_input, devType, 2);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.input_save:
                if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
                    ToastUtil.showToast(mActivity, "没有输入板信息，不能保存");
                    return;
                }
                if (Dev_room.size() == 0) {
                    ToastUtil.showToast(mActivity, "房间没有设备，保存失败");
                    return;
                }
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(mActivity);
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
                        try {
                            listData_all = MyApplication.mInstance.getOut_key_data();
                            for (int i = 0; i < listData_all.size(); i++) {
                                List<PrintCmd> listData = listData_all.get(i).getPrintCmds();
                                for (int j = 0; j < listData.size(); j++) {
                                    if (listData.get(j).isSelect() && listData.get(j).getKey_cmd() == 0) {
                                        ToastUtil.showToast(mActivity, "存在未设置，请设置完成后保存");
                                        mDialog.dismiss();
                                        return;
                                    }
                                }
                            }
                            //根据以上注释掉的数据结构，将已有数据已此格式寄存；
                            UpBoardKeyData data = new UpBoardKeyData();//上传数据实体；
                            List<UpBoardKeyData.ChnOpitemRowsBean> bean_list = new ArrayList<>();//按键板实体集合；

                            for (int i = 0; i < listData_all.size(); i++) {
                                UpBoardKeyData.ChnOpitemRowsBean bean = data.new ChnOpitemRowsBean();
                                bean.setKey_cpuCanID(listData_all.get(i).getUnitid());
                                byte[] Valid_up = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应位置；
                                byte[] Cmd_up = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应的命令；
                                byte[] Valid_down = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键的相应位置；
                                byte[] Cmd_down = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键相应的命令
                                List<PrintCmd> listData = listData_all.get(i).getPrintCmds();

                                for (int j = 0; j < listData.size(); j++) {
                                    if (listData.get(j).isSelect()) {
                                        Cmd_up[j] = (byte) listData.get(j).getKey_cmd();
                                        Valid_up[j] = 1;
                                    } else {
                                        Cmd_up[j] = 0;
                                        Valid_up[j] = 0;
                                    }
                                }
                                for (int j = 0; j < listData.size(); j++) {
                                    Valid_down[j] = 0;
                                    Cmd_down[j] = 0;
                                }
                                bean.setKeyDownValid(0);
                                bean.setKeyUpValid(0);
                                bean.setKeyDownCmd(Cmd_down);
                                bean.setKeyUpCmd(Cmd_up);
                                //因为数据传递时，高位、低位和现实中相反，so循环赋值；
                                String down_v = "";
                                for (int j = 0; j < Valid_up.length; j++) {
                                    down_v += Valid_up[Valid_up.length - 1 - j];
                                }
                                //将改好的2#字符串转成10#；
                                BigInteger bi_down = new BigInteger(down_v, 2);  //转换成BigInteger类型
                                int v_down = Integer.parseInt(bi_down.toString(10)); //参数2指定的是转化成X进制，默认10进制
                                bean.setKeyUpValid(v_down);
                                bean_list.add(bean);
                            }

                            //将以上数据加入到上传实体中；
                            data.setDevUnitID(GlobalVars.getDevid());
                            data.setChn_opitem_rows(bean_list);
                            data.setDatType(BOARD_UP);
                            data.setSubType1(0);
                            data.setSubType2(0);
                            data.setDevType(Dev_room.get(devPosition).getType());
                            data.setDevID(Dev_room.get(devPosition).getDevId());
                            data.setOut_cpuCanID(Dev_room.get(devPosition).getCanCpuId());
                            data.setChn_opitem(bean_list.size());

                            Gson gson = new Gson();
                            System.out.println(gson.toJson(data));
                            MyApplication.sendMsg(gson.toJson(data).toString());
                        } catch (Exception e) {
                            Log.e("Exception", e + "");
                            ToastUtil.showToast(mActivity, "保存失败，请检查数据是否合适");
                        }
                    }
                });
                builder.create().show();
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

        }
    }

    //数据刷新后提供数据接口
    private static OnGetOutKeyDataListeener onGetOutKeyDataListeener;
    private static OnGetIsChooseListener onGetIsChooseListener;

    public static void setOnGetOutKeyDataListeener(OnGetOutKeyDataListeener ongetOutKeyDataListeener) {
        onGetOutKeyDataListeener = ongetOutKeyDataListeener;
    }

    public static void setOnGetIsChooseListener(OnGetIsChooseListener ongetIsChooseListener) {
        onGetIsChooseListener = ongetIsChooseListener;
    }

    interface OnGetOutKeyDataListeener {
        void getOutKeyData();
    }

    interface OnGetIsChooseListener {
        void getOutChoose(boolean ischoose);
    }
}
