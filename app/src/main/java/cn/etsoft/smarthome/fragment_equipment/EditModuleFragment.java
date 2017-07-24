package cn.etsoft.smarthome.fragment_equipment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.ModuleEditAdapter;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareBoardKeyInput;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2016/8/25.
 * 高级设置-控制设置-按键配设备
 */
public class EditModuleFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity mActivity;
    private TextView equip_input, style, backLight, save, room, count;
    private FragmentTransaction transaction;
    private List<String> key_style;
    private List<String> key_backLight;
    private List<String> count_text;
    private EditText name;
    private GridView gridView;
    private ModuleEditAdapter moduleEditAdapter1;
    private String[] keyName;
    private List<String> listData;
    private byte countData = 0;
    private List<String> home_text;
    private int room_position = 0;
    private int count_position = 0;
    private int countNumber = 0;

    public EditModuleFragment(FragmentActivity activity) {
        mActivity = activity;
    }

    private List<String> input_name;
    private int input_position = 0;
    private int style_position = 0;
    private int backLight_position = 0;
    private PopupWindow popupWindow;
    private Dialog mDialog;
    private View view_p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_p = inflater.inflate(R.layout.fragment_edit_module, container, false);
        //解决弹出键盘压缩布局的问题
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //初始化控件
        initView(view_p);
        //加载数据
        initData();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 9 && MyApplication.getWareData().getKyeInputResult() != null
                        && MyApplication.getWareData().getKyeInputResult().getSubType2() == 1) {
                    Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                } else if (what == 9 && MyApplication.getWareData().getKyeInputResult() != null
                        && MyApplication.getWareData().getKyeInputResult().getSubType2() == 0) {
                    Toast.makeText(mActivity, "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view_p;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        equip_input = (TextView) view.findViewById(R.id.equip_input);
        name = (EditText) view.findViewById(R.id.name);
        room = (TextView) view.findViewById(R.id.room);
        count = (TextView) view.findViewById(R.id.count);
        style = (TextView) view.findViewById(R.id.style);
        backLight = (TextView) view.findViewById(R.id.backLight);
        save = (TextView) view.findViewById(R.id.save);
        gridView = (GridView) view.findViewById(R.id.gridView);
        equip_input.setOnClickListener(this);
        style.setOnClickListener(this);
        backLight.setOnClickListener(this);
        save.setOnClickListener(this);
        room.setOnClickListener(this);
        count.setOnClickListener(this);
    }

    String roomName;

    /**
     * 加载数据
     */
    private void initData() {
        if (MyApplication.getWareData().getKeyInputs() == null || MyApplication.getWareData().getKeyInputs().size() == 0) {
            equip_input.setText("无");
            ToastUtil.showToast(mActivity, "没有收到输入板信息");
            return;
        }
        //获取按键板名称
        input_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().size(); i++) {
            input_name.add(MyApplication.getWareData().getKeyInputs().get(i).getBoardName());
        }
        //设置按键板名称
        if (input_name.size() > 0) {
            equip_input.setText(input_name.get(input_position));
        }
        roomName = MyApplication.getWareData().getKeyInputs().get(input_position).getRoomName();
        style_position = MyApplication.getWareData().getKeyInputs().get(input_position).getbResetKey();
        backLight_position = MyApplication.getWareData().getKeyInputs().get(input_position).getLedBkType();
        countNumber = MyApplication.getWareData().getKeyInputs().get(input_position).getKeyCnt();
        name.setText("");
        name.setHint(input_name.get(input_position));
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                equip_input.setText(name.getText().toString());
            }
        });

        home_text = MyApplication.getRoom_list();
        count_position = countNumber;
        if (home_text != null && home_text.size() > 0) {
            room.setText(home_text.get(room_position));
        }
        count_text = new ArrayList<>();
        count_text.add(0, String.valueOf(0));
        count_text.add(1, String.valueOf(1));
        count_text.add(2, String.valueOf(2));
        count_text.add(3, String.valueOf(3));
        count_text.add(4, String.valueOf(4));
        count_text.add(5, String.valueOf(5));
        count_text.add(6, String.valueOf(6));
        count_text.add(7, String.valueOf(7));
        count_text.add(8, String.valueOf(8));
        count.setText(count_text.get(count_position));

        count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initGridView(input_position);
            }
        });

        key_style = new ArrayList<>();
        key_style.add(0, "复位");
        key_style.add(1, "非复位");
        if (style_position == 0 || style_position == 1) {
            style.setText(key_style.get(style_position));
        } else {
            style.setText("未知");
        }
        key_backLight = new ArrayList<>();
        key_backLight.add(0, "随灯状态变化");
        key_backLight.add(1, "常亮");
        key_backLight.add(2, "不亮");
        if (backLight_position == 0 || backLight_position == 1 || backLight_position == 2) {
            backLight.setText(key_backLight.get(backLight_position));
        } else {
            backLight.setText("未知");
        }
        initGridView(input_position);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.equip_input:
                initPopupWindow(equip_input, input_name, 0);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.style:
                initPopupWindow(style, key_style, 1);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.backLight:
                initPopupWindow(backLight, key_backLight, 2);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.room:
                initPopupWindow(room, home_text, 3);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.count:
                initPopupWindow(count, count_text, 4);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.save:
                if (MyApplication.getWareData().getKeyInputs() == null || MyApplication.getWareData().getKeyInputs().size() == 0) {
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
                        WareBoardKeyInput wareBoardKeyInput = null;
                        String data_str = "";
                        String div;
                        String more_data = "";
                        div = ",";
                        wareBoardKeyInput = MyApplication.getWareData().getKeyInputs().get(input_position);
                        //输入板名称
                        byte[] nameData = {0};
                        if ("".equals(name.getText().toString())) {
                            try {
                                nameData = wareBoardKeyInput.getBoardName().getBytes("GB2312");
                            } catch (Exception e) {
                            }
                        } else {
                            try {
                                nameData = name.getText().toString().getBytes("GB2312");
                            } catch (UnsupportedEncodingException e) {
                                ToastUtil.showToast(mActivity, "输入面板名称不合适");
                                return;
                            }
                        }
                        if (name.getText().toString().length() > 24) {
                            ToastUtil.showToast(mActivity, "输入面板名称不能过长");
                            return;
                        }
                        String str_gb = CommonUtils.bytesToHexString(nameData);

                        //房间名称
                        byte[] roomData = {0};
                        try {
                            roomData = home_text.get(room_position).getBytes("GB2312");
                        } catch (Exception e) {
                            return;
                        }
                        String str_gb_room = CommonUtils.bytesToHexString(roomData);

                        //按键名称
                        String name_rows = "";
                        for (int i = 0; i < countData; i++) {
                            if (i < countData - 1)
                                try {
                                    name_rows += "\"" + CommonUtils.bytesToHexString(listData.get(i).getBytes("GB2312")) + "\",";
                                } catch (Exception e) {
                                    System.out.println(e + "");
                                }
                            else
                                try {
                                    name_rows += "\"" + CommonUtils.bytesToHexString(listData.get(i).getBytes("GB2312")) + "\"";
                                } catch (Exception e) {
                                    System.out.println(e + "");
                                }
                        }
                        name_rows = "[" + name_rows + "]";


                        //KeyAllCtrlType
                        String key_rows = "";
                        for (int i = 0; i < wareBoardKeyInput.getKeyAllCtrlType().length; i++) {
                            if (i < wareBoardKeyInput.getKeyAllCtrlType().length - 1)
                                key_rows += wareBoardKeyInput.getKeyAllCtrlType()[i] + ",";
                            else key_rows += wareBoardKeyInput.getKeyAllCtrlType()[i] + "";
                        }
                        key_rows = "[" + key_rows + "]";


                        data_str = "{" +
                                "\"canCpuID\":\"" + wareBoardKeyInput.getCanCpuID() + "\"," +
                                "\"boardName\":\"" + str_gb + "\"," +
                                "\"boardType\":" + wareBoardKeyInput.getBoardType() + "," +
                                "\"keyCnt\":" + countData + "," +
                                "\"bResetKey\":" + style_position + "," +
                                "\"ledBkType\":" + backLight_position + "," +
                                "\"keyName_rows\":" + name_rows + "," +
                                "\"keyAllCtrlType_rows\":" + key_rows + "," +
                                "\"roomName\":\"" + str_gb_room
                                + "\"}" + div;
                        more_data += data_str;

                        try {
                            more_data = more_data.substring(0, more_data.lastIndexOf(","));
                        } catch (Exception e) {
                            System.out.println(e + "");
                        }
                        initDialog("正在保存...");
                        //这就是要上传的字符串:data_hoad
                        String data_hoad = "{" +
                                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                "\"datType\":9" + "," +
                                "\"subType1\":0" + "," +
                                "\"subType2\":1" + "," +
                                "\"keyinput\":" + wareBoardKeyInput.getKeyinput() + "," +
                                "\"keyinput_rows\":[" + more_data + "]}";
                        Log.e("情景模式测试:", data_hoad);
                        MyApplication.sendMsg(data_hoad);
                    }
                });
                builder.create().show();
                break;
        }
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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), view_parent.getWidth(), 150);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, mActivity);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view_p, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (tag == 0) {
                    input_position = position;
//                    initGridView(input_position);
                    initData();
                    tv.setText(text.get(position));
                } else if (tag == 1) {
                    style_position = position;
                } else if (tag == 2) {
                    backLight_position = position;
                } else if (tag == 3) {
                    room_position = position;
                } else if (tag == 4) {
                    count_position = position;
                    initGridView(input_position);
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
     * 初始化GridView
     *
     * @param input_position
     */
    private void initGridView(int input_position) {
        if (MyApplication.getWareData().getKeyInputs().size() == 0) {
            ToastUtil.showToast(getActivity(), "没有收到输入板信息");
            return;
        }
        keyName = MyApplication.getWareData().getKeyInputs().get(input_position).getKeyName();
        //按键数目
        try {
            countData = Byte.parseByte(count_text.get(count_position));
        } catch (Exception e) {
            return;
        }

        //按键名称集合
        listData = new ArrayList<>();

        if (countData > keyName.length) {
            for (int i = 0; i < countData; i++) {
                if (i >= keyName.length) {
                    listData.add("按键" + i);
                } else {
                    listData.add(keyName[i]);
                }
            }
        } else {
            for (int i = 0; i < countData; i++) {
                listData.add(keyName[i]);
            }
        }
        moduleEditAdapter1 = new ModuleEditAdapter(getActivity(), listData);
        gridView.setAdapter(moduleEditAdapter1);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
}
