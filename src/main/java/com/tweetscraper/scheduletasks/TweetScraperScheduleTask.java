package com.tweetscraper.scheduletasks;

import java.nio.charset.Charset;
import java.util.Random;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.tweetscraper.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tweetscraper.entity.TweetUserEntity;
import com.tweetscraper.repository.TweetUserRepository;

@Component
public class TweetScraperScheduleTask {

	@Autowired
	private TweetUserRepository tweetUserRepository;

	// 1hr interval between invocations measured from the completion of the task
	// 1hr = 60*60*1000
	//@Scheduled(fixedDelay = 60*60*1000)
	@Scheduled(fixedDelay = 5000)
	public void scrapeTweets() {
		TweetUserEntity user = new TweetUserEntity();
		user.setName(randomString(10));
		user.setEmail(randomString(20));

		tweetUserRepository.save(user);
	}

	private String randomString(Integer length) {
		byte[] array = new byte[length]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));
	}
}
