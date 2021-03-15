package com.example.hikingapp.data;

public class EmergencyContact {
    private String name;
    private int phoneNumber;
    private String message;

    public EmergencyContact(String name, int phoneNumber, String message) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public String getName(){ return this.name; }
    public int getPhoneNumber(){ return this.phoneNumber; }
    public String getMessage(){ return this.message; }
}
