package com.example.hikingapp.data;

import com.example.hikingapp.model.EmergencyContact;
import com.example.hikingapp.model.User;

import java.util.LinkedList;
import java.util.List;

public class UserRepository implements UserListener {
    private static volatile UserRepository instance;
    private static List<UserListener> listener;
    private UserDataSource dataSource;

    private UserRepository(UserDataSource dataSource) {
        this.dataSource = dataSource;
        this.listener = new LinkedList<UserListener>();
    }

    public static UserRepository getInstance(UserDataSource dataSource) {
        if (instance == null) {
            instance = new UserRepository(dataSource);
        }
        return instance;
    }

    public void userData() {
        dataSource.registerListener(this);
        dataSource.userData();
    }

    public void onUserChange(User user){
        for(UserListener l: this.listener) {
            l.onUserChange(user);
        }
    }



    public void registerListener(UserListener listener){
        this.listener.add(listener);
    }

    public void createEmergencyContact(EmergencyContact contact) { dataSource.createEmergencyContact(contact);}
    public void updateEmergencyContact(EmergencyContact contact) { dataSource.updateEmergencyContact(contact);}
    public void deleteEmergencyContact(EmergencyContact contact) { dataSource.deleteEmergencyContact(contact);}
}
