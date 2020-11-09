package com.natlen.ex4;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private static final String TAG = "ex4";
    private EditText et_op1, et_op2;
    private ArrayList<Button> btn_ops;
    private TextView tv_result;
    private SeekBar seekbar;
    private TextView tv_sbIndicator;
    private double result;

    public void onOperatorClick(View v)
    {
        try{
            // finding through the R class and items IDs the two operands and converting them both to strings.
            String op1_str = ((TextView) findViewById(R.id.op1)).getText().toString();
            String op2_str = ((TextView) findViewById(R.id.op2)).getText().toString();
            // parsing both operands into double type.
            double op1 = Double.parseDouble(op1_str);
            double op2 = Double.parseDouble(op2_str);
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
                    result = op1 / op2;
                    break;
                default:
                    return;
            }
            Log.i(TAG, "result =   " + result);
            setResultText(seekbar.getProgress());
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setResultText(int progress)
    {
        if (progress == 0)  {
            tv_result.setText(String.format("%d", (int)result));
        }
        else  {
            tv_result.setText(String.format("%."+progress+"f", result));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_sbIndicator.setText("" + progress);
        if(tv_result.getText().toString().equals(""))
            return;
        setResultText(seekbar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class TextWatcherHandler implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_result.setText("");

            //checks and sets operators
            if(et_op1.getText().toString().equals("") || et_op2.getText().toString().equals(""))
            {
                for(Button btn : btn_ops)
                    btn.setEnabled(false);
                return;
            }
            for(Button btn : btn_ops)
                btn.setEnabled(true);
            if(Double.parseDouble(et_op2.getText().toString()) == 0)
                findViewById(R.id.btn_divide).setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize edit text operands
        et_op1 = findViewById(R.id.op1);
        et_op2 = findViewById(R.id.op2);

        // initialize buttons operators
        btn_ops = new ArrayList<Button>();
        btn_ops.add((Button) findViewById(R.id.btn_plus));
        btn_ops.add((Button) findViewById(R.id.btn_minus));
        btn_ops.add((Button) findViewById(R.id.btn_multiply));
        btn_ops.add((Button) findViewById(R.id.btn_divide));
        for(Button btn: btn_ops)
            btn.setEnabled(false);

        //initialize view text result
        tv_result = findViewById(R.id.result);

        //attaches text listeners to the text views
        TextWatcherHandler textWatcherHandler = new TextWatcherHandler();
        et_op1.addTextChangedListener(textWatcherHandler);
        et_op2.addTextChangedListener(textWatcherHandler);

        //attaches an onClick listener to a Button
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                et_op1.setText("");
                et_op2.setText("");
                tv_result.setText("");
            }
        });


        //  Dynamically add SeekBar Layout.
        ViewGroup parentLayout = findViewById(R.id.main_layout);
        View child = getLayoutInflater().inflate(R.layout.seek_bar, parentLayout, false);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            child.setPadding(0,64,0,0);
        parentLayout.addView(child);
        //

        // Common logic for dynamically and statically adding SeekBar.
        tv_sbIndicator = findViewById(R.id.tv_sbIndicator);
        seekbar = findViewById(R.id.seekbar1);
        seekbar.setMax(5);
        seekbar.setOnSeekBarChangeListener(this);
    }
    public int dp2px(int dp){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saves result
        outState.putDouble("resultDOUBLE", result);
        outState.putString("resultSTRING", tv_result.getText().toString());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "MainActivity: onRestoreInstanceState(Bundle)");

        /* Restore result TextView's text */
        if (savedInstanceState != null) {
            result = savedInstanceState.getDouble("resultDOUBLE");
            tv_result.setText(savedInstanceState.getString("resultSTRING"));
        }
    }
}
