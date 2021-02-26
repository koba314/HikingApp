package com.example.hikingapp;

import android.os.Bundle;
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

    private NavController navController;
    private BottomNavigationView navView;
    private MapFragment mapFragment;
    private PlansFragment plansFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get desired fragment from intent
        String fragName = getIntent().getStringExtra(SplashScreenActivity.INTENDED_FRAGMENT);

        setContentView(R.layout.activity_main);

        //initialize fragments
        mapFragment = new MapFragment();
        plansFragment = new PlansFragment();
        settingsFragment = new SettingsFragment();

        //set desired fragment
        setCurrentFragment(getDesiredFragment(fragName));

        // set up navbar functionality
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_map:
                        setCurrentFragment(mapFragment);
                        break;
                    case R.id.navigation_plans:
                        setCurrentFragment(plansFragment);
                        break;
                    case R.id.navigation_settings:
                        setCurrentFragment(settingsFragment);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        //
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_map, R.id.navigation_plans, R.id.navigation_settings)
//                .build();
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

    }

    private void setCurrentFragment(Fragment frag){
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
    }

    private Fragment getDesiredFragment(String fragName){
        Fragment frag;
        switch(fragName){
            case MapFragment.MAP_FRAGMENT:
                frag = mapFragment;
                break;
            case PlansFragment.PLANS_FRAGMENT:
                frag = plansFragment;
                break;
            case SettingsFragment.SETTINGS_FRAGMENT:
                frag = settingsFragment;
                break;
            default:
                frag = null;
        }
        return frag;
    }

}