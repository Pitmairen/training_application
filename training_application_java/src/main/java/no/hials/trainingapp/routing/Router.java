package no.hials.trainingapp.routing;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import no.hials.trainingapp.datasource.DataSource;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.pebble.PebbleTemplateEngine;

/**
 *
 * @author Per Myren <progrper@gmail.com>
 */
public class Router
{

    private final PebbleTemplateEngine mTemplateEngine;
    private final DataSource mDataSource;

    public Router(DataSource datasource)
    {
        mDataSource = datasource;
        mTemplateEngine = createTemplateEngine();
    }

    public void get(String route, Class<? extends TemplateRoute> cls)
    {
        get(route, new TemplateRouteFactory(mDataSource, cls));
    }

    public void get(String path, TemplateViewRoute route)
    {
        Spark.get(path, route, mTemplateEngine);
    }

    public void post(String route, Class<? extends TemplateRoute> cls)
    {
        post(route, new TemplateRouteFactory(mDataSource, cls));
    }

    public void post(String path, TemplateViewRoute route)
    {
        Spark.post(path, route, mTemplateEngine);
    }

    public void getAndPost(String route, Class<? extends TemplateRoute> cls)
    {
        get(route, new TemplateRouteFactory(mDataSource, cls));
        post(route, new TemplateRouteFactory(mDataSource, cls));
    }

    private PebbleTemplateEngine createTemplateEngine()
    {
        Loader loader = new ClasspathLoader();

        loader.setPrefix("templates");
        loader.setSuffix(".html");
        PebbleEngine engine = new PebbleEngine(loader);

        // FIXME: Currently weird errors are happening with cache enabled.
        engine.setTemplateCache(null);

        return new PebbleTemplateEngine(engine);

    }
}
