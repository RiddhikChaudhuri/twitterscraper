package com.tweetscraper.repository;

import com.tweetscraper.entity.TwitterChannelEntity;
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
@Transactional
public class TwitterChannelRepositoryTest {

    @Autowired
    private TwitterChannelRepository repository;

    @Test
    public void saveTwitterChannelInformation(){
        TwitterChannelEntity entity = TwitterChannelEntity.builder()
                .screenName("Twitter")
                .followerCount(100L)
                .build();

        repository.save(entity);

        Optional<TwitterChannelEntity> _entity = repository.findById("Twitter");

        Assert.assertTrue(_entity.isPresent());
        _entity.ifPresent(it->{
            Assert.assertEquals(100L, it.getFollowerCount().longValue());
            Assert.assertEquals("Twitter", it.getScreenName());
        });

        TwitterChannelEntity updatedEntity = TwitterChannelEntity.builder()
                .screenName("Twitter")
                .followerCount(200L)
                .build();

        repository.save(updatedEntity);

        Optional<TwitterChannelEntity> _updatedEntity = repository.findById("Twitter");

        Assert.assertTrue(_updatedEntity.isPresent());
        _entity.ifPresent(it->{
            Assert.assertEquals(200L, it.getFollowerCount().longValue());
            Assert.assertEquals("Twitter", it.getScreenName());
        });
    }
}
