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
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
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

    public static final String[] SUPPORTED_IMAGE_FILE_VALUES = new String[]{"JPEG", "PNG", "BMP", "WEBMP", "GIF"};
    public static final Set<String> SUPPORTED_IMAGE_FILES = new HashSet<>(Arrays.asList(SUPPORTED_IMAGE_FILE_VALUES));

    @Override
    public UserProfileImageEntity downloadAndSaveUserProfileImage(Long userId, String imageUrl) {
        String imageLocation = downloadImage(imageUrl);
        if (imageLocation == null) {
            log.error("Unable to download and store user profile image from " + imageUrl + "for User Id " + userId);
        }
        UserProfileImageEntity imageEntity = UserProfileImageEntity.builder().imageUrl(imageUrl).imageLocation(imageLocation).id(userId).build();
        return userProfileImageRepository.save(imageEntity);
    }

    @Override
    public void downloadAndSaveUserProfileImages(Set<TwitterUserEntity> twitterUserEntities) {

        Set<UserProfileImageEntity> userProfileImageEntities = twitterUserEntities.stream()
                .filter(it -> (it.getProfileImageUrl() != null))
                .map(it -> UserProfileImageEntity.builder().id(it.getId()).imageUrl(it.getProfileImageUrl()).build()).collect(Collectors.toSet());

        log.info("Number of User Images to be download = " + userProfileImageEntities.size());

        Set<UserProfileImageEntity> _userProfileImageEntities = userProfileImageEntities.stream().map(it -> {
                    String imageLocation = downloadImage(it.getImageUrl());
                    if (imageLocation == null) {
                        log.error("Unable to download and store user profile image from " + it.getImageUrl() + " for User Id " + it.getId());
                    }

                    it.setImageLocation(imageLocation);
                    return it;
                }
        ).collect(Collectors.toSet());

        userProfileImageRepository.saveAll(_userProfileImageEntities);
    }

    @Override
    public void downloadAndSaveTweetImages(Set<TweetImageEntity> tweetImageEntities) {

        Set<TweetImageEntity> _tweetImageEntities = tweetImageEntities.stream()
                .filter(it -> (it.getImageUrl() != null))
                .map(it -> {
                            String imageLocation = downloadImage(it.getImageUrl());
                            if (imageLocation == null) {
                                log.error("Unable to download and store tweet image from " + it.getImageUrl() + " for Tweet Id " + it.getTweetId());
                            }

                            it.setImageLocation(imageLocation);
                            return it;
                        }

                ).collect(Collectors.toSet());

        log.info("Number of Tweet Images to be download = " + _tweetImageEntities.size());

        tweetImageRepository.saveAll(_tweetImageEntities);
    }

    @Override
    public TweetImageEntity downloadAndSaveTweetImage(Long tweetId, String imageUrl) {
        String imageLocation = downloadImage(imageUrl);
        if (imageLocation == null) {
            log.error("Unable to download and store tweet image from " + imageUrl + " for Tweet Id " + tweetId);
        }

        TweetImageEntity imageEntity = TweetImageEntity.builder().imageUrl(imageUrl).imageLocation(imageLocation).tweetId(tweetId).build();
        return tweetImageRepository.save(imageEntity);
    }


    private String downloadImage(String imageUrl) {
        if (imageUrl != null) {
            log.info("Downloading the image " + imageUrl);
            try {
                URL url = new URL(imageUrl);
                String extension = FilenameUtils.getExtension(url.getPath());
                if (SUPPORTED_IMAGE_FILES.contains(extension.toUpperCase())) {
                    File file = new File(imageDirectory, FilenameUtils.getName(url.getPath()));
                    ImageIO.write(ImageIO.read(url), FilenameUtils.getExtension(url.getPath()), file);
                    return file.getPath();
                }
            } catch (Exception e) {
                log.error("Unable to download and store image from " + imageUrl);
                //log.error("Stack Trace", e);
            }
        }
        return null;
    }
}