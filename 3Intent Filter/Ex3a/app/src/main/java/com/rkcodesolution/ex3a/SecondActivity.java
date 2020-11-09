package com.rkcodesolution.ex3a;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    /* Keys for saving data */
    public static final String FIRST_NAME = "fname";
    public static final String LAST_NAME = "lname";
    public static final String GENDER = "gender";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    /**
     * On Send Back button click
     * @param v
     */
    public void sendBack(View v) {

        /* Read and validate user input */
        EditText firstNameEditText = findViewById(R.id.first_name);
        EditText lastNameEditText = findViewById(R.id.last_name);
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        if (firstName.equals("") || lastName.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill First AND Last name!", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Create new intent to pass user data */
        Intent intent = new Intent();
        intent.putExtra(FIRST_NAME, firstName);
        intent.putExtra(LAST_NAME, lastName);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        String genderText = radioButton.getText().toString();
        intent.putExtra(GENDER, genderText);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

