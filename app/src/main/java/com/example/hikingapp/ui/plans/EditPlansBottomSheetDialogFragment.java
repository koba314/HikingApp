package com.example.hikingapp.ui.plans;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hikingapp.R;
import com.example.hikingapp.data.HikingPlanDataSource;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.ui.map.MapLocationSelectorActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;

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
    Button startTime_button;
    Button endTime_button;
    Button startDate_button;
    Button endDate_button;

    public EditPlansBottomSheetDialogFragment(){ }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        plansViewModel =
                new ViewModelProvider(requireActivity()).get(PlansViewModel.class);
        // BEWARE: this is a reference to the same plan in the list of plans used by PlansFragment
        plan = plansViewModel.getPlanToEdit().getValue();
    }

    @SuppressLint("SetTextI18n")
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
        startTime_button = root.findViewById(R.id.plan_startTime_button);
        endTime_button = root.findViewById(R.id.plan_endTime_button);
        startDate_button = root.findViewById(R.id.plan_startDate_button);
        endDate_button = root.findViewById(R.id.plan_endDate_button);

        //TODO: CREATE VIEWMODEL FOR THE FORM DATA
        name_et.setText(plan.getName());
        startLatitude_et.setText(Double.toString(plan.getStartLatitude()));
        startLongitude_et.setText(Double.toString(plan.getStartLongitude()));
        endLatitude_et.setText(Double.toString(plan.getEndLatitude()));
        endLongitude_et.setText(Double.toString(plan.getEndLongitude()));
        startTime_button.setText(plan.getStartTime());
        endTime_button.setText(plan.getEndTime());
        startDate_button.setText(plan.getStartDate());
        endDate_button.setText(plan.getEndDate());

        View start_pos_button = root.findViewById(R.id.start_location_selector_button);
        View end_pos_button = root.findViewById(R.id.end_location_selector_button);
        start_pos_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // set start,end pos for use by the maplocationselectoractivity
                MapLocationSelectorActivity.MapLocations locations = MapLocationSelectorActivity.MapLocations.getInstance();
                LatLng start = new LatLng(Double.parseDouble(startLatitude_et.getText().toString()), Double.parseDouble(startLongitude_et.getText().toString()));
                locations.setStart(start);
                LatLng end = new LatLng(Double.parseDouble(endLatitude_et.getText().toString()), Double.parseDouble(endLongitude_et.getText().toString()));
                locations.setEnd(end);

                Intent i = new Intent(getContext(), MapLocationSelectorActivity.class);
                i.putExtra(MapLocationSelectorActivity.MODE, MapLocationSelectorActivity.START);
                startActivityForResult(i, MapLocationSelectorActivity.RESULT_START);
            }
        });
        end_pos_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // set start,end pos for use by the maplocationselectoractivity
                MapLocationSelectorActivity.MapLocations locations = MapLocationSelectorActivity.MapLocations.getInstance();
                LatLng start = new LatLng(Double.parseDouble(startLatitude_et.getText().toString()), Double.parseDouble(startLongitude_et.getText().toString()));
                locations.setStart(start);
                LatLng end = new LatLng(Double.parseDouble(endLatitude_et.getText().toString()), Double.parseDouble(endLongitude_et.getText().toString()));
                locations.setEnd(end);

                Intent i = new Intent(getContext(), MapLocationSelectorActivity.class);
                i.putExtra(MapLocationSelectorActivity.MODE, MapLocationSelectorActivity.END);
                startActivityForResult(i, MapLocationSelectorActivity.RESULT_END);
            }
        });

        startTime_button.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               TimePickerFragment timePickerFragment = new TimePickerFragment();
               timePickerFragment.setListenerFor(EditPlansBottomSheetDialogFragment.this, true);
               timePickerFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
           }
        });

        endTime_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setListenerFor(EditPlansBottomSheetDialogFragment.this, false);
                timePickerFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        startDate_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setListenerFor(EditPlansBottomSheetDialogFragment.this, true);
                datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        endDate_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setListenerFor(EditPlansBottomSheetDialogFragment.this, false);
                datePickerFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        Button save_button = root.findViewById(R.id.plan_save_button);
        Button delete_button = root.findViewById(R.id.plan_delete_button);
        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked SAVE plan");
                plan.setName(name_et.getText().toString());
                plan.setStartLatitude(Double.parseDouble(startLatitude_et.getText().toString()));
                plan.setStartLongitude(Double.parseDouble(startLongitude_et.getText().toString()));
                plan.setEndLatitude(Double.parseDouble(endLatitude_et.getText().toString()));
                plan.setEndLongitude(Double.parseDouble(endLongitude_et.getText().toString()));
                plan.setStartTime(startTime_button.getText().toString());
                plan.setEndTime(endTime_button.getText().toString());
                plan.setStartDate(startDate_button.getText().toString());
                plan.setEndDate(endDate_button.getText().toString());
                plansViewModel.updateHikingPlan(plan);
                dismiss();
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked DELETE plan");
                plansViewModel.deleteHikingPlan(plan);
                dismiss();
            }
        });
        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==MapLocationSelectorActivity.RESULT_START){
            Log.i(TAG, "GOT START POS RESULT!");
            startLatitude_et.setText(Double.toString(data.getDoubleExtra(MapLocationSelectorActivity.START_LATITUDE, 0.0)));
            startLongitude_et.setText(Double.toString(data.getDoubleExtra(MapLocationSelectorActivity.START_LONGITUDE, 0.0)));
        }else if(resultCode==MapLocationSelectorActivity.RESULT_END){
            Log.i(TAG, "GOT END POS RESULT!");
            endLatitude_et.setText(Double.toString(data.getDoubleExtra(MapLocationSelectorActivity.END_LATITUDE, 0.0)));
            endLongitude_et.setText(Double.toString(data.getDoubleExtra(MapLocationSelectorActivity.END_LONGITUDE, 0.0)));
        }
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

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private EditPlansBottomSheetDialogFragment listener;
        private boolean startTime;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void setListenerFor(EditPlansBottomSheetDialogFragment listener, boolean startTime){
            this.listener = listener;
            this.startTime = startTime;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            Log.i(EditPlansBottomSheetDialogFragment.TAG, "timepicker got a time: " + hourOfDay + ":" + minute);
            String time = hourOfDay + ":";
            if(minute < 10){
                time += "0" + minute;
            }else{
                time += minute;
            }
            if(listener != null){
                if(startTime){
                    listener.startTime_button.setText(time);
                }else{
                    listener.endTime_button.setText(time);
                }
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private EditPlansBottomSheetDialogFragment listener;
        private boolean startDate;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setListenerFor(EditPlansBottomSheetDialogFragment listener, boolean startDate){
            this.listener = listener;
            this.startDate = startDate;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            Log.i(EditPlansBottomSheetDialogFragment.TAG, "my datepicker got a date: " + year + "/" + month + "/" + day);
            String date = year + "/" + month + "/" + day;
            if(listener != null){
                if(startDate){
                    listener.startDate_button.setText(date);
                }else{
                    listener.endDate_button.setText(date);
                }
            }
        }
    }
}