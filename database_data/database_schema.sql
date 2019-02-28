CREATE DATABASE learn_sys;

USE learn_sys;

CREATE TABLE Users (
	userid VARCHAR(6) PRIMARY KEY,
	username VARCHAR(40) NOT NULL,
	password VARCHAR(40) NOT NULL,
	role ENUM('admin', 'professor', 'student') NOT NULL,
	salt VARCHAR(8) UNIQUE NOT NULL
);
	
CREATE TABLE Courses (
	courseid VARCHAR(6) PRIMARY KEY,
	course_name VARCHAR(40) NOT NULL,
	description VARCHAR(150) NOT NULL,
	userid VARCHAR(6) NOT NULL,
	FOREIGN KEY fk_prof(userid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE
);
	
CREATE TABLE Blocks (
	blockid INT(4) AUTO_INCREMENT,
	courseid VARCHAR(6),
	time VARCHAR(9),
	day ENUM ('mon', 'tue', 'wed', 'thu', 'fri'),
	room VARCHAR(4) NOT NULL,
	FOREIGN KEY fk_courses_block(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (blockid, courseid)
	
);

CREATE TABLE Classes (
	blockid INT(4),
	userid VARCHAR(6),
	FOREIGN KEY fk_student_classes(userid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY fk_blockid_classes(blockid)
	REFERENCES Blocks(blockid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (blockid, userid)
);

CREATE TABLE AllGrades (
	userid VARCHAR(6),
	courseid VARCHAR(6),
	final_grade DECIMAL(4, 2),
	FOREIGN KEY fk_student_agrade(userid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY fk_courses_agrade(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (userid, courseid)
);

CREATE TABLE GradeItems (
	userid VARCHAR(6),
	courseid VARCHAR(6),
	name VARCHAR(40) NOT NULL,
	type ENUM('quiz', 'test', 'project', 'midterm', 'finals'),
	marks VARCHAR(10) CHECK (marks LIKE '%/%'),
	weight VARCHAR(5) CHECK (weight LIKE '%\%'),
	FOREIGN KEY fk_student_gritem(userid)
	REFERENCES Users(userid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	FOREIGN KEY fk_courses_gritem(courseid)
	REFERENCES Courses(courseid)
	ON UPDATE CASCADE
	ON DELETE CASCADE,
	PRIMARY KEY (userid, courseid, name)
);

SHOW TABLES;