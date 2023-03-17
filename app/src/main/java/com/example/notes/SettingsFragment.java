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
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.notes.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private static SettingsListener settingsListener;
    FragmentSettingsBinding settingsBinding;
    private final String SP_NAME = "view";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static boolean linearLayout;
    private static final String VIEW = "linear_layout";


    public interface SettingsListener {
        void onClickNightMode(SwitchCompat switcher);
        void onClickSetPassword(SwitchCompat switcher);
    }

    public static SettingsFragment newInstance() {
        return  new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        linearLayout = sharedPreferences.getBoolean(VIEW, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        return settingsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsListener.onClickNightMode(settingsBinding.switchMode);
        settingsListener.onClickSetPassword(settingsBinding.switchMode3);
        changeView();
        onSettingsBackClick();
    }

    public void changeView(){
        if (linearLayout) {
            settingsBinding.switchMode2.setChecked(true);
        }
        settingsBinding.switchMode2.setOnClickListener(view1 -> {
            editor = sharedPreferences.edit();
            editor.putBoolean(VIEW, !linearLayout);
            editor.apply();
            back();
        });
    }
    public void onSettingsBackClick() {
        settingsBinding.settingsToolbar.settingsBackIcon.setOnClickListener(view -> {
            back();
        });

    }
    public void back(){
        RecyclerviewFragment fragment = RecyclerviewFragment.newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        settingsListener = (SettingsListener) context;
    }
}