package com.example.hikingapp.data;

import java.util.List;

public class User {
    private List<EmergencyContact> emergencyContacts;
    public User() {

    }

    public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }
}
