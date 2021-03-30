package com.example.hikingapp.ui.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hikingapp.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MapLocationSelectorActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location_selector);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback(){
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap){
                mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded(){
                    @Override
                    public void onStyleLoaded(@NonNull Style style){
                        //do something
                    }
                });
            }
        });
    }
}