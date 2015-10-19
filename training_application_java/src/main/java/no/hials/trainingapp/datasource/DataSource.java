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
}
