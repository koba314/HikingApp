package com.example.hikingapp.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hikingapp.MainActivity;
import com.example.hikingapp.R;
import com.example.hikingapp.model.HikingPlan;
import com.example.hikingapp.model.MapPin;
import com.example.hikingapp.ui.plans.ViewPlansBottomSheetDialogFragment;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
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
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;

public class MapFragment extends Fragment implements OnMapReadyCallback, OnSymbolClickListener {

    public static final String TAG = "MapFragment";
    public static final String MAP_FRAGMENT = "com.hikingapp.MAP_FRAGMENT";
    public static final int PERMISSION_FINE_LOCATION = 0;
    public static final String ADDING_PIN = "ADDING_PIN";
    public static final String DEFAULT = "DEFAULT";
    public String state = DEFAULT;

    private MapViewModel mapViewModel;
    private MapView mapView;
    private LocationEngine locationEngine;
    private MapboxMap mapboxMap;
    private SymbolManager symbolManager;
    private SymbolManager userPinSymbolManager;
    private SymbolManager otherPinSymbolManager;
    private Map<Symbol, HikingPlan> startMarkers = new HashMap<>();
    private Map<Symbol, HikingPlan> endMarkers = new HashMap<>();
    private Map<Symbol, MapPin> userPins = new HashMap<>();
    private Map<Symbol, MapPin> otherPins = new HashMap<>();

    private Button addPinButton;
    private Button confirmPinButton;
    private Button cancelPinButton;
    private ImageView hoveringPin;
    private View myLocationButton;

    private static final String PLAN_NAME = "PLAN_NAME";
    private static final String VISIBLE_PLAN_NAMES_SOURCE = "VISIBLE_PLAN_NAMES_SOURCE";
    private static final String VISIBLE_PLAN_NAMES_LAYER = "VISIBLE_PLAN_NAMES_LAYER";
    private static final String ID_ICON_RED = "ID_ICON_RED";
    private static final String ID_ICON_GRAY = "ID_ICON_GRAY";
    private static final String ID_ICON_GREEN = "ID_ICON_GREEN";
    private static final String ID_ICON_BLUE = "ID_ICON_BLUE";

    private LocationListeningCallback locationCallback = new LocationListeningCallback(this);

    private boolean hasLocationPermission = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");

        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        mapViewModel =
                new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.mapView);
        addPinButton = root.findViewById(R.id.add_pin_button);
        confirmPinButton = root.findViewById(R.id.confirm_pin_button);
        cancelPinButton = root.findViewById(R.id.cancel_pin_button);
        myLocationButton = root.findViewById(R.id.my_location_cl);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        mapView.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Location permission already granted");
            hasLocationPermission = true;
            initLocationEngine();
            mapView.getMapAsync(this);
            setLocationObserver();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == PERMISSION_FINE_LOCATION && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "Location permission granted");
                hasLocationPermission = true;
                initLocationEngine();
                mapView.getMapAsync(this);
                setLocationObserver();
            }else{
                Log.i(TAG, "Location permission denied.");
                mapView.getMapAsync(this);
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
//                Log.i(TAG, "Location data updated");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap){
        this.mapboxMap = mapboxMap;
        Drawable dMarkerGray = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_gray, requireActivity().getTheme());
        Drawable dMarkerGreen = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_green, requireActivity().getTheme());
        Drawable dMarkerRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_red, requireActivity().getTheme());
        Drawable dMarkerBlue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mapbox_marker_icon_blue, requireActivity().getTheme());

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
        if(dMarkerBlue != null){
            builder.withImage(ID_ICON_BLUE, dMarkerBlue);
        }

        mapboxMap.setStyle(builder, new Style.OnStyleLoaded(){
            @Override
            public void onStyleLoaded(@NonNull Style style){
                if(hasLocationPermission){
                    setUpLocationComponent(style);
                }
                if(mapViewModel.getCameraPos().getValue() != null && mapViewModel.getZoomLevel().getValue() != null){
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(mapViewModel.getCameraPos().getValue())
                            .zoom(mapViewModel.getZoomLevel().getValue())
                            .build();
                    mapboxMap.setCameraPosition(cameraPosition);
                } else if(hasLocationPermission){
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(mapViewModel.getPos().getValue())
                            .zoom(15)
                            .build();
                    mapboxMap.setCameraPosition(cameraPosition);
                }
                setMapButtonClickListeners();
                setUpPlanMarkers(style);
                setUpPins(style);

            }
        });

        mapboxMap.addOnCameraMoveListener(new MapboxMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mapViewModel.setCameraPos(mapboxMap.getCameraPosition().target);
                mapViewModel.setZoomLevel(mapboxMap.getCameraPosition().zoom);
            }
        });

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

    private void setUpPlanMarkers(Style style, List<HikingPlan> plans){
        Log.i(TAG, "setUpPlanMarkers for " + plans.size() + " plans");

        if(symbolManager != null){
            symbolManager.deleteAll();
        }
        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);

        List<Feature> visiblePlanLabels = new ArrayList<>();
        // create marker symbols for each plan
        for(HikingPlan plan : plans){
            if(!plan.getActive()){
                // start
                Symbol symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(plan.getStartLatitude(), plan.getStartLongitude()))
                        .withIconImage(ID_ICON_GREEN)
                        .withIconSize(0.75f)
                        .withIconOpacity(0.5f));
                startMarkers.put(symbol, plan);
                Feature startPos = Feature.fromGeometry(Point.fromLngLat(plan.getStartLongitude(), plan.getStartLatitude()));
                startPos.addStringProperty(PLAN_NAME, plan.getName());
                visiblePlanLabels.add(startPos);
                // end
                symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(plan.getEndLatitude(), plan.getEndLongitude()))
                        .withIconImage(ID_ICON_RED)
                        .withIconSize(0.75f)
                        .withIconOpacity(0.5f));
                endMarkers.put(symbol, plan);
                Feature endPos = Feature.fromGeometry(Point.fromLngLat(plan.getEndLongitude(), plan.getEndLatitude()));
                endPos.addStringProperty(PLAN_NAME, plan.getName());
                visiblePlanLabels.add(endPos);
            }else{
                // start of active plan
                Symbol symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(plan.getStartLatitude(), plan.getStartLongitude()))
                        .withIconImage(ID_ICON_GREEN));
                startMarkers.put(symbol, plan);
                // end of active plan
                symbol = symbolManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(plan.getEndLatitude(), plan.getEndLongitude()))
                        .withIconImage(ID_ICON_RED));
                endMarkers.put(symbol, plan);
            }
        }
        // collect labels for visible plans
        FeatureCollection labelCollection = FeatureCollection.fromFeatures(visiblePlanLabels);
        if(style.getSource(VISIBLE_PLAN_NAMES_SOURCE) == null){
            GeoJsonSource source = new GeoJsonSource(VISIBLE_PLAN_NAMES_SOURCE, labelCollection);
            style.addSource(source);
        }
        Float[] textOffsets = {0.5f, 0.5f};
        if(style.getLayer(VISIBLE_PLAN_NAMES_LAYER) == null){
            style.addLayer(new SymbolLayer(VISIBLE_PLAN_NAMES_LAYER, VISIBLE_PLAN_NAMES_SOURCE)
                    .withProperties(PropertyFactory.textField(Expression.get(PLAN_NAME)),
                            PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP_LEFT),
                            PropertyFactory.textIgnorePlacement(true),
                            PropertyFactory.textAllowOverlap(true),
                            PropertyFactory.textSize(15f),
                            PropertyFactory.textColor(Color.BLACK)));
        }
    }

    private void setUpPlanMarkers(Style style){
        List<HikingPlan> plans = mapViewModel.getVisiblePlans().getValue();
        if(plans != null){
            setUpPlanMarkers(style, plans);
        }
        mapViewModel.getVisiblePlans().observe(getViewLifecycleOwner(), new Observer<List<HikingPlan>>() {
            @Override
            public void onChanged(List<HikingPlan> plans) {
                setUpPlanMarkers(style, plans);
            }
        });
    }

    private void setUpUserPins(Style style, List<MapPin> pins){
        Log.i(TAG, "setUpPins for " + pins.size() + " pins");

        if(userPinSymbolManager != null){
            userPinSymbolManager.deleteAll();
        }
        userPinSymbolManager = new SymbolManager(mapView, mapboxMap, style);
        userPinSymbolManager.setIconAllowOverlap(true);

        String icon = ID_ICON_GRAY;
        float size = 0.75f;

        for(MapPin pin : pins){
            Symbol symbol = userPinSymbolManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(pin.getLatitude(), pin.getLongitude()))
                    .withIconImage(ID_ICON_BLUE)
                    .withIconSize(1f)
                    .withIconOpacity(0.75f));
            this.userPins.put(symbol, pin);
        }

        userPinSymbolManager.addClickListener(this);
    }

    private void setUpOtherPins(Style style, List<MapPin> pins){
        Log.i(TAG, "setUpPins for " + pins.size() + " pins");

        if(otherPinSymbolManager != null){
            otherPinSymbolManager.deleteAll();
        }
        otherPinSymbolManager = new SymbolManager(mapView, mapboxMap, style);
        otherPinSymbolManager.setIconAllowOverlap(true);

        for(MapPin pin : pins){
            Symbol symbol = otherPinSymbolManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(pin.getLatitude(), pin.getLongitude()))
                    .withIconImage(ID_ICON_GRAY)
                    .withIconSize(0.75f)
                    .withIconOpacity(0.75f));
            this.otherPins.put(symbol, pin);
        }

        otherPinSymbolManager.addClickListener(this);
    }

    @Override
    public boolean onAnnotationClick(Symbol symbol){
        MapPin pin;
        if (userPins.containsKey(symbol)) {
            pin = userPins.get(symbol);
        }else if(otherPins.containsKey(symbol)){
            pin = otherPins.get(symbol);
        }else{
            return true;
        }
        Log.i(TAG, "Clicked pin " + pin.getUid());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(symbol.getLatLng())
                .zoom(15)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mapViewModel.setPinToView(pin);
        ViewPinBottomSheetDialogFragment frag = new ViewPinBottomSheetDialogFragment();
        frag.show(getActivity().getSupportFragmentManager(), "ViewPin");
        return false;
    }

    private void setUpPins(Style style){
        List<MapPin> publicPins = mapViewModel.getPublicPins().getValue();
        if(publicPins != null){
            setUpOtherPins(style, publicPins);
        }
        mapViewModel.getPublicPins().observe(getViewLifecycleOwner(), new Observer<List<MapPin>>() {
            @Override
            public void onChanged(List<MapPin> pins) {
                Log.i(TAG, "got " + pins.size() + " pins");
                setUpOtherPins(style, pins);
            }
        });
        List<MapPin> userPins = mapViewModel.getUserPins().getValue();
        if(userPins != null){
            setUpUserPins(style, userPins);
        }
        mapViewModel.getUserPins().observe(getViewLifecycleOwner(), new Observer<List<MapPin>>() {
            @Override
            public void onChanged(List<MapPin> pins) {
                setUpUserPins(style, pins);
            }
        });
    }

    private void setMapButtonClickListeners(){
        addPinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addPinButton.setVisibility(View.GONE);
                confirmPinButton.setVisibility(View.VISIBLE);
                cancelPinButton.setVisibility(View.VISIBLE);
                state = ADDING_PIN;
                setUpHoveringPin();
            }
        });
        confirmPinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "confirmed pin position: " + mapboxMap.getCameraPosition().target);
                confirmPinButton.setVisibility(View.GONE);
                cancelPinButton.setVisibility(View.GONE);
                addPinButton.setVisibility(View.VISIBLE);
                state = DEFAULT;
                placeHoveringPin();
                mapView.removeView(hoveringPin);
            }
        });
        cancelPinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                confirmPinButton.setVisibility(View.GONE);
                cancelPinButton.setVisibility(View.GONE);
                addPinButton.setVisibility(View.VISIBLE);
                state = DEFAULT;
                mapView.removeView(hoveringPin);
            }
        });
        myLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Location userLoc = mapboxMap.getLocationComponent().getLastKnownLocation();
                LatLng userPos = new LatLng(userLoc.getLatitude(), userLoc.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(userPos)
                        .zoom(15)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    private void setUpHoveringPin() {
        // Add the marker image to the view
        hoveringPin = new ImageView(getContext());
        hoveringPin.setImageResource(R.drawable.ic_mapbox_marker_icon_gray);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        hoveringPin.setLayoutParams(params);
        mapView.addView(hoveringPin);
    }

    private void placeHoveringPin(){
        LatLng pos = mapboxMap.getCameraPosition().target;
        MapPin pin = new MapPin();
        pin.setLatitude(pos.getLatitude());
        pin.setLongitude(pos.getLongitude());
        pin.setIsPublic(false);
        mapViewModel.createMapPin(pin);
        // move camera to the pin, begin editing
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos)
                .zoom(15)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mapViewModel.setPinToView(pin);
        ViewPinBottomSheetDialogFragment frag = new ViewPinBottomSheetDialogFragment();
        frag.setEditMode(true);
        frag.show(getActivity().getSupportFragmentManager(), "ViewPin");

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
        mapView.onDestroy();
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