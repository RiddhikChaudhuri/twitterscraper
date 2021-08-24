package com.tweetscraper.service.implementation;

import com.tweetscraper.service.ImageService;
import org.apache.commons.io.FilenameUtils;
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

    @Override
    public String downloadAndSave(String urlString) {
        try {
            URL url = new URL(urlString);
            File file = new File(imageDirectory,FilenameUtils.getName(url.getPath()));
            ImageIO.write(ImageIO.read(url), FilenameUtils.getExtension(url.getPath()), file);
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}