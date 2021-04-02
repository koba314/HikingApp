package com.example.hikingapp.ui.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.hikingapp.R;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.lang.ref.WeakReference;

public class MapLocationSelectorActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String TAG = "MapLocSelectorActivity";
    public static final int PERMISSION_FINE_LOCATION = 0;
    public static final String START_LATITUDE = "MapLocationSelectorActivity.START_LATITUDE";
    public static final String START_LONGITUDE = "MapLocationSelectorActivity.START_LONGITUDE";
    public static final String END_LATITUDE = "MapLocationSelectorActivity.END_LATITUDE";
    public static final String END_LONGITUDE = "MapLocationSelectorActivity.END_LONGITUDE";
    public static final String MODE = "MapLocationSelectorActivity.MODE";
    public static final String START = "MapLocationSelectorActivity.START";
    public static final String END = "MapLocationSelectorActivity.END";

    private MapView mapView;
    private MapboxMap mapboxMap;

    private boolean hasLocationPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_selector);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Location permission already granted");
            hasLocationPermission = true;
            initLocationEngine();
            setUpMap(true);
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }



    }

    private void initLocationEngine(){

    }

    private void setUpMap(boolean hasLocation){
        if(hasLocation){
            mapView.getMapAsync(new OnMapReadyCallback(){
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap){
                    MapLocationSelectorActivity.this.mapboxMap = mapboxMap;
                    mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded(){
                        @Override
                        public void onStyleLoaded(@NonNull Style style){
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .zoom(12)
                                    .build();
                            mapboxMap.setCameraPosition(cameraPosition);
                            setUpLocationComponent(style);
                        }
                    });
                }
            });
        }else{
            mapView.getMapAsync(new OnMapReadyCallback(){
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap){
                    MapLocationSelectorActivity.this.mapboxMap = mapboxMap;
                    mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded(){
                        @Override
                        public void onStyleLoaded(@NonNull Style style){

                        }
                    });
                }
            });
        }
    }
    @SuppressLint("MissingPermission")
    private void setUpLocationComponent(Style style){
        LocationComponentOptions options = LocationComponentOptions.builder(this)
                .pulseEnabled(true)
                .build();
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, style)
                        .locationComponentOptions(options)
                        .build());
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
        locationComponent.setRenderMode(RenderMode.GPS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_FINE_LOCATION && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Location permission granted");
                hasLocationPermission = true;
                initLocationEngine();
                setUpMap(true);
            } else {
                Log.i(TAG, "Location permission denied.");
                setUpMap(false);
            }
        }
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStart(){
        Log.i(TAG, "onStart()");
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause(){
        Log.i(TAG, "onPause()");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume(){
        Log.i(TAG, "onResume()");
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop(){
        Log.i(TAG, "onStop()");
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private static class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {
        public static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
        public static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

        private final WeakReference<MapLocationSelectorActivity> activityWeakRef;

        LocationListeningCallback(MapLocationSelectorActivity activity){
            this.activityWeakRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result){
            Location lastLocation = result.getLastLocation();
            if(activityWeakRef.get() != null) {
                if(lastLocation != null){
                    LatLng pos = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception){
            Log.w(TAG, "Failed getting location");
        }
    }
}