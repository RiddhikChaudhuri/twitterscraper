package com.tweetscraper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TWITTER_CHANNEL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwitterChannelEntity implements Serializable {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "url")
    private String url;

    @Column(name = "FOLLOWER_COUNT")
    private Long followerCount;

}
