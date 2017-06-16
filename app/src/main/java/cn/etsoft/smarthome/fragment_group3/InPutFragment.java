package cn.etsoft.smarthome.fragment_group3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.adapter_group3.RecyclerViewAdapter_input;
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
 * 高级设置-控制设置-按键配设备
 */
public class InPutFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity mActivity;
    private android.support.v7.widget.RecyclerView RecyclerView;
    private TextView equip_input,input_room,devType_input;
    private Button input_save;
    private ImageView input_choose;
    private RecyclerViewAdapter_input recyclerViewAdapter;
    private Fragment inputFragment_dev;
    private PopupWindow popupWindow;
    private int room_position = 0;
    private int dev_position = 0;
    private List<String> input_name;
    private List<String> home_text;
    private List<String> devType;
    private int input_position = 0;
    private int DATTYPE_SET = 12;
    private FragmentTransaction transaction;
    private View view_p;
    private Handler handler;
    private boolean IsClose = false;
    private Dialog mDialog;
    private boolean IsHaveData = false;

    public InPutFragment(FragmentActivity activity) {
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_p = inflater.inflate(R.layout.fragment_input3, container, false);
        //横向滑动的RecyclerView
        RecyclerView = (android.support.v7.widget.RecyclerView) view_p.findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView.setLayoutManager(layoutManager);
        //通知数据更新
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 11) {
                    IsHaveData = true;
                    MyApplication.mInstance.setInput_key_data(MyApplication.getWareData().getKeyOpItems());
                    try {
                        onGetKeyInputDataListener.getKeyInputData();
                    }catch (Exception e ){

                    }
                }
                if (what == 12 && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                    //保存成功之后将备用数据结果置空
                    MyApplication.getWareData().setResult(null);
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
                //初始化房间
                initRoom();
                //初始化设备类型
                initDev();
                //初始化Fragment
                initFragment();
            }
        };
        initDialog("正在加载...");
        //复写数据
        ReadWrite();
        return view_p;
    }

    //复写数据
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
        input_room = (TextView) view.findViewById(R.id.input_room);
        equip_input.setOnClickListener(this);
        input_save.setOnClickListener(this);
        input_choose.setOnClickListener(this);
        devType_input = (TextView) view.findViewById(R.id.devType_input);
    }

    /**
     * 初始化输入板数据
     */
    private void initData() {
        if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
            equip_input.setText("无");
            ToastUtil.showToast(mActivity, "没有收到输入板信息");
            return;
        }
        //获取按键板名称
        input_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData_Scene().getKeyInputs().size(); i++) {
            input_name.add(MyApplication.getWareData_Scene().getKeyInputs().get(i).getBoardName());
        }
        //设置按键板名称
        if (input_name.size() > 0)
            equip_input.setText(input_name.get(input_position));
        //初始化按键RecycleView
        initRecycleView(view_p);
    }

    /**
     * 初始化按键RecycleView
     */
    private List<String> keyName_list;
    private int index;
    private String uid;

    private void initRecycleView(View view) {
        String[] keyName = MyApplication.getWareData_Scene().getKeyInputs().get(input_position).getKeyName();
        uid = MyApplication.getWareData_Scene().getKeyInputs().get(input_position).getDevUnitID();
        //获取输入板对应设备的数据
        MyApplication.getKeyItemInfo(0, uid);
        initDialog("初始化按键数据中...");
        if (keyName.length == 0)
            return;
        //按键名称集合
        keyName_list = new ArrayList<>();
        for (int i = 0; i < keyName.length; i++) {
            keyName_list.add(keyName[i]);
        }
        Log.e("按键名称", String.valueOf(keyName_list));
        recyclerViewAdapter = new RecyclerViewAdapter_input(keyName_list);
        RecyclerView.setAdapter(recyclerViewAdapter);
        //点击监听
        recyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter_input.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                index = position;
                //获取输入板对应设备的数据
                MyApplication.getKeyItemInfo(index, uid);
                initDialog("初始化按键数据中...");
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
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
        devType.add(0,"灯光");
        devType.add(1,"窗帘");
        devType.add(2,"家电");
        devType_input.setText(devType.get(dev_position));
        devType_input.setOnClickListener(this);
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        Bundle bundle = new Bundle();
        transaction = mActivity.getSupportFragmentManager().beginTransaction();
        inputFragment_dev = new InputFragment_dev(mActivity);
        bundle.putInt("room_position", room_position);
        bundle.putInt("index", index);
        bundle.putInt("dev_position", dev_position);
        //是否打开只看关联设备模式
        bundle.putBoolean("IsClose", IsClose);
        inputFragment_dev.setArguments(bundle);
        transaction.replace(R.id.home, inputFragment_dev);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        if (!IsHaveData){
            ToastUtil.showToast(mActivity, "获取数据异常，请稍后在试");
            return;
        }
//        int widthOff = mActivity.getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
        switch (v.getId()) {
            //按键板
            case R.id.equip_input:
                initPopupWindow(equip_input, input_name, 1);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            //房间
            case R.id.input_room:
                initPopupWindow(input_room, home_text, 2);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            //设备类型
            case R.id.devType_input:
                initPopupWindow(devType_input, devType, 3);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            //是否打开只看关联设备模式
            case R.id.input_choose:
                if (IsClose) {
                    IsClose = false;
                    input_choose.setImageResource(R.drawable.off);
                } else {
                    IsClose = true;
                    input_choose.setImageResource(R.drawable.on);
                }
                //是否打开只看关联设备模式回调
                onIsCloseListener.getIsClose(IsClose);
                break;
            //保存
            case R.id.input_save:
                if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
                    ToastUtil.showToast(mActivity, "没有输入板信息，不能保存");
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
                        } catch (Exception e) {
                            Log.e("Exception", e + "");
                        }
                    }
                });
                builder.create().show();
                break;
        }
    }

    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(mActivity);
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, mActivity);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_p, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (tag == 1){
                    input_position = position;
                    initRecycleView(view_p);
                }else if (tag == 2){
                    room_position = position;
                    initFragment();
                }else if (tag == 3){
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

    /**
     * 是否为只看存在设备模式接口
     */
    private static OnIsCloseListener onIsCloseListener;

    public static void setOnIsCloseListener(OnIsCloseListener onisCloseListener) {
        onIsCloseListener = onisCloseListener;
    }

    interface OnIsCloseListener {
        void getIsClose(boolean isClose);
    }

    /**
     * 获取数据接口
     */
    private static OnGetKeyInputDataListener onGetKeyInputDataListener;

    public static void setOnGetKeyInputDataListener(OnGetKeyInputDataListener ongetKeyInputDataListener) {
        onGetKeyInputDataListener = ongetKeyInputDataListener;
    }

    interface OnGetKeyInputDataListener {
        void getKeyInputData();
    }
}
