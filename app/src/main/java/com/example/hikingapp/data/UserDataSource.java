package com.example.hikingapp.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class UserDataSource {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private static UserDataSource instance;
    private UserListener listener;

    // private constructor
    private UserDataSource(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    // singleton access
    public static UserDataSource getInstance(){
        if (instance == null) {
            instance = new UserDataSource();
        }
        return instance;
    }

    public void userData() {
        FirebaseUser fireBaseUser = auth.getCurrentUser();
        if(fireBaseUser != null) {
            String uid = fireBaseUser.getUid();
            Log.i("UserDataSource", uid);
            DatabaseReference ref = database.getReference("user/" + uid);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User();
                    List<EmergencyContact> emergencyContact = new LinkedList<EmergencyContact>();
                    for(DataSnapshot shot: snapshot.child("emergency_contact").getChildren()) {
                        EmergencyContact e =
                                new EmergencyContact(shot.child("name").getValue(String.class),
                                        shot.child("phone_number").getValue(Integer.class),
                                        shot.child("message").getValue(String.class));
                        emergencyContact.add(e);
                    }
                    user.setEmergencyContacts(emergencyContact);
                    listener.onUserChange(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            ref.addValueEventListener(eventListener);
        }
    }

    public void registerListener(UserListener listener){
        this.listener = listener;
    }
}
