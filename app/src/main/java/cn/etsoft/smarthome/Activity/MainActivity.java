package cn.etsoft.smarthome.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;
import com.example.abc.mybaseactivity.Dialog_Select_Bottom.Dialog_Bottom;
import com.example.abc.mybaseactivity.FileUtils.GetFilePath;
import com.example.abc.mybaseactivity.HttpGetDataUtils.HttpCallback;
import com.example.abc.mybaseactivity.HttpGetDataUtils.OkHttpUtils;
import com.example.abc.mybaseactivity.HttpGetDataUtils.ResultDesc;
import com.example.abc.mybaseactivity.Notifications.NotificationUtils;
import com.example.abc.mybaseactivity.OtherUtils.DialogUtil;
import com.example.abc.mybaseactivity.OtherUtils.LogUtil;
import com.example.abc.mybaseactivity.OtherUtils.ToastUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import cn.etsoft.smarthome.Domain.GlobalVars;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.Utils.SendDataUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private String Tag = this.getClass().getName();
    private ImageView iv_0;
    private ImageView iv_1;
    private ImageView iv_2;
    private EditText inputWSSocket;
    private Button send_bt, sendws_bt;
    private MyHandler mHandler = new MyHandler(this);
    private int UDPDATA = 1000;

    @Override
    public void initView() {
        setLayout(R.layout.main);
        setTitleImageBtn(true, R.drawable.back_image_select, true, R.drawable.ic_launcher_round);

        setTitleText(getString(com.example.abc.mybaseactivity.R.string.TestClass), 20, R.color.white);
        setTitleViewVisible(true, R.color.colorAccent);
        setStatusColor(R.color.colorAccent);

        iv_0 = (ImageView) findViewById(R.id.iv_0);
        iv_0.setOnClickListener(this);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_1.setOnClickListener(this);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_2.setOnClickListener(this);
        send_bt = (Button) findViewById(R.id.send_bt);
        send_bt.setOnClickListener(this);
        sendws_bt = (Button) findViewById(R.id.sendws_bt);
        sendws_bt.setOnClickListener(this);
        inputWSSocket = (EditText) findViewById(R.id.info_tv);
    }


    @Override
    public void initData() {


        getLiftImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showText("你要退出，但是我不同意！！！");
            }
        });
        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTheme(R.style.ActionSheetStyleIOS7);
                Dialog_Bottom menuView = new Dialog_Bottom(MainActivity.this);
                menuView.setCancelButtonTitle("取消");// before add items
                menuView.addItems("编 辑", "删 除", "新 增", "收 藏");
                menuView.setItemClickListener(new Dialog_Bottom.MenuItemClickListener() {
                    @Override
                    public void onItemClick(int itemPosition) {
//                        ToastUtil.showText("点击的是第" + itemPosition + "条！");
                        pickFile();
                    }
                });
                menuView.setCancelableOnTouchMenuOutside(true);
                menuView.showMenu();
            }
        });
    }

    private void pickFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    private String filePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        filePath = GetFilePath.getPath(this, data.getData());
        if (filePath == null)
            return;
        if (filePath.endsWith("txt") || filePath.endsWith("doc") || filePath.endsWith("docx") ||
                filePath.endsWith("wps") || filePath.endsWith("wpt") ||
                filePath.endsWith("ppt") || filePath.endsWith("pptx") ||
                filePath.endsWith("xls") || filePath.endsWith("xlsx") ||
                filePath.endsWith("TXT") || filePath.endsWith("DOC") ||
                filePath.endsWith("DOCX") || filePath.endsWith("WPS") ||
                filePath.endsWith("PPT") || filePath.endsWith("PPTX") ||
                filePath.endsWith("XLS") || filePath.endsWith("XLSX") ||
                filePath.endsWith("pdf") || filePath.endsWith("WPT") ||
                filePath.endsWith("PDF")) {
        } else {
            ToastUtil.showText("选择文件类型不正确！");
            return;
        }
        File f = new File(filePath);

//        OkHttpUtils.postAsynFiles("","",);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        MyApplication.finishActivity(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_0) {
            Map<String, String> params = new HashMap<>();
            params.put("username", "long");
            params.put("password", "123456");
            DialogUtil.showDialogLoading(MainActivity.this);

            OkHttpUtils.postAsyn("http://119.147.115.203:18980/index.php/Webapi/Index/login/", params, new HttpCallback() {
                @Override
                public void onSuccess(ResultDesc resultDesc) {
                    super.onSuccess(resultDesc);
                    LogUtil.e(Tag, "数据请求成功   返回结果：" + "返回码 " + resultDesc.getError_code() + "返回说明" + resultDesc.getReason() + "返回数据" + resultDesc.getResult());
                    DialogUtil.hideDialogLoading();

                }

                @Override
                public void onFailure(int code, String message) {
                    super.onFailure(code, message);
                    LogUtil.e(Tag, "数据请求失败   失败原因：" + message);
                }

                @Override
                public void onProgress(long currentTotalLen, long totalLen) {
                    super.onProgress(currentTotalLen, totalLen);
                }
            });
            LogUtil.e(Tag, "异步请求数据这里继续进行");

        } else if (i == R.id.iv_1) {
            ToastUtil.showText("你好啊我是你的好朋友");

        } else if (i == R.id.iv_2) {
            NotificationUtils.createNotif(
                    this, R.mipmap.ic_launcher, "你好，我是Notification",
                    "标题", "内容", new Intent(this, Circle_MenuActivity.class), 0, 0);
        } else if (i == R.id.send_bt) {
            GlobalVars.setDevid("39ffd505484d303408650743");
            SendDataUtil.getNetWorkInfo();
        } else if (i == R.id.sendws_bt) {
            if (inputWSSocket.getText().toString().isEmpty())
                return;
            MyApplication.mApplication.getWsClient().sendMsg(inputWSSocket.getText().toString());
        }
    }

    static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;
        private MainActivity activity;

        MyHandler(MainActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference == null)
                return;
            activity = weakReference.get();

        }
    }
}
