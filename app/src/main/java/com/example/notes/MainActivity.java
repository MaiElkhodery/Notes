package com.example.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements SettingsFragment.SettingsListener,SetPasswordDialog.SetOnClickListener {
    private RecyclerviewFragment recyclerviewFragment;
    public static final String RECYCLER_FRAG_TAG = "recyclerview";
    SharedPreferences sharedPreferences ;
    private final String SH_NAME = "modeFile";
    private final String PASSWORD = "password";
    static String password;
    SharedPreferences.Editor editor;
    private String MODE ="mode";
    boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewNotes();
        setPassword();
        nightMode();
    }

    //open/show recycler view
    public void viewNotes(){
        recyclerviewFragment = RecyclerviewFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,recyclerviewFragment)
                .addToBackStack(RECYCLER_FRAG_TAG)
                .commit();
    }
    public void nightMode(){
        sharedPreferences = this.getSharedPreferences(SH_NAME,MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MODE, false);
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public void setPassword(){
        sharedPreferences = getSharedPreferences(SH_NAME,MODE_PRIVATE);
        password = sharedPreferences.getString(PASSWORD,null);
        if(password != null){
            EnterPasswordDialog dialog = EnterPasswordDialog.newInstance(password);
            dialog.show(getSupportFragmentManager(),null);
        }
    }

    @Override
    public void onClickNightMode(SwitchCompat switcher) {
        sharedPreferences = this.getSharedPreferences(SH_NAME,MODE_PRIVATE);
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

    @Override
    public void onClickSetPassword(SwitchCompat switcher) {
        sharedPreferences = getSharedPreferences(SH_NAME,MODE_PRIVATE);
        password = sharedPreferences.getString(PASSWORD,null);
        switcher.setChecked(password!=null);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switcher.isChecked()){
                    SetPasswordDialog dialog = SetPasswordDialog.newInstance();
                    dialog.show(getSupportFragmentManager(),null);
                    if(SetPasswordDialog.isBackClicked){
                        switcher.setChecked(false);
                    }

                }else{
                    editor = sharedPreferences.edit();
                    editor.putString(PASSWORD,null);
                    editor.apply();
                    password = null;
                }
            }
        });
    }

    @Override
    public void onClickSave(String text) {
        sharedPreferences = getSharedPreferences(SH_NAME,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(PASSWORD,text);
        editor.apply();
    }
}