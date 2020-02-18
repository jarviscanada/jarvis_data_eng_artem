package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.pool.TypePool.Resolution.Illegal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  @Test
  public void postTweetMustPostAndReturnNewTweet(){
    Tweet newTweet = new Tweet();
    newTweet.setText("hello world");
    when(service.postTweet(any())).thenReturn(newTweet);

    Tweet tweet = controller.postTweet(new String[]{"post","new text","43:79"});

    assertNotNull(tweet.getText());
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenArgumentNumberIsWrong(){
    controller.postTweet(new String[]{"post","new text"});
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenTextIsEmpty(){
    controller.postTweet(new String[]{"post","","43:79"});
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenCoordinatesAreEmpty(){
    controller.postTweet(new String[]{"post","new text",""});
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetMustThrowExceptionWhenCoordinatesAreInWrongFormat(){
    Tweet tweet = controller.postTweet(new String[]{"post","new text",":79"});
  }

  @Test
  public void showTweetMustReturnRequestedTweet(){
    Tweet newTweet = new Tweet();
    newTweet.setIdString("1234567890123456789");
    newTweet.setText("hello world");
    when(service.showTweet(any(),any())).thenReturn(newTweet);

    Tweet tweet = controller.showTweet(new String[]{"post","1234567890123456789"});

    assertEquals(newTweet.getText(),tweet.getText());
    assertEquals(newTweet.getIdString(),tweet.getIdString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetMustThrowExceptionWhenNumberOfArgumentsIsWrong(){
    controller.showTweet(new String[]{"post"});
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetMustThrowExceptionWhenIdIsEmpty(){
    controller.showTweet(new String[]{"post",""});
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetMustThrowExceptionWhenFieldsAreProvidedAndEmpty(){
    controller.showTweet(new String[]{"post","1234567890123456789",""});
  }

  @Test
  public void deleteTweetMustDeleteTweetsAndReturnThem(){
    List<Tweet> deletedTweets = new ArrayList<Tweet>();
    deletedTweets.add(new Tweet());
    deletedTweets.add(new Tweet());

    when(service.deleteTweets(any())).thenReturn(deletedTweets);

    Tweet tweet = controller.showTweet(new String[]{"post","1234567890123456789,1234567890123446789"});
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetMustThrowExceptionWhenArgumentsNumberIsWrong(){
    controller.showTweet(new String[]{"post"});
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetMustThrowExceptionWhenIdListIsEmpty(){
    controller.showTweet(new String[]{"post",""});
  }
}
