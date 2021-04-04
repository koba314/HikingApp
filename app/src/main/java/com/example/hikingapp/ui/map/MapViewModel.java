package com.example.hikingapp.ui.map;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.model.MapPin;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel implements HikingPlanDataSource.HikingPlanListener {

    private HikingPlanDataSource hikingPlanDataSource;

    private MutableLiveData<String> mText;
    private MutableLiveData<List<MapPin>> mPins;
    private MutableLiveData<LatLng> mPos;
    private MutableLiveData<HikingPlan> mActiveHikingPlan;
    private MutableLiveData<List<HikingPlan>> mVisibleHikingPlans;

    public MapViewModel() {
        hikingPlanDataSource = HikingPlanDataSource.getInstance();
        hikingPlanDataSource.initHikingPlansListener(this);
        mText = new MutableLiveData<>();
        mText.setValue("TODO: map fragment");
        mPins = new MutableLiveData<>();
        mPos = new MutableLiveData<>();
        mActiveHikingPlan = new MutableLiveData<>();
        mVisibleHikingPlans = new MutableLiveData<>();
    }

    public LiveData<List<MapPin>> getPins() { return mPins; }

    public LiveData<HikingPlan> getActivePlan(){ return mActiveHikingPlan; }
    public LiveData<List<HikingPlan>> getVisiblePlans() { return mVisibleHikingPlans; }

    public LiveData<LatLng> getPos() { return mPos; }
    public void setPos(LatLng pos) { mPos.setValue(pos); }

    public LiveData<String> getText() {
        return mText;
    }

    /**
     * Sets the active hiking plan as checked-in and deactivates all plans.
     */
    public void checkIn(){
        HikingPlan plan = mActiveHikingPlan.getValue();
        if(plan != null){
            plan.setCheckedIn(true);
            plan.setActive(false);
            plan.setVisible(false);
            hikingPlanDataSource.updateHikingPlan(plan);
            //check and enforce constraint of only one active hiking plan
            List<HikingPlan> plans = mVisibleHikingPlans.getValue();
            if(plans != null){
                for(HikingPlan otherPlan : mVisibleHikingPlans.getValue()){
                    otherPlan.setActive(false);
                    hikingPlanDataSource.updateHikingPlan(otherPlan);
                }
            }
        }
    }

    @Override
    public void onGetHikingPlans(List<HikingPlan> plans){
        List<HikingPlan> visiblePlans = new ArrayList<>();
        mActiveHikingPlan.setValue(null);
        // get the active plan and visible plans
        for(HikingPlan plan : plans){
            if(plan.getActive()){
                mActiveHikingPlan.setValue(plan);
            }
            if(plan.getVisible()){
                visiblePlans.add(plan);
            }
        }
        mVisibleHikingPlans.setValue(visiblePlans);
    }

}