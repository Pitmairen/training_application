
package no.hials.trainingapp.datasource;

import java.sql.SQLException;

/**
 *
 * @author tor-martin
 */


public class DataSourceMssqlTest {
    
    public static void main(String args[]) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
        DataSourceMssql dataSourceMssql = new DataSourceMssql();
        System.out.println(dataSourceMssql.getWorkouts());
        System.out.println(dataSourceMssql.getCustomers());
        System.out.println(dataSourceMssql.getCustomerByUsername("duke@gmail.com"));
    }
}
