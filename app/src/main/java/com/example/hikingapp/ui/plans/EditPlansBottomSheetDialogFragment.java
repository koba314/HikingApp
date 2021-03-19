package com.example.hikingapp.ui.plans;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.hikingapp.R;
import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.OffsetDateTime;

/**
 * EditPlanBottomSheetDialogFragment
 * The dialog bottom sheet for editing a hiking plan.
 */
public class EditPlansBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "EditPlanBSDialogFrag";

    private HikingPlan plan;

    public EditPlansBottomSheetDialogFragment(HikingPlan plan) {
        this.plan = plan;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        View root = inflater.inflate(R.layout.fragment_edit_plans_bottom_sheet_dialog, container, false);
        EditText name_et = root.findViewById(R.id.plan_name_et);
        EditText startLatitude_et = root.findViewById(R.id.plan_startLatitude_et);
        EditText startLongitude_et = root.findViewById(R.id.plan_startLongitude_et);
        EditText endLatitude_et = root.findViewById(R.id.plan_endLatitude_et);
        EditText endLongitude_et = root.findViewById(R.id.plan_endLongitude_et);
        EditText startTime_et = root.findViewById(R.id.plan_startTime_et);
        EditText endTime_et = root.findViewById(R.id.plan_endTime_et);
        //TODO: create viewmodel for this fragment to handle input validation
        name_et.setText(plan.getName());
        startLatitude_et.setText(Double.toString(plan.getStartLatitude()));
        startLongitude_et.setText(Double.toString(plan.getStartLongitude()));
        endLatitude_et.setText(Double.toString(plan.getEndLatitude()));
        endLongitude_et.setText(Double.toString(plan.getEndLongitude()));
        startTime_et.setText(plan.getStartTime());
        endTime_et.setText(plan.getEndTime());

        Button save_button = root.findViewById(R.id.plan_save_button);
        Button delete_button = root.findViewById(R.id.plan_delete_button);
        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked SAVE plan");
                plan.setName(name_et.getText().toString());
                plan.setStartLatitude(Double.valueOf(startLatitude_et.getText().toString()));
                plan.setStartLongitude(Double.valueOf(startLongitude_et.getText().toString()));
                plan.setEndLatitude(Double.valueOf(endLatitude_et.getText().toString()));
                plan.setEndLongitude(Double.valueOf(endLongitude_et.getText().toString()));
                plan.setStartTime(startTime_et.getText().toString());
                plan.setEndTime(endTime_et.getText().toString());
                HikingPlanDataSource source = HikingPlanDataSource.getInstance();
                source.updateHikingPlan(plan);
                dismiss();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked DELETE plan");
                HikingPlanDataSource source = HikingPlanDataSource.getInstance();
                source.deleteHikingPlan(plan);
                dismiss();
            }
        });
        return root;
    }
}