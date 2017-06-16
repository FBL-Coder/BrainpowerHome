package com.example.abc.mybaseactivity.OtherUtils;

import android.content.Context;

import com.example.abc.mybaseactivity.widget.loading.ShapeLoadingDialog;


/**
 * @Description Dialog工具类
 * @Author FBL   2017-5-2
 */
public class DialogUtil {

    private static ShapeLoadingDialog shapeLoadingDialog;

    /**
     * @Description 显示加载状态框
     */
    public static void showDialogLoading(Context context, String message) {
        if (shapeLoadingDialog != null) {
            hideDialogLoading();
            shapeLoadingDialog = new ShapeLoadingDialog(context);
        } else {
            shapeLoadingDialog = new ShapeLoadingDialog(context);
        }

        if (!StringUtil.isEmpty(message)) {
            shapeLoadingDialog.setLoadingText(message);
        } else {
            shapeLoadingDialog.setLoadingText(UIUtils.getString(com.example.abc.mybaseactivity.R.string.loading));
        }

        shapeLoadingDialog.setCanceledOnTouchOutside(false);
        shapeLoadingDialog.show();
    }

    /**
     * @Description 显示加载状态框
     */
    public static void showDialogLoading(Context context) {
        if (shapeLoadingDialog != null) {
            hideDialogLoading();
            shapeLoadingDialog = new ShapeLoadingDialog(context);
        } else {
            shapeLoadingDialog = new ShapeLoadingDialog(context);
        }
        shapeLoadingDialog.setLoadingText(UIUtils.getString(com.example.abc.mybaseactivity.R.string.loading));
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
        shapeLoadingDialog.show();
    }

    /**
     * @Description 关闭加载框
     */
    public static void hideDialogLoading() {
        if (shapeLoadingDialog != null) {
            if (shapeLoadingDialog.isShowing()) {
                shapeLoadingDialog.dismiss();
            }
            shapeLoadingDialog = null;
        }
    }
}
