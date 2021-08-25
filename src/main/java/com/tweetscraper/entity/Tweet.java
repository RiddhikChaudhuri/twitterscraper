package com.tweetscraper.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tweet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Tweet {

	@Id
	/*Internally maintained Id which is different from Tweet Id*/
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="text")
	private String text;
	
	@Column(name="tweetId")
	private Long tweetId;
	
	private LocalDateTime updatedOn;
	
	private LocalDateTime createdOn;
	
	
	private Long timestamp;
	
	@Column(name="language")
	private String lang;
	
	
	private String pictureUrl;
	
	private String picturePath;
	
	private String screenName;
	
	@PrePersist
	public void beforeCreate() {
		this.createdOn = LocalDateTime.now();
		this.updatedOn = LocalDateTime.now();
	}
	
	@PreUpdate
	public void beforeUpdate() {
		this.updatedOn = LocalDateTime.now();
	}
}