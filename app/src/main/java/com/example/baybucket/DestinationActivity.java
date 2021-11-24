package com.example.baybucket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DestinationActivity extends AppCompatActivity {

    Button btnArrived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        btnArrived = findViewById(R.id.btn_arrived);
        btnArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = DestinationFragment.newInstance("CANCEL", "SUBMIT");
        newFragment.setShowsDialog(true);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
}