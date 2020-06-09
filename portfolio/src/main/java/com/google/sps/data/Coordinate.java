package com.google.sps.data;

public final class Coordinate {
    public final double lat;
    public final double lng;
     
  public Coordinate(String line) {
    String[] cells = line.split(",");
    lat = Double.parseDouble(cells[0]);
    lng = Double.parseDouble(cells[1]);
  }
}