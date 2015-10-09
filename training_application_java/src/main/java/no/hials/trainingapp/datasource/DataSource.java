package no.hials.trainingapp.datasource;

import java.sql.SQLException;
import java.util.List;

/**
 * This is the interface the application uses to access the database
 *
 * @author Per Myren <progrper@gmail.com>
 */
public interface DataSource
{

    public DataItem getCustomerByUsername(String username) throws SQLException;

    public List<DataItem> getNextWorkoutsForCustomer(int customerId, int limit) throws SQLException;

    public List<DataItem> getWorkoutLogForCustomer(int customerId, int limit) throws SQLException;

}
