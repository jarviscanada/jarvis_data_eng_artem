package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;


public class TwitterHttpHelperTest {
  private static String CONSUMER_KEY = System.getenv("consumerKey");
  private static String CONSUMER_SECRET = System.getenv("consumerSecret");
  private static String ACCESS_TOKEN = System.getenv("accessToken");
  private static String TOKEN_SECRET = System.getenv("tokenSecret");


  @Test
  public void postRequestMustReturnNewTweetDetails() throws URISyntaxException {
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY,CONSUMER_SECRET,ACCESS_TOKEN,TOKEN_SECRET);
    System.out.println(CONSUMER_KEY+" "+CONSUMER_SECRET+" "+ACCESS_TOKEN+" "+TOKEN_SECRET);
    HttpResponse response = httpHelper.httpPost(
        new URI("https://api.twitter.com/1.1/statuses/update.json?status=hello1"));
    Assert.assertEquals(200,response.getStatusLine().getStatusCode());
  }

  @Test
  public void getRequestMustReturnTweetDetails() throws URISyntaxException {
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY,CONSUMER_SECRET,ACCESS_TOKEN,TOKEN_SECRET);
    System.out.println(CONSUMER_KEY+" "+CONSUMER_SECRET+" "+ACCESS_TOKEN+" "+TOKEN_SECRET);
    HttpResponse response = httpHelper.httpGet(
        new URI("https://api.twitter.com/1.1/statuses/show.json?id=1227065885053128704"));
    Assert.assertEquals(200,response.getStatusLine().getStatusCode());
  }
}