package no.hials.trainingapp.datasource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author tor-martin
 */
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class DataSourceMssql implements DataSource {

    private HikariDataSource sPool;
    private Connection con;

    // Used when a data source is used in a transaction
    private final MssqlTransaction mTransaction;

    private final String url = "jdbc:sqlserver://";
    private final String serverName = "tmh-touchpc\\tmserver";
    private final int portNumber = 1433;
    private final String databaseName = "training_application";
    private final String selectMethod = "cursor";
    private final Boolean integratedSecurity = true;

    public String getConnectionUrl() {
        return url + this.serverName + ":" + this.portNumber + ";databaseName=" + this.databaseName + ";integratedSecurity=" + this.integratedSecurity + ";selectMethod=" + this.selectMethod + ";";
    }

    public DataSourceMssql() throws ClassNotFoundException {
        sPool = createConnectionPool(getConnectionUrl());

        mTransaction = null;

    }

    private DataSourceMssql(MssqlTransaction trans) {
        mTransaction = trans;
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

    private List<DataItem> createResultMap(ResultSet rs) throws SQLException {

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

//    private ResultSet executeQuery(Connection con, String query) throws SQLException {
//        Statement statement = con.createStatement();
//        
//        return statement.executeQuery(query);
//    }
    private ResultSet executeQuery(Connection con, String query, Object... params)
            throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);

        bindParams(statement, params);

        return statement.executeQuery();
    }

    private void bindParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

    }

    private HikariDataSource createConnectionPool(String connectionString) throws ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionString);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;

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

    /**
     * Begins a new database transaction. If something unexpected happens when
     * creating the connection it will make sure the connection is returned to
     * the pool.
     */
    private MssqlTransaction beginTransaction() throws SQLException {
        Connection con = sPool.getConnection();
        try {
            return new MssqlTransaction(con);
        } catch (Exception e) {
            // If something happens make sure the connection is closed.
            con.close();
            throw e;
        }
    }

    @Override
    public void runTransaction(TransactionRunner runner) throws SQLException {
        if (mTransaction != null) {
            throw new RuntimeException("Nested transactions not suppored.");
        }

        try (MssqlTransaction tx = beginTransaction()) {
            runner.runTransaction(tx, new DataSourceMssql(tx));
        }
    }

    /**
     * Mssql transaction
     */
    private static class MssqlTransaction
            implements Transaction, AutoCloseable {

        private final Connection mCon;

        public MssqlTransaction(Connection con) throws SQLException {
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
