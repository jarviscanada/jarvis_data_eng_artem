package ca.jrvs.apps.twitter.dao.helper;

import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TwitterHttpHelper implements HttpHelper {

  private OAuthConsumer consumer;
  private HttpClient httpClient;

  public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken,
      String tokenSecret) {
    consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
    consumer.setTokenWithSecret(accessToken,tokenSecret);

    //httpClient = new DefaultHttpClient();
    httpClient = HttpClientBuilder.create().build();
  }

  public TwitterHttpHelper() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey,consumerSecret);
    consumer.setTokenWithSecret(accessToken,tokenSecret);
    httpClient = new DefaultHttpClient();
  }

  /**
   * Execute a post request using uri
   * @param uri
   * @return json object with new tweet info
   */
  @Override
  public HttpResponse httpPost(URI uri) {
    try {
      return sendRequest(HttpMethod.POST,uri);
    } catch (OAuthException e) {
      throw new RuntimeException("Authentication was not complete",e);
    } catch (IOException e) {
      throw new RuntimeException("Failed to execute",e);
    }
  }

  /**
   * Execute a get request
   * @param uri
   * @return json object with details of the requested tweet
   */
  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      return sendRequest(HttpMethod.GET,uri);
    } catch (OAuthException e) {
      throw new RuntimeException("Authentication was not complete",e);
    } catch (IOException e) {
      throw new RuntimeException("Failed to execute",e);
    }
  }

  /**
   * Send the actual request, depending on the type
   * @param method - Get/Post
   * @param uri
   * @return
   * @throws OAuthException
   * @throws IOException
   */
  private HttpResponse sendRequest(HttpMethod method, URI uri)
      throws OAuthException, IOException {
    if(method == HttpMethod.GET){
      HttpGet getRequest = new HttpGet(uri);
      consumer.sign(getRequest);
      return httpClient.execute(getRequest);
    }else{
      HttpPost postRequest = new HttpPost(uri);
      consumer.sign(postRequest);
      return httpClient.execute(postRequest);
    }
  }
}
