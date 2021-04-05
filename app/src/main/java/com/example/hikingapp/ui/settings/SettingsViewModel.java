package com.example.hikingapp.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hikingapp.model.EmergencyContact;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.model.User;
import com.example.hikingapp.data.UserDataSource;
import com.example.hikingapp.data.UserListener;
import com.example.hikingapp.data.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewModel extends ViewModel implements UserListener {

    private MutableLiveData<String> emergencyContactTitleText;
    private MutableLiveData<String> emergencyContactNameText;
    private MutableLiveData<String> emergencyContactPhoneNumberText;
    private MutableLiveData<String> emergencyContactMessageText;
    private MutableLiveData<String> emergencyContactButtonText;
    private UserRepository userRepository;
    private MutableLiveData<List<EmergencyContact>> mEmergengyContact;
    private MutableLiveData<EmergencyContact> contactToEdit;

    public SettingsViewModel() {
        userRepository = UserRepository.getInstance(UserDataSource.getInstance());
        mEmergengyContact = new MutableLiveData<List<EmergencyContact>>();
        mEmergengyContact.setValue(new ArrayList<EmergencyContact>());
        contactToEdit = new MutableLiveData<EmergencyContact>();
        contactToEdit.setValue(new EmergencyContact());
        emergencyContactTitleText = new MutableLiveData<>();
        emergencyContactTitleText.setValue("Emergency Contact");
        emergencyContactButtonText = new MutableLiveData<>();
        emergencyContactButtonText.setValue("View All");
        emergencyContactNameText = new MutableLiveData<>();
        emergencyContactNameText.setValue("");
        emergencyContactPhoneNumberText = new MutableLiveData<>();
        emergencyContactPhoneNumberText.setValue("");
        emergencyContactMessageText = new MutableLiveData<>();
        emergencyContactMessageText.setValue("");
        userRepository.registerListener(this);
        userRepository.userData();
    }

    public void setContactToEdit(EmergencyContact contact) { contactToEdit.setValue(contact); }
    public LiveData<EmergencyContact> getContactToEdit() { return contactToEdit;}
    public LiveData<List<EmergencyContact>> getEmergencyContact() { return mEmergengyContact; }
    public LiveData<String> getTitleText() {
        return emergencyContactTitleText;
    }
    public LiveData<String> getNameText() {
        return emergencyContactNameText;
    }
    public LiveData<String> getPhoneNumberText() {
        return emergencyContactPhoneNumberText;
    }
    public LiveData<String> getButtonText() { return emergencyContactButtonText; }

    public void onUserChange(User user) {
        List<EmergencyContact> emergencyContact = user.getEmergencyContacts();
        if(emergencyContact.size() > 0) {
            emergencyContactNameText.setValue(emergencyContact.get(0).getName());
            emergencyContactPhoneNumberText.setValue(String.valueOf(emergencyContact.get(0).getPhoneNumber()));
            emergencyContactMessageText.setValue(emergencyContact.get(0).getMessage());
        }
        mEmergengyContact.setValue(emergencyContact);
    }


    public void createEmergencyContact(EmergencyContact contact) { userRepository.createEmergencyContact(contact); }
    public void updateEmergencyContact(EmergencyContact contact) { userRepository.updateEmergencyContact(contact); }
    public void deleteEmergencyContact(EmergencyContact contact) { userRepository.deleteEmergencyContact(contact); }
}