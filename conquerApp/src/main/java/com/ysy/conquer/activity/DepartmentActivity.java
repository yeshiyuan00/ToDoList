package com.ysy.conquer.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;
import com.ysy.conquer.R;
import com.ysy.conquer.adapter.MyBaseAdpter;
import com.ysy.conquer.adapter.ViewHolder;
import com.ysy.conquer.config.Constants;
import com.ysy.conquer.util.A;
import com.ysy.conquer.util.T;
import com.ysy.conquer.util.UserDataUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * User: ysy
 * Date: 2015/11/11
 * Time: 10:38
 */
public class DepartmentActivity extends BaseActivity {
    private EditText et;
    private ListView lv;
    private MyTask task;
    private SchoolAdapter adapter;
    private int school_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copyDB();
        school_id = getIntent().getIntExtra("school_id", 0);
        init();
    }

    private void copyDB() {
        try {
            File file = new File(getFilesDir(), Constants.COURSE_DB_NAME);
            if (file.exists() && file.length() > 1300000) {
                L.i("Splash", "数据库文件只需要拷贝一下，如果拷贝了，不需要重新拷贝了");
            } else {
                //数据库文件只需要拷贝一下，如果拷贝了，不需要重新拷贝了
                AssetManager am = getAssets();
                InputStream is = am.open(Constants.COURSE_DB_NAME);
                // 创建一个文件/data/data/包名/files/address.db
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int length = 0;
                while ((length = is.read(buff)) != -1) {
                    fos.write(buff, 0, length);
                }
                fos.flush();
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        et = (EditText) findViewById(R.id.et);
        lv = (ListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentUser.setDep(schools.get(position).name);
                saveInfo();
            }
        });
        task = new MyTask();
        task.execute(school_id + "");
    }

    private void saveInfo() {
        UserDataUtils.UpdateUserData(context, currentUser, new UserDataUtils.UpdateUserDataListener() {
            @Override
            public void onSuccess() {
                A.finishSelf(context);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                T.show(context, "保存失败，请重试");
            }
        });
    }


    private ArrayList<School> schools;

    private class MyTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            schools = getClassroom(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (adapter == null) {
                adapter = new SchoolAdapter(context, schools);
                lv.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<School> getClassroom(String sch) {
        if (sch == null) sch = "";
        final String sql = "select name from departments where school_id = " + school_id;
        L.i(sql);
        ArrayList<School> schools = new ArrayList<School>();
        File file = new File(getFilesDir(), Constants.COURSE_DB_NAME);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            School school = new School();
            school.name = c.getString(0);
            schools.add(school);
        }
        c.close();
        db.close();
        return schools;
    }


    class SchoolAdapter extends MyBaseAdpter<School> {
        public SchoolAdapter(Context context, ArrayList<School> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_school, parent, false);
            }
            TextView tv = ViewHolder.get(convertView, R.id.tv);
            tv.setText(list.get(position).name);
            return convertView;
        }
    }


    class School {
        String name;
    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_choose_school, null);
    }

    @Override
    protected void initTitleBar(ViewGroup rl_title, TextView tv_title, ImageButton ib_back, ImageButton ib_right, View shadow) {
        tv_title.setText("选择院系");
    }
}
