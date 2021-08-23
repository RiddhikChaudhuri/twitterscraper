#### Execute below mentioned statement to create database, application user & granting permission

** Creates the new database **

	create database twitter_scraper;

** Creates the user **

	create user 'springuser'@'%' identified by 'ThePassword';

**  Gives all privileges to the new user on the newly created database **

	grant all on twitter_scraper.* to 'springuser'@'%';
