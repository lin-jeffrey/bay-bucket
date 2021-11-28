package com.example.baybucket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.baybucket.BucketList;
import com.example.baybucket.R;
import com.example.baybucket.databinding.FragmentHomeBinding;
import com.example.baybucket.db.DestinationRepository;
import com.example.baybucket.models.Destination;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    //Tanvi
    ImageButton sf;

    //db stuff
    Destination myDestination;
    DestinationRepository destinationRepository;

    String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Tanvi
        sf = (ImageButton)root.findViewById(R.id.sanFrancisco);

        //Tanvi
        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                startActivity(intent);
            }
        });
        //Tanvi db stuff
        destinationRepository = new DestinationRepository(getContext());
        myDestination = new Destination("Golden Gate National Recreation Area", "Golden Gate", "San Francisco", "San Francisco");
        myDestination = new Destination("Marin Headlands", "501 Stanyan St", "Urban Park", "San Francisco");
        myDestination = new Destination("Ocean Beach", "Point Lobos Ave", "Beach", "San Francisco");
        destinationRepository.insertDestination(myDestination);

        List<Destination> destinationList = destinationRepository.getAll();

        Log.i(TAG, "Destination: "+destinationList.size());
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}