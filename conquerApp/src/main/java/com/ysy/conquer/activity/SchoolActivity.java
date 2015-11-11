package com.ysy.conquer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ysy.conquer.util.CollectionUtils;
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
 * Time: 12:19
 */
public class SchoolActivity extends BaseActivity {
    private EditText et;
    private ListView lv;
    private MyTask task;
    private SchoolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        copyDB();
        init();
    }

    private void copyDB() {
        try {
            File file = new File(getFilesDir(), Constants.COURSE_DB_NAME);
            if (file.exists() && file.length() > 1300000) {
                L.i("Splash", "数据库文件只需要拷贝一下，如果拷贝了，不需要重新拷贝了");
            } else {
                // 数据库文件只需要拷贝一下，如果拷贝了，不需要重新拷贝了。
                AssetManager am = getAssets();
                InputStream is = am.open(Constants.COURSE_DB_NAME);

                // 创建一个文件/data/data/包名/files/address.db
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
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
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (task != null) {
                    task.cancel(true);
                    task = null;
                }
                task = new MyTask();
                task.execute(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentUser.setSchool(schools.get(position).name);
                saveInfo(schools.get(position).id);
            }
        });
        task = new MyTask();
        task.execute("");
    }

    private void saveInfo(final int school_id) {
        UserDataUtils.UpdateUserData(context, currentUser, new UserDataUtils.UpdateUserDataListener() {
            @Override
            public void onSuccess() {
                Intent i = new Intent(context, DepartmentActivity.class);
                i.putExtra("school_id", school_id);
                A.goOtherActivityFinish(context,i);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                T.show(context, "保存失败，请重试");
            }
        });
    }

    private ArrayList<School> schools = new ArrayList<School>();

    class MyTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            ArrayList<School> s = getClassroom(params[0]);
            if (CollectionUtils.isNotNull(s)) {
                schools.clear();
                schools.addAll(s);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (adapter == null) {
                adapter = new SchoolAdapter(context, schools);
                lv.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }
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


    private ArrayList<School> getClassroom(String sch) {
        if (sch == null) sch = "";
        final String sql = "select id,name from schools where name like '" + "%" + sch + "%'";
        L.i(sql);
        ArrayList<School> schools = new ArrayList<School>();
        File file = new File(getFilesDir(), Constants.COURSE_DB_NAME);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            School school = new School();
            school.id = c.getInt(0);
            school.name = c.getString(1);
            schools.add(school);
        }
        c.close();
        db.close();
        return schools;
    }

    class School {
        String name;
        int id;
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
