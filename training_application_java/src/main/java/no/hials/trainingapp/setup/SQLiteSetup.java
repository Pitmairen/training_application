package no.hials.trainingapp.setup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.hials.trainingapp.auth.Security;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.DataSourceSqlite;
import no.hials.trainingapp.datasource.Transaction;

/**
 * Setup application for SQLite
 */
public class SQLiteSetup {

    private static final String SQLITE_INITIAL_SETUP = "sql/sqlite/initial_setup.sql";

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("You need to provide the absolute path to the file "
                    + " for the sqlite database as the first and the name of the config "
                    + "file as the second argument");
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

        HashMap<String, String> props = new HashMap<>();

        props.put("DATA_SOURCE", "sqlite");
        props.put("DATA_SOURCE_FILE", dbFile.getAbsolutePath());
        
        try {
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(System.in));

            String input;
            input = getInputValue(br, "Please type the username for the admin account: ");
            props.put("ADMIN_USERNAME", input);
            input = getInputValue(br, "Please type the password for the admin account: ");
            props.put("ADMIN_PASSWORD", Security.hashPassword(input));

        } catch (IOException ex) {
            Logger.getLogger(SQLiteSetup.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Something went wrong.");
            return;
        }

        String configFileName = args[1];
        try {
            writeConfigFile(configFileName, props);
        } catch (IOException ex) {
            Logger.getLogger(SQLiteSetup.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Something went wrong.");
            return;

        }

        System.out.println("Config file has been written to " + configFileName);

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

    private static String getInputValue(BufferedReader in, String message) throws IOException {
        System.out.println(message);
        System.out.print("> ");

        String input;
        while ((input = in.readLine()) != null) {
            if (input.isEmpty()) {
                System.out.println("Empty value. Try again.");
                continue;
            }
            return input;
        }
        return null;
    }

    private static void writeConfigFile(String configFilename, HashMap<String, String> propValues) throws IOException {

        Properties prop = new Properties();

        try (OutputStream output = new FileOutputStream(configFilename)) {

            for (String key : propValues.keySet()) {
                prop.setProperty(key, propValues.get(key));
            }

            prop.store(output, null);

        }
    }

}
