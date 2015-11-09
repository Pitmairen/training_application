
PRAGMA foreign_keys = ON;

INSERT INTO program(program_name, program_description) VALUES
    ('Full body routine', 'This is a very intensive full body strenght routine aimed at experienced individuals.'),
    ('Starting Strength A', 'Full body strength routine for novices and beginners. Very core- and lower body intensive.'),
    ('Starting Strength B', 'Full body strength routine for novices and beginners. Very core- and lower body intensive.'),
    ('Stronglifts', 'Full body strength routine for novices and beginners. More balanced.'),
    ('Push Pull 4 day', 'Full body strength routine for advanced and above, days and exercises are split according to the motions in push and pull categories.'),
    ('Madcow/Bill Starr', 'Very intensive full body strength routine. Recommended for advanced lifters and above.'),
    ('Tabata regimen', 'Anything above 12 reps is cardio.')
;


INSERT INTO customer(customer_program_id, customer_email, customer_pw, customer_first_name, customer_last_name, customer_weight, customer_height, customer_date_Of_birth, customer_sex) VALUES
    (1, 'test', '$2a$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO', 'Test Name', 'Test Name', 80, 180, '1911-11-11', 'm'),
    (1, 'w.wilson@email.com', '$2a$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO', 'Wade', 'Wilson', 95, 188, '1997-12-05', 'm'),
    (1, 's.rogers@email.com', '$2a$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO', 'Steven', 'Rogers', 100, 188, '1941-23-11', 'm'),    
    (1, 'j.walters@email.com', '$2a$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO', 'Jennifer', 'Walters', 64, 178, '1980-02-23', 'f'),
    (1, 'f.castle@email.com', '$2a$10$w42h6OtYgtUJHyExOzTtiea3xK1LYd2JLlrDwEgJJ.WffF9GJcSjO', 'Frank', 'Castle', 91, 185, '1975-07-06', 'm')
;


INSERT INTO workout(workout_program_id, workout_name, workout_description, workout_date) VALUES
    (1, 'Overall strength','This session will target the entire body.', date("now", "+1 day")),
    (1, 'Overall strength','This session will target the entire body.', date("now", "+3 day")),
    (1, 'Overall strength','This session will target the entire body.', date("now", "+5 day")),
    (1, 'Overall strength','This session will target the entire body.', date("now", "+7 day")),
    (1, 'Overall strength','This session will target the entire body.', date("now", "+9 day")),
    (1, 'Overall strength','This session will target the entire body.', date("now", "+11 day")),
    (1, 'Overall strength','This session will target the entire body.', date("now", "+13 day"))
;


INSERT INTO exercise(exercise_name, exercise_description) VALUES
    ('The 90-degree', 'Find a wall, support your back against it and make sure you have 90 degrees between the following: your legs and the floor, your legs and thighs. Hold for x number of seconds'),
    ('Push-ups', 'Lay down and use your arms to push your body up, keep body stright. You are now in starting position. Now use your arms to lower your chest down towards the floor and then up again, repeat x times.'),
    ('Sit-ups', 'Lay on your back with your knees bent and your feet firmly planted on the floor. Hold your arms crossed on your chest and use your abs to raise your upper body from the floor, towards your knees and down again. Repeat x times'),
    ('Bodyweight squat', 'Take a stance with your legs, toes pointing outwards in a 45 degree angle. Now crouch towards the floor as if you were trying to sit on it. During the motion make sure to keep your lower and upper spine straight, neither rounding or hyper-extending it. Repeat x times.'),
    ('Barbell squat','Using a squat rack position yourself under the bar with the bar resting on your trapezius muscles. Hold firmly onto the bar and unrack the bar then step away from the rack. Take a stance with your legs, toes pointing outwards in a 45 degree angle. Now crouch towards the floor as if you were trying to sit on it. During the motion make sure to keep your lower and upper spine straight, neither rounding or hyper-extending it. Repeat x times.'),
	('Pull-ups', 'Find a horizontal bar, or ledge you can hang from. Hang and pull your weight up so your head is above the bar, then lower yourself down. repeat x times'),
    ('30 second plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for 30 seconds'),
	('1 minute plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for 1 minute'),
	('2 minute plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for 2 minutes'),
	('3 minute plank','Lay down, then support your weight with your forearms so your body is straight and only your feet and forearms touch the ground, hold for 3 minutes'),
	('Barbell benchpress','Lay on your back on the benchpress. Unrack the barbell from its stand and lower it slowly until it reaches your chest, then press the bar upwards until your elbows lock out.'),
	('Incline barbell benchpress','Lay on your back on the incline bench. Unrack the barbell from its stand and lower it slowly until it reaches your chest, then press the bar upwards until your elbows lock out.'),
	('Dumbell benchpress','Grab a pair of dumbells and lay on your back on a horizontal bench. Place the dumbells to the side of your chest and press them upwards until your elbows lock out. Now lower the dumbells until they reach shoulder height and repeat.'),
	('Incline dumbell press','Grab a pair of dumbells and lay on your back on an incline bench. Place the dumbells to the side of your chest and press them upwards until your elbows lock out. Now lower the dumbells until they reach shoulder height and repeat.'),
	('Hack squat','Place a barbell with weights on the floor and position yourself in front of it. Now bend over and grab the barbell behind you and push from the floor with your leg and thigh muscles as you keep your spine straight'),
	('Deadlift','Position the barbell directly in front of your feet. Now bend over, pull the bar close to your legs and with a straight back, lift the bar up from the floor until you are in the upright position. Now lower the bar again.'),
	('Overhead barbell press','Position the bar on your shoulders. Now press upwards until your elbows lock out. Maintain a straight spine and the bar paralell to the floor throughout the motion.'),
	('Power clean','Position the barbell on the floor, directly above your toes. Now bend over, grab the bar and in a swift controlled movement pull the bar up from the ground, jerk it above your hips and bring it to shoulder height. Personal instruction is advised.'),
	('Lower back hyperextensions','Position yourself facing down on the hyperextension bench, tucking your ankles securely under the footpads and your hips pressed against the pad for your hips. Hold your arms closely to your chest and slowly lower your upper body as far as you can. Then raise your upper body again until your hips lock out'),
	('Barbell row','Place a barbell on the floor in front of your legs. Now pick up the bar and slightly raise your torso. In this position pull the bar upwards towards your stomach using your back muscles and lower the bar again'),
	('Pendlay row','Place a barbell on the floor in front of your legs. Bend over, pull the bar close to your legs, pick up the bar and pull it towards your chest in this position using your arms and back muscles.'),
	('Bent over dumbell row','Using a horizontal bench place a dumbell on the floor to the side of the bench. Put one knee on the bench with the dumbell to the opposing side, grab onto to the bench for support with the hand one the same side as the knee. Now you bend over, grab the dumbell and pull it towards your chest. Lower the dumbell and repeat.'),
	('Alternating dumbell curl','Pick two dumbells of equal weight off the floor. Hold them on opposite sides and with a supinated grip pull one dumbell up towards your chest. Repeat but alternate between left and right.'),
	('Hammer curl','Pick two dumbells of equal weight, or a hammer curl bar off the floor. Hold the weights with a supinated grip and the weights up towards your chest. Lower and repeat.'),
	('Upright row ','For this exercise you can use a barbell, dumbells and the EZ-bar. Pick up the weights of your choice off the floor and stand in an upright position. Now with the weights at, or close to hip height pull them up to shoulder height, hold and lower again'),
	('Shrug','For this exercise you can use dumbells, barbell, kettlebell etc. Pick up the weights off the floor and stand in the upright position. Now raise your shoulders in a shrugging motion, hold and then lower your shoulders again.'),
	('Leg curl','For this exercise you must use the seated leg curl machine. Place your legs in such a way that they are locked in place by the footpads. Fully extend them until your knees lockout, lower and repeat.'),
	('Calf raises','Weighted. Stand at a slight  Balance your weight on your toes and push with your calves until you are standing on your tiptoes. Lower yourself until your feet are horizontal to the ground, then repeat.'),
	('Hanging leg raises','Grab onto and hang from a bar with your legs hanging and fully extended. Pull them up as high as you can while keeping them extended, then lower them.'),
	('Lat pulldowns','For this exercise you will need to use a lat pulldown machine. Seat yourself facing towards machine. Secure your legs under the knee pads and grab onto the bar. Now pull the bar towards you until it reaches shoulder height. Let the bar go up again and repeat.'),
	('Kroc rows','Bend one leg at 90 degrees and extend the other leg diagonally to your side with both feet firmly planted on the floor. Grab a dumbell and pull it to your chest. then repeat.'),
	('Hamstring curl','This exercise will require using a hamstring curl machine. Lay on your stomach on the machine and secure your feet under the protective pads on the lever. Pull the lever towards your using your feet. Release and repeat.'),
	('Good morning','This exercise can be done weighted, or unweighted. Weighted is only recommended for intermediate and above lifters. Place an empty barbell on your back, resting on your trapezius muscles, behind your neck. Bend slightly at the knees and with a straight back bend over at the hip until your body is parallel to the floor. Extend and repeat.')
	;
	


INSERT INTO exercise_set(set_workout_id, set_exercise_id, set_reps_planned,
    set_reps_done, set_weight_planned, set_weight_done) VALUES

    -- Workout 1
    -- Deadlift
    (1, 14, 1, null, 110, null),
    (1, 14, 1, null, 110, null),
    (1, 14, 1, null, 110, null),
    (1, 14, 1, null, 110, null),
    -- Barbell benchpress
    (1, 9, 1, null, 32, null),
    (1, 9, 1, null, 32, null),
    (1, 9, 1, null, 32, null),
    (1, 9, 1, null, 32, null),
    -- Pull ups
    (1, 4, 1, null, 5, null),
    (1, 4, 1, null, 5, null),
    (1, 4, 1, null, 5, null),
    (1, 4, 1, null, 5, null),
    -- Squats
    (1, 3, 1, null, 90, null),
    (1, 3, 1, null, 90, null),
    (1, 3, 1, null, 90, null),
    (1, 3, 1, null, 90, null),

    -- Workout 2
    -- Deadlift
    (2, 14, 1, null, 110, null),
    (2, 14, 1, null, 110, null),
    (2, 14, 1, null, 110, null),
    (2, 14, 1, null, 110, null),
    -- Barbell benchpress
    (2, 9, 1, null, 32, null),
    (2, 9, 1, null, 32, null),
    (2, 9, 1, null, 32, null),
    (2, 9, 1, null, 32, null),
    -- Pull ups
    (2, 4, 1, null, 5, null),
    (2, 4, 1, null, 5, null),
    (2, 4, 1, null, 5, null),
    (2, 4, 1, null, 5, null),
    -- Squats
    (2, 3, 1, null, 90, null),
    (2, 3, 1, null, 90, null),
    (2, 3, 1, null, 90, null),
    (2, 3, 1, null, 90, null),

    -- Workout 3
    -- Deadlift
    (3, 14, 1, null, 110, null),
    (3, 14, 1, null, 110, null),
    (3, 14, 1, null, 110, null),
    (3, 14, 1, null, 110, null),
    -- Barbell benchpress
    (3, 9, 1, null, 32, null),
    (3, 9, 1, null, 32, null),
    (3, 9, 1, null, 32, null),
    (3, 9, 1, null, 32, null),
    -- Pull ups
    (3, 4, 1, null, 5, null),
    (3, 4, 1, null, 5, null),
    (3, 4, 1, null, 5, null),
    (3, 4, 1, null, 5, null),
    -- Squats
    (3, 3, 1, null, 90, null),
    (3, 3, 1, null, 90, null),
    (3, 3, 1, null, 90, null),
    (3, 3, 1, null, 90, null)
;
