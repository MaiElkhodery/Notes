package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.notes.databinding.FragmentSettingsBinding;
import com.example.notes.databinding.SettingsToolbarBinding;

public class SettingsFragment extends Fragment {

    private static final String MODE ="night_mode";
    public static SwitchCompat switcher1 ;
    SwitchCompat switcher2;
    private static SetSettingsListener listener;
    private SettingsToolbarBinding toolbarBinding;
    private static SettingsListener settingsListener ;
    FragmentSettingsBinding settingsBinding;

    public interface SetSettingsListener{
        void changeView(SwitchCompat switcher);
    }
    public interface SettingsListener{
        void onClickNightMode(SwitchCompat switcher);
        void onClickSetPassword(SwitchCompat switcher);
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
        settingsBinding = FragmentSettingsBinding.inflate(inflater,container,false);
        return settingsBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsListener.onClickNightMode(settingsBinding.switchMode);
        settingsListener.onClickSetPassword(settingsBinding.switchMode3);

        listener.changeView(settingsBinding.switchMode2);
        onSettingsBackClick();
    }
    public void onSettingsBackClick(){
        toolbarBinding=settingsBinding.settingsToolbar;
        toolbarBinding.settingsBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("settings-back","done");
                RecyclerviewFragment fragment = RecyclerviewFragment.newInstance();
                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragmentContainer,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       settingsListener = (SettingsListener) context;
    }
}