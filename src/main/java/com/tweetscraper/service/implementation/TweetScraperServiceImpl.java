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
import org.springframework.social.twitter.api.AccountSettings;
import org.springframework.social.twitter.api.FriendOperations;
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

@Service
public class TweetScraperServiceImpl implements TweetScraperService {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;


    @Autowired
    private TwitterChannelRepository twitterChannelRepository;

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
    @Transactional
    public void findTweets(String accountName) {
        try {
            Twitter twitter = twitterTemplateCreator.getTwitterTemplate(accountName);


            TwitterProfile channelProfile = twitter.userOperations().getUserProfile(twitterChannel);
            twitterChannelRepository.save(TwitterChannelEntity.builder()
                    .screenName(channelProfile.getScreenName())
                    .followerCount(Long.valueOf(channelProfile.getFollowersCount()))
                    .build()
            );
/*
            AccountSettings accountSettings = twitter.userOperations().getAccountSettings();

            FriendOperations friendOperations = twitter.friendOperations();

            Long cursor = -1L;
            Long followerCount = 0L;

            do {
                CursoredList<Long> followerIdsInCursor = friendOperations.getFollowerIdsInCursor(twitterChannel, cursor);
                followerCount = followerCount + followerIdsInCursor.size();
                cursor = followerIdsInCursor.getNextCursor();
            } while (cursor != 0);

            twitterChannelRepository.save(TwitterChannelEntity.builder().screenName(twitterChannel).followerCount(followerCount).build());
*/

            SearchOperations searchOperations = twitter.searchOperations();

            boolean isLastPage = false;

            //SearchParameters searchParameters = getSearchParameter(tweetSearchParameterRepository.findById(twitterChannel));

            SearchParameters searchParameters = new SearchParameters(twitterChannel).sinceId(-1);


            while (!isLastPage) {
                log.info("## Search Param Query = " + searchParameters.getQuery() + " ; sinceId = " + searchParameters.getSinceId());
                SearchResults searchResults = searchOperations.search(searchParameters);
                List<Tweet> tweets = searchResults.getTweets();

                if (tweets != null && !tweets.isEmpty()) {
                    tweetProcessingService.processTweets(tweets);
                }

                isLastPage = searchResults.isLastPage();
                log.info("## Search Param Query = " + searchParameters.getQuery() + " ; sinceId = " + searchParameters.getSinceId() + "; isLastPage = " + isLastPage);
                searchParameters.sinceId(searchResults.getSearchMetadata().getMaxId());
            }
        } catch (Exception exception) {
            log.error("Exception occurred while fetching data from the Twitter Api.");
        }

        log.info("#### TWEET SYNC COMPLETED #### ");
    }

    private TweetSearchParameterEntity convertToTweetSearchParameterEntity(SearchParameters searchParameters) {
        return TweetSearchParameterEntity.builder()
                .query(searchParameters.getQuery())
                .sinceId(searchParameters.getSinceId())
                .resultType(searchParameters.getResultType().name())
                .includeEntities(searchParameters.isIncludeEntities())
                .until(searchParameters.getUntil())
                .build();

    }

    private SearchParameters getSearchParameter(Optional<TweetSearchParameterEntity> tweetSearchParameterEntity) {
        SearchParameters searchParameters = null;

        if (tweetSearchParameterEntity.isPresent()) {
            TweetSearchParameterEntity entity = tweetSearchParameterEntity.get();
            searchParameters = new SearchParameters(entity.getQuery())
                    .sinceId(entity.getSinceId())
                    .resultType(SearchParameters.ResultType.valueOf(entity.getResultType()))
                    .includeEntities(entity.getIncludeEntities())
                    .until(new Date());
        } else {
            searchParameters = new SearchParameters(twitterChannel)
                    .sinceId(-1)
                    .resultType(SearchParameters.ResultType.MIXED)
                    .includeEntities(true)
                    .until(new Date());
        }
        return searchParameters;
    }


}


