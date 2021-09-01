package com.tweetscraper.repository;

import com.tweetscraper.entity.UserProfileImageEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileImageRepository extends CrudRepository<UserProfileImageEntity, Long> {
}
