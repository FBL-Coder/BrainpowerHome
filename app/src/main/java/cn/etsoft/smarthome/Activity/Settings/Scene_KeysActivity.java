package cn.etsoft.smarthome.Activity.Settings;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Scene_KeysSet_KeysAdapter;
import cn.etsoft.smarthome.Adapter.RecyclerView.Scene_KeysSet_BoardAdapter;
import cn.etsoft.smarthome.Domain.ChnOpItem_scene;
import cn.etsoft.smarthome.Domain.WareBoardKeyInput;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.Scene_KeysSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

/**
 * Author：FBL  Time： 2017/6/22.
 * 情景配按键 页面 ——按键情景页面
 */

public class Scene_KeysActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private RecyclerView mSceneKeys_Boards;
    private GridView mSceneKeys_Keys;
    private TextView mSceneKeys_TestBtn, mSceneKeys_SaveBtn;
    //设备适配器
    private Scene_KeysSet_BoardAdapter mBoardAdapter;
    //按键适配器
    private Scene_KeysSet_KeysAdapter mKeysAdapter;

    //按键板数据
    private List<WareBoardKeyInput> wareBoardKeyInputs;
    //情景数据
    private List<WareSceneEvent> mSceneS;
    //情景位置
    private int position_keyinput = 0, Scene_ID = -1;

    @Override
    public void initView() {
        setLayout(R.layout.activity_scene_keys_set);

        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                MyApplication.mApplication.dismissLoadDialog();
                if (datType == 58 && MyApplication.getWareData().getChnOpItem_scene().getSubType1() == 1) {
                    WareDataHliper.initCopyWareData().startCopyScene_KeysData();
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
        Data_OuterCircleList = Scene_KeysSetHelper.initSceneCircleOUterData(true, 0);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);
        initEvent();
        mSceneKeys_Keys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isCantain = false;
                for (int i = 0; i < WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size(); i++) {
                    ChnOpItem_scene.Key2sceneItemBean itemBean = WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().get(i);
                    if (itemBean.getEventId() == Scene_ID) {
                        itemBean.setEventId(Scene_ID);
                        itemBean.setKeyUId(wareBoardKeyInputs.get(position_keyinput).getCanCpuID());
                        itemBean.setKeyIndex(position);
                        WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().set(i,itemBean);
                        isCantain = true;
                    }
                }
                if (!isCantain) {
                    ChnOpItem_scene.Key2sceneItemBean itemBean = new ChnOpItem_scene.Key2sceneItemBean();
                    itemBean.setEventId(Scene_ID);
                    itemBean.setKeyUId(wareBoardKeyInputs.get(position_keyinput).getCanCpuID());
                    itemBean.setKeyIndex(position);
                    WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().add(itemBean);
                }
                mKeysAdapter.notifyDataSetChanged(Scene_ID, position_keyinput, Scene_KeysActivity.this, false);
            }
        });
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
        if (WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size() == 0) {
            ToastUtil.showText("数据未加载成功，不可操作！");
            return;
        }
        if (Scene_ID == -1) {
            ToastUtil.showText("请选择情景");
            return;
        }
        switch (v.getId()) {
            case R.id.Scene_KeysSet_Test_Btn: // 测试
                break;
            case R.id.Scene_KeysSet_Save_Btn: // 保存
                if (WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size() > 12) {
                    ToastUtil.showText("最多12个关联事件，目前已有" + WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size() + "个;");
                    return;
                }
                Scene_KeysSetHelper.Save(this);
                break;
        }
    }

    private void initEvent() {
        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                if (WareDataHliper.initCopyWareData().getScenekeysResult().getKey2scene_item().size() == 0) {
                    ToastUtil.showText("数据未加载成功，不可操作！");
                    return;
                }
                if (MyApplication.getWareData().getSceneEvents().size() == 0) {
                    ToastUtil.showText("情景数据加载失败，不可操作！");
                    return;
                }
                Scene_ID = position % WareDataHliper.initCopyWareData().getSceneControlData().size();
                // 转盘点击事件
                initKeyAdapter();
            }
        });
    }

    private void RecyclerViewClick() {
        mBoardAdapter.setOnItemClick(new Scene_KeysSet_BoardAdapter.Scene_KeysSetViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                position_keyinput = position;
                //板子点击事件
                mKeysAdapter.notifyDataSetChanged(Scene_ID, position_keyinput, Scene_KeysActivity.this, false);
            }

            @Override
            public void OnItemLongClick(View view, final int position) {

            }
        });
    }
}
