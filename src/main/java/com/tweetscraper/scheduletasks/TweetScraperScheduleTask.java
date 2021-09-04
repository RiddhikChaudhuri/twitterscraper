package com.tweetscraper.scheduletasks;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.service.TweetScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import com.tweetscraper.repository.TwitterUserRepository;

@Component
public class TweetScraperScheduleTask {

    @Autowired
    private TweetScraperService tweetScraperService;

    @Value("${twitter.account.name}")
    private String accountName;

    // 1hr interval between invocations measured from the completion of the task
    // 1hr = 60*60*1000
    //@Scheduled(fixedDelay = 60*60*1000)
    @Scheduled(fixedDelay = 5000)
    public void scrapeTweets() {
//        tweetScraperService.findTweets(accountName);
    }
}
