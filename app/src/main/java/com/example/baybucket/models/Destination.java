package com.example.baybucket.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.baybucket.db.Converters;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Destination implements Serializable {
    @PrimaryKey
    @NonNull
    private String name;
    private String address;
    private String description;
    @NonNull
    private String bucketListName;
    private int visits;
    @TypeConverters({Converters.class})
    private ArrayList<Integer> ratings;

    public Destination(@NonNull String name, String address, String description, @NonNull String bucketListName){
        this.name = name;
        this.address = address;
        this.description = description;
        this.bucketListName = bucketListName;
        this.ratings = new ArrayList<>();
        this.visits = 0;
    }
    @Ignore
    public Destination(@NonNull String name, String address, String description, @NonNull String bucketListName, ArrayList<Integer> ratings, int visits){
        this.name = name;
        this.address = address;
        this.description = description;
        this.bucketListName = bucketListName;
        this.ratings = ratings;
        this.visits = visits;
    }

    public void addRating(Integer rating){
        this.ratings.add(rating);
    }

    public ArrayList<Integer> getRatings(){
        return this.ratings;
    }

    public void setRatings(ArrayList<Integer> ratings){
        this.ratings = ratings;
    }

    public double getAvgRating(){
        return ratings.stream()
                .mapToDouble(d -> d)
                .average()
                .orElse(0.0);
    }

    public String getBucketListName() {
        return this.bucketListName;
    }

    public void setBucketListName(String bucketListName) {
        this.bucketListName = bucketListName;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVisits() {
        return this.visits;
    }

    public void setVisits(int visits){
        this.visits = visits;
    }

    public void addVisit() {
        this.visits = this.visits + 1;
    }

    public void printDestination(){
        String output = "Name: " + this.name + " Address: " + this.address + " Description: " + this.description +
                " BucketListName: " + this.bucketListName + " Rating: " + this.getRatings() + " Visits: " + this.visits;
        System.out.println(output);
    }
}
