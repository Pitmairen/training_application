<?php


$r = new Router($app, $ds);

$r->get('/', 'IndexController');
$r->get('/workout', 'WorkoutScheduleController');
$r->get('/workout/:id', 'WorkoutController');
$r->post('/workout/:id', 'WorkoutController');
$r->get('/stats', 'WorkoutLogController');
$r->get('/login', 'LoginController');
$r->post('/login', 'LoginController');
$r->get('/logout', 'LogoutController');
$r->post('/logout', 'LogoutController');

$r->get('/help', 'LoadTemplateController', 'help.php');
$r->get('/tos', 'LoadTemplateController', 'tos.php');
$r->get('/about', 'LoadTemplateController', 'about.php');


$r->group('/admin', function($r){
    
    $r->get('/', 'admin\IndexController');
    $r->get('/new-customer', 'admin\NewCustomerController');
    $r->get('/new-workout/:user_id', 'admin\NewWorkoutController');

});



unset($r);