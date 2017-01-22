package cn.semtec.community2.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import cn.etsoft.smarthome.R;


/**
 * 倒计时
 * 验证码获取
 */
public class TimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private TextView btn;//按钮

    public TimeCountUtil(Activity mActivity, long millisInFuture,
                         long countDownInterval, TextView btn) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
    }

    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {

        btn.setEnabled(false);//设置不能点击
        btn.setTextColor(mActivity.getResources().getColor(R.color.regist_gray1));
        btn.setText(millisUntilFinished / 1000 + "秒");//设置倒计时时间
//	     Spannable span = new SpannableString(btn.getText().toString());//获取按钮的文字
//	     span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //讲倒计时时间显示为红色
//	     btn.setText(span);
    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setEnabled(true);//重新获得点击
        btn.setText("获取验证");
        btn.setTextColor(mActivity.getResources().getColor(R.color.white1));
    }
}


