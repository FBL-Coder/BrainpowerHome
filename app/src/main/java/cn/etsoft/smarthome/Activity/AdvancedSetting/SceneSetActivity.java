package cn.etsoft.smarthome.Activity.AdvancedSetting;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.Fragment;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.Adapter.RecyclerView.SceneSet_ScenesAdapter;
import cn.etsoft.smarthome.Fragment.SceneSet.LightSceneFragment;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 设置情景页面
 */

public class SceneSetActivity extends BaseActivity {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private LinearLayout mSceneSet_LinearLayout;
    private RecyclerView mSceneSetScenes;
    private TextView mSceneSetTestBtn, mSceneSetSaveBtn;
    private LinearLayout mSceneSetInfo;
    private SceneSet_ScenesAdapter mScenesAdapter;

    private LightSceneFragment mLightFragment;

    private int ScenePosition, DevType, RoomPosition;

    @Override
    public void initView() {
        setLayout(R.layout.activity_sceneset);

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 22) {
                    initData();
                }
            }
        });
        layout = getViewById(R.id.SceneSet_CircleMenu);
        mSceneSet_LinearLayout = getViewById(R.id.SceneSet_LinearLayout);

        mSceneSetScenes = getViewById(R.id.SceneSet_Scenes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSceneSetScenes.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public void initData() {

        if (MyApplication.getWareData().getRooms().size() == 0) {
            ToastUtil.showText("没有房间数据");
            return;
        }
        initCircleData();
        layout.Init(200, 100);
        layout.setInnerCircleMenuData(Data_InnerCircleList);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        mScenesAdapter = new SceneSet_ScenesAdapter(MyApplication.getWareData().getSceneEvents());
        mSceneSetScenes.setAdapter(mScenesAdapter);

        initEvent();
    }

    private void initEvent() {
        layout.setOnInnerCircleLayoutClickListener(new CircleMenuLayout.OnInnerCircleLayoutClickListener() {
            @Override
            public void onClickInnerCircle(int position, View view) {


                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(ScenePosition, DevType, position);


                Toast.makeText(SceneSetActivity.this, Data_InnerCircleList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction =  manager.beginTransaction();

                if (mLightFragment != null){
                    transaction.hide(mLightFragment);
                }

                switch (position){
                    case 0:
                    case 8:
                        break;
                    case 1:
                    case 9:
                        break;
                    case 2:
                    case 10:
                        if (mLightFragment == null){
                            mLightFragment =  new LightSceneFragment();
                            transaction.add(R.id.SceneSet_Info,mLightFragment);
                        }
                        transaction.show(mLightFragment);
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
                transaction.commit();

                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(ScenePosition, position, RoomPosition);
                Toast.makeText(SceneSetActivity.this, Data_OuterCircleList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        mScenesAdapter.setOnItemClick(new SceneSet_ScenesAdapter.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                if (mSceneSetSceneClickListener != null)
                    mSceneSetSceneClickListener.SceneClickPosition(position, DevType, RoomPosition);
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 初始化转盘数据
     */
    private void initCircleData() {
        Data_OuterCircleList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            if (i == 0 || i == 8) event.setTitle("空调");
            if (i == 1 || i == 9) event.setTitle("电视");
            if (i == 2 || i == 10) event.setTitle("机顶盒");
            if (i == 3 || i == 11) event.setTitle("灯光");
            if (i == 4 || i == 12) event.setTitle("门禁");
            if (i == 5 || i == 13) event.setTitle("监控");
            if (i == 6 || i == 14) event.setTitle("窗帘");
            if (i == 7) event.setTitle("插座");
            Data_OuterCircleList.add(event);
        }
        Data_InnerCircleList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CircleDataEvent event = new CircleDataEvent();
            event.setImage(R.mipmap.ic_launcher_round);
            if (i == 0 || i == 8) event.setTitle("客厅");
            if (i == 1 || i == 9) event.setTitle("厨房");
            if (i == 2 || i == 10) event.setTitle("卫生间");
            if (i == 3 || i == 11) event.setTitle("卧室");
            if (i == 4 || i == 12) event.setTitle("走廊");
            if (i == 5 || i == 13) event.setTitle("阳台");
            if (i == 6 || i == 14) event.setTitle("餐厅");
            if (i == 7) event.setTitle("楼梯");
            Data_InnerCircleList.add(event);
        }
    }


    //情景Position
    public static SceneSetSceneClickListener mSceneSetSceneClickListener;

    public static void setmSceneSetSceneClickListener(SceneSetSceneClickListener mSceneSetSceneClickListener) {
        SceneSetActivity.mSceneSetSceneClickListener = mSceneSetSceneClickListener;
    }

    public interface SceneSetSceneClickListener {
        void SceneClickPosition(int ScenePosition, int DevType, int RoomPositon);
    }
}
