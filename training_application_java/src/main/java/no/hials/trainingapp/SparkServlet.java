package no.hials.trainingapp;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.servlet.SparkApplication;

/**
 * Adds support for auto reload and persistent sessions
 */
public class SparkServlet implements SparkApplication {

    @Override
    public void init() {
        try {
            Main.main(null);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SparkServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SparkServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
