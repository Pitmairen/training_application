<?php

// For serving static files for development only
if (file_exists($_SERVER["DOCUMENT_ROOT"] . $_SERVER["REQUEST_URI"])) {
    return false;
} 

define('APP_ROOT', '../');

require APP_ROOT . 'init.php';

$app->run();
