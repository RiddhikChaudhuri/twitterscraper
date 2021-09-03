package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetSearchParameterEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class TweetSearchParameterRepositoryTest {

    @Autowired
    private TweetSearchParameterRepository repository;

    @Test
    public void saveSearchParameters(){
        TweetSearchParameterEntity entity = TweetSearchParameterEntity.builder()
                .query("Twitter")
                .sinceId(10L)
                .build();

        repository.save(entity);

        Optional<TweetSearchParameterEntity> _entity = repository.findById("Twitter");
        Assert.assertTrue(_entity.isPresent());

        _entity.ifPresent(it->{
            assertEquals("Twitter", it.getQuery());
            assertEquals(10L, it.getSinceId().longValue());
        });

        Date _now = new Date();
        TweetSearchParameterEntity updatedEntity = TweetSearchParameterEntity.builder()
                .query("Twitter")
                .sinceId(100L)
                .build();

        repository.save(updatedEntity);

        Optional<TweetSearchParameterEntity> _updatedEntity = repository.findById("Twitter");
        Assert.assertTrue(_entity.isPresent());

        _entity.ifPresent(it->{
            assertEquals("Twitter", it.getQuery());
            assertEquals(100L, it.getSinceId().longValue());
        });

    }
}
