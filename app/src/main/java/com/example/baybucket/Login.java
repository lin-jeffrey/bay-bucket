package com.example.baybucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
    }

    //jl notes: this section was used to test the db, it can be deleted or modified
    /*
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("hello world");
        DestinationRepository destinationRepository = new DestinationRepository(this);

        System.out.println("Adding myDest1");
        Destination myDest = new Destination("Santa Clara University", "500 El Camino Real", "Go Broncos", "Santa Clara");
        destinationRepository.insertDestination(myDest);

        List<Destination> destinationList = destinationRepository.getAll();
        System.out.println(destinationList.size());
        for(int i = 0; i < destinationList.size(); i++){
            destinationList.get(i).printDestination();
        }

        myDest.addRating(2);
        myDest.addRating(4);
        myDest.addVisit();

        System.out.println("Avg Rating: " + Double.toString(myDest.getAvgRating()));

        System.out.println("Updating myDest1");
        destinationRepository.insertDestination(myDest);

        System.out.println("Adding myDest2");
        Destination myDest2 = new Destination("Coit Tower", "1 Telegraph Hill Blvd, San Francisco, CA", "Tower on the hill", "San Francisco");
        myDest.addRating(6);
        myDest2.addRating(8);
        myDest2.addVisit();

        destinationRepository.insertDestination(myDest2);

        destinationList = destinationRepository.getAll();

        for(int i = 0; i < destinationList.size(); i++){
            destinationList.get(i).printDestination();
        }

        destinationRepository.getDestByBucketList("San Francisco").get(0).printDestination();
    }*/

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void Login(View view) {
//        Intent intent = new Intent(this, Home.class);
//        startActivity(intent);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Invalid Email!");
            editTextEmail.requestFocus();
            return;
        }


        if(password.isEmpty()){
            editTextPassword.setError("Password is Empty!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("Password has less than 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to home activity
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Login.this,"Invalid Email / Password. Please Retry!",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

}
