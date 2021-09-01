package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.entity.TweetEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.repository.TweetRepository;
import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.service.ImageService;
import com.tweetscraper.service.TweetScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.MediaEntity;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
		SearchResults searchResults = searchOperations.search(query);
		List<Tweet> tweets = searchResults.getTweets();

		tweets.forEach(tweet -> {
			TweetEntity tweetEntity = TweetEntity.builder().tweetId(tweet.getId()).retweetCount(tweet.getRetweetCount())
					.favoriteCount(tweet.getFavoriteCount()).build();
			if (!Objects.isNull(tweet.getEntities().getMedia())) {
				List<MediaEntity> mediaEntities = tweet.getEntities().getMedia();
				for (MediaEntity me : mediaEntities) {
					if (me.getType().equals("photo")) {
						tweetEntity.setPictureUrl(mediaEntities.get(0).getMediaUrl());
						break;
					}
				}

				String picturePath = tweetImageDirectory + "/" + tweetEntity.getTweetId().toString();
				if (tweetEntity.getPictureUrl().endsWith("jpg"))
					picturePath += ".jpg";
				else if (tweetEntity.getPictureUrl().endsWith("png"))
					picturePath += ".png";
				else if (tweetEntity.getPictureUrl().endsWith("gif"))
					picturePath += ".gif";
				tweetEntity.setPicturePath(picturePath);
			}
			System.out.println(tweetEntity);
			// tweetRepository.save(tweet);
			TwitterProfile twitterProfile = tweet.getUser();
			TwitterUserEntity twitterUserEntity = getTwitterUserEntity(twitterProfile);
			twitterUserRepository.save(twitterUserEntity);
			imageService.downloadAndSave(twitterProfile.getId(), twitterProfile.getProfileImageUrl());
		});
	}

    private TwitterUserEntity getTwitterUserEntity(TwitterProfile twitterProfile) {
        TwitterUserEntity entity = new TwitterUserEntity();

        entity.setUrl(twitterProfile.getUrl());
        entity.setUserId(twitterProfile.getId());
        entity.setProfileImageUrl(twitterProfile.getProfileImageUrl());
        entity.setName(twitterProfile.getName());
        entity.setScreenName(twitterProfile.getScreenName());
        return entity;
    }

}


