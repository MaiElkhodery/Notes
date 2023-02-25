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

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean gridView;
    private static final String MODE ="night_mode";
    private static final String VIEW ="grid_view";
    public static SwitchCompat switcher1 ;
    SwitchCompat switcher2;
    private static SetSettingsListener listener;
    private SettingsToolbarBinding toolbarBinding;
    private static NightModeListener nightModeListener ;
    FragmentSettingsBinding settingsBinding;

    public interface SetSettingsListener{
        void changeLayoutToLinear();
        void changeLayoutToGrid();
    }
    public interface NightModeListener{
        void onClickNightMode(SwitchCompat switcher);
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

        onClickNightMode();
        onClickLinearView(view);
        onSettingsBackClick();
    }


    public void onClickNightMode(){
        switcher1 = settingsBinding.switchMode;
        nightModeListener.onClickNightMode(switcher1);
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
       nightModeListener = (NightModeListener) context;
    }
}