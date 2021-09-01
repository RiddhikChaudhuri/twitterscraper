package com.tweetscraper.repository;

import com.tweetscraper.entity.UserProfileImageEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Sql
@Transactional
public class UserProfileImageRepositoryTest {

    @Autowired
    private UserProfileImageRepository userProfileImageRepository;

    @Test
    public void fetchUserProfileImage() {
        Optional<UserProfileImageEntity> entity = userProfileImageRepository.findById(11348282L);
        assertTrue(entity.isPresent());
        entity.ifPresent(it -> {
            assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getImageUrl());
            assertEquals("C://temp/nasalogo_twitter_normal.jpg", it.getImageLocation());
        });
    }


    @Test
    public void saveUserProfileImage() {
        UserProfileImageEntity imageEntity = UserProfileImageEntity.builder()
                .id(21348282L)
                .imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .imageLocation("C://temp/nasalogo_twitter_normal.jpg")
                .build();

        userProfileImageRepository.save(imageEntity);


        Optional<UserProfileImageEntity> _imageEntity = userProfileImageRepository.findById(21348282L);
        assertTrue(_imageEntity.isPresent());
        _imageEntity.ifPresent(it -> {
            assertEquals(21348282L, it.getId().longValue());
            assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getImageUrl());
            assertEquals("C://temp/nasalogo_twitter_normal.jpg", it.getImageLocation());
        });

        UserProfileImageEntity updatedImageEntity = UserProfileImageEntity.builder()
                .id(21348282L)
                .imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.png")
                .imageLocation("C://temp/nasalogo_twitter_normal.png")
                .build();
        userProfileImageRepository.save(updatedImageEntity);

        Optional<UserProfileImageEntity> _updatedImageEntity = userProfileImageRepository.findById(21348282L);
        assertTrue(_updatedImageEntity.isPresent());
        _updatedImageEntity.ifPresent(it -> {
            assertEquals(21348282L, it.getId().longValue());
            assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.png", it.getImageUrl());
            assertEquals("C://temp/nasalogo_twitter_normal.png", it.getImageLocation());
        });
    }

}
