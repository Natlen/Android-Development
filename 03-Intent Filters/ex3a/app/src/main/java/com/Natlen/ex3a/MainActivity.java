package com.Natlen.ex3a;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int REGISTER = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onRegisterClick(View v) {
        //sets the RegActivity class as the intent
        Intent intent = new Intent(this, RegActivity.class);
        // call and activate the RegActivity activity
        startActivityForResult(intent, REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTER && resultCode == Activity.RESULT_OK)
        {
            //gathering the intent's data
            String firstName = data.getStringExtra(RegActivity.FIRST_NAME);
            String lastName = data.getStringExtra(RegActivity.LAST_NAME);
            String gender = data.getStringExtra(RegActivity.GENDER);
            String msg = getString(R.string.wb) + " ";

            // formats the display text
            if (gender.equals(getString(R.string.male)))
                msg += getString(R.string.mr);
            else
                msg += getString(R.string.mrs);
            msg += " " + firstName + ", " + lastName + ".";

            // geting ids to update the activity screen
            TextView tv = findViewById(R.id.intro);
            Button b = findViewById(R.id.register);
            //sets the new display
            tv.setText(msg);
            b.setText(getString(R.string.again));
        }
    }
}