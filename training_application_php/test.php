<?php

require 'db.php';
require 'datasource.php';
require 'datasource_base.php';
require 'datasource_sqlite.php';


$pdo = new PDO('sqlite::memory:');
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db = new DB($pdo);
$ds = new DataSourceSqlite($db);


require 'test_data.php';


$trainer = $ds->getTrainerById(1);

$programs = $ds->getProgramsByTrainerId($trainer->trainer_id);
echo 'Programs by: ', $trainer->trainer_name, "\n";
foreach($programs as $prog){
    echo '    ', $prog->program_name, "\n";
}


echo "\n";

$programs = $ds->getAllPrograms();
echo "All Programs: \n";
foreach($programs as $prog){
    echo '    ', $prog->program_name, ' by ', $prog->trainer_name, "\n";
}

echo "\n";




