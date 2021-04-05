package com.example.hikingapp.model;

import com.example.hikingapp.model.EmergencyContact;

import java.util.List;

public class User {
    private List<EmergencyContact> emergencyContacts;
    public User() {

    }

    public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) { this.emergencyContacts = emergencyContacts; }

    public List<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }
}
