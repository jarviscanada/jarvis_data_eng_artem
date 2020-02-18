package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

public class Entities {
  private List<Hashtag> hashtags;
  private List<UserMentions> mentions;

  public List<Hashtag> getHashtags() {
    return hashtags;
  }

  public void setHashtags(List<Hashtag> hashtags) {
    this.hashtags = hashtags;
  }

  public List<UserMentions> getMentions() {
    return mentions;
  }

  @JsonSetter("user_mentions")
  public void setMentions(List<UserMentions> mentions) {
    this.mentions = mentions;
  }

  @Override
  public String toString() {
    return "Entities{" +
        "hashtags=" + hashtags +
        ", mentions=" + mentions +
        '}';
  }
}
