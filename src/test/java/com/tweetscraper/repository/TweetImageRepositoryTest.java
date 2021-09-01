package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TweetImageId;
import com.tweetscraper.entity.UserProfileImageEntity;
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
public class TweetImageRepositoryTest {
    @Autowired
    private TweetImageRepository repository;

    @Test
    public void fetchUserProfileImage() {
        Optional<TweetImageEntity> entity = repository.findById(TweetImageId.builder().tweetId(11348282L).imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg").build());
        assertTrue(entity.isPresent());
        entity.ifPresent(it -> {
            assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getImageUrl());
            assertEquals("C://temp/nasalogo_twitter_normal.jpg", it.getImageLocation());
        });
    }

    @Test
    public void saveUserProfileImage() {
        TweetImageEntity imageEntity = TweetImageEntity.builder()
                .tweetId(21348282L)
                .imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .imageLocation("C://temp/nasalogo_twitter_normal.jpg")
                .build();

        repository.save(imageEntity);


        Optional<TweetImageEntity> _imageEntity = repository.findById(TweetImageId.builder().tweetId(21348282L).imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg").build());
        assertTrue(_imageEntity.isPresent());
        _imageEntity.ifPresent(it -> {
            assertEquals(21348282L, it.getTweetId().longValue());
            assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getImageUrl());
            assertEquals("C://temp/nasalogo_twitter_normal.jpg", it.getImageLocation());
        });

        TweetImageEntity updatedImageEntity = TweetImageEntity.builder()
                .tweetId(21348282L)
                .imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .imageLocation("C://temp/nasalogo_twitter_normal.png")
                .build();
        repository.save(updatedImageEntity);

        Optional<TweetImageEntity> _updatedImageEntity = repository.findById(TweetImageId.builder().tweetId(21348282L).imageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg").build());
        assertTrue(_updatedImageEntity.isPresent());
        _updatedImageEntity.ifPresent(it -> {
            assertEquals(21348282L, it.getTweetId().longValue());
            assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getImageUrl());
            assertEquals("C://temp/nasalogo_twitter_normal.png", it.getImageLocation());
        });
    }
}
