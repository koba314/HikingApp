package com.example.hikingapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hikingapp.model.HikingPlan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HikingPlanDataSource {
    public static final String TAG = "HikingPlanDataSource";

    private static HikingPlanDataSource instance;
    private static ValueEventListener dbListener;

    private FirebaseAuth auth;
    private FirebaseDatabase db;

    public interface HikingPlanListener{
        void onGetHikingPlans(List<HikingPlan> plans);
    }

    private HikingPlanDataSource(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    public static HikingPlanDataSource getInstance(){
        if(instance == null){
            instance = new HikingPlanDataSource();
        }
        return instance;
    }

    public void createHikingPlan(HikingPlan plan){
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference newPlan = db.getReference().child("user").child(user.getUid()).child("hikingplan").push();
        plan.setId(newPlan.getKey());
        newPlan.setValue(plan);
    }

    /**
     * initHikingPlansListener
     * Sets up a value event listener on the firebase DB and links it to a desired callback to receive the data
     * @param listener: the HikingPlanListener implementing the callback to receive data changes
     */
    public void initHikingPlansListener(HikingPlanListener listener){
        FirebaseUser user = auth.getCurrentUser();
        dbListener = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Log.i(TAG, "data=" + dataSnapshot.toString());
                List<HikingPlan> plans = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    HikingPlan plan = data.getValue(HikingPlan.class);
                    plans.add(plan);
                }
                listener.onGetHikingPlans(plans);
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.w(TAG, "db operation failed");
            }
        };
        db.getReference().child("user").child(user.getUid()).child("hikingplan").addValueEventListener(dbListener);
    }

    /**
     * getHikingPlans
     * Gets all hiking plans for the current user once.
     * @param listener: the HikingPlanListener implementing the callback to receive data
     */
    public void getHikingPlans(HikingPlanListener listener){
        FirebaseUser user = auth.getCurrentUser();
        db.getReference().child("user").child(user.getUid()).child("hikingplan").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    List<HikingPlan> plans = new ArrayList<>();
                    DataSnapshot ds = task.getResult();
                    for(DataSnapshot data : ds.getChildren()){
                        HikingPlan plan = data.getValue(HikingPlan.class);
                        plans.add(plan);
                    }
                    listener.onGetHikingPlans(plans);
                }else{
                    Log.w(TAG, "error getting hiking plans from DB");
                }
            }
        });
    }

    public void updateHikingPlan(HikingPlan plan){
        Log.i(TAG, "updateHikingPlan for plan " + plan.getId());
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference dbRef = db.getReference().child("user").child(user.getUid()).child("hikingplan").child(plan.getId());
        dbRef.setValue(plan);
    }

    public void deleteHikingPlan(HikingPlan plan){
        Log.i(TAG, "deleteHikingPlan for plan " + plan.getId());
        FirebaseUser user = auth.getCurrentUser();
        DatabaseReference dbRef = db.getReference().child("user").child(user.getUid()).child("hikingplan").child(plan.getId());
        dbRef.removeValue();
    }
}
