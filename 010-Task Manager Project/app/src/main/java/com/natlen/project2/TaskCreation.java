package com.natlen.project2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TaskCreation extends AppCompatActivity implements TextWatcher {

    public static long task_id = 0;

    public static final String SP_TASK_CREATION = "tasks";
    public static final String SP_META_DATA = "meta_data";


    Activity context = this;

    public static final int RED_PRIORITY = R.color.priority_red;
    public static final int YELLOW_PRIORITY = R.color.priority_yellow;
    public static final int TURQUOISE_PRIORITY = R.color.priority_turquoise;
    public static final int GREEN_PRIORITY = R.color.priority_green;

    public static boolean key = false;
    public static int orientation;

    View priority_red, priority_yellow, priority_turquoise, priority_green;
    EditText    name,
                year, month, day,
                hour, minute,
                location,
                description;
    Button  save;

    Task task = new Task();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_creation);
        loadMetaData();
        bindView();
        task.setPriority(GREEN_PRIORITY);
        task.setStatus("task_incomplete");
        orientation = getResources().getConfiguration().orientation;
        //assigns listeners
            // approach #1 - anonymous listener.
            assignListeners();
            // approach #2 - activity level listener.
            name.addTextChangedListener(this);
            year.addTextChangedListener(this);
            month.addTextChangedListener(this);
            day.addTextChangedListener(this);
            hour.addTextChangedListener(this);
            minute.addTextChangedListener(this);
            location.addTextChangedListener(this);
            description.addTextChangedListener(this);
    }
    void loadMetaData() {
        SharedPreferences meta_data = getSharedPreferences(SP_META_DATA, MODE_PRIVATE);
            task_id = meta_data.getInt("num_of_tasks",0);
    }
    void bindView() {
        priority_red = (View) findViewById(R.id.priority_red);
        priority_yellow = (View) findViewById(R.id.priority_yellow);
        priority_turquoise = (View) findViewById(R.id.priority_turquoise);
        priority_green = (View) findViewById(R.id.priority_green);
        name = (EditText) findViewById(R.id.task_name);
        year = (EditText) findViewById(R.id.task_year);
        month = (EditText) findViewById(R.id.task_month);
        day = (EditText) findViewById(R.id.task_day);
        hour = (EditText) findViewById(R.id.task_hour);
        minute = (EditText) findViewById(R.id.task_minute);
        location = (EditText) findViewById(R.id.task_location);
        description = (EditText) findViewById(R.id.task_description);
        save = (Button) findViewById(R.id.task_save);
        save.setEnabled(false);
    }
    void assignListeners() {
        priority_red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task.setPriority(RED_PRIORITY);
                Toast.makeText(context, R.string.red_priority_had_been_chosen, Toast.LENGTH_SHORT).show();
            }
        });
        priority_yellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task.setPriority(YELLOW_PRIORITY);
                Toast.makeText(context, R.string.yellow_priority_had_been_chosen, Toast.LENGTH_SHORT).show();
            }
        });
        priority_turquoise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task.setPriority(TURQUOISE_PRIORITY);
                Toast.makeText(context, R.string.turquoise_priority_had_been_chosen, Toast.LENGTH_SHORT).show();
            }
        });
        priority_green.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task.setPriority(GREEN_PRIORITY);
                Toast.makeText(context, R.string.green_priority_had_been_chosen, Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                task.setId(++task_id);
                saveData();
                key = true;
                orientation = getResources().getConfiguration().orientation;
                finish();
            }
        });
    }

    public void saveData() {
        SharedPreferences shared_preferences = getSharedPreferences(SP_TASK_CREATION, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_preferences.edit();
        String entry = String.valueOf(task.getId());
        editor.putInt("id" + entry,(int)task.getId());
        editor.putInt("priority" + entry, task.priority);
        editor.putString("name" + entry, task.name);
        editor.putString("date_time" + entry, task.date_time);
        editor.putString("location" + entry, task.location);
        editor.putString("description" + entry, task.description);
        editor.putString("status" + entry,task.status);
        editor.apply();

        SharedPreferences meta_data = getSharedPreferences(SP_META_DATA, MODE_PRIVATE);
        editor = meta_data.edit();
        editor.putBoolean("init", true);
        editor.putInt("num_of_tasks", (int) task_id);
        editor.putInt("current_available_id",-1);
        editor.apply();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // could not care less.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(name.getText().toString().equals("") ||
            year.getText().toString().equals("") ||
            month.getText().toString().equals("") ||
            day.getText().toString().equals("") ||
            location.getText().toString().equals(""))
        {
            save.setEnabled(false);
            return;
        }
        task.setName(name.getText().toString());
        task.setLocation(location.getText().toString());
        String date_time_format = day.getText().toString() + "/" + month.getText().toString() + "/" + year.getText().toString();
        if(!hour.getText().toString().equals("") && !minute.getText().toString().equals(""))
            date_time_format += " " + hour.getText().toString() + ":" + minute.getText().toString();
        else if(!hour.getText().toString().equals("") && minute.getText().toString().equals(""))
            date_time_format += " " + hour.getText().toString() + ":" + "00";
        else if(hour.getText().toString().equals("") && !minute.getText().toString().equals(""))
            date_time_format += " " + "00" + ":" + minute.getText().toString();
        else
            date_time_format += " " + "23:59";
        task.setDate_time(date_time_format);
        if(description.getText().toString().equals(""))
            task.setDescription(" ");
        else
            task.setDescription(description.getText().toString());
        save.setEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // could not care less.
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("priority",task.priority);
        outState.putString("name",name.getText().toString());
        outState.putString("year",year.getText().toString());
        outState.putString("month",month.getText().toString());
        outState.putString("day",day.getText().toString());
        outState.putString("hour",hour.getText().toString());
        outState.putString("minute",minute.getText().toString());
        outState.putString("location",location.getText().toString());
        outState.putString("description",description.getText().toString());
        outState.putInt("orientation", orientation);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null)
        {
            task.setPriority(savedInstanceState.getInt("priority"));
            name.setText(savedInstanceState.getString("name"));
            year.setText(savedInstanceState.getString("year"));
            month.setText(savedInstanceState.getString("month"));
            day.setText(savedInstanceState.getString("day"));
            hour.setText(savedInstanceState.getString("hour"));
            minute.setText(savedInstanceState.getString("minute"));
            location.setText(savedInstanceState.getString("location"));
            description.setText(savedInstanceState.getString("description"));
            orientation = savedInstanceState.getInt("orientation");
        }
    }
}
