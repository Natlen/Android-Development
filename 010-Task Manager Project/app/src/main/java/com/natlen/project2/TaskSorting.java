package com.natlen.project2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class TaskSorting extends AppCompatActivity implements RadioButton.OnClickListener {

    public static final String SP_META_DATA = "meta_data";
    RadioButton sort_priority, sort_date_time;
    Button ret_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_sorting);

        SharedPreferences meta_data = getSharedPreferences(SP_META_DATA, MODE_PRIVATE);

        sort_priority = (RadioButton) findViewById(R.id.sort_by_priority);
        sort_date_time = (RadioButton) findViewById(R.id.sort_by_date_time);
        ret_btn = (Button) findViewById(R.id.return_btn);

        sort_priority.setOnClickListener(this);
        sort_date_time.setOnClickListener(this);
        ret_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch(meta_data.getInt("sorting_preference", R.id.sort_by_priority))
        {
            case R.id.sort_by_priority:
                sort_priority.setChecked(true);
                break;
            case R.id.sort_by_date_time:
                sort_date_time.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences meta_data = getSharedPreferences(SP_META_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = meta_data.edit();
        int sorting_preference = -1;
        switch (v.getId())
        {
            case R.id.sort_by_priority:
                sorting_preference = R.id.sort_by_priority;
                break;
            case R.id.sort_by_date_time:
                sorting_preference = R.id.sort_by_date_time;
        }
        editor.putInt("sorting_preference",sorting_preference);
        editor.apply();
    }
}
