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
	userid INT(6) PRIMARY KEY NOT NULL,
	username VARCHAR(40) NOT NULL,
	password VARCHAR(40) NOT NULL,
	role ENUM('admin', 'professor', 'student') NOT NULL,
	salt VARCHAR(8) UNIQUE NOT NULL
);
	
CREATE TABLE Courses (
	courseid VARCHAR(6) PRIMARY KEY NOT NULL,
	course_name VARCHAR(40) NOT NULL,
	description VARCHAR(150) NOT NULL,
	profid INT(6) NOT NULL,
	FOREIGN KEY fk_prof(profid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);
	
CREATE TABLE Blocks (
	blockid INT(4) AUTO_INCREMENT NOT NULL,
	courseid VARCHAR(6),
	time VARCHAR(9),
	day ENUM ('mon', 'tue', 'wed', 'thu', 'fri'),
	room VARCHAR(4) NOT NULL,
	FOREIGN KEY fk_courses_block(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (blockid)
	
);

CREATE TABLE Classes (
	blockid INT(4),
	stuid INT(6),
	FOREIGN KEY fk_student_classes(stuid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY fk_blockid_classes(blockid)
	REFERENCES Blocks(blockid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (blockid, stuid)
);

CREATE TABLE GradeItems (
	itemid INT(6),
	courseid VARCHAR(6),
	name VARCHAR(40) NOT NULL,
	total INT(3),
	weight INT(3),
	FOREIGN KEY fk_courses_gritem(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (itemid)
);

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
);


SHOW TABLES;