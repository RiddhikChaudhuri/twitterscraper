package com.tweetscraper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TweetServiceLiveTest {
	
	@Autowired
	private TwitterTemplateCreator twitterCreator;

	@Test
	public void whenTweeting_thenNoExceptions() {
		Twitter twitterTemplate = twitterCreator.getTwitterTemplate("twitterscraper");
		System.out.println(twitterTemplate.isAuthorized());
	}
}