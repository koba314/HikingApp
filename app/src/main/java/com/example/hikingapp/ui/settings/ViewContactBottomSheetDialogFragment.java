package com.example.hikingapp.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.hikingapp.R;
import com.example.hikingapp.model.EmergencyContact;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Dialog for creating a new plan.
 */
public class ViewContactBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "CreateContactBSDFrag";

    private SettingsViewModel settingsViewModel;

    private EmergencyContact contact;

    TextView contactNameText;
    TextView contactPhoneNumberText;
    TextView contactMessageText;

    public ViewContactBottomSheetDialogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        contact = settingsViewModel.getContactToEdit().getValue();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_contact_bottom_sheet_dialog, container, false);
        contactNameText = root.findViewById(R.id.contact_name_text);
        contactPhoneNumberText = root.findViewById(R.id.contact_phone_number_text);
        contactMessageText = root.findViewById(R.id.contact_message_text);

        contactNameText.setText(contact.getName());
        contactPhoneNumberText.setText(Integer.toString(contact.getPhoneNumber()));
        contactMessageText.setText(contact.getMessage());

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