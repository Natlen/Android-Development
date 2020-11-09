package com.natlen.ex6;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity implements FragA.Handler, FragB.Handler, ExitDialog.Handler, SettingsDialog.Handler {

    public static String OPERAND_ID = "operId";
    public static String PARAM1 = "param1";
    public static String PARAM2 = "param2";
    public static String PROG = "progress";
    public static String FRAG_TAG = "resTag";

    private EditText mParam1;
    private EditText mParam2;
    private int mOperandId;
    private int mProgress;

    //inflate the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // handler for entries section on the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.exit:
                new ExitDialog().show(getSupportFragmentManager(), "Exit Dialog");
                return true;
            case R.id.settings:
                SettingsDialog settingsDialog = new SettingsDialog();
                Bundle args = new Bundle();
                args.putInt(PROG, mProgress);
                settingsDialog.setArguments(args);
                settingsDialog.show(getSupportFragmentManager(), "Settings Dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // implementing the ExitDialog listener
    @Override
    public void onExitChoose() {
        finish(); // calls upon a clean exit of the app.
    }


    // implementing the SettingsDialog listener
    @Override
    public void onSeekBarProgressChanged(int progress) {
        mProgress = progress;
        setResultText(mParam1.getText().toString(), mParam2.getText().toString(), mOperandId, mProgress);
    }

    @Override
    public void onOperandClick(String param1, String param2, int operandId) {
        mOperandId = operandId;
        /* Get ResultFragment depending on phone orientation */
        FragB resFrag;
        FragmentManager fm = getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resFrag = (FragB) fm.findFragmentById(R.id.result_fragment); // already exists
            resFrag.calc(param1, param2, operandId, mProgress);
        } else {      // Portrait.
            resFrag = new FragB();
            //save arguments.
            Bundle args = new Bundle();
            args.putString(PARAM1, param1);
            args.putString(PARAM2, param2);
            args.putInt(OPERAND_ID, operandId);
            args.putInt(PROG, mProgress);
            //manually creating the result fragment.
            resFrag.setArguments(args);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, resFrag, FRAG_TAG);     // save dynamically added fragment with tag.
            ft.addToBackStack(null);
            ft.hide(fm.findFragmentById(R.id.operand_frag_port));   // hide operand fragment.
            ft.commit();
            fm.executePendingTransactions();
        }
    }
    private void setResultText(String oper1, String oper2, int op, int prog) {
        FragB resFrag;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resFrag = (FragB) getSupportFragmentManager().findFragmentById(R.id.result_fragment);
        }
        else {      // try to restore dynamically added fragment
            resFrag = (FragB) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }
        if (resFrag != null) {
            resFrag.calc(oper1, oper2, op, prog);
        }
    }

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
        super.onRestoreInstanceState(savedInstanceState);
        String param1 = savedInstanceState.getString(PARAM1);
        String param2 = savedInstanceState.getString(PARAM2);
        mParam1.setText(param1);
        mParam2.setText(param2);
        mOperandId = savedInstanceState.getInt(OPERAND_ID);
        mProgress = savedInstanceState.getInt(PROG);

        setResultText(param1, param2, mOperandId, mProgress);
    }
}
