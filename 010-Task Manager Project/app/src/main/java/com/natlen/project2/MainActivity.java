package com.natlen.project2;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ExitDialog.Handler {

    public static final String SP_META_DATA = "meta_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_list);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences meta_data = getSharedPreferences(SP_META_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = meta_data.edit();
        switch (item.getItemId())
        {
            case R.id.new_task:
                startActivity(new Intent(this, TaskCreation.class));
                return true;
            case R.id.activate_service:
                Intent taskSavingService = new Intent(this, TaskSavingService.class);
                if(!meta_data.getBoolean("service_activation",false))
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
                    {
                        // request the permission
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1234);
                    }
                    else
                    {
                        startService(taskSavingService);
                        editor.putBoolean("service_activation", true);
                        editor.apply();
                    }

                }
                else
                {
                    stopService(taskSavingService);
                    editor.putBoolean("service_activation", false);
                    editor.apply();
                }
                return true;
            case R.id.exit:
                new ExitDialog().show(getSupportFragmentManager(), "Exit Dialog");
                return true;
            case R.id.sorting:
                startActivity(new Intent(this, TaskSorting.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onExitChoose() {
        finishAndRemoveTask();
    }
}
