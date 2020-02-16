package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Entities;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService twitterService;

  @Test
  public void postTweetMustReturnPostedTweet(){
    Tweet newTweet = new Tweet();
    newTweet.setText("hello world!");

    when(dao.create(any())).thenReturn(new Tweet());
    Tweet tweet = twitterService.postTweet(newTweet);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenTextIsOutOfRange(){
    Tweet newTweet = new Tweet();
    newTweet.setText("hello world!hello world!hello world!hello world!hello world!hello world!"
        + "hello world!hello world!hello world!hello world!hello world!hello world!hello world!");

    Tweet tweet = twitterService.postTweet(newTweet);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenLatitudeOutOfRange(){
    Tweet newTweet = new Tweet();
    newTweet.setText("hello world!");
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(91.0f);
    coordinates.setCoordinates(coordinatesList);
    newTweet.setCoordinates(coordinates);

    Tweet tweet = twitterService.postTweet(newTweet);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenLongitudeOutOfRange(){
    Tweet newTweet = new Tweet();
    newTweet.setText("hello world!");
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(181.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    newTweet.setCoordinates(coordinates);

    Tweet tweet = twitterService.postTweet(newTweet);
  }

  @Test
  public void showTweetMustShowAllFieldsWhenArrayArgumentIsEmpty(){
    Tweet newTweet = new Tweet();
    Coordinates coordinates = new Coordinates();
    Entities entities = new Entities();
    newTweet.setText("hello world!");
    newTweet.setCoordinates(coordinates);
    newTweet.setEntities(entities);
    newTweet.setId(Long.valueOf(1234567890123468091l));
    newTweet.setIdString("1234567890123468091");
    newTweet.setCreatedAt("some time");
    newTweet.setRetweetCount(Integer.valueOf(0));
    newTweet.setFavoriteCount(Integer.valueOf(0));
    newTweet.setFavorited(Boolean.valueOf(false));
    newTweet.setRetweeted(Boolean.valueOf(false));

    when(dao.findById(any())).thenReturn(newTweet);
    Tweet outputTweet = twitterService.showTweet("1234567890123468091",new String[]{});

    assertNotNull(outputTweet.getId());
    assertNotNull(outputTweet.getIdString());
    assertNotNull(outputTweet.getEntities());
    assertNotNull(outputTweet.getCoordinates());
    assertNotNull(outputTweet.getText());
    assertNotNull(outputTweet.getCreatedAt());
    assertNotNull(outputTweet.getFavoriteCount());
    assertNotNull(outputTweet.getRetweetCount());
    assertNotNull(outputTweet.isFavorited());
    assertNotNull(outputTweet.isRetweeted());
  }

  @Test
  public void showTweetMustReturnTweetWithFieldsFromArrayArgument(){
    Tweet newTweet = new Tweet();
    Coordinates coordinates = new Coordinates();
    Entities entities = new Entities();
    newTweet.setText("hello world!");
    newTweet.setCoordinates(coordinates);
    newTweet.setEntities(entities);
    newTweet.setId(Long.valueOf(1234567890123468091l));
    newTweet.setIdString("1234567890123468091");
    newTweet.setCreatedAt("some time");
    newTweet.setRetweetCount(Integer.valueOf(0));
    newTweet.setFavoriteCount(Integer.valueOf(0));
    newTweet.setFavorited(Boolean.valueOf(false));
    newTweet.setRetweeted(Boolean.valueOf(false));

    when(dao.findById(any())).thenReturn(newTweet);
    Tweet outputTweet = twitterService.showTweet("1234567890123468091",new String[]{"text","id","id_str"});

    assertNotNull(outputTweet.getId());
    assertNotNull(outputTweet.getIdString());
    assertNotNull(outputTweet.getText());
    assertNull(outputTweet.getEntities());
    assertNull(outputTweet.getCoordinates());
    assertNull(outputTweet.getCreatedAt());
    assertNull(outputTweet.getFavoriteCount());
    assertNull(outputTweet.getRetweetCount());
    assertNull(outputTweet.isFavorited());
    assertNull(outputTweet.isRetweeted());
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetMustThrowExceptionWhenIdOutOfRange(){
    twitterService.showTweet("12345678901234680",new String[]{"text","id","id_str"});
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetMustThrowExceptionWhenIdHasWrongFormat(){
    twitterService.showTweet("123456789012346809a",new String[]{"text","id","id_str"});
  }

  @Test
  public void deleteByIdMustDeleteTweetsWithIdsInArrayArgument(){
    String[] ids = new String[]{"1234567890123468091","1234567890123468092","1234567890123468091"};

    when(dao.deleteById(any())).thenReturn(new Tweet());
    List<Tweet> deletedTweets = twitterService.deleteTweets(ids);

    assertEquals(3,deletedTweets.size());
  }
}
