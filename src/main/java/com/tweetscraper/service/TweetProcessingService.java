package com.tweetscraper.service;

import org.springframework.social.twitter.api.Tweet;

import javax.transaction.Transactional;
import java.util.List;

public interface TweetProcessingService {
    void processTweets(List<Tweet> tweets);
}
