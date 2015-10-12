package no.hials.trainingapp.routing;

import no.hials.trainingapp.datasource.DataSource;
import spark.Spark;
import static spark.Spark.staticFileLocation;
import spark.TemplateEngine;
import spark.TemplateViewRoute;

/**
 * The router simplifies adding new routes to Spark's routing system
 *
 * It takes care of wrapping the route handler classes in factory object to make
 * them compatible with Spark's routing system.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Router
{

    private final TemplateEngine mTemplateEngine;
    private final DataSource mDataSource;

    /**
     * Creates a new router
     *
     * @param datasource     the data source object
     * @param templateEngine the template engine to use for template routes
     */
    public Router(DataSource datasource, TemplateEngine templateEngine)
    {
        mDataSource = datasource;
        mTemplateEngine = templateEngine;
    }

    /**
     * Sets the path to serve static files from.
     *
     * This method must be called before any other routing methods.
     *
     * The {@code path} should be a folder in the classpath. Any files or
     * folders inside the {@code path} will be accessible from the server using
     * the relative path to the file/folder inside the {@code path} as the base
     * path in the URL.
     *
     * @param path a path to a folder in the classpath
     */
    public void serveStatic(String path)
    {
        staticFileLocation(path);
    }

    /**
     * Maps GET requests to the specified {@code path} to a route handler
     *
     * @param path         the URL path that maps to the handler
     * @param handlerClass the class that implements the handler logic
     */
    public void get(String path, Class<? extends TemplateRoute> handlerClass)
    {
        get(path, new TemplateRouteFactory(mDataSource, handlerClass));
    }

    /**
     * Maps GET requests to the specified {@code path} to a route handler
     *
     * @param path  the URL path that maps to the handler
     * @param route a route handler object
     */
    public void get(String path, TemplateViewRoute route)
    {
        Spark.get(path, route, mTemplateEngine);
    }

    /**
     * Maps POST requests to the specified {@code path} to a route handler
     *
     * @param path         the URL path that maps to the handler
     * @param handlerClass the class that implements the handler logic
     */
    public void post(String path, Class<? extends TemplateRoute> handlerClass)
    {
        post(path, new TemplateRouteFactory(mDataSource, handlerClass));
    }

    /**
     * Maps POST requests to the specified {@code path} to a route handler
     *
     * @param path  the URL path that maps to the handler
     * @param route a route handler object
     */
    public void post(String path, TemplateViewRoute route)
    {
        Spark.post(path, route, mTemplateEngine);
    }

    /**
     * Maps both GET and POST requests to the specified {@code path} to a route
     * handler
     *
     * @param path         the URL path that maps to the handler
     * @param handlerClass the class that implements the handler logic
     */
    public void getAndPost(String path, Class<? extends TemplateRoute> handlerClass)
    {
        get(path, new TemplateRouteFactory(mDataSource, handlerClass));
        post(path, new TemplateRouteFactory(mDataSource, handlerClass));
    }
}
