package com.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;


public class SetPasswordDialog extends DialogFragment {

    private SetOnClickListener ClickListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SetPasswordDialog.SetOnClickListener){
            ClickListener = (SetOnClickListener) context;
        }
        else
            throw new RuntimeException("Please implement the done button listener");
    }

    public SetPasswordDialog() {
    }

    public static SetPasswordDialog newInstance() {
        SetPasswordDialog fragment = new SetPasswordDialog();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_set_password_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatButton saveButton = view.findViewById(R.id.saveButton);
        AppCompatButton backButton = view.findViewById(R.id.backButton);
        EditText password_editText = view.findViewById(R.id.editTextPassword);

        saveButton.setOnClickListener(view12 -> {

            ClickListener.onClickSave(password_editText.getText().toString());
            dismiss();
        });
        backButton.setOnClickListener(view1 -> {
            MainActivity.passwordSwitcher.setChecked(false);
            dismiss();
        });
    }
    public interface SetOnClickListener{
        void onClickSave(String text);
    }
}