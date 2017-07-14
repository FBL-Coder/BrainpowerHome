package cn.etsoft.smarthome.Activity.Settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.GroupSet_DevAdapter;
import cn.etsoft.smarthome.Domain.GroupSet_Data;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.GroupSetHelper;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.View.CircleMenu.CircleDataEvent;
import cn.etsoft.smarthome.View.CircleMenu.CircleMenuLayout;

import static cn.etsoft.smarthome.UiHelper.GroupSetHelper.reverseString;

/**
 * Author：FBL  Time： 2017/6/22.
 * 组合设置页面
 */

public class GroupSetActivity extends BaseActivity implements View.OnClickListener {

    private CircleMenuLayout layout;
    private List<CircleDataEvent> Data_OuterCircleList;
    private TextView mGroupSetSafetys, mGroupSetSaveBtn, mGroupSet_AddDev;
    private EditText mGroupSetName;
    private ImageView mShiNeng, mSyncSever;
    private GridView mGroupSetGirdView;
    private GroupSet_DevAdapter mAdapter;
    private GroupSet_Data.SecsTriggerRowsBean mBean;
    private boolean IsNoData = true;
    private boolean IsCanClick = false;
    private boolean IsOpenShiNeng = false, IsyncSever = false;
    private int mGroupSetPosition = 0;
    private int CirclePosition = 0;

    @Override
    public void initView() {
        setLayout(R.layout.activity_groupset);
        layout = getViewById(R.id.GroupSet_CircleMenu);
        mGroupSetSaveBtn = getViewById(R.id.GroupSet_Save_Btn);
        mShiNeng = getViewById(R.id.GroupSet_ShiNeng);
        mGroupSetSafetys = getViewById(R.id.GroupSet_Safetys);
        mSyncSever = getViewById(R.id.GroupSet_SyncSever);
        mGroupSetName = getViewById(R.id.GroupSet_Name);
        mGroupSetGirdView = getViewById(R.id.GroupSet_GirdView);
        mGroupSet_AddDev = getViewById(R.id.GroupSet_AddDevs);


        mShiNeng.setOnClickListener(this);
        mSyncSever.setOnClickListener(this);
        mGroupSetSafetys.setOnClickListener(this);
        mGroupSetSaveBtn.setOnClickListener(this);
        mGroupSet_AddDev.setOnClickListener(this);
        MyApplication.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int datType, int subtype1, int subtype2) {
                if (datType == 66) {
                    IsNoData = false;
                    WareDataHliper.initCopyWareData().startCopyGroupSetData();
                    initGroupSet();
                }
                if (datType == 67) {
                    ToastUtil.showText("添加成功");
                    WareDataHliper.initCopyWareData().startCopyGroupSetData();
                    initGroupSet();
                }
            }
        });

    }

    @Override
    public void initData() {
        if (WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows().size() == 0) {
            ToastUtil.showText("没有定时器数据");
            return;
        } else {
            initGroupSet();
        }
    }

    private void initGroupSet() {
        Data_OuterCircleList = GroupSetHelper.initSceneCircleOUterData(IsCanClick, CirclePosition);
        layout.Init(200, 0);
        layout.setOuterCircleMenuData(Data_OuterCircleList);

        layout.setOnOuterCircleLayoutClickListener(new CircleMenuLayout.OnOuterCircleLayoutClickListener() {
            @Override
            public void onClickOuterCircle(int position, View view) {
                IsCanClick = true;
                CirclePosition = position;
                mGroupSetPosition = position % WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows().size();
                mBean = WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows()
                        .get(mGroupSetPosition);
                mAdapter = new GroupSet_DevAdapter(mBean.getRun_dev_item(), GroupSetActivity.this);
                mGroupSetGirdView.setAdapter(mAdapter);

                mGroupSetName.setText("");
                mGroupSetName.setHint(mBean.getTriggerName());

                if (mBean.getRun_dev_item() == null
                        || mBean.getRun_dev_item().size() == 0) {
                    mShiNeng.setImageResource(R.drawable.ic_launcher);
                    mSyncSever.setImageResource(R.drawable.ic_launcher);
                    mGroupSetSafetys.setText("点击选择防区");
                } else {
                    if (mBean.getValid() == 1) {
                        IsOpenShiNeng = true;
                        mShiNeng.setImageResource(R.drawable.ic_launcher_round);
                    } else {
                        IsOpenShiNeng = false;
                        mShiNeng.setImageResource(R.drawable.ic_launcher);
                    }
                    int weekSelect_10 = (int) mBean.getTriggerSecs();
                    String weekSelect_2 = reverseString(Integer.toBinaryString(weekSelect_10));
                    String weekSelect_2_data = "";
                    for (int i = 0; i < weekSelect_2.toCharArray().length; i++) {
                        if (weekSelect_2.toCharArray()[i] == '1')
                            weekSelect_2_data += " " + (i + 1);
                    }
                    mGroupSetSafetys.setText("防区集： " + weekSelect_2_data + "");

                    if (mBean.getReportServ() == 1) {
                        mSyncSever.setImageResource(R.drawable.ic_launcher_round);
                        IsyncSever = true;
                    } else {
                        mSyncSever.setImageResource(R.drawable.ic_launcher);
                        IsyncSever = false;
                    }
                }
            }
        });
        mGroupSetGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBean.getRun_dev_item().get(position).getBOnOff() == 0)
                    mBean.getRun_dev_item().get(position).setBOnOff(1);
                else mBean.getRun_dev_item().get(position).setBOnOff(0);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
            }
        });

        mGroupSetGirdView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mBean.getRun_dev_item().remove(position);
                mAdapter.notifyDataSetChanged(mBean.getRun_dev_item());
                return true;
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        if (IsNoData && WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows().size() == 0) {
            ToastUtil.showText("获取数据异常，请稍后在试");
            return;
        }
        if (!IsCanClick) {
            ToastUtil.showText("请先选择触发器！");
            return;
        }
        switch (v.getId()) {
            case R.id.GroupSet_Save_Btn://保存
                mGroupSetName.getText();


                //TODO  保存
                break;
            case R.id.GroupSet_ShiNeng: //使能开关
                IsOpenShiNeng = !IsOpenShiNeng;
                if (IsOpenShiNeng) mShiNeng.setImageResource(R.drawable.ic_launcher_round);
                else mShiNeng.setImageResource(R.drawable.ic_launcher);
                break;
            case R.id.GroupSet_AddDevs: //添加设备
                Intent intent = new Intent(GroupSetActivity.this, SetAddDevActivity.class);
                intent.putExtra("name", "Group");
                intent.putExtra("position", mGroupSetPosition);
                startActivityForResult(intent, 0);

                break;
            case R.id.GroupSet_Safetys:// 选择防区
                if (MyApplication.getWareData().getResult_safety().getSec_info_rows() == null ||
                        MyApplication.getWareData().getResult_safety().getSec_info_rows().size() == 0) {
                    ToastUtil.showText("没有防区数据");
                    return;
                }
                GroupSetHelper.initWeekDialog(this, mGroupSetSafetys);
                break;
            case R.id.GroupSet_SyncSever://同步服务器
                IsyncSever = !IsyncSever;
                if (IsyncSever) mSyncSever.setImageResource(R.drawable.ic_launcher_round);
                else mSyncSever.setImageResource(R.drawable.ic_launcher);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged(WareDataHliper.initCopyWareData().getGroupSetResult().getSecs_trigger_rows()
                .get(mGroupSetPosition).getRun_dev_item());
    }
}
