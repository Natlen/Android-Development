package com.natlen.project2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ExitDialog extends DialogFragment {
    private Handler listener; // initialized at: onAttach.

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()) // generate a new instance of alert dialog
                .setTitle(R.string.exit) // title text
                .setMessage(R.string.confirm) // body text
                //creating the positive Button
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    //assigning the listener.
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onExitChoose(); // assigning the class's listener.
                        dismiss(); // acknowledge - done.
                    }
                }
                )
                //creating the negative Button
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    //assigning the listener.
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();// acknowledge - done.
                    }
                })
                .create(); // acknowledge - done initializing - create the dialog.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            listener = (Handler) context; // initializing the listener.
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ExitDialog : Handler interface");
        }
    }

    public interface Handler {
        void onExitChoose(); // implemented on hosting activity - MainActivity.
    }
}
