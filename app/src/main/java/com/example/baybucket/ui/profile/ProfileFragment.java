package com.example.baybucket.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baybucket.ImageGridAdapter;
import com.example.baybucket.ImageTestActivity;
import com.example.baybucket.MemoryActivity;
import com.example.baybucket.R;
import com.example.baybucket.databinding.FragmentProfileBinding;
import com.example.baybucket.db.Converters;
import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.db.UserRepository;
import com.example.baybucket.models.Memory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProfileFragment extends Fragment implements OnMapReadyCallback{
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    private GridView imageGrid;
    private ArrayList<Uri> uriList;
    private ArrayList<Memory> memoryList = new ArrayList<Memory>();

    private String currentUsername;
    private int currentPoints;
    private String currentEmail = "";

    private ImageView profile_image;
    private TextView username;
    private TextView points;

    private MapView mapView;
    private GoogleMap mMap;

    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //binding
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //UI linking
        //profile_image = (ImageView)root.findViewById(R.id.profile_picture);
        username = (TextView)root.findViewById(R.id.username);
        points = (TextView)root.findViewById(R.id.points);
/*
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePhoto = new Intent(getActivity(), ImageTestActivity.class);
                startActivity(takePhoto);
            }
        });*/
/*
        UserRepository userRepository = new UserRepository(getActivity());
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(userRepository.getUserByEmail(currentFirebaseUser.getEmail()).size() > 0){
            Log.i("log", "size: " + String.valueOf(userRepository.getUserByEmail(currentFirebaseUser.getEmail()).size()));
            String profileImageUri = userRepository.getUserByEmail(currentFirebaseUser.getEmail()).get(0).getImageUri();
            userRepository.getUserByEmail(currentFirebaseUser.getEmail()).get(0).printUser();
            System.out.println(profileImageUri);
            if(profileImageUri != null){
                Log.i("log", profileImageUri);
                profile_image.setImageURI(Uri.parse(profileImageUri));
            }
            else{
                profile_image.setImageURI(Uri.parse("android.resource://com.example.baybucket/drawable/default_profile_image"));
                Log.i("log", "default profile");
            }
        }*/

        //memory image grid
        imageGrid = (GridView)root.findViewById(R.id.memories_grid);
        uriList = new ArrayList<Uri>();

        //map
        mapView = (MapView)root.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        //firebase db call for user info
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().child("name").getValue()));
                    currentUsername = String.valueOf(task.getResult().child("name").getValue());
                    currentPoints = Integer.parseInt(String.valueOf(task.getResult().child("points").getValue()));
                    setCurrentEmail(String.valueOf(task.getResult().child("email").getValue()));
                    Log.i("Tag", String.valueOf(task.getResult().child("email").getValue()));
                    Log.i("Tag", currentEmail);



                    username.setText(currentUsername);
                    points.setText("Points: " + currentPoints);
                }
            }
        });

        // future db query to get memories
        MemoryRepository memoryRepository = new MemoryRepository(getActivity());

        //firebase call for user email
        currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //get memories from roomdb
        List<Memory> dbMemoryList = memoryRepository.getMemoriesByUser(currentEmail);
        memoryList = new ArrayList<Memory>(dbMemoryList);

        Collections.sort(memoryList, new Comparator<Memory>() {
            @Override
            public int compare(Memory o1, Memory o2) {
                return o2.getCheckInDate().compareTo(o1.getCheckInDate());
            }
        });

        //populate image gallery
        try {
            for(int i = 0; i < memoryList.size(); i++) {
                if(memoryList.get(i).getImageUri().length() != 0){
                    uriList.add(Uri.parse(memoryList.get(i).getImageUri()));
                    Log.i("image", memoryList.get(i).getImageUri());
                }
                else{
                    Log.i("debug", "default image");
                    uriList.add(Uri.parse("android.resource://com.example.baybucket/drawable/default_image"));
                }
                memoryList.get(i).printMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageGrid.setAdapter(new ImageGridAdapter(getActivity(), uriList));

        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), MemoryActivity.class);
                i.putExtra("Memory", memoryList.get(position));
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i("TAG", "Map ready");
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if(memoryList.size() != 0){
            try {
                for(int i = 0; memoryList.size() > 0; i++) {
                    mMap.addMarker(new MarkerOptions().position(Converters.stringToLatLong(memoryList.get(i).getCoordinates())).title(memoryList.get(i).getDestinationName()));
                    builder.include(Converters.stringToLatLong(memoryList.get(i).getCoordinates()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
        mapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setCurrentEmail(String email){
        this.currentEmail = email;
    }
}
