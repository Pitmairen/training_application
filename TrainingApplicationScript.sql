IF DB_ID('trainingapplication') IS NULL
	Create DATABASE trainingapplication;

IF OBJECT_ID('trainer') IS NULL
	CREATE TABLE trainer(
	trainer_id int IDENTITY(1,1) PRIMARY KEY,
	email varChar(20) NOT NULL,
	pw varChar(20) NOT NULL,
	first_name varChar(20) NOT NULL,
	last_name varChar(20) NOT NULL,
	);

IF OBJECT_ID('trainingprogram') IS NULL
	CREATE TABLE trainingprogram(
	trainingprogram_id int IDENTITY(1,1) PRIMARY KEY,
	trainer_id int FOREIGN KEY REFERENCES trainingprogram(trainingprogram_id)
	);

IF OBJECT_ID('customer') IS NULL
	CREATE TABLE customer(
	customer_id int IDENTITY(1,1) PRIMARY KEY,
	email varChar(20) NOT NULL,
	pw varChar(20) NOT NULL,
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
	exercise_description varChar(150) NOT NULL
	);

IF OBJECT_ID('exercise_set') IS NULL
	CREATE TABLE exercise_set(
	exercise_id int FOREIGN KEY REFERENCES exercise(exercise_id),
	workout_id int FOREIGN KEY REFERENCES workout(workout_id),
	repetitions_planed int NOT NULL,
	repetitions_cleared int NOT NULL,
	additional_load int,
	comment varChar(50)
	);

