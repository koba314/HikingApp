package com.example.hikingapp.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.hikingapp.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MapFragment extends Fragment {

    public static final String TAG = "MapFragment";
    public static final String MAP_FRAGMENT = "com.hikingapp.MAP_FRAGMENT";

    private MapViewModel mapViewModel;
    private MapView mapView;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView()");
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = root.findViewById(R.id.mapView);
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
        return root;
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
}