package com.tweetscraper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tweetscraper.repository.TweetUserRepository;
import com.tweetscraper.scheduletasks.TweetScraperScheduleTask;

@SpringBootTest
public class ApplicationTest {
	@Autowired
	private TweetScraperScheduleTask tweetScraperScheduleTask;
	
	@Autowired
	private TweetUserRepository tweetUserRepository;

	@Test
	public void contextLoads() {
		// Basic integration test that shows the context starts up properly
		assertThat(tweetUserRepository).isNotNull();
		assertThat(tweetScraperScheduleTask).isNotNull();
	}
}
