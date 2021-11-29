package com.example.baybucket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DestinationActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tvName, tvDistance;
    Button btnArrived;
    MapView mapView;

    private GoogleMap mMap;

    private static final String TAG = "DestinationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        Intent intent = getIntent();
        int position = intent.getIntExtra("int_value", -1);
        String destinationName = getIntent().getExtras().getString("name");
        String destinationDistance = getIntent().getExtras().getString("distance");

        tvName = findViewById(R.id.destination_name);
        tvName.setText(destinationName);

        tvDistance = findViewById(R.id.tv_distance);
        tvDistance.setText(destinationDistance);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnArrived = findViewById(R.id.btn_arrived);
        btnArrived.setOnClickListener(view -> showDialog());
    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = DestinationFragment.newInstance("CANCEL", "SUBMIT");
        newFragment.setShowsDialog(true);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i(TAG, "Map ready");
        mMap = googleMap;
        // Add a marker on destination and move the camera
        LatLng scu = new LatLng(37.35, -121.94); // TODO: change to destination coordinates
        mMap.addMarker(new MarkerOptions().position(scu).title("Marker in SCU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(scu,15));
        mapView.onResume();
    }
}