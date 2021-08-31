package com.tweetscraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Tweet implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private User user;

    @JsonProperty("retweet_count")
    private Integer retweetCount;

    @JsonProperty("favorite_count")
    private Integer favoriteCount;

    @JsonProperty("retweeted")
    private Boolean retweeted;

    @JsonProperty("lang")
    private String lang;
}
