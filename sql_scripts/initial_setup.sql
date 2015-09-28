use master;

if db_id('training_application') is not null
	drop database training_application;

create database training_application;
	
use training_application;

if object_id('trainer') is null
	create table trainer(
	trainer_id int identity(1,1) primary key,
	email varchar(255) not null,
	pw varchar(255) not null,
	first_name varchar(255) not null,
	last_name varchar(255) not null);

if object_id('trainingprogram') is null
	create table trainingprogram(
	trainingprogram_id int identity(1,1) primary key,
	trainer_id int foreign key references trainer(trainer_id)
	);

if object_id('customer') is null
	create table customer(
	customer_id int identity(1,1) primary key,
	email varchar(60) not null,
	pw varchar(60) not null,
	first_name varchar(20) not null,
	last_name varchar(20) not null,
	current_weight int not null,
	height int not null,
	date_of_birth date not null,
	sex char(1) not null check (sex in('m', 'f')),
	trainingprogram_id int foreign key references trainingprogram(trainingprogram_id)
	);

if object_id('workout') is null
	create table workout(
	workout_id int identity(1,1) primary key,
	trainingprogram_id int foreign key references trainingprogram(trainingprogram_id),
	workout_name varchar(20) not null,
	workout_description text not null
	);

if object_id('exercise') is null
	create table exercise(
	exercise_id int identity(1,1) primary key,
	exercise_name varchar(20) not null,
	exercise_description text not null
	);

if object_id('exercise_set') is null
	create table exercise_set(
	set_nr int not null,
	exercise_id int foreign key references exercise(exercise_id),
	workout_id int foreign key references workout(workout_id),
	repetitions_planed int not null,
	repetitions_cleared int,
	additional_load int,
	additional_load_cleared int,
	comment_by_user text,
	primary key(set_nr, exercise_id, workout_id)
	);

Use master;