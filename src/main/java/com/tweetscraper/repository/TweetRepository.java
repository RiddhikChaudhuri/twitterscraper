package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends CrudRepository<TweetEntity, Long> {
    @Query(value = "select max(id) from TweetEntity")
    public Long maxTweetId();
}
