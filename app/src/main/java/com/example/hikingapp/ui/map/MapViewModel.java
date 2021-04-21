package com.example.hikingapp.ui.map;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.data.MapPinDataSource;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.model.MapPin;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel implements HikingPlanDataSource.HikingPlanListener, MapPinDataSource.MapPinListener {

    private HikingPlanDataSource hikingPlanDataSource;
    private MapPinDataSource mapPinDataSource;

    private MutableLiveData<LatLng> mPos;
    private MutableLiveData<HikingPlan> mActiveHikingPlan;
    private MutableLiveData<List<HikingPlan>> mVisibleHikingPlans;
    private MutableLiveData<List<MapPin>> mPublicPins;
    private MutableLiveData<List<MapPin>> mUserPins;
    private MutableLiveData<List<MapPin>> mVisiblePins;
    private MutableLiveData<MapPin> mPinToView;

    private MutableLiveData<LatLng> mCameraPos;
    private MutableLiveData<Double> mZoomLevel;

    public MapViewModel() {
        hikingPlanDataSource = HikingPlanDataSource.getInstance();
        hikingPlanDataSource.initHikingPlansListener(this);
        mapPinDataSource = MapPinDataSource.getInstance();
        mapPinDataSource.initMapPinListener(this);
        mPos = new MutableLiveData<>();
        mActiveHikingPlan = new MutableLiveData<>();
        mVisibleHikingPlans = new MutableLiveData<>();
        mPublicPins = new MutableLiveData<>();
        mUserPins = new MutableLiveData<>();
        mVisiblePins = new MutableLiveData<>();
        mPinToView = new MutableLiveData<>();

        mCameraPos = new MutableLiveData<>();
        mZoomLevel = new MutableLiveData<>();
    }

    public LiveData<MapPin> getPinToView(){ return mPinToView; }
    public void setPinToView(MapPin pin){ this.mPinToView.setValue(pin); }

    public LiveData<HikingPlan> getActivePlan(){ return mActiveHikingPlan; }
    public LiveData<List<HikingPlan>> getVisiblePlans() { return mVisibleHikingPlans; }

    public LiveData<List<MapPin>> getPublicPins() { return mPublicPins; }
    public LiveData<List<MapPin>> getUserPins() { return mUserPins; }

    public LiveData<LatLng> getPos() { return mPos; }
    public void setPos(LatLng pos) { mPos.setValue(pos); }

    public LiveData<LatLng> getCameraPos(){ return mCameraPos; }
    public void setCameraPos(LatLng pos){ mCameraPos.setValue(pos); }
    public LiveData<Double> getZoomLevel(){ return mZoomLevel; }
    public void setZoomLevel(double zoom ){ mZoomLevel.setValue(zoom); }

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

    public void createMapPin(MapPin pin){ mapPinDataSource.createMapPin(pin); }
    public void updateMapPin(MapPin pin){ mapPinDataSource.updateMapPin(pin); }
    public void deleteMapPin(MapPin pin){ mapPinDataSource.deleteMapPin(pin); }

    @Override
    public void onGetHikingPlans(List<HikingPlan> plans){
        List<HikingPlan> visiblePlans = new ArrayList<>();
        // get the active plan and visible plans
        boolean noActivePlan = true;
        for(HikingPlan plan : plans){
            if(plan.getActive()){
                mActiveHikingPlan.setValue(plan);
                noActivePlan = false;
            }
            if(plan.getVisible()){
                visiblePlans.add(plan);
            }
        }
        if(noActivePlan){
            mActiveHikingPlan.setValue(null);
        }
        mVisibleHikingPlans.setValue(visiblePlans);
    }

    @Override
    public void onGetOtherMapPins(List<MapPin> pins){
        mPublicPins.setValue(pins);
    }

    @Override
    public void onGetUserMapPins(List<MapPin> pins){
        mUserPins.setValue(pins);
    }

}