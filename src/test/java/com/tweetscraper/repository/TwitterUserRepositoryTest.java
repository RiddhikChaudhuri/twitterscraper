package com.tweetscraper.repository;


import com.tweetscraper.entity.TwitterUserEntity;
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
public class TwitterUserRepositoryTest {

    @Autowired
    private TwitterUserRepository repository;

    @Test
    public void fetchTweeterUserUsingTwitterUserId() {
        Optional<TwitterUserEntity> entity = repository.findById(11348282L);
        assertTrue(entity.isPresent());
        entity.ifPresent(it -> {
                    assertEquals("NASA", it.getName());
                    assertEquals("NASA", it.getScreenName());
                    assertEquals("https://t.co/TcEE6NS8nD", it.getUrl());
                    assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getProfileImageUrl());
                    assertEquals("https://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getProfileImageUrlHttps());
                }
        );
    }


    @Test
    public void saveTwitterUserInformation() {

        TwitterUserEntity entity = TwitterUserEntity.builder()
                .name("NASA").screenName("NASA").id(11348283L).profileImageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .profileImageUrlHttps("https://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .url("https://t.co/TcEE6NS8nD")
                .build();
        repository.save(entity);


        Optional<TwitterUserEntity> fetchedFromDB = repository.findById(11348283L);
        assertTrue(fetchedFromDB.isPresent());
        fetchedFromDB.ifPresent(it -> {
                    assertEquals("NASA", it.getName());
                    assertEquals("NASA", it.getScreenName());
                    assertEquals("https://t.co/TcEE6NS8nD", it.getUrl());
                    assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getProfileImageUrl());
                    assertEquals("https://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getProfileImageUrlHttps());
                }
        );


        TwitterUserEntity updatedEntity = TwitterUserEntity.builder()
                .name("NASA Updated").screenName("NASA Updated").id(11348283L).profileImageUrl("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .profileImageUrlHttps("https://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg")
                .url("https://t.co/TcEE6NS8nD")
                .build();
        repository.save(updatedEntity);

        Optional<TwitterUserEntity> updatedData = repository.findById(11348283L);
        assertTrue(updatedData.isPresent());
        updatedData.ifPresent(it -> {
                    assertEquals("NASA Updated", it.getName());
                    assertEquals("NASA Updated", it.getScreenName());
                    assertEquals("https://t.co/TcEE6NS8nD", it.getUrl());
                    assertEquals("http://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getProfileImageUrl());
                    assertEquals("https://pbs.twimg.com/profile_images/188302352/nasalogo_twitter_normal.jpg", it.getProfileImageUrlHttps());
                }
        );

    }
}
