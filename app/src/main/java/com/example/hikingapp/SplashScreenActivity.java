package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hikingapp.ui.map.MapFragment;
import com.example.hikingapp.ui.plans.PlansFragment;
import com.example.hikingapp.ui.settings.SettingsFragment;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String INTENDED_FRAGMENT = "com.hikingapp.INTENDED_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    /*
     * Map Button Callback
     */
    public void onMapButtonClick(View view){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(INTENDED_FRAGMENT, MapFragment.MAP_FRAGMENT);
        startActivity(i);
    }

    /*
     * Hiking Plans Button Callback
     */
    public void onPlansButtonClick(View view){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(INTENDED_FRAGMENT, PlansFragment.PLANS_FRAGMENT);
        startActivity(i);
    }

    /*
     * Settings Button Callback
     */
    public void onSettingsButtonClick(View view){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(INTENDED_FRAGMENT, SettingsFragment.SETTINGS_FRAGMENT);
        startActivity(i);
    }
}