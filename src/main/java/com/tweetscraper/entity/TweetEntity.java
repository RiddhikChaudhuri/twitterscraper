package com.tweetscraper.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "tweet")
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class TweetEntity {

	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "text")
	private String text;

	@Column(name = "link")
	private String link;

	@Column(name = "lang")
	private String lang;

	@Column(name="retweet_count")
	private Integer retweetCount;
	
	@Column(name="favorite_count")
	private Integer favoriteCount;

	@Column(name="retweeted")
	private Boolean retweeted;

	@Column(name="favorited")
	private Boolean favorited;

	@Column(name = "twitter_user_id")
	private Long twitterUserId;

	@Column(name = "twitter_user_name")
	private String twitterUserName;

	@Column(name = "created_at")
	private Date createdAt;

	// For retweets

	@Column(name = "original_twitter_name")
	private String originalTwitterName;

	@Column(name = "original_link")
	private String originalLink;

	@Column(name = "original_created_at")
	private Date originalCreatedAt;

	@Column(name = "original_text")
	private String originalText;

}