package com.tweetscraper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TweetImageId implements Serializable {
    @Column(name = "TWEET_ID")
    @EqualsAndHashCode.Include
    private Long tweetId;

    @Column(name = "IMAGE_URL")
    @EqualsAndHashCode.Include
    private String imageUrl;
}
