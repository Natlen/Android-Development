package com.natlen.ex2;


import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView resultTextView;

    public void onOperatorClick(View v)
    {
        try{
            // finding through the R class and items IDs the two operands and converting them both to strings.
            String op1_str = ((TextView) findViewById(R.id.op1)).getText().toString();
            String op2_str = ((TextView) findViewById(R.id.op2)).getText().toString();
            // as if either the first, second or both of the operands are empty.
            // toast - represent a faded error msg on screen
            if (op1_str.equals("") || op2_str.equals(""))
            {
                Toast.makeText(getApplicationContext(),"Operand is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            // parsing both operands into double type.
            double op1 = Double.parseDouble(op1_str);
            double op2 = Double.parseDouble(op2_str);
            double result;
            //choosing operand to preform.
            switch (v.getId())
            {
                case R.id.btn_plus:
                    result = op1 + op2;
                    break;
                case R.id.btn_minus:
                    result = op1 - op2;
                    break;
                case R.id.btn_multiply:
                    result = op1 * op2;
                    break;
                case R.id.btn_divide:
                    if (op2 == 0)
                    {
                        Toast.makeText(getApplicationContext(), "Cannot divide by 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    result = op1 / op2;
                    break;
                default:
                    return;
            }
            resultTextView.setText(String.format("%.2f", result));
        }
        catch (Exception e)
        {
            Log.e(APP.AppName, e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(APP.AppName, "MainActivity: onCreate()");
        resultTextView = findViewById(R.id.result);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(APP.AppName, "MainActivity: onSaveInstanceState(Bundle)");

        /* Save result TextView's text */
        outState.putString("result", resultTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(APP.AppName, "MainActivity: onRestoreInstanceState(Bundle)");

        /* Restore result TextView's text */
        if (savedInstanceState != null) {
            resultTextView.setText(savedInstanceState.getString("result"));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(APP.AppName, "MainActivity: onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(APP.AppName, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(APP.AppName, "MainActivity: onDestroy()");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.i(APP.AppName, "MainActivity: onRestoreInstanceState(Bundle, PersistableBundle)");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(APP.AppName, "MainActivity: onSaveInstanceState(Bundle, PersistableBundle)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(APP.AppName, "MainActivity: onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(APP.AppName, "MainActivity: onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(APP.AppName, "MainActivity: onRestart()");
    }
}
