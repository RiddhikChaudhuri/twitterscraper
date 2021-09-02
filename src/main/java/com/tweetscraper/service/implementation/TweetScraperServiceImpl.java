package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.entity.TweetEntity;
import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.entity.UserProfileImageEntity;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.service.ImageService;
import com.tweetscraper.service.TweetScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.MediaEntity;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

    @Value("${image.directory}")
    private String tweetImageDirectory;

    @Override
    public void findTweets(String accountName) {

        SearchOperations searchOperations = twitterTemplateCreator.getTwitterTemplate(accountName).searchOperations();

        String query = "BullionStar";

        boolean isLastPage = false;

        while (!isLastPage) {
            SearchResults searchResults = searchOperations.search(query);
            List<Tweet> tweets = searchResults.getTweets();

            if(tweets != null && !tweets.isEmpty()){
                processTweets(tweets);
            }

            isLastPage = searchResults.isLastPage();
        }
    }

    @Override
    @Transactional
    public void processTweets(List<Tweet> tweets) {
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


