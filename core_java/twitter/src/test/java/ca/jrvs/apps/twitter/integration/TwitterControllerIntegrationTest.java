package ca.jrvs.apps.twitter.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntegrationTest {
  TwitterController controller;
  TwitterService service;
  TwitterDao dao;

  @Before
  public void init(){
    HttpHelper httpHelper = new TwitterHttpHelper(System.getenv("consumerKey"),
        System.getenv("consumerSecret"),
        System.getenv("accessToken"),
        System.getenv("tokenSecret"));
    dao = new TwitterDao(httpHelper);
    service = new TwitterService(dao);
    controller = new TwitterController(service);
  }

  @Test
  public void postTweetMustPostNewTweet(){
    String method = "post";
    String text = "This is the text of new tweet #newtweet @aaaua";
    String coordinates = "43:79";
    String[] args = new String[]{method,text,coordinates};

    Tweet newTweet = controller.postTweet(args);

    assertNotNull(newTweet.getId());
    assertNotNull(coordinates);
    assertEquals(text,newTweet.getText());
  }

  @Test
  public void showTweetMustReturnTweetWithRequestedFields(){
    String method = "show";
    String id = controller.postTweet(new String[]{"post","tweet that will be shown","43:79"}).getIdString();
    String fields = "id,coordinates,text,favorited";
    String[] args = new String[]{method,id,fields};

    Tweet tweet = controller.showTweet(args);

    assertNotNull(tweet.getId());
    assertNotNull(tweet.getCoordinates());
    assertNotNull(tweet.getText());
    assertNotNull(tweet.isFavorited());
    assertNull(tweet.getIdString());
    assertNull(tweet.getFavoriteCount());
  }

  @Test
  public void deleteTweetMustDeleteAllTweetsWithProvidedIds(){
    String method = "delete";
    String id = controller.postTweet(new String[]{"post","tweet that will be deleted first","43:79"}).getIdString();
    String id2 = controller.postTweet(new String[]{"post","tweet that will be deleted second","43:79"}).getIdString();
    String id3 = controller.postTweet(new String[]{"post","tweet that will be deleted third","43:79"}).getIdString();
    String ids = id+","+id2+","+id3;
    String[] args = new String[]{method,ids};

    List<Tweet> deletedTweets = controller.deleteTweet(args);

    assertNull(controller.showTweet(new String[]{method,id}).getId());
    assertNull(controller.showTweet(new String[]{method,id2}).getId());
    assertNull(controller.showTweet(new String[]{method,id3}).getId());

    assertNotNull(deletedTweets.get(0).getId());
    assertNotNull(deletedTweets.get(1).getId());
    assertNotNull(deletedTweets.get(2).getId());
  }
}
