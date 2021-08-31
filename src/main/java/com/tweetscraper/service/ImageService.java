package com.tweetscraper.service;

import com.tweetscraper.entity.ImageEntity;

public interface ImageService {
    ImageEntity downloadAndSave(Long userId, String imageUrl);
}
