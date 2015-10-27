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
     * Returns a specific workout.
     *
     * @param workoutId The workout ID.
     * @param workoutProgramId The workout program ID.
     * @return a specific workout.
     * @throws SQLException
     */
    public DataItem getWorkout(int workoutId, int workoutProgramId) throws SQLException;

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
     * Returns all exercises and their corresponding sets in a workout.
     */
    public List<DataItem> getExercises(int set_id, int set_workout_id, int set_exercise_id) throws SQLException;
}
