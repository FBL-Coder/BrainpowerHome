package cn.etsoft.smarthome.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.mybaseactivity.OtherUtils.AppSharePreferenceMgr;

import java.util.ArrayList;

import cn.etsoft.smarthome.Adapter.ViewPage.Welcome_ViewPaperAdapter;
import cn.etsoft.smarthome.MyApplication;
import cn.etsoft.smarthome.R;

/**
 * Author：FBL  Time： 2017/6/13.
 * 第一次启动页面
 */
public class GuideActivity extends Activity {
    private ViewPager mViewPager;
    private Button mBtnGo;
    private Welcome_ViewPaperAdapter vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((Integer) AppSharePreferenceMgr.get(MyApplication.getContext(), "IsFirst", 0) == 1) {
            startActivity(new Intent(GuideActivity.this, WelcomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_guide);
        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        mBtnGo = (Button) findViewById(R.id.btn_go);

        //实例化各个界面的布局对象
        View view1 = View.inflate(this, R.layout.viewpage_guide_item, null);
        View view2 = View.inflate(this, R.layout.viewpage_guide_item, null);
        View view3 = View.inflate(this, R.layout.viewpage_guide_item, null);

        ((ImageView) view1.findViewById(R.id.tv_pic)).setImageResource(R.drawable.one_1);
        ((ImageView) view2.findViewById(R.id.tv_pic)).setImageResource(R.drawable.two_2);
        ((ImageView) view3.findViewById(R.id.tv_pic)).setImageResource(R.drawable.three_3);

        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSharePreferenceMgr.put("IsFirst", 1);
                startActivity(new Intent(GuideActivity.this, WelcomeActivity.class));
                finish();
            }
        });

        view1.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        view2.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        view3.findViewById(R.id.tv_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

        ((TextView) view1.findViewById(R.id.tv_title)).setText("科技生活");
        ((TextView) view1.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#FF5000"));
        ((TextView) view2.findViewById(R.id.tv_title)).setText("便捷操作");
        ((TextView) view2.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#49ca65"));
        ((TextView) view3.findViewById(R.id.tv_title)).setText("个性DIY场景");
        ((TextView) view3.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#16c5c6"));

        ((TextView) view1.findViewById(R.id.tv_desc)).setText("科技与生活的融合\n");
        ((TextView) view2.findViewById(R.id.tv_desc)).setText("给你最好的\n做你最想要的");
        ((TextView) view3.findViewById(R.id.tv_desc)).setText("随心所欲\n精彩由你");

        mViewPager = (ViewPager) findViewById(R.id.vp_guide);

        ArrayList<View> views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        vpAdapter = new Welcome_ViewPaperAdapter(views);

        mViewPager.setOffscreenPageLimit(views.size());
        mViewPager.setPageMargin(-dip2px(135));
        mViewPager.setAdapter(vpAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position + 1 == mViewPager.getAdapter().getCount()) {
                    if (mBtnGo.getVisibility() != View.VISIBLE) {
                        mBtnGo.setVisibility(View.VISIBLE);
                        mBtnGo.startAnimation(AnimationUtils.loadAnimation(GuideActivity.this, android.R.anim.fade_in));
                    }
                } else {
                    if (mBtnGo.getVisibility() != View.GONE) {
                        mBtnGo.setVisibility(View.GONE);
                        mBtnGo.startAnimation(AnimationUtils.loadAnimation(GuideActivity.this, android.R.anim.fade_out));
                    }
                }
            }
        });

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
//            private static final float MIN_ALPHA = 0.5f;

            private static final float MIN_TXT_SCALE = 0.0f;
            private static final float MIN_TXT_ALPHA = 0.0f;

            @SuppressLint("NewApi")
            @Override
            public void transformPage(View view, float position) {
                View mGuideImage = view.findViewById(R.id.tv_pic);
                View mTitle = view.findViewById(R.id.tv_title);
                View mDesc = view.findViewById(R.id.tv_desc);

                int viewWidth = mDesc.getWidth();

                if (position < -1) {
                    mTitle.setAlpha(0);
                    mDesc.setAlpha(0);
                } else if (position <= 1) {
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float scaleTxtFactor = Math.max(MIN_TXT_SCALE, 1 - Math.abs(position));

                    mGuideImage.setScaleX(scaleFactor);
                    mGuideImage.setScaleY(scaleFactor);

                    mTitle.setScaleX(scaleTxtFactor);
                    mTitle.setScaleY(scaleTxtFactor);
                    mTitle.setAlpha(MIN_TXT_ALPHA + (scaleTxtFactor - MIN_TXT_SCALE) / (1 - MIN_TXT_SCALE) * (1 - MIN_TXT_ALPHA));

                    mDesc.setAlpha(mTitle.getAlpha());
                    mDesc.setScaleX(scaleTxtFactor);
                    mDesc.setScaleY(scaleTxtFactor);
                } else {
                    mTitle.setAlpha(0);
                    mDesc.setAlpha(0);
                }
            }
        });
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
