package com.example.hikingapp.ui.plans;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hikingapp.R;
import com.example.hikingapp.model.HikingPlan;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.OffsetDateTime;

/**
 * The Dialog for viewing and possibly activating a plan.
 */
public class ViewPlansBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ViewPlansBSDFrag";

    private HikingPlan plan;

    public ViewPlansBottomSheetDialogFragment(HikingPlan plan) {
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
        View root = inflater.inflate(R.layout.fragment_view_plans_bottom_sheet_dialog, container, false);
        TextView name_tv = root.findViewById(R.id.plan_name_tv);
        TextView startLatitude_tv = root.findViewById(R.id.plan_startLatitude_tv);
        TextView startLongitude_tv = root.findViewById(R.id.plan_startLongitude_tv);
        TextView endLatitude_tv = root.findViewById(R.id.plan_endLatitude_tv);
        TextView endLongitude_tv = root.findViewById(R.id.plan_endLongitude_tv);
        TextView startTime_tv = root.findViewById(R.id.plan_startTime_tv);
        TextView endTime_tv = root.findViewById(R.id.plan_endTime_tv);
        name_tv.setText(plan.getName());
        startLatitude_tv.setText(Double.toString(plan.getStartLatitude()));
        startLongitude_tv.setText(Double.toString(plan.getStartLongitude()));
        endLatitude_tv.setText(Double.toString(plan.getEndLatitude()));
        endLongitude_tv.setText(Double.toString(plan.getEndLongitude()));
        startTime_tv.setText(plan.getStartTime());
        endTime_tv.setText(plan.getEndTime());
        return root;
    }
}