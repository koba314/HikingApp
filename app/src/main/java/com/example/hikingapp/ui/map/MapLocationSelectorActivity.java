package com.example.hikingapp.ui.map;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.hikingapp.R;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
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
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * MapLocationSelectorActivity
 * An activity with a map and some preset markers.
 *
 * Should be called via startActivityForResult(Intent i, int resultCode).
 * RESULT_START, RESULT, or RESULT_END will be passed back as a requestCode in OnActivityResult() of the calling activity or fragment.
 * These pertain to getting a start position, generic position, or end position, respectively.
 *
 * Included is a static singleton class for setting initial locations of the map markers. Before starting this activity, get the instance
 * of this class and set the needed positions for the map markers.
 */
public class MapLocationSelectorActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String TAG = "MapLocSelectorActivity";
    public static final int PERMISSION_FINE_LOCATION = 0;
    /**
     * Various constants to be used in the intent that launches this activity and the result sent to the caller.
     */
    public static final String START_LATITUDE = "MapLocationSelectorActivity.START_LATITUDE";
    public static final String START_LONGITUDE = "MapLocationSelectorActivity.START_LONGITUDE";
    public static final String END_LATITUDE = "MapLocationSelectorActivity.END_LATITUDE";
    public static final String END_LONGITUDE = "MapLocationSelectorActivity.END_LONGITUDE";
    public static final String LATITUDE = "MapLocationSelectorActivity.LATITUDE";
    public static final String LONGITUDE = "MapLocationSelectorActivity.LONGITUDE";
    public static final String MODE = "MapLocationSelectorActivity.MODE";
    public static final String START = "MapLocationSelectorActivity.START";
    public static final String GENERIC = "MapLocationSelectorActivity.GENERIC";
    public static final String END = "MapLocationSelectorActivity.END";
    public static final int RESULT_START = 1;
    public static final int RESULT = 2;
    public static final int RESULT_END = 3;

    private static final String ID_ICON_RED = "ID_ICON_RED";
    private static final String ID_ICON_GRAY = "ID_ICON_GRAY";
    private static final String ID_ICON_GREEN = "ID_ICON_GREEN";

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Button confirmButton;
    private ImageView hoveringMarker;

    private boolean hasLocationPermission = false;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        if(i.getStringExtra(MODE).equals(START)){
            mode = START;
        }else if(i.getStringExtra(MODE).equals(END)){
            mode = END;
        }else if(i.getStringExtra(MODE).equals(GENERIC)){
            mode = GENERIC;
        }

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map_location_selector);
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

        confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LatLng mapTarget = mapboxMap.getCameraPosition().target;
                if(mode.equals(START)){
                    Intent i = new Intent();
                    i.putExtra(START_LATITUDE, mapTarget.getLatitude());
                    i.putExtra(START_LONGITUDE, mapTarget.getLongitude());
                    setResult(RESULT_START, i);
                }else if(mode.equals(END)){
                    Intent i = new Intent();
                    i.putExtra(END_LATITUDE, mapTarget.getLatitude());
                    i.putExtra(END_LONGITUDE, mapTarget.getLongitude());
                    setResult(RESULT_END, i);
                }else{
                    Intent i = new Intent();
                    i.putExtra(LATITUDE, mapTarget.getLatitude());
                    i.putExtra(LONGITUDE, mapTarget.getLongitude());
                    setResult(RESULT, i);
                }
                finish();
            }
        });
    }

    private void initLocationEngine(){

    }

    private void setUpMap(boolean hasLocation){
        if(hasLocation){
            mapView.getMapAsync(new OnMapReadyCallback(){
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap){
                    MapLocationSelectorActivity.this.mapboxMap = mapboxMap;

                    Drawable dMarkerGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_gray, getTheme());
                    Drawable dMarkerGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_green, getTheme());
                    Drawable dMarkerRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_red, getTheme());

                    Style.Builder builder = new Style.Builder()
                            .fromUri(Style.OUTDOORS);
                    if(dMarkerGray != null){
                        builder.withImage(ID_ICON_GRAY, dMarkerGray);
                    }
                    if(dMarkerGreen != null){
                        builder.withImage(ID_ICON_GREEN, dMarkerGreen);
                    }
                    if(dMarkerRed != null){
                        builder.withImage(ID_ICON_RED, dMarkerRed);
                    }

                    mapboxMap.setStyle(builder, new Style.OnStyleLoaded(){
                        @Override
                        public void onStyleLoaded(@NonNull Style style){
                            setUpLocationComponent(style);
                            setUpHoveringMarker(style);
                            setUpExistingMarkers(style);

                            MapLocations locations = MapLocations.getInstance();
                            CameraPosition.Builder b = new CameraPosition.Builder()
                                    .zoom(12);
                            // Sets camera target to current location or start/end marker based on context.
                            if(mode.equals(START)){
                                if(locations.getStart() != null){
                                    b.target(locations.getStart());
                                }else{
                                    try {
                                        double lat = mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude();
                                        double lng = mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude();
                                        LatLng pos = new LatLng(lat,lng);
                                        b.target(pos);
                                    }catch(NullPointerException e){
                                        Log.w(TAG, "unable to get location!");
                                    }
                                }
                            }else if(mode.equals(END)){
                                if(locations.getEnd() != null){
                                    b.target(locations.getEnd());
                                    CameraPosition cameraPosition = b.build();
                                    mapboxMap.setCameraPosition(cameraPosition);
                                }else{
                                    try {
                                        double lat = mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude();
                                        double lng = mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude();
                                        LatLng pos = new LatLng(lat,lng);
                                        b.target(pos);
                                    }catch(NullPointerException e){
                                        Log.w(TAG, "unable to get location!");
                                    }
                                }
                            }
                            CameraPosition cameraPosition = b.build();
                            mapboxMap.setCameraPosition(cameraPosition);
                        }
                    });
                }
            });
        }else{
            mapView.getMapAsync(new OnMapReadyCallback(){
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap){
                    MapLocationSelectorActivity.this.mapboxMap = mapboxMap;

                    Drawable dMarkerGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_gray, getTheme());
                    Drawable dMarkerGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_green, getTheme());
                    Drawable dMarkerRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_red, getTheme());

                    Style.Builder builder = new Style.Builder()
                            .fromUri(Style.OUTDOORS);
                    if(dMarkerGray != null){
                        builder.withImage(ID_ICON_GRAY, dMarkerGray);
                    }
                    if(dMarkerGreen != null){
                        builder.withImage(ID_ICON_GREEN, dMarkerGreen);
                    }
                    if(dMarkerRed != null){
                        builder.withImage(ID_ICON_RED, dMarkerRed);
                    }

                    mapboxMap.setStyle(builder, new Style.OnStyleLoaded(){
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
        locationComponent.setCameraMode(CameraMode.NONE);
        locationComponent.setRenderMode(RenderMode.NORMAL);
    }

    private void setUpExistingMarkers(Style style){
        MapLocations locations = MapLocations.getInstance();

        SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);

        if(mode.equals(START)){
            if(locations.getEnd() != null){
                Symbol symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(locations.getEnd())
                        .withIconImage(ID_ICON_RED));
            }
        }else if(mode.equals(END)){
            if(locations.getStart() != null){
                Symbol symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(locations.getStart())
                        .withIconImage(ID_ICON_GREEN));
            }
        }
    }

    private void setUpHoveringMarker(Style style){
        // Add the marker image to the view
        hoveringMarker = new ImageView(MapLocationSelectorActivity.this);
        if(mode.equals(START)){
            hoveringMarker.setImageResource(R.drawable.ic_mapbox_marker_icon_green);
        }else if(mode.equals(END)){
            hoveringMarker.setImageResource(R.drawable.ic_mapbox_marker_icon_red);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringMarker.setLayoutParams(params);
        mapView.addView(hoveringMarker);

        // Add the marker image to map but keep it hidden
//        style.addImage("dropped-icon-image", BitmapFactory.decodeResource(
//                getResources(), R.drawable.ic_mapbox_marker_icon_green));
//        style.addSource(new GeoJsonSource("dropped-marker-source-id"));
//        style.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
//                "dropped-marker-source-id").withProperties(
//                iconImage("dropped-icon-image"),
//                visibility(NONE),
//                iconAllowOverlap(true),
//                iconIgnorePlacement(true)
//        ));
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * MapLocations
     * Nested Singleton class for the purposes of easily providing start and end positions to the map,
     * as well as an number of other generic markers.
     */
    public static class MapLocations {
        private LatLng start;
        private LatLng end;
        private List<LatLng> markers;
        private static MapLocations instance;
        private MapLocations(){
        }
        public static MapLocations getInstance(){
            if(instance == null){
                instance = new MapLocations();
            }
            return instance;
        }
        public List<LatLng> getMarkers(){ return markers; }
        public void setMarkers(List<LatLng> markers) { this.markers = markers; }
        @Nullable public LatLng getStart(){ return start; }
        public void setStart(@Nullable LatLng start){ this.start = start; }
        @Nullable public LatLng getEnd(){ return end; }
        public void setEnd(@Nullable LatLng end){ this.end = end; }
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