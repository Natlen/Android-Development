package com.rkcodesolution.lab06;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


public class ExitDialog extends DialogFragment {
    private ExitListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.exit_dialog_title)
                .setMessage(R.string.exit_dialog_text)
                .setPositiveButton(R.string.dialog_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onExitPressed();
                                dismiss();
                            }
                        }
                )
                .setNegativeButton(R.string.dialog_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OperandFragment.OperandHandler) {
            mListener = (ExitListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ExitListener");
        }
    }

    public interface ExitListener {
        void onExitPressed();
    }
}
