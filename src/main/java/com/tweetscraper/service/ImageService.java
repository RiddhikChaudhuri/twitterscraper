package com.tweetscraper.service;

import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.entity.UserProfileImageEntity;

import java.util.List;
import java.util.Set;

public interface ImageService {
    UserProfileImageEntity downloadAndSaveUserProfileImage(Long userId, String imageUrl);

    void downloadAndSaveUserProfileImages(Set<TwitterUserEntity> twitterUserEntities);

    void downloadAndSaveTweetImages(Set<TweetImageEntity> tweetImageEntities);

    TweetImageEntity downloadAndSaveTweetImage(Long tweetId, String imageUrl);
}
