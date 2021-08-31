package com.tweetscraper.repository;


import com.tweetscraper.entity.TweetUserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
@Sql
@Transactional
public class TweetUserRepositoryTest {

    @Autowired
    private TweetUserRepository tweetUserRepository;

    @Test
    public void getTweeterUserInfo() {
        Optional<TweetUserEntity> entity = tweetUserRepository.findById(1);
        if (entity.isPresent()) {

            Assert.assertEquals("Email Id is mission", entity.get().getEmail(), "john.doe@example.org");
        }

    }


    @Test
    public void insertTweeterUserInfo() {
        TweetUserEntity entity = new TweetUserEntity();
        entity.setEmail("john.doe@example.org");
        entity.setName("John Doe");
        Integer id = tweetUserRepository.save(entity).getId();

        Optional<TweetUserEntity> entity1 = tweetUserRepository.findById(id);
        entity1.ifPresent(it -> {
                    Assert.assertEquals("Email Id is mission", it.getEmail(), "john.doe@example.org");
                    Assert.assertEquals("Email Id is mission", it.getName(), "John Doe");
                }
        );
    }

}
