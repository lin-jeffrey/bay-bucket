package com.example.baybucket;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baybucket.db.Converters;
import com.example.baybucket.db.MemoryRepository;
import com.example.baybucket.models.Memory;

import java.io.File;
import java.util.List;

public class MemoryActivity extends AppCompatActivity {
    private ImageView iv_image;
    private TextView tv_destination;
    private TextView tv_date;
    private TextView tv_caption;

    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        memory = (Memory) getIntent().getSerializableExtra("Memory");

        iv_image = findViewById(R.id.memory_image);
        tv_destination = findViewById(R.id.destination_name);
        tv_date = findViewById(R.id.check_in_date);
        tv_caption = findViewById(R.id.memory_caption);


        if(memory.getImageUri().length() != 0){
            iv_image.setImageURI(Uri.parse(memory.getImageUri()));
        }
        else{
            Log.i("debug", "default image");
            iv_image.setImageURI(Uri.parse("android.resource://com.example.baybucket/drawable/default_image"));
        }
        tv_destination.setText(memory.getDestinationName());
        tv_date.setText(Converters.dateToTimeStamp(memory.getCheckInDate()));
        tv_caption.setText(memory.getCaption());
    }
}
