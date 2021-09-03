package com.tweetscraper.service.implementation;

import com.tweetscraper.entity.TweetEntity;
import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.service.ImageService;
import com.tweetscraper.service.TweetProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.MediaEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class TweetProcessingServiceImpl implements TweetProcessingService {

    @Autowired
    private TwitterUserRepository twitterUserRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private TweetRepository tweetRepository;


    private static final Logger logger = LoggerFactory.getLogger(TweetProcessingServiceImpl.class);

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

            tweetImageEntities.addAll(extractTweetedImages(tweet));

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

    private Set<TweetImageEntity> extractTweetedImages(Tweet tweet) {
        Set<TweetImageEntity> images = new HashSet<>();
        if (!Objects.isNull(tweet.getEntities().getMedia())) {
            List<MediaEntity> mediaEntities = tweet.getEntities().getMedia();
            // extracting images information
            for (MediaEntity me : mediaEntities) {
                if (me.getType().equals("photo")) {
                    images.add(TweetImageEntity.builder().tweetId(tweet.getId()).imageUrl(mediaEntities.get(0).getMediaUrl()).build());
                }
            }
        }
        return images;
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
