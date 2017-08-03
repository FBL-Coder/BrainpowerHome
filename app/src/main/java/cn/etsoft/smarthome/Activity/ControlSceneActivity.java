package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Control_Scene_DevAdapter;
import cn.etsoft.smarthome.Domain.WareSceneDevItem;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 定时器页面
 */

public class ControlSceneActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mNull_tv;
    private ImageView mRun;
    private GridView mControlSceneGirdView;
    private Control_Scene_DevAdapter mAdapter;
    private List<WareSceneDevItem> mBean;
    private boolean IsCanClick = false;
    private int mScenePosition = -1;
    private int CirclePosition = -1;


    @Override
    public void initView() {
        setLayout(R.layout.activity_timer);
        layout = getViewById(R.id.Timer_CircleMenu);
        mNull_tv = getViewById(R.id.null_tv);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();
            }
        });

    }

    @Override
    public void initData() {
        initScene();
        if (mScenePosition == -1) {
            mNull_tv.setText("请先选择情景");
        }
    }

    private void initScene() {
        Data_OuterCircleList = initSceneCircleOUterData(CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                mNull_tv.setText("没有设备，可以添加设备");
                CirclePosition = position;
                mScenePosition = position % MyApplication.getWareData().getSceneEvents().size();
                mBean = MyApplication.getWareData().getSceneEvents().get(mScenePosition).getItemAry();
                mAdapter = new Control_Scene_DevAdapter(mBean, ControlSceneActivity.this);
                mControlSceneGirdView.setAdapter(mAdapter);
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (!IsCanClick) {
            ToastUtil.showText("请先选择情景");
            return;
        }
        switch (v.getId()) {
            case R.id.Control_Scene_Run://执行情景
                break;
        }
    }

    public List<CircleDataEvent> initSceneCircleOUterData(int position) {

        List<String> SceneName = new ArrayList<>();
        SceneName.add("")
        if (MyApplication.getWareData().getSceneEvents().size() == 0) {

        }
        List<CircleDataEvent> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setTitle(MyApplication.getWareData().getSceneEvents()
                    .get(i % MyApplication.getWareData().getSceneEvents().size()).getSceneName());
            event.setImage(R.drawable.timer_icon);
            if (i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }
}
