package com.rkcodesolution.lab06;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import static com.rkcodesolution.lab06.SettingsDialog.PROG;


public class MainActivity extends AppCompatActivity implements OperandFragment.OperandHandler, ResultFragment.ResultHandler,
        ExitDialog.ExitListener, SettingsDialog.SettingsListener {

    public static final String MYTAG = "MYTAG";
    public static String OPERAND_ID = "operId";
    public static String PARAM1 = "param1";
    public static String PARAM2 = "param2";
    public static String FRAG_TAG = "resTag";

    private EditText mParam1;
    private EditText mParam2;
    private int mOperandId;
    private int mProgress;

    /* Menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Menu selection
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                new ExitDialog().show(getSupportFragmentManager(), "exitDialog");
                return true;
            case R.id.settings:
                SettingsDialog settingsDialog = new SettingsDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(PROG, mProgress);
                settingsDialog.setArguments(bundle);
                settingsDialog.show(getSupportFragmentManager(), "settingsDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* Menu - ExitListener */
    @Override
    public void onExitPressed() {
        finish();   // exit app
    }

    /* Menu - SettingListener */
    @Override
    public void onSeekBarChanged(int progress) {
        mProgress = progress;
        setResultText(mParam1.getText().toString(), mParam2.getText().toString(), mOperandId, mProgress);
    }

    /**
     * Try to find result fragment and update it.
     * @param oper1
     * @param oper2
     * @param op
     * @param prog
     */
    private void setResultText(String oper1, String oper2, int op, int prog) {
        ResultFragment resFrag;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resFrag = (ResultFragment) getSupportFragmentManager().findFragmentById(R.id.result_fragment);
        }
        else {      // try to restore dynamically added fragment
            resFrag = (ResultFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG);
        }
        if (resFrag != null) {
            resFrag.calculate(oper1, oper2, op, prog);
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

    /**
     * Calculate and display result.
     * @param param1 first parameter as double
     * @param param2 second parameter as double
     * @param operandId operand clicked
     */
    @Override
    public void onOperandClick(String param1, String param2, int operandId) {
        mOperandId = operandId;
        /* Get ResultFragment depending on phone orientation */
        ResultFragment resFrag;
        FragmentManager fm = getSupportFragmentManager();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            resFrag = (ResultFragment) fm.findFragmentById(R.id.result_fragment); // already exists
            resFrag.calculate(param1, param2, operandId, mProgress);
        } else {      // Portrait. We have to create the result fragment. and save arguments.
            resFrag = new ResultFragment();
            Bundle args = new Bundle();
            args.putString(PARAM1, param1);
            args.putString(PARAM2, param2);
            args.putInt(OPERAND_ID, operandId);
            args.putInt(PROG, mProgress);
            resFrag.setArguments(args);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, resFrag, FRAG_TAG);     // save dynamically added fragment with tag.
            ft.addToBackStack(null);
            ft.hide(fm.findFragmentById(R.id.operand_frag_port));   // hide operand fragment.
            ft.commit();
        }
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
        String param1 = savedInstanceState.getString(PARAM1);
        String param2 = savedInstanceState.getString(PARAM2);
        mParam1.setText(param1);
        mParam2.setText(param2);
        mOperandId = savedInstanceState.getInt(OPERAND_ID);
        mProgress = savedInstanceState.getInt(PROG);

        setResultText(param1, param2, mOperandId, mProgress);
    }


}
