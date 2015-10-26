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
     * Sqlite transaction
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
