<?php

$app->get('/', function () use($app) {


    $app->render("index.php");
});


$app->get('/hello/:name', function ($name) use($app) {

    $app->render("hello.php", ['name' => $name]);
});

$app->get('/workout', function () use($app, $ds) {

    $user = Auth::getCurrentUser();

    $workouts = $ds->getNextWorkoutsForUser($user->getID());

    $app->render("workout-schedule.php", ['next_workouts' => $workouts]);
});

$app->get('/workout/:id', function ($id) use($app, $ds) {

    $workout = $ds->getWorkoutById($id);

    if(!$workout){
        $app->notFound();
    }

    $sets = $ds->getExerciseSetsForWorkoutById($id);

    if($workout->workout_done) {

        $app->render("workout-done.php", ['sets' => $sets, 'workout' => $workout]);

    }else{

        $form = load_form('workout', ['sets' => $sets]);

        $app->render("workout.php", ['workout_form' => $form, 'workout' => $workout]);

    }

});

$app->post('/workout/:id', function ($id) use($app, $ds) {

    $sets = $ds->getExerciseSetsForWorkoutById($id);

    $form = load_form('workout', ['sets' => $sets]);

    if($form->postedAndValid()){

        $set_vals = [];
        foreach($sets as $set){
            $set_vals[$set->set_id] = [
                'weight' => $form->getValue('weight-'.$set->set_id),
                'reps' => $form->getValue('reps-'.$set->set_id),
            ];
        }
        $desc = $form->getValue('workout_desc');

        $ds->runTransaction(function($ds) use($id, $desc, $set_vals){

            $ds->updateWorkoutCompleted($id, $desc);
            $ds->updateSetValues($set_vals);

            $ds->commit();
        });


        $app->redirect('/workout');
    }

    $app->render("workout.php", ['workout_form' => $form]);
});

$app->get('/stats', function () use($app, $ds) {


    $user = Auth::getCurrentUser();

    $workouts = $ds->getWorkoutLogForUser($user->getID());

    $app->render("stats.php", ['workout_log' => $workouts]);
});

$app->get('/help', function () use($app) {

    $app->render("help.php");
});

$app->get('/tos', function () use($app) {

    $app->render("tos.php");
});

$app->get('/about', function () use($app, $ds) {

    $app->render("about.php");
});


$app->get('/login', function () use($app) {

    if (Auth::getCurrentUser()->isAuthenticated()) {

        return $app->redirect('/');
    }

    $form = load_form('login');

    $app->render("login.php", ['login_form' => $form]);
});

$app->post('/login', function () use($app, $ds) {


    $form = load_form('login');

    $form->addConstraint(function($form) use($ds) {

        $pass = $form->getValue('password');
        $user = $ds->getUserByEmail($form->getValue('username'));
        if ($user && verify_password($pass, $user->user_pw)) {
            return;
        }
        return 'Wrong username or password';
    });


    if ($form->postedAndValid()) {

        $username = $form->getValue('username');

        $user = $ds->getUserByEmail($form->getValue('username'));

        $_SESSION['user_name'] = $username;
        $_SESSION['user_id'] = $user->user_id;
        $_SESSION['user_rank'] = Auth::RANK_USER;

        $app->redirect('/');
    } else {
        $app->render("login.php", ['login_form' => $form]);
    }
});

$app->get('/logout', function () use($app) {

    $app->render("logout.php");
});

$app->post('/logout', function () use($app) {

    unset($_SESSION['user_name']);
    unset($_SESSION['user_rank']);
    unset($_SESSION['user_id']);

    $app->redirect('/');
});
