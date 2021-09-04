package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.entity.TweetSearchParameterEntity;
import com.tweetscraper.entity.TwitterChannelEntity;
import com.tweetscraper.repository.TweetSearchParameterRepository;
import com.tweetscraper.repository.TwitterChannelRepository;
import com.tweetscraper.service.TweetProcessingService;
import com.tweetscraper.service.TweetScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.social.twitter.api.SearchParameters.ResultType.MIXED;

@Service
public class TweetScraperServiceImpl implements TweetScraperService {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    @Autowired
    private TweetSearchParameterRepository tweetSearchParameterRepository;

    @Autowired
    private TweetProcessingService tweetProcessingService;

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

            SearchOperations searchOperations = twitter.searchOperations();

            boolean isLastPage = false;

            Optional<TweetSearchParameterEntity> searchParameterEntity = tweetSearchParameterRepository.findById(twitterChannel);
//            SearchParameters searchParameters = getSearchParameter(searchParameterEntity);

            String query = twitterChannel;

            Long maxId = searchParameterEntity.isPresent() ? searchParameterEntity.get().getSinceId() : -1L;


            SearchParameters searchParameters = new SearchParameters(query)
                    .count(100)
                    .until(new Date())
                    //.sinceId(maxId)
                    .resultType(MIXED)
                    .includeEntities(true);



            log.info("## Search Param Query = " + searchParameters.getQuery() + " ; sinceId = " + searchParameters.getSinceId());
            // search(String query, int pageSize, long sinceId, long maxId);
            SearchResults searchResults = searchOperations.search(searchParameters);
            List<Tweet> tweets = searchResults.getTweets();

            if (tweets != null && !tweets.isEmpty()) {
                tweetProcessingService.processTweets(tweets);
            }

            isLastPage = searchResults.isLastPage();
            log.info("## Search Param Query = " + searchParameters.getQuery()
                    + " ; sinceId = " + searchResults.getSearchMetadata().getSinceId()
                    + " ; maxId = " + searchResults.getSearchMetadata().getMaxId()
                    + "; isLastPage = " + isLastPage
            );
            TweetSearchParameterEntity tweetSearchParameterEntity = convertToTweetSearchParameterEntity(new SearchParameters(twitterChannel)
                    .sinceId(searchResults.getSearchMetadata().getMaxId())
                    .count(100));
            tweetSearchParameterRepository.save(tweetSearchParameterEntity);
        } catch (Exception exception) {
            log.error("Exception occurred while fetching data from the Twitter Api.", exception);
        }

        log.info("#### TWEET SYNC COMPLETED #### ");
    }

    private TweetSearchParameterEntity convertToTweetSearchParameterEntity(SearchParameters searchParameters) {
        return TweetSearchParameterEntity.builder()
                .query(searchParameters.getQuery())
                .sinceId(searchParameters.getSinceId())
                .build();

    }

    private SearchParameters getSearchParameter(Optional<TweetSearchParameterEntity> tweetSearchParameterEntity) {

        if (tweetSearchParameterEntity.isPresent()) {
            TweetSearchParameterEntity entity = tweetSearchParameterEntity.get();
            return new SearchParameters(entity.getQuery())
                    .sinceId(entity.getSinceId())
                    .resultType(MIXED)
                    .includeEntities(true)
                    .count(100);
        } else {
            return new SearchParameters(twitterChannel)
                    .sinceId(-1)
                    .resultType(MIXED)
                    .includeEntities(true)
                    .count(100);
        }
    }


}


