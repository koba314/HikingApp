package com.example.hikingapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.ui.map.MapFragment;
import com.example.hikingapp.ui.map.MapViewModel;
import com.example.hikingapp.ui.plans.PlansFragment;
import com.example.hikingapp.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private BottomNavigationView navView;
    private MapFragment mapFragment;
    private PlansFragment plansFragment;
    private SettingsFragment settingsFragment;
    private MapViewModel mapViewModel;

    private View topBar;
    private TextView checkIn_tv;
    private Button checkIn_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        //Get desired fragment from intent
        String fragName = getIntent().getStringExtra(SplashScreenActivity.INTENDED_FRAGMENT);

        setContentView(R.layout.activity_main);

        topBar = findViewById(R.id.top_bar);
        checkIn_tv = findViewById(R.id.active_plan_check_in_tv);
        checkIn_button = findViewById(R.id.check_in_button);

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

        mapViewModel.getActivePlan().observe(this, new Observer<HikingPlan>() {
            @Override
            public void onChanged(@Nullable HikingPlan plan) {
                if(plan == null){
                    topBar.setVisibility(View.GONE);
                }else{
                    topBar.setVisibility(View.VISIBLE);
                    String result = getDateTimeString(plan);
                    if(result != null){
                        checkIn_tv.setText(result);
                    }
                }
            }
        });

        checkIn_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mapViewModel.checkIn();
            }
        });

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

    @Nullable
    private String getDateTimeString(HikingPlan plan){
        String endDate = plan.getEndDate();
        String endTime = plan.getEndTime();
        try{
            String[] endDateArr = endDate.split("/",3);
            int year = Integer.parseInt(endDateArr[0]);
            int month = Integer.parseInt(endDateArr[1]);
            int day = Integer.parseInt(endDateArr[2]);

            String[] endTimeArr = endTime.split(":", 2);
            int hour = Integer.parseInt(endTimeArr[0]);
            int minute = Integer.parseInt(endTimeArr[1]);
            String min = "";
            if(minute < 10){
                min = "0" + minute;
            }else{
                min += minute;
            }
            String time = "";
            if(DateFormat.is24HourFormat(this)){
                time = endTime;
            }else if(hour > 12){
                time = (hour-12) + ":" + min + " PM";
            }else{
                time = hour + ":" + min + " AM";
            }

            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);
            int currentMonth = now.get(Calendar.MONTH);
            int currentDay = now.get(Calendar.DAY_OF_MONTH);
            String result = "";
            if(currentYear >= year && currentMonth >= month && currentDay > day){
                result = "Check-in missed!";
            }else if(currentYear == year && currentMonth == month && currentDay == day){
                result = time + " Today";
            }else if(currentYear == year && currentMonth == month){
                String wkDay = "";
                if(day - currentDay < 7 && day - currentDay > 1){
                    switch(now.get(Calendar.DAY_OF_WEEK)){
                        case(Calendar.SUNDAY):{
                            wkDay = "Sun";
                            break;
                        }
                        case(Calendar.MONDAY):{
                            wkDay = "Mon";
                            break;
                        }
                        case(Calendar.TUESDAY):{
                            wkDay = "Tue";
                            break;
                        }
                        case(Calendar.WEDNESDAY):{
                            wkDay = "Wed";
                            break;
                        }
                        case(Calendar.THURSDAY):{
                            wkDay = "Thur";
                            break;
                        }
                        case(Calendar.FRIDAY):{
                            wkDay = "Fri";
                        }
                        case(Calendar.SATURDAY):{
                            wkDay = "Sat";
                        }
                    }
                }else if (day - currentDay == 1){
                    wkDay = "Tomorrow";
                }
                result = time + " " + wkDay;
            }else if(currentYear == year){
                result = month + "/" + day + " " + time;
            }else{
                result = endDate + " " + time;
            }
            return result;
        }catch(Exception e){
            Log.w(TAG, "Error parsing date from hiking plan.");
            return null;
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