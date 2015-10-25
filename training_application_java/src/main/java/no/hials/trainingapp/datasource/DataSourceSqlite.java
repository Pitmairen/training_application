package no.hials.trainingapp.datasource;

import com.zaxxer.hikari.HikariConfig;
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
 * Data source for the sqlite database
 */
public class DataSourceSqlite implements DataSource {

    // Internal connection pool
    private static HikariDataSource sPool;

    // Used when a data source is used in a transaction
    private final SqliteTransaction mTransaction;

    /**
     * Initialize the connection pool
     *
     * @param connectionString the connection string
     *
     * @throws ClassNotFoundException
     */
    public static void initPool(String connectionString) throws ClassNotFoundException {
        sPool = createConnectionPool(connectionString);
    }

    /**
     * Creates a new sqlite data source. This can safely be shared between
     * threads.
     */
    public DataSourceSqlite() {
        mTransaction = null;
    }

    /**
     * Creates a new data source for use in a transaction
     *
     * This is used internally to create new instance of the data source to be
     * used inside a transaction. Objects created using this constructor must
     * not be shared between threads.
     */
    private DataSourceSqlite(SqliteTransaction trans) {
        mTransaction = trans;
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

        for(DataItem set: sets){
            executeUpdate(query,
                set.get("set_nr"),
                set.get("set_exercise_id"),
                set.get("set_workout_id"),
                set.get("set_reps_planned"),
                set.get("set_weight_planned"),
                set.get("set_duration_planned") ); 
        }


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
    public DataItem getProgramById(int id) throws SQLException {
        return querySingle(
                "SELECT * FROM program WHERE program_id=?", id);
    }

    @Override
    public void runTransaction(TransactionRunner runner) throws SQLException {
        if (mTransaction != null) {
            throw new RuntimeException("Nested transactions not suppored.");
        }

        try (SqliteTransaction tx = beginTransaction()) {
            runner.runTransaction(tx, new DataSourceSqlite(tx));
        }
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
    private List<DataItem> queryList(String query, Object... params) throws SQLException {

        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return queryList(mTransaction.getConnection(), query, params);
        }

        try (Connection con = sPool.getConnection()) {
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
    private DataItem querySingle(String query, Object... params) throws SQLException {

        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return querySingle(mTransaction.getConnection(), query, params);
        }

        try (Connection con = sPool.getConnection()) {
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
    private int executeUpdate(String query, Object... params)
            throws SQLException {
        // If we are in a transaction use the transaction connection.
        if (mTransaction != null) {
            return executeUpdate(mTransaction.getConnection(), query, params);
        }

        try (Connection con = sPool.getConnection()) {
            return executeUpdate(con, query, params);
        }
    }
    
    
    /**
     * Executes the query. This method should be used for queries that does not
     * return a result set, but need the auto generated id that is
     * created when the row is inserted.
     * It will make sure to use the correct connection if
     * the data source is in a transaction.
     *
     * If not in a transaction the connection will automatically be returned to
     * the pool after this method returns.
     */
    private int executeInsert(String query, Object... params)
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
     * Executes the statement using the provided connection. Returns
     * the number of affected rows.
     */
    private int executeUpdate(Connection con, String query, Object... params)
            throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);

        bindParams(statement, params);
        return statement.executeUpdate();
    }

    /**
     * Executes the statement using the provided connection. Returns the
     * auto generated id
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
    private SqliteTransaction beginTransaction() throws SQLException {
        Connection con = sPool.getConnection();
        try {
            return new SqliteTransaction(con);
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

    /**
     * Sqlite transaction
     */
    private static class SqliteTransaction
            implements Transaction, AutoCloseable {

        private final Connection mCon;

        public SqliteTransaction(Connection con) throws SQLException {
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
