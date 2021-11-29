package com.example.baybucket.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String email;
    @NonNull
    private String username;
    private int points;
    private String imageUri;

    @Ignore
    public User(@NonNull String username, @NonNull String email){
        this.username = username;
        this.email = email;
        this.points = 0;
        this.imageUri = "";
    }

    public User(@NonNull String username, @NonNull String email, int points){
        this.username = username;
        this.email = email;
        this.points = points;
        this.imageUri = "";
    }

    @Ignore
    public User(@NonNull String username, @NonNull String email, int points, String imageUri){
        this.username = username;
        this.email = email;
        this.points = points;
        this.imageUri = imageUri;
    }

    @NonNull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getEmail() {
        return this.email;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints(int points){
        this.points += points;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void printUser(){
        String output = "Username: " + this.username + " Email: " + this.email + " Points: " + this.points +
                " ImageUri: " + this.imageUri;
        System.out.println(output);
    }
}
