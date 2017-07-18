package cn.etsoft.smarthome.Activity.Settings;

import android.view.View;
import android.widget.GridView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.SceneSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设置情景页面
 */

public class DevInfoActivity extends BaseActivity {

    private CircleMenuLayout layout;
    private GridView DevInfoGridView;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private int DevType = 0;
    private String RoomName = "";

    @Override
    public void initView() {
        setLayout(R.layout.activity_devinfo);
        DevInfoGridView = getViewById(R.id.DevInfo_Info);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();
                // TODO  数据返回处理
            }
        });
    }

    @Override
    public void initData() {
        WareDataHliper.initCopyWareData().startCopySceneData();
        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        layout = getViewById(R.id.DevInfo_CircleMenu);
        Data_OuterCircleList = SceneSetHelper.initSceneCircleOUterData();
        Data_InnerCircleList = SceneSetHelper.initSceneCircleInnerData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);
        initEvent();
    }


    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {
                RoomName = Data_InnerCircleList.get(position).getTitle();
                SceneSetHelper.setRoomName(RoomName);
                //内圈点击
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                DevType = position % 8;
                //TODO  外圈点击事件
            }
        });
    }


}
