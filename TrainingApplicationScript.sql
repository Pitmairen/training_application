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
	set_id int not null,
	exercise_id int foreign key references exercise(exercise_id),
	workout_id int foreign key references workout(workout_id),
	repetitions_planed int not null,
	repetitions_cleared int,
	additional_load int,
	comment_by_user text,
	primary key(set_id, exercise_id, workout_id)
	);


insert into trainer(email, pw, first_name, last_name)
values ('ultimate.trainer@gmail.com', '1234', 'Jack', 'Strong'),
('matt.forrest@gmail.com','PushUpsIsCool', 'Matt', 'Forrest'),
('natalie.northwood@gmail.com','VeryLongPassword','Natalie','Northwood');

insert into trainingprogram(trainer_id)
values (1),(2),(3);

insert into customer(email, pw, first_name, last_name, current_weight, height, date_Of_birth, sex, trainingprogram_id)
values ('fancypants@gmail.com', 'DontAsk', 'Tiffany', 'McDonald', '55', '157', '1989-02-23', 'f', 1),
('duke.davidson@gmail.com', 'DogsAreCool', 'Duke', 'Davidson', '68', '179', '1976-12-05', 'm', 1),
('s.harrison@gmail.com', 'D45fsq23s54', 'Sam', 'Harrison', '77', '164', '1954-07-19', 'm', 1);

insert into workout(trainingprogram_id, workout_name, workout_description)
values(1, 'Legs of Endurance', 'Workout program with exercises that improves the endurence in your legs'),
(1, 'Arms of Strength', 'Workout program with exercises that improves your arm strength'),
(1, 'Abs of Steel', 'Workout program with exercises that gives you well defined abs');

insert into exercise(exercise_name, exercise_description)
values ('The 90-degree', 'Find a wall, support your back against it and make sure you have 90 degrees between the following: your legs and the floor, your legs and thighs. Hold for x number of seconds'),
('Push-ups', 'Lay down and use your arms to push your body up, keep body stright. You are now in starting position. Now use your arms to lower your chest down towards the floor and then up agian, repeat x times.'),
('Sit-ups', 'Lay on your back, hold arms crossed on your chest and use your abs to move your upper body up from the floor and down again.'),
('Squats', 'Take a wide stance with your legs, then bend down with your ass outwards and move up again, repeat x times.'),
('Pull-ups', 'Find something like horizontal bar you can hang from. Hang and pull your weight up so your head is above the bar, then lower yourself down and repeat x times'),
('The plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for x seconds');

insert into exercise_set(set_id, exercise_id, workout_id, repetitions_planed, repetitions_cleared, additional_load, comment_by_user)
values (1, 1, 1, 60, null, null, null),
(1, 4, 1, 15, null, null, null),
(1, 2, 2, 20, null, null, null),
(1, 5, 2, 5, null, null, null),
(1, 3, 3, 20, null, null, null),
(1, 6, 3, 60, null, null, null);

Use master;