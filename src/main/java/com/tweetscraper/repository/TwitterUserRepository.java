package com.tweetscraper.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tweetscraper.entity.TwitterUserEntity;

import java.util.Optional;

@Repository
public interface TwitterUserRepository extends CrudRepository<TwitterUserEntity, Integer> {

    Optional<TwitterUserEntity> findByUserId(int i);
}
