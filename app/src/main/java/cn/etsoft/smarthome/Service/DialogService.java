package cn.etsoft.smarthome.Service;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/11/3.
 * 全局Dialog、
 */

public class DialogService extends Service implements Observer {

    private Dialog mDialog;

    @Override
    public void update(Observable observable, Object data) {
        String msg = (String) data;
        Button button = null;
        TextView textView = null;
        if (msg != null) {
            if (mDialog == null) {
                mDialog = new Dialog(MyApplication.getContext());
                mDialog.setContentView(R.layout.dialog_global);
            }
            if (mDialog != null && !mDialog.isShowing()) {
                // 加入系统服务
                mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                mDialog.show();
                button = (Button) mDialog.findViewById(R.id.dialog_ok);
                textView = (TextView) mDialog.findViewById(R.id.dialog_message);
                textView.setText(msg);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });
            }
        } else {
            if (mDialog != null) {
                mDialog.cancel();
                mDialog = null;
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
