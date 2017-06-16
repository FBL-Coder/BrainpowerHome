package cn.etsoft.smarthome.fragment_setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.domain.User;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.RcuInfo;
import cn.etsoft.smarthome.pullmi.entity.UdpProPkt;
import cn.etsoft.smarthome.pullmi.entity.WareAirCondDev;
import cn.etsoft.smarthome.pullmi.entity.WareBoardChnout;
import cn.etsoft.smarthome.pullmi.entity.WareBoardKeyInput;
import cn.etsoft.smarthome.pullmi.entity.WareChnOpItem;
import cn.etsoft.smarthome.pullmi.entity.WareCurtain;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareFreshAir;
import cn.etsoft.smarthome.pullmi.entity.WareKeyOpItem;
import cn.etsoft.smarthome.pullmi.entity.WareLight;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.entity.WareSetBox;
import cn.etsoft.smarthome.pullmi.entity.WareTv;
import cn.etsoft.smarthome.ui.HomeActivity;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SwipeListView;

/**
 * Created by Say GoBay on 2016/11/25.
 * 联网模块
 */
public class Setting_NetWorkFragment_New extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private LinearLayout add, network_search;
    private EditText name, id, pwd;
    private Button sure, cancel;
    private SwipeListView equi_list, equi_list_search;
    private Equi_ListAdapter adapter;
    private Equi_Search_ListAdapter adapter_search;
    private View view;
    private String UnitID = GlobalVars.getDevid();
    private String str;
    private SharedPreferences sharedPreferences;
    private String json_user;
    private String json_rcuinfo_list;
    private List<String> json_rcuinfo_list_search;
    private User user;
    private Gson gson;
    private Dialog mDialog;

    public Setting_NetWorkFragment_New(Activity activity) {
        mActivity = activity;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //解决弹出键盘压缩布局的问题
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = inflater.inflate(R.layout.fragment_network_new, container, false);
        sharedPreferences = MyApplication.mInstance.getSharedPreferences("profile", Context.MODE_PRIVATE);
        json_user = sharedPreferences.getString("user", "");
        json_rcuinfo_list = sharedPreferences.getString("list", "");
        gson = new Gson();
        user = gson.fromJson(json_user, User.class);
        //初始化控件
        initView();

        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_regeditRcu.getValue()) {
                    if (MyApplication.getWareData().getAddNewNet_reslut() == 0) {
                        //更新数据
                        ToastUtil.showToast(mActivity, "添加成功");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("list", str);
                        editor.commit();
                        initView();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    } else if (MyApplication.getWareData().getAddNewNet_reslut() == 1) {
                        ToastUtil.showToast(mActivity, "模块已存在");
                    } else {
                        ToastUtil.showToast(mActivity, "添加失败");
                    }
                }
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getRcuInfo.getValue()) {
                    if (MyApplication.mInstance.isSearch() == false) {
                        initView();
                    } else if (MyApplication.mInstance.isSearch() == true) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                        initView_search();
                        MyApplication.mInstance.setSearch(false);
                    }
                }
                if (what == UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delRcu.getValue()) {
                    int reslut = MyApplication.getWareData().getDeleteNetReslut().getInt("Reslut", 2);
                    if (reslut == 0) {
                        ToastUtil.showToast(mActivity, "删除成功");
                        Gson gson = new Gson();
                        List<RcuInfo> json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
                        }.getType());
                        String DevId = MyApplication.getWareData().getDeleteNetReslut().getString("id");
                        for (int i = 0; i < json_list.size(); i++) {
                            if (json_list.get(i).getDevUnitID().equals(DevId)) {
                                json_list.remove(i);
                            }
                        }
                        String json = gson.toJson(json_list);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("list", json);
                        editor.commit();
                        initView();
                    } else if (reslut == 1)
                        ToastUtil.showToast(mActivity, "模块已删除");
                    else
                        ToastUtil.showToast(mActivity, "删除失败");
                }
            }
        });
        return view;
    }

    /**
     * 添加联网模块
     * 初始化控件
     */
    private void initView() {
        if ("".equals(UnitID) || UnitID == null)
            UnitID = "00";
        sharedPreferences = mActivity.getSharedPreferences("profile", Context.MODE_PRIVATE);
        json_user = sharedPreferences.getString("user", "");
        json_rcuinfo_list = sharedPreferences.getString("list", "");
        add = (LinearLayout) view.findViewById(R.id.network_add);
        add.setOnClickListener(this);
        equi_list = (SwipeListView) view.findViewById(R.id.equi_list);
        add.setOnClickListener(this);
        network_search = (LinearLayout) view.findViewById(R.id.network_search);
        network_search.setOnClickListener(this);
        equi_list_search = (SwipeListView) view.findViewById(R.id.equi_list_search);
        if (!"".equals(json_rcuinfo_list)) {
            adapter = new Equi_ListAdapter(json_rcuinfo_list);
            equi_list.setAdapter(adapter);
        }

        equi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(mActivity);
                builder.setTitle("提示 :");

                builder.setMessage("您要切换联网模块？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        List<RcuInfo> json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
                        }.getType());

                        if (json_list == null || json_list.size() == 0) {
                            ToastUtil.showToast(mActivity, "数据异常");
                            dialog.dismiss();
                            return;
                        }
                        RcuInfo info = json_list.get(position);
                        Log.i("RcuInfo", info.getDevUnitID());
                        if (UnitID.equals(info.getDevUnitID())) {
                            ToastUtil.showToast(mActivity, "正在使用中");
                            dialog.dismiss();
                            return;
                        }

                        MyApplication.mInstance.setRcuInfo(info);
                        json_list.remove(position);
                        json_list.add(info);
                        String str1 = gson.toJson(json_list);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("list", str1);
                        editor.putString("module_str", info.getDevUnitID() + "-" + info.getDevUnitPass());
                        editor.commit();
                        GlobalVars.setDevid(json_list.get(json_list.size() - 1).getDevUnitID());
                        GlobalVars.setDevpass(json_list.get(json_list.size() - 1).getDevUnitPass());
                        MyApplication.getWareData().setAirConds(new ArrayList<WareAirCondDev>());
                        MyApplication.getWareData().setBoardChnouts(new ArrayList<WareBoardChnout>());
                        MyApplication.getWareData().setChnOpItems(new ArrayList<WareChnOpItem>());
                        MyApplication.getWareData().setCurtains(new ArrayList<WareCurtain>());
                        MyApplication.getWareData().setKeyOpItems(new ArrayList<WareKeyOpItem>());
                        MyApplication.getWareData().setDevs(new ArrayList<WareDev>());
                        MyApplication.getWareData().setFreshAirs(new ArrayList<WareFreshAir>());
                        MyApplication.getWareData().setKeyInputs(new ArrayList<WareBoardKeyInput>());
                        MyApplication.getWareData().setLights(new ArrayList<WareLight>());
                        MyApplication.getWareData().setRcuInfos(new ArrayList<RcuInfo>());
                        MyApplication.getWareData().setSceneEvents(new ArrayList<WareSceneEvent>());
                        MyApplication.getWareData().setStbs(new ArrayList<WareSetBox>());
                        MyApplication.getWareData().setTvs(new ArrayList<WareTv>());
                        MyApplication.getWareData().setDATA_LOCAL_FLAG(false);

                        //销毁已启动的主页
                        if (MyApplication.getmHomeActivity() != null)
                            MyApplication.getmHomeActivity().finish();
                        //重新启动主页
                        startActivity(new Intent(mActivity, HomeActivity.class));

                        //发送获取数据命令
                        MyApplication.setRcuDevIDtoLocal();
                        //销毁设置页面
                        mActivity.finish();
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        equi_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(mActivity);
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
                        Gson gson = new Gson();
                        List<RcuInfo> json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
                        }.getType());
                        final String key_str = "{" +
                                "\"userName\":\"" + user.getId() + "\"," +
                                "\"passwd\":\"" + user.getPass() + "\"," +
                                "\"devUnitID\":\"" + json_list.get(position).getDevUnitID() + "\"," +
                                "\"devPass\":\"" + json_list.get(position).getDevUnitPass() + "\"," +
                                "\"canCpuName\":\"" + json_list.get(position).getCanCpuName() + "\"," +
                                "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_delRcu.getValue() + "," +
                                "\"subType1\":0," +
                                "\"subType2\":0" + "}";
                        MyApplication.sendMsg(key_str);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    /**
     * 搜索联网模块
     * 初始化控件
     */
    private void initView_search() {
        if ("".equals(UnitID) || UnitID == null)
            UnitID = "00";
        json_rcuinfo_list_search = new ArrayList<>();
        if (MyApplication.getWareData().getSearchNet() != null && MyApplication.getWareData().getSearchNet().getRcu_rows().size() > 0) {
            for (int i = 0; i < MyApplication.getWareData().getSearchNet().getRcu_rows().size(); i++) {
                json_rcuinfo_list_search.add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(MyApplication.getWareData().getSearchNet().getRcu_rows().get(i).getName())));
            }
            adapter_search = new Equi_Search_ListAdapter(json_rcuinfo_list_search);
            equi_list_search.setAdapter(adapter_search);
        } else if (MyApplication.getWareData().getSearchNet() == null || MyApplication.getWareData().getSearchNet().getRcu_rows().size() == 0) {
            ToastUtil.showToast(getActivity(), "没有搜索到本地模块");
        }
//        if (MyApplication.getWareData().getRcuInfo_searches() != null) {
//            for (int i = 0; i < MyApplication.getWareData().getRcuInfo_searches().size(); i++) {
//                json_rcuinfo_list_search.add(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(MyApplication.getWareData().getRcuInfo_searches().get(i).getName())));
//            }
//            adapter_search = new Equi_Search_ListAdapter(json_rcuinfo_list_search);
//            equi_list_search.setAdapter(adapter_search);
//        } else if (MyApplication.getWareData().getRcuInfo_searches() == null ) {
//            ToastUtil.showToast(getActivity(), "没有搜索到本地模块");
//        }
        equi_list_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        equi_list_search.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }

    /**
     * 初始化自定义dialog
     */
    CustomDialog dialog;

    public void getDialog() {
        dialog = new CustomDialog(mActivity, R.style.customDialog, R.layout.dialog_network);
        dialog.show();
        name = (EditText) dialog.findViewById(R.id.network_et_name);
        id = (EditText) dialog.findViewById(R.id.network_et_id);
        pwd = (EditText) dialog.findViewById(R.id.network_et_pwd);
        sure = (Button) dialog.findViewById(R.id.network_btn_sure);
        cancel = (Button) dialog.findViewById(R.id.network_btn_cancel);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.network_add:
                getDialog();
                break;
            //搜索模块
            case R.id.network_search:
                initDialog("搜索中...");
                sharedPreferences = getActivity().getSharedPreferences("profile", Context.MODE_PRIVATE);
                String appid = sharedPreferences.getString("appid", "");
                String ctlStr = "{" +
                        "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                        "\"devPass\":\"" + GlobalVars.getDevpass() + "\"," +
                        "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_getRcuInfoNoPwd.getValue() + "," +
                        "\"uuid\":\"" + appid + "\"," +
                        "\"subType1\":0," +
                        "\"subType2\":0}";
                MyApplication.mInstance.setSearch(true);
                MyApplication.sendMsg(ctlStr);
                break;
            case R.id.network_btn_sure:
                String name_equi = name.getText().toString();
                String id_equi = id.getText().toString();
                String pass_equi = pwd.getText().toString();

                Gson gson = new Gson();
                List<RcuInfo> json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
                }.getType());

                RcuInfo info = new RcuInfo();
                info.setDevUnitID(id_equi);
                info.setDevUnitPass(pass_equi);
                info.setCanCpuName(name_equi);

                List<RcuInfo> json_list_ok = new ArrayList<>();
                if (json_list != null && json_list.size() > 0) {
                    for (int i = 0; i < json_list.size() + 1; i++) {
                        if (i == 0)
                            json_list_ok.add(info);
                        else
                            json_list_ok.add(json_list.get(i - 1));
                    }
                } else
                    json_list_ok.add(info);
                str = gson.toJson(json_list_ok);
//            {
//                "userName": "hwp",
//                    "passwd": "000000",
//                    "devUnitID": "37ffdb05424e323416702443",    // 客户端启动后，输入的联网模块ID
//                    "devPass": "16072443",    //客户端启动后，输入的联网模块密码
//                    "datType": 63,
//                    "subType1": 0,
//                    "subType2": 0
//            }
                final String key_str = "{" +
                        "\"userName\":\"" + user.getId() + "\"," +
                        "\"passwd\":\"" + user.getPass() + "\"," +
                        "\"devUnitID\":\"" + id_equi + "\"," +
                        "\"devPass\":\"" + pass_equi + "\"," +
                        "\"canCpuName\":\"" + name_equi + "\"," +
                        "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_regeditRcu.getValue() + "," +
                        "\"subType1\":0," +
                        "\"subType2\":0" + "}";
                MyApplication.sendMsg(key_str);
                break;
            case R.id.network_btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    //联网模块的适配器
    class Equi_ListAdapter extends BaseAdapter {
        List<RcuInfo> json_list;

        Equi_ListAdapter(String json_rcuinfo_list) {
            json_list = new Gson().fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
            }.getType());
            if (json_list == null) {
                json_list = new ArrayList<>();
            }
        }

        @Override
        public int getCount() {
            return json_list.size();
        }

        @Override
        public Object getItem(int position) {
            return json_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LinearLayout.inflate(mActivity, R.layout.equi_list_item2, null);
                viewHolder.equi_name = (TextView) convertView.findViewById(R.id.equi_name);
                viewHolder.equi_iv_use = (ImageView) convertView.findViewById(R.id.equi_iv_use);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (json_list.get(position).getDevUnitID().equals(UnitID)) {
                viewHolder.equi_iv_use.setVisibility(View.VISIBLE);
                viewHolder.equi_iv_use.setImageResource(R.drawable.checked);
            } else {
                viewHolder.equi_iv_use.setVisibility(View.GONE);
            }
            //解决0 0 1包返回的数据有汉字和其他格式名字的问题  Pattern.compile("[\u4e00-\u9fa5]+")至少有一个汉字
            if (Pattern.compile("[\u4e00-\u9fa5]+").matcher(json_list.get(position).getCanCpuName()).matches()) {
                viewHolder.equi_name.setText(json_list.get(position).getCanCpuName());
            } else {
                viewHolder.equi_name.setText(CommonUtils.getGBstr(CommonUtils.hexStringToBytes(json_list.get(position).getCanCpuName())));
            }
            return convertView;
        }

        class ViewHolder {
            TextView equi_name;
            ImageView equi_iv_use;
        }
    }

    //搜索联网模块的适配器
    class Equi_Search_ListAdapter extends BaseAdapter {
        private List<String> json_rcuinfo_list_search;

        Equi_Search_ListAdapter(List<String> json_rcuinfo_list_search) {
            this.json_rcuinfo_list_search = json_rcuinfo_list_search;
        }

        @Override
        public int getCount() {
            return json_rcuinfo_list_search.size();
        }

        @Override
        public Object getItem(int position) {
            return json_rcuinfo_list_search.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LinearLayout.inflate(mActivity, R.layout.equi_list_item3, null);
                viewHolder.equi_name = (TextView) convertView.findViewById(R.id.equi_name);
                viewHolder.equi_iv_use = (ImageView) convertView.findViewById(R.id.equi_iv_use);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.equi_iv_use.setImageResource(R.drawable.net_add);
            viewHolder.equi_name.setText(json_rcuinfo_list_search.get(position));
            viewHolder.equi_iv_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(mActivity);
                    builder.setTitle("提示 :");

                    builder.setMessage("您要添加此模块到联网模块吗？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String name_equi = MyApplication.getWareData().getSearchNet().getRcu_rows().get(position).getName();

                            String id_equi = MyApplication.getWareData().getSearchNet().getRcu_rows().get(position).getCanCpuID();
                            String pass_equi = MyApplication.getWareData().getSearchNet().getRcu_rows().get(position).getDevUnitPass();
//                            String name_equi = MyApplication.getWareData().getRcuInfo_searches().get(position).getName();
//
//                            String id_equi = MyApplication.getWareData().getRcuInfo_searches().get(position).getCanCpuID();
//                            String pass_equi = MyApplication.getWareData().getRcuInfo_searches().get(position).getDevUnitPass();
                            Log.e("联网模块名称", name_equi);
                            Log.e("联网模块id", id_equi);
                            Log.e("联网模块密码", pass_equi);
                            Log.e("联网模块位置", position + "");
                            Gson gson = new Gson();
                            List<RcuInfo> json_list = gson.fromJson(json_rcuinfo_list, new TypeToken<List<RcuInfo>>() {
                            }.getType());

                            RcuInfo info = new RcuInfo();
                            info.setDevUnitID(id_equi);
                            info.setDevUnitPass(pass_equi);
                            info.setCanCpuName(name_equi);
                            List<RcuInfo> json_list_ok = new ArrayList<>();
                            if (json_list != null && json_list.size() > 0) {
                                for (int i = 0; i < json_list.size() + 1; i++) {
                                    if (i == 0)
                                        json_list_ok.add(info);
                                    else
                                        json_list_ok.add(json_list.get(i - 1));
                                }
                            } else
                                json_list_ok.add(info);
                            str = gson.toJson(json_list_ok);
//            {
//                "userName": "hwp",
//                    "passwd": "000000",
//                    "devUnitID": "37ffdb05424e323416702443",    // 客户端启动后，输入的联网模块ID
//                    "devPass": "16072443",    //客户端启动后，输入的联网模块密码
//                    "datType": 63,
//                    "subType1": 0,
//                    "subType2": 0
//            }
                            final String key_str = "{" +
                                    "\"userName\":\"" + user.getId() + "\"," +
                                    "\"passwd\":\"" + user.getPass() + "\"," +
                                    "\"devUnitID\":\"" + id_equi + "\"," +
                                    "\"devPass\":\"" + pass_equi + "\"," +
                                    "\"canCpuName\":\"" + name_equi + "\"," +
                                    "\"datType\":" + UdpProPkt.E_UDP_RPO_DAT.e_udpPro_regeditRcu.getValue() + "," +
                                    "\"subType1\":0," +
                                    "\"subType2\":0" + "}";
                            dialog.dismiss();
                            MyApplication.sendMsg(key_str);
                        }
                    });
                    builder.create().show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView equi_name;
            ImageView equi_iv_use;
        }
    }
}
