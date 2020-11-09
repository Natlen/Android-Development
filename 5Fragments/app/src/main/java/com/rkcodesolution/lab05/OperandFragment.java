package com.rkcodesolution.lab05;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


public class OperandFragment extends Fragment implements TextWatcher {
    private OperandHandler mListener;
    private Map<Integer, Button> mOperands;
    private EditText mFirstText;
    private EditText mSecondText;

    public OperandFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Get operand buttons, save them and disable until has input text */
        mOperands = new HashMap<>();
        mOperands.put(R.id.plus, (Button) view.findViewById(R.id.plus));
        mOperands.put(R.id.minus, (Button) view.findViewById(R.id.minus));
        mOperands.put(R.id.divide, (Button) view.findViewById(R.id.divide));
        mOperands.put(R.id.multiply, (Button) view.findViewById(R.id.multiply));
        for (Button b : mOperands.values()) {
            b.setEnabled(false);        // to grey out.
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onOperandClick(mFirstText.getText().toString(), mSecondText.getText().toString(), v.getId());
                    }
                }
            });
        }

        mFirstText = view.findViewById(R.id.op1Input);
        mSecondText = view.findViewById(R.id.op2Input);
        // Attach text listener to TextViews
        mFirstText.addTextChangedListener(this);
        mSecondText.addTextChangedListener(this);
    }

    /* TextWatcher implementation. */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // We want to check for each input / delete on Real Time.
        String firstNumberText = mFirstText.getText().toString();
        String secondNumberText = mSecondText.getText().toString();

        if (firstNumberText.equals("") || secondNumberText.equals("")) {
            for (Button b : mOperands.values()) {            // disable buttons
                b.setEnabled(false);        // for greyed out style
            }
            return;
        }
        for (Button b : mOperands.values()) {        // Enable buttons
            b.setEnabled(true);
        }
        try {
            double op2 = Double.parseDouble(secondNumberText);
            if (op2 == 0) {     // disable divide button
                mOperands.get(R.id.divide).setEnabled(false);
            }
        } catch (Exception e) {
            // Invalid format. do nothing.
        }

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // don't care
    }
    @Override
    public void afterTextChanged(Editable s) {
        // don't care
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_operand, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OperandHandler) {
            mListener = (OperandHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OperandHandler");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OperandHandler {
        void onOperandClick(String param1, String param2, int operandId);
    }

    public void reset() {
        mFirstText.setText("");
        mSecondText.setText("");
    }

}
