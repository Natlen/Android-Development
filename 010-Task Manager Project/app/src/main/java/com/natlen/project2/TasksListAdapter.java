package com.natlen.project2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import java.util.ArrayList;
import static com.natlen.project2.TasksList.DATE_TIME;
import static com.natlen.project2.TasksList.PRIORITY;

public class TasksListAdapter extends ArrayAdapter {

    public static final String SP_TASK_CREATION = "tasks";

    private Activity context;
    private ArrayList<Task> tasksList;

    static class taskStruct {
        public TextView priority;
        public TextView name;
        public TextView date_time;
        public TextView location;
        public ImageView status;
    }
    public TasksListAdapter(Activity context, ArrayList<Task> tasksList) {
        super(context, R.layout.task_infrastructure, tasksList);
        this.context = context;
        this.tasksList = tasksList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row_task = convertView;
        // initializes if view does not exist.
            if (convertView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                row_task = inflater.inflate(R.layout.task_infrastructure, null);
                taskStruct task_view = new taskStruct();
                task_view.priority = row_task.findViewById(R.id.task_priority);
                task_view.name = row_task.findViewById(R.id.task_name);
                task_view.date_time = row_task.findViewById(R.id.task_date_time);
                task_view.location = row_task.findViewById(R.id.task_location);
                task_view.status = row_task.findViewById(R.id.task_status);
                row_task.setTag(task_view);
            }

            // sets view's values.
            Task task = tasksList.get(position);
            taskStruct holder = (taskStruct) row_task.getTag(); // retrieves the object stored in this view.
            holder.priority.setBackgroundColor(context.getResources().getColor(task.priority, null));
            holder.name.setText(task.name);
            holder.date_time.setText(task.date_time);
            holder.location.setText(task.location);
                holder.status.setImageResource(getContext().getResources()
                        .getIdentifier(task.status, "drawable", getContext().getPackageName())); // retrieves the img id for this item
            return row_task; // return the view
    }

    public void markTaskAsComplete(int position) {

        SharedPreferences task_sp = context.getSharedPreferences(SP_TASK_CREATION, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = task_sp.edit();
        String entry = String.valueOf(tasksList.get(position).getId());
        if (tasksList.get(position).getStatus().equals("task_incomplete"))
        {
            tasksList.get(position).setStatus("task_completed");
            editor.putString("status" + entry,"task_completed");
            editor.apply();
        }
        else
        {
            tasksList.remove(position);
            editor.remove("id" + entry);
            editor.remove("priority" + entry);
            editor.remove("name" + entry);
            editor.remove("date_time" + entry);
            editor.remove("location" + entry);
            editor.remove("description" + entry);
            editor.remove("status" + entry);
            editor.apply();
        }
        notifyDataSetChanged(); // acknowledges data change - update adapter's view
    }
    public void createTask(Task task) {
        tasksList.add(task);
        notifyDataSetChanged();// acknowledges data change - update adapter's view
    }

    void sortTasksList(int by) {
        switch (by)
        {
            case PRIORITY:
                for(int i=0; i< tasksList.size(); i++)
                    for(int j=0; j< tasksList.size(); j++)
                        if(i != j)
                            if(tasksList.get(i).getPriority() == TaskCreation.RED_PRIORITY)
                                Collections.swap(tasksList,i,j);
                            else if(tasksList.get(i).getPriority() == TaskCreation.YELLOW_PRIORITY
                                    && tasksList.get(j).getPriority() != TaskCreation.RED_PRIORITY)
                                Collections.swap(tasksList,i,j);
                            else if(tasksList.get(i).getPriority() == TaskCreation.TURQUOISE_PRIORITY
                                    && tasksList.get(j).getPriority() != TaskCreation.RED_PRIORITY
                                    && tasksList.get(j).getPriority() != TaskCreation.YELLOW_PRIORITY)
                                Collections.swap(tasksList,i,j);
                break;
            case DATE_TIME:
                for(int i=0; i< tasksList.size(); i++)
                    for(int j=0; j< tasksList.size(); j++)
                        if(i != j)
                            if(strCmp(tasksList.get(i).getDate_time(), tasksList.get(j).getDate_time()) == 1 )
                                Collections.swap(tasksList, i, j);
                            else if(strCmp(tasksList.get(i).getDate_time(), tasksList.get(j).getDate_time()) == 0)
                                sortAdditionallyBy(PRIORITY, j, i); // inverse (i,j) >> (j,i), because of further list inversion.
                Collections.reverse(tasksList);
                break;
        }
        notifyDataSetChanged();
    }

    void sortAdditionallyBy(int by, int i, int j)
    {
        switch (by)
        {
            case PRIORITY:
                if(tasksList.get(i).getPriority() == TaskCreation.RED_PRIORITY)
                    Collections.swap(tasksList,i,j);
                else if(tasksList.get(i).getPriority() == TaskCreation.YELLOW_PRIORITY
                        && tasksList.get(j).getPriority() != TaskCreation.RED_PRIORITY)
                    Collections.swap(tasksList,i,j);
                else if(tasksList.get(i).getPriority() == TaskCreation.TURQUOISE_PRIORITY
                        && tasksList.get(j).getPriority() != TaskCreation.RED_PRIORITY
                        && tasksList.get(j).getPriority() != TaskCreation.YELLOW_PRIORITY)
                    Collections.swap(tasksList,i,j);
                break;
        }
    }

    int firstIndexOf(String str, char ch) {
        for(int i=0; i<str.length(); i++)
            if(str.toCharArray()[i] == ch)
                return i;
        return -1;
    }

    int strCmp(String str1, String str2) {
        String  str1_cpy, str2_cpy,
                day1, month1, year1, hour1, minute1,
                day2, month2, year2, hour2, minute2;
        str1_cpy = str1.substring(0);
        str2_cpy = str2.substring(0);
        try {
            day1 = str1_cpy.substring(0, firstIndexOf(str1_cpy, '/'));
            str1_cpy = str1_cpy.substring(firstIndexOf(str1_cpy, '/') + 1);
            month1 = str1_cpy.substring(0, firstIndexOf(str1_cpy, '/'));
            str1_cpy = str1_cpy.substring(firstIndexOf(str1_cpy, '/') + 1);
            year1 = str1_cpy.substring(0, firstIndexOf(str1_cpy, ' '));
            str1_cpy = str1_cpy.substring(firstIndexOf(str1_cpy, ' ') + 1);
            hour1 = str1_cpy.substring(0, firstIndexOf(str1_cpy, ':'));
            str1_cpy = str1_cpy.substring(firstIndexOf(str1_cpy, ':') + 1);
            minute1 = str1_cpy;

            day2 = str2_cpy.substring(0, firstIndexOf(str2_cpy, '/'));
            str2_cpy = str2_cpy.substring(firstIndexOf(str2_cpy, '/') + 1);
            month2 = str2_cpy.substring(0, firstIndexOf(str2_cpy, '/'));
            str2_cpy = str2_cpy.substring(firstIndexOf(str2_cpy, '/') + 1);
            year2 = str2_cpy.substring(0, firstIndexOf(str2_cpy, ' '));
            str2_cpy = str2_cpy.substring(firstIndexOf(str2_cpy, ' ') + 1);
            hour2 = str2_cpy.substring(0, firstIndexOf(str2_cpy, ':'));
            str2_cpy = str2_cpy.substring(firstIndexOf(str2_cpy, ':') + 1);
            minute2 = str2_cpy;

            if (Integer.valueOf(year1) > Integer.valueOf(year2))
                return 1;
            else if (Integer.valueOf(year1) < Integer.valueOf(year2))
                return -1;
            if (Integer.valueOf(month1) > Integer.valueOf(month2))
                return 1;
            else if (Integer.valueOf(month1) < Integer.valueOf(month2))
                return -1;
            if (Integer.valueOf(day1) > Integer.valueOf(day2))
                return 1;
            else if (Integer.valueOf(day1) < Integer.valueOf(day2))
                return -1;
            if (Integer.valueOf(hour1) > Integer.valueOf(hour2))
                return 1;
            else if (Integer.valueOf(hour1) < Integer.valueOf(hour2))
                return -1;
            if (Integer.valueOf(minute1) > Integer.valueOf(minute2))
                return 1;
            else if (Integer.valueOf(minute1) < Integer.valueOf(minute2))
                return -1;
            return 0;
        }
        catch (Exception e) {
            return 0;
        }
    }
}
