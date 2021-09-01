package com.tweetscraper.service;

import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.UserProfileImageEntity;

public interface ImageService {
    UserProfileImageEntity downloadAndSaveUserProfileImage(Long userId, String imageUrl);

    TweetImageEntity downloadAndSaveTweetImage(Long tweetId, String imageUrl);
}
