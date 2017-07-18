package cn.etsoft.smarthome.Activity.Settings;

import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;
import com.google.gson.Gson;

import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Scene_KeysSet_KeysAdapter;
import cn.etsoft.smarthome.Adapter.RecyclerView.Scene_KeysSet_BoardAdapter;
import cn.etsoft.smarthome.Domain.WareBoardKeyInput;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Scene_KeysSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;
import cn.etsoft.smarthome.View.SlideGridView;

/**
 * Author：FBL  Time： 2017/6/22.
 * 情景配按键 页面 ——按键情景页面
 */

public class Scene_KeysActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private RecyclerView mSceneKeys_Boards;
    private SlideGridView mSceneKeys_Keys;
    private TextView mSceneKeys_TestBtn, mSceneKeys_SaveBtn;
    //设备适配器
    private Scene_KeysSet_BoardAdapter mBoardAdapter;
    //按键适配器
    private Scene_KeysSet_KeysAdapter mKeysAdapter;
    private boolean IsNoData = true;

    //按键板数据
    private List<WareBoardKeyInput> wareBoardKeyInputs;
    //情景数据
    private List<WareSceneEvent> mSceneS;
    //情景位置
    private int position_keyinput = 0, Scene_ID = 0;

    @Override
    public void initView() {
        setLayout(R.layout.activity_scene_keys_set);

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();

                if (datType == 58 && MyApplication.getWareData().getChnOpItem_scene().getSubType1() == 1) {
                    WareDataHliper.initCopyWareData().startCopyScene_KeysData();
                    IsNoData = false;
                    initKeyAdapter();
                }

                if (datType == 59 && MyApplication.getWareData().getResult() != null && MyApplication.getWareData().getResult().getSubType1() == 1) {
                    ToastUtil.showText("保存成功");
                }
            }
        });

        mSceneKeys_TestBtn = getViewById(R.id.Scene_KeysSet_Test_Btn);
        mSceneKeys_SaveBtn = getViewById(R.id.Scene_KeysSet_Save_Btn);
        mSceneKeys_Keys = getViewById(R.id.Scene_KeysSet_Keys);

        mSceneKeys_TestBtn.setOnClickListener(this);
        mSceneKeys_SaveBtn.setOnClickListener(this);

        mSceneKeys_Boards = getViewById(R.id.Scene_KeysSet_Devs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSceneKeys_Boards.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    @Override
    public void initData() {

        wareBoardKeyInputs = MyApplication.getWareData().getKeyInputs();
        if (wareBoardKeyInputs.size() == 0) {
            ToastUtil.showText("没有输入板信息");
            return;
        }
        //输入板适配器
        if (mBoardAdapter == null)
            mBoardAdapter = new Scene_KeysSet_BoardAdapter(wareBoardKeyInputs);
        else
            mBoardAdapter.upData(wareBoardKeyInputs);
        mSceneKeys_Boards.setAdapter(mBoardAdapter);
        RecyclerViewClick();

        initKeyAdapter();
        //转盘数据适配
        layout = getViewById(R.id.Scene_KeysSet_CircleMenu);
        Data_OuterCircleList = Scene_KeysSetHelper.initSceneCircleOUterData(false, 0);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        initEvent();
    }

    private void initKeyAdapter() {
        //按键适配器
        if (WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size() != 0) {
            if (mKeysAdapter == null)
                mKeysAdapter = new Scene_KeysSet_KeysAdapter(Scene_ID, position_keyinput, this, false);
            else mKeysAdapter.notifyDataSetChanged(Scene_ID, position_keyinput, this, false);
            mSceneKeys_Keys.setAdapter(mKeysAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (IsNoData) {
            ToastUtil.showText("数据未加载成功，不可操作！");
            return;
        }
        switch (v.getId()) {
            case R.id.Scene_KeysSet_Test_Btn: // 测试
                break;
            case R.id.Scene_KeysSet_Save_Btn: // 保存
                Gson gson = new Gson();
                WareDataHliper.initCopyWareData().getScenekeysResult().setDatType(59);
                WareDataHliper.initCopyWareData().getScenekeysResult().setSubType1(0);
                WareDataHliper.initCopyWareData().getScenekeysResult().setSubType2(0);
                WareDataHliper.initCopyWareData().getScenekeysResult().setItemCnt(WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size());
                gson.toJson(WareDataHliper.initCopyWareData().getScenekeysResult());
//                Log.i("WareDataHliper", "onClick: " + gson.toJson(WareDataHliper.initCopyWareData().getScenekeysResult()));
                MyApplication.mApplication.getUdpServer().send(gson.toJson(WareDataHliper.initCopyWareData().getScenekeysResult()));
                break;
        }
    }

    private void initEvent() {
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                if (IsNoData) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                Scene_ID = MyApplication.getWareData().getSceneEvents().
                        get(position % MyApplication.getWareData().getSceneEvents().size()).getEventId();
                // TODO  转盘点击事件
                initKeyAdapter();
            }
        });
    }

    private void RecyclerViewClick() {
        mBoardAdapter.setOnItemClick(new Scene_KeysSet_BoardAdapter.Scene_KeysSetViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                position_keyinput = position;
                //TODO  板子点击事件
            }

            @Override
            public void OnItemLongClick(View view, final int position) {

            }
        });
    }
}
