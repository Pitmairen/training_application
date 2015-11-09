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

    @Override
    public DataItem getCustomerById(int id) throws SQLException {
        return querySingle(
                "select * from customer WHERE customer_id=?", id);
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

    @Override
    public List<DataItem> getWorkoutLogForCustomer(int customerId, Pagination pag) throws SQLException {

        DataItem count = querySingle("SELECT COUNT(1) AS count FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=?", customerId, true);

        pag.setItemCount(count.getInteger("count"));

        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "ORDER BY w.workout_id DESC "
                + "LIMIT ? OFFSET ?",
                customerId, true, pag.getLimit(), pag.getOffset());
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
                + "(set_exercise_id, set_workout_id,"
                + "set_reps_planned, set_weight_planned) "
                + "VALUES(?, ?, ?, ?)";

        for (DataItem set : sets) {
            executeUpdate(query,
                    set.get("set_exercise_id"),
                    set.get("set_workout_id"),
                    set.get("set_reps_planned"),
                    set.get("set_weight_planned"));
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
                + "AND NOT (set_reps_done IS NULL || set_weight_done IS NULL) "
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

    @Override
    public void updateProgram(DataItem data) throws SQLException{
        String query = "UPDATE program SET program_name=?, program_description=? "
                + " WHERE program_id=?";

        executeUpdate(query,
                data.get("program_name"),
                data.get("program_description"),
                data.get("program_id"));
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
    public void storeSetDone(DataItem set) throws SQLException {

        String query = "UPDATE exercise_set "
                + "SET set_reps_done=?, set_weight_done=? "
                + "WHERE set_id=?";

        executeUpdate(query, set.get("set_reps_done"), set.get("set_weight_done"), set.get("set_id"));
    }

    /**
     * XXX
     */
    @Override
    public void storeExerciseDone(int workoutID, String userComment) throws SQLException {

        String query = "UPDATE workout "
                + "SET workout_done=?, workout_comment=? "
                + "WHERE workout_id=?";

        executeUpdate(query, 1, userComment, workoutID);
    }
}
