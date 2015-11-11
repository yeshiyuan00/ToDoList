package com.ysy.conquer.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ysy.conquer.R;

/**
 * User: ysy
 * Date: 2015/11/11
 * Time: 12:06
 */
public class MainActivity extends BaseActivity {
    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.common_title, null);
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {

    }
}
