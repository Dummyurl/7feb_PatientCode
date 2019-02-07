package com.ziffytech.activities;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ziffytech.R;
import com.ziffytech.util.GPSTracker;


public class MapActivity extends CommonActivity implements OnMapReadyCallback {
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        allowBack();
        setHeaderTitle(getString(R.string.tab_map));

        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        Double lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        Double lon = Double.parseDouble(getIntent().getStringExtra("lng"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lon), 12));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
         /*   map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_icon))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(getIntent().getStringExtra("title"))
                    .position(new LatLng(lat, lon)));

    }*/
    }
}
