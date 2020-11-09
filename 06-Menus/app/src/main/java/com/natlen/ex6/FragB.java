package com.natlen.ex6;

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

public class FragB extends Fragment implements SettingsDialog.Handler {

    private Handler mListener;
    private double mResult;
    private String mResultText;
    private TextView mResultTextView;

    public FragB() {
        mResultText = "";
        mResult = 0;
    }

    @Override
    public void onSeekBarProgressChanged(int progress) {
        setResultText(progress, mResultText);
    }

    private void setResultText(int progress, String initialText) {
        if (mResultTextView == null) {
            return;
        }

        if (progress == 0) {
            mResultTextView.setText(initialText + String.format("%d", (int) mResult));
        } else {
            mResultTextView.setText(initialText + String.format("%." + progress + "f", mResult));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultTextView = view.findViewById(R.id.result);
        view.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            //assigning listener to the reset button.
            @Override
            public void onClick(View v) {
                mResult = 0;
                mResultTextView.setText("");
                FragA fragA;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fragA = (FragA) getFragmentManager().findFragmentById(R.id.operand_frag_land);
                } else {
                    fragA = (FragA) getFragmentManager().findFragmentById(R.id.operand_frag_port);
                }
                if (fragA != null)
                    fragA.reset();
            }
        });
        Bundle args = getArguments();
        if (args != null) {
            calc(args.getString(MainActivity.PARAM1), args.getString(MainActivity.PARAM2), args.getInt(MainActivity.OPERAND_ID), args.getInt(MainActivity.PROG));
        }
    }

    // Calculate and display result text.
    public void calc(String op1Str, String op2Str, int operand, int progress) {
        if (op1Str.isEmpty() || op2Str.isEmpty())
            return;     // do nothing
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
                    Toast.makeText(getContext(), "Cannot divide by 0.", Toast.LENGTH_SHORT).show();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_b, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Handler) {
            mListener = (Handler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragB");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Handler {

    }
}
