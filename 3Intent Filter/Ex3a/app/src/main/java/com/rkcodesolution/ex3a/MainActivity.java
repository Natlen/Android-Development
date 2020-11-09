package com.rkcodesolution.ex3a;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    public static final int REGISTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * On Register button click - Call second activity.
     * @param v
     */
    public void onRegisterClick(View v) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivityForResult(intent, REGISTER);
    }

    /**
     * Getting back from SecondActivity.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REGISTER && resultCode == Activity.RESULT_OK) {

            /* Read Data from intent */
            String firstName = data.getStringExtra(SecondActivity.FIRST_NAME);
            String lastName = data.getStringExtra(SecondActivity.LAST_NAME);
            String gender = data.getStringExtra(SecondActivity.GENDER);

            /* Build string to display */
            String textToDisplay = getString(R.string.welcome_back) + " ";
            if (gender.equals(getString(R.string.male)))
                textToDisplay += getString(R.string.mister);
            else textToDisplay += getString(R.string.misiz);
            textToDisplay += " " + firstName + ", " + lastName + ".";

            /* Set text*/
            TextView textView = findViewById(R.id.welcome_text);
            textView.setText(textToDisplay);
            Button registerButton = findViewById(R.id.register);
            registerButton.setText(R.string.again);
        }

    }
}
