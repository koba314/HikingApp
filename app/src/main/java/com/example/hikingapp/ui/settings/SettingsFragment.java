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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.hikingapp.R;

public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";
    public static final String SETTINGS_FRAGMENT = "com.hikingapp.SETTINGS_FRAGMENT";

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i("SettingsFragment", "onCreateView()");
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView emergencyContactTitle = root.findViewById(R.id.text_emergency_contact_title);
        final TextView emergencyContactName = root.findViewById(R.id.text_emergency_contact_name);
        final TextView emergencyContactPhoneNumber = root.findViewById(R.id.text_emergency_contact_phone_number);
        final TextView emergencyContactMessage = root.findViewById(R.id.text_emergency_contact_message);
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
        settingsViewModel.getMessageText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emergencyContactMessage.setText(s);
            }
        });
        settingsViewModel.getButtonText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                emergencyContactButton.setText(s);
            }
        });
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