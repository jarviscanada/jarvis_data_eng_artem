package ca.jrvs.apps.twitter.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntegrationTest {
  TwitterDao twitterDao;
  //@Autowired
  TwitterService twitterService;

  @Before
  public void init(){
    HttpHelper httpHelper = new TwitterHttpHelper(System.getenv("consumerKey"),
        System.getenv("consumerSecret"),
        System.getenv("accessToken"),
        System.getenv("tokenSecret"));
    twitterDao = new TwitterDao(httpHelper);
    twitterService = new TwitterService(twitterDao);
  }

  @Test
  public void postTweetMustPostTweetAndReturnIt(){
    Tweet tweet = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();

    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);

    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    tweet.setText("this is example tweet posted from service "+hashtag+" @aaaua5");

    Tweet createdTweet = twitterService.postTweet(tweet);

    assertNotNull(createdTweet.getId());
    assertNotNull(createdTweet.getIdString());

    assertEquals(tweet.getText(),createdTweet.getText());

    assertEquals(tweet.getCoordinates().getCoordinates().size(),
        createdTweet.getCoordinates().getCoordinates().size());
    assertEquals(tweet.getCoordinates().getCoordinates().get(0),
        createdTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(tweet.getCoordinates().getCoordinates().get(1),
        createdTweet.getCoordinates().getCoordinates().get(1));

    assertTrue(hashtag.contains(createdTweet.getEntities().getHashtags().get(0).getText()));
    assertEquals(1,createdTweet.getEntities().getMentions().size());
  }

  @Test
  public void showTweetMustShowExistentTweet() {
    //create and post tweet that will be searched
    Tweet tweet = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    tweet.setText("this is another example of a tweet from service "+hashtag+" @aaaua5");
    tweet = twitterService.postTweet(tweet);

    //find the tweet
    Tweet foundTweet = twitterService.showTweet(tweet.getIdString(), new String[]{});

    //compare found tweet with posted
    assertEquals(tweet.getText(),foundTweet.getText());

    assertEquals(tweet.getCoordinates().getCoordinates().size(),
        foundTweet.getCoordinates().getCoordinates().size());
    assertEquals(tweet.getCoordinates().getCoordinates().get(0),
        foundTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(tweet.getCoordinates().getCoordinates().get(1),
        foundTweet.getCoordinates().getCoordinates().get(1));

    assertTrue(hashtag.contains(foundTweet.getEntities().getHashtags().get(0).getText()));
    assertEquals(1,foundTweet.getEntities().getMentions().size());
  }

  @Test
  public void deleteTweetsMustDeleteExistentTweets(){
    String[] listOfIds = new String[2];

    Tweet tweet1 = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    tweet1.setCoordinates(coordinates);
    tweet1.setText("this tweet will be deleted from service "+hashtag+" @aaaua5");
    tweet1 = twitterService.postTweet(tweet1);
    listOfIds[0] = tweet1.getIdString();

    Tweet tweet2 = new Tweet();
    coordinates.setCoordinates(coordinatesList);
    tweet2.setCoordinates(coordinates);
    tweet2.setText("this tweet will also be deleted from service "+hashtag+" @aaaua5");
    tweet2 = twitterService.postTweet(tweet2);
    listOfIds[1] = tweet2.getIdString();

    List<Tweet> deletedTweets = twitterService.deleteTweets(listOfIds);


    assertEquals(2,deletedTweets.size());

    //compare deleted tweet with the one that was posted
    assertEquals(tweet1.getText(),deletedTweets.get(0).getText());

    assertEquals(tweet1.getCoordinates().getCoordinates().size(),
        deletedTweets.get(0).getCoordinates().getCoordinates().size());
    assertEquals(tweet1.getCoordinates().getCoordinates().get(0),
        deletedTweets.get(0).getCoordinates().getCoordinates().get(0));
    assertEquals(tweet1.getCoordinates().getCoordinates().get(1),
        deletedTweets.get(0).getCoordinates().getCoordinates().get(1));

    assertTrue(hashtag.contains(deletedTweets.get(0).getEntities().getHashtags().get(0).getText()));
    assertEquals(1,deletedTweets.get(0).getEntities().getMentions().size());
  }
}
