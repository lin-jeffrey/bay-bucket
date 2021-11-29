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
import com.example.baybucket.MemoryActivity;
import com.example.baybucket.R;
import com.example.baybucket.databinding.FragmentProfileBinding;
import com.example.baybucket.models.Memory;
import com.example.baybucket.models.User;
import com.google.android.gms.maps.MapView;
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

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    private GridView imageGrid;
    private ArrayList<Uri> uriList;

    private String currentUsername;
    private String currentEmail;
    private int currentPoints;

    private ImageView profile_image;
    private TextView username;
    private TextView points;
    private MapView map;

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
        map = (MapView)root.findViewById(R.id.map);
        imageGrid = (GridView)root.findViewById(R.id.memories_grid);

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
                    currentEmail = String.valueOf(task.getResult().child("email").getValue());
                    currentPoints = Integer.parseInt(String.valueOf(task.getResult().child("points").getValue()));

                    username.setText(currentUsername);
                    points.setText("Points: " + Integer.toString(currentPoints));
                }
            }
        });

        //map


        //memory grid
        uriList = new ArrayList<Uri>();

        //temp hard coded memories
        Date date =  new Date();
        Memory memorySC = new Memory("Joe", "Santa Clara", date, "This was a fun day in Santa Clara", "android.resource://com.example.baybucket/drawable/santa_clara");
        Memory memorySF = new Memory("Joe", "San Francisco", date, "This was a fun day in San Francisco", "android.resource://com.example.baybucket/drawable/san_francisco");
        Memory memoryPA = new Memory("Joe", "Palo Alto", date, "This was a fun day in Palo Alto", "android.resource://com.example.baybucket/drawable/palo_alto");
        Memory memorySJ = new Memory("Joe", "San Jose", date, "This was a fun day in San Jose", "android.resource://com.example.baybucket/drawable/san_jose");

        ArrayList<Memory> memoryList = new ArrayList<>();
        memoryList.add(memorySC);
        memoryList.add(memorySF);
        memoryList.add(memoryPA);
        memoryList.add(memorySJ);

        /* future db query to get memories
        MemoryRepository memoryRepository = new MemoryRepository(getActivity());
        List<Memory> memoryList = memoryRepository.getMemoriesByUser(user.getUsername());
        */
        try {
            for(int i = 0; i < memoryList.size(); i++) {
                uriList.add(Uri.parse(memoryList.get(i).getImageUri()));
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
