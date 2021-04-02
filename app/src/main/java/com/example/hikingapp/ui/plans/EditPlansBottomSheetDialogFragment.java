package com.example.hikingapp.ui.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hikingapp.R;
import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.ui.map.MapLocationSelectorActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.time.OffsetDateTime;

/**
 * EditPlanBottomSheetDialogFragment
 * The dialog bottom sheet for editing a hiking plan.
 */
public class EditPlansBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "EditPlanBSDialogFrag";

    private PlansViewModel plansViewModel;

    private HikingPlan plan;

    EditText name_et;
    EditText startLatitude_et;
    EditText startLongitude_et;
    EditText endLatitude_et;
    EditText endLongitude_et;
    EditText startTime_et;
    EditText endTime_et;

    public EditPlansBottomSheetDialogFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        plansViewModel =
                new ViewModelProvider(requireActivity()).get(PlansViewModel.class);
        // BEWARE: this is a reference to the same plan in the list of plans used by PlansFragment
        plan = plansViewModel.getPlanToEdit().getValue();
        // set start,end pos in the singleton shared by this fragment and the MapLocationSelectorActivity
        MapLocationSelectorActivity.MapLocations locations = MapLocationSelectorActivity.MapLocations.getInstance();
        LatLng start = new LatLng(plan.getStartLatitude(), plan.getStartLongitude());
        locations.setStart(start);
        LatLng end = new LatLng(plan.getEndLatitude(), plan.getEndLongitude());
        locations.setEnd(start);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        View root = inflater.inflate(R.layout.fragment_edit_plans_bottom_sheet_dialog, container, false);
        name_et = root.findViewById(R.id.plan_name_et);
        startLatitude_et = root.findViewById(R.id.plan_startLatitude_et);
        startLongitude_et = root.findViewById(R.id.plan_startLongitude_et);
        endLatitude_et = root.findViewById(R.id.plan_endLatitude_et);
        endLongitude_et = root.findViewById(R.id.plan_endLongitude_et);
        startTime_et = root.findViewById(R.id.plan_startTime_et);
        endTime_et = root.findViewById(R.id.plan_endTime_et);

        //TODO: CREATE VIEWMODEL FOR THE FORM DATA
        name_et.setText(plan.getName());
        startLatitude_et.setText(Double.toString(plan.getStartLatitude()));
        startLongitude_et.setText(Double.toString(plan.getStartLongitude()));
        endLatitude_et.setText(Double.toString(plan.getEndLatitude()));
        endLongitude_et.setText(Double.toString(plan.getEndLongitude()));
        startTime_et.setText(plan.getStartTime());
        endTime_et.setText(plan.getEndTime());

        ImageView start_pos_button = root.findViewById(R.id.start_location_selector_button);
        ImageView end_pos_button = root.findViewById(R.id.end_location_selector_button);
        start_pos_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // set start,end pos in the singleton shared by this fragment and the MapLocationSelectorActivity
                MapLocationSelectorActivity.MapLocations locations = MapLocationSelectorActivity.MapLocations.getInstance();
                // TODO: use lat,lng from form viewmodel instead of janky text-to-double conversion
                LatLng start = new LatLng(Double.valueOf(startLatitude_et.getText().toString()), Double.valueOf(startLongitude_et.getText().toString()));
                locations.setStart(start);
                LatLng end = new LatLng(Double.valueOf(endLatitude_et.getText().toString()), Double.valueOf(endLongitude_et.getText().toString()));
                locations.setEnd(end);

                Intent i = new Intent(getContext(), MapLocationSelectorActivity.class);
                i.putExtra(MapLocationSelectorActivity.MODE, MapLocationSelectorActivity.START);
                startActivity(i);
            }
        });
        end_pos_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // set start,end pos in the singleton shared by this fragment and the MapLocationSelectorActivity
                MapLocationSelectorActivity.MapLocations locations = MapLocationSelectorActivity.MapLocations.getInstance();
                // TODO: use lat,lng from form viewmodel instead of janky text-to-double conversion
                LatLng start = new LatLng(Double.valueOf(startLatitude_et.getText().toString()), Double.valueOf(startLongitude_et.getText().toString()));
                locations.setStart(start);
                LatLng end = new LatLng(Double.valueOf(endLatitude_et.getText().toString()), Double.valueOf(endLongitude_et.getText().toString()));
                locations.setEnd(end);

                Intent i = new Intent(getContext(), MapLocationSelectorActivity.class);
                i.putExtra(MapLocationSelectorActivity.MODE, MapLocationSelectorActivity.END);
                startActivity(i);
            }
        });

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
        MapLocationSelectorActivity.MapLocations locations = MapLocationSelectorActivity.MapLocations.getInstance();
        startLatitude_et.setText(Double.toString(locations.getStart().getLatitude()));
        startLongitude_et.setText(Double.toString(locations.getStart().getLongitude()));
        endLatitude_et.setText(Double.toString(locations.getEnd().getLatitude()));
        endLongitude_et.setText(Double.toString(locations.getEnd().getLongitude()));
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