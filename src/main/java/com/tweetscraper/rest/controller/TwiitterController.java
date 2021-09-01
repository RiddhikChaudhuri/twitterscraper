package com.tweetscraper.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetscraper.service.TweetScraperService;

@RestController
@RequestMapping("/test-tweet")
public class TwiitterController {

	@Autowired
	private TweetScraperService tweetService;
	
	
	@GetMapping
	public void getLatestTweetIntoDatabase() {
		tweetService.findTweets("twitterscraper");
	}
}
