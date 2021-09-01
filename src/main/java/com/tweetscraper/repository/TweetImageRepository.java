package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetImageEntity;
import com.tweetscraper.entity.TweetImageId;
import org.springframework.data.repository.CrudRepository;

public interface TweetImageRepository extends CrudRepository<TweetImageEntity, TweetImageId> {
}
