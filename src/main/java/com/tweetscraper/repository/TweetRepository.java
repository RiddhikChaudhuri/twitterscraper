package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetEntity;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<TweetEntity, Long> {
}
