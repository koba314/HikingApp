package com.example.hikingapp.ui.plans;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

/**
 * Dialog for creating a new plan.
 */
public class CreatePlansBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "CreatePlansBSDFrag";

    private PlansViewModel plansViewModel;

    private HikingPlan plan;

    public CreatePlansBottomSheetDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plansViewModel = new ViewModelProvider(requireActivity()).get(PlansViewModel.class);
        // BEWARE: this is a reference to the same plan in the list of plans used by PlansFragment
        plan = plansViewModel.getPlanToEdit().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_plans_bottom_sheet_dialog, container, false);
        EditText name_et = root.findViewById(R.id.plan_name_et);
        EditText startLatitude_et = root.findViewById(R.id.plan_startLatitude_et);
        EditText startLongitude_et = root.findViewById(R.id.plan_startLongitude_et);
        EditText endLatitude_et = root.findViewById(R.id.plan_endLatitude_et);
        EditText endLongitude_et = root.findViewById(R.id.plan_endLongitude_et);
        EditText startTime_et = root.findViewById(R.id.plan_startTime_et);
        EditText endTime_et = root.findViewById(R.id.plan_endTime_et);

        Button save_button = root.findViewById(R.id.plan_save_button);
        Button cancel_button = root.findViewById(R.id.plan_delete_button);
        cancel_button.setText(getString(R.string.action_cancel));

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
                source.createHikingPlan(plan);
                dismiss();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked CANCEL plan");
                dismiss();
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        Log.i(TAG, "onAttach()");
        super.onDetach();
    }

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