package com.ysy.conquer.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lidroid.xutils.DbUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ysy.conquer.CustomApplication;
import com.ysy.conquer.R;
import com.ysy.conquer.bean.User;
import com.ysy.conquer.util.SystemBarTintManager;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

/**
 * User: ysy
 * Date: 2015/10/28
 * Time: 10:51
 */
public abstract class BaseActivity extends FragmentActivity {
    protected Context context;
    protected BmobUserManager userManager;
    protected BmobChatManager manager;
    protected CustomApplication mApplication;
    protected User currentUser;
    private int mScreenWidth;
    private int mScreenHeight;
    private ImageLoader loader;
    private DisplayImageOptions option_photo, option_pic;
    DbUtils dbUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initStatusBar();
    }

    /**
     * 沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initStatusBar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.title));
    }


}
