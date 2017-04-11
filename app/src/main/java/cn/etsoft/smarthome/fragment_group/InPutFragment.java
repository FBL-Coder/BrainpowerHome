package cn.etsoft.smarthome.fragment_group;

import android.app.Dialog;
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
import cn.etsoft.smarthome.adapter.IClick_PZ;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.adapter.SwipeAdapter;
import cn.etsoft.smarthome.domain.Save_Quipment;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.Iclick_Tag;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.ui.GroupActivity;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SwipeListView;

/**
 * Created by Say GoBay on 2016/8/25.
 */
public class InPutFragment extends Fragment implements View.OnClickListener {
    private TextView back, ref_equipment, del_equipment, save_equipment;
    private ImageView input_out_iv_nodata;
    private SwipeListView lv;
    private GroupActivity groupActivity;
    private PopupWindow popupWindow;
    private int index;
    private String uid;
    private List<WareKeyOpItem> keyOpItems;
    private SwipeAdapter adapter = null;
    private List<WareDev> dev;
    private List<String> home_text;
    private List<WareDev> mWareDev_room;
    private List<WareDev> mWareDev;
    private int DATTYPE_SET = 12, DATTYPE_DEL = 13, POP_TYPE_DOWNUP = 1, POP_TYPE_STATE = 0, DEL_ALL = 110;
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
                if (what == 11 || what == 12 || what == 13) {
                    if (mDialog != null)
                        mDialog.dismiss();
                }
                if (what == 11) {
                    keyOpItems.clear();
                    initListView();
                }
                if (what == 12 && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);

                }
                if (del_Position != DEL_ALL && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                    keyOpItems.remove(del_Position);
                    del_Position = 0;
                    if (adapter != null)
                        adapter.notifyDataSetChanged(keyOpItems);
                    else {
                        adapter = new SwipeAdapter(getActivity(), keyOpItems, mListener);
                        lv.setAdapter(adapter);
                    }
                }
                if (del_Position == DEL_ALL && MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                    del_Position = 0;
                    keyOpItems.clear();
                    if (adapter != null)
                        adapter.notifyDataSetChanged(keyOpItems);
                    else {
                        adapter = new SwipeAdapter(getActivity(), keyOpItems, mListener, input_out_iv_nodata);
                        lv.setAdapter(adapter);
                    }
                }
            }
        });
        //初始化控件
        initView(view);
        //长按添加输入板数据
        groupActivity.setOnLongClick_inputData(new GroupActivity.OnLongClick_inputData() {

            @Override
            public void AddInput(WareKeyOpItem item) {
                try {
                    boolean tag = true;
                    for (int i = 0; i < keyOpItems.size(); i++) {
                        if (keyOpItems.get(i).getDevType() == item.getDevType()
                                && keyOpItems.get(i).getDevId() == item.getDevId()
                                && keyOpItems.get(i).getDevUnitID().equals(item.getDevUnitID())) {
                            tag = false;
                            Toast.makeText(getActivity(), "设备已存在！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (tag) {
                        keyOpItems.add(item);
                        if (adapter != null)
                            adapter.notifyDataSetChanged(keyOpItems);
                        else {
                            adapter = new SwipeAdapter(getActivity(), keyOpItems, mListener, input_out_iv_nodata);
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

        index = getArguments().getInt("key_index");
        uid = getArguments().getString("uid");
        System.out.println("加载数据");
        dev = new ArrayList<>();
        home_text = new ArrayList<>();

        mWareDev_room = new ArrayList<>();
        mWareDev = MyApplication.getWareData().getDevs();

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

        for (int i = 0; i < mWareDev.size(); i++) {
            if (mWareDev.get(i).getRoomName().equals(home_text.get(0)))
                dev.add(mWareDev.get(i));
        }
    }


    public void initData() {
        keyOpItems = new ArrayList<>();
        MyApplication.getKeyItemInfo(index, uid);
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
    private void initListView() {
        if (MyApplication.getWareData().getKeyOpItems() == null || MyApplication.getWareData().getKeyOpItems().size() == 0) {
            input_out_iv_nodata.setVisibility(View.VISIBLE);
            if (adapter != null)
                adapter.notifyDataSetChanged(keyOpItems);
            else {
                adapter = new SwipeAdapter(getActivity(), keyOpItems, mListener);
                lv.setAdapter(adapter);
            }
            return;
        }
        input_out_iv_nodata.setVisibility(View.GONE);
        for (int i = 0; i < MyApplication.getWareData().getKeyOpItems().size(); i++) {
            keyOpItems.add(MyApplication.getWareData().getKeyOpItems().get(i));
        }
        if (adapter != null)
            adapter.notifyDataSetChanged(keyOpItems);
        else {
            adapter = new SwipeAdapter(getActivity(), keyOpItems, mListener);
            lv.setAdapter(adapter);
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
        else
            popupWindow = new PopupWindow(getActivity().findViewById(R.id.popupWindow_equipment_sv), 235, 300);
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
                    keyOpItems.get(parent_position).setKeyOpCmd((byte) position);
                else
                    keyOpItems.get(parent_position).setKeyOp((byte) position);
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
            List<String> list_text = new ArrayList<>();
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
                            del_Position = position;
                            Save_Quipment save_quipment = new Save_Quipment();
                            List<Save_Quipment.key_Opitem_Rows> list_kor = new ArrayList<>();
                            Save_Quipment.key_Opitem_Rows key_opitem_rows = save_quipment.new key_Opitem_Rows();
                            key_opitem_rows.setOut_cpuCanID(keyOpItems.get(position).getDevUnitID());
                            key_opitem_rows.setDevID(keyOpItems.get(position).getDevId());
                            key_opitem_rows.setDevType(keyOpItems.get(position).getDevType());
                            key_opitem_rows.setKeyOp(keyOpItems.get(position).getKeyOp());
                            key_opitem_rows.setKeyOpCmd(keyOpItems.get(position).getKeyOpCmd());
                            list_kor.add(key_opitem_rows);
                            save_quipment.setDevUnitID(GlobalVars.getDevid());
                            save_quipment.setDatType(DATTYPE_DEL);
                            save_quipment.setKey_cpuCanID(uid);
                            save_quipment.setKey_opitem(keyOpItems.size());
                            save_quipment.setKey_index(index);
                            save_quipment.setSubType1(0);
                            save_quipment.setSubType2(0);
                            save_quipment.setKey_opitem_rows(list_kor);
                            dialog.dismiss();
                            Gson gson = new Gson();
                            System.out.println(gson.toJson(save_quipment));
                            MyApplication.sendMsg(gson.toJson(save_quipment).toString());
                            initDialog("正在删除...");
                        }
                    });
                    builder.create().show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ref_equipment:
                //刷新
                input_out_iv_nodata.setVisibility(View.GONE);
                initData();

                break;
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.del_equipment:
                //删除所有设备
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
                        del_Position = DEL_ALL;
                        if (keyOpItems.size() == 0) {
                            dialog.dismiss();
                            return;
                        }

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
                        save_quipment.setDatType(DATTYPE_DEL);
                        save_quipment.setKey_cpuCanID(uid);
                        save_quipment.setKey_opitem(keyOpItems.size());
                        save_quipment.setKey_index(index);
                        save_quipment.setSubType1(0);
                        save_quipment.setSubType2(0);
                        save_quipment.setKey_opitem_rows(list_kor);
                        dialog.dismiss();
                        Gson gson = new Gson();
                        System.out.println(gson.toJson(save_quipment));
                        MyApplication.sendMsg(gson.toJson(save_quipment).toString());
                        initDialog("正在删除...");
                    }
                });
                builder.create().show();
                break;
            case R.id.save_equipment:
                if (keyOpItems.size() == 0)
                    return;
                //保存
                for (int i = 0; i < keyOpItems.size(); i++) {
                    if (keyOpItems.get(i).getKeyOp() == 2 || keyOpItems.get(i).getKeyOpCmd() == 0) {
                        Toast.makeText(getActivity(), "存在未设置，请设置完", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

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
                initDialog("正在保存...");
                break;
        }
    }
}
