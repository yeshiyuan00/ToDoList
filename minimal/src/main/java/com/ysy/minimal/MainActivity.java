package com.ysy.minimal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewEmptySupport mRecyclerView;
    private FloatingActionButton mAddToDoItemFAB;
    private ArrayList<ToDoItem> mToDoItemsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToDoItemsArrayList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mToDoItemsArrayList = new ArrayList<>();
        makeUpItems(mToDoItemsArrayList, 50);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addToDoItemFAB);

        mRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.toDoRecyclerView);
        mRecyclerView.setEmptyView(findViewById(R.id.toDoEmptyView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        BasicListAdapter basicListAdapter = new BasicListAdapter(mToDoItemsArrayList);
        mRecyclerView.setAdapter(basicListAdapter);
    }

    public void makeUpItems(ArrayList<ToDoItem> items, int len) {
        for (int i = 0; i < len; i++) {
            ToDoItem item = new ToDoItem("" + i, false, new Date());
            items.add(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> {

        private ArrayList<ToDoItem> items;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            ToDoItem item = items.get(position);
            holder.mCheckBox.setChecked(item.HasReminder());
            holder.mTextview.setText(item.getToDoText());
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(v, "Clicked" + position, Snackbar.LENGTH_SHORT);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            Log.d("OskarSchindler", "Count " + items.size());
            return items.size();
        }

        BasicListAdapter(ArrayList<ToDoItem> items) {
            this.items = items;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            CheckBox mCheckBox;
            TextView mTextview;

            public ViewHolder(View v) {
                super(v);
                mView = v;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Clicked on" + getAdapterPosition(), Snackbar.LENGTH_SHORT).show();
                    }
                });
                mCheckBox = (CheckBox) v.findViewById(R.id.toDoListItemCheckBox);
                mTextview = (TextView) v.findViewById(R.id.toDoListItemTextview);
            }
        }
    }
}
