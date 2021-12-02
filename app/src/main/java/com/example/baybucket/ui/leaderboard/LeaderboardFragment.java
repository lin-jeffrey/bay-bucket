package com.example.baybucket.ui.leaderboard;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baybucket.R;
import com.example.baybucket.UserDetails;
import com.example.baybucket.databinding.FragmentLeaderboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    private TableLayout table_main;

    private DatabaseReference mDatabase;

    private ArrayList<UserDetails> userDetailsList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //firebase db call for user info
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userDetailsList = new ArrayList<UserDetails>();

        mDatabase.child("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for(DataSnapshot ds : task.getResult().getChildren()) {
                        UserDetails userDetails = ds.getValue(UserDetails.class);
                        userDetailsList.add(userDetails);
                        Log.d("TAG", ds.child("name").getValue(String.class));
                        Log.d("TAG", String.valueOf(userDetailsList.size()));
                    }
                }
            }
        });

        Log.d("TAG", String.valueOf(userDetailsList.size()));

        //linking table
        table_main = (TableLayout)root.findViewById(R.id.main_table);

        return root;
    }

    @Override
    public void onResume() {
        //populating table
        super.onResume();


        Collections.sort(userDetailsList, new Comparator<UserDetails>() {

            public int compare(UserDetails o1, UserDetails o2) {
                return Integer.valueOf(o2.points) - Integer.valueOf(o1.points);
            }
        });

        for (int i = 0; i < userDetailsList.size(); i++) {
            TableRow tbrow = new TableRow(getActivity());
            TextView t1v = new TextView(getActivity());
            t1v.setText(Integer.toString(i+1));
            t1v.setGravity(Gravity.CENTER);
            t1v.setTextSize(18);
            t1v.setPadding(40, 20, 10, 20 );
            tbrow.addView(t1v);
            TextView t2v = new TextView(getActivity());
            t2v.setText(userDetailsList.get(i).name);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(18);
            t2v.setPadding(325, 20, 325, 20 );
            tbrow.addView(t2v);
            TextView t3v = new TextView(getActivity());
            t3v.setText(userDetailsList.get(i).points);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(18);
            t3v.setPadding(10, 20, 40, 20 );
            tbrow.addView(t3v);
            table_main.addView(tbrow);
        }
    }
/*
    public void init() {
        for (int i = 0; i < 10; i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText("Product " + i);
            t2v.setTextColor(Color.WHITE);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText("Rs." + i);
            t3v.setTextColor(Color.WHITE);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            TextView t4v = new TextView(this);
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);
            stk.addView(tbrow);
        }

    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}