package com.tweetscraper.service.implementation;

import com.tweetscraper.config.TwitterTemplateCreator;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.service.TweetScraperService;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TweetScraperServiceImpl implements TweetScraperService {

    @Autowired
    private TwitterTemplateCreator twitterTemplateCreator;

    @Override
    public void findTweets(String accountName) {

        SearchOperations searchOperations = twitterTemplateCreator.getTwitterTemplate(accountName).searchOperations();

        String query = "nasa&result_type=popular";
        SearchResults searchResults = searchOperations.search(query);
        List<Tweet> tweets = searchResults.getTweets();

        tweets.forEach(
                tweet -> {
                    TwitterUserEntity twitterUserEntity = toTwitterUserEntity(tweet.getUser());
                }
        );
    }

private TwitterUserEntity toTwitterUserEntity(TwitterProfile twitterProfile){
    TwitterUserEntity entity = new TwitterUserEntity();

    entity.setUrl(twitterProfile.getUrl());
    entity.setUserId(twitterProfile.getId());
    entity.setProfileImageUrl(twitterProfile.getProfileImageUrl());
    entity.setName(twitterProfile.getName());
    entity.setScreenName(twitterProfile.getScreenName());
    return entity;
}

}


