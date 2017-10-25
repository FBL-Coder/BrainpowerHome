package cn.etsoft.smarthome.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.etsoft.smarthome.Activity.Settings.UserAddDevsActivty;
import cn.etsoft.smarthome.Adapter.GridView.GridViewAdapter_User;
import cn.etsoft.smarthome.Domain.UdpProPkt;
import cn.etsoft.smarthome.Domain.UserBean;
import cn.etsoft.smarthome.Domain.WareAirCondDev;
import cn.etsoft.smarthome.Domain.WareCurtain;
import cn.etsoft.smarthome.Domain.WareFloorHeat;
import cn.etsoft.smarthome.Domain.WareFreshAir;
import cn.etsoft.smarthome.Domain.WareLight;
import cn.etsoft.smarthome.Domain.WareSetBox;
import cn.etsoft.smarthome.Domain.WareTv;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Utils.NewHttpPort;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/10/24.
 * 用户界面
 */

public class UserInterface extends BaseActivity implements AdapterView.OnItemClickListener {
    private GridView gridView;
    private GridViewAdapter_User adapter_user;
    private UserBean bean;
    private Handler handler;
    private ImageView back;

    @Override
    public void onResume() {
        if (!MyApplication.mApplication.isVisitor())
            getUserData(handler);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 3 || datType == 4 || datType == 35) {
                    initGridView(false);
                }
            }
        });
        super.onResume();
    }

    @Override
    public void initView() {
        setLayout(R.layout.activity_user);
        gridView = (GridView) findViewById(R.id.home_gv);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        if (MyApplication.getWareData().getUserBeen().getUser_bean().size() > 0) {
            MyApplication.mApplication.dismissLoadDialog();
            //初始化GridView
            initGridView(true);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MyApplication.mApplication.dismissLoadDialog();
                if (msg.what == 0)
                    //初始化GridView
                    initGridView(true);
                else {
                    ToastUtil.showText("用户数据获取失败");
                }
            }
        };
    }

    /**
     * 初始化GridView
     */
    private void initGridView(boolean isNew) {
        bean = MyApplication.getWareData().getUserBeen();
        if (isNew || adapter_user == null) {
            adapter_user = new GridViewAdapter_User(MyApplication.getWareData().getUserBeen(), this);
            gridView.setAdapter(adapter_user);
        } else adapter_user.notifyDataSetChanged(MyApplication.getWareData().getUserBeen());
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position < bean.getUser_bean().size()) {
            //给点击按钮添加点击音效
            MyApplication.mApplication.getSp().play(MyApplication.mApplication.getMusic(), 1, 1, 0, 0, 1);
            UserBean.UserBeanBean beanBean = bean.getUser_bean().get(position);
            if (beanBean.isIsDev() == 1) {
                int type_dev = beanBean.getDevType();
                if (type_dev == 0) {
                    for (int j = 0; j < MyApplication.getWareData().getAirConds().size(); j++) {
                        WareAirCondDev AirCondDev = MyApplication.getWareData().getAirConds().get(j);
                        if (beanBean.getCanCpuID().equals(AirCondDev.getDev().getCanCpuId())
                                && beanBean.getDevID() == AirCondDev.getDev().getDevId()) {
                            int cmdValue = 0, modelValue = 0;
                            if (AirCondDev.getbOnOff() == 0) {
                                cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOn.getValue();//打开空调
                            } else {
                                cmdValue = UdpProPkt.E_AIR_CMD.e_air_pwrOff.getValue();//关闭空调
                            }
                            int value = (modelValue << 5) | cmdValue;
                            SendDataUtil.controlDev(AirCondDev.getDev(), value);
                        }
                    }
                } else if (type_dev == 1) {
                    for (int j = 0; j < MyApplication.getWareData().getTvs().size(); j++) {
                        WareTv tv = MyApplication.getWareData().getTvs().get(j);
                        if (beanBean.getCanCpuID().equals(tv.getDev().getCanCpuId())
                                && beanBean.getDevID() == tv.getDev().getDevId()) {
                            SendDataUtil.controlDev(tv.getDev(), 0);
                        }
                    }
                } else if (type_dev == 2) {
                    for (int j = 0; j < MyApplication.getWareData().getStbs().size(); j++) {
                        WareSetBox box = MyApplication.getWareData().getStbs().get(j);
                        if (beanBean.getCanCpuID().equals(box.getDev().getCanCpuId())
                                && beanBean.getDevID() == box.getDev().getDevId()) {
                            SendDataUtil.controlDev(box.getDev(), 0);
                        }
                    }
                } else if (type_dev == 3) {
                    for (int j = 0; j < MyApplication.getWareData().getLights().size(); j++) {
                        WareLight Light = MyApplication.getWareData().getLights().get(j);
                        if (beanBean.getCanCpuID().equals(Light.getDev().getCanCpuId())
                                && beanBean.getDevID() == Light.getDev().getDevId()) {
                            if (Light.getbOnOff() == 0) {
                                SendDataUtil.controlDev(Light.getDev(), 0);
                            } else {
                                SendDataUtil.controlDev(Light.getDev(), 1);
                            }
                        }
                    }
                } else if (type_dev == 4) {
                    for (int j = 0; j < MyApplication.getWareData().getCurtains().size(); j++) {
                        WareCurtain Curtain = MyApplication.getWareData().getCurtains().get(j);
                        if (beanBean.getCanCpuID().equals(Curtain.getDev().getCanCpuId())
                                && beanBean.getDevID() == Curtain.getDev().getDevId()) {
                            if (Curtain.getbOnOff() == 0) {
                            } else {
                            }
                        }
                    }
                } else if (type_dev == 7) {
                    for (int j = 0; j < MyApplication.getWareData().getFreshAirs().size(); j++) {
                        WareFreshAir freshAir = MyApplication.getWareData().getFreshAirs().get(j);
                        if (beanBean.getCanCpuID().equals(freshAir.getDev().getCanCpuId())
                                && beanBean.getDevID() == freshAir.getDev().getDevId()) {
                            if (freshAir.getbOnOff() == 1) {
                                SendDataUtil.controlDev(freshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_close.getValue());
                            } else {
                                SendDataUtil.controlDev(freshAir.getDev(), UdpProPkt.E_FRESHAIR_CMD.e_freshair_open.getValue());
                            }
                        }
                    }
                } else if (type_dev == 9) {
                    for (int j = 0; j < MyApplication.getWareData().getFloorHeat().size(); j++) {
                        WareFloorHeat floorHeat = MyApplication.getWareData().getFloorHeat().get(j);
                        if (beanBean.getCanCpuID().equals(floorHeat.getDev().getCanCpuId())
                                && beanBean.getDevID() == floorHeat.getDev().getDevId()) {
                            if (floorHeat.getbOnOff() == 1)
                                SendDataUtil.controlDev(floorHeat.getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_close.getValue());
                            else
                                SendDataUtil.controlDev(floorHeat.getDev(), UdpProPkt.E_FLOOR_HEAT_CMD.e_floorHeat_open.getValue());
                        }
                    }
                }
            } else {
                SendDataUtil.executelScene(beanBean.getEventId());
            }
        } else {
            startActivity(new Intent(this, UserAddDevsActivty.class));
        }
    }

    private void getUserData(final Handler handler) {
        MyApplication.mApplication.showLoadDialog(this, false);
        HashMap<String, String> map = new HashMap();
        map.put("userName", (String) AppSharePreferenceMgr.get(GlobalVars.USERID_SHAREPREFERENCE, ""));
        map.put("passwd", (String) AppSharePreferenceMgr.get(GlobalVars.USERPASSWORD_SHAREPREFERENCE, ""));
        map.put("devUnitID", (String) AppSharePreferenceMgr.get(GlobalVars.RCUINFOID_SHAREPREFERENCE, ""));

        OkHttpUtils.postAsyn(NewHttpPort.ROOT + NewHttpPort.LOCATION + NewHttpPort.USER_GET,
                map, new HttpCallback() {
                    @Override
                    public void onSuccess(ResultDesc resultDesc) {
                        super.onSuccess(resultDesc);
                        try {
                            Log.i("UserInterface", "onSuccess: " + resultDesc.getResult()
                            );
                            JSONObject object = new JSONObject(resultDesc.getResult());
                            int code = object.getInt("code");
                            if (code != 0) {
                                ToastUtil.showText(object.getInt("msg"));
                                return;
                            }
                            JSONArray array = object.getJSONArray("data");
                            JSONObject object1 = null;
                            if (array.length() > 0) {
                                object1 = array.getJSONObject(0);
                                Gson gson = new Gson();
                                UserBean userBean = gson.fromJson(object1.toString(), UserBean.class);
                                MyApplication.getWareData().setUserBeen(userBean);
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            } else {
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {
                            Log.i("UserInterface", "Exception: " + e);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Log.i("USER_onFailure", "message: " + code + "--" + message);
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        super.onFailure(code, message);
                    }
                });
    }
}
