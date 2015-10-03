<?php

define('BASE_URL', '/api.php');

// Composer autoloading
require APP_ROOT . 'vendor/autoload.php';


/*
 * Autoload our own classes from the inc directory
 */
spl_autoload_register(function ($class) {

    if(strpos('Controller', $class) !== -1
       && file_exists(APP_ROOT . '/controllers/'.$class.'.php'))
    {
        require APP_ROOT . '/controllers/'.$class.'.php';
    }else{
       require APP_ROOT . '/classes/' . $class . '.class.php';
    }
});



