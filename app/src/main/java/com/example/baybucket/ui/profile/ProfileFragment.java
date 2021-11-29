package com.example.baybucket.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {
    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    private GridView imageGrid;
    private ArrayList<Uri> uriList;
    private User user = new User("Joe", "joe@gmail.com", "password", 10, "android.resource://com.example.baybucket/drawable/logo");

    private ImageView profile_image;
    private TextView username;
    private TextView points;
    private MapView map;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //profile picture
        profile_image = (ImageView)root.findViewById(R.id.profile_picture);
        profile_image.setImageURI(Uri.parse(user.getImageUri()));

        //username
        username = (TextView)root.findViewById(R.id.username);
        username.setText(user.getUsername());

        //point total
        points = (TextView)root.findViewById(R.id.points);
        points.setText("Points: " + Integer.toString(user.getPoints()));

        //map
        map = (MapView)root.findViewById(R.id.map);
        //we will figure this out :(

        //memory grid
        imageGrid = (GridView)root.findViewById(R.id.memories_grid);
        uriList = new ArrayList<Uri>();

        //temp hard coded memories
        Date date =  new java.util.Date();
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
