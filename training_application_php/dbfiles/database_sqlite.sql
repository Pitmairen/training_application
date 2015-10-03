

CREATE TABLE program(
    program_id INTEGER PRIMARY KEY,
    program_name VARCHAR(100) NOT NULL,
    program_desc TEXT NOT NULL

);


CREATE TABLE user(
    user_id INTEGER PRIMARY KEY,
    user_program_id INTEGER NOT NULL,

    user_email VARCHAR(100) NOT NULL,
    user_pw VARCHAR(255) NOT NULL,
    user_first_name VARCHAR(50) NOT NULL,
    user_last_name VARCHAR(50) NOT NULL,

    user_weight INTEGER NOT NULL,
    user_height INTEGER NOT NULL,
    user_date_of_birth DATE NOT NULL,
    user_sex VARCHAR NOT NULL CHECK(user_sex IN ('m', 'f')),


    FOREIGN KEY (user_program_id) REFERENCES program(program_id)
);

CREATE INDEX u_program_idx ON user(user_program_id);
CREATE UNIQUE INDEX u_email_idx ON user(user_email);


CREATE TABLE workout(
    workout_id INTEGER PRIMARY KEY,
    workout_program_id INTEGER NOT NULL, 

    workout_name VARCHAR(50) NOT NULL,
    workout_desc TEXT NOT NULL,
    workout_date DATETIME NOT NULL,

    workout_comment TEXT DEFAULT '' NOT NULL,
    workout_done BOOLEAN DEFAULT 0 NOT NULL,

    FOREIGN KEY (workout_program_id) REFERENCES program(program_id)
);

CREATE INDEX w_program_idx ON workout(workout_program_id);
CREATE INDEX w_done_idx ON workout(workout_done);


CREATE TABLE exercise(
    exercise_id INTEGER PRIMARY KEY,
    exercise_name VARCHAR(50) NOT NULL,
    exercise_desc TEXT NOT NULL
);


CREATE TABLE exercise_set(
    set_id INTEGER PRIMARY KEY,

    set_nr INTEGER NOT NULL,
    set_exercise_id INTEGER NOT NULL,
    set_workout_id INTEGER NOT NULL,

    set_reps_planned INTEGER NOT NULL,
    set_reps_done INTEGER NULL,

    set_weight_planned INTEGER NOT NULL,
    set_weight_done INTEGER NULL,

    set_duration_planned INTEGER NOT NULL,
    set_duration_done INTEGER NULL,

    FOREIGN KEY (set_workout_id) REFERENCES workout(workout_id),
    FOREIGN KEY (set_exercise_id) REFERENCES exercise(exercise_id)

);

CREATE UNIQUE INDEX s_uniq_idx ON exercise_set(set_nr, set_exercise_id, set_workout_id);
CREATE INDEX s_exercise_idx ON exercise_set(set_exercise_id);
CREATE INDEX s_workout_idx ON exercise_set(set_workout_id);



INSERT INTO program(program_name, program_desc) VALUES
    ('Test Program', 'This is a test'),
    ('Empty Program', 'Dummy for new users');


INSERT INTO user(user_program_id, user_email, user_pw, user_first_name, user_last_name, user_weight, user_height, user_date_Of_birth, user_sex) VALUES
    (1, 'duke@gmail.com', '$2y$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO', 'Duke', 'Davidson', 68, 179, '1976-12-05', 'm'),
    (2, 'fancypants@gmail.com', '$2y$10$nxHZlY0Tr.fvcYJNgYJDZO7k1MxS4HuUYHeuk7ZncPuGJ9YfIPeE6', 'Tiffany', 'McDonald', 55, 157, '1989-02-23', 'f'),
    (2, 's.harrison@gmail.com', '$2y$10$p1MX36OjGtkakgGiZ9zjUOqRqBqyj8bGTuvHsgvSaPwx1nwklR.he', 'Sam', 'Harrison', 77, 164, '1954-07-19', 'm');


INSERT INTO workout(workout_program_id, workout_name, workout_desc, workout_date) VALUES
    (1, 'Legs of Endurance', 'Workout program with exercises that improves the endurence in your legs', date("now", "+1 day")),
    (1, 'Arms of Strength', 'Workout program with exercises that improves your arm strength', date("now", "+3 day")),
    (1, 'Abs of Steel', 'Workout program with exercises that gives you well defined abs', date("now", "+5 day"));



INSERT INTO exercise(exercise_name, exercise_desc) VALUES
    ('The 90-degree', 'Find a wall, support your back against it and make sure you have 90 degrees between the following: your legs and the floor, your legs and thighs. Hold for x number of seconds'),
    ('Push-ups', 'Lay down and use your arms to push your body up, keep body stright. You are now in starting position. Now use your arms to lower your chest down towards the floor and then up agian, repeat x times.'),
    ('Sit-ups', 'Lay on your back, hold arms crossed on your chest and use your abs to move your upper body up from the floor and down again.'),
    ('Squats', 'Take a wide stance with your legs, then bend down with your ass outwards and move up again, repeat x times.'),
    ('Pull-ups', 'Find something like horizontal bar you can hang from. Hang and pull your weight up so your head is above the bar, then lower yourself down and repeat x times'),
    ('The plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for x seconds');


INSERT INTO exercise_set(set_workout_id, set_nr, set_exercise_id, set_reps_planned,
    set_reps_done, set_weight_planned, set_weight_done, set_duration_planned, set_duration_done) VALUES

    --- Day 1
    (1, 1, 1, 20, null, 0, null, 0, null),
    (1, 2, 1, 20, null, 0, null, 0, null),
    (1, 3, 1, 20, null, 0, null, 0, null),

    (1, 1, 2, 20, null, 0, null, 0, null),
    (1, 2, 2, 20, null, 0, null, 0, null),
    (1, 3, 2, 20, null, 0, null, 0, null),

    (1, 1, 4, 10, null, 80, null, 0, null),
    (1, 2, 4, 10, null, 80, null, 0, null),
    (1, 3, 4, 10, null, 80, null, 0, null),

    --- Day 2

    (2, 1, 2, 20, null, 0, null, 0, null),
    (2, 2, 2, 20, null, 0, null, 0, null),
    (2, 3, 2, 20, null, 0, null, 0, null),

    (2, 1, 5, 20, null, 0, null, 0, null),
    (2, 2, 5, 20, null, 0, null, 0, null),
    (2, 3, 5, 20, null, 0, null, 0, null),

    (2, 1, 6, 1, null, 0, null, 120, null),
    (2, 2, 6, 1, null, 0, null, 120, null),
    (2, 3, 6, 1, null, 0, null, 120, null),

    ---- Day 3

    (3, 1, 3, 20, null, 0, null, 0, null),
    (3, 2, 3, 20, null, 0, null, 0, null),
    (3, 3, 3, 20, null, 0, null, 0, null),

    (3, 1, 6, 1, null, 0, null, 120, null),
    (3, 2, 6, 1, null, 0, null, 120, null),
    (3, 3, 6, 1, null, 0, null, 120, null),

    (3, 1, 4, 10, null, 80, null, 0, null),
    (3, 2, 4, 10, null, 80, null, 0, null),
    (3, 3, 4, 10, null, 80, null, 0, null);


