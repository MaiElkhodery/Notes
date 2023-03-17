package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;


public class MainActivity extends AppCompatActivity implements SettingsFragment.SettingsListener,SetPasswordDialog.SetOnClickListener {
    private RecyclerviewFragment recyclerviewFragment;
    SharedPreferences sharedPreferences ;
    private final String SH_NAME = "modeFile";
    private final String PASSWORD = "password";
    static String password;
    SharedPreferences.Editor editor;
    private final String MODE ="mode";
    boolean nightMode;
    public static SwitchCompat passwordSwitcher;
    boolean nightModeIsClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerviewFragment = RecyclerviewFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer,recyclerviewFragment)
                .addToBackStack(null)
                .commit();
    }

    public void nightMode(){
        sharedPreferences = getSharedPreferences(SH_NAME,MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MODE, false);
        if(nightMode ){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @Override
    public void onClickNightMode(SwitchCompat switcher) {
        sharedPreferences = this.getSharedPreferences(SH_NAME,MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean(MODE, false);
        switcher.setChecked(nightMode);
        switcher.setOnClickListener(view -> {
            nightModeIsClicked = true;
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
            recreate();
        });
    }

    @Override
    public void onClickSetPassword(SwitchCompat switcher) {
        passwordSwitcher = switcher;
        sharedPreferences = getSharedPreferences(SH_NAME,MODE_PRIVATE);
        password = sharedPreferences.getString(PASSWORD,null);
        switcher.setChecked(password!=null);
        switcher.setOnClickListener(view -> {
            if(switcher.isChecked()){
                Log.d("PassOperate", "Open Pass Dialog");
                SetPasswordDialog dialog = SetPasswordDialog.newInstance();
                dialog.show(getSupportFragmentManager(),null);

            }else{
                editor = sharedPreferences.edit();
                editor.putString(PASSWORD,null);
                editor.apply();
                password = null;
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