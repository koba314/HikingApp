package com.example.hikingapp.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.hikingapp.R;
import com.example.hikingapp.SplashScreenActivity;
import com.example.hikingapp.data.LoginDataSource;
import com.example.hikingapp.model.EmergencyContact;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.ui.plans.CreatePlansBottomSheetDialogFragment;

public class SettingsFragment extends Fragment implements LoginDataSource.LoginDataSourceListener {

    public static final String TAG = "SettingsFragment";
    public static final String SETTINGS_FRAGMENT = "com.hikingapp.SETTINGS_FRAGMENT";

    private SettingsViewModel settingsViewModel;
    private EmergencyContactFragment emergencyContactFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        settingsViewModel =
                new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView emergencyContactTitle = root.findViewById(R.id.text_emergency_contact_title);
        final TextView emergencyContactName = root.findViewById(R.id.text_emergency_contact_name);
        final TextView emergencyContactPhoneNumber = root.findViewById(R.id.text_emergency_contact_phone_number);
        final Button emergencyContactButton = root.findViewById(R.id.button_emergency_contact_view);
        final Button editUsernameButton = root.findViewById(R.id.edit_username_button);
        final Button logoutButton = root.findViewById(R.id.logout_button);
        final Button deleteAccountButton = root.findViewById(R.id.delete_account_button);
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
        editUsernameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                
            }
        });
        deleteAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name)
                        .setMessage(getString(R.string.are_you_sure))
                        .setIcon(R.drawable.ic_launcher_foreground)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "ACCOUNT DELETED");
                                dialog.dismiss();
                                settingsViewModel.deleteAccount(SettingsFragment.this);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked log out");
                settingsViewModel.logout();
                Intent i = new Intent(getContext(), SplashScreenActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void onDelete(boolean success){
        if(success){
            Toast.makeText(getContext(), "Account deleted!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "Deletion failed; signed out!", Toast.LENGTH_LONG).show();
        }
        Intent i = new Intent(getContext(), SplashScreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onUpdateUsername(boolean success){

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