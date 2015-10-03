<?php

require APP_ROOT . 'inc/autoload.php';
require APP_ROOT . 'inc/utils.php';

require APP_ROOT . 'config.php';

define('TPL_INC', APP_ROOT . 'templates/inc/');

// Create the app instance and setup view and middleware
$app = new \Slim\Slim([
    'templates.path' => APP_ROOT . 'templates/',

    'cookies.encrypt' => true,
    'cookies.secret_key' => COOKIE_SECRET,
    'cookies.cipher' => MCRYPT_RIJNDAEL_256,
    'cookies.cipher_mode' => MCRYPT_MODE_CBC
]);

$app->config('debug', IS_DEBUGGING);

$app->add(new AuthMiddleware());

$app->add(new \Slim\Middleware\SessionCookie(array(
    'expires' => '10 days',
    'path' => '/',
    'domain' => null,
    'secure' => false,
    'httponly' => true,
    'name' => 'ses',
)));


require APP_ROOT . 'routing.php';

