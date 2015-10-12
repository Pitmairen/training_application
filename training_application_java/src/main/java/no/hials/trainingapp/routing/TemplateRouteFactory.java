package no.hials.trainingapp.routing;

import no.hials.trainingapp.datasource.DataSource;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * The template route factory creates a new route handler object for each
 * request.
 *
 * The main reason of this class is to let each request to a route be handled in
 * a new instance of the route class. This makes the code simpler since each
 * request gets its own instance of the handler and we don't have to worry about
 * multiple threads using the same object.
 *
 * This class does not implement any logic it simply acts as a shim to make the
 * TemplateRute instances compatible with the Spark routing system.
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class TemplateRouteFactory implements TemplateViewRoute
{

    private final Class<? extends TemplateRoute> mClass;
    private final DataSource mDataSource;

    /**
     * Creates a new factory object
     *
     * @param dataSource   the data source object
     * @param routeHandler a route handler class that extends TemplateRoute
     */
    public TemplateRouteFactory(DataSource dataSource,
                                Class<? extends TemplateRoute> routeHandler)
    {
        mClass = routeHandler;
        mDataSource = dataSource;
    }

    /**
     * Handles a request
     *
     * @param req  the current request
     * @param resp the current response
     *
     * @return a ModelAndView object for the spark template system
     *
     * @throws Exception
     */
    @Override
    public ModelAndView handle(Request req, Response resp) throws Exception
    {
        // Construct a new object of the TemplateRoute class 
        TemplateRoute route = mClass.getConstructor(DataSource.class, Request.class,
                                                    Response.class).newInstance(mDataSource, req, resp);

        return route.handle();
    }

}
