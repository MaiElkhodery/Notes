package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean nightMode;
    private static final String MODE ="night_mode";


    private static SetSettingsListener listener;

    public interface SetSettingsListener{
        void onClickChangeView();
    }

    public static SettingsFragment newInstance(SetSettingsListener settingsListener) {
        listener = (SetSettingsListener) settingsListener;
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("mode", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MODE,false);
        onClickLinearView(view);
        onClickNightMode(view);
    }

    public void onClickNightMode(View view){
        SwitchCompat switchButton = view.findViewById(R.id.switchMode);
        if(nightMode){
            switchButton.setChecked(true);
        }
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,true);
                }
                editor.apply();
            }
        });
    }
    public void onClickLinearView(View view){
        SwitchCompat switchButton = view.findViewById(R.id.switchMode2);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickChangeView();
            }
        });
    }
}