package com.tweetscraper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TWITTER_CHANNEL")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class TwitterChannelEntity implements Serializable {

    @Id
    @Column(name = "SCREEN_NAME")
    @EqualsAndHashCode.Include
    private String screenName;

    @Column(name = "FOLLOWER_COUNT")
    private Long followerCount;

}
