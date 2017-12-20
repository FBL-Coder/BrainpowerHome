package cn.etsoft.smarthome.Activity.Settings;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.SetAddDevAdapter;
import cn.etsoft.smarthome.Adapter.GridView.SetAddSceneAdapter;
import cn.etsoft.smarthome.Adapter.PopupWindow.PopupWindowAdapter2;
import cn.etsoft.smarthome.Domain.UserBean;
import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.NewHttpPort;

/**
 * Author：FBL  Time： 2017/10/19.
 * 用户界面天机设备和情景模式
 */

public class UserAddDevsActivty extends BaseActivity implements View.OnClickListener {
    private TextView mDevOrScene, mUserAdddevSave, mRoom;
    private ImageView back;
    private GridView mUserAdddevGv;
    private PopupWindow popupWindow;
    private List<String> room_list;
    private List<WareDev> devs;
    private List<WareDev> Alldevs;
    private List<WareSceneEvent> sceneEvents;
    private int position_room;
    private int position_type;
    private int ROOM = 1, TYPE = 2;
    private SetAddDevAdapter addDevAdapter;
    private SetAddSceneAdapter sceneAdapter;


    @Override
    protected void onStop() {
        for (int i = 0; i < Alldevs.size(); i++) {
            Alldevs.get(i).setSelect(false);
        }
        for (int i = 0; i < sceneEvents.size(); i++) {
            sceneEvents.get(i).setSelect(false);
        }
        super.onStop();
    }

    public void initData() {
        if (mDevOrScene.getText().toString().equals("设备")) {
            mRoom.setClickable(true);
            room_list = new ArrayList<>();
            devs = new ArrayList<>();
            room_list.add("全部");
            room_list.addAll(MyApplication.getWareData().getRooms());
            //房间名称；
            mRoom.setText(room_list.get(position_room));
            if (position_room == 0) {
                devs.addAll(Alldevs);
            } else {
                //根据房间id获取设备；
                for (int i = 0; i < Alldevs.size(); i++) {
                    if (Alldevs.get(i).getRoomName()
                            .equals(mRoom.getText())) {
                        devs.add(Alldevs.get(i));
                    }
                }
            }
            addDevAdapter = new SetAddDevAdapter(this, devs);
            mUserAdddevGv.setAdapter(addDevAdapter);
        } else {
            mRoom.setText("----");
            mRoom.setClickable(false);
            sceneAdapter = new SetAddSceneAdapter(this, sceneEvents);
            mUserAdddevGv.setAdapter(sceneAdapter);
        }
    }

    private List<WareDev> InitDevData(List<WareDev> devs) {
        UserBean bean = MyApplication.getWareData().getUserBeen();
        for (int i = 0; i < devs.size(); i++) {
            for (int j = 0; j < bean.getUser_bean().size(); j++) {
                if (bean.getUser_bean().get(j).isIsDev() == 1) {
                    if (devs.get(i).getDevId() == bean.getUser_bean().get(j).getDevID() &&
                            devs.get(i).getType() == bean.getUser_bean().get(j).getDevType() &&
                            devs.get(i).getCanCpuId().equals(bean.getUser_bean().get(j).getCanCpuID())) {
                        devs.get(i).setSelect(true);
                    }
                }
            }
        }
        return devs;
    }

    private List<WareSceneEvent> initSceneData(List<WareSceneEvent> sceneEvents) {
        UserBean bean = MyApplication.getWareData().getUserBeen();
        for (int i = 0; i < sceneEvents.size(); i++) {
            for (int j = 0; j < bean.getUser_bean().size(); j++) {
                if (bean.getUser_bean().get(j).isIsDev() == 0) {
                    if (sceneEvents.get(i).getEventId() == bean.getUser_bean().get(j).getEventId()) {
                        sceneEvents.get(i).setSelect(true);
                    }
                }
            }
        }
        return sceneEvents;
    }

    public void initView() {
        setLayout(R.layout.activity_user_adddev);

        sceneEvents = new ArrayList<>();
        sceneEvents.addAll(MyApplication.getWareData().getSceneEvents());
        Alldevs = new ArrayList<>();
        Alldevs.addAll(MyApplication.getWareData().getDevs());
        Alldevs = InitDevData(Alldevs);
        sceneEvents = initSceneData(sceneEvents);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 3 || datType == 22) initData();
            }
        });

        mDevOrScene = (TextView) findViewById(R.id.dev_or_scene);
        mRoom = (TextView) findViewById(R.id.room);
        mUserAdddevGv = (GridView) findViewById(R.id.user_adddev_gv);
        mUserAdddevSave = (TextView) findViewById(R.id.user_adddev_save);
        mRoom.setOnClickListener(this);
        mDevOrScene.setOnClickListener(this);
        mUserAdddevSave.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.room:
                initRadioPopupWindow(view, room_list, ROOM);
                popupWindow.showAsDropDown(view, 0, 0);
                break;
            case R.id.dev_or_scene:
                List<String> type = new ArrayList<>();
                type.add("设备");
                type.add("情景");
                initRadioPopupWindow(view, type, TYPE);
                popupWindow.showAsDropDown(view, 0, 0);
                break;
            case R.id.user_adddev_save:

                List<WareDev> Save_dev = new ArrayList<>();
                for (int i = 0; i < devs.size(); i++) {
                    if (devs.get(i).isSelect()) {
                        Save_dev.add(devs.get(i));
                    }
                }

                List<WareSceneEvent> event = new ArrayList<>();
                for (int i = 0; i < sceneEvents.size(); i++) {
                    if (sceneEvents.get(i).isSelect()) {
                        event.add(sceneEvents.get(i));
                    }
                }

                List<UserBean.UserBeanBean> save = new ArrayList<>();
                for (int i = 0; i < Save_dev.size(); i++) {
                    UserBean.UserBeanBean beanBean = new UserBean.UserBeanBean();
                    beanBean.setCanCpuID(Save_dev.get(i).getCanCpuId());
                    beanBean.setDevType(Save_dev.get(i).getType());
                    beanBean.setDevID(Save_dev.get(i).getDevId());
                    beanBean.setIsDev(1);
                    save.add(beanBean);
                }
                for (int i = 0; i < event.size(); i++) {
                    UserBean.UserBeanBean beanBean = new UserBean.UserBeanBean();
                    beanBean.setCanCpuID("");
                    beanBean.setDevType(0);
                    beanBean.setEventId(event.get(i).getEventId());
                    beanBean.setDevID(0);
                    beanBean.setIsDev(0);
                    save.add(beanBean);
                }
                UserBean userBean = new UserBean();
                userBean.setUser_bean(save);
                userBean.setDevUnitID((String) AppSharePreferenceMgr
                        .get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""));
                Gson gson = new Gson();
                String savedata = gson.toJson(userBean);
                HashMap<String, String> map = new HashMap<>();
                map.put("userName", (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, ""));
                map.put("passwd", (String) AppSharePreferenceMgr.get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""));
                map.put("data", savedata);
                Log.i("UserAddDevsActivty", "savedata: " + savedata);

                //本地保存数据
                AppSharePreferenceMgr.put(GlobalVars.USER_DATA_SHAREPREFERENCE, savedata);
                //远程服务器保存数据
                OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.USER_ADD,
                        map, new HttpCallback() {
                            @Override
                            public void onSuccess(ResultDesc resultDesc) {
                                super.onSuccess(resultDesc);
                                ToastUtil.showText("保存成功");
                                finish();
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                super.onFailure(code, message);
                                ToastUtil.showText("远程保存失败，本地保存成功");
                                finish();
                            }
                        });
                break;
        }
    }

    /**
     * 初始化自定义设备的状态以及设备PopupWindow
     */
    private void initRadioPopupWindow(final View view_parent, final List<String> text, final int Falg) {

        //获取自定义布局文件pop.xml的视图
        final View customView = view_parent.inflate(this, R.layout.listview_popupwindow_equipment, null);
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
        popupWindow = new PopupWindow(customView, view_parent.getWidth(), 200);
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
                if (Falg == ROOM)
                    position_room = position;
                else position_type = position;
                initData();
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
}
