package com.example.baybucket;

import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class BucketList extends AppCompatActivity {

    List<BucketListItems> bucketList;
    RecyclerView recyclerView;
    BucketListAdapter bucketListAdapter;

    public final static String TAG = "Success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);

        recyclerView = findViewById(R.id.destinationsList);
        bucketList = new ArrayList<>();

        try {
            loadBucketListItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadBucketListItems() throws IOException {
        final String URL_PREFIX = "https://api.foursquare.com/v3/places/search?";
        final String LAT_LONG = "ll=37.7749%2C-122.419";
        final String CATEGORIES = "&categories=16000";
        final String SORT_BY = "&sort=POPULARITY";
        final String API_KEY = "fsq3ETkzlZ2aaky4SiCtLWknn8YDqtzaN7HYB8+JoPz9IqU=";
        final String NEAR = "&near=san%20francisco";

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
                        String name = null;
                        String distance_str = null;
                        int distance;
                        double distance_in_miles = 0;
                        BucketListItems item = new BucketListItems();
                        try {
                            item.setBucketListItemName(data_obj.getString("name"));
                            distance_str = data_obj.getString("distance");
                            distance = Integer.parseInt(distance_str);
                            distance_in_miles = Math.round((distance / 1609) * 1000) / 1000 ;
                            item.setDistance(String.valueOf(distance_in_miles + " miles"));

                            bucketList.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "Name is: "+item.getBucketListItemName());

                    }

                    BucketList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //listAdapter.notifyDataSetChanged();
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            bucketListAdapter = new BucketListAdapter(getApplicationContext(), bucketList);
                            recyclerView.setAdapter(bucketListAdapter);
                        }
                    });
                }
            }
        });
        /*JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("result");
                            //int maxItems = result.getInt("end");
                            JSONArray resultList = result.getJSONArray(Integer.parseInt("result"));
                            tv_result.append(resultList.toString());
                            Log.i(TAG, "The result is here!!");

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(BucketList.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(BucketList.this, "There was an error", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", API_KEY);
                params.put("Accept-Language", "en");

                return params;
            }
        };
        queue.add(request);*/
    }

}