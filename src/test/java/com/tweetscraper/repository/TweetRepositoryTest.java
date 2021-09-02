package com.tweetscraper.repository;


import com.tweetscraper.entity.TweetEntity;
import com.tweetscraper.entity.TwitterUserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
@Sql
@Transactional
public class TweetRepositoryTest {

    @Autowired
    private TweetRepository repository;

    @Test
    public void saveTweet(){

        TweetEntity entity = TweetEntity.builder()
                .id(967824267948773377L)
                .text("From pilot to astronaut, Robert H. Lawrence was the first African-American to be selected as an astronaut by any na… https://t.co/FjPEWnh804")
                .link("https://twitter.com/i/web/status/967824267948773377")
                .lang("en")
                .retweetCount(1)
                .favoriteCount(10)
                .retweeted(true)
                .favorited(true)
                .twitterUserId(11348282L)
                .twitterUserName("NASA")
                .createdAt(new Date())
                .originalTwitterName("NASA Original")
                .originalLink("https://twitter.com/i/web/status/967824267948773388")
                .originalCreatedAt(new Date())
                .originalText("From pilot to astronaut")
                .build();

        repository.save(entity);

    }
}
