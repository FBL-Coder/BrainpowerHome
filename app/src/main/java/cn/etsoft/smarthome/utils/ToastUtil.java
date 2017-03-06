package cn.etsoft.smarthome.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    static long lastClick;

    public static void showToast(Context context,
                                 String content) {

        try {
            if (System.currentTimeMillis() - lastClick <= 1000) {
                return;
            }
            lastClick = System.currentTimeMillis();
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            } else {
                toast.setText(content);
            }
            toast.show();
        } catch (Exception e) {
            System.out.println("异常：" + e);
            return;
        }
    }
}
