package com.example.baybucket.ui.memory_gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.models.Memory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoryGalleryFragment extends Fragment {

    private MemoryGalleryViewModel memoryGalleryViewModel;
    private FragmentMemoryGalleryBinding binding;

    private GridView imageGrid;
    private ArrayList<Uri> uriList;

    private ArrayList<Memory> memoryList = new ArrayList<Memory>();

    private String userEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memoryGalleryViewModel =
                new ViewModelProvider(this).get(MemoryGalleryViewModel.class);

        binding = FragmentMemoryGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imageGrid = (GridView)root.findViewById(R.id.memories_grid);
        uriList = new ArrayList<Uri>();

        //firebase call for user email
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //local memory db call
        MemoryRepository memoryRepository = new MemoryRepository(getActivity());
        List<Memory> dbMemoryList = memoryRepository.getMemoriesByUser(userEmail);
        memoryList = new ArrayList<Memory>(dbMemoryList);

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
                Log.i("tag", memoryList.get(i).getImageUri());
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