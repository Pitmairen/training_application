Use master;
DROP database trainingapplication;

IF OBJECT_ID('trainingapplication') IS NULL
	Create DATABASE trainingapplication;
	
USE trainingapplication;

IF OBJECT_ID('trainer') IS NULL
	CREATE TABLE trainer(
	trainer_id int IDENTITY(1,1) PRIMARY KEY,
	email varChar(255) NOT NULL,
	pw varChar(255) NOT NULL,
	first_name varChar(255) NOT NULL,
	last_name varChar(255) NOT NULL);

IF OBJECT_ID('trainingprogram') IS NULL
	CREATE TABLE trainingprogram(
	trainingprogram_id int IDENTITY(1,1) PRIMARY KEY,
	trainer_id int FOREIGN KEY REFERENCES trainingprogram(trainingprogram_id)
	);

IF OBJECT_ID('customer') IS NULL
	CREATE TABLE customer(
	customer_id int IDENTITY(1,1) PRIMARY KEY,
	email varChar(60) NOT NULL,
	pw varChar(60) NOT NULL,
	first_name varChar(20) NOT NULL,
	last_name varChar(20) NOT NULL,
	current_weight int NOT NULL,
	height int NOT NULL,
	date_Of_birth Date NOT NULL,
	sex varChar(20) NOT NULL,
	trainingprogram_id int FOREIGN KEY REFERENCES trainingprogram(trainingprogram_id)
	);

IF OBJECT_ID('workout') IS NULL
	CREATE TABLE workout(
	workout_id int IDENTITY(1,1) PRIMARY KEY,
	trainingprogram_id int FOREIGN KEY REFERENCES trainingprogram(trainingprogram_id),
	workout_name varChar(20) NOT NULL,
	workout_description varChar(150) NOT NULL
	);

IF OBJECT_ID('exercise') IS NULL
	CREATE TABLE exercise(
	exercise_id int IDENTITY(1,1) PRIMARY KEY,
	exercise_name varChar(20) NOT NULL,
	exercise_description varChar(300) NOT NULL
	);

IF OBJECT_ID('exercise_set') IS NULL
	CREATE TABLE exercise_set(
	exercise_id int FOREIGN KEY REFERENCES exercise(exercise_id),
	workout_id int FOREIGN KEY REFERENCES workout(workout_id),
	repetitions_planed int NOT NULL,
	repetitions_cleared int,
	additional_load int,
	comment varChar(50)
	);


insert into trainer(email, pw, first_name, last_name)
values ('ultimate.trainer@gmail.com', '1234', 'Jack', 'Strong'),
('matt.forrest@gmail.com','PushUpsIsCool', 'Matt', 'Forrest'),
('natalie.northwood@gmail.com','VeryLongPassword','Natalie','Northwood');

insert into trainingprogram(trainer_id)
values (1),(2),(3);

insert into customer(email, pw, first_name, last_name, current_weight, height, date_Of_birth, sex, trainingprogram_id)
values ('fancypants@gmail.com', 'DontAsk', 'Tiffany', 'McDonald', '55', '157', '1989-02-23', 'female', 1),
('duke.davidson@gmail.com', 'DogsAreCool', 'Duke', 'Davidson', '68', '179', '1976-12-05', 'male', 1),
('s.harrison@gmail.com', 'D45fsq23s54', 'Sam', 'Harrison', '77', '164', '1954-07-19', 'male', 1);

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

insert into exercise_set(exercise_id, workout_id, repetitions_planed, repetitions_cleared, additional_load)
values (1, 1, 60, null, null),
(4, 1, 15, null, null),
(2, 2, 20, null, null),
(5, 2, 5, null, null),
(3, 3, 20, null, null),
(6, 3, 60, null, null);