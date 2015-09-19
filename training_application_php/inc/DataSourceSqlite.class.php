<?php

class DataSourceSqlite implements DataSource
{

    private $db;

    public function __construct($dsn){

        $pdo = new PDO($dsn);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $this->db = new PDOWrapper($pdo);

    }


    public function storeNewTrainer($trainer){
        
        $this->db->execute('INSERT INTO trainer 
            (pw, email, first_name, last_name) VALUES(:name, :email, :first_name, :last_name)',
            [
                'pw' => $trainer->pw,
                'email' => $trainer->email,
                'first_name' => $trainer->first_name,
                'last_name' => $trainer->last_name,
            ]);

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





    public function getCustomerByEmail($email){
        return $this->db->fetchOneObject('SELECT * FROM customer WHERE email=:email', ['email' => $email]);
    }

    public function getCustomerById($id){
        return $this->db->fetchOneObject('SELECT * FROM customer WHERE customer_id=:id', ['id' => $id]);
    }




    public function storeNewProgram($prog){
        
        $this->db->execute('INSERT INTO training_program (trainer_id)
            VALUES(:trainer_id)',
        ['trainer_id' => $prog->trainer_id]);
        $prog->trainingprogram_id = $this->db->lastInsertId();
        return $prog;

    }


    public function getAllPrograms(){
        return $this->db->fetchAllObject('SELECT trainingprogram.*,
            trainer.first_name AS trainer_first_name,
            customer.first_name AS customer_first_name
            FROM trainingprogram 
            INNER JOIN customer ON customer.trainingprogram_id=trainingprogram.trainingprogram_id
            INNER JOIN trainer ON trainer.trainer_id=trainingprogram.trainer_id
            ORDER BY trainingprogram_id ASC');
    }

    public function getProgramsByTrainerId($id){
        return $this->db->fetchAllObject('SELECT trainingprogram.*,
            trainer.first_name AS trainer_first_name,
            customer.first_name AS customer_first_name
            FROM trainingprogram 
            INNER JOIN trainer ON trainer.trainer_id=trainingprogram.trainer_id
            INNER JOIN customer ON customer.trainingprogram_id=trainingprogram.trainingprogram_id
            WHERE trainer.trainer_id=:id
            ORDER BY trainingprogram_id ASC', ['id' => $id]);
    }



    public function beginTransaction(){
        $this->db->beginTransaction();
    }

    public function commit(){
        $this->db->commit();
    }

    public function rollback(){
        $this->db->commit();
    }

    public function runTransaction($callback){

        $this->beginTransaction();

        try{
            $callback($this);
        }
        catch(Exception $e){
            $this->rollback();
            throw $e;
        }


    }


    /**
     * Loads sql instructions from dbfile into the current 
     * database.
     */
    public function loadTestData($dbfile){
        $dbcontent = file_get_contents($dbfile);
        $parts = explode(';', $dbcontent);
        foreach($parts as $sql){
            $this->db->execute($sql);
        }

    }

}

