package com.tweetscraper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

@Component
public class TwitterTemplateCreator {
	
	@Autowired
	private Environment environment;

	/* Account Name Need to be passed twitterscraper */
	public Twitter getTwitterTemplate(String accountName) {
		String consumerKey = environment.getProperty(accountName + ".consumerKey");
		String consumerSecret = environment.getProperty(accountName + ".consumerSecret");
		String accessToken = environment.getProperty(accountName + ".accessToken");
		String accessTokenSecret = environment.getProperty(accountName + ".accessTokenSecret");

		TwitterTemplate twitterTemplate = new TwitterTemplate(consumerKey, consumerSecret, accessToken,
				accessTokenSecret);
		return twitterTemplate;
	}
}