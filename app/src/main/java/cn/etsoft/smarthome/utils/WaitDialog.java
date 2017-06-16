package cn.etsoft.smarthome.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import cn.etsoft.smarthome.view.Circle_Progress;

/**
 * Created by F-B-L on 2017/5/23.
 */

public class WaitDialog {

    private static Dialog mDialog;
    //自定义加载进度条
    public static Dialog initDialog(final Context context, String str) {
        Circle_Progress.setText(str);
        mDialog = Circle_Progress.createLoadingDialog(context);
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mDialog.dismiss();
                ToastUtil.showToast(context,"数据未获取成功");
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
        return mDialog;
    }
}
