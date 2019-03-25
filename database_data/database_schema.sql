# command syntax to run this mysql file:
#
# PRECONDITION: must log in to mysql and has CREATE privilege
#
# CMD:
# mysql> source file/path/to/database_schema.sql;
# DO NOT USE \ aka backward slash. It does not work for mysql.
# Use forward slash aka /

USE munimoe_LMS;

CREATE TABLE Users (
	userid INT(6) AUTO_INCREMENT NOT NULL,
	firstname VARCHAR(20) NOT NULL,
	lastname VARCHAR(20) NOT NULL,
	password VARCHAR(40) NOT NULL,
	role ENUM('admin', 'professor', 'student') NOT NULL,
	salt VARCHAR(8) UNIQUE NOT NULL,
	PRIMARY KEY (userid)
) ENGINE = INNODB;
	
CREATE TABLE Courses (
	courseid VARCHAR(6) PRIMARY KEY NOT NULL,
	course_name VARCHAR(40) NOT NULL,
	description VARCHAR(150) NOT NULL,
	profid INT(6) NOT NULL,
	FOREIGN KEY fk_prof(profid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE
) ENGINE = INNODB;

CREATE TABLE Enrolment (
	courseid VARCHAR(6),
	stuid INT(6),
	FOREIGN KEY fk_student_classes(stuid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY fk_courseid_classes(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (courseid, stuid)
) ENGINE = INNODB;

CREATE TABLE GradeItems (
	itemid INT(6) AUTO_INCREMENT,
	courseid VARCHAR(6),
	name VARCHAR(40) NOT NULL,
	total INT(3),
	weight INT(3),
	FOREIGN KEY fk_courses_gritem(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (itemid)
) ENGINE = INNODB;

CREATE TABLE StuGrades (
	stuid INT(6),
	itemid INT(6),
	grade DECIMAL(4, 2),
	FOREIGN KEY fk_studid(stuid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY fk_itemid(itemid)
	REFERENCES GradeItems(itemid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (stuid, itemid)
) ENGINE = INNODB;

SHOW TABLES;