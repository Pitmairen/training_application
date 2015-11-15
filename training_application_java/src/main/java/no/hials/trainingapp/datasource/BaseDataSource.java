package no.hials.trainingapp.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the data source
 */
public abstract class BaseDataSource implements DataSource {

    // Connection pool
    private static HikariDataSource sPool;
    // Used when a data source is used in a transaction
    private final BaseTransaction mTransaction;

    /**
     * Creates a new data source.
     *
     * If the transaction is null the the connection pool will be used to get a
     * connection. If not the connection associated with the transaction is
     * used.
     */
    protected BaseDataSource(BaseTransaction trans) {
        mTransaction = trans;
    }

    /**
     * Returns a new data source object that can be used in a transaction
     *
     * @param tr the transaction object
     * @return a data source
     */
    protected abstract DataSource createTransactionDataSource(BaseTransaction tr);

    /**
     * Returns the customer with the given username (email)
     *
     * @param username the username
     * @return the customer
     * @throws SQLException
     */
    @Override
    public DataItem getCustomerByUsername(String username) throws SQLException {

        return querySingle("SELECT * FROM customer WHERE customer_email=?", username);
    }

    /**
     * Returns the customer with the given id
     *
     * @param id the id
     * @return the customer
     * @throws SQLException
     */
    @Override
    public DataItem getCustomerById(int id) throws SQLException {
        return querySingle("SELECT * FROM customer WHERE customer_id=?", id);
    }

    /**
     * Returns the workout with the given id
     *
     * @param workoutId the workout id
     * @return the workout
     * @throws SQLException
     */
    @Override
    public DataItem getWorkout(int workoutId) throws SQLException {
        return querySingle("SELECT * FROM workout WHERE workout_id=?", workoutId);
    }

    /**
     * Stores a new workout to the database
     *
     * @param data the workout data
     * @return the stored workout with added primary key (id) value
     * @throws SQLException
     */
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

    /**
     * Stores exercise sets for a workout
     *
     * @param sets the exercise sets
     * @throws SQLException
     */
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

    /**
     * Returns the exercise with the given id
     *
     * @param exerciseId
     * @return
     * @throws SQLException
     */
    @Override
    public DataItem getExerciseById(int exerciseId) throws SQLException {
        return querySingle("SELECT * FROM exercise "
                + "WHERE exercise_id=?",
                exerciseId);
    }

    /**
     * Stores a new exercise to the database
     *
     * @param data the exercise data
     * @throws SQLException
     */
    @Override
    public void storeNewExercise(DataItem data) throws SQLException {

        String query = "INSERT INTO exercise "
                + "(exercise_name, exercise_description)"
                + "VALUES(?, ?)";

        executeUpdate(query,
                data.get("exercise_name"),
                data.get("exercise_description"));

    }

    /**
     * Returns the customer's progress for the given exercise
     *
     * @param customerId the customer id
     * @param exerciseId the exercise id
     * @return
     * @throws SQLException
     */
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

    /**
     * Returns all the exercises in the database
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<DataItem> getAllExercises() throws SQLException {
        return queryList("SELECT * FROM exercise ORDER BY exercise_name ASC");
    }

    /**
     * Returns the training program with the given id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public DataItem getProgramById(int id) throws SQLException {
        return querySingle(
                "SELECT * FROM program WHERE program_id=?", id);
    }

    /**
     * Stores a new training program to the database
     *
     * @param data the training program data
     * @return
     * @throws SQLException
     */
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
     * Updates the training program with new the new values
     *
     * @param data the program data with updated values
     * @throws SQLException
     */
    @Override
    public void updateProgram(DataItem data) throws SQLException {
        String query = "UPDATE program SET program_name=?, program_description=? "
                + " WHERE program_id=?";

        executeUpdate(query,
                data.get("program_name"),
                data.get("program_description"),
                data.get("program_id"));
    }

    /**
     * Returns the exercise sets for the workout with the given id
     *
     * @param set_workout_id the workout id
     * @return
     * @throws SQLException
     */
    @Override
    public List<DataItem> getSets(int set_workout_id) throws SQLException {
        return queryList(
                "SELECT * FROM exercise, exercise_set "
                + "WHERE set_workout_id=? AND set_exercise_id=exercise_id"
                + " ORDER BY set_id ASC", set_workout_id);
    }

    /**
     * Updates the exercise set with the values provided by the user for the
     * number of reps and the weight that the user completed the set with.
     *
     * @param set the set with updated values
     * @throws SQLException
     */
    @Override
    public void storeSetDone(DataItem set) throws SQLException {

        String query = "UPDATE exercise_set "
                + "SET set_reps_done=?, set_weight_done=? "
                + "WHERE set_id=?";

        executeUpdate(query, set.get("set_reps_done"), set.get("set_weight_done"), set.get("set_id"));
    }

    /**
     * Marks the exercise as done (completed) and also the customers comment for
     * the given workout.
     *
     * @param workoutID the workout id
     * @param userComment user comment
     * @throws SQLException
     */
    @Override
    public void storeExerciseDone(int workoutID, String userComment) throws SQLException {

        String query = "UPDATE workout "
                + "SET workout_done=?, workout_comment=? "
                + "WHERE workout_id=?";

        executeUpdate(query, 1, userComment, workoutID);
    }

    //Account info queries
    @Override
    public void changeCustomerWeight(int customerId, int newWeight) throws SQLException {
        String query = "UPDATE customer "
                + "SET customer_weight=?"
                + "WHERE customer_id=?";

        executeUpdate(query, newWeight, customerId);
    }

    @Override
    public void changeCustomerHeight(int customerId, int newHeight) throws SQLException {
        String query = "UPDATE customer "
                + "SET customer_height=?"
                + "WHERE customer_id=?";

        executeUpdate(query, newHeight, customerId);
    }

    @Override
    public void changeCustomerFirstName(int customerId, String newFirstname) throws SQLException {

        String query = "UPDATE customer "
                + "SET customer_first_name=?"
                + "WHERE customer_id=?";

        executeUpdate(query, newFirstname, customerId);
    }

    @Override
    public void changeCustomerLastName(int customerId, String newLastname) throws SQLException {

        String query = "UPDATE customer "
                + "SET customer_last_name=?"
                + "WHERE customer_id=?";

        executeUpdate(query, newLastname, customerId);
    }

    @Override
    public void changeCustomerPassword(int customerId, String newPassword) throws SQLException {
        String query = "UPDATE customer "
                + "SET customer_pw=?"
                + "WHERE customer_id=?";

        executeUpdate(query, newPassword, customerId);
    }

    @Override
    public void changeCustomerSex(int customerId, String sex) throws SQLException {
        String query = "UPDATE customer "
                + "SET customer_sex=?"
                + "WHERE customer_id=?";

        executeUpdate(query, sex, customerId);
    }


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
    @Override
    public void runTransaction(TransactionRunner runner) throws SQLException {
        if (mTransaction != null) {
            throw new RuntimeException("Nested transactions not suppored.");
        }

        try (BaseTransaction tx = beginTransaction()) {
            runner.runTransaction(tx, createTransactionDataSource(tx));
        }
    }

    /**
     * Sets the connection pool for the data source;
     *
     * @param pool the connection pool
     */
    protected static void setConnectionPool(HikariDataSource pool) {
        sPool = pool;
    }

    /**
     * Returns a list of data items for the giver query.
     *
     * This method will make sure to use the correct connection if the data
     * source is in a transaction.
     *
     * If not in a transaction the connection will automatically be returned to
     * the pool after this method returns.
     */
    protected List<DataItem> queryList(String query, Object... params) throws SQLException {

        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return queryList(mTransaction.getConnection(), query, params);
        }

        try (Connection con = getConnectionPool().getConnection()) {
            return queryList(con, query, params);
        }
    }

    /**
     * Returns a single data item for the given query. This method should be
     * used when the query always returns a single row.
     *
     * This method will make sure to use the correct connection if the data
     * source is in a transaction.
     *
     * If not in a transaction the connection will automatically be returned to
     * the pool after this method returns.
     */
    protected DataItem querySingle(String query, Object... params) throws SQLException {

        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return querySingle(mTransaction.getConnection(), query, params);
        }

        try (Connection con = getConnectionPool().getConnection()) {
            return querySingle(con, query, params);
        }
    }

    /**
     * Executes the query. This method should be used for queries that does not
     * return a result set. It will make sure to use the correct connection if
     * the data source is in a transaction.
     *
     * If not in a transaction the connection will automatically be returned to
     * the pool after this method returns.
     */
    protected int executeUpdate(String query, Object... params)
            throws SQLException {
        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return executeUpdate(mTransaction.getConnection(), query, params);
        }

        try (Connection con = getConnectionPool().getConnection()) {
            return executeUpdate(con, query, params);
        }
    }

    /**
     * Executes the query. This method should be used for queries that does not
     * return a result set, but need the auto generated id that is created when
     * the row is inserted. It will make sure to use the correct connection if
     * the data source is in a transaction.
     *
     * If not in a transaction the connection will automatically be returned to
     * the pool after this method returns.
     */
    protected int executeInsert(String query, Object... params)
            throws SQLException {
        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return executeInsert(mTransaction.getConnection(), query, params);
        }

        try (Connection con = sPool.getConnection()) {
            return executeInsert(con, query, params);
        }
    }

    /**
     * Returns a list of data items for the giver query using the provided
     * connection
     */
    private List<DataItem> queryList(Connection con, String query, Object... params) throws SQLException {
        ResultSet res = executeQuery(con, query, params);
        return createResultList(res);
    }

    /**
     * Returns a single data item for the given query using the provided
     * connection
     */
    private DataItem querySingle(Connection con, String query, Object... params) throws SQLException {

        ResultSet res = executeQuery(con, query, params);
        List<DataItem> data = createResultList(res);
        return data.size() > 0 ? data.get(0) : null;

    }

    /**
     * Executes the statement using the provided connection and returns the
     * result set.
     */
    private ResultSet executeQuery(Connection con, String query, Object... params)
            throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);

        bindParams(statement, params);

        return statement.executeQuery();
    }

    /**
     * Executes the statement using the provided connection
     */
    private int executeUpdate(Connection con, String query, Object... params)
            throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);

        bindParams(statement, params);

        return statement.executeUpdate();
    }

    /**
     * Executes the statement using the provided connection. Returns the auto
     * generated id
     */
    private int executeInsert(Connection con, String query, Object... params)
            throws SQLException {
        PreparedStatement statement = con.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS);

        bindParams(statement, params);

        statement.executeUpdate();
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;

    }

    /**
     * Binds the parameters to the prepared statement. The position of the
     * parameters must match with the question marks in the query string.
     */
    private void bindParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    /**
     * Begins a new database transaction. If something unexpected happens when
     * creating the connection it will make sure the connection is returned to
     * the pool.
     */
    private BaseTransaction beginTransaction() throws SQLException {
        Connection con = getConnectionPool().getConnection();
        try {
            return new BaseTransaction(con);
        } catch (Exception e) {
            // If something happens make sure the connection is closed.
            con.close();
            throw e;
        }
    }

    /**
     * Creates a list of data items from the provided result set.
     */
    private List<DataItem> createResultList(ResultSet rs) throws SQLException {

        ArrayList<DataItem> res = new ArrayList<>();

        while (rs.next()) {

            ResultSetMetaData meta = rs.getMetaData();
            DataItem it = new DataItem();
            int columnCount = meta.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                it.put(meta.getColumnLabel(i + 1), rs.getObject(i + 1));
            }
            res.add(it);
        }
        return res;
    }

    private HikariDataSource getConnectionPool() {
        return sPool;
    }

    /**
     * database transaction
     */
    protected static class BaseTransaction
            implements Transaction, AutoCloseable {

        private final Connection mCon;

        public BaseTransaction(Connection con) throws SQLException {
            mCon = con;
            mCon.setAutoCommit(false);
        }

        @Override
        public void commit() throws SQLException {
            mCon.commit();
        }

        @Override
        public void rollback() throws SQLException {
            mCon.rollback();
        }

        /**
         * Close the transaction and return the connection back to the
         * connection pool
         *
         * @throws java.sql.SQLException
         */
        @Override
        public void close() throws SQLException {
            try {
                mCon.rollback();
                mCon.setAutoCommit(true);
            } finally {
                mCon.close();
            }
        }

        /**
         * Returns the connection associated with the transaction
         *
         * @return a db connection
         */
        private Connection getConnection() {
            return mCon;
        }
    }
}
