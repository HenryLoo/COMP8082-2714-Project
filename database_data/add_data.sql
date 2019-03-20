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

INSERT INTO Users VALUES (0, "thomas_bui", "password", "admin", "12345678"), 
	(1, "haonan_zhong", "qwerty", "admin", "abcdefgh"),
	(2, "bosco_kwan", "12345", "admin", "33221144"),
	(10, "isacc_newton", "calculus is mine", "professor", "33rf114a"),
	(11, "gottfried_leibniz", "i created calculus", "professor", "89rf174a"),
	(12, "alan_turing", "123enigma321", "professor", "moonkooj"),
	(13, "grace_hooper", "assembly_goddess", "professor", "zxcvbnm?"),
	(321434, "john_smith", "my_password", "student", "noios14a"),
	(892379, "amanda_waller", "argus_is_the_best", "student", "j&*!KJOd"),
	(457861, "robert_langdon", "HISTORY!!", "student", "koHUWkj9"),
	(090832, "angelica_schyler", "BuRn_six_sept", "student", "fecs1rwq"),
	(178820, "leeroy_jenkins", "atLeastIHaveChicken", "student", "3vwe1257"),
	(082398, "hermione_granger", "Wingardium Levi-O-sa", "student", "p9*^73xs"),
	(492381, "kevin_le", "something1970", "student", "83jdfuwi"),
	(551146, "angela_liang", "hamilton1776", "student", "cs8esf7s"),
	(679123, "jonathan_rosales", "im_smarty314", "student", "oniuw982"),
	(784547, "jolie_lin", "1astronOMY_anime", "student", "uWuWuWuW"),
	(111791, "graeme_chung", "blender_animation12", "student", "oniichan"),
	(017949, "annie_wong", "cats_and_knives1025", "student", "vshue081");
	
	
# Data for Courses: 3 courses, 2 of them has lecture and lab versions.

INSERT INTO Courses VALUES 
	("COA101", "Intro to Computer Architecture", "A beginners' class on how computers work.", 11),
	("MAT203", "Intermediate Calculus (LEC)", "A deeper look at Calculus and its application to real-life problems. This is the lecture version.", 10),
	("MAT204", "Intermediate Calculus (LAB)", "A deeper look at Calculus and its application to real-life problems. This is the lab version.", 11),
	("ASB311", "Advanced Assembly (LEC)", "For people interested in mastering assembly languages. This is the lecture version.", 12),
	("ASB312", "Advanced Assembly (LAB)", "For people interested in mastering assembly languages. This is the lab version.", 12);
	
	
# Data for Blocks: 16 blocks from the 3 courses.

INSERT INTO Blocks (courseid, time, day, room) VALUES 
	("COA101", "1200-1350", "mon", "A102"),
	("COA101", "0800-0950", "wed", "A102"),
	("COA101", "1300-1450", "wed", "A102"),
	("COA101", "0800-0950", "fri", "A102"),
	("MAT203", "0800-0950", "mon", "B202"),
	("MAT203", "0800-0950", "tue", "B202"),
	("MAT203", "0800-0950", "wed", "B202"),
	("MAT204", "1300-1450", "mon", "B205"),
	("MAT204", "1300-1450", "tue", "B205"),
	("MAT204", "1300-1450", "wed", "B205"),
	("ASB311", "1300-1450", "tue", "C111"),
	("ASB311", "1000-1150", "thu", "C111"),
	("ASB311", "1000-1150", "fri", "C111"),
	("ASB312", "1500-1650", "tue", "C212"),
	("ASB312", "1300-1450", "thu", "B212"),
	("ASB312", "1300-1450", "fri", "B212");
	

# Data for classes. Not all students are enrolled in a class for testing purpose 

INSERT INTO Classes (blockid, userid) VALUES
	(1, 321434),
	(1, 082398),
	(1, 784547),
	(1, 492381),
	(2, 679123),
	(2, 551146),
	(2, 492381),
	(6, 111791),
	(6, 457861),
	(7, 090832),
	(7, 492381),
	(9, 111791),
	(9, 457861),
	(10, 090832),
	(10, 492381),
	(12, 111791),
	(12, 321434),
	(12, 090832),
	(12, 492381),
	(12, 892379),
	(12, 178820),
	(15, 111791),
	(15, 321434),
	(15, 090832),
	(15, 492381),
	(15, 892379),
	(15, 178820);
	
# Data for GradeItems. Based on students enrolled in a class:

INSERT INTO GradeItems (itemid, courseid, name, total, weight) VALUES
	(324951, "ASB311", "Review Quiz", 10, 2),
	(129139, "ASB312", "Lab 1", 10, 2),
	(991283, "ASB312", "Lab 2", 10, 2),
	(312345, "COA101", "Quiz 1", 15, 5),
	(439832, "MAT204", "Integral Quiz 1", 15, 5),
	(224142, "MAT204", "Integral Quiz 2", 15, 5),
	(542375, "MAT204", "Integral Test", 20, 10);

INSERT INTO StuGrades (stuid, itemid, grade) VALUES
	(111791, 324951, 6),
	(178820, 324951, 7),
	(321434, 324951, 6),
	(492381, 324951, 9),
	(892379, 324951, 10),
	(090832, 324951, 7),

	(111791, 129139, 6),
	(178820, 129139, 7),
	(321434, 129139, 5),
	(492381, 129139, 8),
	(892379, 129139, 9),
	(090832, 129139, 7),

	(111791, 991283, 8),
	(178820, 991283, 8),
	(321434, 991283, 7),
	(492381, 991283, 7),
	(892379, 991283, 10),
	(090832, 991283, 8),

	(321434, 312345, 10),
	(492381, 312345, 11),
	(784547, 312345, 13),
	(082398, 312345, 14),
	(551146, 312345, 14),
	(679123, 312345, 13),

	(111791, 439832, 10),
	(457861, 439832, 11),
	(492381, 439832, 13),
	(090832, 439832, 14),

	(111791, 224142, 12),
	(457861, 224142, 13),
	(492381, 224142, 13),
	(090832, 224142, 12),

	(111791, 542375, 15),
	(457861, 542375, 15),
	(492381, 542375, 17),
	(090832, 542375,18);
	
	

 
	

	