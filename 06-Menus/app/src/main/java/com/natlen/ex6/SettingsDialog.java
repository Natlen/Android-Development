package com.natlen.ex6;

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

    public static String PROGRESS = "progress";
    private Handler listener;
    private SeekBar mSeekBar;
    private TextView mIndicator;
    private TextView mResultTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // generate a new instance of alert dialog
        View seekBarView = getActivity().getLayoutInflater().inflate(R.layout.seek_bar, null); // inflate the seek bar layout.

        //initializing
        mSeekBar = seekBarView.findViewById(R.id.seekBar);
        mIndicator = seekBarView.findViewById(R.id.seekIndicator);
        mSeekBar.setOnSeekBarChangeListener(this); // allow assigning: this - becasue this classs implements the method.

        //activity communication - init and update
        Bundle args = getArguments();
        if (args != null) {
            int prog = args.getInt(PROGRESS);
            if (0 <= prog && prog <= 5)
                mSeekBar.setProgress(prog);
        }

        builder.setView(seekBarView) // view appearance
                .setTitle("Seek Bar Precision") // title text
                .setIcon(R.drawable.blue_gear) // icon asset
                .setPositiveButton("OK", // button text
                        new DialogInterface.OnClickListener() {
                            @Override
                            //assigning the listener.
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onSeekBarProgressChanged(mSeekBar.getProgress()); // assigning the class's listener.
                                dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            //assigning the listener.
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                );
        return builder.create();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mIndicator.setText(""+progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragA.Handler) {
            listener = (Handler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "  must implement SettingsDialog : Handler interface");
        }
    }

    public interface Handler{
        void onSeekBarProgressChanged(int progress); // implemented on hosting activity - MainActivity.
    }
}
