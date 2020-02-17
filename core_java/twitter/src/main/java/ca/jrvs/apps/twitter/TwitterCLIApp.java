package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwitterCLIApp {
  private Controller controller;

  @Autowired
  public TwitterCLIApp(Controller controller) {
    this.controller = controller;
  }

  public static void main(String[] args) {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    HttpHelper helper = new TwitterHttpHelper(consumerKey,consumerSecret,accessToken,tokenSecret);
    CrdDao dao = new TwitterDao(helper);
    Service service = new TwitterService(dao);
    Controller controller = new TwitterController(service);
    TwitterCLIApp app = new TwitterCLIApp(controller);

    app.run(args);
  }

  public void run(String[] args) {
    String method;

    if(args.length<1){
      throw new IllegalArgumentException("Wrong number of arguments!");
    }else {
      method = args[0];
      if(method.equals("post")){
        printTweet(controller.postTweet(args));
      }else if(method.equals("show")){
        printTweet(controller.showTweet(args));
      }else if(method.equals("delete")){
        controller.deleteTweet(args).forEach(this::printTweet);
      }else {
        throw new IllegalArgumentException("Wrong method name!");
      }
    }
  }

  private void printTweet(Tweet tweet){
    try{
      System.out.println(JsonTranslator.toJson(tweet,true,false));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }
}
