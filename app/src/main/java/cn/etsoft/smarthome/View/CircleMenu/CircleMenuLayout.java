package cn.etsoft.smarthome.View.CircleMenu;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.etsoft.smarthome.R;

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
    private Animation mAnim_circlelayout_outer_open, mAnim_circlelayout_outer_close, mAnim_circlelayout_inner_close, mAnim_circlelayout_inner_open,
            mAnim_iv_rotate_open, mAnim_iv_rotate_close, mAnim_rl_open, mAnim_rl_close;

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

    public void Init() {
        initView();
        UIEvent_Circle_Outer();
        UIEvent_Circle_Inner();
        initAnimations_One();
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
     * 初始化所需动画
     */
    private void initAnimations_One() {
        mAnim_circlelayout_outer_open = AnimationUtils.loadAnimation(context, R.anim.anim_cieclelayout_outer_open);
        mAnim_circlelayout_outer_close = AnimationUtils.loadAnimation(context, R.anim.anim_circlelayout_outer_close);
        mAnim_circlelayout_inner_open = AnimationUtils.loadAnimation(context, R.anim.anim_circlelayout_inner_open);
        mAnim_circlelayout_inner_close = AnimationUtils.loadAnimation(context, R.anim.anim_circlelayout_inner_close);
        mAnim_iv_rotate_open = AnimationUtils.loadAnimation(context, R.anim.anim_circlrlayout_iv_rotate_open);
        mAnim_iv_rotate_close = AnimationUtils.loadAnimation(context, R.anim.anim_circlrlayout_iv_rotate_close);
        mAnim_rl_open = AnimationUtils.loadAnimation(context, R.anim.anim_circlelayout_rl_open);
        mAnim_rl_close = AnimationUtils.loadAnimation(context, R.anim.anim_circlelayout_rl_close);
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
            iv.setImageResource(Data_InnerCircleList.get(i).getImage());
            l.addView(iv);
            final TextView t = new TextView(context);
            if (Data_InnerCircleList.get(i).isSelect)
                t.setTextColor(Color.BLUE);
            else t.setTextColor(Color.WHITE);
            t.setGravity(Gravity.CENTER);
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
            iv.setImageResource(Data_OuterCircleList.get(i).getImage());
            l.addView(iv);
            final TextView t = new TextView(context);
            t.setTextColor(Color.WHITE);
            if (Data_OuterCircleList.get(i).isSelect)
                t.setTextColor(Color.BLUE);
            else t.setTextColor(Color.WHITE);
            t.setGravity(Gravity.CENTER);
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
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_menu, this);
        btn_view = (ImageView) view.findViewById(R.id.btn_view);
        ll_btn_view = (RelativeLayout) view.findViewById(R.id.ll_btn_view);
        circle_1 = (CircleLayout) view.findViewById(R.id.circle_1);
        circle_2 = (CircleLayout) view.findViewById(R.id.circle_2);
        circle_1.setCanScroll(true);
        circle_2.setCanScroll(true);
        circle_1.setRadius(310);
        circle_2.setRadius(160);
        btn_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circle_1.getVisibility() == View.VISIBLE) {
                    circle_1.setVisibility(View.GONE);
                    circle_1.startAnimation(mAnim_circlelayout_outer_close);
                    circle_2.setVisibility(View.GONE);
                    circle_2.startAnimation(mAnim_circlelayout_inner_close);
                    ll_btn_view.setVisibility(View.GONE);
                    ll_btn_view.startAnimation(mAnim_rl_close);
                    btn_view.startAnimation(mAnim_iv_rotate_close);
                } else {
                    circle_1.setVisibility(View.VISIBLE);
                    circle_1.startAnimation(mAnim_circlelayout_outer_open);
                    circle_2.setVisibility(View.VISIBLE);
                    circle_2.startAnimation(mAnim_circlelayout_inner_open);
                    ll_btn_view.setVisibility(View.VISIBLE);
                    ll_btn_view.startAnimation(mAnim_rl_open);
                    btn_view.startAnimation(mAnim_iv_rotate_open);
                }
            }
        });
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
