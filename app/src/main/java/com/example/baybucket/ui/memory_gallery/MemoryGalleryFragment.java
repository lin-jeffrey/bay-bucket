package com.example.baybucket.ui.memory_gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baybucket.ImageGridAdapter;
import com.example.baybucket.MemoryActivity;
import com.example.baybucket.R;
import com.example.baybucket.databinding.FragmentMemoryGalleryBinding;
import com.example.baybucket.models.Memory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

public class MemoryGalleryFragment extends Fragment {

    private MemoryGalleryViewModel memoryGalleryViewModel;
    private FragmentMemoryGalleryBinding binding;

    private GridView imageGrid;
    private ArrayList<Uri> uriList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoryGalleryViewModel =
                new ViewModelProvider(this).get(MemoryGalleryViewModel.class);

        binding = FragmentMemoryGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageGrid = (GridView)root.findViewById(R.id.memories_grid);
        uriList = new ArrayList<Uri>();

        //temp hard coded memories
        Date date =  new java.util.Date();
        Memory memorySC = new Memory("Joe@email.com", "Santa Clara", "37.34927855035714,-121.93883618583588", date, "This was a fun day in Santa Clara", "android.resource://com.example.baybucket/drawable/santa_clara_main");
        Memory memorySF = new Memory("Joe@email.com", "San Francisco","37.82051413617538,-122.47690203902356", date, "This was a fun day in San Francisco", "android.resource://com.example.baybucket/drawable/pier39");
        Memory memoryPA = new Memory("Joe@email.com", "Palo Alto","37.42768671107808,-122.16963732630407", date, "This was a fun day in Palo Alto", "android.resource://com.example.baybucket/drawable/palo_alto_main");
        Memory memorySJ = new Memory("Joe@email.com", "San Jose", "37.33309065682504,-121.89112191755665", date, "This was a fun day in San Jose", "android.resource://com.example.baybucket/drawable/san_jose_main");

        ArrayList<Memory> memoryList = new ArrayList<>();
        memoryList.add(memorySC);
        memoryList.add(memorySF);
        memoryList.add(memoryPA);
        memoryList.add(memorySJ);

        /* future db query to get memories
        MemoryRepository memoryRepository = new MemoryRepository(getActivity());
        List<Memory> memoryList = memoryRepository.getMemoriesByUser(user.getEmail());
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