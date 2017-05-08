package cn.etsoft.smarthome.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.PopupWindowAdapter2;
import cn.etsoft.smarthome.adapter.RecyclerViewAdapter_safety;
import cn.etsoft.smarthome.domain.SetSafetyResult;
import cn.etsoft.smarthome.fragment_safety.SafetyFragment;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2017/4/11.
 * 高级设置-安防设置的Activity页面
 */
public class SafetyActivity extends FragmentActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private EditText safetyName;
    private ImageView add_safety;
    private TextView setSafety, scene;
    private Button save;
    private RecyclerViewAdapter_safety recyclerAdapter;
    private List<String> setSafety_list;
    private ImageView iv_cancel;
    private EditText name;
    private Button sure, cancel;
    private Dialog mDialog;
    private List<SetSafetyResult> list;
    private FragmentTransaction transaction;
    private SafetyFragment safetyFragment;
    private List<String> safetyName_list;
    private List<String> sceneName_list;
    private int safety_position = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);
        //进度条
        initDialog("初始化数据中...");
        //横向滑动RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //初始化控件
        initView();
        //初始化防区名称
        initRecycleView();
        //初始化防区类型和关联情景
        initData();
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();
                if (what == 32) {
                    if (MyApplication.getWareData().getResult() != null && MyApplication.getWareData().getResult().getSubType1() == 5) {
                        ToastUtil.showToast(SafetyActivity.this, "保存成功");
                        //保存成功之后获取最新数据
                        MyApplication.getWareData().setResult(null);
                        String ctlStr = "{\"devUnitID\":\"" + GlobalVars.getDevid() + "\"" +
                                ",\"datType\":32" +
                                ",\"subType1\":3" +
                                ",\"subType2\":255" +
                                "}";
                        MyApplication.sendMsg(ctlStr);
                        return;
                    }
                    //初始化防区名称
                    initRecycleView();
                }
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        safetyName = (EditText) findViewById(R.id.name);
        setSafety = (TextView) findViewById(R.id.setSafety);
        scene = (TextView) findViewById(R.id.scene);
        save = (Button) findViewById(R.id.save);
        add_safety = (ImageView) findViewById(R.id.add_safety);
        setSafety.setOnClickListener(this);
        scene.setOnClickListener(this);
        add_safety.setOnClickListener(this);
        save.setOnClickListener(this);
        setSafety_list = new ArrayList<>();
        setSafety_list.add(0, "24小时布防");
        setSafety_list.add(1, "在家布防");
        setSafety_list.add(2, "外出布防");
        setSafety_list.add(3, "撤防状态");
    }

    /**
     * 初始化防区名称
     */
    private void initRecycleView() {
        if (MyApplication.getWareData().getResult_safety() == null || MyApplication.getWareData().getResult_safety().getSec_info_rows() == null) {
            return;
        }
        if (recyclerAdapter != null) {
            recyclerAdapter.notifyDataSetChanged();
        } else {
            recyclerAdapter = new RecyclerViewAdapter_safety(MyApplication.getWareData().getResult_safety());
            recyclerView.setAdapter(recyclerAdapter);
        }
        safetyName_list = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getResult_safety().getSec_info_rows().size(); i++) {
            safetyName_list.add(MyApplication.getWareData().getResult_safety().getSec_info_rows().get(i).getSecName());
        }
        transaction = getSupportFragmentManager().beginTransaction();
        //加最后一个新增按钮
        safetyName_list.add("");
        safetyName.setText(safetyName_list.get(safety_position));
        Bundle bundle = new Bundle();
        safetyFragment = new SafetyFragment();
        //防区位置
        bundle.putInt("safety_position", safety_position);
        safetyFragment.setArguments(bundle);
        transaction.replace(R.id.safety, safetyFragment);
        transaction.commit();
        recyclerAdapter.setOnItemClick(new RecyclerViewAdapter_safety.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = MyApplication.getWareData().getResult_safety().getSec_info_rows().size();
                transaction = getSupportFragmentManager().beginTransaction();
                safety_position = position;
                if (listSize > 0) {
                    if (position < listSize - 1) {
                        initData();
                        safetyName.setText(safetyName_list.get(position));
                        Bundle bundle = new Bundle();
                        safetyFragment = new SafetyFragment();
                        bundle.putInt("safety_position", safety_position);
                        safetyFragment.setArguments(bundle);
                        transaction.replace(R.id.safety, safetyFragment);
                        transaction.commit();
                    } else {
                        getDialog();
                    }
                }
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 初始化自定义dialog
     * 新增防区
     */
    CustomDialog dialog;
    TextView sceneSet_name;

    public void getDialog() {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_sceneset);
        dialog.show();
        sceneSet_name = (TextView) dialog.findViewById(R.id.sceneSet_name);
        sceneSet_name.setText("新增防区");
        iv_cancel = (ImageView) dialog.findViewById(R.id.scene_iv_cancel);
        name = (EditText) dialog.findViewById(R.id.scene_et_name);
        sure = (Button) dialog.findViewById(R.id.scene_btn_sure);
        cancel = (Button) dialog.findViewById(R.id.scene_btn_cancel);
        iv_cancel.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
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

    int sceneId;
    int secType;

    /**
     * 初始化防区类型和关联情景
     */
    private void initData() {
        if (MyApplication.getWareData().getResult_safety() == null || MyApplication.getWareData().getResult_safety().getSec_info_rows() == null) {
            return;
        }
        sceneName_list = new ArrayList<>();
        sceneId = MyApplication.getWareData().getResult_safety().getSec_info_rows().get(safety_position).getSceneId();
        secType = MyApplication.getWareData().getResult_safety().getSec_info_rows().get(safety_position).getSecType();
        if (secType == 255) {
            setSafety.setText(setSafety_list.get(3));
        } else {
            setSafety.setText(setSafety_list.get(secType));
        }
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
            sceneName_list.add((MyApplication.getWareData().getSceneEvents().get(i).getSceneName()));
            if (sceneId == MyApplication.getWareData().getSceneEvents().get(i).getEventld()) {
                scene.setText(MyApplication.getWareData().getSceneEvents().get(i).getSceneName());
            }
        }

        if (MyApplication.getWareData().getSceneEvents().size() == 0 || sceneId == 255) {
            sceneName_list.add(0, "无");
            scene.setText("无");
        } else if (MyApplication.getWareData().getSceneEvents().size() > 0) {
            sceneName_list.add(MyApplication.getWareData().getSceneEvents().size(), "无");
        }
    }

    private PopupWindow popupWindow;

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initPopupWindow(final View view_parent, final List<String> text, final int type) {

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
        popupWindow = new PopupWindow(view_parent.findViewById(R.id.popupWindow_equipment_sv), 200, 300);
        popupWindow.setContentView(customView);
        ListView list_pop = (ListView) customView.findViewById(R.id.popupWindow_equipment_lv);
        PopupWindowAdapter2 adapter = new PopupWindowAdapter2(text, this);
        list_pop.setAdapter(adapter);
        list_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view_parent;
                tv.setText(text.get(position));
                if (type == 0){
                    secType = position;
                }else if (type == 1){
                    sceneId = position;
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setSafety:
                initPopupWindow(setSafety, setSafety_list,0);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.scene:
                initPopupWindow(scene, sceneName_list,1);
                popupWindow.showAsDropDown(v, 0, 0);
                break;
            case R.id.scene_iv_cancel:
                dialog.dismiss();
                break;
            case R.id.scene_btn_sure:
                dialog.dismiss();
                break;
            case R.id.scene_btn_cancel:
                dialog.dismiss();
                break;
            case R.id.save:
                save();
                break;
            case R.id.add_safety:
                startActivity(new Intent(this, AddEquipSafetyActivity.class).putExtra("safety_position", safety_position));
                break;
        }
    }

    /**
     * 保存防区
     */
    int secDev = 0;

    public void save() {
        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(this);
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
                SetSafetyResult.SecInfoRowsBean SecInfoRowsBean = null;
                String data_str = "";
                String div;
                String more_data = "";
                div = ",";
                SecInfoRowsBean = MyApplication.getWareData().getResult_safety().getSec_info_rows().get(safety_position);
                if (SecInfoRowsBean.getRun_dev_item().size() > 0) {
                    secDev = 1;
                } else {
                    secDev = 0;
                }
                for (int j = 0; j < SecInfoRowsBean.getRun_dev_item().size(); j++) {
                    data_str = "{" +
                            "\"uid\":\"" + SecInfoRowsBean.getRun_dev_item().get(j).getCanCpuID() + "\"," +
                            "\"devType\":" + SecInfoRowsBean.getRun_dev_item().get(j).getDevType() + "," +
                            "\"devID\":" + SecInfoRowsBean.getRun_dev_item().get(j).getDevID() + "," +
                            "\"bOnOff\":" + SecInfoRowsBean.getRun_dev_item().get(j).getBOnOff() + "," +
                            "\"lmVal\":0," +
                            "\"rev2\":0," +
                            "\"rev3\":0," +
                            "\"param1\":0," +
                            "\"param2\":0}" + div;
                    more_data += data_str;
                }
                byte[] nameData = {0};
                try {
                    nameData = SecInfoRowsBean.getSecName().getBytes("GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String str_gb = CommonUtils.bytesToHexString(nameData);
                Log.e("情景模式名称:%s", str_gb);
                try {
                    more_data = more_data.substring(0, more_data.lastIndexOf(","));
                } catch (Exception e) {
                    System.out.println(e + "");
                }
                //这就是要上传的字符串:data_hoad
                String data_hoad = "{" +
                        "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                        "\"datType\":32" + "," +
                        "\"subType1\":5" + "," +
                        "\"subType2\":" + safety_position + "," +
                        "\"secName\":\"" + str_gb + "\"," +
                        "\"secType\":" + secType + "," +
                        "\"sceneId\":" + sceneId + "," +
                        "\"secDev\":" + secDev + "," +
                        "\"itemCnt\":" + SecInfoRowsBean.getRun_dev_item().size() + "," +
                        "\"run_item_dev\":[" + more_data + "]}";
                Log.e("情景模式测试:", data_hoad);
                MyApplication.sendMsg(data_hoad);
            }
        });
        builder.create().show();
    }
}
