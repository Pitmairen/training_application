<?php

require APP_ROOT . 'config.php';
require APP_ROOT . 'autoload.php';


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
$app = new \Slim\Slim();
$app->config('debug', IS_DEBUGGING);

$app->add(new \SlimJson\Middleware(array(
  'json.status' => true,
  'json.override_error' => false,
  'json.override_notfound' => false,
  'json.protect' => true,
)));

