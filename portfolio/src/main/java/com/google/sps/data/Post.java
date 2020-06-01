package com.google.sps.data;

public final class Post {
  private final String name;
  private final String email;
  private final String comment;

  public Post(String name, String email, String comment) {
    this.name = name;
    this.email = email;
    this.comment = comment;
  }
}