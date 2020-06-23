package com.google.sps.data;

// A coordinate object with lat and lng attributes
public final class Coordinate {
    public final double lat;
    public final double lng;
     
  public Coordinate(String line) {
    String[] cells = line.split(",");
    lat = Double.parseDouble(cells[0]);
    lng = Double.parseDouble(cells[1]);
  }
}