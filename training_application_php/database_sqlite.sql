
CREATE TABLE trainer (
    trainer_id INTEGER PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    pw VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);


CREATE TABLE trainingprogram(
    trainingprogram_id INTEGER PRIMARY KEY,
    trainer_id INTEGER,
    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id)
);


CREATE TABLE customer(
    customer_id INTEGER PRIMARY KEY,
    email VARCHAR(60) NOT NULL,
    pw VARCHAR(255) NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    current_weight INTEGER NOT NULL,
    height INTEGER NOT NULL,
    date_of_birth DATE NOT NULL,
    sex VARCHAR NOT NULL CHECK(sex IN ('m', 'f')),

    trainingprogram_id INTEGER NOT NULL,
    FOREIGN KEY (trainingprogram_id) REFERENCES trainingprogram(trainingprogram_id)
);

CREATE TABLE workout(
    workout_id INTEGER PRIMARY KEY,
    trainingprogram_id INTEGER NOT NULL, 
    workout_name VARCHAR(20) NOT NULL,
    workout_description TEXT NOT NULL,

    FOREIGN KEY (trainingprogram_id) REFERENCES trainingprogram(trainingprogram_id)
);


CREATE TABLE exercise(
    exercise_id INTEGER PRIMARY KEY,
    exercise_name VARCHAR(20) NOT NULL,
    exercise_description TEXT NOT NULL
);


CREATE TABLE exercise_set(
    set_nr INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    workout_id INTEGER NOT NULL,
    repetitions_planed INTEGER NOT NULL,
    repetitions_cleared INTEGER NULL,
    additional_load INTEGER NOT NULL,
    additional_load_cleared INTEGER NULL,
    comment_by_user TEXT,

    FOREIGN KEY (workout_id) REFERENCES workout(workout_id),
    FOREIGN KEY (exercise_id) REFERENCES exercise(exercise_id),

    PRIMARY KEY (set_nr, exercise_id, workout_id)
);





INSERT INTO trainer (email, pw, first_name, last_name) VALUES
    ('ultimate.trainer@gmail.com', '1234', 'Jack', 'Strong'),
    ('matt.forrest@gmail.com','PushUpsIsCool', 'Matt', 'Forrest'),
    ('natalie.northwood@gmail.com','VeryLongPassword','Natalie','Northwood');


INSERT INTO trainingprogram(trainer_id) VALUES
    (1),
    (2),
    (3);


INSERT INTO customer
    (email, pw, first_name, last_name, current_weight, height, date_Of_birth, sex, trainingprogram_id)
    VALUES
    ('fancypants@gmail.com', 'DontAsk', 'Tiffany', 'McDonald', '55', '157', '1989-02-23', 'f', 1),
    ('duke.davidson@gmail.com', 'DogsAreCool', 'Duke', 'Davidson', '68', '179', '1976-12-05', 'm', 2),
    ('s.harrison@gmail.com', '1234', 'Sam', 'Harrison', '77', '164', '1954-07-19', 'm', 3);


INSERT INTO workout(trainingprogram_id, workout_name, workout_description) VALUES
    (1, 'Legs of Endurance', 'Workout program with exercises that improves the endurence in your legs'),
    (2, 'Arms of Strength', 'Workout program with exercises that improves your arm strength'),
    (3, 'Abs of Steel', 'Workout program with exercises that gives you well defined abs');


INSERT INTO exercise(exercise_name, exercise_description) VALUES
    ('The 90-degree', 'Find a wall, support your back against it and make sure you have 90 degrees between the following: your legs and the floor, your legs and thighs. Hold for x number of seconds'),
    ('Push-ups', 'Lay down and use your arms to push your body up, keep body stright. You are now in starting position. Now use your arms to lower your chest down towards the floor and then up agian, repeat x times.'),
    ('Sit-ups', 'Lay on your back, hold arms crossed on your chest and use your abs to move your upper body up from the floor and down again.'),
    ('Squats', 'Take a wide stance with your legs, then bend down with your ass outwards and move up again, repeat x times.'),
    ('Pull-ups', 'Find something like horizontal bar you can hang from. Hang and pull your weight up so your head is above the bar, then lower yourself down and repeat x times'),
    ('The plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for x seconds');


INSERT INTO exercise_set(set_nr, exercise_id, workout_id, repetitions_planed,
    repetitions_cleared, additional_load, additional_load_cleared, comment_by_user) VALUES
    (1, 1, 1, 60, null, 0, 0, null),
    (1, 4, 1, 15, null, 0, 0, null),
    (1, 2, 2, 20, null, 0, 0, null),
    (1, 5, 2, 5, null, 0, 0, null),
    (1, 3, 3, 20, null, 0, 0, null),
    (1, 6, 3, 60, null, 0, 0, null);


