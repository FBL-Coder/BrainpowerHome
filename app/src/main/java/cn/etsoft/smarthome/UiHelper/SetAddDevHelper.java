package cn.etsoft.smarthome.UiHelper;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Domain.WareDev;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Author：FBL  Time： 2017/6/30.
 * 设置界面  添加设备帮助类
 */

public class SetAddDevHelper {

    //解决 fragment隐藏后  房间改变监听失效，显示后， 房间未更新；导致数据错误
    public static String RoomName;
    //解决 添加设备的保存
    public static List<WareDev> addDevs;

    private static int[] images = new int[]{R.drawable.woshi, R.drawable.weishengjian, R.drawable.zoulang, R.drawable.chufang
            , R.drawable.canting, R.drawable.yangtai, R.drawable.yushi};

    public static String getRoomName() {
        return RoomName;
    }

    public static void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public static List<WareDev> getAddDevs() {
        return addDevs;
    }

    public static void setAddDevs(List<WareDev> addDevs) {
        SetAddDevHelper.addDevs = addDevs;
    }


    private static List<WareDev> mDev_Room;
    private static String DEVS_ALL_ROOM = "全部";

    public static List<WareDev> getRoomDev(String roomname) {
        List<WareDev> devs = new ArrayList<>();
        devs.addAll(MyApplication.getWareData().getDevs());
        //房间内的设备集合
        mDev_Room = new ArrayList<>();
        //根据房间id获取设备；
        if (DEVS_ALL_ROOM.equals(roomname)) {
            mDev_Room.addAll(devs);
        } else {
            for (int i = 0; i < devs.size(); i++) {
                if (devs.get(i).getRoomName().equals(roomname)) {
                    mDev_Room.add(devs.get(i));
                }
            }
        }
        return mDev_Room;
    }

    /**
     * 初始化外部转盘数据
     */
    public static List<CircleDataEvent> initSceneCircleOUterData() {
        List<CircleDataEvent> Data_OuterCircleList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            if (i == 0 || i == 8) {
                event.setTitle("空调");
                event.setImage(R.drawable.air_icon);
            }
            if (i == 1 || i == 9) {
                event.setTitle("电视");
                event.setImage(R.drawable.tvup_icon);
            }
            if (i == 2 || i == 10) {
                event.setTitle("机顶盒");
                event.setImage(R.drawable.tvup_icon);
            }
            if (i == 3 || i == 11) {
                event.setTitle("灯光");
                event.setImage(R.drawable.light_icon);
            }
            if (i == 4 || i == 12) {
                event.setTitle("窗帘");
                event.setImage(R.drawable.curtian_icon);
            }
            if (i == 5 || i == 13) {
                event.setTitle("监控");
                event.setImage(R.drawable.jiankong_icon);
            }
            if (i == 6 || i == 14) {
                event.setTitle("门禁");
                event.setImage(R.drawable.mensuo_icon);
            }
            if (i == 7) {
                event.setTitle("插座");
                event.setImage(R.drawable.socket_icon);
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
}
