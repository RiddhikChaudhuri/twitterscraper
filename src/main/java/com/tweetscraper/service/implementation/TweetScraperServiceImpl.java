package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.service.TweetProcessingService;
import com.tweetscraper.service.TweetScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private static final Logger log = LoggerFactory.getLogger(TweetScraperServiceImpl.class);

    @Override
    public void findTweets(String accountName) {
        try {
            Twitter twitter = twitterTemplateCreator.getTwitterTemplate(accountName);

            TwitterProfile channelProfile = twitter.userOperations().getUserProfile(twitterChannel);
            tweetProcessingService.processChannelInformation(channelProfile);

            boolean quitSyncing = false;

            int iteration = 1;
            Long maxTweetId = -1L;
            Long sinceId = -1L;

            do {

                maxTweetId = tweetRepository.maxTweetId();
                sinceId = maxTweetId != null ? maxTweetId : -1L;

                log.info("## Search Params for iteration # (" + iteration + ")" + " : sinceId = " + sinceId);

                SearchResults searchResults = twitter.searchOperations().search(twitterChannel, 100, sinceId, -1L);
                List<Tweet> tweets = searchResults.getTweets();

                if (tweets != null && !tweets.isEmpty()) {
                    log.info("## Number of tweets in iteration # (" + iteration + ")" + " : sinceId = " + sinceId);
                    tweetProcessingService.processTweets(tweets);
                }

                quitSyncing = ((tweets != null && tweets.isEmpty()) || searchResults.isLastPage());

                log.info("## Search Result Data from iteration # = (" + iteration + ")"
                        + " ; sinceId = " + searchResults.getSearchMetadata().getSinceId()
                        + " ; maxId = " + searchResults.getSearchMetadata().getMaxId()
                        + "; isLastPage = " + searchResults.isLastPage()
                );
                log.info("===================================================================");
            } while (!quitSyncing);

        } catch (Exception exception) {
            log.error("Exception occurred while fetching data from the Twitter Api.", exception);
        }

        log.debug("#### TWEET SYNC COMPLETED #### ");
    }
}


