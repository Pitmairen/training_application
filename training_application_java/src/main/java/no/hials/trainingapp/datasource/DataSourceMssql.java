package no.hials.trainingapp.datasource;

//import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
//import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author tor-martin
 */
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class DataSourceMssql extends BaseDataSource {

    public DataSourceMssql() {
        super(null);
//sPool = createConnectionPool(getConnectionUrl());
    }

    private DataSourceMssql(BaseTransaction trans) {
        super(trans);
    }

    public static void initPool(String connectionString) throws ClassNotFoundException {
        setConnectionPool(createConnectionPool(connectionString));
    }

    public List<DataItem> getCustomers() throws SQLException {
        return queryList("Exec GetCustomers");
    }
    
  
    @Override
    public DataItem getWorkout(int workoutId, int workoutProgramId) throws SQLException {
        return querySingle(
                "SELECT * FROM workout "
                + "WHERE workout_program_id=? AND workout_id=? ",
                workoutId, workoutProgramId);
    }

    @Override
    public DataItem getCustomerByUsername(String username) throws SQLException {

        return querySingle(
                "select * from customer WHERE customer_email=?", username);

    }

    @Override
    public List<DataItem> getNextWorkoutsForCustomer(int customerId, int limit) throws SQLException {

        return queryList(
                "SELECT  w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? ",
                customerId, false);

    }

    @Override
    public List<DataItem> getWorkoutLogForCustomer(int customerId, int limit) throws SQLException {
        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "ORDER BY w.workout_id DESC ",
                customerId, true);
    }

    @Override
    public void storeNewCustomer(DataItem data) throws SQLException {

        String query = "INSERT INTO customer "
                + "(customer_first_name, customer_last_name,"
                + "customer_email, customer_pw, customer_sex,"
                + "customer_program_id, customer_weight,"
                + "customer_height, customer_date_of_birth) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";

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

    private static HikariDataSource createConnectionPool(String connectionString) throws ClassNotFoundException {

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionString);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;

    }

    @Override
    protected DataSource createTransactionDataSource(BaseTransaction tr) {
        return new DataSourceMssql(tr);
    }

   /**
     * XXX
     */
    @Override
    public List<DataItem> getExercises(int set_id, int set_workout_id, int set_exercise_id) throws SQLException {
        return queryList(
                "SELECT * FROM exercise_set "
                + "WHERE set_id=? AND set_workout_id=? AND set_exercise_id=?" //+ "INNER JOIN set_exercise "
                //+ "ON exercise.exercise_id=exercise_set.set_workout_id "
                //+ "WHERE set_workout_id=?",
                , set_id, set_workout_id, set_exercise_id);
    }

}
