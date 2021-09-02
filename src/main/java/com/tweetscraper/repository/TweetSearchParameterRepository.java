package com.tweetscraper.repository;

import com.tweetscraper.entity.TweetSearchParameterEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetSearchParameterRepository extends CrudRepository<TweetSearchParameterEntity, String> {
}
