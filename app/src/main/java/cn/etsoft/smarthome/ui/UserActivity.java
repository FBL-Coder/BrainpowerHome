package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.GridViewAdapter;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter;
import cn.etsoft.smarthome.domain.User;
import cn.etsoft.smarthome.domain.UserBean;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2017/3/17.
 */
public class UserActivity extends Activity implements View.OnClickListener {

    private ImageView back;
    private TextView title, edit, equipment_close, tv_equipment_parlour;
    private GridView gridView_user;
    private ListView add_equipment_Layout_lv;
    private List<WareDev> dev;
    private LinearLayout add_equipment_Layout_ll;
    private GridViewAdapter mGridViewAdapter;
    private List<String> home_text;
    private PopupWindow popupWindow;
    private int POP_TYPR_ROOM = 2;
    private EquipmentAdapter Equipadapter;
    private List<WareDev> mWareDev;
    private List<WareDev> common_dev;
    private Gson gson;
    private int room_position = 0;
    private int modelValue = 0, curValue = 0, cmdValue = 0;
    String str_Fixed;
    private SharedPreferences sharedPreferences;
    private boolean IsHome = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getUserData();
        gson = new Gson();
        setContentView(R.layout.activity_user);
        //初始化控件
        initView();
        //加载数据
        initData();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == 4) {
                    mGridViewAdapter.notifyDataSetChanged();
                }
                if (what == 86) {
                    if (MyApplication.getWareData().getUserBeen() != null && MyApplication.getWareData().getUserBeen().getSubType2() == 0) {
                        initData();
                    }
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        home_text = MyApplication.getRoom_list();
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        edit = (TextView) findViewById(R.id.edit);
        title.setText("用户界面");
        if (getIntent().getStringExtra("tag").equals("home")) {
            IsHome = true;
            edit.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
        }
        gridView_user = (GridView) findViewById(R.id.gridView_user);
        add_equipment_Layout_lv = (ListView) findViewById(R.id.equipment_loyout_lv);
        add_equipment_Layout_ll = (LinearLayout) findViewById(R.id.add_equipment_Layout_ll);
        equipment_close = (TextView) findViewById(R.id.equipment_close);
        tv_equipment_parlour = (TextView) findViewById(R.id.tv_equipment_parlour);
        if (home_text != null && home_text.size() > 0)
            tv_equipment_parlour.setText(home_text.get(0));
        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        equipment_close.setOnClickListener(this);
        tv_equipment_parlour.setOnClickListener(this);
    }
    /**
     * 加载数据
     */
    private void initData() {
        if (MyApplication.getWareData().getUserBeen() != null && MyApplication.getWareData().getUserBeen().getDev_rows().size() != 0) {
            common_dev = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData().getUserBeen().getDev_rows().size(); i++) {
                UserBean.DevRowsBean bean = MyApplication.getWareData().getUserBeen().getDev_rows().get(i);
                WareDev dev = new WareDev();
                dev.setDevId((byte) bean.getDevID());
                dev.setCanCpuId(bean.getCanCpuID());
                dev.setType((byte) bean.getDevType());
                common_dev.add(dev);
            }
            mGridViewAdapter = new GridViewAdapter(common_dev, this);
            gridView_user.setAdapter(mGridViewAdapter);
        } else {
            sharedPreferences = getSharedPreferences("profile",
                    Context.MODE_PRIVATE);
            String jsondata = sharedPreferences.getString(GlobalVars.getDevid(), "");
            if (!jsondata.equals("")) {
                common_dev = gson.fromJson(jsondata, new TypeToken<List<WareDev>>() {
                }.getType());
                if (common_dev == null)
                    common_dev = new ArrayList<>();
                mGridViewAdapter = new GridViewAdapter(common_dev, this);
                gridView_user.setAdapter(mGridViewAdapter);
            }
        }
        dev = new ArrayList<>();
        mWareDev = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            mWareDev.add(MyApplication.getWareData().getDevs().get(i));
        }
        for (int i = 0; i < mWareDev.size(); i++) {
            if (home_text != null && home_text.size() > 0)
                if (mWareDev.get(i).getRoomName().equals(home_text.get(0)))
                    dev.add(mWareDev.get(i));
        }
        gridView_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type_dev = common_dev.get(position).getType();
                int value = 0;
                if (type_dev == 0) {
                    for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                        WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                        if (common_dev.get(position).getCanCpuId().equals(AirCondDev.getDev().getCanCpuId())
                                && common_dev.get(position).getDevId() == AirCondDev.getDev().getDevId()) {
                            if (AirCondDev.getbOnOff() == 1) {
                                cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                                value = (modelValue << 5) | cmdValue;
                            } else {
                                cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                                value = (modelValue << 5) | cmdValue;
                            }
                            str_Fixed = getDevCmdstr(position);
                            str_Fixed = str_Fixed +
                                    ",\"cmd\":" + value + "}";
                            MyApplication.sendMsg(str_Fixed);
                        }
                    }
                } else if (type_dev == 1) {
                    for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                        WareTv tv = MyApplication.getWareData().getTvs().get(j);
                        if (common_dev.get(position).getCanCpuId().equals(tv.getDev().getCanCpuId())
                                && common_dev.get(position).getDevId() == tv.getDev().getDevId()) {
                            if (tv.getbOnOff() == 0) {
                                value = UdpProPkt.E_TV_CMD.e_tv_offOn.getValue();
                            } else {
                                value = UdpProPkt.E_TV_CMD.e_tv_numRt.getValue();
                            }
                            str_Fixed = getDevCmdstr(position);
                            str_Fixed = str_Fixed +
                                    ",\"cmd\":" + value + "}";
                            MyApplication.sendMsg(str_Fixed);
                        }
                    }
                } else if (type_dev == 2) {
                    for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                        WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                        if (common_dev.get(position).getCanCpuId().equals(box.getDev().getCanCpuId())
                                && common_dev.get(position).getDevId() == box.getDev().getDevId()) {
                            if (box.getbOnOff() == 0) {
                                value = UdpProPkt.E_TVUP_CMD.e_tvUP_offOn.getValue();
                            } else {
                                value = UdpProPkt.E_TVUP_CMD.e_tvUP_numRt.getValue();
                            }
                            str_Fixed = getDevCmdstr(position);
                            str_Fixed = str_Fixed +
                                    ",\"cmd\":" + value + "}";
                            MyApplication.sendMsg(str_Fixed);
                        }
                    }
                } else if (type_dev == 3) {
                    String ctlStr;
                    for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                        WareLight Light = MyApplication.getWareData().getLights().get(j);
                        if (common_dev.get(position).getCanCpuId().equals(Light.getDev().getCanCpuId())
                                && common_dev.get(position).getDevId() == Light.getDev().getDevId()) {
                            if (Light.getbOnOff() == 0) {
                                ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                        ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                                        ",\"subType1\":0" +
                                        ",\"subType2\":0" +
                                        ",\"canCpuID\":\"" + Light.getDev().getCanCpuId() +
                                        "\",\"devType\":" + Light.getDev().getType() +
                                        ",\"devID\":" + Light.getDev().getDevId() +
                                        ",\"cmd\":0" +
                                        "" +
                                        "}";
                                MyApplication.sendMsg(ctlStr);
                            } else {
                                ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                        ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                                        ",\"subType1\":0" +
                                        ",\"subType2\":0" +
                                        ",\"canCpuID\":\"" + Light.getDev().getCanCpuId() +
                                        "\",\"devType\":" + Light.getDev().getType() +
                                        ",\"devID\":" + Light.getDev().getDevId() +
                                        ",\"cmd\":1" +
                                        "}";
                                MyApplication.sendMsg(ctlStr);
                            }
                        }
                    }
                } else if (type_dev == 4) {
                    for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                        WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                        if (common_dev.get(position).getCanCpuId().equals(Curtain.getDev().getCanCpuId())
                                && common_dev.get(position).getDevId() == Curtain.getDev().getDevId()) {
                            if (Curtain.getbOnOff() == 1) {
                                int Value = UdpProPkt.E_CURT_CMD.e_curt_offOn.getValue();
                                String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                        ",\"datType\":4" +
                                        ",\"subType1\":0" +
                                        ",\"subType2\":0" +
                                        ",\"canCpuID\":\"" + Curtain.getDev().getCanCpuId() + "\"" +
                                        ",\"devType\":" + Curtain.getDev().getType() +
                                        ",\"devID\":" + Curtain.getDev().getDevId();
                                str_Fixed = str_Fixed +
                                        ",\"cmd\":" + Value + "}";
                                MyApplication.sendMsg(str_Fixed);
                            } else {
                                int Value = UdpProPkt.E_CURT_CMD.e_curt_offOff.getValue();
                                String str_Fixed = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                        ",\"datType\":4" +
                                        ",\"subType1\":0" +
                                        ",\"subType2\":0" +
                                        ",\"canCpuID\":\"" + Curtain.getDev().getCanCpuId() + "\"" +
                                        ",\"devType\":" + Curtain.getDev().getType() +
                                        ",\"devID\":" + Curtain.getDev().getDevId();
                                str_Fixed = str_Fixed +
                                        ",\"cmd\":" + Value + "}";
                                MyApplication.sendMsg(str_Fixed);
                            }
                        }
                    }
                }
            }
        });
        gridView_user.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                if (IsHome)
                    return false;
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(UserActivity.this);
                builder.setTitle("提示 :");
                builder.setMessage("您确定删除此设备?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        initDialog("正在删除......");
                        //删除设备
                        common_dev.remove(position);
                        mGridViewAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                        ToastUtil.showToast(UserActivity.this, "删除成功");
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    private Dialog mDialog;

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
     * 空调控制  头命令字符串
     *
     * @param i 集合树阴
     * @return
     */
    public String getDevCmdstr(int i) {
        String data = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                ",\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_ctrlDev.getValue() +
                ",\"subType1\":0" +
                ",\"subType2\":0" +
                ",\"canCpuID\":\"" + common_dev.get(i).getCanCpuId() +
                "\",\"devType\":" + common_dev.get(i).getType() +
                ",\"devID\":" + common_dev.get(i).getDevId();
        return data;
    }

    @Override
    protected void onStop() {
        String savdata = gson.toJson(common_dev);
        sharedPreferences = getSharedPreferences("profile",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(GlobalVars.getDevid(), savdata);
        edit.commit();

        if (!IsHome) {
            User user = gson.fromJson(sharedPreferences.getString("user", ""), User.class);

            UserBean ub = new UserBean();
            ub.setDevUnitID(GlobalVars.getDevid());
            if (user != null) {
                ub.setUserName(user.getId());
                ub.setPasswd(user.getPass());
            }
            ub.setDatType(87);
            ub.setSubType1(0);
            ub.setSubType2(0);
            List<UserBean.DevRowsBean> beanlist = new ArrayList<>();
            for (int i = 0; i < common_dev.size(); i++) {
                UserBean.DevRowsBean bean = new UserBean.DevRowsBean();
                bean.setCanCpuID(common_dev.get(i).getCanCpuId());
                bean.setDevID(common_dev.get(i).getDevId());
                bean.setDevType(common_dev.get(i).getType());
                beanlist.add(bean);
            }
            ub.setDev_rows(beanlist);
            final String data = gson.toJson(ub);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyApplication.sendMsg(data);
                    Log.e("情景模式测试:", data);

                    Looper.prepare();
                    final CountDownTimer timer = new CountDownTimer(3000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            MyApplication.sendMsg(data);
                            Log.e("情景模式测试:", data);
                        }
                    }.start();
                    Looper.loop();

                    MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
                        @Override
                        public void upDataWareData(int what) {
                            if (what == 87)
                                timer.cancel();
                        }
                    });
                }
            }).start();

        }
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                //添加页面的item点击，以及listview的初始化
                Equipadapter = new EquipmentAdapter(dev, UserActivity.this);
                add_equipment_Layout_lv.setAdapter(Equipadapter);
                add_equipment_Layout_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        for (int i = 0; i < mWareDev.size(); i++) {
//                            if (mWareDev.get(i).getRoomName().equals(home_text.get(room_position)))
//                                dev.add(mWareDev.get(i));
//                        }
                        WareDev item = dev.get(position);
                        boolean tag = true;
                        if (common_dev == null)
                            common_dev = new ArrayList<>();
                        if (common_dev.size() > 0) {
                            for (int i = 0; i < common_dev.size(); i++) {
                                if (common_dev.get(i).getType() == item.getType()
                                        && common_dev.get(i).getDevId() == item.getDevId()
                                        && common_dev.get(i).getCanCpuId().equals(item.getCanCpuId())) {
                                    tag = false;
                                    Toast.makeText(UserActivity.this, "设备已存在！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        if (tag) {
                            common_dev.add(item);
                            if (mGridViewAdapter != null)
                                mGridViewAdapter.notifyDataSetChanged();
                            else {
                                mGridViewAdapter = new GridViewAdapter(common_dev, UserActivity.this);
                                gridView_user.setAdapter(mGridViewAdapter);
                            }
                        }
                    }
                });
                add_equipment_Layout_ll.setVisibility(View.VISIBLE);
                break;
            case R.id.equipment_close:
                //添加页面的关闭按钮
                add_equipment_Layout_ll.setVisibility(View.GONE);
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
        popupWindow = new PopupWindow(findViewById(R.id.popupWindow_equipment_sv), 200, 200);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter adapter = new PopupWindowAdapter(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                room_position = position;
                dev.clear();
                for (int i = 0; i < mWareDev.size(); i++) {
                    if (mWareDev.get(i).getRoomName().equals(home_text.get(position)))
                        dev.add(mWareDev.get(i));
                }
                if (Equipadapter != null)
                    Equipadapter.notifyDataSetInvalidated();
                else {
                    Equipadapter = new EquipmentAdapter(dev, UserActivity.this);
                    add_equipment_Layout_lv.setAdapter(Equipadapter);
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
     * 设备适配器；
     */

    private int[] image = new int[]{R.drawable.kongtiao, R.drawable.tv_0, R.drawable.jidinghe, R.drawable.dengguang, R.drawable.chuanglian};

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
