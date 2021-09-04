package com.tweetscraper.service;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import javax.transaction.Transactional;
import java.util.List;

public interface TweetProcessingService {
    void processTweets(List<Tweet> tweets);
    void processChannelInformation(TwitterProfile channelProfile);
}
