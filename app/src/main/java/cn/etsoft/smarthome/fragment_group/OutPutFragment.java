package cn.etsoft.smarthome.fragment_group;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import cn.etsoft.smarthome.adapter.IClick_PZ;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.adapter.Swipe_CpnAdapter;
import cn.etsoft.smarthome.domain.PrintCmd;
import cn.etsoft.smarthome.domain.UpBoardKeyData;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.Iclick_Tag;
import cn.etsoft.smarthome.pullmi.entity.WareBoardKeyInput;
import cn.etsoft.smarthome.pullmi.entity.WareChnOpItem;
import cn.etsoft.smarthome.pullmi.utils.Sixteen2Two;
import cn.etsoft.smarthome.ui.GroupActivity;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SwipeListView;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class OutPutFragment extends Fragment implements View.OnClickListener {

    private TextView back, ref_equipment, del_equipment, save_equipment;
    private SwipeListView lv;
    private ImageView input_out_iv_nodata;
    private PopupWindow popupWindow;
    private int devtype;
    private int devid;
    private String uid;
    private GroupActivity groupActivity;
    private List<WareChnOpItem> ChnOpItem;
    private Swipe_CpnAdapter adapter = null;
    private List<String> Board_text;
    private EquipmentAdapter Equipadapter;
    private int POP_TYPE_DOWNUP = 1, POP_TYPE_STATE = 0, POP_TYPR_BOARD = 2;
    private int KEY_ACTION_DOWN = 0, KEY_ACTION_UP = 1;
    private int BOARD_UP = 15, BOARD_DEL = 16;
    private int DEL_ALL = 110;

    private List<PrintCmd> listData;
    private List<WareBoardKeyInput> list_board;

    private Dialog mDialog;
    private int del_Position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);
        groupActivity = MyApplication.mInstance.getGroupActivity();
        //初始化标题栏
        initTitleBar();
        initData();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 14) {
                    ChnOpItem.clear();
                    listData.clear();
                    initListView();
                }
                if (what == 15 && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                }
                if (del_Position != DEL_ALL && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                    listData.remove(del_Position);
                    del_Position = 0;
                    if (adapter != null)
                        adapter.notifyDataSetChanged(listData);
                    else {
                        adapter = new Swipe_CpnAdapter(getActivity(), listData,mListener);
                        lv.setAdapter(adapter);
                    }
                }
                if (del_Position == DEL_ALL && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                    listData.clear();
                    del_Position = 0;
                    if (adapter != null)
                        adapter.notifyDataSetChanged(listData);
                    else {
                        adapter = new Swipe_CpnAdapter(getActivity(), listData,mListener);
                        lv.setAdapter(adapter);
                    }
                }

                if (what == 14 || what == 15 || what == 16) {
                    if (mDialog != null)
                        mDialog.dismiss();
                }
            }
        });

        //初始化控件
        initView(view);
        //长按添加输出板数据
        groupActivity.setOnLongClick_outputData(new GroupActivity.OnLongClick_OutputData() {
            @Override
            public void AddDevs(PrintCmd item, int index) {
                try {
                    item.setListener(mListener);
                    boolean tag = true;
                    for (int i = 0; i < listData.size(); i++) {
                        if (listData.get(i).getDevUnitID().equals(item.getDevUnitID()) ? listData.get(i).getIndex() == index : false) {
                            tag = false;
                            Toast.makeText(getActivity(), "设备已存在！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (tag) {
                        listData.add(item);
                        if (adapter != null)
                            adapter.notifyDataSetChanged(listData);
                        else {
                            adapter = new Swipe_CpnAdapter(getActivity(), listData, mListener,input_out_iv_nodata);
                            lv.setAdapter(adapter);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
        return view;
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
                ToastUtil.showToast(getActivity(), "数据加载失败");
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);
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
     * 初始化标题栏
     */
    private void initTitleBar() {

        devtype = getArguments().getInt("devType", -1);
        devid = getArguments().getInt("devID", -1);
        uid = getArguments().getString("uid");
        MyApplication.getChnItemInfo(uid, devtype, devid);
        System.out.println("加载数据");
        Board_text = new ArrayList<>();
        list_board = MyApplication.getWareData().getKeyInputs();
        for (int i = 0; i < list_board.size(); i++) {
            Board_text.add(list_board.get(i).getBoardName());
        }
    }

    public void initData() {
        MyApplication.getChnItemInfo(uid, devtype, devid);
        ChnOpItem = new ArrayList<>();
        listData = new ArrayList<>();
        initDialog("正在加载...");
    }


    /**
     * 初始化控件
     */
    private void initView(View view) {
        input_out_iv_nodata = (ImageView) view.findViewById(R.id.input_out_iv_nodata);
        ref_equipment = (TextView) view.findViewById(R.id.ref_equipment);
        del_equipment = (TextView) view.findViewById(R.id.del_equipment);
        save_equipment = (TextView) view.findViewById(R.id.save_equipment);
        back = (TextView) view.findViewById(R.id.back);
        lv = (SwipeListView) view.findViewById(R.id.equipment_out_lv);
        ref_equipment.setOnClickListener(this);
        del_equipment.setOnClickListener(this);
        save_equipment.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    /**
     * 初始化listView
     */
    int WareData_size = 0;

    private void initListView() {

        if (MyApplication.getWareData().getChnOpItems() == null || MyApplication.getWareData().getChnOpItems().size() == 0) {
            input_out_iv_nodata.setVisibility(View.VISIBLE);
            if (adapter == null){
                adapter = new Swipe_CpnAdapter(getActivity(), listData,mListener);
                lv.setAdapter(adapter);
            }else {
                adapter.notifyDataSetChanged(listData);
            }
            return;
        }
        input_out_iv_nodata.setVisibility(View.GONE);
        for (int i = 0; i < MyApplication.getWareData().getChnOpItems().size(); i++) {
            ChnOpItem.add(MyApplication.getWareData().getChnOpItems().get(i));
        }

        for (int k = 0; k < ChnOpItem.size(); k++) {

            List<Integer> list_Key_down = Sixteen2Two.decimal2Binary(ChnOpItem.get(k).getKeyDownValid());
            List<Integer> list_Key_up = Sixteen2Two.decimal2Binary(ChnOpItem.get(k).getKeyUpValid());
            List<String> list_Name = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData().getKeyInputs().size(); i++) {
                if (ChnOpItem.get(k).getDevUnitID()
                        .equals(MyApplication.getWareData().getKeyInputs().get(i).getDevUnitID())) {
                    for (int j = 0; j < MyApplication.getWareData().getKeyInputs().get(i).getKeyName().length; j++) {
                        list_Name.add(MyApplication.getWareData().getKeyInputs().get(i).getKeyName()[j]);
                    }
                }
            }
            if (list_Key_down.size() > 0 && list_Key_up.size() == 0) {

                for (int i = 0; i < list_Key_down.size(); i++) {
                    PrintCmd cmd = new PrintCmd();
                    cmd.setDevUnitID(ChnOpItem.get(k).getDevUnitID());
                    cmd.setIndex(list_Key_down.get(i));
                    cmd.setDevType(devtype);
                    cmd.setKey_cmd(ChnOpItem.get(k).getKeyDownCmd()[list_Key_down.get(i)]);
                    cmd.setKeyAct_num(KEY_ACTION_DOWN);
                    if (list_Name.size() == 0)
                        cmd.setKeyname("未知按键");
                    else
                        cmd.setKeyname(list_Name.get(list_Key_down.get(i)));
                    cmd.setListener(mListener);
                    listData.add(cmd);
                }

            } else if (list_Key_up.size() > 0 && list_Key_down.size() > 0) {

                for (int i = 0; i < list_Key_down.size(); i++) {
                    PrintCmd cmd = new PrintCmd();
                    cmd.setDevUnitID(ChnOpItem.get(k).getDevUnitID());
                    cmd.setIndex(list_Key_down.get(i));
                    cmd.setDevType(devtype);
                    cmd.setKey_cmd(ChnOpItem.get(k).getKeyDownCmd()[list_Key_down.get(i)]);
                    cmd.setKeyAct_num(KEY_ACTION_DOWN);
                    if (list_Name.size() == 0)
                        cmd.setKeyname("未知按键");
                    else
                        cmd.setKeyname(list_Name.get(list_Key_down.get(i)));
                    cmd.setListener(mListener);
                    listData.add(cmd);
                }

                for (int i = 0; i < list_Key_up.size(); i++) {
                    PrintCmd cmd = new PrintCmd();
                    cmd.setDevUnitID(ChnOpItem.get(k).getDevUnitID());
                    cmd.setIndex(list_Key_up.get(i));
                    cmd.setDevType(devtype);
                    cmd.setKey_cmd(ChnOpItem.get(k).getKeyUpCmd()[list_Key_up.get(i)]);
                    cmd.setKeyAct_num(KEY_ACTION_UP);
                    if (list_Name.size() == 0)
                        cmd.setKeyname("未知按键");
                    else
                        cmd.setKeyname(list_Name.get(list_Key_up.get(i)));
                    cmd.setListener(mListener);
                    listData.add(cmd);
                }

            } else if (list_Key_up.size() > 0 && list_Key_down.size() == 0) {

                for (int i = 0; i < list_Key_up.size(); i++) {
                    PrintCmd cmd = new PrintCmd();
                    cmd.setDevUnitID(ChnOpItem.get(k).getDevUnitID());
                    cmd.setIndex(list_Key_up.get(i));
                    cmd.setDevType(devtype);
                    cmd.setKey_cmd(ChnOpItem.get(k).getKeyUpCmd()[list_Key_up.get(i)]);
                    cmd.setKeyAct_num(KEY_ACTION_UP);
                    if (list_Name.size() == 0)
                        cmd.setKeyname("未知按键");
                    else
                        cmd.setKeyname(list_Name.get(list_Key_up.get(i)));
                    cmd.setListener(mListener);
                    listData.add(cmd);
                }
            }
        }
        if (adapter == null){
            adapter = new Swipe_CpnAdapter(getActivity(), listData,mListener);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged(listData);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.ref_equipment:
                //刷新
                input_out_iv_nodata.setVisibility(View.GONE);
                if (listData != null) {
                    listData.clear();
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                    else {
                        adapter = new Swipe_CpnAdapter(getActivity(), listData,mListener);
                        lv.setAdapter(adapter);
                    }
                }
                initData();
                break;
            case R.id.del_equipment:
                //删除
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(getActivity());
                builder.setTitle("提示 :");
                builder.setMessage("您确定要删除所有设备吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listData.size() == 0) {
                            dialog.dismiss();
                            return;
                        }

                        for (int i = 0; i < listData.size(); i++) {
                            if (listData.get(i).getKeyAct_num() == 2 || listData.get(i).getKey_cmd() == 0) {
                                Toast.makeText(getActivity(), "存在未设置，请设置完", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
///*{
//                        "chn_opitem_rows":	[{
//                        "key_cpuCanID":	"50ff6c067184515640421267",
//                                "keyDownValid":	0,
//                                "keyUpValid":	49,
//                                "keyUpCmd":	[3, 0, 0, 0, 3, 1],
//                        "keyDownCmd":	[0, 0, 0, 0, 0, 0]
//                    }],
//                        "devUnitID":	"37ffdb05424e323416702443",
//                            "datType":	14,
//                            "subType1":	1,
//                            "subType2":	1,
//                            "chn_opitem":	1
//                    }*/
                        //根据以上注释掉的数据结构，将已有数据已此格式寄存；
                        byte[] Valid_down_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键的相应位置；
                        byte[] Valid_up_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应位置；
                        byte[] Cmd_down_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键相应的命令
                        byte[] Cmd_up_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应的命令；
                        String key_cpuCanID_del = "";//按键板的id；
                        UpBoardKeyData data_del = new UpBoardKeyData();//上传数据实体；
                        List<UpBoardKeyData.ChnOpitemRowsBean> bean_list_del = new ArrayList<>();//按键板实体集合；

                        for (int i = 0; i < listData.size(); i++) {//循环所有数据；

                            List<PrintCmd> listData_id = new ArrayList<>();//实例化一个新的数据集合，存放相同按键板的按键；
                            listData_id.add(listData.get(0));//添加一个按键实体；
                            listData.remove(0);//在所有数据中移除此实体；
                            for (int k = 0; k < listData.size(); k++) {//循环所有数据；
                                for (int m = listData.size() - 1; m >= 0; m--) {
                                    if (listData_id.get(0).getDevUnitID().equals(listData.get(m).getDevUnitID())) {
                                        listData_id.add(listData.get(m));//将所有数据中和第一个添加的按键作比较，相同的话加入新的实体集合，
//                                        listData.remove(m);//在所有数据集合中移除；
                                    }
                                }
                            }
                            UpBoardKeyData.ChnOpitemRowsBean bean = data_del.new ChnOpitemRowsBean();//按键实体

                            for (int j = 0; j < listData_id.size(); j++) {//循环新的/相同的按键板集合；

                                key_cpuCanID_del = listData_id.get(j).getDevUnitID();//为按键板id赋值；

                                //根据按键动作判断，按下和弹起；
                                if (listData_id.get(j).getKeyAct_num() == KEY_ACTION_DOWN) {
                                    //如果是按下，将按下数组中的0位变为1；
                                    Valid_down_del[listData_id.get(j).getIndex()] = 1;
                                    //根据按下位置，将按下命令数组中的相应的0，改为响应命令；
                                    Cmd_down_del[listData_id.get(j).getIndex()] = (byte) listData_id.get(j).getKey_cmd();
                                } else {
                                    Valid_up_del[listData_id.get(j).getIndex()] = 1;
                                    Cmd_up_del[listData_id.get(j).getIndex()] = (byte) listData_id.get(j).getKey_cmd();
                                }
                            }

                            //因为数据传递时，高位、低位和现实中相反，so循环赋值；
                            String down_v = "";
                            for (int j = 0; j < Valid_down_del.length; j++) {
                                down_v += Valid_down_del[Valid_down_del.length - 1 - j];
                            }
                            //将改好的2#字符串转成10#；
                            BigInteger bi_down = new BigInteger(down_v, 2);  //转换成BigInteger类型
                            int v_down = Integer.parseInt(bi_down.toString(10)); //参数2指定的是转化成X进制，默认10进制


                            String up_v = "";
                            for (int j = 0; j < Valid_up_del.length; j++) {
                                up_v += Valid_up_del[Valid_down_del.length - 1 - j];
                            }
                            BigInteger bi_up = new BigInteger(up_v, 2);  //转换成BigInteger类型
                            int v_up = Integer.parseInt(bi_up.toString(10)); //参数2指定的是转化成X进制，默认10进制

                            //将相应的数据加入到上传按键实体中；
                            bean.setKey_cpuCanID(key_cpuCanID_del);
                            bean.setKeyDownValid(v_down);
                            bean.setKeyUpValid(v_up);
                            bean.setKeyDownCmd(Cmd_down_del);
                            bean.setKeyUpCmd(Cmd_up_del);
                            //将每个上传按键实体加入实体集合中；
                            bean_list_del.add(bean);

                        }
                        //将以上数据加入到上传实体中；
                        data_del.setDevUnitID(GlobalVars.getDevid());
                        data_del.setChn_opitem_rows(bean_list_del);
                        data_del.setDatType(BOARD_DEL);
                        data_del.setSubType1(0);
                        data_del.setSubType2(0);
                        data_del.setDevType(devtype);
                        data_del.setDevID(devid);
                        data_del.setOut_cpuCanID(uid);
                        data_del.setChn_opitem(bean_list_del.size());
                        dialog.dismiss();
                        Gson gson_del = new Gson();
                        System.out.println(gson_del.toJson(data_del));
                        MyApplication.sendMsg(gson_del.toJson(data_del).toString());
                        del_Position = DEL_ALL;
                        initDialog("正在删除...");
                    }
                });
                builder.create().show();
                break;
            case R.id.save_equipment:
                //保存

                //判断设备中有没有未设置的，有终止保存。无，继续保存。
                if (listData.size() == 0)
                    return;
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getKeyAct_num() == 2 || listData.get(i).getKey_cmd() == 0) {
                        Toast.makeText(getActivity(), "存在未设置，请设置完", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //根据以上注释掉的数据结构，将已有数据已此格式寄存；

                String key_cpuCanID = "";//按键板的id；
                UpBoardKeyData data = new UpBoardKeyData();//上传数据实体；
                List<UpBoardKeyData.ChnOpitemRowsBean> bean_list = new ArrayList<>();//按键板实体集合；

                do {
                    List<PrintCmd> listData_id = new ArrayList<>();//实例化一个新的数据集合，存放相同按键板的按键；
                    listData_id.add(listData.get(0));//添加一个按键实体；
                    listData.remove(0);//在所有数据中移除此实体；
                    for (int m = listData.size() - 1; m >= 0; m--) {
                        if (listData_id.get(0).getDevUnitID().equals(listData.get(m).getDevUnitID())) {
                            listData_id.add(listData.get(m));//将所有数据中和第一个添加的按键作比较，相同的话加入新的实体集合，
//                            listData.remove(m);//在所有数据集合中移除；
                        }
                    }
                    UpBoardKeyData.ChnOpitemRowsBean bean = data.new ChnOpitemRowsBean();//按键实体
                    byte[] Valid_down = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键的相应位置；
                    byte[] Valid_up = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应位置；
                    byte[] Cmd_down = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键相应的命令
                    byte[] Cmd_up = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应的命令；
                    for (int j = 0; j < listData_id.size(); j++) {//循环新的/相同的按键板集合；

                        key_cpuCanID = listData_id.get(j).getDevUnitID();//为按键板id赋值；

                        //根据按键动作判断，按下和弹起；
                        if (listData_id.get(j).getKeyAct_num() == KEY_ACTION_DOWN) {
                            //如果是按下，将按下数组中的0位变为1；
                            Valid_down[listData_id.get(j).getIndex()] = 1;
                            //根据按下位置，将按下命令数组中的相应的0，改为响应命令；
                            Cmd_down[listData_id.get(j).getIndex()] = (byte) listData_id.get(j).getKey_cmd();
                        } else {
                            Valid_up[listData_id.get(j).getIndex()] = 1;
                            Cmd_up[listData_id.get(j).getIndex()] = (byte) listData_id.get(j).getKey_cmd();
                        }

                    }

                    //因为数据传递时，高位、低位和现实中相反，so循环赋值；
                    String down_v = "";
                    for (int j = 0; j < Valid_down.length; j++) {
                        down_v += Valid_down[Valid_down.length - 1 - j];
                    }
                    //将改好的2#字符串转成10#；
                    BigInteger bi_down = new BigInteger(down_v, 2);  //转换成BigInteger类型
                    int v_down = Integer.parseInt(bi_down.toString(10)); //参数2指定的是转化成X进制，默认10进制


                    String up_v = "";
                    for (int j = 0; j < Valid_up.length; j++) {
                        up_v += Valid_up[Valid_down.length - 1 - j];
                    }
                    BigInteger bi_up = new BigInteger(up_v, 2);  //转换成BigInteger类型
                    int v_up = Integer.parseInt(bi_up.toString(10)); //参数2指定的是转化成X进制，默认10进制

                    //将相应的数据加入到上传按键实体中；
                    bean.setKey_cpuCanID(key_cpuCanID);
                    bean.setKeyDownValid(v_down);
                    bean.setKeyUpValid(v_up);
                    bean.setKeyDownCmd(Cmd_down);
                    bean.setKeyUpCmd(Cmd_up);
                    //将每个上传按键实体加入实体集合中；
                    bean_list.add(bean);

                } while (listData.size() > 0);
                //将以上数据加入到上传实体中；
                data.setDevUnitID(GlobalVars.getDevid());
                data.setChn_opitem_rows(bean_list);
                data.setDatType(BOARD_UP);
                data.setSubType1(0);
                data.setSubType2(0);
                data.setDevType(devtype);
                data.setDevID(devid);
                data.setOut_cpuCanID(uid);
                data.setChn_opitem(bean_list.size());

                Gson gson = new Gson();
                System.out.println(gson.toJson(data));
                MyApplication.sendMsg(gson.toJson(data).toString());
                initDialog("正在保存...");
                break;
            case R.id.tv_equipment_parlour:
                //添加设备页的弹出框
                int widthOff = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(v, -1, Board_text, POP_TYPR_BOARD);
                    popupWindow.showAsDropDown(v, -widthOff, 0);
                }
                break;
        }
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */

    private void initPopupWindow(final View view_parent, final int parent_position, final List<String> text, final int type) {
        //获取自定义布局文件pop.xml的视图
        final View customView = getActivity().getLayoutInflater().from(getActivity()).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);

        // 创建PopupWindow实例

        if (type == 1)
            popupWindow = new PopupWindow(getActivity().findViewById(R.id.popupWindow_equipment_sv), 235, 160);
        else if (type == 0)
            popupWindow = new PopupWindow(getActivity().findViewById(R.id.popupWindow_equipment_sv), 235, 300);
        else
            popupWindow = new PopupWindow(getActivity().findViewById(R.id.popupWindow_equipment_sv), 200, 250);

        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, getActivity());
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (type == POP_TYPE_STATE)
                    listData.get(parent_position).setKey_cmd((byte) position);
                else if (type == POP_TYPE_DOWNUP)
                    listData.get(parent_position).setKeyAct_num((byte) position);

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

    /**
     * 实现类，响应按钮点击事件
     */
    private IClick_PZ mListener = new IClick_PZ() {
        @Override
        public void listViewItemClick(final int position, View v) {
            int widthOff = getActivity().getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
            final List<String> list_text = new ArrayList<>();
            Iclick_Tag tag = (Iclick_Tag) v.getTag();
            System.out.println("设备类型:" + tag.getType());
            for (int i = 0; i < tag.getText().length; i++) {
                list_text.add(tag.getText()[i]);
            }
            switch (v.getId()) {
                case R.id.deploy_choose:
                    if (tag.getType() == 0) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        } else {
                            initPopupWindow(v, position, list_text, POP_TYPE_STATE);
                            popupWindow.showAsDropDown(v, -widthOff, 0);
                        }
                    } else if (tag.getType() == 3) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        } else {
                            initPopupWindow(v, position, list_text, POP_TYPE_STATE);
                            popupWindow.showAsDropDown(v, -widthOff, 0);
                        }
                    } else if (tag.getType() == 4) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            popupWindow = null;
                        } else {
                            initPopupWindow(v, position, list_text, POP_TYPE_STATE);
                            popupWindow.showAsDropDown(v, -widthOff, 0);
                        }
                    }

                    break;
                case R.id.deploy_choose1:
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add("按下");
                        list.add("弹起");
                        list.add("未设置");
                        initPopupWindow(v, position, list, POP_TYPE_DOWNUP);
                        popupWindow.showAsDropDown(v, -widthOff, 0);
                    }
                    break;
                case R.id.deploy_delete:
//                {
//                    "devUnitID": "37ffdb05424e323416702443",
//                        "datType": 13,
//                        "subType1": 0,
//                        "subType2": 0,
//                        "key_cpuCanID": "50ff6c067184515640421267",
//                        "key_index": 0,
//                        "key_opitem": 3,
//                        "key_opitem_rows": [
//                    {
//                        "out_cpuCanID": "31ffdf054257313827502543",
//                            "devType": 3,
//                            "devID": 5,
//                            "keyOpCmd": 3,
//                            "keyOp": 1
//                    },
//                    {
//                        "out_cpuCanID": "31ffdf054257313827502543",
//                            "devType": 3,
//                            "devID": 6,
//                            "keyOpCmd": 2,
//                            "keyOp": 1
//                    },
//                    {
//                        "out_cpuCanID": "31ffdf054257313827502543",
//                            "devType": 3,
//                            "devID": 7,
//                            "keyOpCmd": 2,
//                            "keyOp": 1
//                    }
//                    ]
//                }
//                返回：
//                {
//                    "devUnitID": "37ffdb05424e323416702443",
//                        "datType": 13,
//                        "subType1": 1,
//                        "subType2": 0,
//                        "canCpuID": "50ff6c067184515640421267",
//                        "result": 1
//                }
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(getActivity());
                    builder.setTitle("提示 :");
                    builder.setMessage("您确定要删除此条目吗？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//根据以上注释掉的数据结构，将已有数据已此格式寄存；

                            byte[] Valid_down_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键的相应位置；
                            byte[] Valid_up_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应位置；
                            byte[] Cmd_down_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放按下键相应的命令
                            byte[] Cmd_up_del = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};//存放弹起键相应的命令；
                            String key_cpuCanID_del = "";//按键板的id；
                            UpBoardKeyData data_del = new UpBoardKeyData();//上传数据实体；
                            List<UpBoardKeyData.ChnOpitemRowsBean> bean_list_del = new ArrayList<>();//按键板实体集合；


                            List<PrintCmd> listData_id = new ArrayList<>();//实例化一个新的数据集合，存放相同按键板的按键；
                            listData_id.add(listData.get(position));//添加一个按键实体；
//                            listData.remove(0);//在所有数据中移除此实体；
                            for (int k = 0; k < listData.size(); k++) {//循环所有数据；
                                for (int m = listData.size() - 1; m >= 0; m--) {
                                    if (listData_id.get(0).getDevUnitID().equals(listData.get(m).getDevUnitID())) {
                                        listData_id.add(listData.get(m));//将所有数据中和第一个添加的按键作比较，相同的话加入新的实体集合，
//                                        listData.remove(m);//在所有数据集合中移除；
                                    }
                                }
                            }
                            UpBoardKeyData.ChnOpitemRowsBean bean = data_del.new ChnOpitemRowsBean();//按键实体

                            for (int j = 0; j < listData_id.size(); j++) {//循环新的/相同的按键板集合；

                                key_cpuCanID_del = listData_id.get(j).getDevUnitID();//为按键板id赋值；

                                //根据按键动作判断，按下和弹起；
                                if (listData_id.get(j).getKeyAct_num() == KEY_ACTION_DOWN) {
                                    //如果是按下，将按下数组中的0位变为1；
                                    Valid_down_del[listData_id.get(j).getIndex()] = 1;
                                    //根据按下位置，将按下命令数组中的相应的0，改为响应命令；
                                    Cmd_down_del[listData_id.get(j).getIndex()] = (byte) listData_id.get(j).getKey_cmd();
                                } else {
                                    Valid_up_del[listData_id.get(j).getIndex()] = 1;
                                    Cmd_up_del[listData_id.get(j).getIndex()] = (byte) listData_id.get(j).getKey_cmd();
                                }
                            }

                            //因为数据传递时，高位、低位和现实中相反，so循环赋值；
                            String down_v = "";
                            for (int j = 0; j < Valid_down_del.length; j++) {
                                down_v += Valid_down_del[Valid_down_del.length - 1 - j];
                            }
                            //将改好的2#字符串转成10#；
                            BigInteger bi_down = new BigInteger(down_v, 2);  //转换成BigInteger类型
                            int v_down = Integer.parseInt(bi_down.toString(10)); //参数2指定的是转化成X进制，默认10进制


                            String up_v = "";
                            for (int j = 0; j < Valid_up_del.length; j++) {
                                up_v += Valid_up_del[Valid_down_del.length - 1 - j];
                            }
                            BigInteger bi_up = new BigInteger(up_v, 2);  //转换成BigInteger类型
                            int v_up = Integer.parseInt(bi_up.toString(10)); //参数2指定的是转化成X进制，默认10进制

                            //将相应的数据加入到上传按键实体中；
                            bean.setKey_cpuCanID(key_cpuCanID_del);
                            bean.setKeyDownValid(v_down);
                            bean.setKeyUpValid(v_up);
                            bean.setKeyDownCmd(Cmd_down_del);
                            bean.setKeyUpCmd(Cmd_up_del);
                            //将每个上传按键实体加入实体集合中；
                            bean_list_del.add(bean);


                            //将以上数据加入到上传实体中；
                            data_del.setDevUnitID(GlobalVars.getDevid());
                            data_del.setChn_opitem_rows(bean_list_del);
                            data_del.setDatType(BOARD_DEL);
                            data_del.setSubType1(0);
                            data_del.setSubType2(0);
                            data_del.setDevType(devtype);
                            data_del.setDevID(devid);
                            data_del.setOut_cpuCanID(uid);
                            data_del.setChn_opitem(bean_list_del.size());
                            dialog.dismiss();
                            Gson gson_del = new Gson();
                            System.out.println(gson_del.toJson(data_del));
                            MyApplication.sendMsg(gson_del.toJson(data_del).toString());
                            del_Position = position;
                            initDialog("正在删除...");
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };

    /**
     * 设备适配器；
     */

    private int[] image = new int[]{R.drawable.kongtiao, R.drawable.tv, R.drawable.jidinghe, R.drawable.dengguang, R.drawable.chuanglian};

    class EquipmentAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private String[] key_name;

        public EquipmentAdapter(String[] key_name, Context context) {
            super();
            this.key_name = key_name;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (null != key_name)
                return key_name.length;
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            return key_name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.equipment_listview_item, null);
                viewHolder = new ViewHolder();

                viewHolder.title = (TextView) convertView.findViewById(R.id.equipment_tv);
//                viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);

                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.title.setText(key_name[position]);

//            if (listViewItems.get(position).getType() == 0)
//                viewHolder.image.setImageResource(image[0]);
//            else if (listViewItems.get(position).getType() == 1)
//                viewHolder.image.setImageResource(image[1]);
//            else if (listViewItems.get(position).getType() == 2)
//                viewHolder.image.setImageResource(image[2]);
//            else if (listViewItems.get(position).getType() == 3)
//                viewHolder.image.setImageResource(image[3]);
//            else
//                viewHolder.image.setImageResource(image[4]);

            return convertView;
        }

        public class ViewHolder {
            public TextView title;
//            public ImageView image;
        }
    }
}
