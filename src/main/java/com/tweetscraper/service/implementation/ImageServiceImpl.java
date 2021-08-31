package com.tweetscraper.service.implementation;

import com.tweetscraper.dto.Image;
import com.tweetscraper.entity.ImageEntity;
import com.tweetscraper.repository.ImageRepository;
import com.tweetscraper.service.ImageService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${image.directory}")
    private String imageDirectory;
    @Autowired
    ImageRepository imageRepository;

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public ImageEntity downloadAndSave(Image image) {
        try {
            URL url = new URL(image.getImageUrl());
            File file = new File(imageDirectory, FilenameUtils.getName(url.getPath()));
            ImageIO.write(ImageIO.read(url), FilenameUtils.getExtension(url.getPath()), file);
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageUrl(image.getImageUrl());
            imageEntity.setImageLocation(file.getPath());
            imageEntity.setUserId(image.getUserId());

            return imageRepository.save(imageEntity);

        } catch (IOException e) {
            log.error("Unable to download and store image from {0}", image.getImageUrl());
            log.error("Stack Trace", e);
        }
        return null;
    }
}