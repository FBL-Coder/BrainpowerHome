package cn.etsoft.smarthome.View.CircleMenu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;
import cn.semtec.community2.util.ToastUtil;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Author：FBL  Time： 2017/6/8.
 */

public class CircleMenuLayout extends RelativeLayout {

    private Context context;
    private CircleLayout circle_1;
    private CircleLayout circle_2;
    private List<CircleDataEvent> Data_OuterCircleList;
    private List<CircleDataEvent> Data_InnerCircleList;
    private ImageView btn_view;
    private RelativeLayout ll_btn_view;
    //屏幕适配系数
    private double Adaptive_coefficient = 1;

    public CircleMenuLayout(Context context) {
        super(context);
        this.context = context;
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
    }

    public void Init(int Radius_outer, int Radius_inner) {
        initView(Radius_outer, Radius_inner);
        UIEvent_Circle_Outer();
        UIEvent_Circle_Inner();
    }

    public void setInnerCircleMenuData(List<CircleDataEvent> Data_InnerCircleList) {
        this.Data_InnerCircleList = Data_InnerCircleList;
        UIEvent_Circle_Inner();
    }

    public void setOuterCircleMenuData(List<CircleDataEvent> Data_OuterCircleList) {
        this.Data_OuterCircleList = Data_OuterCircleList;
        UIEvent_Circle_Outer();
    }


    /**
     * 内圆转盘事件
     */
    private void UIEvent_Circle_Inner() {

        if (Data_InnerCircleList == null)
            Data_InnerCircleList = new ArrayList<>();
        circle_2.removeAllViews();
        for (int i = 0; i < Data_InnerCircleList.size(); i++) {
            LinearLayout l = new LinearLayout(context);
            l.setOrientation(LinearLayout.VERTICAL);
            ImageView iv = new ImageView(context);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (50 / Adaptive_coefficient), (int) (70 / Adaptive_coefficient));
            layoutParams.setMargins(10, 0, 10, 0);
            iv.setLayoutParams(layoutParams);
            iv.setImageResource(Data_InnerCircleList.get(i).getImage());
            l.addView(iv);
            final TextView t = new TextView(context);
            if (Data_InnerCircleList.get(i).isSelect) {
                t.setTextColor(Color.BLUE);
            } else {
                t.setTextColor(Color.BLACK);
            }
            t.setGravity(Gravity.CENTER);
            t.setTextSize((int) (12 / Adaptive_coefficient));
            t.setText(Data_InnerCircleList.get(i).getTitle());
            l.addView(t);
            final int Position = i;
            l.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onInnerCircleLayoutClickListener.onClickInnerCircle(Position, v);
                    for (int j = 0; j < Data_InnerCircleList.size(); j++) {
                        if (j == Position)
                            Data_InnerCircleList.get(j).setSelect(true);
                        else Data_InnerCircleList.get(j).setSelect(false);
                    }
                    UIEvent_Circle_Inner();
                }
            });
            circle_2.addView(l);
        }
    }

    /**
     * 外圆转盘事件
     */
    private void UIEvent_Circle_Outer() {
        if (Data_OuterCircleList == null)
            Data_OuterCircleList = new ArrayList<>();
        circle_1.removeAllViews();
        for (int i = 0; i < Data_OuterCircleList.size(); i++) {
            LinearLayout l = new LinearLayout(context);
            l.setOrientation(LinearLayout.VERTICAL);
            ImageView iv = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) (60 / Adaptive_coefficient), (int) (70 / Adaptive_coefficient));
            layoutParams.setMargins(10, 0, 10, 0);
            iv.setImageResource(Data_OuterCircleList.get(i).getImage());
            l.addView(iv);
            final TextView t = new TextView(context);
            t.setTextColor(Color.WHITE);
            if (Data_OuterCircleList.get(i).isSelect) {
                t.setTextColor(getResources().getColor(R.color.font));
            } else {
                t.setTextColor(Color.WHITE);
            }
            t.setGravity(Gravity.CENTER);
            t.setTextSize((int) (12 / Adaptive_coefficient));
            t.setText(Data_OuterCircleList.get(i).getTitle());
            l.addView(t);
            final int Position = i;
            l.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOuterCircleLayoutClickListener.onClickOuterCircle(Position, v);
                    for (int j = 0; j < Data_OuterCircleList.size(); j++) {
                        if (j == Position)
                            Data_OuterCircleList.get(j).setSelect(true);
                        else Data_OuterCircleList.get(j).setSelect(false);
                    }
                    UIEvent_Circle_Outer();
                }
            });
            circle_1.addView(l);
        }
    }

    /**
     * 初始化转盘组件
     */
    private void initView(int Radius_outer, int Radius_inner) {

        int W = cn.semtec.community2.MyApplication.display_width;
        int h = cn.semtec.community2.MyApplication.display_height;
        int SW = W < h ? W : h;
        int dp_SW = CircleMenuLayout.px2dip(SW);

        if (dp_SW > 200 && dp_SW <= 320) {
            Adaptive_coefficient = 1.8;
            Radius_outer = (int) (Radius_outer / Adaptive_coefficient);
            Radius_inner = (int) (Radius_inner / (Adaptive_coefficient));
        } else if (dp_SW > 320 && dp_SW <= 480) {
            Adaptive_coefficient = 1.2;
            Radius_outer = (int) (Radius_outer / (Adaptive_coefficient));
            Radius_inner = (int) (Radius_inner / (Adaptive_coefficient));
        } else if (dp_SW > 480 && dp_SW <= 600) {
            Adaptive_coefficient = 1.5;
            Radius_outer = (int) (Radius_outer / (Adaptive_coefficient - 0.55));
            Radius_inner = (int) (Radius_inner / (Adaptive_coefficient - 0.55));
        } else if (dp_SW > 600 && dp_SW <= 720) {
            Adaptive_coefficient = 1;
            Radius_outer = (int) (Radius_outer / Adaptive_coefficient);
            Radius_inner = (int) (Radius_inner / Adaptive_coefficient);
        } else if (dp_SW > 720) {
            Adaptive_coefficient = 0.9;
            Radius_outer = (int) (Radius_outer / 0.8);
            Radius_inner = (int) (Radius_inner / 0.8);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.circle_menu, this);
        btn_view = (ImageView) view.findViewById(R.id.btn_view);
        ll_btn_view = (RelativeLayout) view.findViewById(R.id.ll_btn_view);
        circle_1 = (CircleLayout) view.findViewById(R.id.circle_1);
        circle_2 = (CircleLayout) view.findViewById(R.id.circle_2);
        circle_1.setMaxWidth(dip2px(Radius_outer + (int) (50 / Adaptive_coefficient)));
        circle_2.setMaxWidth(dip2px(Radius_inner + (int) (50 / Adaptive_coefficient)));
        circle_1.setmTranslationX(-(dip2px(2 * Radius_outer / 3)));
        circle_2.setmTranslationX(-(dip2px(4 * Radius_inner / 5)));
        circle_1.setCanScroll(true);
        circle_2.setCanScroll(true);
        circle_1.setRadius(dip2px(Radius_outer));
        circle_2.setRadius(dip2px(Radius_inner));
        btn_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    static final float scale = MyApplication.mApplication.getResources().getDisplayMetrics().density;

    public static int dip2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }


    private OnOuterCircleLayoutClickListener onOuterCircleLayoutClickListener;
    private OnInnerCircleLayoutClickListener onInnerCircleLayoutClickListener;

    public void setOnOuterCircleLayoutClickListener(OnOuterCircleLayoutClickListener onOuterCircleLayoutClickListener) {
        this.onOuterCircleLayoutClickListener = onOuterCircleLayoutClickListener;
    }

    public void setOnInnerCircleLayoutClickListener(OnInnerCircleLayoutClickListener onInnerCircleLayoutClickListener) {
        this.onInnerCircleLayoutClickListener = onInnerCircleLayoutClickListener;
    }

    public interface OnOuterCircleLayoutClickListener {
        void onClickOuterCircle(int position, View view);
    }

    public interface OnInnerCircleLayoutClickListener {
        void onClickInnerCircle(int position, View view);
    }

}
