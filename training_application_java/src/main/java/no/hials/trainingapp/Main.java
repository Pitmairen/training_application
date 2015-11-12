package no.hials.trainingapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.hials.trainingapp.auth.AdminFilter;
import no.hials.trainingapp.auth.AuthenticationFilter;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.DataSourceMssql;
import no.hials.trainingapp.datasource.DataSourceSqlite;
import no.hials.trainingapp.routes.Workout;
import no.hials.trainingapp.routes.WorkoutLog;
import no.hials.trainingapp.routes.Login;
import no.hials.trainingapp.routes.Logout;
import no.hials.trainingapp.routes.Workouts;
import no.hials.trainingapp.routes.SiteIndex;
import no.hials.trainingapp.routes.History;
import no.hials.trainingapp.routes.ProgressGraph;
import no.hials.trainingapp.routes.admin.AddExercise;
import no.hials.trainingapp.routes.admin.AddNewCustomer;
import no.hials.trainingapp.routes.admin.AddWorkoutToProgram;
import no.hials.trainingapp.routes.admin.AdminIndex;
import no.hials.trainingapp.routes.admin.AdminListCustomers;
import no.hials.trainingapp.routes.admin.AdminLogout;
import no.hials.trainingapp.routes.AccountInfo;
import no.hials.trainingapp.routes.admin.EditProgram;
import no.hials.trainingapp.routing.Router;
import no.hials.trainingapp.routing.SimpleTemplateRoute;
import no.hials.trainingapp.routing.TemplateEngines;
import spark.Spark;

/**
 * The main entry point of the application.
 *
 * This will setup the data source and the routing which will make Spark start
 * the web server.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Main {

    private static DataSource sDataSource;
    private static Router sRouter;

    /**
     * xxx
     */
    public static void main(String[] args) throws ClassNotFoundException {

        try {
            loadConfig(args);
        } catch (IOException ex) {
            System.out.println("Failed to load config file");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        if (Config.getValue("DATA_SOURCE").equals("sqlite")) {
            
            String dbFile = Config.getValue("DATA_SOURCE_FILE");
            if (dbFile == null) {
                System.out.println("Missing DATA_SOURCE_FILE enty in config file");
                return;
            }
            DataSourceSqlite.initPool("jdbc:sqlite:" + dbFile);
            sDataSource = new DataSourceSqlite();
            
        } else if (Config.getValue("DATA_SOURCE").equals("mssql")) {
            
            System.setProperty("java.net.preferIPv6Addresses", "true");
            DataSourceMssql.initPool(Config.getValue("DATA_SOURCE_CONNECTION_STRING"));
            sDataSource = new DataSourceMssql();
        }

        sRouter = new Router(sDataSource, TemplateEngines.createPebbleEngine());

        addRoutes(sRouter);
    }

    /**
     * xxx
     */
    private static void addRoutes(Router r) {

        r.serveStatic("/public");
        
        // Require users to be logged in
        Spark.before(new AuthenticationFilter());

        r.get("/", SiteIndex.class);
        r.getAndPost("/login", Login.class);
        r.getAndPost("/logout", Logout.class);
        r.get("/workouts", Workouts.class);
        r.get("/history", History.class);
        r.get("/workoutLog/:id", WorkoutLog.class);
        r.getAndPost("/workout/:id", Workout.class);
        r.get("/progress/:id", ProgressGraph.class);

        r.get("/tos", new SimpleTemplateRoute("tos"));
        r.get("/help", new SimpleTemplateRoute("help"));
        r.get("/about", new SimpleTemplateRoute("about"));

        //Customer account info
        r.getAndPost("/account-info", AccountInfo.class);

        // Admin 
        Spark.before("/admin/*", new AdminFilter());

        r.get("/admin/", AdminIndex.class);
        r.getAndPost("/admin/logout", AdminLogout.class);
        r.getAndPost("/admin/list-customers", AdminListCustomers.class);
        r.getAndPost("/admin/add-new-customer", AddNewCustomer.class);

        r.getAndPost("/admin/add-new-exercise", AddExercise.class);
        r.getAndPost("/admin/add-new-workout/:prog_id", AddWorkoutToProgram.class);
        r.getAndPost("/admin/edit-program/:prog_id", EditProgram.class);

    }
    
    
    private static void loadConfig(String[] args) throws IOException{
        if(args.length > 0){
            Config.loadConfigFromFile(args[0]);
        }else{
            Config.loadConfigFromResource("/defaultConfig.cfg");
        }
    }
    
}
