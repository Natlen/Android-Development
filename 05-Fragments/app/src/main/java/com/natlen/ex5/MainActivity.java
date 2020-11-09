package com.natlen.ex5;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity implements FragA.Handler, FragB.Handler {
    public static String OPERAND_ID = "operId";
    public static String PARAM1 = "param1";
    public static String PARAM2 = "param2";
    public static String PROG = "progress";
    public static String FRAG_TAG = "resTag";

    private EditText mParam1;
    private EditText mParam2;
    private int mOperandId;
    private int mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mParam1 = findViewById(R.id.op1Input);
        mParam2 = findViewById(R.id.op2Input);
        mOperandId = -1;
        mProgress = 0;
    }

    @Override
    public void onOperandClick(String param1, String param2, int operandId) {
        mOperandId = operandId;
        /* Get ResultFragment depending on phone orientation */
        FragB resFrag;
        FragmentManager fm = getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resFrag = (FragB) fm.findFragmentById(R.id.result_fragment); // already exists
        } else {      // Portrait. We have to create the result fragment.
            resFrag = new FragB();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, resFrag, FRAG_TAG);     // save dynamically added fragment with tag.
            ft.addToBackStack(null);
            ft.hide(fm.findFragmentById(R.id.operand_frag_port));   // hide operand fragment.
            ft.commit();
        }
        resFrag.calc(param1, param2, operandId);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PARAM1, mParam1.getText().toString());
        outState.putString(PARAM2, mParam2.getText().toString());
        outState.putInt(OPERAND_ID, mOperandId);
        outState.putInt(PROG, mProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            String param1 = savedInstanceState.getString(PARAM1);
            String param2 = savedInstanceState.getString(PARAM2);
            mParam1.setText(param1);
            mParam2.setText(param2);
            mOperandId = savedInstanceState.getInt(OPERAND_ID);
            mProgress = savedInstanceState.getInt(PROG);

            /* try restore result text */
            FragB resFrag;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                resFrag = (FragB) getSupportFragmentManager().findFragmentById(R.id.result_fragment);
            }
            else {   // try to restore dynamically added fragment
                resFrag = (FragB) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
            }
            resFrag.calc(param1, param2, mOperandId);
            resFrag.setProgress(mProgress);
            }
            catch (NullPointerException e) {
            // frag b has not created yet.
            }
    }
    @Override
    public void updateProgress(int prog) {
        mProgress = prog;
    }
}
