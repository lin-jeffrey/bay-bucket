package com.example.baybucket.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.baybucket.db.Converters;

import java.io.Serializable;
import java.util.Date;

@Entity(primaryKeys = {"username","destinationName"})
public class Memory implements Serializable {
    @NonNull
    private String username;
    @NonNull
    private String destinationName;
    @NonNull
    @TypeConverters({Converters.class})
    private Date checkInDate;
    private String caption;
    private String imageUri;

    public Memory(@NonNull String username, @NonNull String destinationName, @NonNull Date checkInDate){
        this.username = username;
        this.destinationName = destinationName;
        this.checkInDate = checkInDate;
        this.caption = "";
        this.imageUri = "";
    }

    @Ignore
    public Memory(@NonNull String username, @NonNull String destinationName, @NonNull Date checkInDate, String caption, String imageUri){
        this.username = username;
        this.destinationName = destinationName;
        this.checkInDate = checkInDate;
        this.caption = caption;
        this.imageUri = imageUri;
    }

    @NonNull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getDestinationName() {
        return this.destinationName;
    }

    public void setDestinationName(@NonNull String destinationName) {
        this.destinationName = destinationName;
    }

    @NonNull
    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public void setCheckInDate(@NonNull Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption){
        this.caption = caption;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String imageUri){
        this.imageUri = imageUri;
    }

    public void printMemory(){
        String output = "Username: " + this.username + " DestinationName: " + this.destinationName + " CheckInDate: " + this.checkInDate +
                " Caption: " + this.caption + " ImageUri: " + this.imageUri;
        System.out.println(output);
    }
}
