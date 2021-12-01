package com.example.baybucket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baybucket.db.DestinationRepository;
import com.example.baybucket.models.Destination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BucketList extends AppCompatActivity implements LocationListener {

    List<BucketListItems> bucketList;
    RecyclerView recyclerView;
    BucketListAdapter bucketListAdapter;
    TextView tv_latitude;
    TextView tv_longitude;
    LocationManager locationManager;
    String bucketListName;
    String bucketName;

    //db related
    DestinationRepository destinationRepository;
    List<String> finishedItems;

    //progress bar
    ProgressBar pb_bucketListProgress;
    TextView tv_progress;

    ProgressBar pb_loading;

    String fetchBucketListAPI_Key;
    String fetchDistanceAPI_Key;

    ImageView main_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        pb_loading = findViewById(R.id.pb_loading);
        recyclerView = findViewById(R.id.destinationsList);
        tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);
        pb_bucketListProgress = findViewById(R.id.pb_bucketListProgress);
        tv_progress = findViewById(R.id.tv_progress);
        bucketList = new ArrayList<>();
        bucketListName = getIntent().getExtras().getString("name");
        bucketName = bucketListName;
        bucketName = bucketName.replaceAll("%20", " ");

        destinationRepository = new DestinationRepository(this.getApplicationContext());


        changeMainImage();

        //fetch hidden API keys
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            fetchBucketListAPI_Key = bundle.getString("fourSquareKey");
            fetchDistanceAPI_Key = bundle.getString("distanceMatrixKey");
            //Log.i(TAG, "Bucket List API Key: "+fetchBucketListAPI_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Add runtime permission for accessing current location
        if(ContextCompat.checkSelfPermission(BucketList.this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(BucketList.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        getLocation();

        try {
            loadBucketListItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        check_destinationList();
    }

    private void changeMainImage() {
        main_image = findViewById(R.id.main_image);
        if(bucketName.equals("san francisco")){
            main_image.setBackgroundResource(R.drawable.sf_golden_gate);
        }
        else if(bucketName.equals("santa clara")){
            main_image.setBackgroundResource(R.drawable.santa_clara_main);
        }
        else if(bucketName.equals("san jose")){
            main_image.setBackgroundResource(R.drawable.san_jose_main);
        }
        else if(bucketName.equals("santa cruz")){
            main_image.setBackgroundResource(R.drawable.santa_cruz_main);
        }
        else if(bucketName.equals("berkeley")){
            main_image.setBackgroundResource(R.drawable.berkely_main);
        }
        else if(bucketName.equals("palo alto")){
            main_image.setBackgroundResource(R.drawable.palo_alto_main);
        }
        main_image.requestLayout();
    }

    private void showProgress() {
        List<Destination> destinationList = destinationRepository.getDestByBucketList(bucketName);
        int progress = destinationList.size() * 10;
        //Log.i(TAG, "DESTINATION LIST SIZE: "+destinationList.size());
        if(destinationList.size() > 0){
            pb_bucketListProgress.setProgress(progress);
            tv_progress.setText(String.valueOf(progress)+" % complete");
            pb_bucketListProgress.setVisibility(View.VISIBLE);
            tv_progress.setVisibility(View.VISIBLE);
        }
    }

    private void check_destinationList() {
        List<Destination> destinationList = destinationRepository.getDestByBucketList(bucketName);
        finishedItems = new ArrayList<String>();
        for(int i = 0; i < destinationList.size(); i++){
            String finishedItem = destinationList.get(i).getName();
            finishedItems.add(finishedItem);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, BucketList.this);

        //get location if location didn't change
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        tv_latitude.setText(String.valueOf(myLocation.getLatitude()));
        tv_longitude.setText(String.valueOf(myLocation.getLongitude()));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //this will only be called if location did change
        String latitude= String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        tv_latitude.setText(latitude);
        tv_longitude.setText(longitude);
    }

    private void loadBucketListItems() throws IOException {
        final String URL_PREFIX = "https://api.foursquare.com/v3/places/search?";
        final String CATEGORIES = "&categories=16000";
        final String SORT_BY = "&sort=POPULARITY";
        final String API_KEY = fetchBucketListAPI_Key;
        final String NEAR = "&near="+bucketListName;

        String url = URL_PREFIX + CATEGORIES + NEAR + SORT_BY;

        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", API_KEY)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    final String data = response.body().string();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray placesList = null;
                    try {
                        placesList = jsonObject.getJSONArray("results");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i = 0; i < placesList.length(); i++){
                        JSONObject data_obj = null;
                        try {
                            data_obj = placesList.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BucketListItems item = new BucketListItems();
                        try {
                            String bucketListDestinationName = data_obj.getString("name");
                            item.setBucketListItemName(bucketListDestinationName);

                            if(finishedItems.contains(bucketListDestinationName)){
                                item.setVisited(true);
                            }
                            else {
                                item.setVisited(false);
                            }

                            JSONObject geocodes = data_obj.getJSONObject("geocodes");
                            JSONObject main = geocodes.getJSONObject("main");
                            String latitude_dest = main.getString("latitude");
                            String longitude_dest = main.getString("longitude");
                            String latitude_curr = tv_latitude.getText().toString();
                            String longitude_curr = tv_longitude.getText().toString();
                            double distance = calculateDistance(latitude_dest, longitude_dest, latitude_curr, longitude_curr);
                            //Log.i(TAG, "Distance: "+distance);
                            String distance_str = String.format("%.2f",distance);
                            item.setDistance(distance_str + " miles");
                            item.setLatitude(latitude_dest);
                            item.setLongitude(longitude_dest);
                            item.setBucketListName(bucketName);
                            bucketList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    BucketList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            bucketListAdapter = new BucketListAdapter(getApplicationContext(), bucketList);
                            recyclerView.setAdapter(bucketListAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            showProgress();
                            pb_loading.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private double calculateDistance(String latitude_dest, String longitude_dest, String latitude_curr, String longitude_curr) throws IOException, JSONException {
        double latitude_dest_d = Double.parseDouble(latitude_dest);
        double longitude_dest_d = Double.parseDouble(longitude_dest);
        double latitude_curr_d = Double.parseDouble(latitude_curr);
        double longitude_curr_d = Double.parseDouble(longitude_curr);

        longitude_dest_d = Math.toRadians(longitude_dest_d);
        longitude_curr_d = Math.toRadians(longitude_curr_d);
        latitude_dest_d = Math.toRadians(latitude_dest_d);
        latitude_curr_d = Math.toRadians(latitude_curr_d);

        double dlon = longitude_curr_d - longitude_dest_d;
        double dlat = latitude_curr_d - latitude_dest_d;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(latitude_curr_d) * Math.cos(latitude_dest_d)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth
        double r = 3956;

        // calculate the result
        return(c * r);

    }
}