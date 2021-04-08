package com.example.hikingapp.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.example.hikingapp.R;
import com.example.hikingapp.model.EmergencyContact;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Dialog for creating a new plan.
 */
public class EditContactBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "CreateContactBSDFrag";

    private SettingsViewModel settingsViewModel;

    private EmergencyContact contact;

    EditText contactNameET;
    EditText contactPhoneNumberET;
    EditText contactMessageET;

    public EditContactBottomSheetDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        contact = settingsViewModel.getContactToEdit().getValue();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_contact_bottom_sheet_dialog, container, false);
        contactNameET = root.findViewById(R.id.contact_name_et);
        contactPhoneNumberET = root.findViewById(R.id.contact_phone_number_et);
        contactMessageET = root.findViewById(R.id.contact_message_et);

        contactNameET.setText(contact.getName());
        contactPhoneNumberET.setText(contact.getPhoneNumber());
        contactMessageET.setText(contact.getMessage());

        Button save_button = root.findViewById(R.id.contact_save_button);
        Button cancel_button = root.findViewById(R.id.contact_delete_button);
        cancel_button.setText(getString(R.string.action_delete));

        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked SAVE contact");
                contact.setName(contactNameET.getText().toString());
                contact.setPhoneNumber(contactPhoneNumberET.getText().toString());
                contact.setMessage(contactMessageET.getText().toString());
                settingsViewModel.updateEmergencyContact(contact);
                dismiss();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "clicked DELETE contact");
                settingsViewModel.deleteEmergencyContact(contact);
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