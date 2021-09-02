package com.tweetscraper.service;

import org.springframework.social.twitter.api.Tweet;

import java.util.List;

public interface TweetScraperService {

    public void findTweets(String accountName);

    void processTweets(List<Tweet> tweets);
}
