package com.tweetscraper.scheduletasks;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import com.tweetscraper.config.TwitterTemplateCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import com.tweetscraper.repository.TwitterUserRepository;

@Component
public class TweetScraperScheduleTask {

	@Autowired
	private TwitterUserRepository tweetUserRepository;
	
	@Autowired
	private TwitterTemplateCreator twitterTemplateCreator;

	// 1hr interval between invocations measured from the completion of the task
	// 1hr = 60*60*1000
	//@Scheduled(fixedDelay = 60*60*1000)
	@Scheduled(fixedDelay = 5000)
	public void scrapeTweets() {
	/*
		TweeterUserEntity user = new TweeterUserEntity();
		user.setName(randomString(10));
		user.setEmail(randomString(20));

		tweetUserRepository.save(user);
	*/
	}

	private String randomString(Integer length) {
		byte[] array = new byte[length]; // length is bounded by 7
		new Random().nextBytes(array);
		return new String(array, Charset.forName("UTF-8"));
	}
	
	public ArrayList<Long> getIdList(String userName) {

		Twitter twitter = twitterTemplateCreator.getTwitterTemplate(userName);
		long cursor = -1;
		CursoredList<Long> listId;
		ArrayList<Long> idList = new ArrayList<>();

		do {
			listId = twitter.friendOperations().getFollowerIdsInCursor("Riddhikgunners", cursor);
			for (Long ids : listId) {
				idList.add(ids);
			}

		} while ((cursor = listId.getNextCursor()) != 0);

		return idList;
	}
}
