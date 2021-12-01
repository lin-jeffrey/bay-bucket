package com.example.baybucket;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
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

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    //to access user that is logged in
    private FirebaseUser user;
    private DatabaseReference reference;
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
                Snackbar.make(view, "Select any of the available locations to view a list of the must-see places in the city. As you visit and check off places, you will be awarded points. You can also view a leaderboard with the top players. Gotta visit 'em all to be the best.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

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
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails userProfile = snapshot.getValue(UserDetails.class);
                if(userProfile!=null){
                    userName = userProfile.name;
                    userPoints =  userProfile.points;

                    //setting header text elements
                    nav_header_username.setText(userName);
                    nav_header_points.setText("Points: " + userPoints);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this,"Something went wrong in login",Toast.LENGTH_LONG).show();

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
}