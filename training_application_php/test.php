<?php

require 'db.php';
require 'datasource.php';
require 'datasource_base.php';
require 'datasource_sqlite.php';



define("ROOT_FOLDER", "./");


$pdo = new PDO('sqlite::memory:');
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db = new DB($pdo);
$ds = new DataSourceSqlite($db);


$dbcontent = file_get_contents(ROOT_FOLDER . 'database_sqlite.sql');


$parts = explode(';', $dbcontent);
foreach($parts as $sql){
    $db->execute($sql);
}


$trainer = $ds->getTrainerById(1);


$programs = $ds->getProgramsByTrainerId($trainer->trainer_id);
echo 'Programs by: ', $trainer->first_name, "\n";
foreach($programs as $prog){
    echo '    program for ', $prog->customer_first_name, "\n";
}


echo "\n";

$programs = $ds->getAllPrograms();
echo "All Programs: \n";
foreach($programs as $prog){
    echo '    program for ', $prog->customer_first_name, "\n";
}

echo "\n";




