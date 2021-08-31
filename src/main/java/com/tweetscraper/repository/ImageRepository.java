package com.tweetscraper.repository;

import com.tweetscraper.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<ImageEntity, Integer> {
}
