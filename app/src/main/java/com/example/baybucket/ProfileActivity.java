package com.example.baybucket;

import android.net.Uri;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.models.Memory;
import com.example.baybucket.models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private GridView imageGrid;
    private ArrayList<Uri> uriList;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        this.imageGrid = findViewById(R.id.memories_grid);
        this.uriList = new ArrayList<Uri>();

        MemoryRepository memoryRepository = new MemoryRepository(this);
        List<Memory> memoryList = memoryRepository.getMemoriesByUser(user.getUsername());

        try {
            for(int i = 0; i < memoryList.size(); i++) {
                this.uriList.add(Uri.fromFile(new File(memoryList.get(i).getImageUri())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.imageGrid.setAdapter(new ImageGridAdapter(this, this.uriList));
    }
}