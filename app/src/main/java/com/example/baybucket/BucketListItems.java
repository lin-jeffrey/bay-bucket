package com.example.baybucket;

public class BucketListItems {
    private String bucketListItemName, bucketListName;
    private String distance;
    private Boolean visited;
    private String latitude, longitude;


    public BucketListItems(){

    }

    public BucketListItems(String bucketListItemName, String distance, Boolean visited, String latitude, String longitude, String bucketListName){
        this.bucketListItemName = bucketListItemName;
        this.distance = distance;
        this.visited = visited;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bucketListName = bucketListName;

    }

    public String getBucketListItemName() {
        return bucketListItemName;
    }

    public void setBucketListItemName(String bucketListItemName) {
        this.bucketListItemName = bucketListItemName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Boolean getVisited() {
        return visited;
    }
    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public String getLatLng() { return new String(this.latitude + ',' + this.longitude); }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getBucketListName() {
        return bucketListName;
    }
    public void setBucketListName(String bucketListName) {
        this.bucketListName = bucketListName;
    }

}


