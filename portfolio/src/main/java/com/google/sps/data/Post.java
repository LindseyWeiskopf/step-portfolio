package com.google.sps.data;

public final class Post {
  private final long id;
  private final String name;
  private final String email;
  private final String comment;
  private final long timestamp;

  public Post(long id, String name, String email, String comment, long timestamp) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.comment = comment;
    this.timestamp = timestamp;
  }
}