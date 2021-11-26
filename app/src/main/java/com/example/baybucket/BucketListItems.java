package com.example.baybucket;

public class BucketListItems {
    private String bucketListItemName;
    private String distance;

    public BucketListItems(){

    }

    public BucketListItems(String bucketListItemName, String distance){
        this.bucketListItemName = bucketListItemName;
        this.distance = distance;
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
}


