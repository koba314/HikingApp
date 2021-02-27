package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hikingapp.ui.map.MapFragment;
import com.example.hikingapp.ui.plans.PlansFragment;
import com.example.hikingapp.ui.settings.SettingsFragment;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String TAG = "SplashScreenActivity";
    public static final String INTENDED_FRAGMENT = "com.hikingapp.INTENDED_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
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

    //
    //
    // METHOD OVERRIDES FOR LOGGING
    //
    //

    @Override
    public void onStart(){
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onPause(){
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onResume(){
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onStop(){
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}