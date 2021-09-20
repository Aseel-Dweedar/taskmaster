package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the Location type in your schema. */
public final class Location {
  private final String name;
  private final Double longitude;
  private final Double latitude;
  public String getName() {
      return name;
  }
  
  public Double getLongitude() {
      return longitude;
  }
  
  public Double getLatitude() {
      return latitude;
  }
  
  public Location(String name, Double longitude, Double latitude) {
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Location location = (Location) obj;
      return ObjectsCompat.equals(getName(), location.getName()) &&
              ObjectsCompat.equals(getLongitude(), location.getLongitude()) &&
              ObjectsCompat.equals(getLatitude(), location.getLatitude());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getName())
      .append(getLongitude())
      .append(getLatitude())
      .toString()
      .hashCode();
  }
  
  public static BuildStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(name,
      longitude,
      latitude);
  }
  public interface BuildStep {
    Location build();
    BuildStep name(String name);
    BuildStep longitude(Double longitude);
    BuildStep latitude(Double latitude);
  }
  

  public static class Builder implements BuildStep {
    private String name;
    private Double longitude;
    private Double latitude;
    @Override
     public Location build() {
        
        return new Location(
          name,
          longitude,
          latitude);
    }
    
    @Override
     public BuildStep name(String name) {
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }
    
    @Override
     public BuildStep latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String name, Double longitude, Double latitude) {
      super.name(name)
        .longitude(longitude)
        .latitude(latitude);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder longitude(Double longitude) {
      return (CopyOfBuilder) super.longitude(longitude);
    }
    
    @Override
     public CopyOfBuilder latitude(Double latitude) {
      return (CopyOfBuilder) super.latitude(latitude);
    }
  }
  
}
