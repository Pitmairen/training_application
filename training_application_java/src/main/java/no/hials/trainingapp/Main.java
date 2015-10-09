package no.hials.trainingapp;

import no.hials.trainingapp.auth.AuthenticationFilter;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.DataSourceSqlite;
import no.hials.trainingapp.routes.Login;
import no.hials.trainingapp.routes.Logout;
import no.hials.trainingapp.routes.NextWorkouts;
import no.hials.trainingapp.routes.SiteIndex;
import no.hials.trainingapp.routes.WorkoutLog;
import no.hials.trainingapp.routing.Router;
import no.hials.trainingapp.routing.SimpleTemplateRoute;
import static spark.Spark.before;
import static spark.Spark.staticFileLocation;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Main
{

    private static DataSource sDataSource;

    private static Router sRouter;

    public static void main(String[] args) throws ClassNotFoundException
    {

        sDataSource = new DataSourceSqlite("jdbc:sqlite:/tmp/trainingdbjava.db");
        sRouter = new Router(sDataSource);

        addRoutes(sRouter);

    }

    private static void addRoutes(Router r)
    {

        staticFileLocation("/public");         
                
        // Require users to be logged in
        before(new AuthenticationFilter());

        r.get("/", SiteIndex.class);
        r.getAndPost("/login", Login.class);
        r.getAndPost("/logout", Logout.class);
        r.get("/workout", NextWorkouts.class);
        r.get("/stats", WorkoutLog.class);

        r.get("/tos", new SimpleTemplateRoute("tos"));
        r.get("/help", new SimpleTemplateRoute("help"));
        r.get("/about", new SimpleTemplateRoute("about"));

    }

    
 
    
}
