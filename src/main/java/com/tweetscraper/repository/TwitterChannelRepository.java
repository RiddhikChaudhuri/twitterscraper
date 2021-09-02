package com.tweetscraper.repository;

import com.tweetscraper.entity.TwitterChannelEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitterChannelRepository extends CrudRepository<TwitterChannelEntity, String> {
}
