<?php


interface DataSource {
 
    public function storeNewTrainer($trainer);
    public function updateTrainer($trainer);
    public function getTrainerById($id);
    public function getAllTrainers();
    
    
    public function storeNewProgram($program);
    public function getAllPrograms();
    public function getProgramsByTrainerId($id);


    public function lastInsertId();
    
}
