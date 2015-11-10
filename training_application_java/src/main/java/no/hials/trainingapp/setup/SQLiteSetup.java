package no.hials.trainingapp.setup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Scanner;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.DataSourceSqlite;
import no.hials.trainingapp.datasource.Transaction;

/**
 * Setup application for SQLite
 */
public class SQLiteSetup {

    private static final String SQLITE_INITIAL_SETUP = "sql/sqlite/initial_setup.sql";

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("You need to provide the absolute path to the file "
                    + " for the sqlite database as the first and only argument.");
            return;
        }

        File dbFile = new File(args[0]);

        if (dbFile.exists()) {
            System.out.println("The database file already exists. (" + dbFile.getAbsolutePath() + ")");
            return;
        }

        String conString = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        try {
            DataSourceSqlite.initPool(conString);
        } catch (ClassNotFoundException | RuntimeException ex) {
            System.out.println("The sqlite driver is not installed. "
                    + " Please see the system documentation.");

            return;
        }

        DataSourceSqlite ds = new DataSourceSqlite();

        try {
            createDatabaseStructure(ds);
        } catch (FileNotFoundException | SQLException ex) {
            System.out.println("An error occured please, read the system documentation" + ex.toString());
            return;
        }

        System.out.println("The database has been successfully created in the"
                + " file: " + dbFile.getAbsolutePath());
    }

    public static void createDatabaseStructure(DataSourceSqlite dataSource) throws SQLException, FileNotFoundException {

        ClassLoader classLoader = SQLiteSetup.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(SQLITE_INITIAL_SETUP);

        try (Scanner in = createScanner(stream)) {

            dataSource.runTransaction((Transaction tx, DataSource ds) -> {

                DataSourceSqlite sqlite = (DataSourceSqlite) ds;

                while (in.hasNext()) {

                    String statement = in.next();
                    sqlite.execureDDLStatement(statement);

                }
                tx.commit();
            });

        }

    }

    private static Scanner createScanner(InputStream in) throws FileNotFoundException {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|;");
        return s;
    }

}
