package com.rkcodesolution.lab08;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String MYTAG = "MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /** Main menu item selection */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:     // settings item click.
                FragmentManager fm = getSupportFragmentManager();
                Fragment toHide = fm.findFragmentById(R.id.fragment);
                FragmentTransaction ft = fm.beginTransaction();
                if (toHide != null) {
                    ft.hide(toHide);    // hide main fragment.
                }
                ft.add(R.id.settings_container, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
