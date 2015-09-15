<?php

require APP_ROOT . 'config.php';
require APP_ROOT . 'autoload.php';
require APP_ROOT . 'utils.php';
require APP_ROOT . 'auth.php';


// Setup global objects
$pdo = new PDO(PDO_DSN, PDO_USER, PDO_PASS);
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db = new DB($pdo);
$ds = new DataSourceSqlite($db);


// Load test data
$dbcontent = file_get_contents(APP_ROOT . 'database_sqlite.sql');
$parts = explode(';', $dbcontent);
foreach($parts as $sql){
    $db->execute($sql);
}



// Create the app instance and setup view and middleware
$app = new \Slim\Slim([
    'templates.path' => APP_ROOT . 'templates/',

    'cookies.encrypt' => true,
    'cookies.secret_key' => COOKIE_SECRET,
    'cookies.cipher' => MCRYPT_RIJNDAEL_256,
    'cookies.cipher_mode' => MCRYPT_MODE_CBC
]);

$app->config('debug', IS_DEBUGGING);

// $app->add(new \SlimJson\Middleware(array(
//   'json.status' => true,
//   'json.override_error' => false,
//   'json.override_notfound' => false,
//   'json.protect' => true,
// )));
//

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

