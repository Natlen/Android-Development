package com.natlen.ex5;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class FragB extends Fragment implements SeekBar.OnSeekBarChangeListener {
    private Handler mListener;
    private double mResult;
    private String mResultText;
    private SeekBar mSeekBar;
    private TextView mIndicator;
    private TextView mResultTextView;

    public FragB() {
        mResultText = "";
        mResult = 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultTextView = view.findViewById(R.id.result);
        mIndicator = view.findViewById(R.id.seekIndicator);
        mSeekBar = view.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        setResultText(mSeekBar.getProgress());
        view.findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResult = 0;
                setProgress(-1);            // reset command
                FragA opFrag;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    opFrag = (FragA) getFragmentManager().findFragmentById(R.id.operand_frag_land);
                }
                else {
                    opFrag = (FragA) getFragmentManager().findFragmentById(R.id.operand_frag_port);
                }
                if (opFrag != null) opFrag.reset();
            }
        });
    }

    /**
     * Calculate and display result text.
     */
    public void calc(String op1Str, String op2Str, int operand) {
        try {
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
            if (mSeekBar != null) {
                setResultText(mSeekBar.getProgress());
            }
        } catch (Exception e) {
            Log.e("Frag B:", e.getMessage());
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Set result text corresponding to progress bar.
     */
    private void setResultText(int progress) {
        if (progress == 0)  {
            mResultTextView.setText(mResultText + String.format("%d", (int)mResult));
        }
        else  {
            mResultTextView.setText(mResultText + String.format("%."+progress+"f", mResult));
        }
    }

    public int getProgress() {
        if (mSeekBar == null) return 0;
        return mSeekBar.getProgress();
    }

    public void setProgress(int progress) {
        if (progress == -1) {           // Resetting command.
            mSeekBar.setProgress(0);
            mResultTextView.setText("");
            return;
        }
        if (mSeekBar == null || progress < 0 || progress > 5)
            return;
        mSeekBar.setProgress(progress);
    }


    /* OnSeekBarChangeListener implementation */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mIndicator.setText(""+progress);
        if (mResultTextView.getText().toString().equals("")) {
            return;
        }
        setResultText(progress);
        mListener.updateProgress(progress);
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // don't care
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // don't care
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        void updateProgress(int prog);
    }


}
