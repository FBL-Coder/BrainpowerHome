package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Control_Scene_DevAdapter;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.SendDataUtil;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 情景控制页面
 */

public class ControlSceneActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mNull_tv, mRun;
    private GridView mControlSceneGirdView;
    private Control_Scene_DevAdapter mAdapter;
    private boolean IsCanClick = false;
    private int mScenePosition = -1;
    private int CirclePosition = -1;
    private List<WareSceneEvent> mSceneDatas;
    private static int[] images = new int[]{R.drawable.scene_baitian, R.drawable.scene_yejian,
            R.drawable.scene_quankai, R.drawable.scene_quanguan, R.drawable.scene_yongcan,
            R.drawable.scene_xiuxian};


    @Override
    protected void onResume() {
        MyApplication.mApplication.setSceneIsShow(true);
        super.onResume();
    }

    @Override
    public void initView() {
        setLayout(R.layout.activity_controlscene);
        layout = getViewById(R.id.Control_Scene_CircleMenu);
        mNull_tv = getViewById(R.id.null_tv);
        mRun = getViewById(R.id.Control_Scene_Run);
        mControlSceneGirdView = getViewById(R.id.Control_Scene_GirdView);
        mControlSceneGirdView.setEmptyView(mNull_tv);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();
                if (datType == 35) {
                    if (mScenePosition == -1) return;
                    if (mAdapter == null)
                        mAdapter = new Control_Scene_DevAdapter(mSceneDatas.get(mScenePosition).getItemAry(), ControlSceneActivity.this);
                    else
                        mAdapter.notifyDataSetChanged(mSceneDatas.get(mScenePosition).getItemAry());
                    mControlSceneGirdView.setAdapter(mAdapter);
                }
            }
        });
        mSceneDatas = WareDataHliper.initCopyWareData().getSceneControlData();
    }

    @Override
    public void initData() {
        initScene();
        if (mScenePosition == -1) {
            mNull_tv.setText("请先选择情景");
        }
    }

    private void initScene() {
        Data_OuterCircleList = initSceneCircleOUterData(mSceneDatas, CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                mNull_tv.setText("没有设备，通过情景设置添加设备");
                CirclePosition = position;
                mScenePosition = position % mSceneDatas.size();
                if (mAdapter == null)
                    mAdapter = new Control_Scene_DevAdapter(mSceneDatas.get(mScenePosition).getItemAry(), ControlSceneActivity.this);
                else mAdapter.notifyDataSetChanged(mSceneDatas.get(mScenePosition).getItemAry());
                mAdapter = new Control_Scene_DevAdapter(mSceneDatas.get(mScenePosition).getItemAry(), ControlSceneActivity.this);
                mControlSceneGirdView.setAdapter(mAdapter);
            }
        });

        mRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScenePosition == -1) {
                    ToastUtil.showText("请选择情景");
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ControlSceneActivity.this);
                builder.setTitle("提示");
                builder.setMessage("您是否启用此情景？");
                builder.setPositiveButton("启用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SendDataUtil.executelScene(mSceneDatas.get(mScenePosition).getEventId());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();

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

    public List<CircleDataEvent> initSceneCircleOUterData(List<WareSceneEvent> mlist, int position) {
        List<CircleDataEvent> list = new ArrayList<>();
        int num = 0;
        if (mlist.size() <= 2) num = 4;
        else if (mlist.size() > 2 && mlist.size() < 5) num = 6;
        else num = 10;
        for (int i = 0; i < num; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setTitle(mlist.get(i % mlist.size()).getSceneName());
            if (event.getTitle().contains("白"))
                event.setImage(R.drawable.scene_baitian);
            else if (event.getTitle().contains("夜"))
                event.setImage(R.drawable.scene_yejian);
            else if (event.getTitle().contains("客"))
                event.setImage(R.drawable.scene_huike);
            else if (event.getTitle().contains("休"))
                event.setImage(R.drawable.scene_xiuxian);
            else if (event.getTitle().contains("全开"))
                event.setImage(R.drawable.scene_quankai);
            else if (event.getTitle().contains("全关"))
                event.setImage(R.drawable.scene_quanguan);
            else if (event.getTitle().contains("用餐"))
                event.setImage(R.drawable.scene_yongcan);
            else {
                try {
                    event.setImage(images[i % mlist.size()]);
                } catch (Exception e) {
                    event.setImage(images[0]);
                }
            }
            if (i == position)
                event.setSelect(true);
            list.add(event);
        }
        return list;
    }

    @Override
    protected void onStop() {
        MyApplication.mApplication.setSceneIsShow(false);
        super.onStop();
    }
}
