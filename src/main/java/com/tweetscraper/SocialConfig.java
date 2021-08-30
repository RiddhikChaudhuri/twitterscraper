package com.tweetscraper;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
public class SocialConfig {

	@Inject
	private Environment environment;

	@Value("${twitterscraper.consumerKey}")
	private String consumerKey;

	@Value("${twitterscraper.consumerSecret}")
	private String consumerSecret;

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new TwitterConnectionFactory(environment.getProperty(consumerKey),
				environment.getProperty(consumerSecret)));
		return registry;
	}

}