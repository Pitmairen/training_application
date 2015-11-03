package no.hials.trainingapp.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Data source for the sqlite database
 */
public class DataSourceSqlite extends BaseDataSource {

    /**
     * Initialize the connection pool
     *
     * @param connectionString the connection string
     *
     * @throws ClassNotFoundException
     */
    public static void initPool(String connectionString) throws ClassNotFoundException {

        setConnectionPool(createConnectionPool(connectionString));

    }

    /**
     * Creates a new sqlite data source. This can safely be shared between
     * threads.
     */
    public DataSourceSqlite() {
        super(null);
    }

    @Override
    public DataItem getCustomerByUsername(String username) throws SQLException {

        return querySingle(
                "select * from customer WHERE customer_email=?", username);
    }

    /**
     * Returns all the customers
     *
     * @param limit limit the number of users returned
     * @return a list of customers
     * @throws SQLException
     */
    @Override
    public List<DataItem> getAllCustomers(int limit) throws SQLException {

        return queryList(
                "select * from customer ORDER BY customer_first_name,"
                + " customer_last_name LIMIT ?", limit);
    }

    @Override
    public void storeNewCustomer(DataItem data) throws SQLException {

        String query = "INSERT INTO customer "
                + "(customer_first_name, customer_last_name,"
                + "customer_email, customer_pw, customer_sex,"
                + "customer_program_id, customer_weight,"
                + "customer_height, customer_date_of_birth) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, date('now'))";

        executeUpdate(query,
                data.get("customer_first_name"),
                data.get("customer_last_name"),
                data.get("customer_email"),
                data.get("customer_pw"),
                data.get("customer_sex"),
                data.get("customer_program_id"),
                60, // height and weight and date of birth hard coded for now
                180);

    }

    @Override
    public List<DataItem> getNextWorkoutsForCustomer(int customerId, int limit) throws SQLException {

        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "LIMIT ?",
                customerId, false, limit);
    }

    @Override
    public List<DataItem> getWorkoutLogForCustomer(int customerId, int limit) throws SQLException {
        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "ORDER BY w.workout_id DESC "
                + "LIMIT ?",
                customerId, true, limit);
    }

    /**
     * XXX
     */
    @Override
    public DataItem getWorkout(int workoutId) throws SQLException {
        return querySingle(
                "SELECT * FROM workout "
                + "WHERE workout_id=?",
                workoutId);
    }

    @Override
    public DataItem storeNewWorkout(DataItem data) throws SQLException {

        String query = "INSERT INTO workout "
                + "(workout_name, workout_description,"
                + "workout_date, workout_program_id) "
                + "VALUES(?, ?, ?, ?)";

        int id = executeInsert(query,
                data.get("workout_name"),
                data.get("workout_description"),
                data.get("workout_date"),
                data.get("workout_program_id"));

        data.put("workout_id", id);
        return data;
    }

    @Override
    public void storeNewWorkoutSets(List<DataItem> sets) throws SQLException {

        String query = "INSERT INTO exercise_set "
                + "(set_nr, set_exercise_id, set_workout_id,"
                + "set_reps_planned, set_weight_planned, "
                + "set_duration_planned)"
                + "VALUES(?, ?, ?, ?, ?, ?)";

        for (DataItem set : sets) {
            executeUpdate(query,
                    set.get("set_nr"),
                    set.get("set_exercise_id"),
                    set.get("set_workout_id"),
                    set.get("set_reps_planned"),
                    set.get("set_weight_planned"),
                    set.get("set_duration_planned"));
        }

    }

    @Override
    public DataItem getExerciseById(int exerciseId) throws SQLException {
        return querySingle("SELECT * FROM exercise "
                + "WHERE exercise_id=?",
                exerciseId);
    }

    @Override
    public void storeNewExercise(DataItem data) throws SQLException {

        String query = "INSERT INTO exercise "
                + "(exercise_name, exercise_description)"
                + "VALUES(?, ?)";

        executeUpdate(query,
                data.get("exercise_name"),
                data.get("exercise_description"));

    }

    @Override
    public List<DataItem> getProgressForExercise(int customerId, int exerciseId) throws SQLException {

        return queryList("SELECT workout_date, "
                + "MAX(set_reps_done) AS max_reps, "
                + "MAX(set_weight_done) AS max_weight "
                + "FROM exercise_set "
                + "INNER JOIN workout ON set_workout_id=workout_id "
                + "INNER JOIN customer ON customer_program_id=workout_program_id "
                + "WHERE set_exercise_id=? AND workout_done=? AND customer_id=? "
                + "GROUP BY workout_id "
                + "ORDER BY workout_id ",
                exerciseId, true, customerId);

    }

    @Override
    public List<DataItem> getAllExercises() throws SQLException {
        return queryList("SELECT * FROM exercise ORDER BY exercise_name ASC");
    }

    @Override
    public DataItem getProgramById(int id) throws SQLException {
        return querySingle(
                "SELECT * FROM program WHERE program_id=?", id);
    }

    @Override
    public int storeNewProgram(DataItem data) throws SQLException {

        String query = "INSERT INTO program "
                + "(program_name, program_description)"
                + "VALUES(?, ?)";

        return executeInsert(query,
                data.get("program_name"),
                data.get("program_description"));

    }

    /**
     * XXX
     */
    @Override
    public List<DataItem> getSets(int set_workout_id) throws SQLException {
        return queryList(
                "SELECT * FROM exercise, exercise_set "
                + "WHERE set_workout_id=? AND set_exercise_id=exercise_id"
                        + " ORDER BY set_id ASC", set_workout_id);
    }

    /**
     * Creates a new data source for use in a transaction
     *
     * This is used internally to create new instance of the data source to be
     * used inside a transaction. Objects created using this constructor must
     * not be shared between threads.
     */
    protected DataSourceSqlite(BaseTransaction trans) {
        super(trans);
    }

    @Override
    protected DataSource createTransactionDataSource(BaseTransaction tr) {
        return new DataSourceSqlite(tr);
    }

    /**
     * Create the connection pool
     */
    private static HikariDataSource createConnectionPool(String connectionString) throws ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");
        
        HikariConfig config = new HikariConfig();
       
        config.setJdbcUrl(connectionString);
        config.addDataSourceProperty("foreign_keys", "true");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

    /**
     * XXX
     */
    @Override
    public void setDone(int setID, int repsDone, int loadUsed) {

    }

    /**
     * XXX
     */
    @Override
    public void exerciseDone(int workoutID) {

    }
}
