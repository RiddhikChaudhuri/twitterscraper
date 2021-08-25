package com.tweetscraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Component;

@Component
public class TwitterTemplateCreator {
	@Autowired
	private Environment env;

	/* Account Name Need to be passed twitterscraper */
	public Twitter getTwitterTemplate(String accountName) {
		String consumerKey = env.getProperty(accountName + ".consumerKey");
		String consumerSecret = env.getProperty(accountName + ".consumerSecret");
		String accessToken = env.getProperty(accountName + ".accessToken");
		String accessTokenSecret = env.getProperty(accountName + ".accessTokenSecret");

		TwitterTemplate twitterTemplate = new TwitterTemplate(consumerKey, consumerSecret, accessToken,
				accessTokenSecret);
		return twitterTemplate;
	}
}