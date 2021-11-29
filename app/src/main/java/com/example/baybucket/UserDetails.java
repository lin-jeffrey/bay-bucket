package com.example.baybucket;

public class UserDetails {
    public String name;
    public String email;
    public String points;
    public UserDetails(){

    }

    public UserDetails(String name, String email, int points){
        this.name = name;
        this.email = email;
        this.points = Integer.toString(points);
    }


}
