package cn.etsoft.smarthome.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.util.List;

import cn.etsoft.smarthome.Adapter.GridView.Control_Scene_DevAdapter;
import cn.etsoft.smarthome.Domain.WareSceneEvent;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.UiHelper.WareDataHliper;
import cn.etsoft.smarthome.Utils.SendDataUtil;

/**
 * Author：FBL  Time： 2017/6/22.
 * 情景控制页面
 */

public class ControlSceneActivity extends BaseActivity {

    private GridView mControlSceneGirdView;
    private ImageView Control_Back;
    private Control_Scene_DevAdapter mAdapter;
    private List<WareSceneEvent> mSceneDatas;

    @Override
    public void initView() {
        setLayout(R.layout.activity_controlscene);
        Control_Back = getViewById(R.id.Control_Back);
        mControlSceneGirdView = getViewById(R.id.Control_Scene_GirdView);
        Control_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSceneDatas = WareDataHliper.initCopyWareData().getSceneControlData();
    }

    @Override
    public void initData() {
        initScene();
    }

    private void initScene() {
        if (mAdapter == null)
            mAdapter = new Control_Scene_DevAdapter(ControlSceneActivity.this);
        else mAdapter.notifyDataSetChanged();
        mAdapter = new Control_Scene_DevAdapter(ControlSceneActivity.this);
        mControlSceneGirdView.setAdapter(mAdapter);

        mControlSceneGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ControlSceneActivity.this);
                builder.setTitle("提示");
                builder.setMessage("您是否启用\"" + WareDataHliper.initCopyWareData().getCopyScenes().get(i).getSceneName() + "\"情景？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialogInterface.dismiss();
                        SendDataUtil.executelScene(mSceneDatas.get(i).getEventId());
                        ToastUtil.showText("正在执行情景");
                    }
                });
                builder.create().show();
            }
        });
    }
}
