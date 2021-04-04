package com.example.hikingapp.ui.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hikingapp.R;
import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * The Dialog for viewing and possibly activating a plan.
 */
public class ViewPlansBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ViewPlansBSDFrag";

    private PlansViewModel plansViewModel;

    private HikingPlan plan;

    public ViewPlansBottomSheetDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        plansViewModel =
                new ViewModelProvider(requireActivity()).get(PlansViewModel.class);
        plan = plansViewModel.getPlanToEdit().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_plans_bottom_sheet_dialog, container, false);
        TextView name_tv = root.findViewById(R.id.plan_name_tv);
        TextView startLatitude_tv = root.findViewById(R.id.plan_startLatitude_tv);
        TextView startLongitude_tv = root.findViewById(R.id.plan_startLongitude_tv);
        TextView endLatitude_tv = root.findViewById(R.id.plan_endLatitude_tv);
        TextView endLongitude_tv = root.findViewById(R.id.plan_endLongitude_tv);
        TextView startTime_tv = root.findViewById(R.id.plan_startTime_tv);
        TextView endTime_tv = root.findViewById(R.id.plan_endTime_tv);
        TextView startDate_tv = root.findViewById(R.id.plan_startDate_tv);
        TextView endDate_tv = root.findViewById(R.id.plan_endDate_tv);
        name_tv.setText(plan.getName());
        startLatitude_tv.setText(Double.toString(plan.getStartLatitude()));
        startLongitude_tv.setText(Double.toString(plan.getStartLongitude()));
        endLatitude_tv.setText(Double.toString(plan.getEndLatitude()));
        endLongitude_tv.setText(Double.toString(plan.getEndLongitude()));
        startTime_tv.setText(plan.getStartTime());
        endTime_tv.setText(plan.getEndTime());
        startDate_tv.setText(plan.getStartDate());
        endDate_tv.setText(plan.getEndDate());

        Button activate = root.findViewById(R.id.activate_plan_button);
        if(plan.getActive()){
            activate.setText(getString(R.string.action_deactivate_plan));
        }
        activate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (plan.getActive()) {
                    Log.i(TAG, "clicked DEACTIVATE plan");
                    plansViewModel.deactivateHikingPlans();
                }else{
                    Log.i(TAG, "clicked ACTIVATE plan");
                    plansViewModel.activateHikingPlan(plan);
                }
                dismiss();
            }
        });

        return root;
    }
}