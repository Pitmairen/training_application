<?php



$db->execute('CREATE TABLE trainer (
    trainer_id INTEGER PRIMARY KEY,
    trainer_name VARCHAR NOT NULL
)');

$db->execute('CREATE TABLE training_program (
    program_id INTEGER PRIMARY KEY,
    trainer_id INTEGER NOT NULL,
    program_name VARCHAR NOT NULL,

    FOREIGN KEY (trainer_id) REFERENCES trainer(trainer_id)
)');

$db->execute('CREATE INDEX trainer_id_idx ON training_program(trainer_id)'); 


$trainer1 = (object)['trainer_name' => 'Trainer1'];
$ds->storeNewTrainer($trainer1);
$trainer2 = (object)['trainer_name' => 'Trainer2'];
$ds->storeNewTrainer($trainer2);


$prog = (object)['program_name' => 'Program1', 'trainer_id' => $trainer1->trainer_id];
$ds->storeNewProgram($prog);
$prog = (object)['program_name' => 'Program2', 'trainer_id' => $trainer1->trainer_id];
$ds->storeNewProgram($prog);
$prog = (object)['program_name' => 'Program3', 'trainer_id' => $trainer2->trainer_id];
$ds->storeNewProgram($prog);
$prog = (object)['program_name' => 'Program4', 'trainer_id' => $trainer2->trainer_id];
$ds->storeNewProgram($prog);
$prog = (object)['program_name' => 'Program5', 'trainer_id' => $trainer2->trainer_id];
$ds->storeNewProgram($prog);







