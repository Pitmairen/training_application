<?php

define('BASE_URL', '/api.php');

// Composer autoloading
require APP_ROOT . 'vendor/autoload.php';


/*
 * Autoload our own classes from the inc directory
 */
spl_autoload_register(function ($class) {
    require APP_ROOT . '/inc/' . $class . '.class.php';
});



