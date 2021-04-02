package com.example.hikingapp.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hikingapp.MainActivity;
import com.example.hikingapp.R;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
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
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.lang.ref.WeakReference;
import java.util.List;

public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";
    public static final String MAP_FRAGMENT = "com.hikingapp.MAP_FRAGMENT";
    public static final int PERMISSION_FINE_LOCATION = 0;

    private MapViewModel mapViewModel;
    private MapView mapView;
    private LocationEngine locationEngine;
    private MapboxMap mapboxMap;

    private LocationListeningCallback locationCallback = new LocationListeningCallback(this);

    private boolean hasLocationPermission = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Location permission already granted");
            hasLocationPermission = true;
            initLocationEngine();
            setUpMap(true);
            setLocationObserver();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == PERMISSION_FINE_LOCATION && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "Location permission granted");
                hasLocationPermission = true;
                initLocationEngine();
                setUpMap(true);
                setLocationObserver();
            }else{
                Log.i(TAG, "Location permission denied.");
                setUpMap(false);
            }
        }
    }

    private void initLocationEngine(){
        locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());
        LocationEngineRequest request = new LocationEngineRequest.Builder(LocationListeningCallback.DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
                .setMaxWaitTime(LocationListeningCallback.DEFAULT_MAX_WAIT_TIME)
                .build();
        try {
            locationEngine.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
            locationEngine.getLastLocation(locationCallback);
        }catch(SecurityException e) {
            Log.w(TAG, "Attempted requesting location without having location permissions!");
        }
    }

    private void setLocationObserver(){
        mapViewModel.getPos().observe(getViewLifecycleOwner(), new Observer<LatLng>(){
            @Override
            public void onChanged(@Nullable LatLng pos){
                Log.i(TAG, "Location data updated");
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void setUpMap(boolean hasLocation) {
        if(hasLocation){
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mapViewModel.getPos().getValue())
                    .zoom(12)
                    .build();
            mapView.getMapAsync(new OnMapReadyCallback(){
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap){
                    MapFragment.this.mapboxMap = mapboxMap;
                    mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded(){
                        @Override
                        public void onStyleLoaded(@NonNull Style style){
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
                    MapFragment.this.mapboxMap = mapboxMap;
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
        LocationComponentOptions options = LocationComponentOptions.builder(getContext())
                .pulseEnabled(true)
                .build();
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(getContext(), style)
                        .locationComponentOptions(options)
                        .build());
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
        locationComponent.setRenderMode(RenderMode.GPS);
        locationComponent.setLocationEngine(locationEngine);
    }

    //
    //
    // METHOD OVERRIDES FOR LOGGING
    //
    //

    @Override
    public void onDestroyView(){
        Log.i(TAG, "onDestroyView()");
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onAttach(Context context){
        Log.i(TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        Log.i(TAG, "onAttach()");
        super.onDetach();
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
        if(locationEngine != null){
            locationEngine.removeLocationUpdates(locationCallback);
        }
        mapView.onStop();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
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

        private final WeakReference<MapFragment> fragmentWeakRef;

        LocationListeningCallback(MapFragment fragment){
            this.fragmentWeakRef = new WeakReference<>(fragment);
        }

        @Override
        public void onSuccess(LocationEngineResult result){
            Location lastLocation = result.getLastLocation();
            if(fragmentWeakRef.get() != null) {
                if(lastLocation != null){
                    LatLng pos = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    fragmentWeakRef.get().mapViewModel.setPos(pos);
                }
            }
        }

        @Override
        public void onFailure(@NonNull Exception exception){
            Log.w(TAG, "Failed getting location");
        }
    }
}