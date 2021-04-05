package com.example.hikingapp.model;

public class EmergencyContact {
    private String name;
    private int phoneNumber;
    private String message;
    private String id;

    public EmergencyContact(String id, String name, int phoneNumber, String message) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public EmergencyContact() {

    }

    public void setId(String id) { this.id = id; }
    public void setMessage(String message) { this.message = message; }
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getId() { return id; }
    public String getName(){ return this.name; }
    public int getPhoneNumber(){ return this.phoneNumber; }
    public String getMessage(){ return this.message; }
}
