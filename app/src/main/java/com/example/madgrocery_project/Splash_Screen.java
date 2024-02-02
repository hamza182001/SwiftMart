package com.example.madgrocery_project;

import static com.example.madgrocery_project.Login_Screen.PREF_AUTHENTICATED;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (isUserAuthenticated()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                    finish();
                }
            },3000);

        } else {
        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash_Screen.this,
                                Intro_Screen.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
    }
}

    private boolean isUserAuthenticated() {

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_AUTHENTICATED, false);
    }
}


