package com.example.abc.mybaseactivity.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.mybaseactivity.MyApplication.MyApplication;
import com.example.abc.mybaseactivity.NetWorkListener.NetBroadcastReceiver;
import com.example.abc.mybaseactivity.NetWorkListener.NetUtil;
import com.example.abc.mybaseactivity.OtherUtils.UIUtils;
import com.example.abc.mybaseactivity.R;

/**
 * BaseActivity
 * 作者：FBL  时间： 2017/4/27.
 */
public abstract class BaseActivity extends FragmentActivity {

    private RelativeLayout title_layout;
    private TextView network, titleText;
    private ImageView leftBack, rightMore;
    private FrameLayout llcontent_other;
    private LayoutInflater inflater;
    /**
     * 状态栏颜色
     */
    private int SystemTitleColor = R.color.colorAccent;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉系统的TitleBar
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        try {
            //添加Activity在ActivityList中
            MyApplication.addActivity(this);
            //初始化BaseActivity
            initBaseActivity();
            //加载布局以及控件
            initView();
            //数据处理
            initData();
        } catch (Exception e) {
            Log.e("Base_Exception", e + "");

            //异常后自动重启
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }

    /**
     * 设置系统状态栏颜色
     *
     * @param colorResourceid 颜色(必须是16进制的颜色值)
     */
    public void setStatusColor(int colorResourceid) {
        int color = UIUtils.getColor(colorResourceid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);

            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        } else {

            Window window = this.getWindow();
            ViewGroup mContentView = (ViewGroup) this.findViewById(Window.ID_ANDROID_CONTENT);

            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(this);

            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                //如果已经为 ChildView 设置过了 marginTop, 再次调用时直接跳过
                if (lp != null && lp.topMargin < statusBarHeight && lp.height != statusBarHeight) {
                    //不预留系统空间
                    ViewCompat.setFitsSystemWindows(mChildView, false);
                    lp.topMargin += statusBarHeight;
                    mChildView.setLayoutParams(lp);
                }
            }

            View statusBarView = mContentView.getChildAt(0);
            if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == statusBarHeight) {
                //避免重复调用时多次添加 View
                statusBarView.setBackgroundColor(color);
                return;
            }
            statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            statusBarView.setBackgroundColor(color);
            //向 ContentView 中添加假 View
            mContentView.addView(statusBarView, 0, lp);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context 环境
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 初始化BaseActivity
     */
    protected void initBaseActivity() {
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);
        network = (TextView) findViewById(R.id.no_network);
        titleText = (TextView) findViewById(R.id.title_center);
        leftBack = (ImageView) findViewById(R.id.left_back);
        rightMore = (ImageView) findViewById(R.id.right_more);
        llcontent_other = (FrameLayout) findViewById(R.id.llcontent_other);


        //判断网络是否可用
        if (NetUtil.getNetWorkState(BaseActivity.this) == NetUtil.NETWORK_NONE)
            network.setVisibility(View.VISIBLE);
        else network.setVisibility(View.GONE);
        //网络改变监听
        NetBroadcastReceiver.setEvevt(new NetBroadcastReceiver.NetEvevtChangListener() {
            @Override
            public void onNetChange(int netMobile) {
                getNetChangeListener.NetChange();
                if (netMobile == NetUtil.NETWORK_NONE)
                    network.setVisibility(View.VISIBLE);
                else network.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 设置标题栏是否显示
     *
     * @param isVisible       是否显示
     * @param colorResourceid 背景颜色
     */
    public void setTitleViewVisible(boolean isVisible, int colorResourceid) {
        if (!isVisible) title_layout.setVisibility(View.GONE);
        else {
            title_layout.setVisibility(View.VISIBLE);
            if (colorResourceid == 0)
                title_layout.setBackgroundResource(SystemTitleColor);
            else
                title_layout.setBackgroundResource(colorResourceid);
        }
    }

    /**
     * 获取标题栏布局
     *
     * @return 标题栏布局
     */
    public View getTitleView() {
        return title_layout;
    }

    /**
     * 获取左边图标按钮
     *
     * @return 左边图标控件
     */
    public View getLiftImage() {
        return leftBack;
    }

    /**
     * 获取右边图标按钮
     *
     * @return 右边图标控件
     */
    public View getRightImage() {
        return rightMore;
    }

    /**
     * 设置标题栏图片资源
     *
     * @param isVisible_left        是否显示左边图片按钮
     * @param imageResourceid_left  左边图按钮资源
     * @param isVisible_right       是否显示右边图片按钮
     * @param imageResourceid_right 右边图按钮资源
     */
    public void setTitleImageBtn(boolean isVisible_left, int imageResourceid_left, boolean isVisible_right, int imageResourceid_right) {

        if (isVisible_left) {
            leftBack.setVisibility(View.VISIBLE);
            if (imageResourceid_left != 0)
                leftBack.setImageResource(imageResourceid_left);
        } else leftBack.setVisibility(View.GONE);
        if (isVisible_right) {
            rightMore.setVisibility(View.VISIBLE);
            if (imageResourceid_right != 0)
                rightMore.setImageResource(imageResourceid_right);
        } else rightMore.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param textContent 标题内容
     * @param textSize    字体大小
     * @param textColor   字体颜色
     */
    public void setTitleText(String textContent, int textSize, int textColor) {
        titleText.setText(textContent);
        titleText.setTextSize(textSize);
        titleText.setTextColor(UIUtils.getColor(textColor));
    }

    /**
     * 查找组件
     *
     * @param viewId：View的ID
     * @return View
     */
    @SuppressWarnings("unchecked")
    protected <view extends View> view getViewById(int viewId) {
        return (view) findViewById(viewId);
    }

    /**
     * 设置页面布局
     *
     * @param layoutID 布局资源ID
     */
    public void setLayout(int layoutID) {
        llcontent_other.addView(inflater.inflate(layoutID, null));
    }

    /**
     * 初始化布局控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();


    public static getNetChangeListener getNetChangeListener;

    public static void setGetNetChangeListener(BaseActivity.getNetChangeListener getNetChangeListener) {
        BaseActivity.getNetChangeListener = getNetChangeListener;
    }

    public interface getNetChangeListener {
        void NetChange();
    }
}



