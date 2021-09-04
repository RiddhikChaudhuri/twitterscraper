package com.tweetscraper.repository;

import com.tweetscraper.entity.TwitterUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterUserRepository extends CrudRepository<TwitterUserEntity, Long> {
}
