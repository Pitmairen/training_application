
PRAGMA foreign_keys = ON;


INSERT INTO workout(workout_program_id, workout_name, workout_description, workout_date) VALUES
    (1, 'Upper body','This session will target your upper body.', date("now", "+1 day")),
    (1, 'Lower body','This session will target your lower body.', date("now", "+3 day")),
    (1, 'Core strength','This session will target your core.', date("now", "+5 day"))
;


INSERT INTO exercise_set(set_workout_id, set_exercise_id, set_reps_planned,
    set_reps_done, set_weight_planned, set_weight_done) VALUES

    -- Workout 1
    (101, 11, 10, null, 50, null),
    (101, 11, 10, null, 60, null),
    (101, 11, 10, null, 70, null),
    (101, 11, 10, null, 80, null),

    (101, 17, 10, null, 15, null),
    (101, 17, 10, null, 20, null),
    (101, 17, 10, null, 30, null),
    (101, 17, 10, null, 30, null),

    (101, 22, 10, null, 20, null),
    (101, 22, 10, null, 30, null),
    (101, 22, 10, null, 35, null),
    (101, 22, 10, null, 35, null),

    (101, 6, 10, null, 0, null),
    (101, 6, 10, null, 10, null),
    (101, 6, 10, null, 15, null),
    (101, 6, 10, null, 15, null),

    
    -- Workout 2
    (102, 5, 8, null, 60, null),
    (102, 5, 8, null, 80, null),
    (102, 5, 8, null, 100, null),
    (102, 5, 8, null, 110, null),

    (102, 16, 8, null, 60, null),
    (102, 16, 8, null, 80, null),
    (102, 16, 8, null, 100, null),
    (102, 16, 8, null, 110, null),

    (102, 27, 15, null, 20, null),
    (102, 27, 15, null, 30, null),
    (102, 27, 15, null, 40, null),
    (102, 27, 15, null, 40, null),

    (102, 28, 15, null, 20, null),
    (102, 28, 15, null, 25, null),
    (102, 28, 15, null, 35, null),
    (102, 28, 15, null, 30, null),

    -- Workout 3
    (103, 8, 1, null, 0, null),
    (103, 8, 1, null, 0, null),
    (103, 8, 1, null, 0, null),
    (103, 8, 1, null, 0, null),

    (103, 3, 20, null, 0, null),
    (103, 3, 20, null, 0, null),
    (103, 3, 20, null, 0, null),
    (103, 3, 20, null, 0, null),

    (103, 9, 1, null, 0, null),
    (103, 9, 1, null, 0, null),
    (103, 9, 1, null, 0, null),
    (103, 9, 1, null, 0, null),

    (103, 3, 20, null, 0, null),
    (103, 3, 20, null, 0, null),
    (103, 3, 20, null, 0, null),
    (103, 3, 20, null, 0, null),

    (103, 8, 1, null, 0, null),
    (103, 8, 1, null, 0, null),
    (103, 8, 1, null, 0, null),
    (103, 8, 1, null, 0, null)
;
