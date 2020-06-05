package com.google.sps.data;

// Represents the physical location of a Food scrap Dropoff in NYC
public final class ScrapDropoff {
  
  private double lat;
  private double lng;

  public ScrapDropoff(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }
}