package com.example.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.room.PrimaryKey;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterPasswordDialog extends DialogFragment {

    private  static final String PASSWORD ="password";
    private static String saved_password;
    public static EnterPasswordDialog newInstance(String password) {
        EnterPasswordDialog fragment = new EnterPasswordDialog();
        Bundle args = new Bundle();
        saved_password = password;
        args.putString(PASSWORD,password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_password_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatButton confirmButton = view.findViewById(R.id.confirmButton);
        EditText password_editText = view.findViewById(R.id.editTextConfirmPassword);
        TextView warning_msg = view.findViewById(R.id.warningTextView);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_password = password_editText.getText().toString();
                Log.d("getPass", getArguments().getString(PASSWORD));
                if(entered_password.equals(getArguments().getString(PASSWORD))){
                    dismiss();
                }
                else{
                    warning_msg.setText("That is not right");
                }
            }
        });
    }
}