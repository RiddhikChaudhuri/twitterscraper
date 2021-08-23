package com.tweetscraper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tweetscraper.entity.TweetUserEntity;

@Repository
public interface TweetUserRepository extends CrudRepository<TweetUserEntity, Integer> {

}
