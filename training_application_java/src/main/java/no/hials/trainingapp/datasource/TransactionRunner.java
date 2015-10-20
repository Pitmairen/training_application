package no.hials.trainingapp.datasource;

import java.sql.SQLException;

/**
 * The transaction runner represents a object that implements the 
 * code that should run in a database transaction.
 * 
 * The runner object can be passed to the data source's runTransaction 
 * method which wraps the transactions runner's runTransaction-method in
 * a transaction.
 * 
 */
public interface TransactionRunner {

    /**
     * Contains the code that should run in a transaction
     * 
     * If everything goes well the transaction runner must call
     * tx.commit() to commit the transaction.
     * 
     * If something unexpected happens the transaction will automatically 
     * be rolled back after this method returns.
     * 
     * Any calls to the data source must be done through the data source object 
     * passed in as the second argument. This object will make sure all the
     * calls happens in the same transaction.
     * 
     * @param tx the current transaction
     * @param ds the current data source
     * @throws java.sql.SQLException
     */
    public void runTransaction(Transaction tx, DataSource ds) throws SQLException;
    
}
