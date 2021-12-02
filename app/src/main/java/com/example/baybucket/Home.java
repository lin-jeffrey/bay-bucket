package com.example.baybucket;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baybucket.db.UserRepository;
import com.example.baybucket.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.baybucket.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Home extends AppCompatActivity implements DrawerLayout.DrawerListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    //to access user that is logged in
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private String userID;

    private String userName="";
    private String userPoints="";

    private TextView nav_header_username;
    private TextView nav_header_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Select any of the available locations to view a list of the must-see places in the city.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        drawer.setDrawerListener(this);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            logout();
            return true;
        });

        //link navigation header ui elements
        View headerView = navigationView.getHeaderView(0);
        TextView nav_header_username = (TextView) headerView.findViewById(R.id.nav_header_username);
        TextView nav_header_points = (TextView) headerView.findViewById(R.id.nav_header_points);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_gallery, R.id.nav_leaderboard)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        //Accessing user details of current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        mDatabase.child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().child("name").getValue()));
                    String currentUsername = String.valueOf(task.getResult().child("name").getValue());
                    String currentEmail = String.valueOf(task.getResult().child("email").getValue());
                    String currentPoints = String.valueOf(task.getResult().child("points").getValue());

                    //setting header text elements
                    nav_header_username.setText(currentUsername);
                    nav_header_points.setText("Points: " + currentPoints);

                    UserRepository userRepository = new UserRepository(Home.this);
                    userRepository.updateUser(new User(currentUsername, currentEmail, Integer.valueOf(currentPoints), ""));
                }
            }
        });


    }

    private void doMySearch(String query) {
        Intent intent = new Intent(this, BucketList.class);
        switch(query.toLowerCase()){
            case "san francisco" :
                intent.putExtra("name", "san%20francisco");
                startActivity(intent);
                break;
            case "santa clara" :
                intent.putExtra("name", "santa%20clara");
                startActivity(intent);
                break;
            case "san jose" :
                intent.putExtra("name", "san%20jose");
                startActivity(intent);
                break;
            case "santa cruz" :
                intent.putExtra("name", "santa%20cruz");
                startActivity(intent);
                break;
            case "berkeley" :
                intent.putExtra("name", "berkeley");
                startActivity(intent);
                break;
            case "palo alto" :
                intent.putExtra("name", "palo%20alto");
                startActivity(intent);
                break;
            default : Toast.makeText(this, "Sorry! " + query + " bucket list is not available. Please try some other city!", Toast.LENGTH_SHORT).show();

        }

    }

    private void logout() {
       // Toast.makeText(Home.this,"In logout",Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Home.this,Login.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML

        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        UserRepository userRepository = new UserRepository(Home.this);
        int userPoints = userRepository.getUserByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get(0).getPoints();
        Log.i("debug", Integer.toString(userPoints));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navPoints = (TextView) headerView.findViewById(R.id.nav_header_points);
        navPoints.setText("Points: " + Integer.toString(userPoints));
    }

    @Override
    public void onDrawerClosed (View drawerView){

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}