package com.example.baybucket;

public class BucketListItems {
    private String bucketListItemName;
    private String distance;
    private Boolean visited;

    public BucketListItems(){

    }

    public BucketListItems(String bucketListItemName, String distance, Boolean visited){
        this.bucketListItemName = bucketListItemName;
        this.distance = distance;
        this.visited = visited;
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
}


