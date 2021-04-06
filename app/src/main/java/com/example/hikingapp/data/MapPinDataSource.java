package com.example.hikingapp.data;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.model.MapPin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapPinDataSource {
    public static final String TAG = "MapPinDataSource";

    private static MapPinDataSource instance;

    private static ValueEventListener dbListener;
    private static ValueEventListener userDbListener;
    private FirebaseAuth auth;
    private FirebaseDatabase db;

    public interface MapPinListener{
        void onGetOtherMapPins(List<MapPin> pins);
        void onGetUserMapPins(List<MapPin> pins);
    }

    private MapPinDataSource(){
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    public static MapPinDataSource getInstance(){
        if(instance == null){
            instance = new MapPinDataSource();
        }
        return instance;
    }

    public void initMapPinListener(MapPinListener listener) {
        FirebaseUser user = auth.getCurrentUser();
        dbListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "data=" + dataSnapshot.toString());
                List<MapPin> pins = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    MapPin pin = data.getValue(MapPin.class);
                    // only get other users' pins
                    if(!pin.getCreatedByUid().equals(user.getUid())){
                        pins.add(pin);
                    }
                }
                listener.onGetOtherMapPins(pins);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "db operation failed");
            }
        };
        db.getReference().child("mappin").addValueEventListener(dbListener);
        userDbListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "data=" + dataSnapshot.toString());
                List<MapPin> pins = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    MapPin pin = data.getValue(MapPin.class);
                    pins.add(pin);
                }
                listener.onGetUserMapPins(pins);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "db operation failed");
            }
        };
        db.getReference().child("user").child(user.getUid()).child("mappin").addValueEventListener(userDbListener);
    }

    public void createMapPin(MapPin pin){
        FirebaseUser user = auth.getCurrentUser();
        if(pin.getIsPublic()){
            DatabaseReference newPin = db.getReference().child("mappin").push();
            pin.setId(newPin.getKey());
            DatabaseReference newUserPin = db.getReference().child("user").child(user.getUid()).child("mappin").push();
            pin.setUid(newUserPin.getKey());
            pin.setCreatedByUid(user.getUid());
            pin.setUsername(user.getDisplayName());
            newPin.setValue(pin);
            newUserPin.setValue(pin);
        }else{
            DatabaseReference newUserPin = db.getReference().child("user").child(user.getUid()).child("mappin").push();
            pin.setId(null);
            pin.setUid(newUserPin.getKey());
            pin.setCreatedByUid(user.getUid());
            pin.setUsername(user.getDisplayName());
            newUserPin.setValue(pin);
        }
    }

    public void updateMapPin(MapPin pin){
        FirebaseUser user = auth.getCurrentUser();
        if(pin.getCreatedByUid().equals(user.getUid())){
            DatabaseReference userDbPin = db.getReference().child("user").child(user.getUid()).child("mappin").child(pin.getUid());
            userDbPin.setValue(pin);
            if(pin.getIsPublic() && pin.getId() != null){
                // update a public pin
                DatabaseReference dbPin = db.getReference().child("mappin").child(pin.getId());
                dbPin.setValue(pin);
            }else if(pin.getIsPublic() && pin.getId() == null){
                // publicize a pin
                DatabaseReference dbPin = db.getReference().child("mappin").push();
                pin.setId(dbPin.getKey());
                dbPin.setValue(pin);
            }else if(!pin.getIsPublic() && pin.getId() != null){
                // un-publicize a pin
                try{
                    DatabaseReference dbPin = db.getReference().child("mappin").child(pin.getId());
                    dbPin.removeValue();
                }catch (Exception e){
                    Log.w(TAG, "tried to remove nonexistent pin from DB");
                }
            }
        }
    }

    public void deleteMapPin(MapPin pin){
        FirebaseUser user = auth.getCurrentUser();
        if(pin.getCreatedByUid().equals(user.getUid())){
            DatabaseReference userDbPin = db.getReference().child("user").child(user.getUid()).child("mappin").child(pin.getUid());
            userDbPin.removeValue();
            if(pin.getIsPublic() && pin.getId() != null){
                DatabaseReference dbPin = db.getReference().child("mappin").child(pin.getId());
                dbPin.removeValue();
            }
        }
    }

}
