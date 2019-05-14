package com.reinis.gaztest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.reinis.gaztest.R.layout.activity_splash);


        Intent intent =new Intent(this,MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
