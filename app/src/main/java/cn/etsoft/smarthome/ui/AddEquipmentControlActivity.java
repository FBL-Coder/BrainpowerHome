package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.IClick_PZ;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.adapter.SwipeAdapter;
import cn.etsoft.smarthome.domain.Save_Quipment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.pullmi.entity.Iclick_Tag;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SwipeListView;

/**
 * Created by Say GoBay on 2016/8/29.
 */
public class AddEquipmentControlActivity extends Activity implements View.OnClickListener {
    private TextView mTitle, equipment_close, tv_equipment_parlour, ref_equipment, del_equipment, save_equipment;
    private ImageView back;
    private RelativeLayout add_equipment_btn;
    private SwipeListView lv;
    private ListView add_equipment_Layout_lv;
    private PopupWindow popupWindow;
    private LinearLayout add_equipment_Layout_ll;
    private int index;
    private String uid;
    private List<WareKeyOpItem> keyOpItems;
    private SwipeAdapter adapter = null;
    private List<WareDev> dev;
    private List<String> home_text;
    private List<WareDev> mWareDev_room;
    private List<WareDev> mWareDev;
    private EquipmentAdapter Equipadapter;
    private int DATTYPE_SET = 12,DATTYPE_DEL = 13, POP_TYPE_DOWNUP = 1, POP_TYPE_STATE = 0, POP_TYPR_ROOM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipmentdeploy_listview);
        //初始化标题栏
        initTitleBar();
        //初始化控件
        initView();
//        //初始化listView
//        initListView();


        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 11) {
                    keyOpItems = null;
                    initListView();
                }
                if (MyApplication.getWareData().getResult() != null
                        && MyApplication.getWareData().getResult().getResult() == 1) {
                    Toast.makeText(AddEquipmentControlActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    MyApplication.getWareData().setResult(null);
                }
                super.handleMessage(msg);
            }
        };
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                Message message = mHandler.obtainMessage(what);
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        mTitle = (TextView) findViewById(R.id.tv_home);
        mTitle.setText(getIntent().getStringExtra("title"));

        index = getIntent().getExtras().getInt("key_index");
        uid = getIntent().getExtras().getString("uid");

        MyApplication.getKeyItemInfo(index, uid);
        System.out.println("加载数据");
        dev = new ArrayList<>();
        home_text = new ArrayList<>();
        keyOpItems = new ArrayList<>();
        mWareDev_room = new ArrayList<>();
        mWareDev = MyApplication.getWareData().getDevs();

        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            mWareDev_room.add(MyApplication.getWareData().getDevs().get(i));
        }

        for (int i = 0; i < mWareDev_room.size() - 1; i++) {
            for (int j = mWareDev_room.size() - 1; j > i; j--) {
                if (mWareDev_room.get(i).getRoomName().equals(mWareDev_room.get(j).getRoomName())
                        || !(mWareDev_room.get(i).getCanCpuId()).equals(MyApplication.getWareData().getBoardChnouts().get(j).getDevUnitID())) {
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

    /**
     * 初始化控件
     */
    private void initView() {
        add_equipment_btn = (RelativeLayout) findViewById(R.id.equipment_out_rl);
        equipment_close = (TextView) findViewById(R.id.equipment_close);
        tv_equipment_parlour = (TextView) findViewById(R.id.tv_equipment_parlour);
        ref_equipment = (TextView) findViewById(R.id.ref_equipment);
        del_equipment = (TextView) findViewById(R.id.del_equipment);
        save_equipment = (TextView) findViewById(R.id.save_equipment);

        add_equipment_Layout_ll = (LinearLayout) findViewById(R.id.add_equipment_Layout_ll);
        add_equipment_Layout_lv = (ListView) findViewById(R.id.equipment_loyout_lv);
        lv = (SwipeListView) findViewById(R.id.equipment_out_lv);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        ref_equipment.setOnClickListener(this);
        del_equipment.setOnClickListener(this);
        save_equipment.setOnClickListener(this);
        add_equipment_btn.setOnClickListener(this);
        equipment_close.setOnClickListener(this);
        tv_equipment_parlour.setOnClickListener(this);

        tv_equipment_parlour.setText(home_text.get(0));
    }

    /**
     * 初始化listView
     */
    private void initListView() {

        keyOpItems = MyApplication.getWareData().getKeyOpItems();

        if (adapter != null)
            adapter.notifyDataSetChanged();
        else {
            adapter = new SwipeAdapter(this, keyOpItems, mListener);
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.equipment_out_rl:
                //添加页面的item点击，以及listview的初始化

                Equipadapter = new EquipmentAdapter(dev, this);
                add_equipment_Layout_lv.setAdapter(Equipadapter);

                add_equipment_Layout_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        WareKeyOpItem item = new WareKeyOpItem();
                        item.setDevId((byte) dev.get(position).getDevId());
                        item.setDevType((byte) dev.get(position).getType());
                        item.setDevUnitID(dev.get(position).getCanCpuId());
                        item.setKeyOpCmd((byte) 0);
                        item.setKeyOp((byte) 3);

                        boolean tag = true;
                        for (int i = 0; i < keyOpItems.size(); i++) {
                            if (keyOpItems.get(i).getDevType() == item.getDevType()
                                    && keyOpItems.get(i).getDevId() == item.getDevId()
                                    && keyOpItems.get(i).getDevUnitID().equals(item.getDevUnitID())) {
                                tag = false;
                                Toast.makeText(AddEquipmentControlActivity.this, "设备已存在！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        if (tag) {
                            keyOpItems.add(item);
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                            else {
                                adapter = new SwipeAdapter(AddEquipmentControlActivity.this,keyOpItems,mListener );
                                lv.setAdapter(Equipadapter);
                            }
                        }
                    }
                });

                add_equipment_Layout_ll.setVisibility(View.VISIBLE);
                add_equipment_btn.setVisibility(View.GONE);
                break;
            case R.id.equipment_close:
                //添加页面的关闭按钮
                add_equipment_Layout_ll.setVisibility(View.GONE);
                add_equipment_btn.setVisibility(View.VISIBLE);
                break;
            case R.id.ref_equipment:
                //刷新
                initTitleBar();
                break;
            case R.id.del_equipment:
                //删除所有设备
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(AddEquipmentControlActivity.this);
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

                        save_quipment.setDevUnitID(MyApplication.getWareData().getRcuInfos().get(0).getDevUnitID());
                        save_quipment.setDatType(DATTYPE_DEL);
                        save_quipment.setKey_cpuCanID(uid);
                        save_quipment.setKey_opitem(keyOpItems.size());
                        save_quipment.setKey_index(0);
                        save_quipment.setSubType1(0);
                        save_quipment.setSubType2(0);
                        save_quipment.setKey_opitem_rows(list_kor);

                        Gson gson = new Gson();
                        System.out.println(gson.toJson(save_quipment));
                        MyApplication.sendMsg(gson.toJson(save_quipment).toString());

                        keyOpItems.clear();
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        else {
                            adapter = new SwipeAdapter(AddEquipmentControlActivity.this, keyOpItems, mListener);
                            lv.setAdapter(adapter);
                        }

                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.save_equipment:
                //保存
                for (int i = 0; i < keyOpItems.size(); i++) {
                    if (keyOpItems.get(i).getKeyOp() == 2 || keyOpItems.get(i).getKeyOpCmd() == 0) {
                        Toast.makeText(AddEquipmentControlActivity.this, "存在未设置，请设置完", Toast.LENGTH_SHORT).show();
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

                save_quipment.setDevUnitID(MyApplication.getWareData().getRcuInfos().get(0).getDevUnitID());
                save_quipment.setDatType(DATTYPE_SET);
                save_quipment.setKey_cpuCanID(uid);
                save_quipment.setKey_opitem(keyOpItems.size());
                save_quipment.setKey_index(0);
                save_quipment.setSubType1(0);
                save_quipment.setSubType2(0);
                save_quipment.setKey_opitem_rows(list_kor);

                Gson gson = new Gson();
                System.out.println(gson.toJson(save_quipment));
                MyApplication.sendMsg(gson.toJson(save_quipment).toString());

                break;
            case R.id.tv_equipment_parlour:
                //添加设备页的弹出框
                int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                } else {
                    initPopupWindow(v, -1, home_text, POP_TYPR_ROOM);
                    popupWindow.showAsDropDown(v, -widthOff, 0);
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final int parent_position, final List<String> text, final int type) {
        //获取自定义布局文件pop.xml的视图
        final View customView = getLayoutInflater().from(this).inflate(R.layout.popupwindow_equipment_listview, null);
        customView.setBackgroundResource(R.drawable.selectbg);

        // 创建PopupWindow实例

        if (add_equipment_Layout_ll.getVisibility() == View.VISIBLE) {
            if (type == 1)
                popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 155, 160);
            else if (type == 0)
                popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 155, 300);
            else
                popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 250, 150);

        } else {
            if (type == 1)
                popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 235, 160);
            else if (type == 0)
                popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 235, 300);
            else
                popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 200, 250);

        }
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (type == POP_TYPE_STATE)
                    keyOpItems.get(parent_position).setKeyOpCmd((byte) position);
                else if (type == POP_TYPE_DOWNUP)
                    keyOpItems.get(parent_position).setKeyOp((byte) position);
                else {
                    dev.clear();
                    for (int i = 0; i < mWareDev.size(); i++) {
                        if (mWareDev.get(i).getRoomName().equals(home_text.get(position)))
                            dev.add(mWareDev.get(i));

                    }
                    if (Equipadapter != null)
                        Equipadapter.notifyDataSetInvalidated();
                    else {
                        Equipadapter = new EquipmentAdapter(dev, AddEquipmentControlActivity.this);
                        add_equipment_Layout_lv.setAdapter(Equipadapter);
                    }
                }
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
            int widthOff = getWindow().getWindowManager().getDefaultDisplay().getWidth() / 500;
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
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(AddEquipmentControlActivity.this);
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
                            keyOpItems.remove(position);
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                            else {
                                adapter = new SwipeAdapter(AddEquipmentControlActivity.this, keyOpItems, mListener);
                                lv.setAdapter(adapter);
                            }
                            dialog.dismiss();
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
        private List<WareDev> listViewItems;

        public EquipmentAdapter(List<WareDev> title, Context context) {
            super();
            listViewItems = title;
            mInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            if (null != listViewItems)
                return listViewItems.size();
            else
                return 0;

        }

        @Override
        public Object getItem(int position) {
            return listViewItems.get(position);
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
                viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);

                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.title.setText(listViewItems.get(position).getDevName());

            if (listViewItems.get(position).getType() == 0)
                viewHolder.image.setImageResource(image[0]);
            else if (listViewItems.get(position).getType() == 1)
                viewHolder.image.setImageResource(image[1]);
            else if (listViewItems.get(position).getType() == 2)
                viewHolder.image.setImageResource(image[2]);
            else if (listViewItems.get(position).getType() == 3)
                viewHolder.image.setImageResource(image[3]);
            else
                viewHolder.image.setImageResource(image[4]);

            return convertView;
        }

        public class ViewHolder {
            public TextView title;
            public ImageView image;
        }
    }

}
