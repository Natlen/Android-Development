package com.natlen.project2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public static final String SP_TASK_CREATION = "tasks";
    public static final String SP_META_DATA = "meta_data";
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            SmsMessage smsMessage = msgs[0];
            if (smsMessage != null)
            {
                saveData(formatRawDataIntoTask(smsMessage.getDisplayMessageBody()));
            }
        }
    }
    int firstIndexOf(String str, char ch) {
        for(int i=0; i<str.length(); i++)
            if(str.toCharArray()[i] == ch)
                return i;
        return -1;
    }
    public Task formatRawDataIntoTask(String raw_data) {
        SharedPreferences meta_data = context.getSharedPreferences(SP_META_DATA, context.MODE_PRIVATE);
        Task task = new Task();
        String raw_data_cpy = raw_data.substring(0);
        String current;
        String operation;
        try {
            operation = raw_data_cpy.substring(0, firstIndexOf(raw_data_cpy, '\n'));
        }
        catch (Exception e) {
            // not a task.
            return null;
        }
        if (!operation.equals("משימה"))
            return null; // not a task.
        try {
            // trying as if the sms written within the expected format.
            raw_data_cpy = raw_data_cpy.substring(firstIndexOf(raw_data_cpy, '\n') + 1);

            //as for the task's NAME.
            current = raw_data_cpy.substring(0, firstIndexOf(raw_data_cpy, '\n')); // retrieves the current arg.
            task.setName(current);
            raw_data_cpy = raw_data_cpy.substring(firstIndexOf(raw_data_cpy, '\n') + 1); // moves towards the next arg.

            //as for the task's DATE & TIME
            current = raw_data_cpy.substring(0, firstIndexOf(raw_data_cpy, '\n')); // retrieves the current arg.
            if(current.lastIndexOf(':') == -1)
                current += " " + "23:59";
            task.setDate_time(current);
            raw_data_cpy = raw_data_cpy.substring(firstIndexOf(raw_data_cpy, '\n') + 1); // moves towards the next arg.

            //as for the task's LOCATION
            current = raw_data_cpy.substring(0, firstIndexOf(raw_data_cpy, '\n')); // retrieves the current arg.
            task.setLocation(current);
            raw_data_cpy = raw_data_cpy.substring(firstIndexOf(raw_data_cpy, '\n') + 1); // moves towards the next arg.
        }
        catch (Exception e)
        {
            // not written within the expected format.
            return null;
        }
        // should it reach this point - sms is a confirmed task.
        try {
            boolean dont_substring = false;
            //as for the task's PRIORITY.
            current = raw_data_cpy.substring(0, firstIndexOf(raw_data_cpy, '\n')); // retrieves the current arg.
            if (current.equals("אדום"))
                task.setPriority(R.color.priority_red);
            else if (current.equals("צהוב"))
                task.setPriority(R.color.priority_yellow);
            else if (current.equals("כחול"))
                task.setPriority(R.color.priority_turquoise);
            else if(current.equals("ירוק"))
                task.setPriority(R.color.priority_green);
            else {
                task.setPriority(R.color.priority_green); // as a default.
                dont_substring = true;
            }
            if(!dont_substring)
                raw_data_cpy = raw_data_cpy.substring(firstIndexOf(raw_data_cpy, '\n') + 1); // moves towards the next arg.
        }
        catch (Exception e) {
            // PRIORITY unspecified - it's alright.
            task.setPriority(R.color.priority_green); // as a default.
        }
        try {
            //as for the task's PRIORITY.
            current = raw_data_cpy.substring(0, firstIndexOf(raw_data_cpy, '\n')); // retrieves the current arg.
            task.setDescription(current);
            raw_data_cpy = raw_data_cpy.substring(firstIndexOf(raw_data_cpy, '\n') + 1); // moves towards the next arg.
        }
        catch (Exception e) {
            // Description unspecified - it's alright.
            task.setDescription(" "); // as a default.
        }
        task.setId((long)meta_data.getInt("num_of_tasks", 1) + 1);
        task.setStatus("task_incomplete");
        return task;
    }

    public void saveData(Task task) {
        if(task == null)
            return;
        SharedPreferences tasks_sp = context.getSharedPreferences(SP_TASK_CREATION, context.MODE_PRIVATE);
        SharedPreferences meta_data = context.getSharedPreferences(SP_META_DATA, context.MODE_PRIVATE);

        SharedPreferences.Editor editor = meta_data.edit();
        editor.putInt("num_of_tasks",meta_data.getInt("num_of_tasks", 0) + 1);
        editor.putBoolean("init", true);
        editor.apply();

        editor = tasks_sp.edit();
        String entry = String.valueOf(meta_data.getInt("num_of_tasks",0));
        editor.putInt("id" + entry,(int)task.getId());
        editor.putInt("priority" + entry, task.priority);
        editor.putString("name" + entry, task.name);
        editor.putString("date_time" + entry, task.date_time);
        editor.putString("location" + entry, task.location);
        editor.putString("description" + entry, task.description);
        editor.putString("status" + entry,task.status);
        editor.apply();
    }
}
