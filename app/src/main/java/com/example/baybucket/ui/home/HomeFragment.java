package com.example.baybucket.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    ImageButton sf, sc, scr, sj, b, pa;

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
        pa = (ImageButton)root.findViewById(R.id.sanFrancisco);


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