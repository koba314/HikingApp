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
    private MutableLiveData<HikingPlan> planToEdit;

    public PlansViewModel() {
        hikingPlanDataSource = HikingPlanDataSource.getInstance();
        hikingPlanDataSource.initHikingPlansListener(this);
        mText = new MutableLiveData<>();
        mText.setValue("TODO: plans fragment");
        mPlans = new MutableLiveData<List<HikingPlan>>();
        //ensure mPlans does not hold a null value while waiting for data
        mPlans.setValue(new ArrayList<HikingPlan>());
        planToEdit = new MutableLiveData<>();
        hikingPlanDataSource.getHikingPlans(this);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<HikingPlan>> getPlans(){ return mPlans; }

    public LiveData<HikingPlan> getPlanToEdit(){ return planToEdit; }
    public void setPlanToEdit(HikingPlan plan){ planToEdit.setValue(plan); }

    /**
     * Activates a hiking plan by setting it active, updating the data source, and
     * deactivating all other plans in the data source.
     * @param plan the HikingPlan to activate
     */
    public void activateHikingPlan(HikingPlan plan){
        //enforce constraint of only one active hiking plan
        deactivateHikingPlans();
        plan.setCheckedIn(false);
        plan.setActive(true);
        hikingPlanDataSource.updateHikingPlan(plan);
    }

    /**
     * Deactivates ALL hiking plans in the data source.
     */
    public void deactivateHikingPlans(){
        List<HikingPlan> plans = mPlans.getValue();
        if(plans != null){
            for(HikingPlan plan : plans){
                plan.setActive(false);
                hikingPlanDataSource.updateHikingPlan(plan);
            }
        }
    }

    public void createHikingPlan(HikingPlan plan){
        hikingPlanDataSource.createHikingPlan(plan);
    }

    public void updateHikingPlan(HikingPlan plan){
        hikingPlanDataSource.updateHikingPlan(plan);
    }

    public void deleteHikingPlan(HikingPlan plan){
        hikingPlanDataSource.deleteHikingPlan(plan);
    }

    public void onGetHikingPlans(List<HikingPlan> plans){
        // always put the active plan first
        for(HikingPlan plan : plans){
            if(plan.getActive()){
                plans.remove(plan);
                plans.add(0, plan);
                break;
            }
        }
        mPlans.setValue(plans);
    }
}