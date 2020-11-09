package com.natlen.ex3b;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.Natlen.ex3b.R;

public class RegActivity extends AppCompatActivity {
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String GENDER = "Gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);
    }

    public void sendback(View v)
    {
        //gathering activity-lvl data.
        EditText fname_et = findViewById(R.id.fname);
        EditText lname_et = findViewById(R.id.lname);
        RadioGroup rg = findViewById(R.id.gender);
        RadioButton gender_rb = findViewById(rg.getCheckedRadioButtonId());

        //casting into app-lvl data.
        String fname_str = fname_et.getText().toString();
        String lname_str = lname_et.getText().toString();
        if(fname_str.equals("") || lname_str.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Name is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        String gender_str = gender_rb.getText().toString();

        //wrapping data into Intend.
        Intent intednt = new Intent();
        intednt.putExtra(FIRST_NAME,fname_str);
        intednt.putExtra(LAST_NAME,lname_str);
        intednt.putExtra(GENDER,gender_str);

        //sending the wrapped data.
        setResult(RESULT_OK,intednt);

        //returning to whomever called upon this activity.
        finish();
    }
}
