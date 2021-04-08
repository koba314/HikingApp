package com.example.hikingapp.ui.map;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hikingapp.R;
import com.example.hikingapp.model.MapPin;
import com.example.hikingapp.ui.plans.PlansViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * ViewPinBottomSheetDialogFragment
 * A bottom sheet with details about an individual map pin
 */
public class ViewPinBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "ViewPinBSDFragment";

    private MapViewModel mapViewModel;
    private MapPin pin;

    private TextView name_tv;
    private TextView user_tv;
    private TextView desc_tv;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;
    private EditText name_et;
    private EditText desc_et;
    private CheckBox isPublic_cb;

    private boolean editMode = false;

    public ViewPinBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        mapViewModel =
                new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        pin = mapViewModel.getPinToView().getValue();
        Log.i(TAG, "got pin with id: " + pin.getId() + ", uid: "+pin.getUid() + ", cuid: "+pin.getCreatedByUid());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_pin_bottom_sheet, container, false);
        name_tv = root.findViewById(R.id.pin_name_tv);
        user_tv = root.findViewById(R.id.pin_username_tv);
        desc_tv = root.findViewById(R.id.pin_desc_tv);
        name_et = root.findViewById(R.id.pin_name_et);
        desc_et = root.findViewById(R.id.pin_desc_et);
        editButton = root.findViewById(R.id.pin_edit_button);
        saveButton = root.findViewById(R.id.pin_save_button);
        deleteButton = root.findViewById(R.id.pin_delete_button);
        isPublic_cb = root.findViewById(R.id.pin_public_checkbox);

        updateView(editMode);

        name_tv.setText(pin.getName());
        user_tv.setText(pin.getUsername());
        desc_tv.setText(pin.getInfo());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(pin.getCreatedByUid().equals(user.getUid())){
            editButton.setVisibility(View.VISIBLE);
        }else{
            editButton.setVisibility(View.GONE);
        }

        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateView(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pin.setName(name_et.getText().toString());
                pin.setInfo(desc_et.getText().toString());
                pin.setIsPublic(isPublic_cb.isChecked());
                mapViewModel.updateMapPin(pin);
                updateView(false);
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapViewModel.deleteMapPin(pin);
                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                dismiss();
            }
        });

        mapViewModel.getPinToView().observe(getViewLifecycleOwner(), new Observer<MapPin>() {
            @Override
            public void onChanged(MapPin pin) {
                name_tv.setText(pin.getName());
                desc_tv.setText(pin.getInfo());
            }
        });


        return root;
    }

    private void updateView(boolean editMode){
        this.editMode = editMode;
        if(editMode){
            editButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            name_tv.setVisibility(View.GONE);
            name_et.setVisibility(View.VISIBLE);
            desc_tv.setVisibility(View.GONE);
            desc_et.setVisibility(View.VISIBLE);
            isPublic_cb.setVisibility(View.VISIBLE);

            isPublic_cb.setChecked(pin.getIsPublic());
            name_et.setText(name_tv.getText());
            desc_et.setText(desc_tv.getText());
        }else{
            editButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
            name_tv.setVisibility(View.VISIBLE);
            name_et.setVisibility(View.GONE);
            desc_tv.setVisibility(View.VISIBLE);
            desc_et.setVisibility(View.GONE);
            isPublic_cb.setVisibility(View.GONE);

            name_tv.setText(name_et.getText());
            desc_tv.setText(desc_et.getText());
        }
    }

    public void setEditMode(boolean editMode){
        this.editMode = editMode;
    }

    @Override
    public void onStart(){
        super.onStart();
        // make sure the background map isn't dimmed
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}