package com.rkcodesolution.lab04;

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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    public static final String MYTAG = "MYTAG";
    private TextView mResultTextView;
    private EditText mFirstText;
    private EditText mSecondText;
    private ArrayList<Button> mOperands;
    private SeekBar mSeekBar;
    private double mResult;
    private TextView mIndicator;

    /**
     * Setting result text
     * @param progress
     */
    private void setResultText(int progress)
    {
        if (progress == 0)  {
            mResultTextView.setText(String.format("%d", (int)mResult));
        }
        else  {
            mResultTextView.setText(String.format("%."+progress+"f", mResult));
        }
    }

    /**
     * /Setting text below SeekBar
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mIndicator.setText(""+progress);
        if (mResultTextView.getText().toString().equals("")) {
            return;
        }
        setResultText(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

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
            switch (v.getId())
            {
                case R.id.minus:
                    mResult = op1 - op2;
                    break;
                case R.id.plus:
                    mResult = op1 + op2;
                    break;
                case R.id.multiply:
                    mResult = op1 * op2;
                    break;
                case R.id.divide:
                    if (op2 == 0){
                        Toast.makeText(getApplicationContext(), "Can't divide by 0!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mResult = op1 / op2;
                    break;
                default:
                    return;     // Not operand button. do nothing.
            }
            Log.i(MYTAG, "result = "+mResult);
            setResultText(mSeekBar.getProgress());
        }
        catch (Exception e) {
            Log.e(MYTAG, e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private class TextChangeHandler implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mResultTextView.setText("");        // reset text;

            // We want to check for each input / delete on Real Time.
            String firstNumberText = mFirstText.getText().toString();
            String secondNumberText = mSecondText.getText().toString();

            if (firstNumberText.equals("") || secondNumberText.equals("")) {
                for (Button b : mOperands) {            // disable buttons
                    b.setEnabled(false);        // for greyed out style
                }
                return;
            }
            for (Button b : mOperands) {        // Enable buttons
                b.setEnabled(true);
            }

            double op2 = Double.parseDouble(secondNumberText);
            if (op2 == 0) {     // disable divide button
                findViewById(R.id.divide).setEnabled(false);
            }

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // don't care
        }
        @Override
        public void afterTextChanged(Editable s) {
            // don't care
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOperands = new ArrayList<>();
        mOperands.add( (Button) findViewById(R.id.plus));
        mOperands.add( (Button) findViewById(R.id.minus));
        mOperands.add( (Button) findViewById(R.id.divide));
        mOperands.add( (Button) findViewById(R.id.multiply));
        for (Button b : mOperands) {
            b.setEnabled(false);        // for greyed out style
        }
        mFirstText = findViewById(R.id.op1Input);
        mSecondText = findViewById(R.id.op2Input);
        mResultTextView = findViewById(R.id.result);

        // Attach text listener to TextViews
        TextChangeHandler textChangeHandler = new TextChangeHandler();
        mFirstText.addTextChangedListener(textChangeHandler);
        mSecondText.addTextChangedListener(textChangeHandler);

        // Attach on click listener to RESET button
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                mFirstText.setText("");
                mSecondText.setText("");
                mResultTextView.setText("");
            }
        });

        //  Dynamically add SeekBar Layout.
        ViewGroup parentLayout = findViewById(R.id.seekbarContainer);
        View child = getLayoutInflater().inflate(R.layout.seek_bar, parentLayout, false);
        int layoutWidthHeight;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutWidthHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            layoutWidthHeight = ViewGroup.LayoutParams.MATCH_PARENT;    // portrait
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(layoutWidthHeight,layoutWidthHeight,10);
        int px = dp2px(10);
        params.setMargins(px, px, px, px);
        parentLayout.addView(child, params);
        //

        // Common logic for dynamically and statically adding SeekBar.
        mIndicator = findViewById(R.id.seekIndicator);
        mSeekBar = findViewById(R.id.seekBar);
        mSeekBar.setMax(5);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public int dp2px(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /* Save result */
        outState.putDouble("result", mResult);
        outState.putString("resultString", mResultTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /* Restore result  */
        if (savedInstanceState != null) {
            double res = savedInstanceState.getDouble("result");
            String reString = savedInstanceState.getString("resultString");

            // A good check to determine if reset button was pressed.
            if (!reString.equals("") && Double.parseDouble(reString) == res)
            {
                mResult = res;
                setResultText(mSeekBar.getProgress());
            }
        }
    }


}
