package com.example.hikingapp.ui.plans;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;

import java.util.ArrayList;
import java.util.List;

public class PlansViewModel extends ViewModel implements HikingPlanDataSource.HikingPlanListener {

    private HikingPlanDataSource hikingPlanDataSource;
    private MutableLiveData<String> mText;
    private MutableLiveData<List<HikingPlan>> mPlans;

    public PlansViewModel() {
        hikingPlanDataSource = HikingPlanDataSource.getInstance();
        hikingPlanDataSource.initHikingPlansListener(this);
        mText = new MutableLiveData<>();
        mText.setValue("TODO: plans fragment");
        mPlans = new MutableLiveData<List<HikingPlan>>();
        //ensure mPlans does not hold a null value while waiting for data
        mPlans.setValue(new ArrayList<HikingPlan>());
        hikingPlanDataSource.getHikingPlans(this);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<HikingPlan>> getPlans(){ return mPlans; }

    public void onGetHikingPlans(List<HikingPlan> plans){
        mPlans.setValue(plans);
    }
}