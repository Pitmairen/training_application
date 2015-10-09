package no.hials.trainingapp.routing;

import no.hials.trainingapp.datasource.DataSource;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class TemplateRouteFactory implements TemplateViewRoute
{

    private final Class<? extends TemplateRoute> mClass;
    private final DataSource mDataSource;

    public TemplateRouteFactory(DataSource ds, Class<? extends TemplateRoute> c)
    {
        mClass = c;
        mDataSource = ds;
    }

    @Override
    public ModelAndView handle(Request req, Response resp) throws Exception
    {
        TemplateRoute r = mClass.getConstructor(DataSource.class, Request.class,
                                                Response.class).newInstance(mDataSource, req, resp);

        return r.handle();
    }

}
