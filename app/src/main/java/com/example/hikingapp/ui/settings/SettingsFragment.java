package com.example.hikingapp.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.hikingapp.R;
import com.example.hikingapp.model.EmergencyContact;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.ui.plans.CreatePlansBottomSheetDialogFragment;

public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";
    public static final String SETTINGS_FRAGMENT = "com.hikingapp.SETTINGS_FRAGMENT";

    private SettingsViewModel settingsViewModel;
    private EmergencyContactFragment emergencyContactFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView emergencyContactTitle = root.findViewById(R.id.text_emergency_contact_title);
        final TextView emergencyContactName = root.findViewById(R.id.text_emergency_contact_name);
        final TextView emergencyContactPhoneNumber = root.findViewById(R.id.text_emergency_contact_phone_number);
        final Button emergencyContactButton = root.findViewById(R.id.button_emergency_contact_view);
        settingsViewModel.getTitleText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emergencyContactTitle.setText(s);
            }
        });
        settingsViewModel.getNameText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emergencyContactName.setText(s);
            }
        });
        settingsViewModel.getPhoneNumberText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emergencyContactPhoneNumber.setText(s);
            }
        });
        settingsViewModel.getButtonText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emergencyContactButton.setText(s);
            }
        });
        emergencyContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, emergencyContactFragment, TAG);
                transaction.commit();
            }
        });
        emergencyContactFragment = new EmergencyContactFragment();
        return root;
    }

    //
    //
    // METHOD OVERRIDES FOR LOGGING
    //
    //

    @Override
    public void onDestroyView(){
        Log.i(TAG, "onDestroyView()");
        super.onDestroyView();
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