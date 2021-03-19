package com.example.hikingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.hikingapp.ui.map.MapFragment;
import com.example.hikingapp.ui.plans.PlansFragment;
import com.example.hikingapp.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private NavController navController;
    private BottomNavigationView navView;
    private MapFragment mapFragment;
    private PlansFragment plansFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        //Get desired fragment from intent
        String fragName = getIntent().getStringExtra(SplashScreenActivity.INTENDED_FRAGMENT);

        setContentView(R.layout.activity_main);

        //initialize fragments
        mapFragment = new MapFragment();
        plansFragment = new PlansFragment();
        settingsFragment = new SettingsFragment();



        // set up navbar functionality
        navView = findViewById(R.id.nav_view);
        //set nav view listener
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_map:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mapFragment).commit();
                        break;
                    case R.id.navigation_plans:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, plansFragment).commit();
                        break;
                    case R.id.navigation_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, settingsFragment).commit();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        //set desired fragment
        setCurrentFragment(fragName);

        //
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_map, R.id.navigation_plans, R.id.navigation_settings)
//                .build();
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

    }

    private void setCurrentFragment(String fragName){
        Fragment frag;
        switch(fragName){
            case MapFragment.MAP_FRAGMENT:
                frag = mapFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                navView.setSelectedItemId(R.id.navigation_map);
                break;
            case PlansFragment.PLANS_FRAGMENT:
                frag = plansFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                navView.setSelectedItemId(R.id.navigation_plans);
                break;
            case SettingsFragment.SETTINGS_FRAGMENT:
                frag = settingsFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
                navView.setSelectedItemId(R.id.navigation_settings);
                break;
            default:
                frag = null;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
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