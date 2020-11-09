package com.rkcodesolution.lab06;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.rkcodesolution.lab06.MainActivity.OPERAND_ID;
import static com.rkcodesolution.lab06.MainActivity.PARAM1;
import static com.rkcodesolution.lab06.MainActivity.PARAM2;
import static com.rkcodesolution.lab06.SettingsDialog.PROG;


public class ResultFragment extends Fragment implements SettingsDialog.SettingsListener{
    private ResultHandler mListener;
    private double mResult;
    private String mResultText;
    private TextView mResultTextView;

    public ResultFragment() {
        mResultText = "";
        mResult = 0;
    }

    @Override
    public void onSeekBarChanged(int progress) {
        setResultText(progress, mResultText);
    }

    private void setResultText(int progress, String initialText){
        if (mResultTextView == null){
            return;
        }

        if (progress == 0)  {
            mResultTextView.setText(initialText + String.format("%d", (int)mResult));
        }
        else  {
            mResultTextView.setText(initialText + String.format("%."+progress+"f", mResult));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultTextView = view.findViewById(R.id.result);

        view.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = 0;
                mResultTextView.setText("");
                OperandFragment opFrag;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    opFrag = (OperandFragment) getFragmentManager().findFragmentById(R.id.operand_frag_land);
                } else {
                    opFrag = (OperandFragment) getFragmentManager().findFragmentById(R.id.operand_frag_port);
                }
                if (opFrag != null) opFrag.reset();
            }
        });
        Bundle args = getArguments();
        if (args != null) {
            calculate(args.getString(PARAM1), args.getString(PARAM2), args.getInt(OPERAND_ID), args.getInt(PROG));
        }
    }

    /**
     * Calculate and display result text.
     */
    public void calculate(String op1Str, String op2Str, int operand, int progress) {
        if (op1Str.isEmpty() || op2Str.isEmpty()) {
            return;     // do nothing
        }
        double param1 = Double.parseDouble(op1Str);
        double param2 = Double.parseDouble(op2Str);
        String op;
        switch (operand) {
            case R.id.minus:
                mResult = param1 - param2;
                op = " - ";
                break;
            case R.id.plus:
                mResult = param1 + param2;
                op = " + ";
                break;
            case R.id.multiply:
                mResult = param1 * param2;
                op = " * ";
                break;
            case R.id.divide:
                if (param2 == 0) {
                    Toast.makeText(getContext(), "Can't divide by 0!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mResult = param1 / param2;
                op = " / ";
                break;
            default:
                return;     // Not operand button. do nothing.
        }
        mResultText = String.valueOf(param1) + op + String.valueOf(param2) + " = ";
        setResultText(progress, mResultText);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResultHandler) {
            mListener = (ResultHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ResultFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    public interface ResultHandler {
    }


}
