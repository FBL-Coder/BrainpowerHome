package cn.etsoft.smarthome.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.adapter.GridViewAdapter_parlour;
import cn.etsoft.smarthome.adapter.ParlourGridViewAdapter;
import cn.etsoft.smarthome.pullmi.app.GlobalVars;
import cn.etsoft.smarthome.pullmi.common.CommonUtils;
import cn.etsoft.smarthome.pullmi.entity.WareDev;
import cn.etsoft.smarthome.pullmi.entity.WareSceneDevItem;
import cn.etsoft.smarthome.pullmi.entity.WareSceneEvent;
import cn.etsoft.smarthome.pullmi.utils.LogUtils;
import cn.etsoft.smarthome.widget.CustomDialog;
import cn.etsoft.smarthome.widget.CustomDialog_comment;

/**
 * Created by Say GoBay on 2016/9/2.
 * 情景房间之设备设置
 */
public class ParlourActivity extends Activity implements View.OnClickListener {
    private GridView gridView;
    private ImageView back, dialog_back;
    private TextView title;
    private int[] pic1 = {R.drawable.curtain6, R.drawable.curtain5, R.drawable.curtain7};
    private String[] des1 = {"全开", "半开", "全关"};
    private int[] pic2 = {R.drawable.on, R.drawable.off};
    private String[] des2 = {"打开", "关闭"};

    private ParlourGridViewAdapter parlourGridViewAdapter;
    private List<WareDev> listViewItems;
    private String RoomName;
    private int eventId;
    private List<List> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parlour);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化标题栏
        initTitleBar();
        //初始化GridView
        initGridView();

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                initGridView();
                super.handleMessage(msg);
            }
        };
        MyApplication.mInstance.setOnGetWareDataListener(new MyApplication.OnGetWareDataListener() {
            @Override
            public void upDataWareData(int what) {
                Message message = mHandler.obtainMessage();
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        title = (TextView) findViewById(R.id.tv_home);
        back = (ImageView) findViewById(R.id.back);
        RoomName = getIntent().getExtras().getString("title");
        title.setText(RoomName);
        eventId = getIntent().getExtras().getInt("eventId"); //默认全关模式
        back.setOnClickListener(this);
    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        gridView = (GridView) findViewById(R.id.parlour_gv);
        listViewItems = new ArrayList<>();
        for (int i = 0; i < MyApplication.getWareData().getDevs().size(); i++) {
            if (RoomName.equals(MyApplication.getWareData().getDevs().get(i).getRoomName()))
                listViewItems.add(MyApplication.getWareData().getDevs().get(i));
            else
                continue;
        }
        mList = new ArrayList<List>();
        for (int i = 0; i < listViewItems.size(); i++) {
            mList.add(null);
        }
        if (parlourGridViewAdapter != null) {
            parlourGridViewAdapter.notifyDataSetChanged();
        } else {
            parlourGridViewAdapter = new ParlourGridViewAdapter(this, listViewItems, eventId);
            gridView.setAdapter(parlourGridViewAdapter);
        }
        gridView.setSelector(R.drawable.selector_gridview_item);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //在这里，确定设置设备。
                TextView tv = (TextView) view.findViewById(R.id.light_gv_title);
                ImageView imageView = (ImageView) view.findViewById(R.id.light_gv_img);
                List list = new ArrayList();
                getDialog(position, list, imageView);
            }
        });
    }

    /**
     * 初始化自定义dialog
     */
    CustomDialog dialog;

    public void getDialog(int position, List list, ImageView imageView) {
        dialog = new CustomDialog(this, R.style.customDialog, R.layout.dialog_parlour_curtain);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        //初始化DialogGridView
        initDialogGridView(position, list, imageView);
    }

    /**
     * 初始化DialogGridView
     */
    private void initDialogGridView(final int parent_position, final List list, final ImageView imageView) {
        gridView = (GridView) dialog.findViewById(R.id.parlour_dialog_gv);
        int devType = listViewItems.get(parent_position).getType();
        switch (devType) {
            case 0:
            case 1:
            case 2:
            case 3:
                gridView.setAdapter(new GridViewAdapter_parlour(pic2, des2, this));
                break;
            case 4:
                gridView.setAdapter(new GridViewAdapter_parlour(pic1, des1, this));
                break;
            case 5:
                break;
        }
        gridView.setSelector(R.drawable.selector_gridview_item);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                String cpuid = listViewItems.get(parent_position).getCanCpuId();
                                                int type = listViewItems.get(parent_position).getType();
                                                int devid = listViewItems.get(parent_position).getDevId();
                                                if (type == 3) {
                                                    if (position == 1) {
                                                        imageView.setImageResource(R.drawable.lightoff);
                                                        list.add(0);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);

                                                    } else {
                                                        imageView.setImageResource(R.drawable.lighton);
                                                        list.add(1);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);

                                                    }
                                                } else if (type == 0) {//空调
                                                    if (position == 1) {
                                                        imageView.setImageResource(R.drawable.off3);
                                                        list.add(0);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    } else {
                                                        imageView.setImageResource(R.drawable.on3);
                                                        list.add(1);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    }
                                                } else if (type == 4) {//窗帘

                                                    if (position == 2) {
                                                        imageView.setImageResource(R.drawable.clg);
                                                        list.add(1);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);

                                                    } else if (position == 1) {
                                                        imageView.setImageResource(R.drawable.clbk);
                                                        list.add(1);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    } else {
                                                        imageView.setImageResource(R.drawable.clk);
                                                        list.add(2);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    }
                                                } else if (type == 1)

                                                {//电视
                                                    if (position == 1) {
                                                        imageView.setImageResource(R.drawable.dsg);
                                                        list.add(0);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    } else {
                                                        imageView.setImageResource(R.drawable.dsk);
                                                        list.add(1);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    }
                                                } else if (type == 2)

                                                {//机顶盒
                                                    if (position == 1) {
                                                        imageView.setImageResource(R.drawable.jdhg);
                                                        list.add(0);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    } else {
                                                        imageView.setImageResource(R.drawable.jdhk);
                                                        list.add(1);
                                                        list.add(cpuid);
                                                        list.add(type);
                                                        list.add(devid);
                                                    }
                                                }

                                                dialog.dismiss();
                                                mList.set(parent_position, list);
                                            }

                                        }

        );
        dialog_back = (ImageView) dialog.findViewById(R.id.dialog_back);
        dialog_back.setOnClickListener(this);
    }

    public void save() {
        CustomDialog_comment.Builder builder = new CustomDialog_comment.Builder(ParlourActivity.this);
        builder.setTitle("提示 :");
        builder.setMessage("您要保存这些设置吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = null;
                String data_mouth = "";
                String data_hoad = null;
                WareSceneEvent Sceneevent = null;
                for (int i = 0; i < MyApplication.getWareData().getSceneEvents().size(); i++) {
                    if (eventId == MyApplication.getWareData().getSceneEvents().get(i).getEventld()) {
                        Sceneevent = MyApplication.getWareData().getSceneEvents().get(i);
                        break;
                    }
                }
                if (Sceneevent == null) {
                    dialog.dismiss();
                    finish();
                    return;
                }
                List<WareSceneDevItem> item = Sceneevent.getItemAry();
                String div;
                int devCnt = 0;
                if (item == null) {
                    int num = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) == null)
                            continue;
                        else {
                            num++;
                        }
                    }

                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) == null)
                            continue;
                        else {
                            if (num > 1)
                                div = ",";
                            else
                                div = "";
                            data = "{" +
                                    "\"uid\":\"" + mList.get(i).get(1) + "\"," +
                                    "\"devType\":" + mList.get(i).get(2) + "," +
                                    "\"devID\":" + mList.get(i).get(3) + "," +
                                    "\"bOnOff\":" + mList.get(i).get(0) + "," +
                                    "\"lmVal\":0," +
                                    "\"rev2\":0," +
                                    "\"rev3\":0," +
                                    "\"param1\":0," +
                                    "\"param2\":0}" + div;
                            data_mouth += data;
                            devCnt++;
                            num--;
                        }
                    }
                } else {
                    int num = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) == null)
                            continue;
                        else {
                            for (int k = 0; k < item.size(); k++) {
                                //判断是否和之前的重复，是，跳出，不是，添加到上传字符串中；
                                if (item.get(k).getDevType() == (int) mList.get(i).get(2)
                                        && item.get(k).getDevID() == (int) mList.get(i).get(3)
                                        && item.get(k).getUid().equals(mList.get(i).get(1))) {

                                    item.remove(k);
                                    break;
                                }
                            }
                            num++;
                        }
                    }
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i) == null)
                            continue;
                        else {
                            if (num > 0)
                                div = ",";
                            else
                                div = "";
                            data = "{" +
                                    "\"uid\":\"" + mList.get(i).get(1) + "\"," +
                                    "\"devType\":" + mList.get(i).get(2) + "," +
                                    "\"devID\":" + mList.get(i).get(3) + "," +
                                    "\"bOnOff\":" + mList.get(i).get(0) + "," +
                                    "\"lmVal\":0," +
                                    "\"rev2\":0," +
                                    "\"rev3\":0," +
                                    "\"param1\":0," +
                                    "\"param2\":0}" + div;
                            data_mouth += data;
                            devCnt++;
                        }
                    }
                    int item_size = item.size();
                    //这里是拿出之前的设备信息，一并上传使用；
                    for (int j = 0; j < item.size(); j++) {
                        if (item_size > 1)
                            div = ",";
                        else
                            div = "";

                        data = "{" +
                                "\"uid\":\"" + item.get(j).getUid() + "\"," +
                                "\"devType\":" + item.get(j).getDevType() + "," +
                                "\"devID\":" + item.get(j).getDevID() + "," +
                                "\"bOnOff\":" + item.get(j).getbOnOff() + "," +
                                "\"lmVal\":0," +
                                "\"rev2\":0," +
                                "\"rev3\":0," +
                                "\"param1\":0," +
                                "\"param2\":0}" + div;
                        data_mouth += data;
                        devCnt++;
                        item_size--;
                    }
                }

                byte[] nameData = {0};
                try {
                    nameData = Sceneevent.getSceneName().getBytes("GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String str_gb = CommonUtils.bytesToHexString(nameData);
                LogUtils.LOGE("情景模式名称:%s", str_gb);

                //这就是要上传的字符串:data_hoad
                data_hoad = "{" +
                        "\"devUnitID\":\"" + GlobalVars.getDevid() + "\"," +
                        "\"sceneName\":\"" + str_gb + "\"," +
                        "\"datType\":24" + "," +
                        "\"subType1\":0" + "," +
                        "\"subType2\":0" + "," +
                        "\"eventId\":" + Sceneevent.getEventld() + "," +
                        "\"devCnt\":" + devCnt + "," +
                        "\"itemAry\":[" + data_mouth + "]}";

                LogUtils.LOGE("情景模式测试:", data_hoad);

                MyApplication.sendMsg(data_hoad);
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            save();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                save();
                break;
            case R.id.dialog_back:
                dialog.dismiss();
                break;

        }
    }
}
