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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import com.google.sps.data.Post;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.FetchOptions;

// Servlet that returns some example content. 
@WebServlet("/comments")
public class DataServlet extends HttpServlet {
  
  private final int DEFAULT_QUANTITY = 10;
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    
    List<Post> posts = entityToList(results, request);
    
    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(convertToJson(posts));
  }
  
  // Posts information to the server to save as inputs from the data form
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Entity commentEntity = createEntity(request);
  

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/contact.html");
  }
  
  private String getLanguage(HttpServletRequest request) {
    String languageChoice = request.getParameter("language");
    return languageChoice;
  }

  private int getCommentNum(HttpServletRequest request) {
    String numChoiceString = request.getParameter("quantity");
   
    // Convert the input to an int.
    int commentNum;
    try {
      commentNum = Integer.parseInt(numChoiceString);
    } catch (NumberFormatException e) {
      return DEFAULT_QUANTITY;
    }
    return commentNum;
  }

  private String convertToJson(List<Post> posts) {
    Gson gson = new Gson();
    String json = gson.toJson(posts);
    return json;
  }

  private Entity createEntity(HttpServletRequest request) {

    String name = request.getParameter("name-input");
    String email = request.getParameter("email-input");
    String comment = request.getParameter("comment-input");
    long timestamp = System.currentTimeMillis();   

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("email", email);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("timestamp", timestamp);
    return commentEntity;
  }
  
  private List<Post> entityToList(PreparedQuery results, HttpServletRequest request) {

    List<Post> posts = new ArrayList<>();

    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(getCommentNum(request)))) {
      long id = entity.getKey().getId();
      long timestamp = (long) entity.getProperty("timestamp");
      String name = (String) entity.getProperty("name");
      String email = (String) entity.getProperty("email");
      //String comment = (String) entity.getProperty("comment");
      String comment = translateComment(request, entity);
      Post post = new Post(id, name, email, comment, timestamp);
     
      posts.add(post);
    }  
    return posts;  
  }

  private String translateComment(HttpServletRequest request, Entity entity) {
    String originalComment = (String) entity.getProperty("comment");
    String languageChoice = getLanguage(request);

    // Do the translation.
    Translate translate = TranslateOptions.getDefaultInstance().getService();
    Translation translation =
        translate.translate(originalComment, Translate.TranslateOption.targetLanguage(languageChoice));
    String translatedText = translation.getTranslatedText();
    return translatedText;
  }
}
