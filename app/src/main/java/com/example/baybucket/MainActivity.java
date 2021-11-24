package com.example.baybucket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

        // TODO delete me
        // temporary for accessing destination & destination review screens
        TextView tv_HelloWorld = findViewById(R.id.helloWorld);
        tv_HelloWorld.setOnClickListener(view -> {
            Intent intentDestination = new Intent(getApplicationContext(), DestinationActivity.class);
            startActivity(intentDestination);
        });
    }
}