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
    public List<DataItem> getWorkout(int workoutId, int workoutProgramId) throws SQLException;
    
    
    
    /**
     * Wraps the transaction runner in a database transaction. 
     * 
     * If everything goes well the runner must commit the transaction before
     * it returns. 
     * 
     * The transaction will automatically rolled back after the runner returns.
     * 
     * @param runner the transaction runner
     * @throws SQLException 
     */
    public void runTransaction(TransactionRunner runner) throws SQLException;
    
    
}
