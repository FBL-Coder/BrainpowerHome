package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.CommonUtils;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Author：FBL  Time： 2017/6/28.
 * 情景页面设置界面 辅助类
 */

public class SceneSetHelper {

    //解决 fragment隐藏后  房间改变监听失效，显示后， 房间未更新；导致数据错误
    public static String RoomName;

    public static String getRoomName() {
        return RoomName;
    }

    public static void setRoomName(String roomName) {
        RoomName = roomName;
    }

    private static int[] images = new int[]{R.drawable.woshi, R.drawable.weishengjian, R.drawable.zoulang, R.drawable.chufang
            , R.drawable.canting, R.drawable.yangtai, R.drawable.yushi};

    /**
     * 初始化外部转盘数据
     */
    public static List<CircleDataEvent> initSceneCircleOUterData() {
        List<CircleDataEvent> Data_OuterCircleList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();

            if (i == 0 || i == 10) {
                event.setTitle("空调");
                event.setImage(R.drawable.air_icon);
            }
            if (i == 1 || i == 11) {
                event.setTitle("电视");
                event.setImage(R.drawable.tv_icon);
            }
            if (i == 2 || i == 12) {
                event.setTitle("机顶盒");
                event.setImage(R.drawable.tvup_icon);
            }
            if (i == 3 || i == 13) {
                event.setTitle("灯光");
                event.setImage(R.drawable.light_icon);
            }
            if (i == 4 || i == 14) {
                event.setTitle("窗帘");
                event.setImage(R.drawable.curtian_icon);
            }
            if (i == 5) {
                event.setTitle("监控");
                event.setImage(R.drawable.jiankong_icon);
            }
            if (i == 6) {
                event.setTitle("门禁");
                event.setImage(R.drawable.mensuo_icon);
            }
            if (i == 7) {
                event.setTitle("新风");
                event.setImage(R.drawable.ic_launcher);
            }
            if (i == 8) {
                event.setTitle("插座");
                event.setImage(R.drawable.socket_icon);
            }
            if (i == 9) {
                event.setTitle("地暖");
                event.setImage(R.drawable.ic_launcher);
            }
            Data_OuterCircleList.add(event);
        }
        return Data_OuterCircleList;
    }

    /**
     * 初始化内部转盘数据
     */
    public static List<CircleDataEvent> initSceneCircleInnerData() {
        List<CircleDataEvent> Data_InnerCircleList = new ArrayList<>();
        switch (MyApplication.getWareData().getRooms().size()) {
            case 1:
                Data_InnerCircleList = initRoom(4, Data_InnerCircleList);
                break;
            case 2:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
            case 3:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
            case 4:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                Data_InnerCircleList = initRoom(8, Data_InnerCircleList);
                break;
        }
        return Data_InnerCircleList;
    }

    /**
     * 初始化转盘房间数据
     */
    public static List<CircleDataEvent> initRoom(int maxsize, List<CircleDataEvent> Data_InnerCircleList) {
        Data_InnerCircleList.clear();
        for (int i = 0; i < maxsize; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setTitle(MyApplication.getWareData().getRooms().get(i % MyApplication.getWareData().getRooms().size()));
            if (event.getTitle().contains("卧"))
                event.setImage(R.drawable.woshi);
            else if (event.getTitle().contains("厨"))
                event.setImage(R.drawable.chufang);
            else if (event.getTitle().contains("餐"))
                event.setImage(R.drawable.canting);
            else if (event.getTitle().contains("卫") || event.getTitle().contains("厕"))
                event.setImage(R.drawable.weishengjian);
            else if (event.getTitle().contains("阳"))
                event.setImage(R.drawable.yangtai);
            else if (event.getTitle().contains("客") || event.getTitle().contains("大厅"))
                event.setImage(R.drawable.keting);
            else if (event.getTitle().contains("走") || event.getTitle().contains("过道"))
                event.setImage(R.drawable.zoulang);
            else {
                event.setImage(images[i % images.length]);
            }
            Data_InnerCircleList.add(event);
        }
        return Data_InnerCircleList;
    }

    public static void AddScene(final Activity activity) {
        final Dialog dialog = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        dialog.setContentView(R.layout.dialog_addscene);
        dialog.show();
        final EditText mDialogName = (EditText) dialog.findViewById(R.id.dialog_addScene_name);
        TextView mDialogCancle = (TextView) dialog.findViewById(R.id.dialog_addScene_cancle);
        TextView mDialogOk = (TextView) dialog.findViewById(R.id.dialog_addScene_ok);
        mDialogCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AddSceneSenddata(mDialogName, activity);
            }
        });
    }

    /**
     * 添加情景模式
     */
    public static void AddSceneSenddata(EditText name, Activity activity) {

        String data = name.getText().toString();
        if (!"".equals(data)) {
            MyApplication.mApplication.showLoadDialog(activity);
            //查询可用ID
            List<Integer> Scene_int = new ArrayList<>();
            for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                Scene_int.add((int) MyApplication.getWareData().getSceneEvents().get(i).getEventId());
            }
            List<Integer> Scene_id = new ArrayList<>();
            for (int i = 2; i < 8; i++) {
                Scene_id.add(i);
            }
            List<Integer> ID = new ArrayList<>();
            for (int i = 0; i < Scene_id.size(); i++) {
                if (!Scene_int.contains(Scene_id.get(i))) {
                    ID.add(Scene_id.get(i));
                }
            }
            if (ID.size() == 0) {
                ToastUtil.showText("自定义情景最多6个");
                return;
            }
            SendDataUtil.addscene(ID.get(0), data);
        } else {
            ToastUtil.showText("请填写情景名称");
        }
    }


    /**
     * 保存情景设置；
     */
    public static void saveScene(final Activity activity, int ScenePosition) {


        final int sceneid = WareDataHliper.wareDataHliper.getCopyScenes().get(ScenePosition).getEventId();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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
                MyApplication.mApplication.showLoadDialog(activity);
                WareSceneEvent Sceneevent = null;
                try {
                    for (int i = 0; i < WareDataHliper.wareDataHliper.getCopyScenes().size(); i++) {
                        if (sceneid == WareDataHliper.wareDataHliper.getCopyScenes().get(i).getEventId()) {
                            Sceneevent = WareDataHliper.wareDataHliper.getCopyScenes().get(i);
                            break;
                        }
                    }
                    String div;
                    String more_data = "";
                    String data_str = "";
                    div = ",";
                    for (int j = 0; j < Sceneevent.getItemAry().size(); j++) {
                        data_str = "{" +
                                "\"canCpuID\":\"" + Sceneevent.getItemAry().get(j).getCanCpuID() + "\"," +
                                "\"devType\":" + Sceneevent.getItemAry().get(j).getDevType() + "," +
                                "\"devID\":" + Sceneevent.getItemAry().get(j).getDevID() + "," +
                                "\"bOnOff\":" + Sceneevent.getItemAry().get(j).getbOnOff() + "," +
                                "\"lmVal\":0," +
                                "\"rev2\":0," +
                                "\"rev3\":0," +
                                "\"param1\":0," +
                                "\"param2\":0}" + div;
                        more_data += data_str;
                    }
                    byte[] nameData = {0};
                    try {
                        nameData = Sceneevent.getSceneName().getBytes("GB2312");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String str_gb = CommonUtils.bytesToHexString(nameData);
                    Log.e("情景模式名称:%s", str_gb);
                    try {
                        more_data = more_data.substring(0, more_data.lastIndexOf(","));
                    } catch (Exception e) {
                        MyApplication.mApplication.dismissLoadDialog();
                        System.out.println(e + "");
                    }
                    //这就是要上传的字符串:data_hoad
                    String data_hoad = "{" +
                            "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                            "\"sceneName\":\"" + str_gb + "\"," +
                            "\"datType\":24" + "," +
                            "\"subType1\":0" + "," +
                            "\"subType2\":0" + "," +
                            "\"eventId\":" + Sceneevent.getEventId() + "," +
                            "\"devCnt\":" + Sceneevent.getItemAry().size() + "," +
                            "\"itemAry\":[" + more_data + "]}";
                    Log.e("情景模式测试:", data_hoad);
                    MyApplication.mApplication.getUdpServer().send(data_hoad);
                } catch (Exception e) {
                    MyApplication.mApplication.dismissLoadDialog();
                    Log.e("Exception", e + "");
                    ToastUtil.showText("保存失败，数据异常");
                }
            }
        });
        builder.create().show();
    }

}
