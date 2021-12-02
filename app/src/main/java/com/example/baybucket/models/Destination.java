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
    @NonNull
    private String bucketListName;

    public Destination(@NonNull String name, @NonNull String bucketListName){
        this.name = name;
        this.bucketListName = bucketListName;
    }

    @NonNull
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

    public void printDestination(){
        String output = "Name: " + this.name + " BucketListName: " + this.bucketListName;
        System.out.println(output);
    }
}
