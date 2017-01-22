package cn.semtec.community2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class MyBaseActivity extends Activity {
    private Dialog buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    protected void showProgress() {
        if (buffer == null) {
            buffer = new AlertDialog.Builder(this).create();
            buffer.setCanceledOnTouchOutside(false);
        }
        buffer.show();
        buffer.getWindow().setContentView(cn.etsoft.smarthome.R.layout.buffer_bar);
    }

    protected void cancelProgress() {
        if (buffer != null) {
            buffer.cancel();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (buffer != null) {
            buffer.cancel();
        }
    }
}
