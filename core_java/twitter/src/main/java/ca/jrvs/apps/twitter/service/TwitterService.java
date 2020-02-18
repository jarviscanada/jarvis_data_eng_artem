package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class TwitterService implements Service {
  private CrdDao dao;

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  @Override
  public Tweet postTweet(Tweet tweet) {

    validatePostTweet(tweet);

    return (Tweet) dao.create(tweet);
  }

  private void validatePostTweet(Tweet tweet) {
    if(tweet.getText().length()>140){
      throw new IllegalArgumentException("Text exceeds maximum length (max 140 characters)");
    }
    if(tweet.getCoordinates()!=null){
      float latitude = tweet.getCoordinates().getCoordinates().get(1);
      float longitude = tweet.getCoordinates().getCoordinates().get(0);

      if(latitude > 90 || latitude < -90){
        throw new IllegalArgumentException("latitude out of range");
      }else if(longitude > 180 || longitude < -180){
        throw new IllegalArgumentException("longitude out of range");
      }
    }
  }

  @Override
  public Tweet showTweet(String id, String[] fields) {
    validateTweetId(id);
    Tweet tweet = (Tweet) dao.findById(id);

    if(fields.length!=0){
      List fieldsList = Arrays.asList(fields);

      if(!fieldsList.contains("id")){ tweet.setId(null); }
      if(!fieldsList.contains("id_str")){ tweet.setIdString(null); }
      if(!fieldsList.contains("text")){ tweet.setText(null); }
      if(!fieldsList.contains("coordinates")){ tweet.setCoordinates(null); }
      if(!fieldsList.contains("entities")){ tweet.setEntities(null); }
      if(!fieldsList.contains("retweet_count")){ tweet.setRetweetCount(null); }
      if(!fieldsList.contains("favorite_count")){ tweet.setFavoriteCount(null); }
      if(!fieldsList.contains("favorited")){ tweet.setFavorited(null); }
      if(!fieldsList.contains("retweeted")){ tweet.setRetweeted(null); }
      if(!fieldsList.contains("created_at")){ tweet.setCreatedAt(null); }
    }

    return tweet;
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> deletedTweets = new ArrayList<Tweet>();

    Arrays.stream(ids).forEach(id -> {
      validateTweetId(id);
      deletedTweets.add((Tweet) dao.deleteById(id));
    });
    return deletedTweets;
  }

  private void validateTweetId(String id) {
    if(id.length()!=19){
      throw new IllegalArgumentException("Id is out of range! Must be 64-bit signed integer");
    }
    else if(!id.matches("[0-9]+")){
      throw new IllegalArgumentException("Id is not numeric! Must be 64-bit signed integer");
    }
  }
}
