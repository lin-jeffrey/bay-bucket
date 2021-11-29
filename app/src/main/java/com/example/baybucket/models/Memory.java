package com.example.baybucket.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import com.example.baybucket.db.Converters;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

@Entity(primaryKeys = {"email","destinationName"})
public class Memory implements Serializable {
    @NonNull
    private String email;
    @NonNull
    private String destinationName;
    @NonNull
    private String coordinates;
    @NonNull
    @TypeConverters({Converters.class})
    private Date checkInDate;
    private String caption;
    private String imageUri;

    public Memory(@NonNull String email, @NonNull String destinationName, @NonNull String coordinates, @NonNull Date checkInDate){
        this.email = email;
        this.destinationName = destinationName;
        this.coordinates = coordinates;
        this.checkInDate = checkInDate;
        this.caption = "";
        this.imageUri = "";
    }

    @Ignore
    public Memory(@NonNull String email, @NonNull String destinationName, @NonNull String coordinates, @NonNull Date checkInDate, String caption, String imageUri){
        this.email = email;
        this.destinationName = destinationName;
        this.coordinates = coordinates;
        this.checkInDate = checkInDate;
        this.caption = caption;
        this.imageUri = imageUri;
    }

    @NonNull
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
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

    public String getCoordinates(){return this.coordinates;}

    public void setCoordinates(String coordinates){this.coordinates = coordinates;}

    public void printMemory(){
        String output = "Email: " + this.email + " DestinationName: " + this.destinationName + " CheckInDate: " + this.checkInDate +
                " Caption: " + this.caption + " ImageUri: " + this.imageUri;
        System.out.println(output);
    }
}
