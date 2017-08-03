package cn.semtec.community2.model;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import cn.etsoft.smarthome.R;
import cn.semtec.community2.activity.LoginActivity;

/**
 * Created by Ladystyle005 on 2016/7/8.
 */
public class LoginAnimHelper {
    private final MyListener repeatListener;        //动画结束监听
    private final MyUpdateListener updateListener;  //动画实施更新监听
    private int vheight;
    private int dheight;
    private int dwidth;     //drawable的宽度
    private int vwidth;     //控件的宽度，这里取得屏幕的宽度
    private Drawable drawable;      //drawable 图片对象
    private float scale = 1.0f;     //把图片压缩到屏幕高度时的比例
    private ImageView mImageView;   //显示图片的控件
    private int distance;           //压缩后的图片的宽度和控件宽度的差，也就是动画要移动的距离
    private ValueAnimator animator; //动画对象
    private boolean isRightIn;   //是向右显示图片，还是向左

    /**
     * @param act       Activity 对象
     * @param imageView 需要动态的ImageView
     * @param res       drawable图片路径
     */
    public LoginAnimHelper(Activity act, ImageView imageView, int res) {
        mImageView = imageView;

        if (act != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            vheight = metrics.heightPixels;
            vwidth = metrics.widthPixels;   //获取屏幕的宽高
            drawable = act.getResources().getDrawable(R.drawable.login_bg);
            if (drawable != null) {
                dheight = drawable.getIntrinsicHeight();
                dwidth = drawable.getIntrinsicWidth();
                scale = vheight / (float) dheight;  //获取 drawable 并计算出压缩的比例
            }
        }
        repeatListener = new MyListener();
        updateListener = new MyUpdateListener(mImageView.getImageMatrix());

        Matrix matrix = new Matrix();        //Matrix对象 作用是存储参数
        matrix.setScale(scale, scale);      // 设置宽度 和高的压缩比例
        matrix.postTranslate(0, 0);            //显示drawable的位置
        mImageView.setImageMatrix(matrix);  //ImageView设置 Matrix
        mImageView.setImageDrawable(drawable);  //设置图片
        distance = (int) (dwidth * scale) - vwidth; //计算出要移动的距离
    }

    public void RinghtIn() {
        isRightIn = true;
        animator = ValueAnimator.ofInt(0, -distance);
        animator.addUpdateListener(updateListener);
        animator.addListener(repeatListener);
        animator.setDuration(30000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(500);
        animator.start();
    }

    public void LeftIn() {
        isRightIn = false;
        animator = ValueAnimator.ofInt(-distance, 0);
        animator.addUpdateListener(updateListener);
        animator.addListener(repeatListener);
        animator.setDuration(30000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(500);
        animator.start();
    }

    public void stop() {
        animator.removeAllUpdateListeners();
        animator.removeAllListeners();
        animator.cancel();
    }

    public void resume() {
        if (isRightIn) {
            LeftIn();
        } else {
            RinghtIn();
        }
    }

    private class MyUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private Matrix mPrimaryMatrix;

        public MyUpdateListener(Matrix matrix) {
            mPrimaryMatrix = new Matrix(matrix);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int dx = (Integer) animation.getAnimatedValue();
            Matrix matrix = new Matrix(mPrimaryMatrix);
            matrix.postTranslate(dx, 0);
            mImageView.setImageMatrix(matrix);
            Log.i("aaaa", "update");
        }
    }

    private class MyListener implements ValueAnimator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animator.cancel();
            Log.i("aaaa", "end");
            if (LoginActivity.instace == null)
                return;
            if (isRightIn) {
                LeftIn();
            } else {
                RinghtIn();
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
