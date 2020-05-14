package ca.jrvs.apps.twitter.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntegrationTest {
  TwitterDao twitterDao;

  @Before
  public void init(){
    HttpHelper httpHelper = new TwitterHttpHelper(System.getenv("consumerKey"),
        System.getenv("consumerSecret"),
        System.getenv("accessToken"),
        System.getenv("tokenSecret"));
    twitterDao = new TwitterDao(httpHelper);
  }

  @Test
  public void createMustReturnNewlyPostedTweet() {
    //create a new tweet with coordinates
    Tweet tweet = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();

    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);

    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    tweet.setText("this is example tweet "+hashtag+" @aaaua5");

    //send a post request
    Tweet createdTweet = twitterDao.create(tweet);

    //check if created tweet is correct
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
  public void findByIdShouldReturnCorrectTweet() {
    //create and post tweet that will be searched
    Tweet tweet = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    tweet.setText("this is another example of a tweet "+hashtag+" @aaaua5");
    tweet = twitterDao.create(tweet);

    //find the tweet
    Tweet foundTweet = twitterDao.findById(tweet.getIdString());

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
  public void deleteMustReturnDeletedTweet() {
    //create and post tweet that will be deleted
    Tweet tweet = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    tweet.setText("this tweet will be deleted "+hashtag+" @aaaua5");
    tweet = twitterDao.create(tweet);

    Tweet deletedTweet = twitterDao.deleteById(tweet.getIdString());

    //make sure tweet is deleted
    assertEquals(null,twitterDao.findById(deletedTweet.getIdString()).getId());

    //compare deleted tweet with the one that was posted
    assertEquals(tweet.getText(),deletedTweet.getText());

    assertEquals(tweet.getCoordinates().getCoordinates().size(),
        deletedTweet.getCoordinates().getCoordinates().size());
    assertEquals(tweet.getCoordinates().getCoordinates().get(0),
        deletedTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(tweet.getCoordinates().getCoordinates().get(1),
        deletedTweet.getCoordinates().getCoordinates().get(1));

    assertTrue(hashtag.contains(deletedTweet.getEntities().getHashtags().get(0).getText()));
    assertEquals(1,deletedTweet.getEntities().getMentions().size());
  }

}
