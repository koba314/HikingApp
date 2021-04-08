package com.example.hikingapp.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hikingapp.R;
import com.example.hikingapp.model.EmergencyContact;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.ui.plans.CreatePlansBottomSheetDialogFragment;
import com.example.hikingapp.ui.plans.EditPlansBottomSheetDialogFragment;
import com.example.hikingapp.ui.plans.PlansFragment;
import com.example.hikingapp.ui.plans.ViewPlansBottomSheetDialogFragment;

import java.util.List;

public class EmergencyContactFragment extends Fragment {

    public static final String TAG = "EmergencyContactFrag";
    public static final String EMERGENCY_CONTACT_FRAGMENT = "com.hikingapp.EMERGENCY_CONTACT_FRAGMENT";

    private SettingsViewModel settingsViewModel;
    private RecyclerView mContactRecyclerView;
    private EmergencyContactFragment.ContactAdapter mPlanAdapter;
    private List<EmergencyContact> mEmergencyContactList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        settingsViewModel = new ViewModelProvider(requireActivity()).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_emergency_contact, container, false);

        Button create_contact_button = root.findViewById(R.id.create_emergency_contact_button);
        Activity activity = getActivity();
        mContactRecyclerView = root.findViewById(R.id.emergency_contact_recycler_view);
        if(activity != null){
            mContactRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            mPlanAdapter = new EmergencyContactFragment
                    .ContactAdapter(settingsViewModel.getEmergencyContact().getValue());
            mContactRecyclerView.setAdapter(mPlanAdapter);
            mContactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        create_contact_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                settingsViewModel.setContactToEdit(new EmergencyContact());
                CreateContactBottomSheetDialogFragment dialog = new CreateContactBottomSheetDialogFragment();
                dialog.show(getActivity().getSupportFragmentManager(), CreateContactBottomSheetDialogFragment.TAG);
            }
        });

        settingsViewModel.getEmergencyContact().observe(getViewLifecycleOwner(), new Observer<List<EmergencyContact>>() {
            @Override
            public void onChanged(@Nullable List<EmergencyContact> contacts){
                if(contacts != null){
                    Log.i(TAG, "contacts changed");
                    mContactRecyclerView.setAdapter(new EmergencyContactFragment.ContactAdapter(contacts));
                }
            }
        });

        return root;
    }

    private class ContactHolder extends RecyclerView.ViewHolder {
        private EmergencyContact contact;
        private Button editContactButton;
        private Button viewContactButton;
        private String name;
        private String phoneNumber;
        private TextView nameView;
        private TextView phoneNumberView;

        public ContactHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_emergency_contact, parent, false));
            viewContactButton = itemView.findViewById(R.id.view_contact_button);
            editContactButton = itemView.findViewById(R.id.edit_contact_button);

            editContactButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.i(TAG, "clicked EDIT contact");
                    mContactRecyclerView.scrollToPosition(getAdapterPosition());
                    settingsViewModel.setContactToEdit(contact);
                    EditContactBottomSheetDialogFragment dialog = new EditContactBottomSheetDialogFragment();
                    dialog.show(getParentFragmentManager(), EditContactBottomSheetDialogFragment.TAG);
                }
            });
            viewContactButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Log.i(TAG, "clicked VIEW contact");
                    mContactRecyclerView.scrollToPosition(getAdapterPosition());
                    settingsViewModel.setContactToEdit(contact);
                    ViewContactBottomSheetDialogFragment dialog = new ViewContactBottomSheetDialogFragment();
                    dialog.show(getParentFragmentManager(), ViewContactBottomSheetDialogFragment.TAG);
                }
            });

            nameView = itemView.findViewById(R.id.text_emergency_contact_name);
            phoneNumberView = itemView.findViewById(R.id.text_emergency_contact_phone_number);
        }

        public void bind(EmergencyContact contact){
            this.contact = contact;
            this.name = contact.getName();
            this.phoneNumber = contact.getPhoneNumber();
            nameView.setText(this.name);
            phoneNumberView.setText(phoneNumber);
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<EmergencyContactFragment.ContactHolder> {
        private List<EmergencyContact> contactList;

        public ContactAdapter(List<EmergencyContact> contactList){
            this.contactList = contactList;
        }

        @Override
        public EmergencyContactFragment.ContactHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new EmergencyContactFragment.ContactHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(EmergencyContactFragment.ContactHolder holder, int position){
            EmergencyContact contact = EmergencyContactFragment.this.settingsViewModel.getEmergencyContact().getValue().get(position);
            holder.bind(contact);
        }

        @Override
        public int getItemCount(){
            return contactList.size();
        }
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