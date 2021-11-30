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

    ImageButton sf, sc, scr, sj, b, pa;

    //db stuff

    DestinationRepository destinationRepository;

    //String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        sf = (ImageButton)root.findViewById(R.id.sanFrancisco);
        sc = (ImageButton)root.findViewById(R.id.santaClara);
        sj = (ImageButton)root.findViewById(R.id.sanJose);
        scr = (ImageButton)root.findViewById(R.id.santaCruz);
        b = (ImageButton)root.findViewById(R.id.berkeley);
        pa = (ImageButton)root.findViewById(R.id.paloAlto);


        sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                intent.putExtra("name", "san%20francisco");
                startActivity(intent);
            }
        });
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                intent.putExtra("name", "santa%20clara");
                startActivity(intent);
            }
        });
        sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                intent.putExtra("name", "san%20jose");
                startActivity(intent);
            }
        });
        scr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                intent.putExtra("name", "santa%20cruz");
                startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                intent.putExtra("name", "berkeley");
                startActivity(intent);
            }
        });
        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BucketList.class);
                intent.putExtra("name", "palo%20alto");
                startActivity(intent);
            }
        });
      
        //Tanvi db stuff
        destinationRepository = new DestinationRepository(getContext());
        Destination myDestination_sf1 = new Destination("Presidio of San Francisco", "201 Fort Mason", "Playground", "san francisco");
        Destination myDestination_sf2 = new Destination("Mission Dolores Park", "Point Lobos Ave", "Urban Park", "san francisco");
        Destination myDestination_sf3 = new Destination("Fort Mason", "2 Marina Blvd", "Park", "San Francisco");
        Destination myDestination_ber1 = new Destination("Berkeley Marina", "27725 Greenfield Rd", "Harbor", "berkeley");
        Destination myDestination_ber2 = new Destination("Albany Bulb", "1 Buchanan St", "Park", "Berkeley");
        Destination myDestination_sc1 = new Destination("Central Park", "909 Kiely Blvd", "Playground", "santa clara");
        Destination myDestination_sc2 = new Destination("Lakewood Park", "834 Lakechime Dr", "Playground", "Santa Clara");
        Destination myDestination_sj1 = new Destination("East Foothills", "San Jose", "Other Great Outdoors", "San Jose");
        destinationRepository.insertDestination(myDestination_sf1);
        destinationRepository.insertDestination(myDestination_sf2);
        destinationRepository.insertDestination(myDestination_sf3);
        destinationRepository.insertDestination(myDestination_ber1);
        destinationRepository.insertDestination(myDestination_ber2);
        destinationRepository.insertDestination(myDestination_sc1);
        destinationRepository.insertDestination(myDestination_sc2);
        destinationRepository.insertDestination(myDestination_sj1);

        //List<Destination> destinationList = destinationRepository.getAll();
        //Log.i(TAG, "Destination: "+destinationList.size());

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