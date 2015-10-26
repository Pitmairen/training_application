package no.hials.trainingapp.datasource;

import java.sql.SQLException;

/**
 * Represents a database transaction
 */
public interface Transaction
{

    /**
     * Commits the transaction
     *
     * @throws java.sql.SQLException
     */
    public void commit() throws SQLException;

    /**
     * Roll back the transaction
     *
     * @throws java.sql.SQLException
     */
    public void rollback() throws SQLException;

}
