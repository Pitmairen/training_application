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
        
        $this->db->execute('INSERT INTO program (program_name, program_desc)
            VALUES(:name, :desc)',
        ['name' => $prog->program_name, 'desc' => $prog->program_desc]);
        $prog->program_id = $this->db->lastInsertId();
        return $prog;

    }

    public function storeNewUser($user){

        $this->db->execute('INSERT INTO user 
            (user_pw, user_email, user_first_name, user_last_name, user_weight,
            user_height, user_date_of_birth, user_sex, user_program_id)
            VALUES(:pw, :email, :first_name, :last_name, :current_weight,
            :height, :date_of_birth, :sex, :prog)',
            [
                'pw' => $user->user_pw,
                'email' => $usr->user_email,
                'first_name' => $user->user_first_name,
                'last_name' => $user->user_last_name,
                'current_weight' => $user->user_current_weight,
                'height' => $user->user_height,
                'date_of_birth' => $user->user_date_of_birth,
                'sex' => $user->user_sex,
                'prog' => $user->user_program_id,
            ]);

        $user->user_id = $this->db->lastInsertId();
        return $user;

    }

    public function getUserByEmail($email){
        return $this->db->fetchOneObject('SELECT * FROM user WHERE user_email=:email', ['email' => $email]);
    }

    public function getUserById($id){
        return $this->db->fetchOneObject('SELECT * FROM user WHERE user_id=:id', ['id' => $id]);
    }



    public function getWorkoutById($id){
        return $this->db->fetchOneObject(
            'SELECT w.* FROM workout AS w WHERE workout_id=:id',
            ['id' => $id]);
    }

    public function getWorkoutLogForUser($user_id, $limit=10){
        return $this->db->fetchAllObject(
            'SELECT w.* FROM workout AS w 
           INNER JOIN user AS u ON u.user_program_id=w.workout_program_id
            WHERE u.user_id=:id AND w.workout_done=:done', 
            
            ['id' => $user_id, 'done' => true]);
    }


    public function getNextWorkoutsForUser($user_id, $limit=5){
        return $this->db->fetchAllObject(
            'SELECT w.* FROM workout AS w 
           INNER JOIN user AS u ON u.user_program_id=w.workout_program_id
            WHERE u.user_id=:id AND w.workout_done=:done', 
            
            ['id' => $user_id, 'done' => false]);
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

