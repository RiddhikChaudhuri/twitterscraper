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
@Table(name = "twitter_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwitterUserEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2370861793047189128L;

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
}
