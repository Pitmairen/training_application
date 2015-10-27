package no.hials.trainingapp;

import no.hials.trainingapp.auth.AdminFilter;
import no.hials.trainingapp.auth.AuthenticationFilter;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.DataSourceSqlite;
import no.hials.trainingapp.routes.Workout;
import no.hials.trainingapp.routes.Login;
import no.hials.trainingapp.routes.Logout;
import no.hials.trainingapp.routes.Workouts;
import no.hials.trainingapp.routes.SiteIndex;
import no.hials.trainingapp.routes.admin.AddNewCustomer;
import no.hials.trainingapp.routes.admin.AdminIndex;
import no.hials.trainingapp.routes.History;
import no.hials.trainingapp.routes.admin.AddExercise;
import no.hials.trainingapp.routes.admin.AddWorkoutToProgram;
import no.hials.trainingapp.routes.admin.AdminLogout;
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

        String ds = System.getenv("DATA_SOURCE");
        String conString = System.getenv("DATA_SOURCE_CS");
        
        System.out.println(ds);
        
        if(ds == null || ds.equals("sqlite")){
           
            if(conString == null){
                conString = "jdbc:sqlite:/tmp/trainingdbjava.db";
            }
            
            DataSourceSqlite.initPool(conString);
            sDataSource = new DataSourceSqlite();
        }else if(ds.equals("mssql")){
            
            
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
        r.get("/workout/:id/:id2", Workout.class);

        r.get("/tos", new SimpleTemplateRoute("tos"));
        r.get("/help", new SimpleTemplateRoute("help"));
        r.get("/about", new SimpleTemplateRoute("about"));

        
        // Admin 
        
        
        Spark.before("/admin/*", new AdminFilter());
  
        r.get("/admin/", AdminIndex.class);
        r.getAndPost("/admin/logout", AdminLogout.class);
        r.getAndPost("/admin/add-new-customer", AddNewCustomer.class);
        r.getAndPost("/admin/add-new-exercise", AddExercise.class);
        r.getAndPost("/admin/add-new-workout/:prog_id", AddWorkoutToProgram.class);

    }
}
