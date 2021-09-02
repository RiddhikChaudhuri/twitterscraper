package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.entity.TweetEntity;
import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TweetSearchParameterEntity;
import com.tweetscraper.entity.TwitterChannelEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.entity.UserProfileImageEntity;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.repository.TweetSearchParameterRepository;
import com.tweetscraper.repository.TwitterChannelRepository;
import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.service.ImageService;
import com.tweetscraper.service.TweetScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.AccountSettings;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.MediaEntity;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TweetScraperServiceImpl implements TweetScraperService {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    @Autowired
    private TwitterUserRepository twitterUserRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TwitterChannelRepository twitterChannelRepository;

    @Autowired
    private TweetSearchParameterRepository tweetSearchParameterRepository;

    @Value("${image.directory}")
    private String tweetImageDirectory;

    @Value("${twitter.channel}")
    private String twitterChannel;

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    @Transactional
    public void findTweets(String accountName) {

        Twitter twitter = twitterTemplateCreator.getTwitterTemplate(accountName);
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

        SearchOperations searchOperations = twitter.searchOperations();

        boolean isLastPage = false;

        //SearchParameters searchParameters = getSearchParameter(tweetSearchParameterRepository.findById(twitterChannel));

        SearchParameters searchParameters = new SearchParameters(twitterChannel).sinceId(-1);


        while (!isLastPage) {
            log.info("## Search Param Query = " + searchParameters.getQuery() + " ; sinceId = " + searchParameters.getSinceId());
            SearchResults searchResults = searchOperations.search(searchParameters);
            List<Tweet> tweets = searchResults.getTweets();

            if(tweets != null && !tweets.isEmpty()){
                processTweets(tweets);
            }

            isLastPage = searchResults.isLastPage();
            log.info("## Search Param Query = " + searchParameters.getQuery() + " ; sinceId = " + searchParameters.getSinceId() + "; isLastPage = " +isLastPage);
            searchParameters.sinceId(searchResults.getSearchMetadata().getMaxId());
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

    @Override
    @Transactional
    public void processTweets(List<Tweet> tweets) {
        Long tweetCount = 0L;
        Set<TwitterUserEntity> twitterUserEntities = new HashSet<>();
        Set<TweetImageEntity> tweetImageEntities = new HashSet<>();
        Set<TweetEntity> tweetEntities = new HashSet<>();

        tweets.forEach(tweet -> {

            TweetEntity tweetEntity = TweetEntity.builder()
                    .id(tweet.getId())
                    .text(tweet.getText())
                    .link(getTweetLink(tweet))
                    .lang(tweet.getLanguageCode())
                    .retweetCount(tweet.getRetweetCount())
                    .favoriteCount(tweet.getFavoriteCount())
                    .retweeted(tweet.isRetweeted())
                    .favorited(tweet.isFavorited())
                    .twitterUserId(tweet.getFromUserId())
                    .twitterUserName(tweet.getFromUser())
                    .createdAt(tweet.getCreatedAt())
                    .build();

            Tweet originalTweet = tweet.getRetweetedStatus();
            if (originalTweet != null) {
                tweetEntity.setOriginalTwitterName(originalTweet.getFromUser());
                tweetEntity.setOriginalLink(getTweetLink(originalTweet));
                tweetEntity.setOriginalCreatedAt(originalTweet.getCreatedAt());
                tweetEntity.setOriginalText(originalTweet.getText());
            }

            if (!Objects.isNull(tweet.getEntities().getMedia())) {
                List<MediaEntity> mediaEntities = tweet.getEntities().getMedia();
                // extracting images information
                for (MediaEntity me : mediaEntities) {
                    if (me.getType().equals("photo")) {
                        tweetImageEntities.add(TweetImageEntity.builder().tweetId(tweet.getId()).imageUrl(mediaEntities.get(0).getMediaUrl()).build());
                        break;
                    }
                }
            }

            tweetEntities.add(tweetEntity);

            twitterUserEntities.add(getTwitterUserEntity(tweet.getUser()));

        });

        // Save Tweets
        tweetRepository.saveAll(tweetEntities);

        // Download & Save Tweet Images
        imageService.downloadAndSaveTweetImages(tweetImageEntities);

        // Save Twitter User Profile
        twitterUserRepository.saveAll(twitterUserEntities);

        // Download & Save Twitter User Profile Images
        imageService.downloadAndSaveUserProfileImages(twitterUserEntities);

    }

    private String getTweetLink(Tweet tweet) {
        if (tweet.getEntities().hasUrls()) {
            return tweet.getEntities().getUrls().get(0).getExpandedUrl();
        }

        return null;
    }

    private TwitterUserEntity getTwitterUserEntity(TwitterProfile twitterProfile) {
        return TwitterUserEntity.builder()
                .url(twitterProfile.getUrl())
                .id(twitterProfile.getId())
                .name(twitterProfile.getName())
                .screenName(twitterProfile.getScreenName())
                .profileImageUrl(twitterProfile.getProfileImageUrl())
                .build();
    }

}


