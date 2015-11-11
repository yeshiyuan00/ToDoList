package com.ysy.conquer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ysy.conquer.R;
import com.ysy.conquer.util.A;
import com.ysy.conquer.util.AlertDialogUtils;
import com.ysy.conquer.util.T;
import com.ysy.conquer.util.UserDataUtils;

/**
 * User: ysy
 * Date: 2015/11/11
 * Time: 12:19
 */
public class SchoolInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView year;
    private TextView department;
    private TextView school;
    private Button bt_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        school = (TextView) findViewById(R.id.school);
        department = (TextView) findViewById(R.id.department);
        year = (TextView) findViewById(R.id.year);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        school.setEnabled(false);
        department.setEnabled(false);
        year.setEnabled(false);
        findViewById(R.id.ll_school).setOnClickListener(this);
        findViewById(R.id.ll_department).setOnClickListener(this);
        findViewById(R.id.ll_year).setOnClickListener(this);
    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_schoolinfo, null);
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {
        tv_title.setText("完善信息");
        ib_back.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (currentUser == null) {
            T.show(context, "请先登录");
            return;
        }
        switch (v.getId()) {
            case R.id.ll_school:
            case R.id.ll_department:
                A.goOtherActivity(context, SchoolActivity.class);
                break;
            case R.id.ll_year:
                final String[] years = new String[] { "2012年", "2012年", "2013年", "2014年" };
                AlertDialogUtils.showChiceGender(context, years, 0, new AlertDialogUtils.OkCallBack() {
                    @Override
                    public void onOkClick(DialogInterface dialog, int which) {
                        currentUser.setYear(years[which]);
                        year.setText(years[which]);
                        if (!TextUtils.isEmpty(school.getText().toString().trim())
                                && !TextUtils.isEmpty(department.getText().toString().trim())
                                && !TextUtils.isEmpty(year.getText().toString().trim())) {
                            bt_save.setVisibility(View.VISIBLE);
                        }
                    }
                });
                break;
            case R.id.bt_save:
                saveInfo();
                break;
        }
    }

    private void saveInfo() {
        UserDataUtils.UpdateUserData(context, currentUser, new UserDataUtils.UpdateUserDataListener() {
            @Override
            public void onSuccess() {
                bt_save.setEnabled(false);
                A.goOtherActivityFinish(context, MainActivity.class);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                bt_save.setEnabled(true);
                T.show(context, "保存失败，请重试");
            }
        });
    }
}
