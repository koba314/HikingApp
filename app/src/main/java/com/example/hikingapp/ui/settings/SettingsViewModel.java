package com.example.hikingapp.ui.settings;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hikingapp.data.EmergencyContact;
import com.example.hikingapp.data.User;
import com.example.hikingapp.data.UserDataSource;
import com.example.hikingapp.data.UserListener;
import com.example.hikingapp.data.UserRepository;

import java.util.List;

public class SettingsViewModel extends ViewModel implements UserListener {

    private MutableLiveData<String> emergencyContactTitleText;
    private MutableLiveData<String> emergencyContactNameText;
    private MutableLiveData<String> emergencyContactPhoneNumberText;
    private MutableLiveData<String> emergencyContactMessageText;
    private MutableLiveData<String> emergencyContactButtonText;
    private UserRepository userRepository;

    public SettingsViewModel() {
        userRepository = UserRepository.getInstance(UserDataSource.getInstance());
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

    public void getEmergencyContact(String user_id) {

    }

    public LiveData<String> getTitleText() {
        return emergencyContactTitleText;
    }
    public LiveData<String> getNameText() {
        return emergencyContactNameText;
    }
    public LiveData<String> getPhoneNumberText() {
        return emergencyContactPhoneNumberText;
    }
    public LiveData<String> getMessageText() {
        return emergencyContactMessageText;
    }
    public LiveData<String> getButtonText() {
        return emergencyContactButtonText;
    }

    public void onUserChange(User user) {
        List<EmergencyContact> emergencyContact = user.getEmergencyContacts();
        if(emergencyContact.size() > 0) {
            emergencyContactNameText.setValue(emergencyContact.get(0).getName());
            emergencyContactPhoneNumberText.setValue(String.valueOf(emergencyContact.get(0).getPhoneNumber()));
            emergencyContactMessageText.setValue(emergencyContact.get(0).getMessage());
        }
    }

}