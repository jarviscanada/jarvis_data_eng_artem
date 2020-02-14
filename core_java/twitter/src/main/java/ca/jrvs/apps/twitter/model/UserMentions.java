package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Arrays;

public class UserMentions {
  private long id;
  private String idString;
  private int[] indices;
  private String name;
  private String screenName;

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

  public int[] getIndices() {
    return indices;
  }

  public void setIndices(int[] indices) {
    this.indices = indices;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getScreenName() {
    return screenName;
  }
  @JsonSetter("screen_name")
  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  @Override
  public String toString() {
    return "UserMentions{" +
        "id=" + id +
        ", idString='" + idString + '\'' +
        ", indices=" + Arrays.toString(indices) +
        ", name='" + name + '\'' +
        ", screenName='" + screenName + '\'' +
        '}';
  }
}
