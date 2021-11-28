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