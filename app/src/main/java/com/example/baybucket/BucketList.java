package com.example.baybucket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    //public final static String TAG = "Success";

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


        fetchBucketListAPI_Key = BuildConfig.FOURSQUARE_KEY;
        fetchDistanceAPI_Key = BuildConfig.DISTANCE_MATRIX_KEY;



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
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
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
                            double distance = calculateDistance(latitude_dest, longitude_dest, latitude_curr, longitude_curr) * 0.00062137;
                            //Log.i(TAG, "Distance: "+distance);
                            String distance_str = String.format("%.2f",distance);
                            item.setDistance(distance_str + " miles");
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
        String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
        String ORIGINS = "origins="+latitude_curr+"%2C"+longitude_curr;
        String DESTINATIONS = "&destinations="+latitude_dest+"%2C"+longitude_dest;
        String API_KEY = "&key="+fetchDistanceAPI_Key;

        String request_url = URL + ORIGINS + DESTINATIONS + API_KEY;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(request_url)
                .method("GET", null)
                .build();
        double distance = 0;
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            //Log.i(TAG, "here's the response: "+data);
            JSONObject jsonObject = new JSONObject(data);
            distance = Double.valueOf(jsonObject.getJSONArray("rows").getJSONObject(0)
                    .getJSONArray("elements").getJSONObject(0)
                    .getJSONObject("distance").get("value").toString());
            //Log.i(TAG, "Distance in metres: "+distance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distance;
    }
}