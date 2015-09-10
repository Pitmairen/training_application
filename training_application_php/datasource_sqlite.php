<?php


class DataSourceSqlite extends DataSourceBase implements DataSource
{



    public function storeNewTrainer($trainer){
        
        $this->db->execute('INSERT INTO trainer (trainer_name) VALUES(:name)',
        ['name' => $trainer->trainer_name]);
        $trainer->trainer_id = $this->db->lastInsertId();
        return $trainer;

    }

    public function updateTrainer($trainer){

        return $this->db->execute('UPDATE trainer SET trainer_name=:name WHERE trainer_id=:id',
            ['name' => $trainer->name, 'id' => $trainer->id]);
    }


    public function getTrainerById($id){
        return $this->db->fetchOneObject('SELECT * FROM trainer WHERE trainer_id=:id', ['id' => $id]);
    }

    public function getAllTrainers(){
        return $this->db->fetchAllObject('SELECT * FROM trainer ORDER BY trainer_id');
    }




    public function storeNewProgram($prog){
        
        $this->db->execute('INSERT INTO training_program (trainer_id, program_name)
            VALUES(:trainer_id, :name)',
        ['name' => $prog->program_name, 'trainer_id' => $prog->trainer_id]);
        $prog->program_id = $this->db->lastInsertId();
        return $prog;

    }


    public function getAllPrograms(){
        return $this->db->fetchAllObject('SELECT * FROM training_program 
            INNER JOIN trainer ON trainer.trainer_id=training_program.trainer_id
            ORDER BY program_id ASC');
    }

    public function getProgramsByTrainerId($id){
        return $this->db->fetchAllObject('SELECT * FROM training_program 
            INNER JOIN trainer ON trainer.trainer_id=training_program.trainer_id
            WHERE trainer.trainer_id=:id
            ORDER BY program_id ASC', ['id' => $id]);
    }
}

