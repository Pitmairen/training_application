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
                1,
                10,
                34);

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
    public DataItem getWorkout(int workoutId, int workoutProgramId) throws SQLException {
        return querySingle(
                "SELECT * FROM workout "
                + "WHERE workout_program_id=? AND workout_id=? ",
                workoutId, workoutProgramId);
    }

    /**
     * XXX
     */
    @Override
    public List<DataItem> getSets(int set_workout_id) throws SQLException {
        return queryList(
                "SELECT * FROM exercise, exercise_set WHERE set_workout_id=? AND set_exercise_id=exercise_id", set_workout_id);
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
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

}
