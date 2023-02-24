package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean nightMode;
    private boolean gridView;
    private static final String MODE ="night_mode";
    private static final String VIEW ="grid_view";
    SwitchCompat switcher1;
    SwitchCompat switcher2;
    private static SetSettingsListener listener;

    public interface SetSettingsListener{
        void changeLayoutToLinear();
        void changeLayoutToGrid();
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

        onClickNightMode(view);
        onClickLinearView(view);
    }

    public void onClickNightMode(View view){
        sharedPreferences = getActivity().getSharedPreferences("mode", Context.MODE_PRIVATE);
        switcher1 = view.findViewById(R.id.switchMode);
        nightMode = sharedPreferences.getBoolean(MODE,false);
        if(nightMode){
            switcher1.setChecked(true);
        }
        switcher1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,false);
                    switcher1.setChecked(false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,true);
                    switcher1.setChecked(true);
                }
                editor.apply();
            }
        });
    }
    public void onClickLinearView(View view){
        sharedPreferences = getActivity().getSharedPreferences("mode", Context.MODE_PRIVATE);
        gridView = sharedPreferences.getBoolean(VIEW,false);
        if(gridView){
            switcher2.setChecked(true);
        }
        switcher2 = view.findViewById(R.id.switchMode2);
        switcher2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gridView){
                    listener.changeLayoutToLinear();
                    editor = sharedPreferences.edit();
                    editor.putBoolean(VIEW,false);
                }
                else {
                    listener.changeLayoutToGrid();
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,true);
                }
                editor.apply();
            }
        });
    }
}