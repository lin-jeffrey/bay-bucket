package com.example.baybucket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baybucket.db.DestinationRepository;
import com.example.baybucket.models.Destination;
import com.github.jinatonic.confetti.CommonConfetti;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DestinationActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView tvName, tvDistance;
    Button btnArrived;
    MapView mapView;
    ImageView main_image;
    String destinationName;
    String destinationDistance;
    String destinationCoordinates;
    String destinationBucket;
    int position;

    private GoogleMap mMap;

    DialogFragment newFragment;

    private static final String TAG = "DestinationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        Intent intent = getIntent();
        destinationName = getIntent().getExtras().getString("name");
        destinationDistance = getIntent().getExtras().getString("distance");
        destinationCoordinates = getIntent().getExtras().getString("coordinates");
        destinationBucket = getIntent().getExtras().getString("bucket");

        changeMainImage();

        tvName = findViewById(R.id.destination_name);
        tvName.setText(destinationName);

        tvDistance = findViewById(R.id.tv_distance);
        tvDistance.setText(destinationDistance);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        btnArrived = findViewById(R.id.btn_arrived);
        btnArrived.setOnClickListener(view -> {
            Intent intentDestination = new Intent(DestinationActivity.this, DestinationCheckInActivity.class);
            intentDestination.putExtra("name", destinationName);
            intentDestination.putExtra("distance", destinationDistance);
            intentDestination.putExtra("coordinates", destinationCoordinates);
            intentDestination.putExtra("bucket", destinationBucket);
            startActivity(intentDestination);
        });
    }

    private void changeMainImage() {
        main_image = findViewById(R.id.imageView_destination);

        switch (destinationBucket) {
            case "san francisco":
                main_image.setBackgroundResource(R.drawable.sf_golden_gate);
                break;
            case "santa clara":
                main_image.setBackgroundResource(R.drawable.santa_clara_main);
                break;
            case "san jose":
                main_image.setBackgroundResource(R.drawable.san_jose_main);
                break;
            case "santa cruz":
                main_image.setBackgroundResource(R.drawable.santa_cruz_main);
                break;
            case "berkeley":
                main_image.setBackgroundResource(R.drawable.berkely_main);
                break;
            case "palo alto":
                main_image.setBackgroundResource(R.drawable.palo_alto_main);
                break;
        }
        main_image.requestLayout();
    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        CommonConfetti.rainingConfetti(findViewById(R.id.container), new int[] { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN })
                .infinite();
        newFragment = DestinationFragment.newInstance(destinationName, destinationCoordinates, "FOO");
        newFragment.setShowsDialog(true);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i(TAG, "Map ready");
        mMap = googleMap;
        // Add a marker on destination and move the camera
        String[] coordinates = destinationCoordinates.split(",");
        double latitude = Double.parseDouble(coordinates[0]);
        double longitude = Double.parseDouble(coordinates[1]);

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in " + destinationName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        mapView.onResume();
    }

}