package com.rkcodesolution.ex3client;

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
    public static final int REGISTER = 1;
    public static final String FIRST_NAME = "fname";
    public static final String LAST_NAME = "lname";
    public static final String GENDER = "gender";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View v) {
        String errorMessage = "";
        try {
            // Get relevant text: phone / email / web site..
            String text = getTextByButtonId(v.getId());
            if (v.getId() != R.id.register && text.equals("")) {
                Toast.makeText(getApplicationContext(), "Input is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()) {
                case R.id.call:
                    /* Validate phone number */
                    if (Patterns.PHONE.matcher(text).matches()) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + text)));
                    } else {
                        errorMessage = "Invalid phone number!";
                    }
                    break;

                case R.id.surf:
                    // Bypass ActivityNotFoundException if http protocol is missing */
                    if (!text.startsWith("https://") && !text.startsWith("http://")) {
                        text = "http://" + text;
                    }
                    /* Validate web address */
                    if (Patterns.WEB_URL.matcher(text).matches()) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(text)));
                    } else {
                        errorMessage = "Invalid web address!";
                    }
                    break;

                case R.id.email:
                    /* Validate Email Address */
                    if (Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + text)));
                    } else {
                        errorMessage = "Invalid Email address!";
                    }
                    break;

                case R.id.register:
                    Intent intent = new Intent("com.action.register");
                    startActivityForResult(intent, REGISTER);
                    break;
            }
        }
        catch (Exception e)
        {
            errorMessage = e.getMessage();      // might not find activity to handle events.
        }
        finally {
            if (!errorMessage.equals("")) {
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Return TextView Text corresponding to the given Button ID;
     * @param id
     * @return Empty string if invalid.
     */
    private String getTextByButtonId(int id){
        EditText editText;
        switch (id){
            case R.id.call:
                editText = findViewById(R.id.call_text);
                return editText.getText().toString();
            case R.id.surf:
                editText = findViewById(R.id.surf_text);
                return editText.getText().toString();
            case R.id.email:
                editText = findViewById( R.id.email_text);
                return editText.getText().toString();
            case R.id.register:
                editText = findViewById( R.id.register_text);
                return editText.getText().toString();
                default:
                    return "";
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
            if (gender.equals("Male"))
                textToDisplay = getString(R.string.mister);
            else textToDisplay = getString(R.string.misiz);
            textToDisplay += " " + firstName + " " + lastName;

            /* Set Text */
            EditText editText = findViewById(R.id.register_text);
            editText.setText(textToDisplay);
        }
    }
}
