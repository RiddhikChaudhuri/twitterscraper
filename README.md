#### Execute below mentioned statement to create database, application user & granting permission

** Creates the new database **

	create database twitter_scraper;

** Creates the user **

	create user 'springuser'@'%' identified by 'ThePassword';

**  Gives all privileges to the new user on the newly created database **

	grant all on twitter_scraper.* to 'springuser'@'%';

	
** Create the Image directory for storing images and update below-mentioned property in the application.properties **
	
	image.directory



-------------------------

Technologies:
- Java
- Spring boot
- MySQL
- Twitter API

This spring boot service shall poll a channel on twitter (configurable) every hour and update the content in the database (MySQL).
The content shall include:
- Number of followers
- All recent tweets made in the channel including re-tweets.
  Each tweet shall include:
- ID of tweet
- Date of tweet
- Name of tweeter
- Link of tweeter
- Image data (Base64) of tweeter's profile image
- Tweet text and image data (Base64)
- Number of comments
- Number of re-tweets
- Number of likes
- Link to tweet
- Name of original tweeter (for re-tweets)
- Link to original tweeter (for re-tweets)
- Date of original tweet (for re-tweets)
- Text of original tweet (for re-tweets)

The project should be written in Java using Spring boot and write its data to a MySQL database.
The data should use the official Twitter API to obtain the data and you need to have your own API key and test on your own channel proving to use that it work.
The java service will run on Linux and no use of JNI is allowed.

----------------------

Twitter Api to be used: 
https://developer.twitter.com/en/docs/twitter-api/v1/tweets/search/api-reference/get-search-tweets