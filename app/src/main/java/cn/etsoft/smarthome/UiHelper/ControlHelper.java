package cn.etsoft.smarthome.UiHelper;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;

/**
 * Author：FBL  Time： 2017/6/28.
 * 情景页面设置界面 辅助类
 */

public class ControlHelper {

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
        for (int i = 0; i < 10; i++) {
            CircleDataEvent event = new CircleDataEvent();
            if (i == 0) {
                event.setTitle("空调");
                event.setImage(R.drawable.air_icon);
            } else if (i == 1) {
                event.setTitle("灯光");
                event.setImage(R.drawable.light_icon);
                event.setSelect(true);
            } else if (i == 2) {
                event.setTitle("窗帘");
                event.setImage(R.drawable.curtian_icon);
            } else if (i == 3) {
                event.setTitle("新风");
                event.setImage(R.drawable.freshair);
            } else if (i == 4) {
                event.setTitle("地暖");
                event.setImage(R.drawable.floorheat);
            } else if (i == 5) {
                event.setTitle("电视");
                event.setImage(R.drawable.tv_icon);
            } else if (i == 6) {
                event.setTitle("插座");
                event.setImage(R.drawable.socket_icon);
            } else if (i == 7) {
                event.setTitle("机顶盒");
                event.setImage(R.drawable.tvup_icon);
            } else if (i == 8) {
                event.setTitle("监控");
                event.setImage(R.drawable.jiankong_icon);
            } else if (i == 9) {
                event.setTitle("门禁");
                event.setImage(R.drawable.mensuo_icon);
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
        Data_InnerCircleList = initRoom(MyApplication.getWareData().getRooms().size(), Data_InnerCircleList);
        return Data_InnerCircleList;
    }

    /**
     * 初始化转盘房间数据
     */
    public static List<CircleDataEvent> initRoom(int maxsize, List<CircleDataEvent> Data_InnerCircleList) {
        Data_InnerCircleList.clear();
        CircleDataEvent event_1 = new CircleDataEvent();
        event_1.setImage(R.drawable.quanbu);
        event_1.setTitle("全部");
        event_1.setSelect(true);
        Data_InnerCircleList.add(event_1);
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
