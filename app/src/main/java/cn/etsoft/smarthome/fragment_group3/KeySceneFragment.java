package cn.etsoft.smarthome.fragment_group3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter_group3.RecyclerViewAdapter_equip;
import cn.etsoft.smarthome.adapter_group3.RecyclerViewAdapter_keyScene;
import cn.etsoft.smarthome.domain.ChnOpItem_scene;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.entity.WareData;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.utils.Dtat_Cache;
import cn.etsoft.smarthome.utils.ToastUtil;
import cn.etsoft.smarthome.view.Circle_Progress;
import cn.etsoft.smarthome.widget.CustomDialog_comment;
import cn.etsoft.smarthome.widget.SpacesItemDecoration;

/**
 * Created by Say GoBay on 2017/6/15.
 */
public class KeySceneFragment extends Fragment implements View.OnClickListener{

    private FragmentActivity mActivity;
    private View view_parent;
    private Dialog mDialog;
    private Handler handler;
    private android.support.v7.widget.RecyclerView RecyclerView_scene, RecyclerView_key;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Button input_save;
    private ImageView input_choose;
    private List<WareSceneEvent> event;
    private RecyclerViewAdapter_keyScene recyclerAdapter;
    private RecyclerViewAdapter_equip recyclerAdapter_equip;
    private List<String> input_name;
    private Fragment keySceneFragment_key;
    private boolean ISCHOOSE = false;
    private byte sceneid = 0;
    private int position_keyinput = 0;
    private ChnOpItem_scene listData_all;
    private boolean IsHaveData = false;

    public KeySceneFragment(FragmentActivity activity) {
        mActivity = activity;
    }
    //自定义加载进度条
    private void initDialog(String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(mActivity);
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (mDialog.isShowing()) {
                        handler.sendMessage(handler.obtainMessage());
                    }
                } catch (Exception e) {
                    System.out.println(e + "");
                }
            }
        }).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_parent = inflater.inflate(R.layout.fragment_key_scene3, container, false);
        RecyclerView_scene = (android.support.v7.widget.RecyclerView) view_parent.findViewById(R.id.RecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mActivity);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerView_scene.setLayoutManager(layoutManager1);
        RecyclerView_key = (android.support.v7.widget.RecyclerView) view_parent.findViewById(R.id.RecyclerView_equip);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mActivity);
        RecyclerView_key.setLayoutManager(layoutManager2);
        RecyclerView_key.addItemDecoration(new SpacesItemDecoration(10));
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        //通知数据更新
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                if (mDialog != null)
                    mDialog.dismiss();

                if (what == 58 && MyApplication.getWareData().getChnOpItem_scene().getSubType1() == 1) {
                    IsHaveData = true;
                    listData_all = MyApplication.getWareData().getChnOpItem_scene();
                    MyApplication.mInstance.setKey_scene_data(listData_all);
                    onGetKeySceneDataListeener.getKeySceneData();
                }

                if (what == 59 && MyApplication.getWareData().getResult() != null && MyApplication.getWareData().getResult().getSubType1() == 1) {
                    Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mDialog != null)
                    mDialog.dismiss();
                //初始化控件
                initView(view_parent);
                //初始化情景的RecycleView
                initRecycleView_Scene();
                //初始化输入板的RecycleView
                initRecyclerView_input();
            }
        };
        initDialog("正在加载...");
        ReadWrite();
        return view_parent;
    }

    //复写数据
    private void ReadWrite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Dtat_Cache.writeFile(GlobalVars.getDevid(), MyApplication.getWareData());
                MyApplication.setWareData_Scene((WareData) Dtat_Cache.readFile(GlobalVars.getDevid()));
                handler.sendMessage(handler.obtainMessage());
            }
        }).start();
    }
    /**
     * 初始化控件
     */
    private void initView(View view) {
        fragmentManager = mActivity.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        input_save = (Button) view.findViewById(R.id.input_save);
        input_choose = (ImageView) view.findViewById(R.id.input_choose);
        input_save.setOnClickListener(this);
        input_choose.setOnClickListener(this);
        event = new ArrayList<>();
    }

    /**
     * 初始化情景的ListView
     */
    private void initRecycleView_Scene() {
        event.clear();
        for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
            event.add(MyApplication.getWareData().getSceneEvents().get(i));
        }
        recyclerAdapter = new RecyclerViewAdapter_keyScene(mActivity, event);
        RecyclerView_scene.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClick(new RecyclerViewAdapter_keyScene.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                int listSize = event.size();
                if (listSize > 0) {
                    sceneid = MyApplication.getWareData().getSceneEvents().get(position).getEventld();
                    transaction = mActivity.getSupportFragmentManager().beginTransaction();
                    keySceneFragment_key = new KeySceneFragment_key();
                    Bundle bundle = new Bundle();
                    bundle.putInt("sceneid", sceneid);
                    bundle.putInt("keyinput_position", position_keyinput);
                    bundle.putBoolean("ISCHOOSE", ISCHOOSE);
                    keySceneFragment_key.setArguments(bundle);
                    transaction.replace(R.id.home, keySceneFragment_key);
                    transaction.commit();
                } else {
                    ToastUtil.showToast(mActivity, "数据异常");
                }
            }

            @Override
            public void OnItemLongClick(View view, int position) {
            }
        });
    }
    /**
     * 初始化设备
     */
    private void initRecyclerView_input() {
        if (MyApplication.getWareData().getKeyInputs().size() == 0) {
            ToastUtil.showToast(mActivity, "没有收到输入板数据");
            return;
        }
        input_name = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getKeyInputs().size(); i++) {
            input_name.add(MyApplication.getWareData().getKeyInputs().get(i).getBoardName());
        }
        recyclerAdapter_equip = new RecyclerViewAdapter_equip(input_name);
        RecyclerView_key.setAdapter(recyclerAdapter_equip);
        transaction = mActivity.getSupportFragmentManager().beginTransaction();
        keySceneFragment_key = new KeySceneFragment_key();
        Bundle bundle = new Bundle();
        bundle.putInt("sceneid", 0);
        bundle.putInt("keyinput_position", 0);
        bundle.putBoolean("ISCHOOSE", ISCHOOSE);
        keySceneFragment_key.setArguments(bundle);
        transaction.replace(R.id.home, keySceneFragment_key);
        transaction.commit();
        recyclerAdapter_equip.setOnItemClick(new RecyclerViewAdapter_equip.SceneViewHolder.OnItemClick() {
            @Override
            public void OnItemClick(View view, int position) {
                transaction = mActivity.getSupportFragmentManager().beginTransaction();
                keySceneFragment_key = new KeySceneFragment_key();
                Bundle bundle = new Bundle();
                bundle.putInt("sceneid", sceneid);
                bundle.putInt("keyinput_position", position);
                bundle.putBoolean("ISCHOOSE", ISCHOOSE);
                position_keyinput = position;
                keySceneFragment_key.setArguments(bundle);
                transaction.replace(R.id.home, keySceneFragment_key);
                transaction.commit();
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        if (!IsHaveData) {
            ToastUtil.showToast(mActivity, "获取数据异常，请稍后在试");
            return;
        }
        switch (v.getId()) {
            case R.id.input_save:
                if (MyApplication.getWareData_Scene().getKeyInputs().size() == 0) {
                    ToastUtil.showToast(mActivity, "没有输入板信息，不能保存");
                    return;
                }
                CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(mActivity);
                builder.setTitle("提示 :");
                builder.setMessage("您要保存这些设置吗？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ChnOpItem_scene chnOpItem_scene = null;
                        String data_str = "";
                        String div;
                        String more_data = "";
                        div = ",";
                        chnOpItem_scene = MyApplication.mInstance.getKey_scene_data();
                        if (chnOpItem_scene.getKey2scene_item().size() > 12) {
                            ToastUtil.showToast(getActivity(), "最多只能添加12个按键");
                            return;
                        } else {
                            for (int j = 0; j < chnOpItem_scene.getKey2scene_item().size(); j++) {
                                data_str = "{" +
                                        "\"canCpuID\":\"" + chnOpItem_scene.getKey2scene_item().get(j).getCanCpuID() + "\"," +
                                        "\"keyIndex\":" + chnOpItem_scene.getKey2scene_item().get(j).getKeyIndex() + "," +
                                        "\"eventId\":" + chnOpItem_scene.getKey2scene_item().get(j).getEventId()
                                        + "}" + div;
                                more_data += data_str;
                            }
                        }
                        try {
                            more_data = more_data.substring(0, more_data.lastIndexOf(","));
                        } catch (Exception e) {
                            System.out.println(e + "");
                        }
                        initDialog("正在保存...");
                        //这就是要上传的字符串:data_hoad
                        String data_hoad = "{" +
                                "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                                "\"datType\":59" + "," +
                                "\"subType1\":0" + "," +
                                "\"subType2\":0" + "," +
                                "\"itemCnt\":" + chnOpItem_scene.getKey2scene_item().size() + "," +
                                "\"key2scene_item\":[" + more_data + "]}";
                        Log.e("情景模式测试:", data_hoad);
                        MyApplication.sendMsg(data_hoad);
                    }
                });
                builder.create().show();
                break;
            case R.id.input_choose:
                if (ISCHOOSE) {
                    ISCHOOSE = false;
                    input_choose.setImageResource(R.drawable.off);
                } else {
                    ISCHOOSE = true;
                    input_choose.setImageResource(R.drawable.on);
                }
                onGetIsChooseListener.getOutChoose(ISCHOOSE);
                break;
        }

    }

    //数据刷新后提供数据接口
    private static OnGetKeySceneDataListeener onGetKeySceneDataListeener;
    private static OnGetIsChooseListener onGetIsChooseListener;

    public static void setOnGetKeySceneDataListeener(OnGetKeySceneDataListeener ongetKeySceneDataListeener) {
        onGetKeySceneDataListeener = ongetKeySceneDataListeener;
    }

    public static void setOnGetIsChooseListener(OnGetIsChooseListener ongetIsChooseListener) {
        onGetIsChooseListener = ongetIsChooseListener;
    }

    interface OnGetKeySceneDataListeener {
        void getKeySceneData();
    }

    interface OnGetIsChooseListener {
        void getOutChoose(boolean ischoose);
    }
}
