package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.service.TweetProcessingService;
import com.tweetscraper.service.TweetScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;

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

    @Autowired
    private Environment environment;

    @Autowired
    private TwitterFactory twitterFactory;

    private static final Logger log = LoggerFactory.getLogger(TweetScraperServiceImpl.class);

    @Override
    public void findTweets(String accountName) {
        try {
            Twitter twitter = twitterFactory.getInstance();

            User user = twitter.showUser(twitterChannel);
            tweetProcessingService.processChannelInformation(user);

            boolean quitSyncing = false;

            int iteration = 1;

            Long maxTweetId = tweetRepository.maxTweetId();
            Long sinceId = maxTweetId != null ? maxTweetId : -1L;

            Query query = new Query(twitterChannel);
            query.setSinceId(sinceId);
            query.setCount(100);

            do {

                QueryResult result = twitter.search(query);
                List<Status> tweets = result.getTweets();

                log.info("## Search Params for iteration # (" + iteration + ")" + " : Query = " + query);

                if (tweets != null && !tweets.isEmpty()) {
                    log.info("## Tweet count in iteration # (" + iteration + ") = " + tweets.size());
                    tweetProcessingService.processTweets(tweets);
                }

                quitSyncing = !result.hasNext();
                if (result.hasNext()) {
                    log.info("## Search Params for iteration # (" + iteration + ")" + " : Next Query = " + result.nextQuery());
                    query = result.nextQuery();
                }
                iteration++;
                log.info("=============================================================================");
            } while (!quitSyncing);

        } catch (Exception exception) {
            log.error("Exception occurred while fetching data from the Twitter Api.");
            log.error("Exception from Twitter Api.", exception);
        }

        log.debug("#### TWEET SYNC COMPLETED #### ");
    }

}
