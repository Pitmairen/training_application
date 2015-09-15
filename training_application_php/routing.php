<?php

$app->get('/', function () use($app) {

    $app->render("index.php");
});


$app->get('/hello/:name', function ($name) use($app) {

    $app->render("hello.php", ['name' => $name]);
});


$app->get('/about', function () use($app) {

    $app->render("about.php");
});


