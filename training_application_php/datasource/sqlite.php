<?php

class DataSourceSqlite implements DataSource
{

    private $db;

    public function __construct($dsn){

        $pdo = new PDO($dsn);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $this->db = new PDOWrapper($pdo);

    }


    public function storeNewProgram($prog){
        
        $this->db->execute('INSERT INTO program (program_name, program_description)
            VALUES(:name, :desc)',
        ['name' => $prog->program_name, 'desc' => $prog->program_description]);
        $prog->program_id = $this->db->lastInsertId();
        return $prog;

    }

    public function storeNewCustomer($cust){

        $this->db->execute('INSERT INTO customer 
            (customer_pw, customer_email, customer_first_name, customer_last_name, customer_weight,
            customer_height, customer_date_of_birth, customer_sex, customer_program_id)
            VALUES(:pw, :email, :first_name, :last_name, :current_weight,
            :height, :date_of_birth, :sex, :prog)',
            [
                'pw' => $cust->customer_pw,
                'email' => $cust->customer_email,
                'first_name' => $cust->customer_first_name,
                'last_name' => $cust->customer_last_name,
                'current_weight' => $cust->customer_current_weight,
                'height' => $cust->customer_height,
                'date_of_birth' => $cust->customer_date_of_birth,
                'sex' => $cust->customer_sex,
                'prog' => $cust->customer_program_id,
            ]);

        $cust->customer_id = $this->db->lastInsertId();
        return $cust;

    }

    public function getCustomerByEmail($email){
        return $this->db->fetchOneObject('SELECT * FROM customer WHERE customer_email=:email', ['email' => $email]);
    }

    public function getCustomerById($id){
        return $this->db->fetchOneObject('SELECT * FROM customer WHERE customer_id=:id', ['id' => $id]);
    }



    public function getWorkoutById($id){
        return $this->db->fetchOneObject(
            'SELECT w.* FROM workout AS w WHERE workout_id=:id',
            ['id' => $id]);
    }

    public function getWorkoutLogForCustomer($customer_id, $limit=10){
        return $this->db->fetchAllObject(
            'SELECT w.* FROM workout AS w 
           INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id
            WHERE u.customer_id=:id AND w.workout_done=:done', 
            
            ['id' => $customer_id, 'done' => true]);
    }


    public function getNextWorkoutsForCustomer($customer_id, $limit=5){
        return $this->db->fetchAllObject(
            'SELECT w.* FROM workout AS w 
           INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id
            WHERE u.customer_id=:id AND w.workout_done=:done', 
            
            ['id' => $customer_id, 'done' => false]);
    }

    public function updateWorkoutCompleted($id, $desc){

        $this->db->execute('UPDATE workout SET 
            workout_done=:done, workout_comment=:desc WHERE workout_id=:id',
        ['done' => true, 'desc' => $desc, 'id' => $id]);

            
    }

    public function getExerciseSetsForWorkoutById($id){
        return $this->db->fetchAllObject(
            'SELECT s.*, e.*
            FROM exercise_set AS s 
            INNER JOIN exercise AS e ON e.exercise_id = s.set_exercise_id
            WHERE s.set_workout_id=:id
            ORDER BY s.set_exercise_id ASC, s.set_nr ASC
            ', ['id' => $id]);
    }



    public function updateExerciseSetValues($set_vals){

        foreach($set_vals as $sid => $set) { 
            $this->db->execute('UPDATE exercise_set SET 
                set_reps_done=:reps, set_weight_done=:weight WHERE set_id=:id',
            ['reps' => $set['reps'], 'weight' => $set['weight'], 'id' => $sid]);

        }
            
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

