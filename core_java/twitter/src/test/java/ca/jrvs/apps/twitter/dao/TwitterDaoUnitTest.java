package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.JsonTranslator;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Entities;
import ca.jrvs.apps.twitter.model.Hashtag;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.model.UserMentions;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {
  @Mock
  HttpHelper httpHelper;

  @InjectMocks
  TwitterDao twitterDao;

  @Test(expected = RuntimeException.class)
  public void createMustThrowExceptionWhenRequestWasNotSent(){
    Tweet tweet = new Tweet();
    String hashtag = "#exampletweet";
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    tweet.setText("this new test tweet "+hashtag+" @aaaua5");

    when(httpHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("failed to execute"));

    twitterDao.create(tweet);
  }

  @Test
  public void createMustReturnCreatedTweet(){
    Tweet tweet = new Tweet();
    tweet.setId(12231421421413241l);
    tweet.setIdString("12231421421413241");
    tweet.setText("hello its me");
    TwitterDao spyDao = spy(twitterDao);

    when(httpHelper.httpPost(isNotNull())).thenReturn(null);
    doReturn(tweet).when(spyDao).extractTweetFromResponseBody(any(), any());
    doNothing().when(spyDao).checkStatus(any());

    Tweet createdTweet = spyDao.create(tweet);
    assertNotNull(createdTweet);
    assertEquals(tweet.getId(), createdTweet.getId());
    assertEquals(tweet.getText(), createdTweet.getText());
  }

  @Test(expected = RuntimeException.class)
  public void findByIdMustThrowExceptionWhenTweetDoesNotExist() {
    TwitterDao spyDao = spy(twitterDao);
    when(httpHelper.httpGet(isNotNull())).thenReturn(null);

    spyDao.findById("0");
  }

  @Test
  public void findByIdMustReturnExistentTweet() {
    Tweet tweet = new Tweet();
    tweet.setId(12231421421413241l);
    tweet.setIdString("12231421421413241");
    tweet.setText("hello its me");
    TwitterDao spyDao = spy(twitterDao);

    when(httpHelper.httpGet(isNotNull())).thenReturn(null);
    doReturn(tweet).when(spyDao).extractTweetFromResponseBody(any(), any());

    Tweet foundTweet = spyDao.findById(tweet.getIdString());
    assertNotNull(foundTweet);
    assertEquals(tweet.getId(),foundTweet.getId());
    assertEquals(tweet.getText(),foundTweet.getText());
  }

  @Test(expected = RuntimeException.class)
  public void deleteByIdMustThrowExceptionWhenTweetDoesNotExist(){
    TwitterDao spyDao = spy(twitterDao);

    when(httpHelper.httpPost(isNotNull())).thenReturn(null);
    doThrow(new RuntimeException("Unexpected status code!")).when(spyDao).checkStatus(any());

    spyDao.deleteById("0");
  }

  @Test
  public void deleteByIdMustReturnDeletedTweet() {
    Tweet tweet = new Tweet();
    tweet.setId(12231421421413241l);
    tweet.setIdString("12231421421413241");
    tweet.setText("hello its me again");
    TwitterDao spyDao = spy(twitterDao);

    when(httpHelper.httpPost(isNotNull())).thenReturn(null);
    doNothing().when(spyDao).checkStatus(any());
    doReturn(tweet).when(spyDao).extractTweetFromResponseBody(any(), any());


    Tweet deletedTweet = spyDao.deleteById(tweet.getIdString());
    assertNotNull(deletedTweet);
    assertEquals(tweet.getId(), deletedTweet.getId());
    assertEquals(tweet.getText(), deletedTweet.getText());
  }

  @Test(expected = RuntimeException.class)
  public void extractTweetFromResponseBodyMustThrowExceptionWhenResponseBodyIsEmpty() {
    HttpResponseFactory factory = new DefaultHttpResponseFactory();
    HttpResponse response = factory.newHttpResponse(
        new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, null),
        null);
    twitterDao.extractTweetFromResponseBody(new Tweet(),response);
  }

  @Test
  public void extractTweetMustReturnCorrectTweet() throws JsonProcessingException {
    HttpResponseFactory factory = new DefaultHttpResponseFactory();
    HttpResponse response = factory.newHttpResponse(
        new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, null),
        null);

    //create tweet
    Tweet tweet = new Tweet();
    tweet.setId(1227999693692719107l);
    tweet.setIdString("1227999693692719107");
    tweet.setText("this is another example of a tweet #exampletweet @aaaua5");
    Coordinates coordinates = new Coordinates();
    List<Float> coordinatesList = new ArrayList<Float>();
    coordinatesList.add(79.0f);
    coordinatesList.add(43.0f);
    coordinates.setCoordinates(coordinatesList);
    tweet.setCoordinates(coordinates);
    Entities entities = new Entities();
    List<Hashtag> hashtags = new ArrayList<Hashtag>();
    Hashtag hashtag = new Hashtag();
    hashtag.setText("exampletweet");
    hashtag.setIndices(new int[]{35,48});
    hashtags.add(hashtag);
    entities.setHashtags(hashtags);
    List<UserMentions> userMentions = new ArrayList<UserMentions>();
    UserMentions userMention = new UserMentions();
    userMention.setId(1081419028109971456l);
    userMention.setIdString("1081419028109971456");
    userMention.setScreenName("aaaua5");
    userMention.setName("aaaua5");
    userMention.setIndices(new int[]{49,56});
    userMentions.add(userMention);
    entities.setMentions(userMentions);
    tweet.setEntities(entities);


    response.setEntity(
        EntityBuilder.create().setText(JsonTranslator.toJson(tweet,false,true))
            .setContentType(ContentType.APPLICATION_JSON).build());

    Tweet extractedTweet = twitterDao.extractTweetFromResponseBody(new Tweet(),response);

    assertEquals(tweet.getId(),extractedTweet.getId());
    assertEquals(tweet.getIdString(),extractedTweet.getIdString());
    assertEquals(tweet.getText(),extractedTweet.getText());
    assertEquals(tweet.getCoordinates().getCoordinates(),extractedTweet.getCoordinates().getCoordinates());
    assertEquals(tweet.getEntities().getHashtags().get(0).getText(),
        extractedTweet.getEntities().getHashtags().get(0).getText());
    assertEquals(tweet.getEntities().getMentions().get(0).getScreenName(),
        extractedTweet.getEntities().getMentions().get(0).getScreenName());
  }
}