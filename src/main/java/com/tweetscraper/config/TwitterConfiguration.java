package com.tweetscraper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.inject.Inject;

@Configuration
public class TwitterConfiguration {

    @Inject
    private Environment environment;

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new TwitterConnectionFactory(
                environment.getProperty("twitterscraper.consumerKey"),
                environment.getProperty("twitterscraper.consumerSecret")));
        return registry;
    }

}