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
import com.example.baybucket.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
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
        profile_image = (ImageView)root.findViewById(R.id.profile_picture);
        username = (TextView)root.findViewById(R.id.username);
        points = (TextView)root.findViewById(R.id.points);

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
                    points.setText("Points: " + Integer.toString(currentPoints));
                }
            }
        });

        // future db query to get memories
        MemoryRepository memoryRepository = new MemoryRepository(getActivity());

        //firebase call for user email
        currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //temp hard coded memories
        Date date =  new Date();
        Memory memorySC = new Memory(currentEmail, "Santa Clara", "37.34927855035714,-121.93883618583588", date, "This was a fun day in Santa Clara", "android.resource://com.example.baybucket/drawable/santa_clara_main");
        Memory memorySF = new Memory(currentEmail, "San Francisco","37.82051413617538,-122.47690203902356", date, "This was a fun day in San Francisco", "android.resource://com.example.baybucket/drawable/pier39");
        Memory memoryPA = new Memory(currentEmail, "Palo Alto","37.42768671107808,-122.16963732630407", date, "This was a fun day in Palo Alto", "android.resource://com.example.baybucket/drawable/palo_alto_main");
        Memory memorySJ = new Memory(currentEmail, "San Jose", "37.33309065682504,-121.89112191755665", date, "This was a fun day in San Jose", "android.resource://com.example.baybucket/drawable/san_jose_main");
        //memoryRepository.deleteMemory(memorySJ);
        //Memory memorySJ = new Memory(currentEmail, "San Jose", "37.33309065682504,-121.89112191755665", date, "This was a fun day in San Jose", "content://com.example.baybucket.fileprovider/my_images/JPEG_20211201_195122_7954856801698247343.jpg");
        //Memory memoryBlankImage = new Memory("Joe@email.com", "Berkeley", "37.87170633038972,-122.26054864815632", date, "This was a fun day in Berkeley", "");

        memoryRepository.insertMemory(memorySC);
        memoryRepository.insertMemory(memorySF);
        memoryRepository.insertMemory(memoryPA);
        memoryRepository.insertMemory(memorySJ);
        //memoryList.add(memoryBlankImage);

        List<Memory> dbMemoryList = memoryRepository.getMemoriesByUser(currentEmail);
        memoryList = new ArrayList<Memory>(dbMemoryList);
        try {
            for(int i = 0; i < memoryList.size(); i++) {
                if(memoryList.get(i).getImageUri() != ""){
                    uriList.add(Uri.parse(memoryList.get(i).getImageUri()));
                    Log.i("image", memoryList.get(i).getImageUri());
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

                //Intent i = new Intent(getActivity(), ImageTestActivity.class);
                //startActivity(i);
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
                for(int i = 0; i < memoryList.size(); i++) {
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
