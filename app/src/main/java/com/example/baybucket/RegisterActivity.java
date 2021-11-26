package com.example.baybucket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }



    public void backToLoginPage(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void onRegister(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}
