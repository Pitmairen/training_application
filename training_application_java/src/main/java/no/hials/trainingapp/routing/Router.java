package no.hials.trainingapp.routing;

import no.hials.trainingapp.datasource.DataSource;
import spark.Spark;
import static spark.Spark.staticFileLocation;
import spark.TemplateEngine;
import spark.TemplateViewRoute;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Router {

    private final TemplateEngine mTemplateEngine;
    private final DataSource mDataSource;

    public Router(DataSource datasource, TemplateEngine templateEngine) {
        mDataSource = datasource;
        mTemplateEngine = templateEngine;
    }

    public void serveStatic(String route){
        staticFileLocation(route);
    }
    
    public void get(String route, Class<? extends TemplateRoute> cls) {
        get(route, new TemplateRouteFactory(mDataSource, cls));
    }

    public void get(String path, TemplateViewRoute route) {
        Spark.get(path, route, mTemplateEngine);
    }

    public void post(String route, Class<? extends TemplateRoute> cls) {
        post(route, new TemplateRouteFactory(mDataSource, cls));
    }

    public void post(String path, TemplateViewRoute route) {
        Spark.post(path, route, mTemplateEngine);
    }

    public void getAndPost(String route, Class<? extends TemplateRoute> cls) {
        get(route, new TemplateRouteFactory(mDataSource, cls));
        post(route, new TemplateRouteFactory(mDataSource, cls));
    }
}
