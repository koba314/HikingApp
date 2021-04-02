package com.example.hikingapp.ui.map;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hikingapp.model.MapPin;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.lang.ref.WeakReference;
import java.util.List;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<MapPin>> mPins;
    private MutableLiveData<LatLng> mPos;

    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("TODO: map fragment");
        mPins = new MutableLiveData<>();
        mPos = new MutableLiveData<>();
    }

    public LiveData<List<MapPin>> getPins() { return mPins; }

    public LiveData<LatLng> getPos() { return mPos; }
    public void setPos(LatLng pos) { mPos.setValue(pos); }

    public LiveData<String> getText() {
        return mText;
    }

}