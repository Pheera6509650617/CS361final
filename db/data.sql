drop table if exists `users`;
create table users (
	id int PRIMARY KEY AUTO_INCREMENT,
	gmail VARCHAR(100) UNIQUE,
	username VARCHAR(100) UNIQUE,
	password VARCHAR(100),
	apiKey VARCHAR(100),
	Image TEXT
);

create table post (
	postId int PRIMARY KEY AUTO_INCREMENT,
	postOwnerID int,
	dateTime VARCHAR(100),
	content VARCHAR(300),
	Image TEXT
);