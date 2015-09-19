<?php


interface DataSource {
 
    /**
     * Save a new trainer to the database
     */
    public function storeNewTrainer($trainer);

    /**
     * Updates the database with the data in the trainer
     * object provided in the input.
     */
    public function updateTrainer($trainer);

    /**
     * Returns the data for a trainer with the provided id.
     */
    public function getTrainerById($id);

    /**
     * Returns a list of all the trainers in the database.
     */
    public function getAllTrainers();
    
    
    /**
     * Save a new training program to the database.
     */
    public function storeNewProgram($program);

    /**
     * Returns a list of all the training programs.
     */
    public function getAllPrograms();

    /**
     * Returns a list of training programs created by 
     * the trainer with the specified id.
     */
    public function getProgramsByTrainerId($id);



    /**
     * Returns the customer with the specified email
     */
    public function getCustomerByEmail($email);

    /**
     * Returns the customer with the specified id
     */
    public function getCustomerById($id);




    /**
     * Begins a new transaction
     */
    public function beginTransaction();

    /**
     * Commits the current transaction
     */
    public function commit();

    /**
     * Rollback the current transaction
     */
    public function rollback();

    /**
     * Wraps the callback in a transaction
     * The callback will receive a single argument
     * which is the current datasource object.
     */
    public function runTransaction($callback);

}
