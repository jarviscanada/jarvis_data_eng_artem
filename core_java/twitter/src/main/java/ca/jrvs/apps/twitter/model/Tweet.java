package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Tweet {
  private long id;
  private String idString;
  private String createdAt;
  private String text;
  private Entities entities;
  private Coordinates coordinates;
  private int retweetCount;
  private Integer favoriteCount;
  private boolean favorited;
  private boolean retweeted;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getIdString() {
    return idString;
  }

  @JsonSetter("id_str")
  public void setIdString(String idString) {
    this.idString = idString;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  @JsonSetter("created_at")
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Entities getEntities() {
    return entities;
  }

  public void setEntities(Entities entities) {
    this.entities = entities;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public int getRetweetCount() {
    return retweetCount;
  }

  @JsonSetter("retweet_count")
  public void setRetweetCount(int retweetCount) {
    this.retweetCount = retweetCount;
  }

  public Integer getFavoriteCount() {
    return favoriteCount;
  }

  @JsonSetter("favorite_count")
  public void setFavoriteCount(Integer favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public boolean isFavorited() {
    return favorited;
  }

  public void setFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public boolean isRetweeted() {
    return retweeted;
  }

  public void setRetweeted(boolean retweeted) {
    this.retweeted = retweeted;
  }

  @Override
  public String toString() {
    return "Tweet{" +
        "id=" + id +
        ", idString='" + idString + '\'' +
        ", timestamp='" + createdAt + '\'' +
        ", text='" + text + '\'' +
        ", entities=" + entities +
        ", coordinates=" + coordinates +
        ", retweetCount=" + retweetCount +
        ", favoriteCount=" + favoriteCount +
        ", favorited=" + favorited +
        ", retweeted=" + retweeted +
        '}';
  }
}
