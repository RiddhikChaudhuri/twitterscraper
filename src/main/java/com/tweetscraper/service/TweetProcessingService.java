package com.tweetscraper.service;

import twitter4j.Status;
import twitter4j.User;

import java.util.List;

public interface TweetProcessingService {
    void processTweets(List<Status> tweets);

    void processChannelInformation(User channelProfile);

}
