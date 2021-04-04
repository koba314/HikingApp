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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MapFragment extends Fragment implements OnMapReadyCallback {

    public static final String TAG = "MapFragment";
    public static final String MAP_FRAGMENT = "com.hikingapp.MAP_FRAGMENT";
    public static final int PERMISSION_FINE_LOCATION = 0;

    private MapViewModel mapViewModel;
    private MapView mapView;
    private LocationEngine locationEngine;
    private MapboxMap mapboxMap;
    private SymbolManager symbolManager;
    private Symbol start;
    private Symbol end;
    private Map<Symbol, HikingPlan> startMarkers = new HashMap<>();
    private Map<Symbol, HikingPlan> endMarkers = new HashMap<>();

    private static final String PLAN_NAME = "PLAN_NAME";
    private static final String VISIBLE_PLAN_NAMES_SOURCE = "VISIBLE_PLAN_NAMES_SOURCE";
    private static final String VISIBLE_PLAN_NAMES_LAYER = "VISIBLE_PLAN_NAMES_LAYER";
    private static final String ID_ICON_RED = "ID_ICON_RED";
    private static final String ID_ICON_GRAY = "ID_ICON_GRAY";
    private static final String ID_ICON_GREEN = "ID_ICON_GREEN";

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
                if(hasLocationPermission){
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(mapViewModel.getPos().getValue())
                            .zoom(12)
                            .build();
                    mapboxMap.setCameraPosition(cameraPosition);
                    setUpLocationComponent(style);
                }
                setUpPlanMarkers(style);
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
        Log.i(TAG, "setUpPlanMarkers for " + plans.size());
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

    private void setUpActivePlanMarkers(Style style, HikingPlan plan){
        symbolManager = new SymbolManager(mapView, mapboxMap, style);
        symbolManager.setIconAllowOverlap(true);
        start = symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(plan.getStartLatitude(), plan.getStartLongitude()))
                .withIconImage(ID_ICON_GREEN));

        end = symbolManager.create(new SymbolOptions()
                .withLatLng(new LatLng(plan.getEndLatitude(), plan.getEndLongitude()))
                .withIconImage(ID_ICON_RED));

    }

    private void updateActivePlanMarkers(Style style, HikingPlan plan){
        assert(symbolManager != null);
        symbolManager.setIconAllowOverlap(true);
        if(start == null){
            start = symbolManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(plan.getStartLatitude(), plan.getStartLongitude()))
                    .withIconImage(ID_ICON_GREEN));
        }else{
            start.setLatLng(new LatLng(plan.getStartLatitude(), plan.getStartLongitude()));
        }
        if(end == null) {
            end = symbolManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(plan.getEndLatitude(), plan.getEndLongitude()))
                    .withIconImage(ID_ICON_RED));
        }else{
            end.setLatLng(new LatLng(plan.getEndLatitude(), plan.getEndLongitude()));
        }
    }

    private void setUpPlanMarkers(Style style){
        HikingPlan plan = mapViewModel.getActivePlan().getValue();
        if(plan != null){
//            setUpActivePlanMarkers(style, plan);
        }
        List<HikingPlan> plans = mapViewModel.getVisiblePlans().getValue();
        if(plans != null){
            setUpPlanMarkers(style, plans);
        }
        mapViewModel.getActivePlan().observe(getViewLifecycleOwner(), new Observer<HikingPlan>() {
            @Override
            public void onChanged(HikingPlan plan) {
//                updateActivePlanMarkers(style, plan);
            }
        });
        mapViewModel.getVisiblePlans().observe(getViewLifecycleOwner(), new Observer<List<HikingPlan>>() {
            @Override
            public void onChanged(List<HikingPlan> plans) {
                setUpPlanMarkers(style, plans);
            }
        });
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