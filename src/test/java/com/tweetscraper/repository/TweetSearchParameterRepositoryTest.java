package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetSearchParameterEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.test.context.jdbc.Sql;
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
        Date now = new Date();
        TweetSearchParameterEntity entity = TweetSearchParameterEntity.builder()
                .query("Twitter")
                .sinceId(10L)
                .resultType(SearchParameters.ResultType.RECENT.name())
                .includeEntities(true)
                .until(now)
                .build();

        repository.save(entity);

        Optional<TweetSearchParameterEntity> _entity = repository.findById("Twitter");
        Assert.assertTrue(_entity.isPresent());

        _entity.ifPresent(it->{
            assertEquals("Twitter", it.getQuery());
            assertEquals(10L, it.getSinceId().longValue());
            assertEquals(SearchParameters.ResultType.RECENT, SearchParameters.ResultType.valueOf(it.getResultType()));
            assertEquals(true, it.getIncludeEntities());
            assertEquals(now, it.getUntil());
        });

        Date _now = new Date();
        TweetSearchParameterEntity updatedEntity = TweetSearchParameterEntity.builder()
                .query("Twitter")
                .sinceId(100L)
                .resultType(SearchParameters.ResultType.MIXED.name())
                .includeEntities(true)
                .until(_now)
                .build();

        repository.save(updatedEntity);

        Optional<TweetSearchParameterEntity> _updatedEntity = repository.findById("Twitter");
        Assert.assertTrue(_entity.isPresent());

        _entity.ifPresent(it->{
            assertEquals("Twitter", it.getQuery());
            assertEquals(100L, it.getSinceId().longValue());
            assertEquals(SearchParameters.ResultType.MIXED, SearchParameters.ResultType.valueOf(it.getResultType()));
            assertEquals(true, it.getIncludeEntities());
            assertEquals(_now, it.getUntil());
        });

    }
}
