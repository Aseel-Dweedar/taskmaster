package com.example.taskmaster;

public class TaskLocation {

    private String LocationName;
    private float longitude;
    private float latitude;

    public TaskLocation() {}

    public TaskLocation(String LocationName, float longitude, float latitude) {
        this.LocationName = LocationName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLocationName() {
        return LocationName;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
