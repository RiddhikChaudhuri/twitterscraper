package com.tweetscraper.service;

import com.tweetscraper.dto.Image;
import com.tweetscraper.entity.ImageEntity;

public interface ImageService {
    ImageEntity downloadAndSave(Image image);
}
