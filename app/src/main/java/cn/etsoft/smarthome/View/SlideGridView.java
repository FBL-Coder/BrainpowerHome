package cn.etsoft.smarthome.View;

import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

/**
 * Author：FBL  Time： 2017/7/11.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.etsoft.smarthome.R;
import cn.etsoft.smarthome.View.RotateBtn.RotateControButton;


/**
 * @ClassName: MyGridView
 * @Description: TODO(自定义GridView——解决和ScollView的滑动冲突问题)
 */
public class SlideGridView extends GridView {


    public SlideGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public SlideGridView(Context context) {
        super(context);
    }


    public SlideGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean isIntercept = true;
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout linearLayout = ((LinearLayout) getChildAt(i));
            for (int j = 0; j < linearLayout.getChildCount(); j++) {
                if (linearLayout.getChildAt(j).getId() == R.id.RotateControButton) {
                    RotateControButton btn = (RotateControButton) linearLayout.getChildAt(j);
                    if (inRangeOfView(btn, event)) {
                        isIntercept = false;
                    }
                }
            }
        }

        return isIntercept;
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() + getX() < x || ev.getX() + getX() > (x + view.getWidth()) || ev.getY() + getY() < y || ev.getY() + getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
