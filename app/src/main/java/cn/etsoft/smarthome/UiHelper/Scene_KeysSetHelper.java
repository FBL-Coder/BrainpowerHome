package cn.etsoft.smarthome.UiHelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.ChnOpItem_scene;
import cn.etsoft.smarthome.Utils.GlobalVars;
import cn.etsoft.smarthome.Domain.Out_List_printcmd;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Author：FBL  Time： 2017/7/6.
 * 设备配按键页面   辅助类
 */

public class Scene_KeysSetHelper {


    //解决 fragment隐藏后  房间改变监听失效，显示后， 房间未更新；导致数据错误
    public static String RoomName;
    //所有按键板
    private static List<Out_List_printcmd> listData_all;

    public static String getRoomName() {
        return RoomName;
    }

    public static void setRoomName(String roomName) {
        RoomName = roomName;
    }


    public static List<Out_List_printcmd> getListData_all() {
        if (listData_all == null)
            listData_all = new ArrayList<>();
        return listData_all;
    }

    public static void setListData_all(List<Out_List_printcmd> listData_all) {
        Scene_KeysSetHelper.listData_all = listData_all;
    }

    /**
     * 初始化外部转盘数据
     */
    public static List<CircleDataEvent> initSceneCircleOUterData(boolean IsClick, int position) {

        int SceneSize = MyApplication.getWareData().getSceneEvents().size();
        if (SceneSize == 0)
            return new ArrayList<>();
        int num = 0;
        if (SceneSize <= 2) num = 4;
        else if (SceneSize > 2 && SceneSize < 5) num = 6;
        else num = 8;
        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            CircleDataEvent event = new CircleDataEvent();

            event.setTitle(WareDataHliper.initCopyWareData().getSceneControlData().get(
                    i % WareDataHliper.initCopyWareData().getSceneControlData().size()).getSceneName());
            event.setImage(R.drawable.set_scene);
            if (IsClick && i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
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
            event.setImage(R.mipmap.ic_launcher_round);
            event.setTitle(MyApplication.getWareData().getRooms().get(i % MyApplication.getWareData().getRooms().size()));
            Data_InnerCircleList.add(event);
        }
        return Data_InnerCircleList;
    }

    public static void Save(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示 :");
        builder.setMessage("您要保存这些设置吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChnOpItem_scene scene_Save = WareDataHliper.initCopyWareData().getScenekeysResult();
                scene_Save.setDatType(59);
                scene_Save.setSubType1(0);
                scene_Save.setSubType2(0);
                scene_Save.setDevUnitID(GlobalVars.getDevid());
                scene_Save.setItemCnt(scene_Save.getKey2scene_item().size());
                Gson gson = new Gson();
                MyApplication.mApplication.getUdpServer().send(gson.toJson(scene_Save),59);
                MyApplication.mApplication.showLoadDialog(activity);
            }
        });
        builder.create().show();
    }
}
