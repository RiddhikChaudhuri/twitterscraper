package com.tweetscraper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.social.twitter.api.SearchMetadata;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class TweetResponse implements Serializable {

    @JsonProperty("statuses")
    private List<Tweet> tweets;

    @JsonProperty("search_metadata")
    private SearchMetadata searchMetadata;

}
