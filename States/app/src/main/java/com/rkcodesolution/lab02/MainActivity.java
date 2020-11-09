package com.rkcodesolution.lab02;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private TextView mResultTextView;

    public void onOperandClick(View v)
    {
        try {
            String op1Str = ((TextView) findViewById(R.id.op1Input)).getText().toString();
            String op2Str = ((TextView) findViewById(R.id.op2Input)).getText().toString();
            if (op1Str.equals("") || op2Str.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Operand input is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            double op1 = Double.parseDouble(op1Str);
            double op2 = Double.parseDouble(op2Str);
            double res;
            switch (v.getId())
            {
                case R.id.minus:
                    res = op1 - op2;
                    break;
                case R.id.plus:
                    res = op1 + op2;
                    break;
                case R.id.multiply:
                    res = op1 * op2;
                    break;
                case R.id.divide:
                    if (op2 == 0){
                        Toast.makeText(getApplicationContext(), "Can't divide by 0!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    res = op1 / op2;
                    break;
                default:
                    return;     // Not operand button. do nothing.
            }
            mResultTextView.setText(String.format("%.2f", res));
        }
        catch (Exception e) {
            Log.e(MyApplication.MYAPP, e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(MyApplication.MYAPP, "MainActivity: onCreate()");
        mResultTextView = findViewById(R.id.result);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(MyApplication.MYAPP, "MainActivity: onSaveInstanceState(Bundle)");

        /* Save result TextView's text */
        outState.putString("result", mResultTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(MyApplication.MYAPP, "MainActivity: onRestoreInstanceState(Bundle)");

        /* Restore result TextView's text */
        if (savedInstanceState != null) {
            mResultTextView.setText(savedInstanceState.getString("result"));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(MyApplication.MYAPP, "MainActivity: onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(MyApplication.MYAPP, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(MyApplication.MYAPP, "MainActivity: onDestroy()");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.i(MyApplication.MYAPP, "MainActivity: onRestoreInstanceState(Bundle, PersistableBundle)");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(MyApplication.MYAPP, "MainActivity: onSaveInstanceState(Bundle, PersistableBundle)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(MyApplication.MYAPP, "MainActivity: onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MyApplication.MYAPP, "MainActivity: onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(MyApplication.MYAPP, "MainActivity: onRestart()");
    }
}
