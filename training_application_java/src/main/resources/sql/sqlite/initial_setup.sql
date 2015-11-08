
PRAGMA foreign_keys = ON;

CREATE TABLE program(         --The users overall program. Beginner, advanced, etc.  
    program_id INTEGER PRIMARY KEY,
    program_name VARCHAR(100) NOT NULL,
    program_description TEXT NOT NULL
);


CREATE TABLE customer(        --The user. Both admin and non-admin.
    customer_id INTEGER PRIMARY KEY,
    customer_program_id INTEGER NOT NULL,

    customer_email VARCHAR(100) NOT NULL,
    customer_pw VARCHAR(255) NOT NULL,
    customer_first_name VARCHAR(50) NOT NULL,
    customer_last_name VARCHAR(50) NOT NULL,

    customer_weight INTEGER NOT NULL,
    customer_height INTEGER NOT NULL,
    customer_date_of_birth DATE NOT NULL,
    customer_sex VARCHAR NOT NULL CHECK(customer_sex IN ('m', 'f')),

    FOREIGN KEY (customer_program_id) REFERENCES program(program_id)
);

CREATE INDEX c_program_idx ON customer(customer_program_id);
CREATE UNIQUE INDEX c_email_idx ON customer(customer_email);


CREATE TABLE workout(         --The workout to be done on a specific day.
    workout_id INTEGER PRIMARY KEY,
    workout_program_id INTEGER NOT NULL, 

    workout_name VARCHAR(50) NOT NULL,
    workout_description TEXT NOT NULL,
    workout_date DATETIME NOT NULL,

    workout_comment TEXT DEFAULT '' NOT NULL,
    workout_done BOOLEAN DEFAULT 0 NOT NULL,

    FOREIGN KEY (workout_program_id) REFERENCES program(program_id)
);

CREATE INDEX w_program_idx ON workout(workout_program_id);
CREATE INDEX w_done_idx ON workout(workout_done);


CREATE TABLE exercise(        --Exercises makes up a workout.
    exercise_id INTEGER PRIMARY KEY,
    exercise_name VARCHAR(50) NOT NULL,
    exercise_description TEXT NOT NULL
);


CREATE TABLE exercise_set(    --Individual sets in an exercise.
    set_id INTEGER PRIMARY KEY,

    set_exercise_id INTEGER NOT NULL,
    set_workout_id INTEGER NOT NULL,

    set_reps_planned INTEGER NOT NULL,
    set_reps_done INTEGER NULL,

    set_weight_planned INTEGER NOT NULL,
    set_weight_done INTEGER NULL,

    FOREIGN KEY (set_workout_id) REFERENCES workout(workout_id),
    FOREIGN KEY (set_exercise_id) REFERENCES exercise(exercise_id)
);

CREATE INDEX s_exercise_idx ON exercise_set(set_exercise_id);
CREATE INDEX s_workout_idx ON exercise_set(set_workout_id);