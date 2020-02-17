package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
import java.util.List;

public class TwitterController implements Controller {
  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private Service service;

  //@Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  @Override
  public Tweet postTweet(String[] args) {
    checkNumberOfArguments("post",args);

    String text = args[1];
    String coordinatesString = args[2];

    if(text==null || text.length()==0){
      throw new IllegalArgumentException("Tweet text cannot be empty!");
    }else if(coordinatesString ==null || coordinatesString.length()==0){
      throw new IllegalArgumentException("Tweet coordinates cannot be empty!");
    }

    Float longitude;
    Float latitude;

    try{
      String[] longAndLat = coordinatesString.split(COORD_SEP);
      longitude = Float.valueOf(longAndLat[1]);
      latitude = Float.valueOf(longAndLat[0]);
    }catch(Exception e){
      throw new IllegalArgumentException("Wrong coordinates format! Tweet coordinates must be in format [latitude:longitude]!");
    }

    Tweet tweet = buildTweet(text, longitude, latitude);

    return service.postTweet(tweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    checkNumberOfArguments("show",args);

    String tweetId = args[1];
    String[] fields = new String[]{};

    if(tweetId==null || tweetId.length()==0){
      throw new IllegalArgumentException("Tweet id cannot be empty!");
    }

    if(args.length==3){
      if(args[2].length()==0){
        throw new IllegalArgumentException("If provided, fields cannot be empty");
      }else{
        fields = args[2].split(COMMA);
      }
    }

    return service.showTweet(tweetId,fields);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    checkNumberOfArguments("delete",args);

    String idsString = args[1];

    if(idsString==null || idsString.length()==0){
      throw new IllegalArgumentException("Tweet ids list cannot be empty!");
    }

    String[] ids = idsString.split(COMMA);

    return service.deleteTweets(ids);
  }

  private void checkNumberOfArguments(String method, String[] args) {
    if(method.equals("show")){
      if(args.length < 2 || args.length > 3){
        throw new IllegalArgumentException("Invalid number of arguments!");
      }
    }else if(method.equals("post")){
      if(args.length!=3){
        throw new IllegalArgumentException("Invalid number of arguments!");
      }
    }else{
      //method is "delete"
      if(args.length!=2){
        throw new IllegalArgumentException("Invalid number of arguments!");

      }
    }
  }

  private Tweet buildTweet(String text, Float longitude, Float latitude) {
    Tweet tweet = new Tweet();
    Coordinates tweetCoordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();

    tweet.setText(text);
    coordinatesList.add(longitude);
    coordinatesList.add(latitude);
    tweetCoordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(tweetCoordinates);

    return tweet;
  }
}
