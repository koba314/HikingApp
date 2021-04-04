package com.example.hikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hikingapp.data.LoginDataSource;
import com.example.hikingapp.data.LoginRepository;
import com.example.hikingapp.ui.login.LoginActivity;
import com.example.hikingapp.ui.map.MapFragment;
import com.example.hikingapp.ui.plans.PlansFragment;
import com.example.hikingapp.ui.settings.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    public static final String TAG = "SplashScreenActivity";
    public static final String INTENDED_FRAGMENT = "com.hikingapp.INTENDED_FRAGMENT";

    private Button loginRegisterButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginRegisterButton = findViewById(R.id.login_register_button);
        logoutButton = findViewById(R.id.logout_button);
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

    /*
     * Login or Register Button Callback
     */
    public void onLoginButtonClick(View view){
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra(LoginActivity.NEXT, TAG);
        startActivity(i);
    }

    /*
     * Logout Button Click
     */
    public void onLogoutButtonClick(View view){
        LoginRepository loginRepo = LoginRepository.getInstance(LoginDataSource.getInstance());
        loginRepo.logout();
        Toast.makeText(getApplicationContext(), "Logged out!", Toast.LENGTH_SHORT).show();
        if(!LoginRepository.getInstance(LoginDataSource.getInstance()).isLoggedIn()){
            loginRegisterButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.GONE);
        }
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            loginRegisterButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
        }
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