# command syntax to run this mysql file:
#
# PRECONDITION: must log in to mysql and has CREATE privilege
#
# CMD:
# mysql> source file/path/to/add_data.sql;
# DO NOT USE \ aka backward slash. It does not work for mysql.
# Use forward slash aka /

USE munimoe_LMS;

# Data for Users: 3 admins, 3 professors, 6 students.
INSERT INTO Users (firstname, lastname, password, role, salt)
  VALUES
	("thomas_bui", "password", "admin", "12345678"),
	("haonan_zhong", "qwerty", "admin", "abcdefgh"),
	("bosco_kwan", "12345", "admin", "33221144"),
	("isacc_newton", "calculus is mine", "professor", "33rf114a"),
	("gottfried_leibniz", "i created calculus", "professor", "89rf174a"),
	("alan_turing", "123enigma321", "professor", "moonkooj"),
	("grace_hooper", "assembly_goddess", "professor", "zxcvbnm?"),
	("john_smith", "my_password", "student", "noios14a"),
	("amanda_waller", "argus_is_the_best", "student", "j&*!KJOd"),
	("robert_langdon", "HISTORY!!", "student", "koHUWkj9"),
	("angelica_schyler", "Burn_six_sept", "student", "fecs1rwq"),
	("leeroy_jenkins", "atLeastIHaveChicken", "student", "3vwe1257"),
	("hermione_granger", "Wingardium Levi-O-sa", "student", "p9*^73xs");

# Data for Courses: 3 courses, 2 of them has lecture and lab versions.

INSERT INTO Courses VALUES 
	("COA101", "Intro to Computer Architecture", "A beginners' class on how computers work.", 6),
	("MAT203", "Intermediate Calculus (LEC)", "A deeper look at Calculus and its application to real-life problems. This is the lecture version.", 4),
	("MAT204", "Intermediate Calculus (LAB)", "A deeper look at Calculus and its application to real-life problems. This is the lab version.", 5),
	("ASB311", "Advanced Assembly", "For people interested in mastering assembly languages.", 7);


# Data for classes. Not all students are enrolled in a class for testing purpose 

INSERT INTO Enrolment (courseid, stuid) VALUES
	("COA101", 8),
	("COA101", 10),
	("COA101", 11),
	("COA101", 12),
	("COA101", 13),
	("MAT203", 9),
	("MAT203", 11),
	("MAT203", 13),
	("MAT204", 9),
	("MAT204", 11),
	("MAT204", 13),
	("ASB311", 10),
	("ASB311", 11),
	("ASB311", 13);

# Data for GradeItems. Based on students enrolled in a class:

INSERT INTO GradeItems (courseid, name, total, weight) VALUES
	("ASB311", "Review Quiz", 10, 20),
	("ASB311", "Lab 1", 10, 20),
	("ASB311", "Lab 2", 10, 30),
	("COA101", "Quiz 1", 15, 50),
	("MAT204", "Integral Quiz 1", 15, 25),
	("MAT204", "Integral Quiz 2", 15, 25),
	("MAT204", "Integral Test", 20, 25);
	("MAT204", "Derivative Quiz 1", 15, 25);

# Data for StuGrades

INSERT INTO StuGrades (stuid, itemid, grade) VALUES
  (8, 4, 10),
  (10, 4, 11),
  (11, 4, 13),
  (12, 4, 13),
  (13, 4, 15),
  (9, 5, 12),
  (11, 5, 13),
  (13, 5, 15),
  (9, 6, 13),
  (11, 6, 14),
  (13, 6, 15),
  (9, 7, 16),
  (11, 7, 17),
  (13, 7, 20),
  (9, 8, 13),
  (11, 8, 11),
  (13, 8, 16),
  (10, 1, 8),
  (11, 1, 7),
  (13, 1, 10),
  (10, 2, 7),
  (11, 2, 7),
  (13, 2, 10),
  (10, 3, 9),
  (11, 3, 9),
  (13, 3, 10);
  
