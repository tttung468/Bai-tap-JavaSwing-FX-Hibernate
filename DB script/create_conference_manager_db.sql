CREATE DATABASE conference_manager_db;
USE conference_manager_db;

CREATE TABLE places (
	place_id INT(11) NOT NULL AUTO_INCREMENT,
    place_name VARCHAR(50) NOT NULL,
    place_address VARCHAR(50) NOT NULL,
    capacity INT(11) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,

	CONSTRAINT pk_places PRIMARY KEY (place_id)
) ENGINE = INNODB DEFAULT CHARSET=utf8;
INSERT INTO places(place_name, place_address, capacity) VALUES ('Old Trafford stadium','Old Trafford, Greater Manchester, England',5);
INSERT INTO places(place_name, place_address, capacity) VALUES ('Juventus Stadium','Turin, Italy',4);
INSERT INTO places(place_name, place_address, capacity) VALUES ('Santiago Bernabeu stadium','Chamartin, Madrid, Spain',5);
INSERT INTO places(place_name, place_address, capacity) VALUES ('My Dinh stadimum','Nam Tu Liem, Hanoi, Vietnam',2);
INSERT INTO places(place_name, place_address, capacity) VALUES ('Anfield stadium','Anfield, Liverpool, Merseyside, England',1);
INSERT INTO places(place_name, place_address, capacity) VALUES ('Camp Nou stadium','Barcelona, Catalonia, Spain',2);
INSERT INTO places(place_name, place_address, capacity) VALUES ('Allianz Arena stadium','Munich, Bavaria',3);
INSERT INTO places(place_name, place_address, capacity) VALUES ('Wembley Stadium', 'North west London, England',4);

CREATE TABLE conferences (
	conference_id INT(11) NOT NULL AUTO_INCREMENT,
	conference_name VARCHAR(50) NOT NULL,
	brief_description VARCHAR(50) NOT NULL,
	detailed_description VARCHAR(100) NOT NULL,
    image_link VARCHAR(50) NOT NULL,
	organized_time DATE NOT NULL,
	organized_place_id INT(11) NOT NULL,
	registered_attendees INT(11) DEFAULT 0 NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
  
	CONSTRAINT pk_conferences PRIMARY KEY (conference_id),
	CONSTRAINT fk_places FOREIGN KEY (organized_place_id) REFERENCES places(place_id)
) ENGINE = INNODB DEFAULT CHARSET=utf8;
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id, registered_attendees)
VALUES ('1999 UEFA Champions League Final','Match between MU vs Bayer'
,'Man United won the treble of trophies (the Premier League, FA Cup and C1)'
,'image1.jpg', '1999-05-26',1,5);
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id)
VALUES ('G7 summit','G7 = Group of Seven'
,'Core G7 members :Canada, France, Germany, Italy, Japan, UK, US'
,'image2.jpg','2020-02-02',2);
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id)
VALUES ('The Last Dance Tour','The sixth concert tour of Big Bang'
,'The Last Dance Tour was Big Bang''s sixth concert tour in Japan'
,'image3.jpg','2020-03-03',1);
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id)
VALUES ('The 2018 FIFA World Cup','The 2018 FIFA World Cup in Russia'
,'An international football tournament contested by men''s national teams'
,'image4.jpg','2020-04-04',4);
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id)
VALUES ('The 2003 Southeast Asian Games','It was held in Vietnam'
,'The 22nd Southeast Asian Games, It was held in Vietnam'
,'image5.jpg','2020-05-05',5);
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id)
VALUES ('The 2010 FIFA World Cup','Egypt and Morocco host the finals'
,'It was the 19th FIFA World Cup, Egypt and Morocco host the finals'
,'image6.jpg','2020-06-06',8);
INSERT INTO conferences(conference_name, brief_description, detailed_description, image_link, organized_time, organized_place_id)
VALUES ('The 2020 Summer Olympics','officially the Games of the XXXII Olympiad'
,'the Games were rescheduled for 2021 as a result of the COVID-19 pandemic'
,'image7.jpg','2020-07-07',6);

CREATE TABLE admins (
	admin_id INT(11) NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    pass VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,

	CONSTRAINT pk_admins PRIMARY KEY(admin_id)
) ENGINE = INNODB DEFAULT CHARSET=utf8;
INSERT INTO admins(full_name, username, pass, email) VALUES ('Admin 1', 'admin1', '123', 'admin1@gmail.com');
INSERT INTO admins(full_name, username, pass, email) VALUES ('Admin 2', 'admin2', '123', 'admin2@gmail.com');

CREATE TABLE users (
	user_id INT(11) NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    pass VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    is_blocked BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,

	CONSTRAINT pk_users PRIMARY KEY(user_id)
) ENGINE = INNODB DEFAULT CHARSET=utf8;
INSERT INTO users(full_name, username, pass, email) VALUES ('User A', 'userA', '456', 'userA@gmail.com');
INSERT INTO users(full_name, username, pass, email) VALUES ('User B', 'userB', '456', 'userB@gmail.com');
INSERT INTO users(full_name, username, pass, email) VALUES ('User C', 'userC', '456', 'userC@gmail.com');
INSERT INTO users(full_name, username, pass, email, is_blocked) VALUES ('User D', 'userD', '456', 'userD@gmail.com', TRUE);

CREATE TABLE registered_users (
    user_id INT(11) NOT NULL,
    conference_id INT(11) NOT NULL,
    is_accepted BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
	
    CONSTRAINT pk_registered_users PRIMARY KEY(user_id, conference_id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_conferences FOREIGN KEY (conference_id) REFERENCES conferences(conference_id)
) ENGINE = INNODB DEFAULT CHARSET=utf8;
INSERT INTO registered_users (user_id, conference_id, is_accepted) VALUES (1, 1, TRUE);
INSERT INTO registered_users (user_id, conference_id, is_accepted) VALUES (1, 2, TRUE);
INSERT INTO registered_users (user_id, conference_id, is_accepted) VALUES (2, 1, TRUE);
INSERT INTO registered_users (user_id, conference_id) VALUES (3, 1);





DROP DATABASE conference_manager_db;

DROP TABLE admins;
DROP TABLE conferences;
DROP TABLE places;
DROP TABLE registered_users;
DROP TABLE users;


ALTER TABLE places DROP PRIMARY KEY;
ALTER TABLE conferences DROP PRIMARY KEY;
ALTER TABLE admins DROP PRIMARY KEY;
ALTER TABLE users DROP PRIMARY KEY;
ALTER TABLE registered_users DROP PRIMARY KEY;

ALTER TABLE conferences DROP FOREIGN KEY fk_places;
ALTER TABLE registered_users DROP FOREIGN KEY fk_users;
ALTER TABLE registered_users DROP FOREIGN KEY fk_conferences;


