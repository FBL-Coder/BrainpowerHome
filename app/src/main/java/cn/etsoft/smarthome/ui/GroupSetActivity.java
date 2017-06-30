package cn.etsoft.smarthome.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.domain.GroupSet_Data;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;

import static cn.etsoft.smarthome.R.id.tv_equipment_parlour;

/**
 * 定时器界面
 * Created by F-B-L on 2017/5/18.
 */
public class  GroupSetActivity extends FragmentActivity implements View.OnClickListener {

    private Button btn_save;
    private TextView tv_enabled, event_way, add_dev_groupSet,
            tv_text_parlour, add_dev_Layout_close;
    private int[] image = new int[]{R.drawable.kongtiao, R.drawable.tv_0, R.drawable.jidinghe, R.drawable.dengguang, R.drawable.chuanglian};
    private EditText et_name;
    private GridView gridView_groupSet;
    private ListView add_dev_Layout_lv, safety;
    private LinearLayout add_dev_Layout_ll;
    private RecyclerView RecyclerView_env;
    private Dialog mDialog;
    private RecyclerViewAdapter_groupSet adapter_groupSet;
    private PopupWindow popupWindow;
    private GridViewAdapter_groupSet GridViewAdapter_groupSet;
    private List<String> home_text;
    private List<WareDev> dev;
    private List<WareDev> mWareDev;
    private List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> common_dev;
    private GroupSetActivity.EquipmentAdapter Equipadapter;
    //添加设备房间position；
    private int home_position;
    //触发器所在列表位置
    private int GroupSet_position;
    private boolean IsHaveData = false;
//    private SafetyAdapter safetyAdapter;
//    private List<String> safetyName;

    String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
            ",\"datType\":66" +
            ",\"subType1\":0" +
            ",\"subType2\":255" +
            "}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //解决弹出键盘压缩布局的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_groupset);
        initDialog("初始化数据中...");
        //初始化组件
        initView();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 66) {
                    IsHaveData = true;
                    if (mDialog != null)
                        mDialog.dismiss();
                    //初始化RecycleView
                    initRecycleView(GroupSet_position);
                    if (GroupSet_position != 0) {
                        ToastUtil.showToast(GroupSetActivity.this, "保存成功");
                    }
                }
            }
        });
        MyApplication.sendMsg(ctlStr);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        btn_save = (Button) findViewById(R.id.save);
        tv_enabled = (TextView) findViewById(R.id.enabled);
        event_way = (TextView) findViewById(R.id.event_way);
        add_dev_groupSet = (TextView) findViewById(R.id.add_dev_groupSet);
        et_name = (EditText) findViewById(R.id.name);
        gridView_groupSet = (GridView) findViewById(R.id.gridView_groupSet);

        //添加设备布局
        add_dev_Layout_lv = (ListView) findViewById(R.id.equipment_loyout_lv);
        add_dev_Layout_close = (TextView) findViewById(R.id.equipment_close);
        tv_text_parlour = (TextView) findViewById(tv_equipment_parlour);
        add_dev_Layout_ll = (LinearLayout) findViewById(R.id.add_dev_Layout_ll);
        add_dev_Layout_close.setOnClickListener(this);
        tv_text_parlour.setOnClickListener(this);

        btn_save.setOnClickListener(this);
        event_way.setOnClickListener(this);
        tv_enabled.setOnClickListener(this);
        add_dev_groupSet.setOnClickListener(this);
        et_name.setOnClickListener(this);

        RecyclerView_env = (RecyclerView) findViewById(R.id.RecyclerView_groupSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView_env.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

//        safety = (ListView) findViewById(R.id.safety);
//        safetyAdapter = new SafetyAdapter(mListener);
//        safety.setAdapter(safetyAdapter);
    }

//    class SafetyAdapter extends BaseAdapter {
//
//        SafetyAdapter(IClick listener) {
//            safetyName = new ArrayList<>();
//            for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
//                safetyName.add(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecName());
//            }
//            mListener = listener;
//        }
//
//        @Override
//        public void notifyDataSetChanged() {
//            safetyName = new ArrayList<>();
//            for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
//                safetyName.add(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecName());
//            }
//            super.notifyDataSetChanged();
//        }
//
//        @Override
//        public int getCount() {
//            return safetyName.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return safetyName.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(GroupSetActivity.this).inflate(R.layout.listview_safety_item, null);
//                viewHolder = new ViewHolder();
//                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
//                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
//                convertView.setTag(viewHolder);
//            } else
//                viewHolder = (ViewHolder) convertView.getTag();
//
//            viewHolder.name.setText(safetyName.get(position));
//            viewHolder.checkBox.setChecked(true);
//
//            viewHolder.checkBox.setOnClickListener(mListener);
//            viewHolder.checkBox.setTag(position);
//            return convertView;
//        }
//
//        private class ViewHolder {
//            private TextView name;
//            public CheckBox checkBox;
//        }
//    }
//
//
//    /**
//     * 实现类，响应按钮点击事件
//     */
//    private IClick mListener = new IClick() {
//        @Override
//        public void listViewItemClick(final int position, View v) {
//
//
//        }
//    };


    /**
     * 初始化GridView
     */
    public void initGridView(int RecycleViewposition) {
        if (MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().size() == 0)
            return;
        common_dev = MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(RecycleViewposition).getRun_dev_item();
        GridViewAdapter_groupSet = new GridViewAdapter_groupSet(common_dev);
        gridView_groupSet.setAdapter(GridViewAdapter_groupSet);

        gridView_groupSet.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                common_dev.remove(position);
                GridViewAdapter_groupSet.notifyDataSetChanged(common_dev);
                return true;
            }
        });
        gridView_groupSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (common_dev.get(position).getBOnOff() == 0)
                    common_dev.get(position).setBOnOff(1);
                else
                    common_dev.get(position).setBOnOff(0);
                GridViewAdapter_groupSet.notifyDataSetChanged(common_dev);
            }
        });
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
     * 初始化数据
     *
     * @param groupSet_position
     */
    public void initData(int groupSet_position) {
        home_text = MyApplication.getRoom_list();
        if (MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().size() == 0)
            return;
        et_name.setText("");
        et_name.setHint(MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(groupSet_position).getTriggerName());
        if (MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(groupSet_position).getRun_dev_item() == null
                || MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(groupSet_position).getRun_dev_item().size() == 0) {
//            tv_enabled.setText("禁用");
//            event_way.setText("否");
            ToastUtil.showToast(this,"该组合触发器下没有设备");
        }
        else {
            if (MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(groupSet_position).getValid() == 1)
                tv_enabled.setText("启用");
            else tv_enabled.setText("禁用");

            if (MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(groupSet_position).getReportServ() == 1)
                event_way.setText("是");
            else event_way.setText("否");
        }
    }

    /**
     * 初始化 添加设备 数据
     *
     * @param home_position
     * @return
     */
    public List<WareDev> initGridViewAddData(int home_position) {
        dev = new ArrayList<>();
        mWareDev = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            mWareDev.add(MyApplication.getWareData().getDevs().get(i));
        }
        for (int i = 0; i < mWareDev.size(); i++) {
            if (home_text != null && home_text.size() > 0)
                if (mWareDev.get(i).getRoomName().equals(home_text.get(home_position)))
                    dev.add(mWareDev.get(i));
        }
        return dev;
    }

    @Override
    public void onClick(View v) {
        if (!IsHaveData) {
            ToastUtil.showToast(GroupSetActivity.this, "获取数据异常，请稍后在试");
            return;
        }
        switch (v.getId()) {
            case R.id.save://保存
                try {
                    GroupSet_Data time_data = new GroupSet_Data();
                    List<GroupSet_Data.SecsTriggerRowsBean> envEvent_rows = new ArrayList<>();
                    GroupSet_Data.SecsTriggerRowsBean bean = new GroupSet_Data.SecsTriggerRowsBean();
                    //  "devCnt": 1,
                    bean.setDevCnt(common_dev.size());
                    //"eventId":	0,
                    bean.setTriggerId(GroupSet_position);
                    // "run_dev_item":
                    bean.setRun_dev_item(common_dev);

                    if ("".equals(et_name.getText().toString())) {
                        bean.setTriggerName(CommonUtils.bytesToHexString(MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().get(GroupSet_position).getTriggerName().getBytes("GB2312")));
                    } else {
                        try {
                            //触发器名称
                            bean.setTriggerName(CommonUtils.bytesToHexString(et_name.getText().toString().getBytes("GB2312")));
                        } catch (UnsupportedEncodingException e) {
                            ToastUtil.showToast(GroupSetActivity.this, "触发器名称不合适");
                            return;
                        }
                    }
                    if (et_name.getText().toString().length() > 24) {
                        ToastUtil.showToast(GroupSetActivity.this, "触发器名称不能过长");
                        return;
                    }
                    //组合触发器是否启用   "valid":
                    if ("启用".equals(tv_enabled.getText().toString()))
                        bean.setValid(1);
                    else
                        bean.setValid(0);

                    //是否上报服务器
                    if ("是".equals(event_way.getText().toString()))
                        bean.setReportServ(1);
                    else bean.setReportServ(0);

                    envEvent_rows.add(bean);
                    time_data.setDatType(66);
                    time_data.setDevUnitID(GlobalVars.getDevid());
                    time_data.setItemCnt(1);
                    time_data.setSubType1(2);
                    time_data.setSubType2(0);
                    time_data.setSecs_trigger_rows(envEvent_rows);
                    Gson gson = new Gson();
                    Log.i("保存触发器数据", gson.toJson(time_data));
                    initDialog("保存数据中...");
                    MyApplication.sendMsg(gson.toJson(time_data));
                } catch (Exception e) {
                    if (mDialog != null)
                        mDialog.dismiss();
                    Log.e("保存触发器数据", "保存数据异常" + e);
                    ToastUtil.showToast(this, "保存数据异常,请检查数据是否合适");
                }
                //TODO   保存名字不合适。
                break;
            case R.id.add_dev_groupSet://点击添加设备按钮事件

                //添加页面的item点击，以及listview的初始化
                Equipadapter = new GroupSetActivity.EquipmentAdapter(initGridViewAddData(home_position), this);
                add_dev_Layout_lv.setAdapter(Equipadapter);
                tv_text_parlour.setText(home_text.get(home_position));
                add_dev_Layout_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        WareDev item = dev.get(position);
                        boolean tag = true;
                        if (common_dev == null)
                            common_dev = new ArrayList<>();
                        if (common_dev.size() > 0) {
                            for (int i = 0; i < common_dev.size(); i++) {
                                if (common_dev.get(i).getDevType() == item.getType()
                                        && common_dev.get(i).getDevID() == item.getDevId()
                                        && common_dev.get(i).getCanCpuID().equals(item.getCanCpuId())) {
                                    tag = false;
                                    Toast.makeText(GroupSetActivity.this, "设备已存在！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (tag) {
                            if (common_dev.size() == 4) {
                                ToastUtil.showToast(GroupSetActivity.this, "定时设备最多4个！");
                                return;
                            }
                            GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean bean = new GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean();
                            bean.setDevID(item.getDevId());
                            bean.setBOnOff(item.getbOnOff());
                            bean.setDevType(item.getType());
                            bean.setCanCpuID(item.getCanCpuId());
                            common_dev.add(bean);
                            if (GridViewAdapter_groupSet != null)
                                GridViewAdapter_groupSet.notifyDataSetChanged(common_dev);
                            else {
                                GridViewAdapter_groupSet = new GridViewAdapter_groupSet(common_dev);
                                gridView_groupSet.setAdapter(GridViewAdapter_groupSet);
                            }
                        }
                    }
                });

                add_dev_Layout_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        if (common_dev.size() > 0) {
                            common_dev.remove(position);
                            if (GridViewAdapter_groupSet != null)
                                GridViewAdapter_groupSet.notifyDataSetChanged(common_dev);
                            else {
                                GridViewAdapter_groupSet = new GridViewAdapter_groupSet(common_dev);
                                gridView_groupSet.setAdapter(GridViewAdapter_groupSet);
                            }
                        }
                        return true;
                    }
                });
                add_dev_Layout_ll.setVisibility(View.VISIBLE);
                break;
            case R.id.enabled: //启用开关
                List<String> Enabled = new ArrayList<>();
                Enabled.add("禁用");
                Enabled.add("启用");
                initRadioPopupWindow(tv_enabled, Enabled);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.event_way://是否上传服务器
                List<String> Event_Way = new ArrayList<>();
                Event_Way.add("否");
                Event_Way.add("是");
                initRadioPopupWindow(event_way, Event_Way);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.tv_equipment_parlour://添加设备 选择房间
                initRadioPopupWindow(tv_text_parlour, home_text);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.equipment_close: //关闭  添加设备界面
                add_dev_Layout_ll.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 初始化环境时间名称
     */
    private void initRecycleView(int groupSet_position) {
        mDialog.dismiss();
        adapter_groupSet = new RecyclerViewAdapter_groupSet(MyApplication.getWareData().getmGroupSet_Data());
        adapter_groupSet.setSelectPosition(groupSet_position);
        RecyclerView_env.setAdapter(adapter_groupSet);

        adapter_groupSet.setOnItemClick(new RecyclerViewAdapter_groupSet.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = MyApplication.getWareData().getmGroupSet_Data().getSecs_trigger_rows().size();
                if (listSize > 0) {
                    GroupSet_position = position;
                    initData(position);
                    initGridView(position);
                }
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
        initGridView(groupSet_position);
        initData(groupSet_position);
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.popupwindow_equipment_listview, null);
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
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                popupWindow.dismiss();

                if (view_parent.getId() == R.id.tv_equipment_parlour) {
                    dev = new ArrayList<>();
                    for (int i = 0; i < mWareDev.size(); i++) {
                        if (mWareDev.get(i).getRoomName().equals(home_text.get(position)))
                            dev.add(mWareDev.get(i));
                    }
                    home_position = position;
                    if (Equipadapter != null)
                        Equipadapter.notifyDataSetChanged(dev);
                    else {
                        Equipadapter = new GroupSetActivity.EquipmentAdapter(dev, GroupSetActivity.this);
                        gridView_groupSet.setAdapter(Equipadapter);
                    }
                }
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
     * RecyclerView Adapter
     */
    static class RecyclerViewAdapter_groupSet extends RecyclerView.Adapter<RecyclerViewAdapter_groupSet.SceneViewHolder> {
        private List<GroupSet_Data.SecsTriggerRowsBean> list;
        private int mPosition = 0;
        private int[] image = {R.drawable.zaijiamoshi, R.drawable.waichumoshi,
                R.drawable.yingyuanmoshi, R.drawable.jiuqingmoshi,
                R.drawable.huikemoshi};
        private SceneViewHolder.OnItemClick onItemClick;

        public RecyclerViewAdapter_groupSet(GroupSet_Data result) {
            if (list == null)
                list = new ArrayList<>();
            for (int i = 0; i < result.getSecs_trigger_rows().size(); i++) {
                list.add(result.getSecs_trigger_rows().get(i));
            }
        }

        public void setSelectPosition(int groupSet_position) {
            mPosition = groupSet_position;
        }

        public void setOnItemClick(RecyclerViewAdapter_groupSet.SceneViewHolder.OnItemClick onItemClick) {
            this.onItemClick = onItemClick;
        }

        @Override
        public RecyclerViewAdapter_groupSet.SceneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, null);
            RecyclerViewAdapter_groupSet.SceneViewHolder holder = new RecyclerViewAdapter_groupSet.SceneViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter_groupSet.SceneViewHolder holder, final int position) {
            if (mPosition == position) {
                holder.itemView.setBackgroundResource(R.color.color_334eade6);  //选中项背景
            } else {
                holder.itemView.setBackgroundResource(R.color.color_00000000);  //其他项背景
            }
            holder.iv.setImageResource(image[position % 5]);
            holder.tv.setText(list.get(position).getTriggerName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        int pos = holder.getLayoutPosition();
                        mPosition = pos;
                        onItemClick.OnItemClick(holder.itemView, pos);
                        notifyDataSetChanged();
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClick != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClick.OnItemLongClick(holder.itemView, pos);
                    }
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class SceneViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private TextView tv;

            public SceneViewHolder(View itemView) {
                super(itemView);
                iv = (ImageView) itemView.findViewById(R.id.img_list_item);
                tv = (TextView) itemView.findViewById(R.id.text_list_item);
            }

            public interface OnItemClick {
                void OnItemClick(View view, int position);

                void OnItemLongClick(View view, int position);
            }
        }
    }

    /**
     * GridView Adapter
     */
    class GridViewAdapter_groupSet extends BaseAdapter {

        private List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> env_list;

        GridViewAdapter_groupSet(List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> list) {
            env_list = list;
        }

        public void notifyDataSetChanged(List<GroupSet_Data.SecsTriggerRowsBean.RunDevItemBean> list) {
            env_list = list;
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (env_list == null)
                return 0;
            return env_list.size();
        }

        @Override
        public Object getItem(int position) {
            if (env_list == null)
                return null;
            return env_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(GroupSetActivity.this).inflate(R.layout.gridview_item_user, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.equip_name);
                viewHolder.type = (ImageView) convertView.findViewById(R.id.equip_type);
                viewHolder.state = (TextView) convertView.findViewById(R.id.equip_style);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            int type_dev = env_list.get(position).getDevType();
            if (type_dev == 0) {
                for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                    WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                    if (env_list.get(position).getDevID() == AirCondDev.getDev().getDevId() &&
                            env_list.get(position).getCanCpuID().endsWith(AirCondDev.getDev().getCanCpuId())) {
                        viewHolder.name.setText(AirCondDev.getDev().getDevName());
                        if (env_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.kongtiao1);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.kongtiao2);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 1) {
                for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                    WareTv tv = MyApplication.getWareData().getTvs().get(j);
                    if (env_list.get(position).getDevID() == tv.getDev().getDevId() &&
                            env_list.get(position).getCanCpuID().endsWith(tv.getDev().getCanCpuId())) {
                        viewHolder.name.setText(tv.getDev().getDevName());
                        if (env_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.ds);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.ds);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 2) {
                for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                    WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                    if (env_list.get(position).getDevID() == box.getDev().getDevId() &&
                            env_list.get(position).getCanCpuID().endsWith(box.getDev().getCanCpuId())) {
                        viewHolder.name.setText(box.getDev().getDevName());
                        if (env_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.jidinghe);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.jidinghe);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 3) {
                for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                    WareLight Light = MyApplication.getWareData().getLights().get(j);
                    if (env_list.get(position).getDevID() == Light.getDev().getDevId() &&
                            env_list.get(position).getCanCpuID().endsWith(Light.getDev().getCanCpuId())) {
                        viewHolder.name.setText(Light.getDev().getDevName());
                        if (env_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.light);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.lightk);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            } else if (type_dev == 4) {
                for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                    WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                    if (env_list.get(position).getDevID() == Curtain.getDev().getDevId() &&
                            env_list.get(position).getCanCpuID().endsWith(Curtain.getDev().getCanCpuId())) {
                        viewHolder.name.setText(Curtain.getDev().getDevName());
                        if (env_list.get(position).getBOnOff() == 0) {
                            viewHolder.type.setImageResource(R.drawable.quanguan);
                            viewHolder.state.setText("关闭");
                        } else {
                            viewHolder.type.setImageResource(R.drawable.quankai);
                            viewHolder.state.setText("打开");
                        }
                    }
                }
            }

            return convertView;
        }

        private class ViewHolder {
            private TextView name, state;
            private ImageView type;
        }
    }

    /**
     * 添加设备然后设备列表适配器
     */
    class EquipmentAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<WareDev> listViewItems;

        public EquipmentAdapter(List<WareDev> title, Context context) {
            super();
            listViewItems = title;
            mInflater = LayoutInflater.from(context);
        }

        public void notifyDataSetChanged(List<WareDev> title) {
            listViewItems = title;
            super.notifyDataSetChanged();
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
            GroupSetActivity.EquipmentAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.equipment_listview_item, null);
                viewHolder = new GroupSetActivity.EquipmentAdapter.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.equipment_tv);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.equipment_iv);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (GroupSetActivity.EquipmentAdapter.ViewHolder) convertView.getTag();
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
