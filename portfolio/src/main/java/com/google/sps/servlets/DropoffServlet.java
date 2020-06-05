package com.google.sps.servlets;

import com.google.sps.data.ScrapDropoff;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Will send JSON formatted coordinates of dropoff locations to 
// script.js when fetched
@WebServlet("/dropoff-data")
public class DropoffServlet extends HttpServlet {
  
  private List<ScrapDropoff> dropoffs;
  
  @Override
  public void init(){
    dropoffs = new ArrayList<ScrapDropoff>;
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/dropoff-locations.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      double lat = Double.parseDouble(cells[0]);
      double lng = Double.parseDouble(cells[1]);

      dropoffs.add(new ScrapDropoff(lat, lng));
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