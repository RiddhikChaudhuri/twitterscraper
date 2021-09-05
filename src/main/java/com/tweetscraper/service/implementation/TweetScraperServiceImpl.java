package com.tweetscraper.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.service.TweetProcessingService;
import com.tweetscraper.service.TweetScraperService;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TweetScraperServiceImpl implements TweetScraperService {

	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;

	@Autowired
	private TweetProcessingService tweetProcessingService;

	@Autowired
	private TweetRepository tweetRepository;

	@Value("${image.directory}")
	private String tweetImageDirectory;

	@Value("${twitter.channel}")
	private String twitterChannel;

	@Autowired
	private Environment environment;

	private static final Logger log = LoggerFactory.getLogger(TweetScraperServiceImpl.class);

	@Override
	public void findTweets(String accountName) { 
		String consumerKey = environment.getProperty(accountName + ".consumerKey");
		String consumerSecret = environment.getProperty(accountName + ".consumerSecret");
		String accessToken = environment.getProperty(accountName + ".accessToken");
		String accessTokenSecret = environment.getProperty(accountName + ".accessTokenSecret");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter4j.Twitter twitter = tf.getSingleton();


		Query query = new Query("source:" + twitterChannel);
		try {
			QueryResult result = twitter.search(query);
			List<Status> tweets = result.getTweets();

			int iteration = 1;
			Long maxTweetId = -1L;
			Long sinceId = -1L;

			maxTweetId = tweetRepository.maxTweetId();
			sinceId = maxTweetId != null ? maxTweetId : -1L;

			log.info("## Search Params for iteration # (" + iteration + ")" + " : sinceId = " + sinceId);

			// SearchResults searchResults =
			// twitter.searchOperations().search(twitterChannel, 100, sinceId, -1L);
			// List<Tweet> tweets = searchResults.getTweets();

			if (tweets != null && !tweets.isEmpty()) {
				log.info("## Tweet count in iteration # (" + iteration + ") = " + tweets.size());
				tweetProcessingService.processTweets(tweets);
			}
		} catch (Exception exception) {
			 log.error("Exception occurred while fetching data from the Twitter Api.");
		}

		log.debug("#### TWEET SYNC COMPLETED #### ");
	}
}
