package com.tweetscraper.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetscraper.service.TweetScraperService;

@RestController
@RequestMapping("/test-tweet")
public class TwitterController {

	@Autowired
	private TweetScraperService tweetService;

	@Value("${twitter.account.name}")
	private String accountName;
	
	@GetMapping
	public void getLatestTweetIntoDatabase() {
		tweetService.findTweets(accountName);
	}
}
