package com.tweetscraper;

import com.tweetscraper.repository.TwitterUserRepository;
import com.tweetscraper.scheduletasks.TweetScraperScheduleTask;
import com.tweetscraper.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ApplicationTest {
    @Autowired
    private TweetScraperScheduleTask tweetScraperScheduleTask;

    @Autowired
    private TwitterUserRepository tweetUserRepository;

    @Autowired
    private ImageService imageService;

    @Test
    public void contextLoads() {
        // Basic integration test that shows the context starts up properly
        assertThat(tweetUserRepository).isNotNull();
        assertThat(imageService).isNotNull();
        assertThat(tweetScraperScheduleTask).isNotNull();
    }
}
