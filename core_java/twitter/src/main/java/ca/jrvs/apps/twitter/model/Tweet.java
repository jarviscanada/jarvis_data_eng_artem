package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Tweet {
  private Long id;
  private String idString;
  private String createdAt;
  private String text;
  private Entities entities;
  private Coordinates coordinates;
  private Integer retweetCount;
  private Integer favoriteCount;
  private Boolean favorited;
  private Boolean retweeted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public Integer getRetweetCount() {
    return retweetCount;
  }

  @JsonSetter("retweet_count")
  public void setRetweetCount(Integer retweetCount) {
    this.retweetCount = retweetCount;
  }

  public Integer getFavoriteCount() {
    return favoriteCount;
  }

  @JsonSetter("favorite_count")
  public void setFavoriteCount(Integer favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public Boolean isFavorited() {
    return favorited;
  }

  public void setFavorited(Boolean favorited) {
    this.favorited = favorited;
  }

  public Boolean isRetweeted() {
    return retweeted;
  }

  public void setRetweeted(Boolean retweeted) {
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
