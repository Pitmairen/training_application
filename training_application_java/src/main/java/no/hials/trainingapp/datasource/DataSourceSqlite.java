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
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class DataSourceSqlite implements DataSource
{

    private final HikariDataSource mPool;

    public DataSourceSqlite(String connectionString) throws ClassNotFoundException
    {
        mPool = createConnectionPool(connectionString);
    }

    @Override
    public DataItem getCustomerByUsername(String username) throws SQLException
    {

       return querySingle(
                "select * from customer WHERE customer_email=?", username);

    }

    @Override
    public List<DataItem> getNextWorkoutsForCustomer(int customerId, int limit) throws SQLException
    {

        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "LIMIT ?",
                customerId, false, limit);

    }

    @Override
    public List<DataItem> getWorkoutLogForCustomer(int customerId, int limit) throws SQLException
    {
        return queryList(
                "SELECT w.* FROM workout AS w "
                + "INNER JOIN customer AS u ON u.customer_program_id=w.workout_program_id "
                + "WHERE u.customer_id=? AND w.workout_done=? "
                + "ORDER BY w.workout_id DESC "
                + "LIMIT ?",
                customerId, true, limit);
    }

    
    private List<DataItem> queryList(String query, Object... params) throws SQLException{
        
        try(Connection con = mPool.getConnection()){
            
            ResultSet res = executeQuery(con, query, params);
            
            return createResultMap(res);
        }
    }
    
    private DataItem querySingle(String query, Object... params) throws SQLException{
        
        try(Connection con = mPool.getConnection()){
            
            ResultSet res = executeQuery(con, query, params);
            
            List<DataItem> data = createResultMap(res);
            
            return data.size() > 0 ? data.get(0) : null;
        }
 
    }    
    private List<DataItem> createResultMap(ResultSet rs) throws SQLException
    {

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

    private ResultSet executeQuery(Connection con, String query) throws SQLException
    {
        Statement statement = con.createStatement();

        return statement.executeQuery(query);
    }

    private ResultSet executeQuery(Connection con, String query, Object... params) 
            throws SQLException
    {
        PreparedStatement statement = con.prepareStatement(query);

        bindParams(statement, params);

        return statement.executeQuery();
    }

    private void bindParams(PreparedStatement statement, Object... params) throws SQLException
    {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }

    }

    private HikariDataSource createConnectionPool(String connectionString) throws ClassNotFoundException
    {

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
