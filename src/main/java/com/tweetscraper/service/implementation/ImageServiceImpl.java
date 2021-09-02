package com.tweetscraper.service.implementation;

import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import com.tweetscraper.entity.UserProfileImageEntity;
import com.tweetscraper.repository.TweetImageRepository;
import com.tweetscraper.repository.UserProfileImageRepository;
import com.tweetscraper.service.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${image.directory}")
    private String imageDirectory;

    @Autowired
    UserProfileImageRepository userProfileImageRepository;

    @Autowired
    TweetImageRepository tweetImageRepository;

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public UserProfileImageEntity downloadAndSaveUserProfileImage(Long userId, String imageUrl) {
        String imageLocation = null;
        try {
            imageLocation = downloadImage(imageUrl);
        } catch (IOException e) {
            log.error("Unable to download and store image from {0} for User Id {1}", imageUrl, userId);
            log.error("Stack Trace", e);
        }
        UserProfileImageEntity imageEntity = UserProfileImageEntity.builder().imageUrl(imageUrl).imageLocation(imageLocation).id(userId).build();
        return userProfileImageRepository.save(imageEntity);
    }

    @Override
    public void downloadAndSaveUserProfileImages(Set<TwitterUserEntity> twitterUserEntities) {

        Set<UserProfileImageEntity> userProfileImageEntities = twitterUserEntities.stream().map(it -> UserProfileImageEntity.builder().id(it.getId()).imageUrl(it.getProfileImageUrl()).build()).collect(Collectors.toSet());

        Set<UserProfileImageEntity> _userProfileImageEntities = userProfileImageEntities.stream().map(it -> {
                    String imageLocation = null;
                    try {
                        imageLocation = downloadImage(it.getImageUrl());
                    } catch (IOException e) {
                        log.error("Unable to download and store image from {0} for User Id {1}", it.getImageUrl(), it.getId());
                        log.error("Stack Trace", e);
                    } finally {
                        it.setImageLocation(imageLocation);
                    }
                    return it;
                }
        ).collect(Collectors.toSet());

        userProfileImageRepository.saveAll(_userProfileImageEntities);
    }

    @Override
    public void downloadAndSaveTweetImages(Set<TweetImageEntity> tweetImageEntities) {
        Set<TweetImageEntity> _tweetImageEntities = tweetImageEntities.stream().map(it -> {
                    String imageLocation = null;
                    try {
                        imageLocation = downloadImage(it.getImageUrl());
                    } catch (IOException e) {
                        log.error("Unable to download and store image from {0} for Tweet Id {1}", it.getImageUrl(), it.getTweetId());
                        log.error("Stack Trace", e);
                    } finally {
                        it.setImageLocation(imageLocation);
                    }
                    return it;
                }

        ).collect(Collectors.toSet());

        tweetImageRepository.saveAll(_tweetImageEntities);
    }

    @Override
    public TweetImageEntity downloadAndSaveTweetImage(Long tweetId, String imageUrl) {
        String imageLocation = null;
        try {
            imageLocation = downloadImage(imageUrl);
        } catch (IOException e) {
            log.error("Unable to download and store image from {0} for Tweet Id {1}", imageUrl, tweetId);
            log.error("Stack Trace", e);
        }
        TweetImageEntity imageEntity = TweetImageEntity.builder().imageUrl(imageUrl).imageLocation(imageLocation).tweetId(tweetId).build();
        return tweetImageRepository.save(imageEntity);
    }


    private String downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        File file = new File(imageDirectory, FilenameUtils.getName(url.getPath()));
        ImageIO.write(ImageIO.read(url), FilenameUtils.getExtension(url.getPath()), file);
        return file.getPath();
    }
}