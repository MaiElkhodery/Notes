package com.example.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.notes.Database.Database;
import com.example.notes.databinding.FragmentRecyclerviewBinding;
import com.example.notes.databinding.FragmentSettingsBinding;
import com.example.notes.databinding.MainToolbarBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SettingsFragment.NightModeListener {
    public Database database ;
    private RecyclerviewFragment recyclerviewFragment;
    public static final String RECYCLER_FRAG_TAG = "recyclerview";
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private String MODE ="mode";
    boolean nightMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewNotes();
        sharedPreferences = this.getSharedPreferences("modeFile",MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MODE, false);
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    //open/show recycler view
    public void viewNotes(){
        recyclerviewFragment = RecyclerviewFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,recyclerviewFragment)
                .addToBackStack(RECYCLER_FRAG_TAG)
                .commit();
    }

    @Override
    public void onClickNightMode(SwitchCompat switcher) {
        sharedPreferences = this.getSharedPreferences("modeFile",MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MODE, false);
        switcher.setChecked(nightMode);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,false);
                    switcher.setChecked(false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean(MODE,true);
                    switcher.setChecked(true);
                }
                editor.apply();
            }
        });

    }
}