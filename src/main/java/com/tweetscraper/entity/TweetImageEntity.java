package com.tweetscraper.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TWEET_IMAGE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(TweetImageId.class)
public class TweetImageEntity implements Serializable {

    @Id
    @Column(name = "TWEET_ID")
    @EqualsAndHashCode.Include
    private Long tweetId;

    @Id
    @Column(name = "IMAGE_URL")
    @EqualsAndHashCode.Include
    private String imageUrl;

    @Column(name = "image_location")
    private String imageLocation;
}