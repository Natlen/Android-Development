package com.natlen.project2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import java.util.ArrayList;

public class TasksList extends ListFragment {

    private TasksListAdapter tasksListAdapter;
    public static final String SP_TASK_CREATION = "tasks";
    public static final String SP_META_DATA = "meta_data";


    final ArrayList<Task> tasksList = new ArrayList<Task>();

    public static final int PRIORITY = R.id.sort_by_priority;
    public static final int DATE_TIME = R.id.sort_by_date_time;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences meta_data = getActivity().getSharedPreferences(SP_META_DATA, getActivity().MODE_PRIVATE);

        loadTasks();
        tasksListAdapter = new TasksListAdapter(getActivity(), tasksList);
        setListAdapter(tasksListAdapter);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tasksListAdapter.markTaskAsComplete(position);
                return true;
            }
        });
        tasksListAdapter.sortTasksList(meta_data.getInt("sorting_preference",PRIORITY));
    }
    public void loadTasks() {
        SharedPreferences task_sp = getActivity().getSharedPreferences(SP_TASK_CREATION, getActivity().MODE_PRIVATE);
        SharedPreferences meta_data = getActivity().getSharedPreferences(SP_META_DATA, getActivity().MODE_PRIVATE);
        Task task;
        if(meta_data.getBoolean("init",false))
        {
            String task_id;
            for(int i=1; i <= meta_data.getInt("num_of_tasks",1); i++)
            {
                task_id = String.valueOf(i);
                if(task_sp.getInt("id" + task_id,0) != 0)
                {
                    task = new Task();
                    task.setId(task_sp.getInt("id" + task_id, 0));
                    task.setPriority(task_sp.getInt("priority" + task_id, TaskCreation.GREEN_PRIORITY));
                    task.setName(task_sp.getString("name" + task_id, " "));
                    task.setDate_time(task_sp.getString("date_time" + task_id, " "));
                    task.setLocation(task_sp.getString("location" + task_id, " "));
                    task.setDescription(task_sp.getString("description" + task_id, " "));
                    task.setStatus(task_sp.getString("status" + task_id, "task_incomplete"));
                    tasksList.add(task);
                }
            }
        }
   }

    @Override
    public void onResume() {
        // callback upon after pressing "save" on Task Creation activity.
        SharedPreferences task_sp = getActivity().getSharedPreferences(SP_TASK_CREATION, getActivity().MODE_PRIVATE);
        SharedPreferences meta_data = getActivity().getSharedPreferences(SP_META_DATA, getActivity().MODE_PRIVATE);
        super.onResume();
       if(TaskCreation.key)
       {
            String task_id = String.valueOf(meta_data.getInt("num_of_tasks",1));
            Task task = new Task();
            task.setId(task_sp.getInt( "id" + task_id, 0));
            task.setPriority(task_sp.getInt("priority" + task_id, TaskCreation.GREEN_PRIORITY));
            task.setName(task_sp.getString("name" + task_id, " "));
            task.setDate_time(task_sp.getString("date_time" + task_id, " "));
            task.setLocation(task_sp.getString("location" + task_id, " "));
            task.setDescription(task_sp.getString("description" + task_id, " "));
            task.setStatus(task_sp.getString(task_id + "status", "task_incomplete"));
            if(!isAlreadyListed(task))
                tasksListAdapter.createTask(task);
            TaskCreation.key = false;
       }
        tasksListAdapter.sortTasksList(meta_data.getInt("sorting_preference",PRIORITY));
    }
    boolean isAlreadyListed(Task task) {
        for(int i=0; i<tasksList.size(); i++)
            if(tasksList.get(i).getId() == task.getId())
                return true;
        return false;
    }
}
