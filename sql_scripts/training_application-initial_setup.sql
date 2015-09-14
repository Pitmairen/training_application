
/*
  If database already exists, disconnect all users, then disconnect from
  the database by connecting to master instead. Finally drop database.
*/
if DB_ID('training_application') IS NOT NULL
BEGIN
	ALTER DATABASE training_application SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
END

USE master;
DROP DATABASE training_application;
GO

CREATE DATABASE training_application;
GO
USE training_application;

CREATE TABLE trainer (
	trainer_id int NOT NULL PRIMARY KEY,
	pw VARCHAR(40) NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	email VARCHAR(40) NOT NULL,
);

CREATE TABLE training_program (
	training_program_id int NOT NULL PRIMARY KEY,
	trainer_id int FOREIGN KEY REFERENCES trainer(trainer_id)
);

CREATE TABLE customer (
	customer_id int NOT NULL PRIMARY KEY,
	training_program_id int FOREIGN KEY REFERENCES training_program(training_program_id),
	pw VARCHAR(40) NOT NULL,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30) NOT NULL,
	email VARCHAR(40) NOT NULL,
	weight int NOT NULL,
	height int NOT NULL,
	date_of_birth int NOT NULL,
	sex BIT NOT NULL,
);

CREATE TABLE workout (
	workout_id INT PRIMARY KEY,
	training_program_id INT NOT NULL FOREIGN KEY REFERENCES training_program(training_program_id),
	workout_date DATE NOT NULL,
	workout_name VARCHAR(30) NOT NULL,
	workout_description VARCHAR(200) NOT NULL,
);

CREATE TABLE exercise (
	exercise_id INT NOT NULL PRIMARY KEY,
	exercise_name VARCHAR(30) NOT NULL,
	exercise_info VARCHAR(200) NOT NULL,
);

CREATE TABLE exercise_set (
	workout_id INT FOREIGN KEY REFERENCES workout(workout_id),
	exercise_id INT FOREIGN KEY REFERENCES exercise(exercise_id),
	repititions INT NOT NULL,
	weight INT NOT NULL,
	repititions_cleared INT NOT NULL,
	comment VARCHAR(200),
);