package no.hials.trainingapp.datasource;

import java.sql.SQLException;
import java.util.List;

/**
 * This is the interface the application uses to access the database
 *
 * @author Per Myren <progrper@gmail.com>
 */
public interface DataSource {

    /**
     * Returns the customer with the given username
     *
     * @param username the username of the user
     * @return the user data
     * @throws SQLException
     */
    public DataItem getCustomerByUsername(String username) throws SQLException;

    /**
     * Returns the customer with the given ID
     *
     * @param id the id of the user
     * @return the user data
     * @throws SQLException
     */
    public DataItem getCustomerById(int id) throws SQLException;

    /**
     * Returns all the customers
     *
     * @param limit limit the number of users returned
     * @return a list of customers
     * @throws SQLException
     */
    public List<DataItem> getAllCustomers(int limit) throws SQLException;

    /**
     * Store a new customer in the data source
     *
     * @param data the data that represents the user
     * @throws SQLException
     */
    public void storeNewCustomer(DataItem data) throws SQLException;

    /**
     * Returns a list of the user's upcoming workouts
     *
     * @param customerId the customer id
     * @param limit limit the number of workouts to return
     * @return a list of workouts
     * @throws SQLException
     */
    public List<DataItem> getNextWorkoutsForCustomer(int customerId, int limit) throws SQLException;

    /**
     * Returns a list of the user's completed workouts
     *
     * @param customerId the customer id
     * @param limit limit the number of workouts to return
     * @return a list of workouts
     * @throws SQLException
     */
    public List<DataItem> getWorkoutLogForCustomer(int customerId, int limit) throws SQLException;

    /**
     * Returns a list of the user's completed workouts
     *
     * The pagination object will be modified with the total number of log
     * entries in the database.
     *
     * @param customerId the customer id
     * @param pag a pagination object that defines the pagination of the log
     * @return a list of workouts
     * @throws SQLException
     */
    public List<DataItem> getWorkoutLogForCustomer(int customerId, Pagination pag) throws SQLException;

    /**
     * Returns a specific workout.
     *
     * @param workoutId The workout ID.
     * @return a specific workout.
     * @throws SQLException
     */
    public DataItem getWorkout(int workoutId) throws SQLException;

    /**
     * Stores a new workout in the data source
     *
     * @param data the data that represents the workout
     * @return the data item representing the workout with added primary key
     * @throws SQLException
     */
    public DataItem storeNewWorkout(DataItem data) throws SQLException;

    /**
     * Stores new sets for a workout
     *
     * @param sets a data representing the sets
     * @throws SQLException
     */
    public void storeNewWorkoutSets(List<DataItem> sets) throws SQLException;

    /**
     * Store a new exercise to the data source
     *
     * @param data the data that represents the exercise
     * @throws SQLException
     */
    public void storeNewExercise(DataItem data) throws SQLException;

    /**
     * Returns the exercise with the given id
     *
     * @param exerciseId the exercise id
     * @return the exercise data
     * @throws SQLException
     */
    public DataItem getExerciseById(int exerciseId) throws SQLException;

    /**
     * Returns the progress of the completed weight and resp for a given
     * customer on a given exercise
     *
     * @param customerId the customer id
     * @param exerciseId the exercise id
     * @return a list of the progress data
     * @throws SQLException
     */
    public List<DataItem> getProgressForExercise(int customerId, int exerciseId) throws SQLException;

    /**
     * Returns all exercises
     *
     * @return a list of exercises
     * @throws SQLException
     */
    public List<DataItem> getAllExercises() throws SQLException;

    /**
     * Returns a training program by id
     *
     * @param id the id of the program
     * @return data representing the program
     * @throws SQLException
     */
    public DataItem getProgramById(int id) throws SQLException;

    /**
     * Store a new training program to the data source
     *
     * @param data the data that represents the program
     * @return returns the new id of the program
     * @throws SQLException
     */
    public int storeNewProgram(DataItem data) throws SQLException;

    /**
     * Updates a training program with new data
     *
     * @param data the data that represents the program
     * @throws SQLException
     */
    public void updateProgram(DataItem data) throws SQLException;

    /**
     * Wraps the transaction runner in a database transaction.
     *
     * If everything goes well the runner must commit the transaction before it
     * returns.
     *
     * The transaction will automatically rolled back after the runner returns.
     *
     * @param runner the transaction runner
     * @throws SQLException
     */
    public void runTransaction(TransactionRunner runner) throws SQLException;

    /**
     * Returns all sets in a specific workout session.
     */
    public List<DataItem> getSets(int set_workout_id) throws SQLException;

    /**
     * Stores a completed set in the database.
     *
     * @param set The completed workout set.
     */
    public void storeSetDone(DataItem set) throws SQLException;

    /**
     * Sets the completed flag of an exercise to true.
     *
     * @param workoutID The unique ID of an exercise.
     * @param userComment Comment typed in by the user.
     */
    public void storeExerciseDone(int workoutID);

    public void storeExerciseDone(int workoutID, String userComment) throws SQLException;

    /**
     * Updates the weight of a customer
     *
     * @param customerId The unique ID of a customer
     * @param newWeight the new weight the customer has entered
     * @throws java.sql.SQLException
     */
    public void changeCustomerWeight(int customerId, int newWeight) throws SQLException;

    /**
     * Updates the height of a customer
     *
     * @param customerId The unique ID of a customer
     * @param newWeight The new height the customer has entered
     * @throws java.sql.SQLException
     */
    public void changeCustomerHeight(int customerId, int newWeight) throws SQLException;

    /**
     * Updates the name of a customer
     *
     * @param customerId The unique ID of a customer
     * @param newFirstname The new first name the customer has entered
     * @param newLastname The new last name the customer has entered
     * @throws java.sql.SQLException
     */
    public void changeCustomerName(int customerId, String newFirstname, String newLastname) throws SQLException;

    /**
     * Updates the sex of a customer
     *
     * @param customerId The unique ID of a customer
     * @param sex The new sex the customer has entered
     * @throws java.sql.SQLException
     */
    public void changeCustomerSex(int customerId, String sex) throws SQLException;

    /**
     * Update the password of a customer
     *
     * @param customerId The unique ID of a customer
     * @param newPassword The new password the customer wants
     * @throws java.sql.SQLException
     */
    public void changeCustomerPassword(int customerId, String newPassword) throws SQLException;

    /**
     * Returns the customers encrypted password
     *
     * @param customerId The unique ID of a customer
     * @return password The users password in a encrypted format
     * @throws java.sql.SQLException
     */
    public DataItem getCustomerPassword(int customerId) throws SQLException;
}
