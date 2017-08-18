package cn.etsoft.smarthome.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/7/11.
 * 自定义GirdView
 * 重写事件拦截，用于Item中的旋转按钮   解决旋转按钮冲突
 */


/**
 * @ClassName: MyGridView
 * @Description: 自定义GridView——解决和ScollView的滑动冲突问题
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
            ViewGroup viewGroup = ((ViewGroup) getChildAt(i));
            for (int j = 0; j < viewGroup.getChildCount(); j++) {
                if (viewGroup.getChildAt(j).getId() == R.id.RotateControButton ||
                        viewGroup.getChildAt(j).getId() == R.id.img_list_item ) {
                    View view = viewGroup.getChildAt(j);
                    if (inRangeOfView(view, event)) {
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
        if (ev.getX() + ((View) getParent()).getX() < x || ev.getX() + ((View) getParent()).getX() > (x + view.getWidth()) || ev.getY() + ((View) getParent()).getY() < y || ev.getY() + ((View) getParent()).getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
