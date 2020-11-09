package com.rkcodesolution.lab06;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class SettingsDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    public static String PROG = "progress";
    private SettingsListener mListener;
    private SeekBar mSeekBar;
    private TextView mIndicator;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SettingsDialog);
        View seekBarView = getActivity().getLayoutInflater().inflate(R.layout.seek_bar, null);
        mSeekBar = seekBarView.findViewById(R.id.seekBar);
        mIndicator = seekBarView.findViewById(R.id.seekIndicator);
        mSeekBar.setOnSeekBarChangeListener(this);

        /* Get precision and update seekBar */
        Bundle args = getArguments();
        if (args != null) {
            int prog = args.getInt(PROG);
            if (0 <= prog && prog <= 5)
                mSeekBar.setProgress(prog);
        }

        // Inflate and set the layout for the dialog
        builder.setView(seekBarView)
                .setTitle(R.string.precision)
                .setIcon(R.drawable.icon)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListener.onSeekBarChanged(mSeekBar.getProgress());
                                dismiss();
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
        return builder.create();
    }

    /* OnSeekBarChangeListener implementation */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mIndicator.setText(""+progress);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OperandFragment.OperandHandler) {
            mListener = (SettingsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SettingsListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * To be called with on OK button click.
     */
    public interface SettingsListener {
        void onSeekBarChanged(int progress);
    }

}
