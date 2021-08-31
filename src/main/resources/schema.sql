SET MODE MySQL;

CREATE TABLE `TWITTER_USER` (
    ID INTEGER AUTO_INCREMENT,
    USER_ID INTEGER,
	NAME VARCHAR(500),
	SCREEN_NAME VARCHAR(500),
	PROFILE_IMAGE_URL TEXT,
	PROFILE_IMAGE_URL_HTTPS TEXT,
    URL TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY TWITTER_USER_ID_IDX (USER_ID)
);

CREATE TABLE `IMAGE` (
    ID INTEGER AUTO_INCREMENT,
    USER_ID INTEGER,
	IMAGE_URL TEXT,
	IMAGE_LOCATION TEXT,
    PRIMARY KEY (id)
);
