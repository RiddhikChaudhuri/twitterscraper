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

@Entity
@Table(name = "tweet")
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class TweetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "text")
	private String text;
	
	@Column(name = "tweet_id")
	private Long tweetId;
	
	@Column(name = "lang")
	private String lang;
	
    @Column(name = "image_url")
	private String pictureUrl;
	
    @Column(name = "image_location")
	private String picturePath;
	
	@Column(name="screen_name")
	private String screenName;
	
	@Column(name="retweet_count")
	private Integer retweetCount;
	
	@Column(name="favorite_count")
	private Integer favoriteCount;
}