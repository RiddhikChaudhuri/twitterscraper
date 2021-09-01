package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.service.ImageService;
import com.tweetscraper.service.TweetScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetScraperServiceImpl implements TweetScraperService {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    @Autowired
    private TwitterUserRepository twitterUserRepository;

    @Autowired
    private ImageService imageService;

    @Override
    public void findTweets(String accountName) {

        SearchOperations searchOperations = twitterTemplateCreator.getTwitterTemplate(accountName).searchOperations();

        String query = "BullionStar";
        SearchResults searchResults = searchOperations.search(query);
        List<Tweet> tweets = searchResults.getTweets();

        tweets.forEach(
                tweet -> {
                    TwitterProfile twitterProfile = tweet.getUser();
                    TwitterUserEntity twitterUserEntity = getTwitterUserEntity(twitterProfile);
                    twitterUserRepository.save(twitterUserEntity);
                    imageService.downloadAndSave(twitterProfile.getId(), twitterProfile.getProfileImageUrl());
                }
        );
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


