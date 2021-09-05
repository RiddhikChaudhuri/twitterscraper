package com.tweetscraper.service.implementation;

import com.tweetscraper.entity.TweetEntity;
import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TwitterChannelEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.repository.TwitterChannelRepository;
import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.service.ImageService;
import com.tweetscraper.service.TweetProcessingService;

import twitter4j.Status;
import twitter4j.User;

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

    @Autowired
    private TwitterChannelRepository twitterChannelRepository;

    private static final Logger logger = LoggerFactory.getLogger(TweetProcessingServiceImpl.class);

    @Override
    @Transactional
    public void processTweets(List<Status> tweets) {
        Set<TwitterUserEntity> twitterUserEntities = new HashSet<>();
        Set<TweetImageEntity> tweetImageEntities = new HashSet<>();
        Set<TweetEntity> tweetEntities = new HashSet<>();

        tweets.forEach(tweet -> {
            TweetEntity tweetEntity = TweetEntity.builder()
                    .id(tweet.getId())
                    .text(tweet.getText())
                    .link(getTweetLink(tweet))
                    .lang(tweet.getLang())
                    .retweetCount(tweet.getRetweetCount())
                    .favoriteCount(tweet.getFavoriteCount())
                    .retweeted(tweet.isRetweeted())
                    .favorited(tweet.isFavorited())
                    .twitterUserId(tweet.getUser().getId())
                    .twitterUserName(tweet.getUser().getName())
                    .createdAt(tweet.getCreatedAt())
                    .build();

            Status originalTweet = tweet.getRetweetedStatus();
            if (originalTweet != null) {
                tweetEntity.setOriginalTwitterName(originalTweet.getUser().getName());
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

    @Override
    @Transactional
    public void processChannelInformation(User channelProfile) {
        twitterChannelRepository.save(TwitterChannelEntity.builder()
                .id(channelProfile.getId())
                .name(channelProfile.getName())
                .screenName(channelProfile.getScreenName())
                .profileImageUrl(channelProfile.getProfileImageURL())
                .url(channelProfile.getURL())
                .followerCount(Long.valueOf(channelProfile.getFollowersCount()))
                .build()
        );
    }

	private Set<TweetImageEntity> extractTweetedImages(Status tweet) {
		Set<TweetImageEntity> images = new HashSet<>();
		if (!Objects.isNull(tweet.getMediaEntities())) {
			twitter4j.MediaEntity[] mediaEntities = tweet.getMediaEntities();
			// extracting images information
			for (int i = 0; i < mediaEntities.length; i++) {
				if (mediaEntities[i].getType().equals("photo")) {
					images.add(TweetImageEntity.builder().tweetId(tweet.getId())
							.imageUrl(mediaEntities[i].getMediaURL()).build());
				}
			}
		}
		return images;
	}

    private String getTweetLink(Status tweet) {
        if (tweet.getURLEntities()!=null) {
            return tweet.getURLEntities()[0].getExpandedURL();
        }

        return null;
    }

    private TwitterUserEntity getTwitterUserEntity(User twitterProfile) {
        return TwitterUserEntity.builder()
                .url(twitterProfile.getURL())
                .id(twitterProfile.getId())
                .name(twitterProfile.getName())
                .screenName(twitterProfile.getScreenName())
                .profileImageUrl(twitterProfile.getBiggerProfileImageURL())
                .build();
    }


}
