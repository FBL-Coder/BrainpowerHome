package com.example.abc.mybaseactivity.BaseFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abc.mybaseactivity.BaseActivity.BaseActivity;

/**
 * 基础的Fragment，该类简化了原始类的生命周期，并增加了一些常用方法，强烈建议Fragment继承该类
 * 作者：FBL  时间： 2017/5/4.
 */

public abstract class BaseFragment extends Fragment {
    //TODO 下拉刷新，下拉加载为封装未完成。待完成！！
    /**
     * 贴附的activity
     */
    protected Activity mActivity;

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (Activity) activity;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);

        initFragment();

        return mRootView;
    }

    public void initFragment() {

        initView();

        initData(getArguments());

        mIsPrepare = true;

        onLazyLoad();

        setListener();
    }

    /**
     * 初始化数据
     *
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    public abstract void initData(Bundle arguments);

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 设置监听事件
     */
    protected abstract void setListener();

    /**
     * 懒加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.mIsVisible = isVisibleToUser;
        if (isVisibleToUser) {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisibleToUser() {
        if (mIsPrepare && mIsVisible) {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     */
    protected void onLazyLoad() {

    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id) {
        if (mRootView == null) {
            return null;
        }
        return (T) mRootView.findViewById(id);
    }

    /**
     * 设置Fragment布局资源id
     */
    protected abstract int setLayoutResouceId();

}
