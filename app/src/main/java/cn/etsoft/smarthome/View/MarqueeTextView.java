package cn.etsoft.smarthome.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Author：FBL  Time： 2017/6/20.
 */

public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    // 焦点
    @Override
    public boolean isFocused() {
        return true;
    }
}
