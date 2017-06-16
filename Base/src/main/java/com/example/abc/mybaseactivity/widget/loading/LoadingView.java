package com.example.abc.mybaseactivity.widget.loading;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @Description 自定义loadingView
 * @Author FBL   2017-5-2
 */
public class LoadingView extends FrameLayout {

    private static final int ANIMATION_DURATION = 500;
    private static float mDistance = 200;
    public float factor = 1.2f;

    private ShapeLoadingView mShapeLoadingView, mShapeLoadingView1, mShapeLoadingView2;
    private ImageView mIndicationIm, mIndicationIm1, mIndicationIm2;
    private TextView mLoadTextView;
    private int mTextAppearance;

    private String mLoadText;
    private AnimatorSet mUpAnimatorSet;
    private AnimatorSet mDownAnimatorSet;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.example.abc.mybaseactivity.R.styleable.LoadingView);
        mLoadText = typedArray.getString(com.example.abc.mybaseactivity.R.styleable.LoadingView_loadingText);
        mTextAppearance = typedArray.getResourceId(com.example.abc.mybaseactivity.R.styleable.LoadingView_loadingTextAppearance, -1);

        typedArray.recycle();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(com.example.abc.mybaseactivity.R.layout.load_view, null);
        mDistance = dip2px(54f);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mShapeLoadingView = (ShapeLoadingView) view.findViewById(com.example.abc.mybaseactivity.R.id.shapeLoadingView);
        mShapeLoadingView.setShape(ShapeLoadingView.Shape.SHAPE_RECT);
        mShapeLoadingView1 = (ShapeLoadingView) view.findViewById(com.example.abc.mybaseactivity.R.id.shapeLoadingView_1);
        mShapeLoadingView1.setShape(ShapeLoadingView.Shape.SHAPE_CIRCLE);
        mShapeLoadingView2 = (ShapeLoadingView) view.findViewById(com.example.abc.mybaseactivity.R.id.shapeLoadingView_2);
        mShapeLoadingView2.setShape(ShapeLoadingView.Shape.SHAPE_TRIANGLE);
        mIndicationIm = (ImageView) view.findViewById(com.example.abc.mybaseactivity.R.id.indication);
        mIndicationIm1 = (ImageView) view.findViewById(com.example.abc.mybaseactivity.R.id.indication1);
        mIndicationIm2 = (ImageView) view.findViewById(com.example.abc.mybaseactivity.R.id.indication2);
        mLoadTextView = (TextView) view.findViewById(com.example.abc.mybaseactivity.R.id.promptTV);

        if (mTextAppearance != -1) {
            mLoadTextView.setTextAppearance(getContext(), mTextAppearance);
        }
        setLoadingText(mLoadText);
        addView(view, layoutParams);
        startLoading(900);
    }

    private AnimatorSet mAnimatorSet = null;

    private Runnable mFreeFallRunnable = new Runnable() {
        @Override
        public void run() {
            freeFall(mShapeLoadingView, mIndicationIm);
            freeFall(mShapeLoadingView1, mIndicationIm1);
            freeFall(mShapeLoadingView2, mIndicationIm2);
        }
    };

    private void startLoading(long delay) {
        if (mDownAnimatorSet != null && mDownAnimatorSet.isRunning()) {
            return;
        }
        this.removeCallbacks(mFreeFallRunnable);
        if (delay > 0) {
            this.postDelayed(mFreeFallRunnable, delay);
        } else {
            this.post(mFreeFallRunnable);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    private void stopLoading() {
        if (mAnimatorSet != null) {
            if (mAnimatorSet.isRunning()) {
                mAnimatorSet.cancel();
            }
            mAnimatorSet = null;
        }
        if (mUpAnimatorSet != null) {
            if (mUpAnimatorSet.isRunning()) {
                mUpAnimatorSet.cancel();
            }
            mUpAnimatorSet.removeAllListeners();
            for (Animator animator : mUpAnimatorSet.getChildAnimations()) {
                animator.removeAllListeners();
            }
        }
        if (mDownAnimatorSet != null) {
            if (mDownAnimatorSet.isRunning()) {
                mDownAnimatorSet.cancel();
            }
            mDownAnimatorSet.removeAllListeners();
            for (Animator animator : mDownAnimatorSet.getChildAnimations()) {
                animator.removeAllListeners();
            }
        }
        this.removeCallbacks(mFreeFallRunnable);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startLoading(200);
        } else {
            stopLoading();
        }
    }

    public void setLoadingText(CharSequence loadingText) {
        if (TextUtils.isEmpty(loadingText)) {
            mLoadTextView.setVisibility(GONE);
        } else {
            mLoadTextView.setVisibility(VISIBLE);
        }
        mLoadTextView.setText(loadingText);
    }

    /**
     * 上抛
     */
    public void upThrow(final ShapeLoadingView mShapeLoadingView, final ImageView mIndicationIm) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", mDistance, 0);
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 1, 0.2f);
        ObjectAnimator objectAnimator_all = null;
        switch (mShapeLoadingView.getShape()) {
            case SHAPE_RECT:
                objectAnimator_all = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, -120);
                break;
            case SHAPE_CIRCLE:
                objectAnimator_all = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
            case SHAPE_TRIANGLE:
                objectAnimator_all = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);
                break;
        }

        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator_all.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator(factor));
        objectAnimator_all.setInterpolator(new DecelerateInterpolator(factor));
        mUpAnimatorSet = new AnimatorSet();
        mUpAnimatorSet.setDuration(ANIMATION_DURATION);
        mUpAnimatorSet.playTogether(objectAnimator, objectAnimator_all, scaleIndication);

        mUpAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                freeFall(mShapeLoadingView, mIndicationIm);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mUpAnimatorSet.start();
    }

    /**
     * 下落
     */
    public void freeFall(final ShapeLoadingView mShapeLoadingView, final ImageView mIndicationIm) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY", 0, mDistance);
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 0.2f, 1);
        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new AccelerateInterpolator(factor));
        mDownAnimatorSet = new AnimatorSet();
        mDownAnimatorSet.setDuration(ANIMATION_DURATION);
        mDownAnimatorSet.playTogether(objectAnimator, scaleIndication);
        mDownAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mShapeLoadingView.changeShape();
                upThrow(mShapeLoadingView, mIndicationIm);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mDownAnimatorSet.start();
    }
}
