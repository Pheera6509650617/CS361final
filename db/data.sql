drop table if exists `users`;
create table users (
	id int PRIMARY KEY AUTO_INCREMENT,
	gmail VARCHAR(100) UNIQUE,
	username VARCHAR(100) UNIQUE,
	password VARCHAR(100),
	apiKey VARCHAR(100)
);