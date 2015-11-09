package no.hials.trainingapp.datasource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
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

//    @Override
//    public DataItem getWorkout(int workoutId, int workoutProgramId) throws SQLException {
//        return querySingle(
//                "SELECT * FROM workout "
//                + "WHERE workout_program_id=? AND workout_id=? ",
//                workoutId, workoutProgramId);
//    }
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
    public List<DataItem> getWorkoutLogForCustomer(int customerId, Pagination pag) throws SQLException {

        DataItem count = querySingle("SELECT COUNT(1) AS count FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=?", customerId, true);

        pag.setItemCount(count.getInteger("count"));

        // FIXME: Don't know if this is working
        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "ORDER BY w.workout_id DESC "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY",
                customerId, true, pag.getOffset(), pag.getLimit());

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

//   /**
//     * XXX
//     */
//    @Override
//    public List<DataItem> getExercises(int set_id, int set_workout_id, int set_exercise_id) throws SQLException {
//        return queryList(
//                "SELECT * FROM exercise_set "
//                + "WHERE set_id=? AND set_workout_id=? AND set_exercise_id=?" //+ "INNER JOIN set_exercise "
//                //+ "ON exercise.exercise_id=exercise_set.set_workout_id "
//                //+ "WHERE set_workout_id=?",
//                , set_id, set_workout_id, set_exercise_id);
//    }
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
                + "set_reps_planned, set_weight_planned)"
                + "VALUES(?, ?, ?, ?, ?, ?)";

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
    public List<DataItem> getAllExercises() throws SQLException {
        return queryList("SELECT * FROM exercise ORDER BY exercise_name ASC");
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
                + "WHERE set_workout_id=? AND set_exercise_id=exercise_id", set_workout_id);
    }

    /**
     * XXX
     */
    @Override
    public void storeSetDone(String setID, String repsDone, String loadUsed) throws SQLException {

        String query = "UPDATE exercise_set "
                + "SET set_reps_done=?, set_weight_done=? "
                + "WHERE set_id=?";

        executeUpdate(query, repsDone, loadUsed, setID);
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
