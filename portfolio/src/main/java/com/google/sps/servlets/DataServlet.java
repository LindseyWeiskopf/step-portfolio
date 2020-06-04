// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private ArrayList<String> names;
  private ArrayList<String> emails; 
  private ArrayList<String> comments;

  private ArrayList<String> messages;
 
  @Override
  public void init() {
    names = new ArrayList<>();
    emails = new ArrayList<>();
    comments = new ArrayList<>();

    messages = new ArrayList<>();
    messages.add("Howdy");
    messages.add("Hey, how are you?");
    messages.add("Good morning, friend!");
  }
  
  /** Gets information from the server to display to the user. Picks a random message from the 
  messages arraylist and sends the response instance to the screen */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = messages.get((int) (Math.random() * messages.size()));
    response.setContentType("text/html;");
    response.getWriter().println(message);
  }
  
  /** Posts information to the server to save as inputs from the data form */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("name-input");
    String email = request.getParameter("email-input");
    String comment = request.getParameter("comment-input");

    response.sendRedirect("http://lweiskopf-step-2020.appspot.com/contact.html");
    
    names.add(name);
    emails.add(email);
    comments.add(comment);
  }
}
