package com.google.sps.servlets;

import com.google.sps.data.ScrapDropoff;
import com.google.sps.data.Coordinate;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Sends JSON formatted coordinates of dropoff locations to 
// script.js when fetched
@WebServlet("/dropoff-data")
public class DropoffServlet extends HttpServlet {
  
  private Collection<ScrapDropoff> dropoffs;
  
  // Create array list with parse csv values as ScrapDropoff objects
  @Override
  public void init(){
    dropoffs = new ArrayList<>();
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/dropoff-locations.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      Coordinate coord = new Coordinate(line);
      dropoffs.add(new ScrapDropoff(coord.lat, coord.lng));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(dropoffs);
    response.getWriter().println(json);
  }
}