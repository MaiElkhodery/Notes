package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EnterPassword_MainActivity extends AppCompatActivity implements EnterPasswordDialog.OnConfirmListener {

    SharedPreferences sharedPreferences ;
    private final String SH_NAME = "modeFile";
    private final String PASSWORD = "password";
    static String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password_main);
        sharedPreferences = getSharedPreferences(SH_NAME,MODE_PRIVATE);
        password = sharedPreferences.getString(PASSWORD,null);
        if (password == null){
            Intent intent = new Intent(EnterPassword_MainActivity.this,MainActivity.class);
            startActivity(intent);
        }
        else
            setPassword();
    }
    public void setPassword(){
        EnterPasswordDialog fragment = EnterPasswordDialog.newInstance(password,this);
        getSupportFragmentManager().beginTransaction().replace(R.id.passContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickConfirm() {
        Intent intent = new Intent(EnterPassword_MainActivity.this,MainActivity.class);
        startActivity(intent);
    }
}