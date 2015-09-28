use training_application;

insert into trainer(email, pw, first_name, last_name)
values ('ultimate.trainer@gmail.com', '1234', 'Jack', 'Strong'),
('matt.forrest@gmail.com','PushUpsIsCool', 'Matt', 'Forrest'),
('natalie.northwood@gmail.com','VeryLongPassword','Natalie','Northwood');

insert into trainingprogram(trainer_id)
values (1),(2),(3);

insert into customer(email, pw, first_name, last_name, current_weight, height, date_Of_birth, sex, trainingprogram_id)
values ('fancypants@gmail.com', 'DontAsk', 'Tiffany', 'McDonald', 55, '157', '1989-02-23', 'f', 1),
('duke.davidson@gmail.com', 'DogsAreCool', 'Duke', 'Davidson', 68, '179', '1976-12-05', 'm', 1),
('s.harrison@gmail.com', 'D45fsq23s54', 'Sam', 'Harrison', 77, '164', '1954-07-19', 'm', 1);

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

insert into exercise_set(set_nr, exercise_id, workout_id, repetitions_planed, repetitions_cleared, additional_load, additional_load_cleared, comment_by_user)
values (1, 1, 1, 60, null, 0, null, null),
(1, 4, 1, 15, null, 0, null, null),
(1, 2, 2, 20, null, 0, null, null),
(1, 5, 2, 5, null, 0, null, null),
(1, 3, 3, 20, null, 0, null, null),
(1, 6, 3, 60, null, 0, null, null);

Use master;