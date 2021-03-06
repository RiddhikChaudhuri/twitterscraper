package com.tweetscraper.repository;

import com.tweetscraper.entity.UserProfileImageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileImageRepository extends CrudRepository<UserProfileImageEntity, Long> {
}
