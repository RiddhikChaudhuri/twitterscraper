mysql> create database twitter_scraper; -- Creates the new database
mysql> create user 'springuser'@'%' identified by 'ThePassword'; -- Creates the user
mysql> grant all on twitter_scraper.* to 'springuser'@'%'; -- Gives all privileges to the new user on the newly created database