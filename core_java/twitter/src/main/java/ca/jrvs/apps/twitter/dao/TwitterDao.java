package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.JsonTranslator;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao<Tweet, String> {

  //URI constraints
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";

  //URI Symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet entity) {
    Tweet tweet = new Tweet();
    URI uri = createUriUsingObject(entity);

    HttpResponse response = httpHelper.httpPost(uri);
    checkStatus(response);

    tweet = extractTweetFromResponseBody(tweet, response);

    return tweet;
  }

  @Override
  public Tweet findById(String id) {
    Tweet tweet = new Tweet();
    URI uri = createUriUsingString(id, "findById");

    //send request
    HttpResponse response = httpHelper.httpGet(uri);

    //translate json object to tweet
    tweet = extractTweetFromResponseBody(tweet, response);
    return tweet;
  }

  @Override
  public Tweet deleteById(String id) {
    Tweet tweet = new Tweet();
    URI uri = createUriUsingString(id, "deleteById");

    HttpResponse response = httpHelper.httpPost(uri);
    checkStatus(response);

    tweet = extractTweetFromResponseBody(tweet, response);

    return tweet;
  }

  public Tweet extractTweetFromResponseBody(Tweet tweet, HttpResponse response) {
    String requestBody;

    if(response.getEntity()!=null){
      try {
        requestBody = EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Unable to extract response body!");
      }
    }else{
      throw new RuntimeException("Response body is empty!");
    }

    try {
      tweet = JsonTranslator
          .toObject(requestBody, Tweet.class, true);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to extract tweet from response body!");
    }
    return tweet;
  }

  public void checkStatus(HttpResponse response) {
    if (response.getStatusLine().getStatusCode() != HTTP_OK) {
      throw new RuntimeException("Unexpected status code!");
    }
  }

  private URI createUriUsingString(String text, String operation) {
    URI uri = null;

    try {
      if (operation == "findById") {
        uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + text);
      } else if (operation == "deleteById") {
        uri = new URI(API_BASE_URI + DELETE_PATH + "/" + text + ".json");
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Unable to create URI. Wrong input!");
    }
    return uri;
  }

  private URI createUriUsingObject(Tweet entity) {
    URI uri = null;
    PercentEscaper percentEscaper = new PercentEscaper("", false);

    //default uri
    String uriText = API_BASE_URI + POST_PATH + QUERY_SYM
        + "status" + EQUAL + percentEscaper.escape(entity.getText());

    //add latitude and longitude to uri if available
    if (entity.getCoordinates() != null) {
      float longitude = entity.getCoordinates().getCoordinates().get(0);
      uriText += AMPERSAND + "long" + EQUAL + longitude;
      float latitude = entity.getCoordinates().getCoordinates().get(1);
      uriText += AMPERSAND + "lat" + EQUAL + latitude;
    }

    try {
      uri = new URI(uriText);
    } catch (URISyntaxException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Unable to create URI. Wrong input!");
    }
    return uri;
  }
}
