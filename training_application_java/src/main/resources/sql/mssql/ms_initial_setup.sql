
/* If database already exists, disconnect all users, then disconnect from
   the database by connecting to master database. Finally drop database. */
if DB_ID('training_application') IS NOT NULL
BEGIN
    ALTER DATABASE training_application SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
END
USE master;
DROP DATABASE training_application;
GO
CREATE DATABASE training_application;
GO

/* Start creating the database with the necessary tables. */
USE training_application;

CREATE TABLE program(
    program_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,

    program_name VARCHAR(100) NOT NULL,
    program_description TEXT NOT NULL
);

CREATE TABLE customer (
    customer_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    customer_program_id INT FOREIGN KEY REFERENCES program(program_id),

    customer_first_name VARCHAR(50) NOT NULL,
    customer_last_name VARCHAR(50) NOT NULL,
    customer_pw VARCHAR(255) NOT NULL,
    customer_email VARCHAR(100) NOT NULL UNIQUE,

    customer_weight INT NOT NULL,
    customer_height INT NOT NULL,
    customer_date_of_birth DATE NOT NULL,
    customer_sex CHAR(1) NOT NULL CHECK(customer_sex IN('f', 'm'))
);

CREATE TABLE workout (
    workout_id INT IDENTITY(1,1) PRIMARY KEY,
    workout_program_id INT NOT NULL FOREIGN KEY REFERENCES program(program_id),

    workout_name VARCHAR(50) NOT NULL,
    workout_description TEXT NOT NULL,    
    workout_date DATE NOT NULL,
    
    workout_comment TEXT DEFAULT '' NOT NULL,
    workout_done BIT DEFAULT 0 NOT NULL
);

CREATE TABLE exercise (
    exercise_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,

    exercise_name VARCHAR(50) NOT NULL,
    exercise_description TEXT NOT NULL
);

CREATE TABLE exercise_set (
    set_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,

    set_workout_id INT FOREIGN KEY REFERENCES workout(workout_id),
    set_exercise_id INT FOREIGN KEY REFERENCES exercise(exercise_id),

    set_reps_planned INT NOT NULL,
    set_reps_done INT NULL,

    set_weight_planned INT NOT NULL,
    set_weight_done INT NULL,

);

USE master;