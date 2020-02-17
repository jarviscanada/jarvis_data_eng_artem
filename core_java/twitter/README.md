# Introduction
Twitter App works with Twitter API and is used to create, show and delete tweets. The app was built using Maven, Spring, and Intellij IDEA. It consists of three layers: Controller, Service, and DAO. The app runs using a command-line interface where arguments specify which command must be executed. It was tested using JUnit and Mockito. During the creation of this project, I learned the structure of a real-world application and communication of API using REST architecture. In addition, I learned that there are multiple ways to handle dependencies inside the program, which makes it possible to write the app using different ways.

# Design
![UML diagram](/core_java/twitter/twitterUML.png)

# Quick Start
#### To package the app, Maven command "package" must be executed: <br/>
`mvn package`<br/>

#### To run the app in a command line use a template:<br/>
`TwitterCLI post|show|delete [options]` <br/>
<br/>
#### To create tweet:<br/>
`TwitterCLIpost "post" "tweet_text" "latitude:longitude"` <br/>
<br/>
Arguments: 
1. tweet_text - the text of the tweet (utf-8, max. 140 characters), 
2. latitude:longitude - geo position  
<br/>

#### To show tweet:<br/>
`TwitterCLI show tweet_id [field1,fields2]`<br/>
<br/>
Arguments:
1. tweet_id - the id of a tweet (64bit signed integer)
2. (optional) [field1,field2,...] - fields that will be included in the output tweet (if not provided, all fields will be displayed)
<br/>

#### To delete tweet:<br/>
`TwitterCLI delete [id1,id2,..]`<br/>
<br/>
Argument:<br/>
[id1,id2,...] - ids of tweets that will be deleted<br/>

# Model
The model in this project is a Tweet. Tweet consists of multiple fields:
1. id - 64-bit signed integer that uniquely identifies a tweet
2. str_id - a text version of id
3. created_at - a timestamp, which identifies when the tweet was created
4. text - utf-8 encoded contents of a tweet 
5. coordinates - geo-position which include longitude and latitude of location where the tweet was posted
6. entities:
  * hashtags - details of hashtags that were in the text of the tweet
  * user_mentions - details of the users that were mentioned in the tweet
7. favorited_count - number of likes
8. retweeted_count - number of retweets
9. retweeted - true/false identifies if the tweet was retweeted
10. favorited - true/false identifies if the tweet was liked

# Improvements 
1. Extend the Tweet model in termes of fields
2. Add more options for Tweet manipulations, which will be implemented in the service layer
3. Create exceptions for each type of error that can occur during the execution of the app commands

