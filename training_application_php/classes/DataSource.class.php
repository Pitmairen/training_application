<?php


interface DataSource {
 

    
    /**
     * Save a new training program to the database.
     */
    public function storeNewProgram($program);



    /**
     * Save a new user to the database.
     */
    public function storeNewUser($user);

    /**
     * Returns the user with the specified email
     */
    public function getUserByEmail($email);

    /**
     * Returns the user with the specified id
     */
    public function getUserById($id);


    /**
     * Returns the workout with the specified id
     */
    public function getWorkoutById($id);

    /**
     * Returns the workout log for the specified user
     */
    public function getWorkoutLogForUser($user_id, $limit=10);

    /**
     * Returns the next workouts for the specified user
     */
    public function getNextWorkoutsForUser($user_id, $limit=10);

    /**
     * Update a workout and change it to completed
     */
    public function updateWorkoutCompleted($id, $desc);

    /**
     * Returns all the sets of all the exercises in a workout
     */
    public function getExerciseSetsForWorkoutById($id);

    /**
     * Update all the sets in with the provided reps and weight values
     */
    public function updateExerciseSetValues($set_vals);


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
