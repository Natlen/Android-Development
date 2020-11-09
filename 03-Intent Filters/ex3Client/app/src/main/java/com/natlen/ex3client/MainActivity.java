package com.natlen.ex3client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REGISTER = 1001;
    public static final String FIRST_NAME = "First Name";
    public static final String LAST_NAME = "Last Name";
    public static final String GENDER = "Gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public String getEditText(int id){
        EditText et;
        switch(id)
        {
            case R.id.call_btn:
                et = findViewById(R.id.phoneNum);
                break;
            case R.id.surf_btn:
                et = findViewById(R.id.webAddress);
                break;
            case R.id.email_btn:
                et = findViewById(R.id.emailAddress);
                break;
            case R.id.register_btn:
                et = findViewById(R.id.register);
                break;
            default:
                return "";
        }
        return et.getText().toString();
    }
    public void onButtonClick(View v)
    {
        String errMsg = "";
        try {
            String in = getEditText(v.getId());
            //validates correct input.
            if (v.getId() != R.id.register_btn && in.equals("")) {
                Toast.makeText(getApplicationContext(), "input is missing", Toast.LENGTH_SHORT).show();
                return;
            }

            // calls upon the right application
            switch(v.getId())
            {
                case R.id.call_btn:
                    if(Patterns.PHONE.matcher(in).matches())
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + in)));
                    else
                        errMsg = "Invalid phone number";
                    break;
                case R.id.surf_btn:
                    if(!in.startsWith("http://") && !in.startsWith("https://"))
                        if(!in.startsWith("www."))
                            in = "http://www." + in;
                        else
                            in = "http://" +in;
                    if(Patterns.WEB_URL.matcher(in).matches())
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(in)));
                    else
                        errMsg = "Invalid URL";
                    break;
                case R.id.email_btn:
                    if(Patterns.EMAIL_ADDRESS.matcher(in).matches())
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + in)));
                    else
                        errMsg = "Invalid email address";
                    break;
                case R.id.register_btn:
                    startActivityForResult(new Intent("com.action.register"), REGISTER);
                    break;
            }
        }
        catch (Exception e) {
            errMsg = e.getMessage();
        }
        finally {
            if (!errMsg.equals(""))
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER && resultCode == Activity.RESULT_OK)
        {
            /* Read Data from intent */
            String firstName = data.getStringExtra(FIRST_NAME);
            String lastName = data.getStringExtra(LAST_NAME);
            String gender = data.getStringExtra(GENDER);

            /* Build string to display */
            String textToDisplay;
            if (gender.equals(getString(R.string.male)))
                textToDisplay = getString(R.string.mr);
            else
                textToDisplay = getString(R.string.mrs);
            textToDisplay += " " + firstName + " " + lastName;

            /* Set Text */
            EditText editText = findViewById(R.id.register);
            editText.setText(textToDisplay);
        }
    }
}
