<?php

$app->get('/', function () use($app) {

    $app->render("index.php");
});


$app->get('/hello/:name', function ($name) use($app) {

    $app->render("hello.php", ['name' => $name]);
});


$app->get('/about', function () use($app, $ds) {

    $programs = $ds->getAllPrograms();



    $app->render("about.php", ['training_programs' => $programs]);
});


$app->get('/login', function () use($app) {


    $app->render("login.php");
});

$app->post('/login', function () use($app) {

    $_SESSION['user'] = $app->request->post('username');

    $app->redirect('/');

    // $app->render("login.php", ['error_msg' => 'Wrong usernam or password']);
});

$app->get('/logout', function () use($app) {

    $app->render("logout.php");
});

$app->post('/logout', function () use($app) {

    unset($_SESSION['user']);

    $app->redirect('/');
});
